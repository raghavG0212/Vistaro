import React, { useEffect, useState, useRef } from "react";
import { useParams, useNavigate } from "react-router-dom";
import {
	Box,
	Flex,
	Image,
	Text,
	Button,
	Badge,
	Stack,
	HStack,
	VStack,
	Divider,
	Icon,
	useColorModeValue,
	Modal,
	ModalOverlay,
	ModalContent,
	ModalHeader,
	ModalBody,
	ModalFooter,
	ModalCloseButton,
	FormControl,
	FormLabel,
	Input,
	Select,
	IconButton,
	Accordion,
	AccordionItem,
	AccordionButton,
	AccordionPanel,
	AccordionIcon,
	Spinner,
	StackDivider,
} from "@chakra-ui/react";
import { StarIcon } from "@chakra-ui/icons";
import {
	FiPlay,
	FiMapPin,
	FiCalendar,
	FiEdit2,
	FiTrash2,
	FiChevronLeft,
	FiChevronRight,
} from "react-icons/fi";
import { IoPerson } from "react-icons/io5";
import { toast } from "react-toastify";
import Loader from "../components/Loader";
import { getEventById, updateEvent, deleteEvent } from "../apis/eventApi";
import { getMovieByEventId } from "../apis/movieApi";
import { getSportsByEventId } from "../apis/sportApi";
import { getGeneralEventByEventId } from "../apis/generalEventDetailsApi";
import {
	getSlotsByEventId,
	addSlot,
	updateSlot,
} from "../apis/eventSlotApi";
import { getFoodsBySlot, addFood, deleteFood } from "../apis/foodApi";
import axios from "axios";
import { useSelector } from "react-redux";

const VENUE_API = "/api/v1/venue";
const RATING_API = "/api/v1/ratings";

