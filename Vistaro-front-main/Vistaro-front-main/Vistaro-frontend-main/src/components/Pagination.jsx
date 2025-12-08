import { HStack, Button } from "@chakra-ui/react";

export default function Pagination({ page, totalPages, onChange }) {
	// Always show at least 1 page
	const safeTotal = totalPages > 0 ? totalPages : 1;

	const pages = [];
	for (let i = 1; i <= safeTotal; i++) pages.push(i);

	return (
		<HStack spacing={2} justify="center" mt={8} mb={4}>
			<Button
				size="sm"
				bg="gray.800"
				color="white"
				border="1px solid"
				borderColor="gray.600"
				_hover={{ bg: "teal.600" }}
				isDisabled={page === 1}
				onClick={() => onChange(page - 1)}
			>
				Prev
			</Button>

			{pages.map((p) => (
				<Button
					key={p}
					size="sm"
					bg={page === p ? "teal.500" : "gray.800"}
					color={page === p ? "white" : "gray.300"}
					border="1px solid"
					borderColor={page === p ? "teal.300" : "gray.600"}
					_hover={{ bg: "teal.600", color: "white" }}
					onClick={() => onChange(p)}
				>
					{p}
				</Button>
			))}

			<Button
				size="sm"
				bg="gray.800"
				color="white"
				border="1px solid"
				borderColor="gray.600"
				_hover={{ bg: "teal.600" }}
				isDisabled={page === safeTotal}
				onClick={() => onChange(page + 1)}
			>
				Next
			</Button>
		</HStack>
	);
}
