import React, { useEffect, useState, useCallback } from "react";
import {
  Box,
  Text,
  Grid,
  Select,
  VStack,
  Spinner,
} from "@chakra-ui/react";
import axios from "axios";
import { useSelector } from "react-redux";
import HomeMainSlider from "../components/HomeMainSlider";
import EventCard from "../components/EventCard";
import { searchByCategory } from "../apis/eventApi";
import HeadingComponent from "../components/HeadingComponent";
import Pagination from "../components/Pagination";
import Loader from "../components/Loader";

const SLOT_BASE = "/api/v1/eventslot/search/city";

const MANUAL_LANGUAGES = [
  "English",
  "Hindi",
  "Punjabi",
  "Gujarati",
  "Bengali",
  "Tamil",
  "Telugu",
  "Kannada",
];

export default function Movies() {
  const city = useSelector((s) => s.city?.selectedCity?.city || "");

  const [movies, setMovies] = useState([]);
  const [display, setDisplay] = useState([]);
  const [slotLangMap, setSlotLangMap] = useState({});
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const [languageFilter, setLanguageFilter] = useState("");
  const [genreFilter, setGenreFilter] = useState("");

  // PAGINATION
  const [page, setPage] = useState(1);
  const ITEMS_PER_PAGE = 10;

  useEffect(() => {
    window.scrollTo(0, 0);
  }, []);

  const loadData = useCallback(async (cityName) => {
    setLoading(true);
    setError(null);
    setMovies([]);
    setDisplay([]);
    setSlotLangMap({});

    try {
      const [moviesRes, slotsRes] = await Promise.all([
        searchByCategory(cityName, "MOVIE"),
        axios.get(SLOT_BASE, { params: { city: cityName } }),
      ]);

      const moviesData = Array.isArray(moviesRes.data) ? moviesRes.data : [];
      const slotsData = Array.isArray(slotsRes.data) ? slotsRes.data : [];

      const map = {};
      slotsData.forEach((s) => {
        if (!s || s.eventId == null) return;
        if (!map[s.eventId]) map[s.eventId] = new Set();
        (s.language || "")
          .split(",")
          .map((t) => t.trim())
          .filter(Boolean)
          .forEach((t) => map[s.eventId].add(t.toLowerCase()));
      });

      const finalLower = {};
      Object.keys(map).forEach((id) => {
        finalLower[id] = Array.from(map[id]).join(", ");
      });

      setSlotLangMap(finalLower);

      const filteredMovies = moviesData.filter((m) => m && m.eventId != null);
      setMovies(filteredMovies);

      const onlyWithSlots = filteredMovies.filter((m) => finalLower[m.eventId]);
      setDisplay(onlyWithSlots);
    } catch (err) {
      console.error("Movies load error:", err);
      setError(err?.message || "Failed to load data");
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    if (!city) return;
    loadData(city);
  }, [city, loadData]);

  useEffect(() => {
    if (!movies.length) return;

    let arr = movies.filter((m) => slotLangMap[m.eventId]);

    if (genreFilter)
      arr = arr.filter((m) =>
        (m.subCategory || "").toLowerCase().includes(genreFilter.toLowerCase())
      );

    if (languageFilter)
      arr = arr.filter((m) => {
        const langs = (slotLangMap[m.eventId] || "").split(",").map((t) => t.trim());
        return langs.includes(languageFilter.toLowerCase());
      });

    setDisplay(arr);
    setPage(1); // reset pagination on filter change
  }, [movies, slotLangMap, genreFilter, languageFilter]);

  const totalPages = Math.ceil(display.length / ITEMS_PER_PAGE);
  const currentItems = display.slice((page - 1) * ITEMS_PER_PAGE, page * ITEMS_PER_PAGE);

  if (loading)
    return (
      <Loader/>
    );

  if (error)
    return (
      <Box bg="gray.900" color="white" px={6} py={8} minH="100vh">
        <Box minH="320px"><HomeMainSlider /></Box>
        <HeadingComponent mb={2}>{`Movies in ${city}`}</HeadingComponent>
        <Box color="red.300" p={4}>Error: {error}</Box>
      </Box>
    );

  return (
    <Box bg="gray.900" color="white" px={6} py={8} minH="100vh">
      <Box minH="320px"><HomeMainSlider /></Box>

      <HeadingComponent mb={2}>{`Movies in ${city}`}</HeadingComponent>
      <Text color="gray.400" mb={6}>Now Showing</Text>

      {/* FILTERS */}
      <Box display="flex" gap={4} mb={6}>
        <Select
          placeholder="Filter by Language"
          bg="gray.800"
          color="white"
          w="220px"
          _hover={{ borderColor: "teal.300" }}
          _focus={{ borderColor: "teal.300", boxShadow: "0 0 0 1px teal" }}
          sx={{
            option: {
              background: "#1A202C", // Dark dropdown bg
              color: "white", // White text 
              padding: "10px",
            },
          }}
          value={languageFilter}
          onChange={(e) => setLanguageFilter(e.target.value)}
        >
          <option value="">All</option>
          {MANUAL_LANGUAGES.map((lng) => (
            <option key={lng} value={lng.toLowerCase()}>{lng}</option>
          ))}
        </Select>

        <Select
          placeholder="Filter by Genre"
          bg="gray.800"
          color="white"
          w="220px"
          _hover={{ borderColor: "teal.300" }}
          _focus={{ borderColor: "teal.300", boxShadow: "0 0 0 1px teal" }}
          sx={{
            option: {
              background: "#1A202C", // Dark dropdown bg
              color: "white", // White text 
              padding: "10px",
            },
          }}
          value={genreFilter}
          onChange={(e) => setGenreFilter(e.target.value)}
        >
          <option value="">All</option>
          <option value="Action">Action</option>
          <option value="Comedy">Comedy</option>
          <option value="Thriller">Thriller</option>
          <option value="Romance">Romance</option>
          <option value="Drama">Drama</option>
          <option value="Animation">Animation</option>
          <option value="Horror">Horror</option>
          <option value="Sci-Fi">Sci-Fi</option>
          <option value="Superhero">Superhero</option>
        </Select>

        <Box
          as="button"
          onClick={() => {
            setLanguageFilter("");
            setGenreFilter("");
            setPage(1);
          }}
          bg="red.400"
          color="white"
          px={4}
          py={2}
          rounded="full"
          fontSize="sm"
          fontWeight="semibold"
          height="40px"
          display="flex"
          alignItems="center"
          _hover={{ bg: "red.500" }}
          _active={{ bg: "red.600" }}
        >
          Clear Filters
        </Box>

        <Text fontSize="xs" color="gray.500">{display.length} results</Text>

      </Box>

      {/* MOVIES GRID */}
      <Grid templateColumns="repeat(auto-fill, minmax(260px, 1fr))" gap={2}>
        {currentItems.map((m) => (
          <EventCard key={m.eventId} event={m} />
        ))}
      </Grid>

      <Pagination page={page} totalPages={totalPages} onChange={setPage} />
    </Box>
  );
}

function CenterFallback() {
  return (
    <Box py={12} textAlign="center">
      <Spinner color="teal.300" size="lg" />
      <Text mt={3} color="gray.300">Loading movies...</Text>
    </Box>
  );
}
