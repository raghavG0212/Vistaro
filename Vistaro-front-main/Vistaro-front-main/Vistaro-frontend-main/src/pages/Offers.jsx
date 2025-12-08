import React from "react";
import {
	Box,
	Heading,
	Text,
	VStack,
	SimpleGrid,
	Card,
	CardBody,
	Badge,
	HStack,
	IconButton,
	Divider,
} from "@chakra-ui/react";
import { FiChevronLeft, FiChevronRight } from "react-icons/fi";

export default function Offers() {
	// EXACT same offers we inserted earlier
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
			color: "purple",
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
			color: "blue",
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
			color: "green",
		},
	];

	return (
		<Box
			minH="100vh"
			bg="#050816"
			color="white"
			px={{ base: 4, md: 10 }}
			py={10}
		>
			{/* Featured Banner + Controls */}
			<VStack align="start" spacing={5} w="full" mb={8}>
				<HStack justify="space-between" w="full">
					<Heading size="lg">Offers & Deals</Heading>
					<HStack spacing={2}>
						<IconButton
							icon={<FiChevronLeft />}
							aria-label="Prev"
							variant="outline"
							borderColor="whiteAlpha.400"
						/>
						<IconButton
							icon={<FiChevronRight />}
							aria-label="Next"
							variant="outline"
							borderColor="whiteAlpha.400"
						/>
					</HStack>
				</HStack>

				<Text color="gray.300">
					Best discounts & promotions available right now.
				</Text>
			</VStack>

			{/* Offer Cards */}
			<SimpleGrid columns={{ base: 1, md: 2, lg: 3 }} spacing={7}>
				{offers.map((o) => (
					<Card
						key={o.id}
						bg="whiteAlpha.100"
						border="1px solid"
						borderColor="whiteAlpha.300"
						borderRadius="lg"
						backdropFilter="blur(5px)"
						_hover={{
							transform: "scale(1.03)",
							boxShadow: "0px 0px 15px rgba(255,255,255,0.15)",
							transition: "0.25s",
						}}
					>
						<CardBody>
							<VStack align="start" spacing={3}>
								<HStack spacing={3}>
									<Badge
										colorScheme={o.color}
										variant="solid"
										px={3}
										py={1}
										borderRadius="full"
									>
										{o.tag}
									</Badge>

									<Badge
										colorScheme="gray"
										variant="outline"
										px={3}
										py={1}
										borderRadius="full"
										borderColor="whiteAlpha.500"
									>
										Code: {o.code}
									</Badge>
								</HStack>

								<Heading size="md" fontWeight="bold">
									{o.title}
								</Heading>

								<Text fontSize="sm" color="gray.300">
									{o.description}
								</Text>

								<Divider borderColor="whiteAlpha.200" />

								<VStack align="start" spacing={1} fontSize="sm">
									<Text color="gray.200">
										Discount:{" "}
										<b>
											{o.discountPercent}% (Upto ₹
											{o.maxDiscount})
										</b>
									</Text>

									<Text color="gray.400">
										Valid: {o.validFrom} — {o.validTill}
									</Text>
								</VStack>
							</VStack>
						</CardBody>
					</Card>
				))}
			</SimpleGrid>
		</Box>
	);
}
