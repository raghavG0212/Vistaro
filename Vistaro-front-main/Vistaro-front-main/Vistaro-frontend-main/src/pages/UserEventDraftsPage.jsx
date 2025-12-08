import React, { useEffect, useMemo, useRef, useState } from "react";
import {
  Box,
  Heading,
  Text,
  Tabs,
  TabList,
  TabPanels,
  Tab,
  TabPanel,
  HStack,
  VStack,
  Badge,
  Tag,
  TagLabel,
  Button,
  IconButton,
  SimpleGrid,
  Skeleton,
  SkeletonText,
  useDisclosure,
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
  Textarea,
  Select,
  Divider,
  AlertDialog,
  AlertDialogOverlay,
  AlertDialogContent,
  AlertDialogHeader,
  AlertDialogBody,
  AlertDialogFooter,
} from "@chakra-ui/react";
import { useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";
import {
  FiClock,
  FiMapPin,
  FiCalendar,
  FiChevronRight,
  FiTrash2,
  FiCheckCircle,
  FiXCircle,
  FiPlus,
} from "react-icons/fi";
import { toast } from "react-toastify";

import { getAllVenues } from "../apis/venueApi";
import {
  approveDraft,
  createUserEventDraft,
  deleteDraft,
  getDraftsByStatus,
  getDraftsByUser,
  rejectDraft,
} from "../apis/UserListEventApi";
import axios from "axios";
// --------- small helpers ----------

const toDate = (value) => {
  if (!value) return null;
  try {
    return new Date(value);
  } catch {
    return null;
  }
};

const formatDateTime = (iso) => {
  const d = toDate(iso);
  if (!d) return "";
  return d.toLocaleString("en-IN", {
    day: "2-digit",
    month: "short",
    year: "numeric",
    hour: "2-digit",
    minute: "2-digit",
  });
};

const formatDate = (iso) => {
  const d = toDate(iso);
  if (!d) return "";
  return d.toLocaleDateString("en-IN", {
    day: "2-digit",
    month: "short",
    year: "numeric",
  });
};

const formatTime = (iso) => {
  const d = toDate(iso);
  if (!d) return "";
  return d.toLocaleTimeString("en-IN", {
    hour: "2-digit",
    minute: "2-digit",
  });
};

const statusColor = (status) => {
  switch (status) {
    case "PENDING":
      return "yellow";
    case "APPROVED":
      return "green";
    case "REJECTED":
      return "red";
    default:
      return "gray";
  }
};

// --------- CREATE DRAFT MODAL (USER) ----------

function CreateDraftModal({
  isOpen,
  onClose,
  onCreated,
  venues,
  selectedCityName,
  userId,
}) {
  const [form, setForm] = useState({
    title: "",
    description: "",
    subCategory: "",
    bannerUrl: "",
    thumbnailUrl: "",
    artist: "",
    host: "",
    genre: "",
    eventStart: "",
    eventEnd: "",
    venueId: "",
    slotStart: "",
    slotEnd: "",
    basePrice: "",
  });

  const [thumbnailFile, setThumbnailFile] = useState(null);
  const [bannerFile, setBannerFile] = useState(null);

  const [thumbnailPreview, setThumbnailPreview] = useState("");
  const [bannerPreview, setBannerPreview] = useState("");
  const thumbnailRef = useRef();
  const bannerRef = useRef();

  const handleImageChange = (e, type) => {
    const file = e.target.files[0];
    if (!file) return;

    const reader = new FileReader();
    reader.onload = () => {
      if (type === "thumbnail") {
        setThumbnailFile(file);
        setThumbnailPreview(reader.result);
        update("thumbnailUrl", reader.result); // Base64 to backend
      } else {
        setBannerFile(file);
        setBannerPreview(reader.result);
        update("bannerUrl", reader.result); // Base64 to backend
      }
    };

    reader.readAsDataURL(file); // convert file → base64
  };

  const [submitting, setSubmitting] = useState(false);

  const uploadImage = async (file) => {
    const formData = new FormData();
    formData.append("file", file);

    try {
      const res = await axios.post(
        "http://localhost:9090/api/v1/upload/image",
        formData,
        { headers: { "Content-Type": "multipart/form-data" } }
      );

      return res.data.url;
    } catch (err) {
      console.error("Upload error", err);
      toast.error("Image upload failed");
      return null;
    }
  };

  useEffect(() => {
    if (!isOpen) {
      setForm({
        title: "",
        description: "",
        subCategory: "",
        bannerUrl: "",
        thumbnailUrl: "",
        artist: "",
        host: "",
        genre: "",
        eventStart: "",
        eventEnd: "",
        venueId: "",
        slotStart: "",
        slotEnd: "",
        basePrice: "",
      });
    }
  }, [isOpen]);

  const update = (field, value) => {
    setForm((prev) => {
      const next = { ...prev, [field]: value };
      if (field === "eventStart") {
        next.slotStart = value;
      }
      if (field === "eventEnd") {
        next.slotEnd = value;
      }
      return next;
    });
  };

  const handleSubmit = async () => {
    if (!userId) {
      toast.error("User not found. Please login again.");
      return;
    }

    if (
      !form.title.trim() ||
      !form.description.trim() ||
      !form.eventStart ||
      !form.eventEnd ||
      !form.venueId ||
      !form.basePrice
    ) {
      toast.warn("Please fill title, description, time, venue and price.");
      return;
    }

    if (new Date(form.eventEnd) <= new Date(form.eventStart)) {
      toast.warn("Event end time must be after start time.");
      return;
    }

    setSubmitting(true);
    try {
      const payload = {
        userId: userId,
        title: form.title.trim(),
        description: form.description.trim(),
        subCategory: form.subCategory || null,
        bannerUrl: form.bannerUrl || null,
        thumbnailUrl: form.thumbnailUrl || null,
        artist: form.artist || null,
        host: form.host || null,
        genre: form.genre || null,
        eventStart: form.eventStart,
        eventEnd: form.eventEnd,
        venueId: Number(form.venueId),
        slotStart: form.slotStart || form.eventStart,
        slotEnd: form.slotEnd || form.eventEnd,
        basePrice: Number(form.basePrice),
      };

      await createUserEventDraft(payload);
      toast.success("Your event listing request has been submitted!");
      onClose();
      onCreated && onCreated();
    } catch (err) {
      console.error(err);
      const msg =
        err?.response?.data?.message ||
        err?.response?.data ||
        "Failed to create event draft.";
      toast.error(msg);
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <Modal
      isOpen={isOpen}
      onClose={onClose}
      size="4xl"
      isCentered
      scrollBehavior="inside"
    >
      <ModalOverlay />
      <ModalContent bg="gray.900" color="gray.100">
        <ModalHeader>Create Your Event Listing</ModalHeader>
        <ModalCloseButton />
        <ModalBody>
          <Text fontSize="sm" color="gray.400" mb={4}>
            Events created here will be sent to our team for review. Once
            approved, they’ll appear on the main listing as a public event.
          </Text>

          <VStack spacing={4} align="stretch">
            <FormControl isRequired>
              <FormLabel>Title</FormLabel>
              <Input
                bg="gray.800"
                value={form.title}
                onChange={(e) => update("title", e.target.value)}
                placeholder="Eg. Indie Music Night"
              />
            </FormControl>

            <FormControl isRequired>
              <FormLabel>Description</FormLabel>
              <Textarea
                bg="gray.800"
                value={form.description}
                onChange={(e) => update("description", e.target.value)}
                placeholder="Tell people what your event is about..."
              />
            </FormControl>

            <HStack spacing={4} flexWrap="wrap">
              <FormControl>
                <FormLabel>Sub Category</FormLabel>
                <Input
                  bg="gray.800"
                  value={form.subCategory}
                  onChange={(e) => update("subCategory", e.target.value)}
                  placeholder="Music, Workshop, Comedy..."
                />
              </FormControl>
              <FormControl>
                <FormLabel>Genre</FormLabel>
                <Input
                  bg="gray.800"
                  value={form.genre}
                  onChange={(e) => update("genre", e.target.value)}
                  placeholder="Rock, Standup, EDM..."
                />
              </FormControl>
            </HStack>

            <HStack spacing={4} flexWrap="wrap">
              <FormControl>
                <FormLabel>Artist</FormLabel>
                <Input
                  bg="gray.800"
                  value={form.artist}
                  onChange={(e) => update("artist", e.target.value)}
                  placeholder="Main performer"
                />
              </FormControl>
              <FormControl>
                <FormLabel>Host</FormLabel>
                <Input
                  bg="gray.800"
                  value={form.host}
                  onChange={(e) => update("host", e.target.value)}
                  placeholder="Organizer / Host"
                />
              </FormControl>
            </HStack>

            <HStack spacing={4} flexWrap="wrap">
              {/* Thumbnail Upload */}
              <FormControl>
                <FormLabel>Thumbnail Image</FormLabel>

                <Box
                  border="2px dashed #4B5563"
                  p={4}
                  rounded="lg"
                  textAlign="center"
                  cursor="pointer"
                  onClick={() => thumbnailRef.current.click()}
                  _hover={{ bg: "gray.800" }}
                >
                  {form.thumbnailUrl ? (
                    <img
                      src={form.thumbnailUrl}
                      alt="thumbnail preview"
                      style={{ width: "120px", borderRadius: "8px" }}
                    />
                  ) : (
                    <Text color="gray.400">Click to upload thumbnail</Text>
                  )}

                  <Input
                    type="file"
                    accept="image/*"
                    ref={thumbnailRef}
                    display="none"
                    onChange={async (e) => {
                      const file = e.target.files?.[0];
                      if (!file) return;

                      const url = await uploadImage(file);
                      if (url) update("thumbnailUrl", url);
                    }}
                  />
                </Box>
              </FormControl>

              {/* Banner Upload */}
              <FormControl>
                <FormLabel>Banner Image</FormLabel>

                <Box
                  border="2px dashed #4B5563"
                  p={4}
                  rounded="lg"
                  textAlign="center"
                  cursor="pointer"
                  onClick={() => bannerRef.current.click()}
                  _hover={{ bg: "gray.800" }}
                >
                  {form.bannerUrl ? (
                    <img
                      src={form.bannerUrl}
                      alt="banner preview"
                      style={{
                        width: "100%",
                        maxHeight: "180px",
                        objectFit: "cover",
                        borderRadius: "8px",
                      }}
                    />
                  ) : (
                    <Text color="gray.400">Click to upload banner</Text>
                  )}

                  <Input
                    type="file"
                    accept="image/*"
                    ref={bannerRef}
                    display="none"
                    onChange={async (e) => {
                      const file = e.target.files?.[0];
                      if (!file) return;

                      const url = await uploadImage(file);
                      if (url) update("bannerUrl", url);
                    }}
                  />
                </Box>
              </FormControl>
            </HStack>

            <HStack spacing={4} flexWrap="wrap">
              <FormControl isRequired>
                <FormLabel>Event Start</FormLabel>
                <Input
                  type="datetime-local"
                  bg="gray.800"
                  value={form.eventStart}
                  onChange={(e) => update("eventStart", e.target.value)}
                />
              </FormControl>
              <FormControl isRequired>
                <FormLabel>Event End</FormLabel>
                <Input
                  type="datetime-local"
                  bg="gray.800"
                  value={form.eventEnd}
                  onChange={(e) => update("eventEnd", e.target.value)}
                />
              </FormControl>
            </HStack>

            <HStack spacing={4} flexWrap="wrap">
              <FormControl isRequired>
                <FormLabel>Venue ({selectedCityName || "city"})</FormLabel>
                <Select
                  bg="gray.800"
                  placeholder="Select venue"
                  value={form.venueId}
                  onChange={(e) => update("venueId", e.target.value)}
                >
                  {venues.map((v) => (
                    <option
                      key={v.venueId}
                      value={v.venueId}
                      style={{ background: "#111827", color: "white" }}
                    >
                      {v.name} • {v.screenName || ""} • {v.city}
                    </option>
                  ))}
                </Select>
              </FormControl>

              <FormControl isRequired>
                <FormLabel>Base Ticket Price (₹)</FormLabel>
                <Input
                  type="number"
                  min={0}
                  bg="gray.800"
                  value={form.basePrice}
                  onChange={(e) => update("basePrice", e.target.value)}
                />
              </FormControl>
            </HStack>

            <Divider borderColor="gray.700" />
            <Text fontSize="xs" color="gray.500">
              Note: Only one slot will be created for this event. Slot time
              defaults to event time, but can be changed later by admin tools.
            </Text>
          </VStack>
        </ModalBody>

        <ModalFooter>
          <Button variant="ghost" mr={3} onClick={onClose}>
            Cancel
          </Button>
          <Button
            colorScheme="teal"
            onClick={handleSubmit}
            isLoading={submitting}
          >
            Submit for Review
          </Button>
        </ModalFooter>
      </ModalContent>
    </Modal>
  );
}

// --------- MAIN PAGE ----------

export default function UserEventDraftsPage() {
  const navigate = useNavigate();
  const user = useSelector((s) => s.user);
  const cityState = useSelector((s) => s.city?.selectedCity);
  const userId = user?.userId;
  const role = user?.role;
  const isLoggedIn = !!user?.isAuthenticated;
  const isAdmin = role === "ADMIN";
  const isNormalUser = role === "USER";

  const [loading, setLoading] = useState(true);
  const [drafts, setDrafts] = useState([]);
  const [venues, setVenues] = useState([]);

  const [adminStatusTab, setAdminStatusTab] = useState(0); // 0=Pending,1=Approved,2=Rejected
  const [userStatusTab, setUserStatusTab] = useState(0);

  const {
    isOpen: isCreateOpen,
    onOpen: openCreate,
    onClose: closeCreate,
  } = useDisclosure();

  const {
    isOpen: isDeleteOpen,
    onOpen: openDelete,
    onClose: closeDelete,
  } = useDisclosure();
  const [draftToDelete, setDraftToDelete] = useState(null);
  const [deleteLoading, setDeleteLoading] = useState(false);

  const {
    isOpen: isApproveOpen,
    onOpen: openApprove,
    onClose: closeApprove,
  } = useDisclosure();
  const {
    isOpen: isRejectOpen,
    onOpen: openReject,
    onClose: closeReject,
  } = useDisclosure();
  const [selectedDraft, setSelectedDraft] = useState(null);
  const [adminComment, setAdminComment] = useState("");
  const [actionLoading, setActionLoading] = useState(false);

  const selectedCityName = cityState?.city || user?.city || "Your City";

  // venues
  useEffect(() => {
    const loadVenues = async () => {
      try {
        const res = await getAllVenues();
        setVenues(res.data || []);
      } catch (err) {
        console.error(err);
      }
    };
    loadVenues();
  }, []);

  // load user drafts
  const loadUserDrafts = async () => {
    if (!userId) {
      setDrafts([]);
      setLoading(false);
      return;
    }
    setLoading(true);
    try {
      const res = await getDraftsByUser(userId);
      setDrafts(Array.isArray(res.data) ? res.data : []);
    } catch (err) {
      console.error(err);
      toast.error("Failed to load your listings.");
    } finally {
      setLoading(false);
    }
  };

  // load admin drafts by status
  const loadAdminDrafts = async (status = "PENDING") => {
    setLoading(true);
    try {
      const res = await getDraftsByStatus(status);
      setDrafts(Array.isArray(res.data) ? res.data : []);
    } catch (err) {
      console.error(err);
      toast.error("Failed to load user event listings.");
    } finally {
      setLoading(false);
    }
  };

  // initial load
  useEffect(() => {
    if (!isLoggedIn) {
      setLoading(false);
      return;
    }
    if (isNormalUser) {
      loadUserDrafts();
    } else if (isAdmin) {
      loadAdminDrafts("PENDING");
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [isLoggedIn, isNormalUser, isAdmin, userId]);

  // group for user view
  const grouped = useMemo(() => {
    const pending = [];
    const approved = [];
    const rejected = [];

    drafts.forEach((d) => {
      if (d.approvalStatus === "APPROVED") approved.push(d);
      else if (d.approvalStatus === "REJECTED") rejected.push(d);
      else pending.push(d);
    });

    return { pending, approved, rejected };
  }, [drafts]);

  const findVenue = (venueId) =>
    venues.find((v) => v.venueId === venueId) || null;

  const renderSkeletonList = () => (
    <VStack spacing={4} align="stretch">
      {[1, 2, 3].map((i) => (
        <Box key={i} bg="gray.800" borderRadius="lg" p={4} boxShadow="md">
          <Skeleton height="22px" mb={3} />
          <SkeletonText noOfLines={3} spacing={2} />
        </Box>
      ))}
    </VStack>
  );

  const renderEmpty = (label) => (
    <Box
      borderRadius="lg"
      border="1px dashed"
      borderColor="gray.600"
      p={8}
      textAlign="center"
      bg="gray.800"
    >
      <Heading size="sm" mb={2}>
        No {label} listings yet
      </Heading>
      <Text color="gray.400" mb={4}>
        When you create events, they’ll show up here.
      </Text>
      {isNormalUser && (
        <Button leftIcon={<FiPlus />} colorScheme="teal" onClick={openCreate}>
          Create an Event Listing
        </Button>
      )}
    </Box>
  );

  const openDeleteDialog = (draft) => {
    setDraftToDelete(draft);
    openDelete();
  };

  const handleDelete = async () => {
    if (!draftToDelete) return;
    setDeleteLoading(true);
    try {
      await deleteDraft(draftToDelete.draftId);
      toast.success("Draft deleted.");
      setDrafts((prev) =>
        prev.filter((d) => d.draftId !== draftToDelete.draftId)
      );
      closeDelete();
      setDraftToDelete(null);
    } catch (err) {
      console.error(err);
      const msg =
        err?.response?.data?.message ||
        err?.response?.data ||
        "Failed to delete draft.";
      toast.error(msg);
    } finally {
      setDeleteLoading(false);
    }
  };

  const openApproveDialog = (draft) => {
    setSelectedDraft(draft);
    setAdminComment("");
    openApprove();
  };

  const openRejectDialog = (draft) => {
    setSelectedDraft(draft);
    setAdminComment("");
    openReject();
  };

  const handleApprove = async () => {
    if (!selectedDraft) return;
    setActionLoading(true);
    try {
      await approveDraft(selectedDraft.draftId, {
        adminComment: adminComment || null,
      });
      toast.success("Draft approved and event created.");

      const status =
        adminStatusTab === 0
          ? "PENDING"
          : adminStatusTab === 1
          ? "APPROVED"
          : "REJECTED";
      await loadAdminDrafts(status);

      closeApprove();
      setSelectedDraft(null);
    } catch (err) {
      console.error(err);
      const msg =
        err?.response?.data?.message ||
        err?.response?.data ||
        "Failed to approve draft.";
      toast.error(msg);
    } finally {
      setActionLoading(false);
    }
  };

  const handleReject = async () => {
    if (!selectedDraft) return;
    if (!adminComment.trim()) {
      toast.warn("Please provide a reason for rejection.");
      return;
    }
    setActionLoading(true);
    try {
      await rejectDraft(selectedDraft.draftId, {
        adminComment: adminComment.trim(),
      });
      toast.success("Draft rejected.");

      const status =
        adminStatusTab === 0
          ? "PENDING"
          : adminStatusTab === 1
          ? "APPROVED"
          : "REJECTED";
      await loadAdminDrafts(status);

      closeReject();
      setSelectedDraft(null);
    } catch (err) {
      console.error(err);
      const msg =
        err?.response?.data?.message ||
        err?.response?.data ||
        "Failed to reject draft.";
      toast.error(msg);
    } finally {
      setActionLoading(false);
    }
  };

  const renderDraftCard = (draft, isUserView) => {
    const {
      draftId,
      title,
      description,
      subCategory,
      genre,
      artist,
      host,
      eventStart,
      eventEnd,
      venueId,
      basePrice,
      approvalStatus,
      adminComment,
      createdAt,
    } = draft;

    const venue = findVenue(venueId);
    const venueLabel = venue
      ? `${venue.name}${venue.screenName ? " • " + venue.screenName : ""} • ${
          venue.city
        }`
      : `Venue #${venueId}`;

    return (
      <Box
        key={draftId}
        bg="gray.800"
        borderRadius="lg"
        overflow="hidden"
        boxShadow="md"
        _hover={{ boxShadow: "xl", transform: "translateY(-2px)" }}
        transition="all 0.2s"
      >
        <Box bgGradient="linear(to-r, teal.500, purple.500)" p={4}>
          <HStack justify="space-between" align="flex-start">
            <VStack align="flex-start" spacing={1}>
              <Text fontSize="lg" fontWeight="bold" noOfLines={1}>
                {title}
              </Text>
              <Text fontSize="xs" color="whiteAlpha.800">
                Created on {formatDateTime(createdAt)}
              </Text>
            </VStack>
            <Badge
              colorScheme={statusColor(approvalStatus)}
              variant="solid"
              borderRadius="full"
            >
              {approvalStatus}
            </Badge>
          </HStack>
        </Box>

        <Box p={4}>
          <VStack align="stretch" spacing={3}>
            <Text fontSize="sm" color="gray.300" noOfLines={3}>
              {description}
            </Text>

            <HStack spacing={2} flexWrap="wrap">
              {subCategory && (
                <Tag
                  size="sm"
                  variant="subtle"
                  colorScheme="cyan"
                  borderRadius="full"
                >
                  <TagLabel>{subCategory}</TagLabel>
                </Tag>
              )}
              {genre && (
                <Tag
                  size="sm"
                  variant="subtle"
                  colorScheme="purple"
                  borderRadius="full"
                >
                  <TagLabel>{genre}</TagLabel>
                </Tag>
              )}
              {artist && (
                <Tag
                  size="sm"
                  variant="subtle"
                  colorScheme="pink"
                  borderRadius="full"
                >
                  <TagLabel>{artist}</TagLabel>
                </Tag>
              )}
              {host && (
                <Tag
                  size="sm"
                  variant="subtle"
                  colorScheme="orange"
                  borderRadius="full"
                >
                  <TagLabel>Hosted by {host}</TagLabel>
                </Tag>
              )}
            </HStack>

            <HStack spacing={3} flexWrap="wrap" fontSize="sm" color="gray.300">
              <HStack>
                <FiCalendar />
                <Text>{formatDate(eventStart)}</Text>
              </HStack>
              <HStack>
                <FiClock />
                <Text>
                  {formatTime(eventStart)} – {formatTime(eventEnd)}
                </Text>
              </HStack>
              <HStack>
                <FiMapPin />
                <Text>{venueLabel}</Text>
              </HStack>
            </HStack>

            <HStack justify="space-between" align="center">
              <Text fontSize="lg" fontWeight="bold">
                ₹{Number(basePrice || 0).toFixed(2)}{" "}
                <Text
                  as="span"
                  fontSize="xs"
                  color="gray.400"
                  fontWeight="normal"
                >
                  base ticket
                </Text>
              </Text>

              <HStack spacing={2}>
                {/* Preview button */}
                <Button
                  variant="ghost"
                  size="sm"
                  rightIcon={<FiChevronRight />}
                  onClick={() => {
                    toast.info("Preview coming soon.");
                  }}
                >
                  Preview
                </Button>

                {isUserView ? (
                  <>
                    {(approvalStatus === "PENDING" ||
                      approvalStatus === "REJECTED") && (
                      <IconButton
                        size="sm"
                        aria-label="Delete draft"
                        icon={<FiTrash2 />}
                        colorScheme="red"
                        variant="outline"
                        onClick={() => openDeleteDialog(draft)}
                      />
                    )}
                  </>
                ) : (
                  <>
                    {approvalStatus === "PENDING" && (
                      <>
                        <IconButton
                          size="sm"
                          aria-label="Approve"
                          icon={<FiCheckCircle />}
                          colorScheme="teal"
                          variant="outline"
                          onClick={() => openApproveDialog(draft)}
                        />
                        <IconButton
                          size="sm"
                          aria-label="Reject"
                          icon={<FiXCircle />}
                          colorScheme="red"
                          variant="outline"
                          onClick={() => openRejectDialog(draft)}
                        />
                      </>
                    )}
                  </>
                )}
              </HStack>
            </HStack>

            {adminComment && (
              <Box
                mt={2}
                p={2}
                bg="gray.700"
                borderRadius="md"
                fontSize="xs"
                color="orange.200"
              >
                Admin note: {adminComment}
              </Box>
            )}
          </VStack>
        </Box>
      </Box>
    );
  };

  if (!isLoggedIn) {
    return (
      <Box bg="gray.900" color="gray.100" minH="100vh" py={10} px={6}>
        <Box
          maxW="lg"
          mx="auto"
          bg="gray.800"
          borderRadius="lg"
          p={8}
          textAlign="center"
          boxShadow="lg"
        >
          <Heading size="md" mb={3}>
            You’re not logged in
          </Heading>
          <Text color="gray.400" mb={6}>
            Please sign in to create events and track your listings.
          </Text>
          <Button colorScheme="teal" onClick={() => navigate("/login")}>
            Go to Login
          </Button>
        </Box>
      </Box>
    );
  }

  // ---------- USER VIEW ----------
  const renderUserView = () => (
    <Box bg="gray.900" color="gray.100" minH="100vh" py={8} px={4}>
      <Box maxW="6xl" mx="auto">
        <HStack
          justify="space-between"
          align="center"
          mb={6}
          flexWrap="wrap"
          gap={3}
        >
          <Box>
            <Heading size="lg" mb={1}>
              My Event Listings
            </Heading>
            <Text color="gray.400" fontSize="sm">
              Create and track events you’ve submitted for approval.
            </Text>
          </Box>
          <Button leftIcon={<FiPlus />} colorScheme="teal" onClick={openCreate}>
            Create Event Listing
          </Button>
        </HStack>

        <Tabs
          index={userStatusTab}
          onChange={setUserStatusTab}
          variant="soft-rounded"
          colorScheme="teal"
        >
          <TabList mb={4}>
            <Tab>Pending ({grouped.pending.length})</Tab>
            <Tab>Approved ({grouped.approved.length})</Tab>
            <Tab>Rejected ({grouped.rejected.length})</Tab>
          </TabList>

          <TabPanels>
            <TabPanel px={0}>
              {loading ? (
                renderSkeletonList()
              ) : grouped.pending.length === 0 ? (
                renderEmpty("pending")
              ) : (
                <VStack spacing={4} align="stretch">
                  {grouped.pending.map((d) => renderDraftCard(d, true))}
                </VStack>
              )}
            </TabPanel>

            <TabPanel px={0}>
              {loading ? (
                renderSkeletonList()
              ) : grouped.approved.length === 0 ? (
                renderEmpty("approved")
              ) : (
                <VStack spacing={4} align="stretch">
                  {grouped.approved.map((d) => renderDraftCard(d, true))}
                </VStack>
              )}
            </TabPanel>

            <TabPanel px={0}>
              {loading ? (
                renderSkeletonList()
              ) : grouped.rejected.length === 0 ? (
                renderEmpty("rejected")
              ) : (
                <VStack spacing={4} align="stretch">
                  {grouped.rejected.map((d) => renderDraftCard(d, true))}
                </VStack>
              )}
            </TabPanel>
          </TabPanels>
        </Tabs>
      </Box>

      <CreateDraftModal
        isOpen={isCreateOpen}
        onClose={closeCreate}
        onCreated={loadUserDrafts}
        venues={venues}
        selectedCityName={selectedCityName}
        userId={userId}
      />

      <AlertDialog isOpen={isDeleteOpen} onClose={closeDelete}>
        <AlertDialogOverlay>
          <AlertDialogContent bg="gray.800" color="gray.100">
            <AlertDialogHeader fontSize="lg" fontWeight="bold">
              Delete this draft?
            </AlertDialogHeader>
            <AlertDialogBody>
              {draftToDelete ? (
                <>
                  <Text mb={2}>
                    You are about to delete{" "}
                    <Text as="span" fontWeight="bold">
                      {draftToDelete.title}
                    </Text>
                    .
                  </Text>
                  <Text fontSize="sm" color="gray.400">
                    This action cannot be undone.
                  </Text>
                </>
              ) : (
                "Are you sure?"
              )}
            </AlertDialogBody>
            <AlertDialogFooter>
              <Button onClick={closeDelete} variant="ghost">
                Cancel
              </Button>
              <Button
                colorScheme="red"
                ml={3}
                onClick={handleDelete}
                isLoading={deleteLoading}
              >
                Delete
              </Button>
            </AlertDialogFooter>
          </AlertDialogContent>
        </AlertDialogOverlay>
      </AlertDialog>
    </Box>
  );

  // ---------- ADMIN VIEW ----------
  const renderAdminView = () => (
    <Box bg="gray.900" color="gray.100" minH="100vh" py={8} px={4}>
      <Box maxW="6xl" mx="auto">
        <Box mb={6}>
          <Heading size="lg" mb={1}>
            User Event Listings (Admin)
          </Heading>
          <Text color="gray.400" fontSize="sm">
            Review and moderate events submitted by users. Approving a draft
            will create the official event & slot.
          </Text>
        </Box>

        <Tabs
          index={adminStatusTab}
          onChange={(idx) => {
            setAdminStatusTab(idx);
            const status =
              idx === 0 ? "PENDING" : idx === 1 ? "APPROVED" : "REJECTED";
            loadAdminDrafts(status);
          }}
          variant="soft-rounded"
          colorScheme="teal"
        >
          <TabList mb={4}>
            <Tab>Pending</Tab>
            <Tab>Approved</Tab>
            <Tab>Rejected</Tab>
          </TabList>

          <TabPanels>
            {[0, 1, 2].map((statusIdx) => {
              const label =
                statusIdx === 0
                  ? "pending"
                  : statusIdx === 1
                  ? "approved"
                  : "rejected";
              return (
                <TabPanel px={0} key={statusIdx}>
                  {loading ? (
                    renderSkeletonList()
                  ) : drafts.length === 0 ? (
                    renderEmpty(label)
                  ) : (
                    <VStack spacing={4} align="stretch">
                      {drafts.map((d) => renderDraftCard(d, false))}
                    </VStack>
                  )}
                </TabPanel>
              );
            })}
          </TabPanels>
        </Tabs>
      </Box>

      {/* approve dialog */}
      <AlertDialog isOpen={isApproveOpen} onClose={closeApprove}>
        <AlertDialogOverlay>
          <AlertDialogContent bg="gray.800" color="gray.100">
            <AlertDialogHeader fontSize="lg" fontWeight="bold">
              Approve this event listing?
            </AlertDialogHeader>
            <AlertDialogBody>
              {selectedDraft ? (
                <>
                  <Text mb={2}>
                    This will create a public event for{" "}
                    <Text as="span" fontWeight="bold">
                      {selectedDraft.title}
                    </Text>
                    .
                  </Text>
                  <Text fontSize="sm" color="gray.300" mb={3}>
                    Event time: {formatDateTime(selectedDraft.eventStart)} –{" "}
                    {formatTime(selectedDraft.eventEnd)}
                  </Text>
                  <FormControl>
                    <FormLabel fontSize="sm">
                      Admin comment (optional)
                    </FormLabel>
                    <Textarea
                      bg="gray.700"
                      value={adminComment}
                      onChange={(e) => setAdminComment(e.target.value)}
                      placeholder="Any internal notes or comments for this approval..."
                    />
                  </FormControl>
                </>
              ) : (
                "Approve this draft?"
              )}
            </AlertDialogBody>
            <AlertDialogFooter>
              <Button variant="ghost" onClick={closeApprove}>
                Cancel
              </Button>
              <Button
                colorScheme="teal"
                ml={3}
                onClick={handleApprove}
                isLoading={actionLoading}
              >
                Approve & Publish
              </Button>
            </AlertDialogFooter>
          </AlertDialogContent>
        </AlertDialogOverlay>
      </AlertDialog>

      {/* reject dialog */}
      <AlertDialog isOpen={isRejectOpen} onClose={closeReject}>
        <AlertDialogOverlay>
          <AlertDialogContent bg="gray.800" color="gray.100">
            <AlertDialogHeader fontSize="lg" fontWeight="bold">
              Reject this event listing?
            </AlertDialogHeader>
            <AlertDialogBody>
              {selectedDraft ? (
                <>
                  <Text mb={2}>
                    This will reject{" "}
                    <Text as="span" fontWeight="bold">
                      {selectedDraft.title}
                    </Text>{" "}
                    and notify the user.
                  </Text>
                  <FormControl isRequired>
                    <FormLabel fontSize="sm">Reason for rejection</FormLabel>
                    <Textarea
                      bg="gray.700"
                      value={adminComment}
                      onChange={(e) => setAdminComment(e.target.value)}
                      placeholder="Eg. Incomplete details, invalid venue, conflicting schedule..."
                    />
                  </FormControl>
                </>
              ) : (
                "Reject this draft?"
              )}
            </AlertDialogBody>
            <AlertDialogFooter>
              <Button variant="ghost" onClick={closeReject}>
                Cancel
              </Button>
              <Button
                colorScheme="red"
                ml={3}
                onClick={handleReject}
                isLoading={actionLoading}
              >
                Reject
              </Button>
            </AlertDialogFooter>
          </AlertDialogContent>
        </AlertDialogOverlay>
      </AlertDialog>
    </Box>
  );

  return isNormalUser ? renderUserView() : renderAdminView();
}
