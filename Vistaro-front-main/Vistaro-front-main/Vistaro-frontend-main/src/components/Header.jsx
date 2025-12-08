import React, { useState, useEffect, useRef } from "react";
import {
	Box, HStack, Text, Button, Menu, MenuButton, MenuList, MenuItem,
	Modal, ModalOverlay, ModalContent,
	InputGroup, InputLeftElement, Input,
	VStack, Divider, SimpleGrid, Center,
	Drawer, DrawerOverlay, DrawerContent, DrawerCloseButton, DrawerBody,
	AlertDialog, AlertDialogOverlay, AlertDialogContent,
	AlertDialogHeader, AlertDialogFooter, AlertDialogBody
} from "@chakra-ui/react";

import { Link, useNavigate, useLocation } from "react-router-dom";
import { FiSearch, FiMenu } from "react-icons/fi";
import { IoLocationSharp } from "react-icons/io5";
import { ChevronDownIcon } from "@chakra-ui/icons";

import { useSelector, useDispatch } from "react-redux";
import { setCity } from "../redux/citySlice";
import { logoutUser } from "../redux/userSlice";
import { persistor } from "../redux/store";

import axios from "axios";
import { searchByTitle } from "../apis/eventApi";
import { toast } from "react-toastify";
import AdminCreateFlow from "./AdminCreateFlow";


