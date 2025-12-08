import React, { useEffect, useMemo, useState } from "react";
import {
	Box,
	Heading,
	Text,
	Badge,
	HStack,
	VStack,
	Stack,
	Button,
	Tag,
	TagLabel,
	TagLeftIcon,
	Icon,
	Tabs,
	TabList,
	TabPanels,
	Tab,
	TabPanel,
	Spinner,
	Divider,
	SimpleGrid,
	useDisclosure,
	Modal,
	ModalOverlay,
	ModalContent,
	ModalHeader,
	ModalBody,
	ModalCloseButton,
	ModalFooter,
	AlertDialog,
	AlertDialogOverlay,
	AlertDialogContent,
	AlertDialogHeader,
	AlertDialogBody,
	AlertDialogFooter,
} from "@chakra-ui/react";

import { useSelector } from "react-redux";
import { toast } from "react-toastify";

import { getBookingsByUser, deleteBooking } from "../apis/bookingApi";
import { getEventById } from "../apis/eventApi";
import axios from "axios";

import {
	FiMapPin,
	FiClock,
	FiCreditCard,
	FiPlayCircle,
	FiTrash2,
	FiInfo,
} from "react-icons/fi";
import { IoTicketOutline } from "react-icons/io5";
import { MdOutlineFastfood } from "react-icons/md";

const EVENT_SLOT_API = "/api/v1/eventslot";

function formatDateTime(value) {
	if (!value) return "";
	try {
		const d = new Date(value);
		return d.toLocaleString("en-IN", {
			weekday: "short",
			day: "numeric",
			month: "short",
			hour: "2-digit",
			minute: "2-digit",
		});
	} catch {
		return String(value);
	}
}

function humanTimeDiff(showStart) {
	if (!showStart) return "";
	try {
		const now = new Date();
		const start = new Date(showStart);
		const diffMs = start.getTime() - now.getTime();
		const diffHours = Math.round(diffMs / (1000 * 60 * 60));

		if (diffHours > 24) return `In ${Math.round(diffHours / 24)} days`;
		if (diffHours > 1) return `In ${diffHours} hours`;
		if (diffHours > 0) return "Starting soon";
		if (diffHours > -3) return "Ongoing / starting now";
		return "Completed";
	} catch {
		return "";
	}
}

function formatMoney(n) {
	if (n == null) return "0.00";
	const num = Number(n);
	if (Number.isNaN(num)) return String(n);
	return num.toFixed(2);
}

/**
 * booking: enriched booking object
 * onOpenDetails: open modal handler
 * onCancel: handler to open cancel dialog
 */
