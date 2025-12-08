import React, { useEffect, useState } from "react";
import {
  Box,
  Text,
  Grid,
  Select,
  HStack,
  Center,
  Skeleton,
} from "@chakra-ui/react";
import { useSelector } from "react-redux";
import HomeMainSlider from "../components/HomeMainSlider";
import { searchByCategory, searchBySubCategory } from "../apis/eventApi";
import EventCard from "../components/EventCard";
import HeadingComponent from "../components/HeadingComponent";
import Pagination from "../components/Pagination";

export default function Sports() {
  const city = useSelector((s) => s.city?.selectedCity?.city || "");

  const [sports, setSports] = useState([]);
  const [loading, setLoading] = useState(true);
  const [type, setType] = useState("");

  // PAGINATION
  const [page, setPage] = useState(1);
  const ITEMS_PER_PAGE = 10;

  useEffect(() => {
    window.scrollTo(0, 0);
  }, []);

  const fetchSports = () => {
    setLoading(true);
    const apiCall = type
      ? searchBySubCategory(city, type)
      : searchByCategory(city, "SPORT");

    apiCall.then((res) => setSports(res.data || [])).finally(() => setLoading(false));
  };

  useEffect(() => {
    if (!city) return;
    fetchSports();
  }, [city, type]);

  useEffect(() => {
    setPage(1); // reset on filter
  }, [type]);

  const totalPages = Math.ceil(sports.length / ITEMS_PER_PAGE);
  const currentItems = sports.slice((page - 1) * ITEMS_PER_PAGE, page * ITEMS_PER_PAGE);

  return (
    <Box bg="gray.900" color="white" px={6} py={8} minH="100vh">
      <Box minH="320px"><HomeMainSlider /></Box>

      <HeadingComponent mb={2}>Sports in {city}</HeadingComponent>
      <Text color="gray.400" mb={6}>Watch your favorite sports live.</Text>

      <HStack spacing={4} mb={6} align="center">
        <Select
          placeholder="Filter by Type"
          value={type}
          onChange={(e) => setType(e.target.value)}
          bg="gray.800"
          color="white"
          w="220px"
          _hover={{ borderColor: "teal.300" }}
          _focus={{ borderColor: "teal.300", boxShadow: "0 0 0 1px teal" }}
          sx={{
            option: {
              background: "#1A202C",
              color: "white",
              padding: "10px",
            },
          }}
        >
          <option value="Cricket">Cricket</option>
          <option value="Football">Football</option>
          <option value="Hockey">Hockey</option>
          <option value="Kabaddi">Kabaddi</option>
          <option value="Tennis">Tennis</option>
          <option value="Badminton">Badminton</option>
        </Select>

        {/* CLEAR FILTER BUTTON - RED */}
        <Box
          as="button"
          onClick={() => {
            setType("");
            setPage(1);
          }}
          bg="red.400"
          color="white"
          px={4}
          py={2}
          rounded="full"
          fontSize="sm"
          fontWeight="semibold"
          _hover={{ bg: "red.500" }}
          _active={{ bg: "red.600" }}
        >
          Clear Filters
        </Box>

        <Text fontSize="xs" color="gray.500">
          {sports.length} results
        </Text>
      </HStack>


      {loading ? (
        <Grid templateColumns="repeat(auto-fill, minmax(260px, 1fr))" gap={6}>
          {[...Array(6)].map((_, i) => (
            <Skeleton key={i} height="360px" borderRadius="lg" />
          ))}
        </Grid>
      ) : currentItems.length === 0 ? (
        <Center py={20}><Text>No sports events available.</Text></Center>
      ) : (
        <>
          <Grid templateColumns="repeat(auto-fill, minmax(260px, 1fr))" gap={2}>
            {currentItems.map((s) => (
              <EventCard key={s.eventId} event={s} />
            ))}
          </Grid>

          <Pagination page={page} totalPages={totalPages} onChange={setPage} />
        </>
      )}
    </Box>
  );
}