export default function EventDetailsPage() {
	const { eventId } = useParams();
	const navigate = useNavigate();

	const [event, setEvent] = useState(null);
	const [details, setDetails] = useState(null);
	const [loading, setLoading] = useState(true);
	const [error, setError] = useState(null);
	const [eventType, setEventType] = useState("");

	const role = useSelector((state) => state.user?.role);
	const user = useSelector((state) => state.user);
	const selectedCity = useSelector(
		(state) => state.city?.selectedCity?.city || ""
	);
	const isAdmin = role === "ADMIN";
	const isLoggedIn = !!user?.isAuthenticated;

	// edit event modal state
	const [isEditOpen, setEditOpen] = useState(false);
	const [isDeleteOpen, setDeleteOpen] = useState(false);
	const [editForm, setEditForm] = useState({
		eventId: "",
		title: "",
		description: "",
		category: "",
		subCategory: "",
		bannerUrl: "",
		thumbnailUrl: "",
		startTime: "",
		endTime: "",
	});

	// Slot & Food state
	const [slots, setSlots] = useState([]);
	const [slotsLoading, setSlotsLoading] = useState(false);

	const [isAddSlotOpen, setAddSlotOpen] = useState(false);
	const [creatingSlot, setCreatingSlot] = useState(false);
	const [addSlotForm, setAddSlotForm] = useState({
		venueId: "",
		startTime: "",
		endTime: "",
		format: "NA",
		language: "",
		basePrice: "",
		foods: [],
		newFoodName: "",
		newFoodPrice: "",
	});

	const [isEditSlotOpen, setEditSlotOpen] = useState(false);
	const [editingSlotId, setEditingSlotId] = useState(null);
	const [editSlotForm, setEditSlotForm] = useState({
		slotId: null,
		venueId: "",
		startTime: "",
		endTime: "",
		format: "NA",
		language: "",
		basePrice: "",
	});
	const [updatingSlot, setUpdatingSlot] = useState(false);

	const [foodsBySlot, setFoodsBySlot] = useState({});
	const [foodsLoadingBySlot, setFoodsLoadingBySlot] = useState({});
	const [foodInput, setFoodInput] = useState({ name: "", price: "" });

	const [venues, setVenues] = useState([]);

	// Rating state
	const [isRatingOpen, setRatingOpen] = useState(false);
	const [userRatingStars, setUserRatingStars] = useState(null); // 1–5
	const [hoverStars, setHoverStars] = useState(null);
	const [submittingRating, setSubmittingRating] = useState(false);

	// Offers carousel
	const offers = [
		{
			id: 1,
			code: "VIST50",
			title: "Flat 50% OFF",
			description: "Get 50% off up to ₹150 on your first movie booking.",
			discountPercent: 50,
			maxDiscount: 150,
			validFrom: "01 Dec 2025",
			validTill: "31 Dec 2025",
			tag: "Limited Time",
		},
		{
			id: 2,
			code: "BANK10",
			title: "10% Bank Offer",
			description: "Flat 10% off with XYZ Bank Credit Cards.",
			discountPercent: 10,
			maxDiscount: 200,
			validFrom: "05 Dec 2025",
			validTill: "31 Jan 2026",
			tag: "Bank Offer",
		},
		{
			id: 3,
			code: "WEEKEND20",
			title: "Weekend Bonanza",
			description:
				"Extra 20% off on Fri–Sun night shows. Auto-applied at checkout.",
			discountPercent: 20,
			maxDiscount: 100,
			validFrom: "01 Dec 2025",
			validTill: "31 Dec 2025",
			tag: "Weekend Special",
		},
	];
	const [activeOfferIndex, setActiveOfferIndex] = useState(0);

	const showtimesRef = useRef(null);

	// Theme
	const bgColor = useColorModeValue("#050816", "#050816");
	const cardBg = useColorModeValue("whiteAlpha.100", "whiteAlpha.100");
	const borderClr = useColorModeValue("whiteAlpha.300", "whiteAlpha.300");

	const darkSelectProps = {
		bg: "gray.900",
		color: "white",
		borderColor: "gray.600",
		_hover: { borderColor: "teal.300" },
		_focus: { borderColor: "teal.300", boxShadow: "0 0 0 1px #38B2AC" },
	};

	const darkInputProps = {
		bg: "gray.900",
		color: "white",
		borderColor: "gray.600",
		_hover: { borderColor: "teal.300" },
		_focus: { borderColor: "teal.300", boxShadow: "0 0 0 1px #38B2AC" },
	};

	// Load event + details
	useEffect(() => {
		const loadDetails = async () => {
			try {
				setLoading(true);
				setError(null);

				const eventRes = await getEventById(eventId);
				setEvent(eventRes.data);

				let detailsRes = null;
				switch (eventRes.data.category) {
					case "MOVIE":
						detailsRes = await getMovieByEventId(eventId);
						setEventType("MOVIE");
						break;
					case "SPORT":
						detailsRes = await getSportsByEventId(eventId);
						setEventType("SPORT");
						break;
					case "EVENT":
						detailsRes = await getGeneralEventByEventId(eventId);
						setEventType("EVENT");
						break;
					default:
						setEventType("");
				}
				setDetails(detailsRes?.data || null);
			} catch (err) {
				console.error(err);
				setError("Failed to load event details");
			} finally {
				setLoading(false);
			}
		};

		loadDetails();
	}, [eventId]);

	// Load slots
	useEffect(() => {
		if (!eventId) return;
		fetchSlots();
	}, [eventId]);

	// Load venues list (for dropdowns)
	useEffect(() => {
		fetchVenues();
	}, [selectedCity]);

	// Keep edit form synced with event
	useEffect(() => {
		if (!event) return;
		setEditForm({
			eventId: event.eventId,
			title: event.title,
			description: event.description,
			category: event.category,
			subCategory: event.subCategory,
			bannerUrl: event.bannerUrl,
			thumbnailUrl: event.thumbnailUrl,
			startTime: event.startTime ? event.startTime.slice(0, 16) : "",
			endTime: event.endTime ? event.endTime.slice(0, 16) : "",
		});
	}, [event]);

	const fetchVenues = async () => {
		try {
			let res;
			if (selectedCity) {
				res = await axios.get(`${VENUE_API}/search/city`, {
					params: { city: selectedCity },
				});
			} else {
				res = await axios.get(`${VENUE_API}/all`);
			}
			setVenues(res.data || []);
		} catch (err) {
			console.error("Failed to load venues", err);
			setVenues([]);
		}
	};

	const fetchSlots = async () => {
		try {
			setSlotsLoading(true);
			const res = await getSlotsByEventId(eventId);
			const data = Array.isArray(res.data) ? res.data : [];

			// Enrich each slot with venue details (name, address, city, screenName)
			const enriched = await Promise.all(
				data.map(async (s) => {
					if (!s || !s.venueId) return s;
					try {
						const venueRes = await axios.get(`${VENUE_API}/${s.venueId}`);
						const v = venueRes.data;
						return {
							...s,
							venueName: v.name,
							venueCity: v.city,
							screenName: v.screenName,
							venueAddress: v.address,
						};
					} catch (err) {
						console.error("Failed to load venue for slot", s.slotId, err);
						return {
							...s,
							venueName: "-",
							venueCity: "-",
							screenName: "-",
							venueAddress: "-",
						};
					}
				})
			);

			setSlots(enriched);

			// If you still want foods preloaded for edit modal
			enriched.forEach((s) => {
				if (s && s.slotId) fetchFoodsForSlot(s.slotId);
			});
		} catch (err) {
			console.error("Failed to fetch slots", err);
			toast.error("Failed to load slots");
		} finally {
			setSlotsLoading(false);
		}
	};

	const fetchFoodsForSlot = async (slotId) => {
		try {
			setFoodsLoadingBySlot((p) => ({ ...p, [slotId]: true }));
			const res = await getFoodsBySlot(slotId);
			setFoodsBySlot((p) => ({
				...p,
				[slotId]: Array.isArray(res.data) ? res.data : [],
			}));
		} catch (err) {
			console.error("Failed to fetch foods for slot", slotId, err);
			setFoodsBySlot((p) => ({ ...p, [slotId]: [] }));
		} finally {
			setFoodsLoadingBySlot((p) => ({ ...p, [slotId]: false }));
		}
	};

	// ---- Slot actions ----

	const openAddSlotModal = () => {
		setAddSlotForm({
			venueId: "",
			startTime: "",
			endTime: "",
			format: "NA",
			language: "",
			basePrice: "",
			foods: [],
			newFoodName: "",
			newFoodPrice: "",
		});
		setAddSlotOpen(true);
	};

	const handleAddSlotChange = (field, value) =>
		setAddSlotForm((p) => ({ ...p, [field]: value }));

	const handleAddTemporaryFood = () => {
		const { newFoodName, newFoodPrice } = addSlotForm;
		if (!newFoodName || !newFoodPrice) {
			toast.warning("Provide food name and price");
			return;
		}
		setAddSlotForm((p) => ({
			...p,
			foods: [
				...(p.foods || []),
				{ name: p.newFoodName.trim(), price: Number(p.newFoodPrice) },
			],
			newFoodName: "",
			newFoodPrice: "",
		}));
	};

	const handleRemoveTempFood = (index) => {
		setAddSlotForm((p) => ({
			...p,
			foods: p.foods.filter((_, i) => i !== index),
		}));
	};

	const submitAddSlot = async () => {
		try {
			if (
				!addSlotForm.venueId ||
				!addSlotForm.startTime ||
				!addSlotForm.endTime ||
				!addSlotForm.language ||
				!addSlotForm.basePrice
			) {
				toast.error("Please fill required slot fields");
				return;
			}
			setCreatingSlot(true);

			const payload = {
				eventId: Number(eventId),
				venueId: Number(addSlotForm.venueId),
				startTime: addSlotForm.startTime,
				endTime: addSlotForm.endTime,
				format: addSlotForm.format || "NA",
				language: addSlotForm.language,
				basePrice: Number(addSlotForm.basePrice),
			};

			const slotRes = await addSlot(payload);
			const createdSlot = slotRes.data;
			toast.success("Slot created");

			if (
				addSlotForm.foods &&
				addSlotForm.foods.length &&
				createdSlot &&
				createdSlot.slotId
			) {
				for (const f of addSlotForm.foods) {
					try {
						await addFood({
							slotId: createdSlot.slotId,
							name: f.name,
							price: Number(f.price),
						});
					} catch (err) {
						console.error("Failed to add food", f, err);
					}
				}
			}

			setAddSlotOpen(false);
			await fetchSlots();
		} catch (err) {
			console.error(err);
			toast.error("Failed to create slot");
		} finally {
			setCreatingSlot(false);
		}
	};

	const submitEditSlot = async () => {
		try {
			if (
				!editSlotForm.venueId ||
				!editSlotForm.startTime ||
				!editSlotForm.endTime ||
				!editSlotForm.language ||
				!editSlotForm.basePrice
			) {
				toast.error("Please fill required slot fields");
				return;
			}
			setUpdatingSlot(true);

			const payload = {
				venueId: Number(editSlotForm.venueId),
				startTime: editSlotForm.startTime,
				endTime: editSlotForm.endTime,
				format: editSlotForm.format || "NA",
				language: editSlotForm.language,
				basePrice: Number(editSlotForm.basePrice),
			};

			await updateSlot(editSlotForm.slotId, payload);
			toast.success("Slot updated");
			setEditSlotOpen(false);
			await fetchSlots();
		} catch (err) {
			console.error(err);
			toast.error("Failed to update slot");
		} finally {
			setUpdatingSlot(false);
		}
	};

	// Foods actions
	const handleAddFoodToSlot = async (slotId) => {
		if (!foodInput.name || !foodInput.price) {
			toast.warning("Provide food name & price");
			return;
		}
		try {
			await addFood({
				slotId,
				name: foodInput.name.trim(),
				price: Number(foodInput.price),
			});
			setFoodInput({ name: "", price: "" });
			toast.success("Food added");
			await fetchFoodsForSlot(slotId);
		} catch (err) {
			console.error(err);
			toast.error("Failed to add food");
		}
	};

	const handleDeleteFood = async (foodId, slotId) => {
		if (!window.confirm("Delete this food item?")) return;
		try {
			await deleteFood(foodId);
			toast.success("Food deleted");
			await fetchFoodsForSlot(slotId);
		} catch (err) {
			console.error(err);
			toast.error("Failed to delete food");
		}
	};

	// ---- Event edit/delete ----
	const openEditModal = () => setEditOpen(true);

	const handleEditChange = (e) => {
		setEditForm({ ...editForm, [e.target.name]: e.target.value });
	};

	const handleSubmitEdit = async () => {
		try {
			const payload = {
				...editForm,
				startTime: new Date(editForm.startTime).toISOString(),
				endTime: new Date(editForm.endTime).toISOString(),
			};
			await updateEvent(eventId, payload);
			toast.success("Event updated successfully!");
			setEditOpen(false);
			const refresh = await getEventById(eventId);
			setEvent(refresh.data);
		} catch (err) {
			console.error(err);
			toast.error("Failed to update event");
		}
	};

	const handleConfirmDelete = async () => {
		try {
			await deleteEvent(eventId);
			toast.success("Event deleted successfully!");
			setDeleteOpen(false);
			navigate("/");
		} catch (err) {
			console.error(err);
			toast.error("Delete failed");
		}
	};

	const checkUserRating = async () => {
		if (!isLoggedIn) return;

		try {
			const res = await axios.get(`${RATING_API}/check`, {
				params: {
					userId: user.userId,
					eventId: Number(eventId),
				},
			});

			if (res.data.exists) {
				setUserRatingStars(res.data.stars); // pre-fill stars
			}
		} catch (err) {
			console.error("Failed to check rating", err);
		}
	};

	useEffect(() => {
		if (eventId) {
			checkUserRating();
		}
	}, [eventId, isLoggedIn]);

	// ---- Rating ----
	const handleOpenRating = () => {
		if (!isLoggedIn) {
			toast.warning("Login to rate this movie.");
			navigate("/login");
			return;
		}
		setRatingOpen(true);
	};

	const handleSubmitRating = async () => {
		if (!userRatingStars) {
			toast.warning("Please select a rating.");
			return;
		}
		try {
			setSubmittingRating(true);
			await axios.post(`${RATING_API}/add`, {
				userId: user.userId,
				eventId: Number(eventId),
				stars: userRatingStars, // dto expects stars
			});
			toast.success("Thanks for rating!");

			if (eventType === "MOVIE") {
				try {
					const res = await getMovieByEventId(eventId);
					setDetails(res.data);
				} catch (err) {
					console.error("Failed to refresh movie details after rating", err);
				}
			}

			setRatingOpen(false);
		} catch (err) {
			console.error(err);
			const msg =
				err?.response?.data?.message ||
				err?.response?.data ||
				"Failed to submit rating.";
			toast.error(msg);
		} finally {
			setSubmittingRating(false);
		}
	};

	if (loading) return <Loader />;

	if (error || !event) {
		return (
			<Box bg={bgColor} minH="100vh" color="white" p={6}>
				<Text textAlign="center" mt={10}>
					{error || "Event not found."}
				</Text>
			</Box>
		);
	}

	const isMovie = eventType === "MOVIE";
	const isSport = eventType === "SPORT";
	const isGeneral = eventType === "EVENT";

	const castList =
		isMovie && details && Array.isArray(details.castJson)
			? details.castJson
			: [];

	const currentOffer = offers[activeOfferIndex];

	const formattedMovieRating = (() => {
		if (!isMovie || !details || details.rating == null) return null;
		const num = Number(details.rating);
		if (Number.isNaN(num)) return details.rating;
		return `${num.toFixed(1)} / 10`;
	})();

	return (
		<Box bg={bgColor} minH="100vh" color="white">
			{/* TOP BANNER */}
			<Box
				position="relative"
				height={{ base: "220px", md: "320px", lg: "360px" }}
				bg="black"
			>
				<Image
					src={event.bannerUrl}
					objectFit="cover"
					width="100%"
					height="100%"
					opacity={0.4}
					zIndex={5}
				/>
				<Box
					position="absolute"
					inset="0"
					bgGradient="linear(to-t, blackAlpha.900, blackAlpha.400)"
				/>

				{isAdmin && (
					<HStack
						position="absolute"
						top="15px"
						right="15px"
						spacing={3}
						zIndex={20}
					>
						<Button
							size="sm"
							leftIcon={<FiEdit2 />}
							colorScheme="green"
							variant="solid"
						>
							{/* We still use openEditModal */}
							<span onClick={openEditModal}>Edit</span>
						</Button>
						<Button
							size="sm"
							leftIcon={<FiTrash2 />}
							colorScheme="red"
							onClick={() => setDeleteOpen(true)}
							isDisabled={new Date(event.startTime) <= new Date() && new Date()<= new Date(event.endTime)}
						>
							Delete
						</Button>
					</HStack>
				)}

				{/* OVERLAY CONTENT */}
				<Flex
					position="absolute"
					inset="0"
					align="flex-end"
					px={{ base: 4, md: 10 }}
					pb={{ base: 4, md: 8 }}
					gap={6}
					direction={{ base: "column", md: "row" }}
				>
					{/* Thumbnail + Trailer */}
					<Box
						flexShrink={0}
						width={{ base: "120px", md: "180px", lg: "210px" }}
						position="relative"
					>
						<Image
							src={event.thumbnailUrl}
							alt={event.title}
							borderRadius="lg"
							objectFit="cover"
							width="100%"
							height={{ base: "170px", md: "240px" }}
							boxShadow="xl"
						/>
						{isMovie && details?.trailerUrl && (
							<Button
								leftIcon={<FiPlay />}
								size="sm"
								position="absolute"
								bottom="10px"
								left="50%"
								transform="translateX(-50%)"
								colorScheme="red"
								variant="solid"
								onClick={() => window.open(details.trailerUrl, "_blank")}
							>
								Trailer
							</Button>
						)}
					</Box>

					{/* Title + primary info */}
					<Box flex="1">
						<Stack spacing={3}>
							<HStack spacing={3} align="center">
								<Text
									fontSize={{ base: "2xl", md: "3xl" }}
									fontWeight="bold"
									noOfLines={2}
								>
									{event.title}
								</Text>
								{event.subCategory && (
									<Badge colorScheme="purple" borderRadius="md" marginTop="2">
										{event.subCategory}
									</Badge>
								)}
							</HStack>

							{isMovie && details && (
								<HStack spacing={4}>
									<HStack>
										<StarIcon color="yellow.400" />
										<Text fontWeight="semibold">
											{formattedMovieRating || "N/A"}
										</Text>
										{details.totalReviews != null && (
											<Text fontSize="sm" color="gray.300">
												({details.totalReviews} reviews)
											</Text>
										)}
									</HStack>
									<Button
										size="sm"
										variant="solid"
										colorScheme={userRatingStars ? "yellow" : "gray"}
										onClick={handleOpenRating}
										isDisabled={isAdmin}
									>
										{userRatingStars
											? `Rated ★${userRatingStars}`
											: "Rate Now"}
									</Button>
								</HStack>
							)}

							{/* Generic info strip */}
							<HStack spacing={4} flexWrap="wrap">
								{isMovie && details && (
									<>
										{details.language && (
											<Badge
												colorScheme="blue"
												variant="outline"
												mt={1}
												borderColor="blue.300"
											>
												{details.language}
											</Badge>
										)}
										{details.genre && (
											<Badge
												colorScheme="green"
												variant="outline"
												mt={1}
												borderColor="green.300"
											>
												{details.genre}
											</Badge>
										)}
										{details.releaseDate && (
											<Text fontSize="sm" color="gray.300" mt={1}>
												Release:{" "}
												{new Date(details.releaseDate).toLocaleDateString()}
											</Text>
										)}
									</>
								)}

								{isSport && details && (
									<>
										{details.sportType && (
											<Badge
												colorScheme="orange"
												variant="outline"
												mt={1}
												borderColor="orange.300"
											>
												{details.sportType}
											</Badge>
										)}
										{details.matchFormat && (
											<Badge
												colorScheme="purple"
												variant="outline"
												mt={1}
												borderColor="purple.300"
											>
												{details.matchFormat}
											</Badge>
										)}
									</>
								)}

								{isGeneral && details?.genre && (
									<Badge
										colorScheme="pink"
										variant="outline"
										mt={1}
										borderColor="pink.300"
									>
										{details.genre}
									</Badge>
								)}
							</HStack>

							{/* Time & venue for sport/general */}
							<Stack spacing={1} fontSize="sm" color="gray.200">
								{(isSport || isGeneral) && (
									<HStack>
										<Icon as={FiCalendar} />
										<Text>
											{details?.startTime
												? new Date(details.startTime).toLocaleString()
												: "TBD"}{" "}
											-{" "}
											{details?.endTime
												? new Date(details.endTime).toLocaleString()
												: "TBD"}
										</Text>
									</HStack>
								)}
								{isSport && details?.venueInfo && (
									<HStack>
										<Icon as={FiMapPin} />
										<Text>{details.venueInfo}</Text>
									</HStack>
								)}
							</Stack>

							{/* BOOK / SLOT control */}
							<Box
								mt={3}
								p={4}
								borderRadius="lg"
								bg={cardBg}
								border="1px solid"
								borderColor={borderClr}
							>
								{isAdmin ? (
									<Flex
										justify="space-between"
										align={{ base: "flex-start", md: "center" }}
										direction={{ base: "column", md: "row" }}
										gap={3}
									>
										<Box>
											<Text fontWeight="bold" fontSize="lg">
												Create Event Slot
											</Text>
											<Text fontSize="sm" color="gray.300">
												Add slot details and seating for this event.
											</Text>
										</Box>
										<HStack spacing={3}>
											<Button
												variant="subtle"
												size="md"
												color="green"
												borderColor="purple.300"
												onClick={() => {
													if (showtimesRef.current) {
														showtimesRef.current.scrollIntoView({
															behavior: "smooth",
															block: "start",
														});
													}
												}}
											>
												View Showtimes
											</Button>
											<Button
												colorScheme="purple"
												size="md"
												onClick={openAddSlotModal}
											>
												Add Slot
											</Button>
										</HStack>
									</Flex>
								) : (
									<Flex
										justify="space-between"
										align={{ base: "flex-start", md: "center" }}
										direction={{ base: "column", md: "row" }}
										gap={3}
									>
										<Box>
											<Text fontWeight="bold" fontSize="lg">
												Book Tickets
											</Text>
											<Text fontSize="sm" color="gray.300">
												Select showtime and seats on the next step.
											</Text>
										</Box>
										<HStack spacing={3}>
											<Button
												variant="solid"
												size="sm"
												colorScheme="purple"
												borderColor="purple.300"
												onClick={() => {
													if (showtimesRef.current) {
														showtimesRef.current.scrollIntoView({
															behavior: "smooth",
															block: "start",
														});
													}
												}}
											>
												View Showtimes
											</Button>
											{isLoggedIn ? (
												<Button
													colorScheme="purple"
													size="md"
													onClick={() => navigate(`/eventslots/${eventId}`)}
												>
													Book Tickets
												</Button>
											) : (
												<Button
													colorScheme="purple"
													size="md"
													onClick={() => {
														navigate("/login");
														toast.warning("Login to book tickets.");
													}}
												>
													Book Tickets
												</Button>
											)}
										</HStack>
									</Flex>
								)}
							</Box>
						</Stack>
					</Box>
				</Flex>
			</Box>

			{/* MAIN BODY */}
			<Box px={{ base: 4, md: 10 }} py={6}>
				<Flex
					gap={8}
					direction={{ base: "column", lg: "row" }}
					alignItems="flex-start"
				>
					{/* LEFT COLUMN */}
					<Box flex="3" minW={0}>
						{/* Description */}
						<Box
							mb={6}
							p={5}
							borderRadius="lg"
							bg={cardBg}
							border="1px solid"
							borderColor={borderClr}
						>
							<Text fontSize="xl" fontWeight="bold" mb={2}>
								About the Event
							</Text>
							<Text fontSize="sm" color="gray.200">
								{event.description || "No description provided."}
							</Text>
						</Box>

						{/* Showtimes */}
						<Box
							ref={showtimesRef}
							mb={6}
							p={5}
							borderRadius="lg"
							bg={cardBg}
							border="1px solid"
							borderColor={borderClr}
							maxH="380px"
							overflowY="auto"
						>
							<HStack justify="space-between" mb={4}>
								<Text fontSize="xl" fontWeight="bold">
									Showtimes
								</Text>
								<Text fontSize="sm" color="gray.400">
									{slots.length} slots
								</Text>
							</HStack>

							{slotsLoading ? (
								<Box textAlign="center" py={6}>
									<Spinner color="teal.300" />
								</Box>
							) : slots.length === 0 ? (
								<Text color="gray.400">No showtimes scheduled.</Text>
							) : (
								<Accordion allowToggle reduceMotion>
									{slots.map((s) => (
										<AccordionItem key={s.slotId} border="none">
											{({ isExpanded }) => (
												<>
													<HStack align="stretch">
														<AccordionButton
															px={0}
															flex="1"
															_expanded={{ bg: "whiteAlpha.50" }}
														>
															<Box flex="1" textAlign="left" py={3}>
																<HStack
																	justify="space-between"
																	align="center"
																>
																	<Box>
																		<Text fontWeight="semibold">
																			{s.venueName || "-"}
																			{s.venueCity && (
																				<Text
																					as="span"
																					color="gray.400"
																				>{` • ${s.venueCity}`}</Text>
																			)}{" "}
																			{s.screenName && (
																				<Text
																					as="span"
																					color="gray.400"
																				>{` • ${s.screenName}`}</Text>
																			)}{" "}
																			• {s.language} •{" "}
																			{s.format === "NA" ? "" : s.format}
																		</Text>
																		<Text
																			fontSize="sm"
																			color="gray.300"
																		>
																			{s.startTime
																				? new Date(
																					s.startTime
																				).toLocaleString()
																				: "TBD"}{" "}
																			—{" "}
																			{s.endTime
																				? new Date(
																					s.endTime
																				).toLocaleString()
																				: "TBD"}
																		</Text>
																	</Box>
																	<HStack>
																		<Text
																			fontSize="sm"
																			color="gray.200"
																		>
																			₹{s.basePrice}
																		</Text>
																		<AccordionIcon />
																	</HStack>
																</HStack>
															</Box>
														</AccordionButton>
													</HStack>
													<AccordionPanel pb={4} pt={2}>
														<Stack
															spacing={3}
															divider={
																<StackDivider borderColor="whiteAlpha.200" />
															}
														>
															<Box>
																<Text
																	fontSize="sm"
																	color="gray.400"
																>
																	Venue
																</Text>
																<Text fontWeight="semibold">
																	{s.venueName || "-"}
																</Text>
																{s.screenName && (
																	<Text fontSize="sm" color="gray.200">
																		Screen: {s.screenName}
																	</Text>
																)}
																{s.venueAddress && (
																	<Text fontSize="sm" color="gray.200">
																		{s.venueAddress}
																	</Text>
																)}
																{s.venueCity && (
																	<Text fontSize="sm" color="gray.400">
																		{s.venueCity}
																	</Text>
																)}
															</Box>
															<Box>
																<Text
																	fontSize="sm"
																	color="gray.400"
																>
																	Languages / Format
																</Text>
																<Text>
																	{s.language} •{" "}
																	{s.format === "NA"
																		? "Standard"
																		: s.format}
																</Text>
															</Box>
														</Stack>
													</AccordionPanel>
												</>
											)}
										</AccordionItem>
									))}
								</Accordion>
							)}
						</Box>

						{/* Event-specific details */}
						{isMovie && details && (
							<Box
								mb={6}
								p={5}
								borderRadius="lg"
								bg={cardBg}
								border="1px solid"
								borderColor={borderClr}
							>
								<Text fontSize="xl" fontWeight="bold" mb={3}>
									Cast & Crew
								</Text>
								{castList.length > 0 ? (
									<HStack spacing={3} mb={4}>
										{castList.map((c, idx) => (
											<Box
												key={idx}
												textAlign="center"
												
											>
												<IoPerson className="text-[80px] rounded-full bg-slate-500" />
												<Text
													py={3}
													
												>
													{typeof c === "string" || typeof c === "number"
														? c
														: JSON.stringify(c)}
												</Text>
											</Box>
										))}
									</HStack>
								) : (
									<Text fontSize="sm" color="gray.400" mb={3}>
										Cast information not available.
									</Text>
								)}
								{details.director && (
									<Box mt={2}>
										<Text fontWeight="semibold" fontSize="xl">
											Director
										</Text>
										<Text
											fontSize="md"
											color="gray.200"
											py={3}
										>
											{details.director}
										</Text>
									</Box>
								)}
							</Box>
						)}

						{isSport && details && (
							<Box
								mb={6}
								p={5}
								borderRadius="lg"
								bg={cardBg}
								border="1px solid"
								borderColor={borderClr}
							>
								<Text fontSize="xl" fontWeight="bold" mb={3}>
									Match Details
								</Text>
								<HStack spacing={4} mb={3}>
									<Text fontSize="lg" fontWeight="bold">
										{details.team1}
									</Text>
									<Text fontSize="lg" color="gray.300">
										VS
									</Text>
									<Text fontSize="lg" fontWeight="bold">
										{details.team2}
									</Text>
								</HStack>
								{details.matchFormat && (
									<Text fontSize="sm" color="gray.200" mb={1}>
										Format: {details.matchFormat}
									</Text>
								)}
								{details.venueInfo && (
									<Text fontSize="sm" color="gray.200">
										Venue: {details.venueInfo}
									</Text>
								)}
							</Box>
						)}

						{isGeneral && details && (
							<Box
								mb={6}
								p={5}
								borderRadius="lg"
								bg={cardBg}
								border="1px solid"
								borderColor={borderClr}
							>
								<Text fontSize="xl" fontWeight="bold" mb={3}>
									Event Highlights
								</Text>
								<Stack spacing={2}>
									<Text fontSize="sm" color="gray.200">
										<b>Artist:</b> {details.artist}
									</Text>
									{details.host && (
										<Text fontSize="sm" color="gray.200">
											<b>Host:</b> {details.host}
										</Text>
									)}
									{details.additionalInfo && (
										<Text fontSize="sm" color="gray.200">
											<b>More Info:</b> {details.additionalInfo}
										</Text>
									)}
								</Stack>
							</Box>
						)}
					</Box>

					{/* RIGHT COLUMN */}
					<Box flex="1.2" minW={{ base: "100%", lg: "320px" }}>
						{/* OFFERS with carousel */}
						<Box
							mb={6}
							p={4}
							borderRadius="lg"
							bg={cardBg}
							border="1px solid"
							borderColor={borderClr}
						>
							<HStack justify="space-between" mb={2}>
								<Text fontSize="lg" fontWeight="bold">
									Offers & Promotions
								</Text>
								<HStack spacing={1}>
									<IconButton
										size="sm"
										aria-label="Previous offer"
										icon={<FiChevronLeft />}
										variant="outline"
										colorScheme="gray"
										borderColor="gray.500"
										onClick={() =>
											setActiveOfferIndex((idx) =>
												idx === 0 ? offers.length - 1 : idx - 1
											)
										}
									/>
									<IconButton
										size="sm"
										aria-label="Next offer"
										icon={<FiChevronRight />}
										variant="outline"
										colorScheme="gray"
										borderColor="gray.500"
										onClick={() =>
											setActiveOfferIndex((idx) =>
												idx === offers.length - 1 ? 0 : idx + 1
											)
										}
									/>
								</HStack>
							</HStack>
							<Divider mb={3} borderColor="whiteAlpha.300" />
							<VStack align="flex-start" spacing={2}>
								<HStack>
									<Badge
										colorScheme="green"
										variant="outline"
										borderRadius="full"
										borderColor="green.300"
									>
										{currentOffer.tag}
									</Badge>
									<Badge
										colorScheme="purple"
										variant="outline"
										borderRadius="full"
										borderColor="purple.300"
									>
										Code: {currentOffer.code}
									</Badge>
								</HStack>
								<Text fontSize="md" fontWeight="semibold">
									{currentOffer.title}
								</Text>
								<Text fontSize="sm" color="gray.200">
									{currentOffer.description}
								</Text>
								<Text fontSize="xs" color="gray.400">
									{currentOffer.discountPercent}% off • Max discount ₹
									{currentOffer.maxDiscount}
								</Text>
								<Text fontSize="xs" color="gray.500">
									Valid: {currentOffer.validFrom} – {currentOffer.validTill}
								</Text>
							</VStack>
						</Box>

						{/* Event Info */}
						<Box
							p={4}
							borderRadius="lg"
							bg={cardBg}
							border="1px solid"
							borderColor={borderClr}
						>
							<Text fontSize="lg" fontWeight="bold" mb={2}>
								Event Info
							</Text>
							<Divider mb={3} borderColor="whiteAlpha.300" />
							<VStack align="flex-start" spacing={2} fontSize="sm">
								<HStack>
									<Icon as={FiCalendar} />
									<Text>
										{new Date(event.startTime).toLocaleString()} -{" "}
										{new Date(event.endTime).toLocaleString()}
									</Text>
								</HStack>
								<HStack>
									<Text fontWeight="semibold">Category:</Text>
									<Text>{event.category}</Text>
								</HStack>
								{event.subCategory && (
									<HStack>
										<Text fontWeight="semibold">Sub Category:</Text>
										<Text>{event.subCategory}</Text>
									</HStack>
								)}
							</VStack>
						</Box>
					</Box>
				</Flex>
			</Box>

			{/* --------------- MODALS --------------- */}

			{/* Edit Event */}
			<Modal
				isOpen={isEditOpen}
				onClose={() => setEditOpen(false)}
				size="xl"
				isCentered
			>
				<ModalOverlay />
				<ModalContent bg="#10121a" color="white">
					<ModalHeader>Edit Event</ModalHeader>
					<ModalCloseButton />
					<ModalBody>
						<Stack spacing={4}>
							<FormControl>
								<FormLabel>Event ID</FormLabel>
								<Input value={editForm.eventId} disabled {...darkInputProps} />
							</FormControl>
							<FormControl>
								<FormLabel>Title</FormLabel>
								<Input
									name="title"
									value={editForm.title}
									onChange={handleEditChange}
									{...darkInputProps}
								/>
							</FormControl>
							<FormControl>
								<FormLabel>Description</FormLabel>
								<Input
									name="description"
									value={editForm.description}
									onChange={handleEditChange}
									{...darkInputProps}
								/>
							</FormControl>
							<FormControl>
								<FormLabel>Category</FormLabel>
								<Select value={editForm.category} disabled {...darkSelectProps}>
									<option>{editForm.category}</option>
								</Select>
							</FormControl>
							<FormControl>
								<FormLabel>Sub Category</FormLabel>
								<Input
									name="subCategory"
									value={editForm.subCategory}
									onChange={handleEditChange}
									{...darkInputProps}
								/>
							</FormControl>
							<FormControl>
								<FormLabel>Banner URL</FormLabel>
								<Input
									name="bannerUrl"
									value={editForm.bannerUrl}
									onChange={handleEditChange}
									{...darkInputProps}
								/>
							</FormControl>
							<FormControl>
								<FormLabel>Thumbnail URL</FormLabel>
								<Input
									name="thumbnailUrl"
									value={editForm.thumbnailUrl}
									onChange={handleEditChange}
									{...darkInputProps}
								/>
							</FormControl>
							<FormControl>
								<FormLabel>Start Time</FormLabel>
								<Input
									type="datetime-local"
									name="startTime"
									value={editForm.startTime}
									onChange={handleEditChange}
									{...darkInputProps}
								/>
							</FormControl>
							<FormControl>
								<FormLabel>End Time</FormLabel>
								<Input
									type="datetime-local"
									name="endTime"
									value={editForm.endTime}
									onChange={handleEditChange}
									{...darkInputProps}
								/>
							</FormControl>
						</Stack>
					</ModalBody>
					<ModalFooter>
						<Button
							colorScheme="gray"
							mr={3}
							variant="solid"
							borderColor="gray.500"
							onClick={() => setEditOpen(false)}
						>
							Cancel
						</Button>
						<Button colorScheme="yellow" onClick={handleSubmitEdit}>
							Save Changes
						</Button>
					</ModalFooter>
				</ModalContent>
			</Modal>

			{/* Delete Event */}
			<Modal
				isOpen={isDeleteOpen}
				onClose={() => setDeleteOpen(false)}
				isCentered
			>
				<ModalOverlay />
				<ModalContent bg="#1a1c25" color="white">
					<ModalHeader>Confirm Delete</ModalHeader>
					<ModalCloseButton />
					<ModalBody>
						<Text>Are you sure you want to delete this event?</Text>
					</ModalBody>
					<ModalFooter>
						<Button
							mr={3}
							variant="solid"
							colorScheme="gray"
							borderColor="gray.500"
							onClick={() => setDeleteOpen(false)}
						>
							Cancel
						</Button>
						<Button colorScheme="red" onClick={handleConfirmDelete}>
							Delete
						</Button>
					</ModalFooter>
				</ModalContent>
			</Modal>

			{/* Add Slot */}
			<Modal
				isOpen={isAddSlotOpen}
				onClose={() => setAddSlotOpen(false)}
				size="xl"
				isCentered
				scrollBehavior="inside"
			>
				<ModalOverlay />
				<ModalContent bg="#10121a" color="white">
					<ModalHeader>Add Event Slot</ModalHeader>
					<ModalCloseButton />
					<ModalBody>
						<VStack spacing={4} align="stretch">
							<FormControl>
								<FormLabel>Venue</FormLabel>
								<Select
									{...darkSelectProps}
									value={addSlotForm.venueId}
									onChange={(e) =>
										handleAddSlotChange("venueId", e.target.value)
									}
								>
									<option style={{ background: "#1A202C" }} value="">
										Select venue
									</option>
									{venues.map((v) => (
										<option
											key={v.venueId}
											value={v.venueId}
											style={{ background: "#1A202C" }}
										>
											{v.name} • {v.screenName} • {v.city}
										</option>
									))}
								</Select>
							</FormControl>
							<HStack>
								<FormControl>
									<FormLabel>Show Start</FormLabel>
									<Input
										type="datetime-local"
										value={addSlotForm.startTime}
										onChange={(e) =>
											handleAddSlotChange("startTime", e.target.value)
										}
										{...darkInputProps}
									/>
								</FormControl>
								<FormControl>
									<FormLabel>Show End</FormLabel>
									<Input
										type="datetime-local"
										value={addSlotForm.endTime}
										onChange={(e) =>
											handleAddSlotChange("endTime", e.target.value)
										}
										{...darkInputProps}
									/>
								</FormControl>
							</HStack>
							<HStack>
								<FormControl>
									<FormLabel>Format</FormLabel>
									<Select
										{...darkSelectProps}
										value={addSlotForm.format}
										onChange={(e) =>
											handleAddSlotChange("format", e.target.value)
										}
									>
										<option style={{ background: "#1A202C" }} value="NA">
											NA
										</option>
										<option style={{ background: "#1A202C" }} value="_2D">
											2D
										</option>
										<option style={{ background: "#1A202C" }} value="_3D">
											3D
										</option>
										<option style={{ background: "#1A202C" }} value="_4DX">
											4DX
										</option>
									</Select>
								</FormControl>
								<FormControl>
									<FormLabel>Language</FormLabel>
									<Input
										placeholder="e.g. Hindi, English"
										value={addSlotForm.language}
										onChange={(e) =>
											handleAddSlotChange("language", e.target.value)
										}
										{...darkInputProps}
									/>
								</FormControl>
								<FormControl>
									<FormLabel>Base Price (₹)</FormLabel>
									<Input
										type="number"
										value={addSlotForm.basePrice}
										onChange={(e) =>
											handleAddSlotChange("basePrice", e.target.value)
										}
										{...darkInputProps}
									/>
								</FormControl>
							</HStack>
							{/* Foods */}
							<Box>
								<Text fontSize="sm" color="gray.400" mb={2}>
									Food & Beverages for this slot (optional)
								</Text>
								<HStack mb={3}>
									<Input
										placeholder="Food name e.g. Popcorn"
										value={addSlotForm.newFoodName}
										onChange={(e) =>
											handleAddSlotChange("newFoodName", e.target.value)
										}
										{...darkInputProps}
									/>
									<Input
										placeholder="Price (₹)"
										type="number"
										value={addSlotForm.newFoodPrice}
										onChange={(e) =>
											handleAddSlotChange("newFoodPrice", e.target.value)
										}
										{...darkInputProps}
									/>
									<Button
										size="sm"
										colorScheme="teal"
										onClick={handleAddTemporaryFood}
									>
										Add
									</Button>
								</HStack>
								<VStack align="stretch">
									{(addSlotForm.foods || []).map((f, i) => (
										<HStack key={i} justify="space-between">
											<Text>
												{f.name} • ₹{f.price}
											</Text>
											<IconButton
												size="sm"
												aria-label="Remove"
												icon={<FiTrash2 />}
												onClick={() => handleRemoveTempFood(i)}
												variant="ghost"
												color="red.300"
											/>
										</HStack>
									))}
								</VStack>
							</Box>
						</VStack>
					</ModalBody>
					<ModalFooter>
						<Button
							colorScheme="gray"
							mr={3}
							variant="solid"
							borderColor="gray.500"
							onClick={() => setAddSlotOpen(false)}
						>
							Cancel
						</Button>
						<Button
							colorScheme="purple"
							onClick={submitAddSlot}
							isLoading={creatingSlot}
						>
							Add Slot
						</Button>
					</ModalFooter>
				</ModalContent>
			</Modal>

			{/* Edit Slot */}
			<Modal
				isOpen={isEditSlotOpen}
				onClose={() => setEditSlotOpen(false)}
				size="xl"
				isCentered
				scrollBehavior="inside"
			>
				<ModalOverlay />
				<ModalContent bg="#10121a" color="white">
					<ModalHeader>Edit Slot</ModalHeader>
					<ModalCloseButton />
					<ModalBody>
						<VStack spacing={4} align="stretch">
							<FormControl>
								<FormLabel>Venue</FormLabel>
								<Select
									{...darkSelectProps}
									value={editSlotForm.venueId}
									onChange={(e) =>
										setEditSlotForm((p) => ({
											...p,
											venueId: e.target.value,
										}))
									}
								>
									<option style={{ background: "#1A202C" }} value="">
										Select venue
									</option>
									{venues.map((v) => (
										<option
											key={v.venueId}
											value={v.venueId}
											style={{ background: "#1A202C" }}
										>
											{v.name} • {v.screenName} • {v.city}
										</option>
									))}
								</Select>
							</FormControl>
							<HStack>
								<FormControl>
									<FormLabel>Show Start</FormLabel>
									<Input
										type="datetime-local"
										value={editSlotForm.startTime}
										onChange={(e) =>
											setEditSlotForm((p) => ({
												...p,
												startTime: e.target.value,
											}))
										}
										{...darkInputProps}
									/>
								</FormControl>
								<FormControl>
									<FormLabel>Show End</FormLabel>
									<Input
										type="datetime-local"
										value={editSlotForm.endTime}
										onChange={(e) =>
											setEditSlotForm((p) => ({
												...p,
												endTime: e.target.value,
											}))
										}
										{...darkInputProps}
									/>
								</FormControl>
							</HStack>
							<HStack>
								<FormControl>
									<FormLabel>Format</FormLabel>
									<Select
										{...darkSelectProps}
										value={editSlotForm.format}
										onChange={(e) =>
											setEditSlotForm((p) => ({
												...p,
												format: e.target.value,
											}))
										}
									>
										<option style={{ background: "#1A202C" }} value="NA">
											NA
										</option>
										<option style={{ background: "#1A202C" }} value="_2D">
											2D
										</option>
										<option style={{ background: "#1A202C" }} value="_3D">
											3D
										</option>
										<option style={{ background: "#1A202C" }} value="_4DX">
											4DX
										</option>
									</Select>
								</FormControl>
								<FormControl>
									<FormLabel>Language</FormLabel>
									<Input
										value={editSlotForm.language}
										onChange={(e) =>
											setEditSlotForm((p) => ({
												...p,
												language: e.target.value,
											}))
										}
										{...darkInputProps}
									/>
								</FormControl>
								<FormControl>
									<FormLabel>Base Price (₹)</FormLabel>
									<Input
										type="number"
										value={editSlotForm.basePrice}
										onChange={(e) =>
											setEditSlotForm((p) => ({
												...p,
												basePrice: e.target.value,
											}))
										}
										{...darkInputProps}
									/>
								</FormControl>
							</HStack>
							{/* Foods */}
							<Box>
								<Text fontSize="sm" color="gray.400" mb={2}>
									Food & Beverages for this slot
								</Text>
								{foodsLoadingBySlot[editingSlotId] ? (
									<Spinner />
								) : (
									<>
										<VStack align="stretch" mb={3}>
											{(foodsBySlot[editingSlotId] || []).map((f) => (
												<HStack
													key={f.foodId}
													justify="space-between"
												>
													<Text>
														{f.name} • ₹{f.price}
													</Text>
													<IconButton
														size="sm"
														aria-label="Delete food"
														icon={<FiTrash2 />}
														onClick={() =>
															handleDeleteFood(f.foodId, editingSlotId)
														}
														variant="ghost"
													/>
												</HStack>
											))}
											{(foodsBySlot[editingSlotId] || []).length === 0 && (
												<Text color="gray.400">No food items.</Text>
											)}
										</VStack>
										<HStack>
											<Input
												placeholder="Food name e.g. Popcorn"
												value={foodInput.name}
												onChange={(e) =>
													setFoodInput((p) => ({
														...p,
														name: e.target.value,
													}))
												}
												{...darkInputProps}
											/>
											<Input
												placeholder="Price (₹)"
												type="number"
												value={foodInput.price}
												onChange={(e) =>
													setFoodInput((p) => ({
														...p,
														price: e.target.value,
													}))
												}
												{...darkInputProps}
											/>
											<Button
												colorScheme="teal"
												onClick={() => handleAddFoodToSlot(editingSlotId)}
											>
												Add
											</Button>
										</HStack>
									</>
								)}
							</Box>
						</VStack>
					</ModalBody>
					<ModalFooter>
						<Button
							colorScheme="gray"
							mr={3}
							variant="outline"
							borderColor="gray.500"
							onClick={() => setEditSlotOpen(false)}
						>
							Cancel
						</Button>
						<Button
							colorScheme="purple"
							onClick={submitEditSlot}
							isLoading={updatingSlot}
						>
							Save Slot
						</Button>
					</ModalFooter>
				</ModalContent>
			</Modal>

			{/* Rating Modal */}
			<Modal
				isOpen={isRatingOpen}
				onClose={() => setRatingOpen(false)}
				isCentered
			>
				<ModalOverlay />
				<ModalContent bg="#10121a" color="white">
					<ModalHeader>Rate this movie</ModalHeader>
					<ModalCloseButton />
					<ModalBody>
						<VStack spacing={4} align="center">
							<Text fontSize="sm" color="gray.300">
								Tap the stars to rate. Your rating helps others decide.
							</Text>
							<HStack spacing={2}>
								{[1, 2, 3, 4, 5].map((star) => {
									const active =
										hoverStars != null
											? star <= hoverStars
											: star <= (userRatingStars || 0);
									return (
										<IconButton
											key={star}
											aria-label={`Rate ${star} star`}
											icon={<StarIcon />}
											variant="ghost"
											size="lg"
											fontSize="2xl"
											color={active ? "yellow.400" : "gray.500"}
											_hover={{ color: "yellow.300", bg: "transparent" }}
											onMouseEnter={() => setHoverStars(star)}
											onMouseLeave={() => setHoverStars(null)}
											onClick={() => setUserRatingStars(star)}
										/>
									);
								})}
							</HStack>
							{userRatingStars && (
								<Text fontSize="sm" color="gray.200">
									You selected{" "}
									<b>
										{userRatingStars} / 5
									</b>{" "}
									({(userRatingStars * 2).toFixed(1)} / 10)
								</Text>
							)}
						</VStack>
					</ModalBody>
					<ModalFooter>
						<Button
							mr={3}
							onClick={() => setRatingOpen(false)}
							variant="solid"
							colorScheme="gray"
							borderColor="gray.500"
						>
							Cancel
						</Button>
						<Button
							colorScheme="yellow"
							onClick={handleSubmitRating}
							isLoading={submittingRating}
						>
							Submit Rating
						</Button>
					</ModalFooter>
				</ModalContent>
			</Modal>
		</Box>
	);
}