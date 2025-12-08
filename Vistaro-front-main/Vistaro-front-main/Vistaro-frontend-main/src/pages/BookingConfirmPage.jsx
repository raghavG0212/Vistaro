import React, {
	useCallback,
	useEffect,
	useMemo,
	useState,
} from "react";
import { useLocation, useNavigate } from "react-router-dom";
import {
	Box,
	Flex,
	Heading,
	Text,
	Badge,
	VStack,
	HStack,
	Divider,
	Button,
	Spinner,
	Stat,
	StatLabel,
	StatNumber,
	StatHelpText,
	SimpleGrid,
	Tag,
	useDisclosure,
	Modal,
	ModalOverlay,
	ModalContent,
	ModalHeader,
	ModalBody,
	ModalFooter,
	ModalCloseButton,
	Progress,
	Stack,
} from "@chakra-ui/react";
import { motion } from "framer-motion";
import { toast } from "react-toastify";
import { useSelector } from "react-redux";

import { getEventById } from "../apis/eventApi";
import { getSlotsByEventId } from "../apis/eventSlotApi";
import { getSeatsForSlot, unlockSeats } from "../apis/seatApi";
import { getFoodsBySlot } from "../apis/foodApi";
import { createBooking } from "../apis/bookingApi";
import Loader from "../components/Loader";

const MotionBox = motion(Box);

// simple client-side offer estimation to match your offers
function computeOfferDiscount(offerCode, grossTotal) {
	if (!offerCode) return 0;
	const code = offerCode.trim().toUpperCase();
	let pct = 0;
	let max = Infinity;

	if (code === "VIST50") {
		pct = 50;
		max = 150;
	} else if (code === "BANK10") {
		pct = 10;
		max = 200;
	} else if (code === "WEEKEND20") {
		pct = 20;
		max = 100;
	} else {
		return 0;
	}

	let discount = (grossTotal * pct) / 100;
	if (discount > max) discount = max;
	if (discount < 0) discount = 0;
	return discount;
}