function BookingCard({ booking, onOpenDetails, onCancel }) {
	const event = booking.event;
	const slotMeta = booking.slotMeta;

	const showDate = formatDateTime(booking.showStart);
	const status = booking.payment?.paymentStatus || "SUCCESS";
	const isRefunded = status === "REFUNDED";
	const isUpcoming = new Date(booking.showStart) > new Date();

	const canCancel = isUpcoming && !isRefunded;

	// format label
	let fmtLabel = slotMeta?.format || "";
	if (fmtLabel === "_2D") fmtLabel = "2D";
	else if (fmtLabel === "_3D") fmtLabel = "3D";
	else if (fmtLabel === "_4DX") fmtLabel = "4DX";
	else if (fmtLabel === "NA") fmtLabel = "";

	const seatsPreview =
		booking.seats?.map((s) => `${s.row_label}${s.seat_number}`).join(", ") || "";

	const foodSummary =
		booking.foodItems && booking.foodItems.length > 0
			? booking.foodItems
				.map((f) => `${f.food_name} ×${f.quantity}`)
				.join(", ")
			: "No food added";

	return (
		<Box
			bg="gray.800"
			borderRadius="2xl"
			p={4}
			borderWidth="1px"
			borderColor="gray.700"
			_hover={{
				borderColor: "teal.400",
				boxShadow: "0 18px 45px rgba(15, 118, 110, 0.45)",
				transform: "translateY(-4px)",
			}}
			transition="all 0.22s ease-out"
		>
			<HStack align="stretch" spacing={4}>
				{/* Poster */}
				<Box
					w={28}
					minW={28}
					borderRadius="xl"
					overflow="hidden"
					bg="gray.900"
					position="relative"
				>
					<img
						src={event?.thumbnailUrl || event?.bannerUrl || "/placeholder-poster.png"}
						alt={event?.title || booking.eventTitle}
						style={{
							width: "100%",
							height: "100%",
							objectFit: "cover",
						}}
					/>
					<Badge
						position="absolute"
						top={2}
						left={2}
						colorScheme={isRefunded ? "purple" : "green"}
						borderRadius="full"
						px={2}
						fontSize="0.7rem"
					>
						{isRefunded ? "Refunded" : "Booked"}
					</Badge>
				</Box>

				{/* Main Content */}
				<VStack align="stretch" spacing={2} flex={1}>
					{/* Title row */}
					<HStack justify="space-between" align="flex-start">
						<VStack align="flex-start" spacing={1}>
							<HStack spacing={2}>
								<Heading size="md" color="white">
									{event?.title || booking.eventTitle}
								</Heading>
								{event?.category && (
									<Badge
										colorScheme={
											event.category === "MOVIE"
												? "pink"
												: event.category === "SPORT"
													? "yellow"
													: "blue"
										}
										borderRadius="full"
										px={2}
									>
										{event.category}
									</Badge>
								)}
								{event?.subCategory && (
									<Badge
										variant="outline"
										colorScheme="gray"
										borderRadius="full"
										px={2}
									>
										{event.subCategory}
									</Badge>
								)}
							</HStack>

							{/* Venue + Date */}
							<HStack spacing={3} color="gray.300" fontSize="sm">
								<HStack spacing={1}>
									<Icon as={FiMapPin} />
									<Text>{booking.venueName}</Text>
								</HStack>

								<HStack spacing={1}>
									<Icon as={FiClock} />
									<Text>{showDate}</Text>
									<Text color="teal.300" fontSize="xs">
										• {humanTimeDiff(booking.showStart)}
									</Text>
								</HStack>
							</HStack>

							{/* Format + Language + Seats */}
							<HStack spacing={2} mt={1} flexWrap="wrap">
								{fmtLabel && (
									<Tag size="sm" borderRadius="full" colorScheme="cyan">
										<TagLabel>{fmtLabel}</TagLabel>
									</Tag>
								)}

								{slotMeta?.language && (
									<Tag size="sm" borderRadius="full" colorScheme="purple">
										<TagLabel>{slotMeta.language}</TagLabel>
									</Tag>
								)}

								{seatsPreview && (
									<Tag
										size="sm"
										borderRadius="full"
										colorScheme="orange"
										maxW="260px"
										overflow="hidden"
										textOverflow="ellipsis"
										whiteSpace="nowrap"
									>
										<TagLeftIcon as={IoTicketOutline} />
										<TagLabel>{seatsPreview}</TagLabel>
									</Tag>
								)}
							</HStack>
						</VStack>

						{/* Right side: money + status + actions */}
						<VStack align="flex-end" spacing={2} minW="180px">
							<Text fontSize="xs" color="gray.400">
								Total Paid
							</Text>
							<Text fontSize="2xl" fontWeight="bold" color="teal.300">
								₹{formatMoney(booking.finalAmount)}
							</Text>

							<HStack>
								<Tag
									size="sm"
									borderRadius="full"
									colorScheme={
										isRefunded ? "purple" : status === "SUCCESS" ? "green" : "red"
									}
								>
									<TagLeftIcon as={FiCreditCard} />
									<TagLabel>
										{isRefunded ? "Refunded" : booking.payment?.paymentMode || "Paid"}
									</TagLabel>
								</Tag>
							</HStack>

							<HStack spacing={2} mt={1}>
								<Button
									size="sm"
									variant="solid"
									borderColor="gray.600"
									_hover={{ bg: "gray.700" }}
									leftIcon={<FiInfo />}
									onClick={onOpenDetails}
								>
									Details
								</Button>

								<Button
									size="sm"
									colorScheme="red"
									variant="solid"
									leftIcon={<FiTrash2 />}
									onClick={onCancel}
									isDisabled={!canCancel}
									opacity={canCancel ? 1 : 0.6}
								>
									Cancel
								</Button>
							</HStack>
						</VStack>
					</HStack>

					<Divider borderColor="gray.700" my={2} />

					{/* Food + Payment snippet */}
					<SimpleGrid columns={[1, 2, 3]} spacing={3} fontSize="sm">
						<HStack spacing={2}>
							<Icon as={IoTicketOutline} color="teal.300" />
							<Text color="gray.300">
								Tickets: ₹{formatMoney(booking.ticketTotal)}
							</Text>
						</HStack>

						<HStack spacing={2}>
							<Icon as={MdOutlineFastfood} color="orange.300" />
							<Text color="gray.300">
								Food: ₹{formatMoney(booking.foodTotal)}{" "}
								<Text as="span" color="gray.500">
									({foodSummary})
								</Text>
							</Text>
						</HStack>

						<HStack spacing={2}>
							<Icon as={FiPlayCircle} color="blue.300" />
							<Text color="gray.300">
								Transaction:{" "}
								<Text as="span" color="gray.400">
									{booking.payment?.transactionId}
								</Text>
							</Text>
						</HStack>
					</SimpleGrid>
				</VStack>
			</HStack>
		</Box>
	);
}

