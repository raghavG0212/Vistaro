import React, { useEffect, useState } from "react";
import {
  Box,
  Text,
  Grid,
  Center,
  HStack,
  Select,
  Skeleton,
} from "@chakra-ui/react";
import { useSelector } from "react-redux";
import { useNavigate, useLocation } from "react-router-dom";
import HomeMainSlider from "../components/HomeMainSlider";
import EventCard from "../components/EventCard";
import { searchByCategory } from "../apis/eventApi";
import HeadingComponent from "../components/HeadingComponent";
import Pagination from "../components/Pagination";

export default function Events() {
  const navigate = useNavigate();
  const location = useLocation();
  const city = useSelector((s) => s.city?.selectedCity?.city || "");

  const [events, setEvents] = useState([]);
  const [filtered, setFiltered] = useState([]);
  const [loading, setLoading] = useState(true);
  const [typeFilter, setTypeFilter] = useState("");

  const ITEMS_PER_PAGE = 10;
  const [page, setPage] = useState(1);

  useEffect(() => {
    window.scrollTo(0, 0);
  }, []);

  // ⭐ Read filter from URL
  useEffect(() => {
    const params = new URLSearchParams(location.search);
    const type = params.get("type"); // ex: standup

    if (type) {
      const formatted =
        type.charAt(0).toUpperCase() + type.slice(1).toLowerCase();

      setTypeFilter(formatted); // ex: Standup
    }
  }, [location.search]);

  // Load events
  useEffect(() => {
    if (!city) return;
    setLoading(true);

    searchByCategory(city, "EVENT")
      .then((res) => {
        setEvents(res.data || []);
        setFiltered(res.data || []);
      })
      .finally(() => setLoading(false));
  }, [city]);

  // Apply filter
  useEffect(() => {
    let arr = [...events];

    if (typeFilter) {
      const filteredArr = arr.filter((e) =>
        (e.subCategory || "").toLowerCase().includes(typeFilter.toLowerCase())
      );

      // ⭐ FALLBACK → If no events match, show all instead
      if (filteredArr.length === 0) {
        setFiltered(arr);
      } else {
        setFiltered(filteredArr);
      }
    } else {
      setFiltered(arr);
    }

    setPage(1);
  }, [typeFilter, events]);


  const totalPages = Math.ceil(filtered.length / ITEMS_PER_PAGE);
  const currentItems = filtered.slice(
    (page - 1) * ITEMS_PER_PAGE,
    page * ITEMS_PER_PAGE
  );

  return (
    <Box bg="gray.900" color="white" px={6} py={8} minH="100vh">
      <Box minH="320px">
        <HomeMainSlider />
      </Box>

      <HeadingComponent mb={2}>Events in {city}</HeadingComponent>

      <Text color="gray.400" mb={6}>
        Concerts, Standup Comedy & More
      </Text>

      {/* FILTER BAR */}
      <HStack spacing={4} mb={6} align="center">
        <Select
          placeholder="Filter by Type"
          bg="gray.800"
          w="220px"
          color="white"
          _hover={{ borderColor: "teal.300" }}
          _focus={{ borderColor: "teal.300", boxShadow: "0 0 0 1px teal" }}
          sx={{
            option: {
              background: "#1A202C", // Dark dropdown bg
              color: "white", // White text 
              padding: "10px",
            },
          }}
          value={typeFilter}
          onChange={(e) => setTypeFilter(e.target.value)}
        >
          <option value="Concert">Concert</option>
          <option value="Standup">Standup Comedy</option>
          <option value="Fashion">Fashion</option>
          <option value="Food Fest">Food Fest</option>
          <option value="Exhibition">Exhibition</option>
        </Select>

        <Box
          as="button"
          onClick={() => {
            setTypeFilter("");
            setPage(1);
            navigate("/events");
          }}
          bg="red.400"
          color="white"
          px={4}
          py={2}
          rounded="full"
        >
          Clear Filters
        </Box>

        <Text fontSize="xs" color="gray.500">
          {filtered.length} results
        </Text>
      </HStack>

      {loading ? (
        <Grid templateColumns="repeat(auto-fill, minmax(260px, 1fr))" gap={6}>
          {[...Array(6)].map((_, i) => (
            <Skeleton key={i} height="360px" borderRadius="lg" />
          ))}
        </Grid>
      ) : currentItems.length === 0 ? (
        <Center py={20}>
          <Text>No events available.</Text>
        </Center>
      ) : (
        <>
          <Grid templateColumns="repeat(auto-fill, minmax(260px, 1fr))" gap={2}>
            {currentItems.map((e) => (
              <EventCard key={e.eventId} event={e} />
            ))}
          </Grid>

          <Pagination page={page} totalPages={totalPages} onChange={setPage} />
        </>
      )}
    </Box>
  );
}