export default function BookingConfirmPage() {
	const location = useLocation();
	const navigate = useNavigate();
	const { isOpen, onOpen, onClose } = useDisclosure();
	const userId = useSelector((state) => state.user.userId);

	const state = location.state || {};
	const {
		eventId,
		slotId,
		seatIds,
		foodItems = [],
		offerCode,
		giftCardCode,
		paymentMode,
	} = state;

	// redirect if direct open without context
	useEffect(() => {
		if (!eventId || !slotId || !seatIds || !seatIds.length) {
			toast.error("Booking session not found. Please start again.");
			navigate(`/eventslots/${eventId || ""}`, { replace: true });
		}
	}, [eventId, slotId, seatIds, navigate]);

	// ------------ STATE -------------
	const [loading, setLoading] = useState(true);
	const [event, setEvent] = useState(null);
	const [slot, setSlot] = useState(null);
	const [seats, setSeats] = useState([]);
	const [foods, setFoods] = useState([]);
	const [bookingDone, setBookingDone] = useState(false);
	const [submitting, setSubmitting] = useState(false);
	const [timeLeft, setTimeLeft] = useState(600); // 10 min

	// ------------ LOAD DATA -------------
	useEffect(() => {
		const fetchAll = async () => {
			try {
				setLoading(true);
				const [eventRes, slotsRes, seatsRes, foodsRes] = await Promise.all([
					getEventById(eventId),
					getSlotsByEventId(eventId),
					getSeatsForSlot(slotId),
					getFoodsBySlot(slotId),
				]);

				setEvent(eventRes.data);
				const slotList = slotsRes.data || [];
				const foundSlot = slotList.find((s) => s.slotId === slotId);
				setSlot(foundSlot || null);

				setSeats(seatsRes.data || []);
				setFoods(foodsRes.data || []);
			} catch (err) {
				console.error("Failed to load booking confirm data", err);
				const msg =
					err?.response?.data?.message ||
					err?.response?.data ||
					"Failed to load booking details.";
				toast.error(msg);
			} finally {
				setLoading(false);
			}
		};

		if (eventId && slotId) fetchAll();
	}, [eventId, slotId]);

	// ------------ HELPERS -------------
	const formatDateTime = (val) => {
		if (!val) return "";
		try {
			return new Date(val).toLocaleString();
		} catch {
			return String(val);
		}
	};

	// seats that correspond to seatIds
	const selectedSeatObjects = useMemo(() => {
		if (!seatIds || !seatIds.length) return [];
		return seatIds
			.map((id) =>
				seats.find((s) => (s.seatId || s.seat_id) === id)
			)
			.filter(Boolean);
	}, [seats, seatIds]);

	const selectedFoodObjects = useMemo(() => {
		if (!foodItems || !foodItems.length) return [];
		return foodItems
			.map((fi) => {
				const food = foods.find(
					(f) => (f.foodId || f.food_id) === fi.foodId
				);
				return food
					? {
						...food,
						quantity: fi.quantity,
					}
					: null;
			})
			.filter(Boolean);
	}, [foods, foodItems]);

	// ------------ PRICE CALC -------------
	const ticketTotal = useMemo(() => {
		return selectedSeatObjects.reduce((sum, seat) => {
			const price = Number(seat.price);
			return sum + (isNaN(price) ? 0 : price);
		}, 0);
	}, [selectedSeatObjects]);

	const foodTotal = useMemo(() => {
		return selectedFoodObjects.reduce((sum, f) => {
			const price = Number(f.price);
			const qty = f.quantity || 0;
			return sum + (isNaN(price) ? 0 : price * qty);
		}, 0);
	}, [selectedFoodObjects]);

	const estimatedTotal = ticketTotal + foodTotal;
	const offerDiscount = computeOfferDiscount(offerCode, estimatedTotal);
	const finalClientTotal = Math.max(0, estimatedTotal - offerDiscount);

	// ------------ TIMER + AUTO UNLOCK -------------
	const handleTimeout = useCallback(async () => {
		if (bookingDone) return;
		toast.error("Payment time expired. Seats have been released.");

		try {
			if (seatIds && seatIds.length) {
				await unlockSeats(seatIds);
			}
		} catch (err) {
			console.error("Failed to unlock seats on timeout", err);
		}

		navigate(`/eventslots/${eventId}`, { replace: true });
	}, [bookingDone, seatIds, eventId, navigate]);

	useEffect(() => {
		if (!seatIds || !seatIds.length) return;

		const interval = setInterval(() => {
			setTimeLeft((prev) => {
				if (prev <= 1) {
					clearInterval(interval);
					handleTimeout();
					return 0;
				}
				return prev - 1;
			});
		}, 1000);

		return () => clearInterval(interval);
	}, [handleTimeout, seatIds]);

	// unlock on unmount if booking not done
	useEffect(() => {
		return () => {
			if (!bookingDone && seatIds && seatIds.length) {
				unlockSeats(seatIds).catch((err) =>
					console.error("Failed to unlock on unmount", err)
				);
			}
		};
	}, [bookingDone, seatIds]);

	const minutes = String(Math.floor(timeLeft / 60)).padStart(2, "0");
	const seconds = String(timeLeft % 60).padStart(2, "0");
	const progressPercent = (timeLeft / 600) * 100;

	// ------------ CONFIRM BOOKING (ACTUAL API) -------------
	const handleConfirmBooking = async () => {
		if (!slot) {
			toast.error("No show selected");
			return;
		}
		if (!seatIds || !seatIds.length) {
			toast.error("No seats selected");
			return;
		}
		if (!paymentMode) {
			toast.error("No payment mode selected");
			return;
		}

		try {
			setSubmitting(true);
			const payload = {
				userId,
				slotId,
				seatIds,
				offerCode: offerCode || null,
				giftCardCode: giftCardCode || null,
				foodItems: foodItems || [],
				paymentMode,
			};

			await createBooking(payload);
			setBookingDone(true);

			toast.success("Booking confirmed!");
			navigate("/bookings");
		} catch (err) {
			console.error("Booking failed", err);
			const msg =
				err?.response?.data?.message ||
				err?.response?.data ||
				"Booking failed. Please try again.";
			toast.error(msg);
		} finally {
			setSubmitting(false);
		}
	};

	// ------------ RENDER -------------
	if (loading) {
		return <Loader />;
	}

	if (!event || !slot) {
		return (
			<Flex
				minH="100vh"
				bg="gray.900"
				align="center"
				justify="center"
				color="red.300"
			>
				<Text>Unable to load booking details.</Text>
			</Flex>
		);
	}

	return (
		<Box bg="gray.950" minH="100vh" color="gray.100" py={6} px={[4, 6, 10]}>
			<Box maxW="1100px" mx="auto">
				{/* HEADER */}
				<MotionBox
					bg="gray.900"
					borderRadius="2xl"
					p={5}
					mb={6}
					borderWidth="1px"
					borderColor="gray.700"
					initial={{ opacity: 0, y: -10 }}
					animate={{ opacity: 1, y: 0 }}
					transition={{ duration: 0.3 }}
				>
					<Flex justify="space-between" align="center" gap={4}>
						<VStack align="flex-start" spacing={1} maxW="70%">
							<Heading size="md" color="gray.100">
								{event.title}
							</Heading>
							<HStack spacing={3}>
								<Badge colorScheme="purple">{event.category}</Badge>
								{event.subCategory && (
									<Badge variant="outline" colorScheme="gray">
										{event.subCategory}
									</Badge>
								)}
							</HStack>
							<Text fontSize="sm" color="gray.300">
								{formatDateTime(slot.startTime)} • {slot.language} •{" "}
								{slot.format}
							</Text>
							<Text fontSize="xs" color="gray.400">
								Complete payment before timer expires, or your seats
								will be released automatically.
							</Text>
						</VStack>

						{/* Timer card */}
						<MotionBox
							p={4}
							borderRadius="xl"
							bg="gray.800"
							borderWidth="1px"
							borderColor="gray.700"
							minW="220px"
							initial={{ opacity: 0, scale: 0.9 }}
							animate={{ opacity: 1, scale: 1 }}
							transition={{ duration: 0.3, delay: 0.1 }}
						>
							<Text fontSize="xs" color="gray.400" mb={1}>
								Payment time left
							</Text>
							<Heading
								size="lg"
								color={timeLeft < 60 ? "red.300" : "green.300"}
							>
								{minutes}:{seconds}
							</Heading>
							<Progress
								mt={3}
								value={progressPercent}
								size="sm"
								borderRadius="full"
								colorScheme={timeLeft < 60 ? "red" : "green"}
							/>
						</MotionBox>
					</Flex>
				</MotionBox>

				<Flex direction={["column", null, "row"]} gap={6}>
					{/* LEFT: details */}
					<MotionBox
						flex="2"
						bg="gray.900"
						borderRadius="2xl"
						borderWidth="1px"
						borderColor="gray.700"
						p={5}
						initial={{ opacity: 0, x: -10 }}
						animate={{ opacity: 1, x: 0 }}
						transition={{ duration: 0.4 }}
					>
						<Heading size="md" mb={3} color="gray.100">
							Review your booking
						</Heading>
						<Divider mb={4} borderColor="gray.700" />

						{/* Seats */}
						<Box mb={4}>
							<Text fontWeight="semibold" color="gray.200" mb={1}>
								Seats
							</Text>
							{selectedSeatObjects.length === 0 ? (
								<Text fontSize="sm" color="red.300">
									No seats found for this booking context.
								</Text>
							) : (
								<HStack spacing={2} wrap="wrap">
									{selectedSeatObjects.map((s) => (
										<Tag
											key={s.seatId || s.seat_id}
											size="sm"
											bg="gray.800"
											borderRadius="full"
											color="white"
										>
											{s.seatNumber || s.seat_number}
										</Tag>
									))}
								</HStack>
							)}
						</Box>

						{/* Food */}
						<Box mb={4}>
							<Text fontWeight="semibold" color="gray.200" mb={1}>
								Food & Beverages
							</Text>
							{selectedFoodObjects.length === 0 ? (
								<Text fontSize="sm" color="gray.400">
									No food items selected.
								</Text>
							) : (
								<SimpleGrid columns={[1, 2]} spacing={3}>
									{selectedFoodObjects.map((f) => (
										<Box
											key={f.foodId || f.food_id}
											bg="gray.800"
											borderRadius="lg"
											p={3}
											borderWidth="1px"
											borderColor="gray.700"
										>
											<Text fontSize="sm" fontWeight="medium">
												{f.name}
											</Text>
											<Text fontSize="xs" color="gray.400">
												Qty: {f.quantity} • ₹{Number(f.price || 0)}
											</Text>
										</Box>
									))}
								</SimpleGrid>
							)}
						</Box>

						{/* Price summary */}
						<Box mt={2}>
							<Heading size="sm" mb={2}>
								Price Summary
							</Heading>
							<Divider mb={3} borderColor="gray.700" />
							<SimpleGrid columns={[1, 2]} spacing={4}>
								<Stat>
									<StatLabel>Ticket total</StatLabel>
									<StatNumber fontSize="lg">
										₹{ticketTotal.toFixed(2)}
									</StatNumber>
								</Stat>
								<Stat>
									<StatLabel>Food total</StatLabel>
									<StatNumber fontSize="lg">
										₹{foodTotal.toFixed(2)}
									</StatNumber>
								</Stat>
								<Stat>
									<StatLabel>Subtotal</StatLabel>
									<StatNumber fontSize="lg">
										₹{estimatedTotal.toFixed(2)}
									</StatNumber>
									<StatHelpText fontSize="xs">
										Before offers
									</StatHelpText>
								</Stat>
								<Stat>
									<StatLabel>Offer discount</StatLabel>
									<StatNumber fontSize="lg" color="green.300">
										-₹{offerDiscount.toFixed(2)}
									</StatNumber>
									<StatHelpText fontSize="xs">
										{offerCode
											? `Estimated for "${offerCode.toUpperCase()}"`
											: "No offer applied"}
									</StatHelpText>
								</Stat>
							</SimpleGrid>
							<Divider my={3} borderColor="gray.700" />
							<Flex justify="space-between" align="center">
								<Text fontWeight="semibold">Amount payable</Text>
								<Text fontSize="xl" fontWeight="bold" color="teal.300">
									₹{finalClientTotal.toFixed(2)}
								</Text>
							</Flex>
							<Text mt={2} fontSize="xs" color="gray.500">
								Final amount is calculated on the server based on active offers
								and availability. This is an estimate to help you review the
								discount before paying.
							</Text>
						</Box>

						<Box mt={6}>
							<Button
								colorScheme="teal"
								size="lg"
								onClick={onOpen}
								isLoading={submitting}
								loadingText="Processing..."
							>
								Proceed to Pay
							</Button>
						</Box>
					</MotionBox>

					{/* RIGHT: summary card */}
					{/* (you can leave your existing right sidebar as is or simplify it,
              since we already show full summary on the left now) */}

					<Box
						flex="1"
						bg="gray.900"
						borderRadius="2xl"
						borderWidth="1px"
						borderColor="gray.700"
						p={4}
					>
						<Heading size="sm" mb={3}>
							Quick Summary
						</Heading>
						<Divider mb={3} borderColor="gray.700" />
						<Stack spacing={2} fontSize="sm">
							<Text fontWeight="semibold">{event.title}</Text>
							<Text color="gray.300">{formatDateTime(slot.startTime)}</Text>
							<HStack spacing={2} mb={2}>
								{slot.language && (
									<Tag size="sm" bg="gray.800" color="white">
										{slot.language}
									</Tag>
								)}
								{slot.format && (
									<Tag size="sm" bg="gray.800" color="white">
										{slot.format.replace("_", "")}
									</Tag>
								)}
							</HStack>

							<Divider borderColor="gray.700" />

							<Text fontWeight="medium">Seats</Text>
							<Text fontSize="xs" color="gray.200" textColor="white">
								{selectedSeatObjects.length} seat(s)
							</Text>

							<Divider borderColor="gray.700" />

							<Text fontWeight="medium">Offer</Text>
							{offerCode ? (
								<Text fontSize="xs" color="green.300">
									{offerCode.toUpperCase()} applied
								</Text>
							) : (
								<Text fontSize="xs" color="gray.400">
									No offer applied
								</Text>
							)}

							<Divider borderColor="gray.700" />

							<Text fontWeight="medium">To Pay (estimate)</Text>
							<Text fontSize="lg" fontWeight="bold" color="teal.300">
								₹{finalClientTotal.toFixed(2)}
							</Text>
						</Stack>
					</Box>
				</Flex>
			</Box>

			{/* CONFIRM MODAL */}
			<Modal isOpen={isOpen} onClose={onClose} isCentered>
				<ModalOverlay />
				<ModalContent bg="gray.900" color="gray.100">
					<ModalHeader>Confirm & Pay</ModalHeader>
					<ModalCloseButton />
					<ModalBody>
						<Text mb={2}>
							You are about to pay{" "}
							<b>₹{finalClientTotal.toFixed(2)}</b> for:
						</Text>
						<Text fontWeight="bold" color="teal.300">
							{event.title}
						</Text>
						<Text mt={2} fontSize="sm" color="gray.300">
							Show time: {formatDateTime(slot.startTime)}
						</Text>
						{offerCode && (
							<Text mt={2} fontSize="sm" color="green.300">
								Offer <b>{offerCode.toUpperCase()}</b> applied (estimated
								discount: ₹{offerDiscount.toFixed(2)}).
							</Text>
						)}
						<Text mt={3} fontSize="xs" color="gray.500">
							On confirm, booking will be created. If the server rejects the
							offer (expired/invalid), you’ll see the exact error.
						</Text>
					</ModalBody>
					<ModalFooter>
						<Button variant="ghost" mr={3} onClick={onClose}>
							Cancel
						</Button>
						<Button
							colorScheme="teal"
							onClick={handleConfirmBooking}
							isLoading={submitting}
						>
							Confirm & Pay ₹{finalClientTotal.toFixed(2)}
						</Button>
					</ModalFooter>
				</ModalContent>
			</Modal>
		</Box>
	);
}