export default function MyBookingsPage() {
	const user = useSelector((s) => s.user);
	const userId = user?.userId;

	const [bookings, setBookings] = useState([]);
	const [loading, setLoading] = useState(true);
	const [detailsBooking, setDetailsBooking] = useState(null);

	const [cancelTarget, setCancelTarget] = useState(null);
	const [cancelLoading, setCancelLoading] = useState(false);
	const cancelDialog = useDisclosure();
	const detailsModal = useDisclosure();
	const cancelRef = React.useRef();

	// fetch bookings + enrich
	useEffect(() => {
		const load = async () => {
			if (!userId) {
				setLoading(false);
				return;
			}

			try {
				setLoading(true);
				const res = await getBookingsByUser(userId);
				const base = Array.isArray(res.data) ? res.data : [];

				if (base.length === 0) {
					setBookings([]);
					return;
				}

				// Enrich each booking with slot + event
				const enriched = await Promise.all(
					base.map(async (b) => {
						try {
							let slotMeta = null;
							let event = null;

							// Get slot info
							try {
								const slotRes = await axios.get(
									`${EVENT_SLOT_API}/${b.slotId}`
								);
								slotMeta = slotRes.data;
							} catch (err) {
								console.error("Failed to fetch slot for booking", b.bookingId, err);
							}

							// Get event via eventId inside slot
							try {
								const eventId = slotMeta?.eventId;
								if (eventId) {
									const evRes = await getEventById(eventId);
									event = evRes.data;
								}
							} catch (err) {
								console.error("Failed to fetch event for booking", b.bookingId, err);
							}

							return { ...b, slotMeta, event };
						} catch (err) {
							console.error("Enrich booking failed", err);
							return { ...b, slotMeta: null, event: null };
						}
					})
				);

				setBookings(enriched);
			} catch (err) {
				console.error(err);
				toast.error("Failed to load your bookings.");
			} finally {
				setLoading(false);
			}
		};

		load();
	}, [userId]);

	const now = new Date();

	const upcoming = useMemo(
		() =>
			bookings
				.filter((b) => new Date(b.showStart) >= now)
				.sort(
					(a, b) =>
						new Date(a.showStart).getTime() - new Date(b.showStart).getTime()
				),
		[bookings, now]
	);

	const past = useMemo(
		() =>
			bookings
				.filter((b) => new Date(b.showStart) < now)
				.sort(
					(a, b) =>
						new Date(b.showStart).getTime() - new Date(a.showStart).getTime()
				),
		[bookings, now]
	);

	const handleOpenDetails = (booking) => {
		setDetailsBooking(booking);
		detailsModal.onOpen();
	};

	const handleAskCancel = (booking) => {
		setCancelTarget(booking);
		cancelDialog.onOpen();
	};

	const handleConfirmCancel = async () => {
		if (!cancelTarget) return;
		try {
			setCancelLoading(true);
			const res = await deleteBooking(cancelTarget.bookingId);
			const data = res.data;

			toast.success(
				`Booking cancelled. Refund ₹${formatMoney(
					data?.refundAmount
				)} (${data?.refundPercentage || ""})`
			);

			setBookings((prev) =>
				prev.filter((b) => b.bookingId !== cancelTarget.bookingId)
			);
			cancelDialog.onClose();
			setCancelTarget(null);
		} catch (err) {
			console.error(err);
			const msg =
				err?.response?.data?.message ||
				err?.response?.data ||
				"Failed to cancel booking.";
			toast.error(msg);
		} finally {
			setCancelLoading(false);
		}
	};

	if (!userId) {
		return (
			<Box bg="gray.900" color="white" minH="100vh" py={12} px={6}>
				<Box maxW="4xl" mx="auto" textAlign="center">
					<Heading size="lg" mb={4}>
						Your bookings
					</Heading>
					<Text color="gray.400" mb={6}>
						Please sign in to view your bookings.
					</Text>
					{/* you can link to login page here if you want */}
				</Box>
			</Box>
		);
	}

	return (
		<Box bg="gray.900" color="white" minH="100vh" py={10} px={4}>
			<Box maxW="6xl" mx="auto">
				{/* Page header */}
				<VStack align="flex-start" spacing={1} mb={6}>
					<Heading size="lg">Your Bookings</Heading>
					<Text color="gray.400" fontSize="sm">
						All your movie, event and sports bookings in one place.
					</Text>
				</VStack>

				{/* Loading state */}
				{loading ? (
					<Box textAlign="center" py={20}>
						<Spinner size="xl" color="teal.300" />
						<Text mt={4} color="gray.400">
							Fetching your bookings…
						</Text>
					</Box>
				) : bookings.length === 0 ? (
					<Box textAlign="center" py={20}>
						<Text fontSize="lg" color="gray.300" mb={2}>
							You haven&apos;t booked anything yet.
						</Text>
						<Text fontSize="sm" color="gray.500">
							Explore movies, events and sports to get started!
						</Text>
					</Box>
				) : (
					<Tabs variant="soft-rounded" colorScheme="teal">
						<TabList mb={4}>
							<Tab
								_selected={{ bg: "teal.500", color: "white" }}
								fontWeight="600"
							>
								Upcoming ({upcoming.length})
							</Tab>
							<Tab
								_selected={{ bg: "gray.700", color: "teal.200" }}
								fontWeight="600"
							>
								Past ({past.length})
							</Tab>
						</TabList>

						<TabPanels>
							<TabPanel px={0}>
								<VStack spacing={4} align="stretch">
									{upcoming.map((b) => (
										<BookingCard
											key={b.bookingId}
											booking={b}
											onOpenDetails={() => handleOpenDetails(b)}
											onCancel={() => handleAskCancel(b)}
										/>
									))}
									{upcoming.length === 0 && (
										<Box textAlign="center" py={10}>
											<Text color="gray.400">
												No upcoming bookings. Time to plan something! 🎟️
											</Text>
										</Box>
									)}
								</VStack>
							</TabPanel>

							<TabPanel px={0}>
								<VStack spacing={4} align="stretch">
									{past.map((b) => (
										<BookingCard
											key={b.bookingId}
											booking={b}
											onOpenDetails={() => handleOpenDetails(b)}
											onCancel={() => handleAskCancel(b)}
										/>
									))}
									{past.length === 0 && (
										<Box textAlign="center" py={10}>
											<Text color="gray.400">
												No past bookings yet. Your history will appear here.
											</Text>
										</Box>
									)}
								</VStack>
							</TabPanel>
						</TabPanels>
					</Tabs>
				)}
			</Box>

			{/* Details Modal */}
			<Modal isOpen={detailsModal.isOpen} onClose={detailsModal.onClose} size="4xl" isCentered>
				<ModalOverlay />
				<ModalContent bg="gray.900" color="white">
					<ModalHeader borderBottom="1px solid" borderColor="gray.700">
						Booking Details
					</ModalHeader>
					<ModalCloseButton />
					<ModalBody>
						{detailsBooking && (
							<VStack align="stretch" spacing={4}>
								{/* Top banner */}
								<HStack align="stretch" spacing={4}>
									<Box
										flex="1"
										borderRadius="xl"
										overflow="hidden"
										bg="gray.800"
										minH="150px"
									>
										<img
											src={
												detailsBooking.event?.bannerUrl ||
												detailsBooking.event?.thumbnailUrl ||
												"/placeholder-banner.png"
											}
											alt={detailsBooking.event?.title || detailsBooking.eventTitle}
											style={{
												width: "100%",
												height: "100%",
												objectFit: "cover",
											}}
										/>
									</Box>
									<VStack align="flex-start" spacing={1} flex="1">
										<Heading size="md">
											{detailsBooking.event?.title || detailsBooking.eventTitle}
										</Heading>
										<Text fontSize="sm" color="gray.400">
											{detailsBooking.event?.description}
										</Text>
										<HStack spacing={2} mt={2}>
											{detailsBooking.event?.category && (
												<Badge colorScheme="teal" borderRadius="full">
													{detailsBooking.event.category}
												</Badge>
											)}
											{detailsBooking.event?.subCategory && (
												<Badge variant="outline" borderRadius="full">
													{detailsBooking.event.subCategory}
												</Badge>
											)}
										</HStack>
										<HStack spacing={2} mt={2} color="gray.300" fontSize="sm">
											<Icon as={FiMapPin} />
											<Text>{detailsBooking.venueName}</Text>
										</HStack>
										<HStack spacing={2} color="gray.300" fontSize="sm">
											<Icon as={FiClock} />
											<Text>{formatDateTime(detailsBooking.showStart)}</Text>
										</HStack>
									</VStack>
								</HStack>

								<Divider borderColor="gray.700" />

								{/* Seats + Food */}
								<SimpleGrid columns={[1, 2]} spacing={4}>
									<Box>
										<Heading size="sm" mb={2}>
											Seats
										</Heading>
										{detailsBooking.seats && detailsBooking.seats.length > 0 ? (
											<HStack spacing={2} flexWrap="wrap">
												{detailsBooking.seats.map((s) => (
													<Tag
														key={s.seat_id}
														size="sm"
														borderRadius="full"
														colorScheme="orange"
													>
														{s.row_label}
														{s.seat_number}
													</Tag>
												))}
											</HStack>
										) : (
											<Text fontSize="sm" color="gray.400">
												No seat data.
											</Text>
										)}
									</Box>

									<Box>
										<Heading size="sm" mb={2}>
											Food & Beverages
										</Heading>
										{detailsBooking.foodItems &&
											detailsBooking.foodItems.length > 0 ? (
											<VStack align="stretch" spacing={1} fontSize="sm">
												{detailsBooking.foodItems.map((f) => (
													<HStack key={f.booking_food_id} justify="space-between">
														<Text color="gray.200">
															{f.food_name} ×{f.quantity}
														</Text>
														<Text color="gray.300">
															₹{formatMoney(f.line_total)}
														</Text>
													</HStack>
												))}
											</VStack>
										) : (
											<Text fontSize="sm" color="gray.400">
												No food added for this booking.
											</Text>
										)}
									</Box>
								</SimpleGrid>

								<Divider borderColor="gray.700" />

								{/* Payment Summary */}
								<Box>
									<Heading size="sm" mb={2}>
										Payment Summary
									</Heading>
									<VStack align="stretch" spacing={1} fontSize="sm">
										<HStack justify="space-between">
											<Text color="gray.300">Tickets</Text>
											<Text>₹{formatMoney(detailsBooking.ticketTotal)}</Text>
										</HStack>
										<HStack justify="space-between">
											<Text color="gray.300">Food & Beverages</Text>
											<Text>₹{formatMoney(detailsBooking.foodTotal)}</Text>
										</HStack>
										{detailsBooking.offerApplied && (
											<HStack justify="space-between">
												<Text color="gray.300">Offer Applied</Text>
												<Text color="teal.300">
													{detailsBooking.offerApplied}
												</Text>
											</HStack>
										)}
										<HStack justify="space-between" mt={2}>
											<Text color="gray.400">Final Amount Paid</Text>
											<Text fontSize="lg" fontWeight="bold" color="teal.300">
												₹{formatMoney(detailsBooking.finalAmount)}
											</Text>
										</HStack>
										{detailsBooking.payment && (
											<>
												<HStack justify="space-between">
													<Text color="gray.500">Payment Mode</Text>
													<Text>{detailsBooking.payment.paymentMode}</Text>
												</HStack>
												<HStack justify="space-between">
													<Text color="gray.500">Status</Text>
													<Text>{detailsBooking.payment.paymentStatus}</Text>
												</HStack>
												<HStack justify="space-between">
													<Text color="gray.500">Transaction ID</Text>
													<Text>{detailsBooking.payment.transactionId}</Text>
												</HStack>
											</>
										)}
									</VStack>
								</Box>
							</VStack>
						)}
					</ModalBody>
					<ModalFooter>
						<Button variant="ghost" onClick={detailsModal.onClose}>
							Close
						</Button>
					</ModalFooter>
				</ModalContent>
			</Modal>

			{/* Cancel dialog */}
			<AlertDialog
				isOpen={cancelDialog.isOpen}
				leastDestructiveRef={cancelRef}
				onClose={cancelDialog.onClose}
			>
				<AlertDialogOverlay>
					<AlertDialogContent bg="gray.900" color="white">
						<AlertDialogHeader fontSize="lg" fontWeight="bold">
							Cancel booking?
						</AlertDialogHeader>

						<AlertDialogBody color="gray.300">
							{cancelTarget && (
								<>
									You&apos;re about to cancel booking for{" "}
									<Text as="span" color="teal.300" fontWeight="semibold">
										{cancelTarget.event?.title || cancelTarget.eventTitle}
									</Text>
									. Refund amount depends on how much time is left before the
									show, as per policy.
								</>
							)}
						</AlertDialogBody>

						<AlertDialogFooter>
							<Button ref={cancelRef} onClick={cancelDialog.onClose}>
								No, go back
							</Button>
							<Button
								colorScheme="red"
								ml={3}
								onClick={handleConfirmCancel}
								isLoading={cancelLoading}
							>
								Yes, cancel booking
							</Button>
						</AlertDialogFooter>
					</AlertDialogContent>
				</AlertDialogOverlay>
			</AlertDialog>
		</Box>
	);
}