export default function Header() {
	const navigate = useNavigate();
	const location = useLocation();
	const dispatch = useDispatch();

	const user = useSelector((state) => state.user);
	const userCity = user?.city || null;
	const storedCity = useSelector((state) => state.city?.selectedCity || null);

	const isLoggedIn = !!user?.isAuthenticated;
	const role = user?.role;
	const isAdmin = isLoggedIn && role === "ADMIN";
	const isNormalUser = isLoggedIn && role === "USER";

	const currentCityName = storedCity?.city || userCity || "Goa";

	useEffect(() => {
		if (userCity && !storedCity) {
			dispatch(setCity({ id: null, city: userCity }));
		}
	}, [userCity, storedCity, dispatch]);

	const allCities = [
		"Mumbai",
		"Delhi",
		"Bengaluru",
		"Chennai",
		"Kolkata",
		"Hyderabad",
		"Jaipur",
		"Agra",
		"Lucknow",
		"Goa",
	];

	const cityEmojis = {
		Mumbai: "🏙️",
		Delhi: "🏛️",
		Bengaluru: "💻",
		Chennai: "🏝️",
		Kolkata: "🌆",
		Hyderabad: "🕌",
		Jaipur: "🏰",
		Agra: "🕌",
		Lucknow: "🏖️",
		Goa: "🌇",
	};

	const [filteredCities, setFilteredCities] = useState(allCities);
	const [searchCity, setSearchCity] = useState("");
	const [cityModalOpen, setCityModalOpen] = useState(false);

	// ---------------- CREATE MODAL ----------------
	const [createOpen, setCreateOpen] = useState(false);
	const [createType, setCreateType] = useState("");

	const openCreate = (type) => {
		setCreateType(type);
		setCreateOpen(true);
	};

	// ---------------- SIDEBAR ----------------
	const [sidebarOpen, setSidebarOpen] = useState(false);

	// ---------------- LOGOUT POPUP ----------------
	const [logoutOpen, setLogoutOpen] = useState(false);
	const logoutCancelRef = useRef();

	// ---------------- SEARCH BAR ----------------
	const [searchText, setSearchText] = useState("");
	const [showPanel, setShowPanel] = useState(false);
	const [results, setResults] = useState({ movies: [], events: [], sports: [] });
	const [cityEventIds, setCityEventIds] = useState(new Set());
	const panelRef = useRef(null);

	const SidebarNavItem = ({ to, disabled, children }) => {
		if (disabled) {
			return (
				<Text as="span" color="gray.500" opacity={0.6} cursor="not-allowed">
					{children}
				</Text>
			);
		}
		return (
			<Link to={to} onClick={() => setSidebarOpen(false)}>
				{children}
			</Link>
		);
	};

	useEffect(() => {
		if (!searchCity.trim()) {
			setFilteredCities(allCities);
			return;
		}

		setFilteredCities(
			allCities.filter((c) =>
				c.toLowerCase().includes(searchCity.toLowerCase())
			)
		);
	}, [searchCity]);

	
	// load event slots for city
	useEffect(() => {
		const cityName = storedCity?.city || userCity;
		if (!cityName) return;

		const load = async () => {
			try {
				const res = await axios.get("/api/v1/eventslot", {
					params: { city: cityName },
				});
				setCityEventIds(new Set(res.data?.map((s) => s.eventId)));
			} catch {
				setCityEventIds(new Set());
			}
		};

		load();
	}, [storedCity, userCity]);

	useEffect(() => {
		const handler = (e) => {
			if (panelRef.current && !panelRef.current.contains(e.target))
				setShowPanel(false);
		};

		document.addEventListener("mousedown", handler);
		return () => document.removeEventListener("mousedown", handler);
	}, []);

	useEffect(() => {
		if (!searchText.trim()) {
			setResults({ movies: [], events: [], sports: [] });
			return;
		}

		const timeout = setTimeout(fetchSearch, 300);
		return () => clearTimeout(timeout);
	}, [searchText, cityEventIds]);

	const fetchSearch = async () => {
		const cityName = storedCity?.city || userCity;
		if (!cityName) return;

		try {
			const res = await searchByTitle(cityName, searchText);
			let data = res.data || [];

			data = data.filter((e) => cityEventIds.has(e.eventId));

			setResults({
				movies: data.filter((e) => e.category === "MOVIE"),
				events: data.filter((e) => e.category === "EVENT"),
				sports: data.filter((e) => e.category === "SPORT"),
			});
		} catch {
			setResults({ movies: [], events: [], sports: [] });
		}
	};

	useEffect(() => {
		setShowPanel(false);

		if (location.pathname === "/") {
			setSearchText("");
			setResults({ movies: [], events: [], sports: [] });
		}
	}, [location.pathname]);

	const handleLogout = () => {
		dispatch(logoutUser());
		persistor.purge();
		navigate("/login");
		setLogoutOpen(false);
		setShowPanel(false);
		setSidebarOpen(false);
		toast.success("Logged out.");
	};

	const bookingsDisabled = !isLoggedIn || isAdmin;
	const profileDisabled = !isLoggedIn;

	return (
		<>
			{/* TOP NAVBAR */}
			<Box
				bg="linear-gradient(180deg,#0b1320 0%,#071018 100%)"
				color="gray.200"
				px={6}
				py={3}
				boxShadow="lg"
				position="relative"
				zIndex={1000}
			>
				<div className="flex items-center justify-between">
					<Link to="/">
						<img
							src="/Logo-final-2.png"
							alt="Vistaro"
							style={{ height: "100px", width: "150px", objectFit: "contain" }}
						/>
					</Link>

					<div className="hidden md:flex gap-6 text-gray-300 font-medium">
						<Link to="/">Home</Link>
						<Link to="/events">Events</Link>
						<Link to="/sports">Sports</Link>
						<Link to="/movies">Movies</Link>
						<Link to="/offers">Offers</Link>
						<Link to="/about">About</Link>
					</div>

					{/* SEARCH BAR */}
					<Box flex="1" maxW="500px" mx={6} display={{ base: "none", md: "block" }}>
						<InputGroup>
							<InputLeftElement pointerEvents="none">
								<FiSearch color="gray.400" />
							</InputLeftElement>

							<Input
								placeholder="Search for movies, events, sports…"
								bg="gray.800"
								border="1px solid #2D3748"
								color="gray.200"
								value={searchText}
								onChange={(e) => {
									setSearchText(e.target.value);
									setShowPanel(true);
								}}
								onFocus={() => setShowPanel(true)}
							/>
						</InputGroup>
					</Box>

					{/* RIGHT SIDE */}
					<HStack spacing={3}>
						{/* CITY SELECTOR */}
						<Button
							size="sm"
							bg="#2bb0a0"
							color="white"
							onClick={() => setCityModalOpen(true)}
							_hover={{ bg: "#239485" }}
						>
							<HStack spacing={2}>
								<Text fontSize="sm">{currentCityName}</Text>
								<IoLocationSharp />
							</HStack>
						</Button>

						{/* CREATE EVENT (ADMIN ONLY) */}
						{isAdmin && (
							<Button
								size="sm"
								bg="gray.800"
								color="white"
								_hover={{ bg: "gray.600" }}
								onClick={() => setCreateOpen(true)}
							>
								Create Event
							</Button>
						)}

						{!isLoggedIn && (
							<Link to="/login">
								<Button size="sm" bg="white" color="black">Sign In</Button>
							</Link>
						)}

						<Button
							bg="gray.700"
							color="white"
							onClick={() => setSidebarOpen(true)}
							_hover={{ bg: "gray.600" }}
						>
							<FiMenu size={20} />
						</Button>
					</HStack>
				</div>
			</Box>

			{/* SEARCH RESULTS PANEL */}
			{showPanel && searchText.trim() && (
				<Box
					ref={panelRef}
					position="absolute"
					top="80px"
					width="100%"
					bg="gray.900"
					color="gray.200"
					p={8}
					boxShadow="0 12px 50px rgba(0,0,0,0.6)"
					zIndex={1500}
				>
					<Text fontSize="xl" fontWeight="bold">
						Search results for "{searchText}"
					</Text>
					<Divider my={4} />

					<HStack align="start" spacing={10}>
						<Box flex="1">
							<Text fontWeight="bold">Movies</Text>
							<VStack align="start" mt={3}>
								{results.movies.map((m) => (
									<HStack
										key={m.eventId}
										cursor="pointer"
										onClick={() => navigate(`/event/${m.eventId}`)}
									>
										<img src={m.thumbnailUrl} style={{ width: 50, height: 50 }} />
										<Text>{m.title}</Text>
									</HStack>
								))}
							</VStack>
						</Box>

						<Box flex="1">
							<Text fontWeight="bold">Events</Text>
							<VStack align="start" mt={3}>
								{results.events.map((e) => (
									<HStack
										key={e.eventId}
										cursor="pointer"
										onClick={() => navigate(`/event/${e.eventId}`)}
									>
										<img src={e.thumbnailUrl} style={{ width: 50, height: 50 }} />
										<Text>{e.title}</Text>
									</HStack>
								))}
							</VStack>
						</Box>

						<Box flex="1">
							<Text fontWeight="bold">Sports</Text>
							<VStack align="start" mt={3}>
								{results.sports.map((s) => (
									<HStack
										key={s.eventId}
										cursor="pointer"
										onClick={() => navigate(`/event/${s.eventId}`)}
									>
										<img src={s.thumbnailUrl} style={{ width: 50, height: 50 }} />
										<Text>{s.title}</Text>
									</HStack>
								))}
							</VStack>
						</Box>
					</HStack>
				</Box>
			)}

			{/* CITY MODAL */}
			<Modal isOpen={cityModalOpen} onClose={() => setCityModalOpen(false)} size="5xl" isCentered>
				<ModalOverlay />
				<ModalContent bg="#161b22" color="white" p={6} borderRadius="xl">
					<Text fontSize="2xl" fontWeight="bold" mb={4}>
						Select your city
					</Text>

					{/* CITY SEARCH BAR */}
					<Input
						placeholder="Search city"
						bg="#0d1117"
						border="1px solid #30363d"
						color="white"
						mb={5}
						value={searchCity}
						onChange={(e) => setSearchCity(e.target.value)}
					/>

					<Divider mb={4} borderColor="#30363d" />

					<SimpleGrid columns={4} spacing={6}>
						{filteredCities.map((city) => (
							<Box
								key={city}
								p={5}
								border="1px solid #30363d"
								bg="#0d1117"
								borderRadius="lg"
								textAlign="center"
								cursor="pointer"
								_hover={{
									boxShadow: "0px 0px 12px rgba(43,176,160,0.5)",
									borderColor: "#2BB0A0",
									transform: "scale(1.05)",
								}}
								transition="0.2s"
								onClick={() => {
									dispatch(setCity({ id: null, city }));
									setCityModalOpen(false);
									setSearchCity("");
								}}
							>
								<Text fontSize="3xl" mb={2}>
									{cityEmojis[city] || "📍"}
								</Text>

								<Text fontSize="lg" fontWeight="600" color="#2BB0A0">
									{city}
								</Text>
							</Box>
						))}
					</SimpleGrid>
				</ModalContent>
			</Modal>


			{/* ADMIN CREATE FLOW */}
			<Modal
				isOpen={createOpen}
				onClose={() => setCreateOpen(false)}
				size="5xl"
				isCentered
			>
				<ModalOverlay />
				<ModalContent bg="gray.900" color="white">
					<AdminCreateFlow
						isOpen={createOpen}
						onClose={() => setCreateOpen(false)}
						selectedCity={currentCityName}
					/>
				</ModalContent>
			</Modal>

			{/* SIDEBAR */}
			<Drawer isOpen={sidebarOpen} placement="right" onClose={() => setSidebarOpen(false)}>
				<DrawerOverlay />

				<DrawerContent
					bg="#0b1320"
					color="white"
					borderLeft="1px solid #1e293b"
				>
					<DrawerCloseButton />

					<DrawerBody mt={10}>
						{/* Logo */}
						<Center>
							<img
								src="/Logo-final-2.png"
								style={{ width: "130px", marginBottom: "20px" }}
							/>
						</Center>

						{/* User Details */}
						<VStack spacing={1} mb={4}>
							<Text fontSize="xl" fontWeight="bold">
								{isLoggedIn ? user.name : "Guest"}
							</Text>
							<Text fontSize="sm" color="gray.400">
								{isLoggedIn ? user.email : ""}
							</Text>
						</VStack>

						<Divider borderColor="gray.600" />

						{/* Navigation Links */}
						<VStack align="stretch" spacing={0} mt={4} fontSize="lg">
							{/* Events */}
							<Link to="/events" onClick={() => setSidebarOpen(false)}>
								<Box py={3} px={1} _hover={{ bg: "gray.700" }}>Events</Box>
							</Link>
							<Divider borderColor="gray.700" />

							{/* Sports */}
							<Link to="/sports" onClick={() => setSidebarOpen(false)}>
								<Box py={3} px={1} _hover={{ bg: "gray.700" }}>Sports</Box>
							</Link>
							<Divider borderColor="gray.700" />

							{/* Movies */}
							<Link to="/movies" onClick={() => setSidebarOpen(false)}>
								<Box py={3} px={1} _hover={{ bg: "gray.700" }}>Movies</Box>
							</Link>
							<Divider borderColor="gray.700" />

							{/* About */}
							<Link to="/about" onClick={() => setSidebarOpen(false)}>
								<Box py={3} px={1} _hover={{ bg: "gray.700" }}>About</Box>
							</Link>
							<Divider borderColor="gray.700" />

							{/* Create Event or Admin Listing */}
							<SidebarNavItem to="/my-events" disabled={!isLoggedIn}>
								<Box py={3} _hover={{ bg: "gray.700" }}>
									{isAdmin ? "User Listings" : "Create Your Event"}
								</Box>
							</SidebarNavItem>
							<Divider borderColor="gray.700" />

							{/* Bookings */}
							<SidebarNavItem to="/bookings" disabled={bookingsDisabled}>
								<Box py={3} _hover={{ bg: "gray.700" }}>View Your Bookings</Box>
							</SidebarNavItem>
							<Divider borderColor="gray.700" />

							{/* Profile */}
							<SidebarNavItem to="/profile/edit" disabled={profileDisabled}>
								<Box py={3} _hover={{ bg: "gray.700" }}>Update Profile</Box>
							</SidebarNavItem>

						</VStack>

						<Divider borderColor="gray.600" my={6} />

						{/* Login / Logout Button */}
						{!isLoggedIn ? (
							<Link to="/login" onClick={() => setSidebarOpen(false)}>
								<Button
									width="100%"
									bg="teal.500"
									color="white"
									_hover={{ bg: "teal.600" }}
								>
									Sign In
								</Button>
							</Link>
						) : (
							<Button
								width="100%"
								bg="red.500"
								color="white"
								_hover={{ bg: "red.600" }}
								onClick={() => setLogoutOpen(true)}
							>
								Logout
							</Button>
						)}
					</DrawerBody>
				</DrawerContent>
			</Drawer>


			{/* LOGOUT POPUP */}
			<AlertDialog
				isOpen={logoutOpen}
				leastDestructiveRef={logoutCancelRef}
				onClose={() => setLogoutOpen(false)}
			>
				<AlertDialogOverlay>
					<AlertDialogContent bg="gray.800" color="white">
						<AlertDialogHeader fontSize="lg" fontWeight="bold">
							Do you want to logout?
						</AlertDialogHeader>

						<AlertDialogBody>
							You will need to sign in again to access your account.
						</AlertDialogBody>

						<AlertDialogFooter>
							<Button ref={logoutCancelRef} onClick={() => setLogoutOpen(false)}>
								No
							</Button>
							<Button colorScheme="red" ml={3} onClick={handleLogout}>
								Yes
							</Button>
						</AlertDialogFooter>
					</AlertDialogContent>
				</AlertDialogOverlay>
			</AlertDialog>
		</>
	);
}