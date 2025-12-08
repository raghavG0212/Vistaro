// src/components/HomeEventType.jsx

import React from "react";
import { Box, Text, Image } from "@chakra-ui/react";
import Carousel from "react-multi-carousel";
import "react-multi-carousel/lib/styles.css";
import { useNavigate } from "react-router-dom";
import HeadingComponent from "./HeadingComponent";

export default function HomeEventType() {
  const navigate = useNavigate();

  // IMPORTANT: filter values MUST match DB
  const types = [
	{
	  title: "Standup Comedy",
	  filter: "Standup",        // EXACT match with DB
	  img: "https://play-lh.googleusercontent.com/ZFEdWR7oVxxKYuuAqWeb2Bs0RliNS66EQBd-jiPH32rKtyKQXZUSGEHQeq1N8ywsvJQ",
	},
	{
	  title: "Live Concert",
	  filter: "Concert",
	  img: "https://images.travelandleisureasia.com/wp-content/uploads/sites/2/2022/12/30232040/live-concerts.jpeg",
	},
	{
	  title: "Theatre Show",
	  filter: "Theatre",        // only if exists in DB
	  img: "https://images.ctfassets.net/6pezt69ih962/5UZHrwtxomD72NAopWrfaU/25d6debcce2c6648e0d1924ec6c89df4/Amber_Davies__Centre__and_the_original_West_End_company_of_The_Great_Gatsby__c__Johan_Persson.jpg?h=550&fm=webp&q=90",
	},
	{
	  title: "Cultural Event",
	  filter: "Cultural",
	  img: "https://english.news.cn/asiapacific/20230419/fd638e6cfb9943298f71ee3ddb81ca48/20230419fd638e6cfb9943298f71ee3ddb81ca48_20230419a8bd10f69e8947d7aa09e5e3149624e1.jpg",
	},
	{
	  title: "Festival Exhibition",
	  filter: "Exhibition",
	  img: "https://assets.thehansindia.com/h-upload/2021/10/12/1116937-numanish.webp",
	},
	{
	title: "FOOD_FEST",
	filter: "Food Fest",
	img: "https://dt4l9bx31tioh.cloudfront.net/eazymedia/eazytrendz/3896/trend20230426095408.jpg?width=750&height=436&mode=crop"
	},
	{
	  title: "Workshop Class",
	  filter: "Workshop",  // only if exists
	  img: "https://cdn.eventespresso.com/wp-content/uploads/2023/10/25213931/Teachers-workshop-lecture-and-presentation-skills-training-1024x576.jpg",
	},
	{
	  title: "Open Mic",
	  filter: "Open Mic",  // only if exists
	  img: "https://voca-land.sgp1.cdn.digitaloceanspaces.com/43844/1721534041893/71bb94cbac215e90ab362d374c784390.jpg",
	},
	{
	  title: "Tech Fest",
	  filter: "Tech Fest",  // only if exists
	  img: "https://www.askiitians.com/blog/wp-content/uploads/2014/08/50.png",
	},
  ];

  const responsive = {
	desktop: { breakpoint: { max: 3000, min: 1024 }, items: 5 },
	tablet: { breakpoint: { max: 1024, min: 600 }, items: 3 },
	mobile: { breakpoint: { max: 600, min: 0 }, items: 2 },
  };

  return (
	<Box mt={3} px={4} py={12}>
	  <HeadingComponent heading="Pick Your Vibe" />

	  <Carousel responsive={responsive} arrows infinite>
		{types.map((t) => (
		  <Box
			key={t.title}
			cursor="pointer"
			px={2}
			onClick={() => navigate(`/events?type=${t.filter.toLowerCase()}`)}
		  >
			<Box
			  borderRadius="lg"
			  overflow="hidden"
			  bg="gray.800"
			  boxShadow="md"
			  transition="0.2s"
			  _hover={{ transform: "scale(1.05)" }}
			>
			  <Image
				src={t.img}
				alt={t.title}
				width="100%"
				height="160px"
				objectFit="cover"
			  />

			  <Box p={3}>
				<Text
				  color="white"
				  fontWeight="bold"
				  textAlign="center"
				  fontSize="md"
				  noOfLines={1}
				>
				  {t.title}
				</Text>
			  </Box>
			</Box>
		  </Box>
		))}
	  </Carousel>
	</Box>
  );
}
