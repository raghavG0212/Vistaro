package com.project.vistaro.controller;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.project.vistaro.dto.EventDto;
import com.project.vistaro.model.Event;
import com.project.vistaro.model.EventCategory;
import com.project.vistaro.service.EventService;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class EventControllerTest {
	 private MockMvc mockMvc;

	    @Mock
	    private EventService service;

	    @InjectMocks
	    private EventController controller;

	    private ObjectMapper mapper = new ObjectMapper()
	            .registerModule(new JavaTimeModule())
	            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

	    private Event event;
	    private EventDto dto;

	    @BeforeEach
	    void setup() {
	        MockitoAnnotations.openMocks(this);
	        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

	        event = new Event();
	        event.setEventId(1);
	        event.setTitle("Test Event");
	        event.setDescription("Sample Desc");
	        event.setCategory(EventCategory.EVENT);
	        event.setSubCategory("Music");
	        event.setBannerUrl("banner.jpg");
	        event.setThumbnailUrl("thumb.jpg");
	        event.setStartTime(LocalDateTime.now());
	        event.setEndTime(LocalDateTime.now().plusHours(2));

	        dto = new EventDto(
	                1,
	                "Test Event",
	                "Sample Desc",
	                EventCategory.EVENT,
	                "Music",
	                "banner.jpg",
	                "thumb.jpg",
	                event.getStartTime(),
	                event.getEndTime()
	        );
	    }

	    // ---------------------------
	    //      GET ALL BY CITY
	    // ---------------------------
	    @Test
	    void testGetAllEvents() throws Exception {
	        when(service.findAllByCity("Mumbai")).thenReturn(Arrays.asList(event));

	        mockMvc.perform(get("/api/v1/event")
	                .param("city", "Mumbai"))
	                .andExpect(status().isOk())
	                .andExpect(jsonPath("$[0].title").value("Test Event"));
	    }

	    // ---------------------------
	    //      GET BY ID
	    // ---------------------------
	    @Test
	    void testGetById() throws Exception {
	        when(service.findById(1)).thenReturn(event);

	        mockMvc.perform(get("/api/v1/event/1"))
	                .andExpect(status().isOk())
	                .andExpect(jsonPath("$.title").value("Test Event"));
	    }

	    // ---------------------------
	    //      SEARCH BY TITLE
	    // ---------------------------
	    @Test
	    void testSearchByTitle() throws Exception {
	        when(service.searchByTitle("Mumbai", "Test"))
	                .thenReturn(Arrays.asList(event));

	        mockMvc.perform(get("/api/v1/event/search/title")
	                .param("city", "Mumbai")
	                .param("title", "Test"))
	                .andExpect(status().isOk())
	                .andExpect(jsonPath("$[0].title").value("Test Event"));
	    }

	    // ---------------------------
	    //      SEARCH BY CATEGORY
	    // ---------------------------
	    @Test
	    void testSearchByCategory() throws Exception {
	        when(service.searchByCategory("Mumbai", "EVENT"))
	                .thenReturn(Arrays.asList(event));

	        mockMvc.perform(get("/api/v1/event/search/category")
	                .param("city", "Mumbai")
	                .param("category", "EVENT"))
	                .andExpect(status().isOk())
	                .andExpect(jsonPath("$[0].category").value("EVENT"));
	    }

	    // ---------------------------
	    //      SEARCH BY SUBCATEGORY
	    // ---------------------------
	    @Test
	    void testSearchBySubCategory() throws Exception {
	        when(service.searchBySubCategory("Mumbai", "Music"))
	                .thenReturn(Arrays.asList(event));

	        mockMvc.perform(get("/api/v1/event/search/sub-category")
	                .param("city", "Mumbai")
	                .param("subCategory", "Music"))
	                .andExpect(status().isOk())
	                .andExpect(jsonPath("$[0].subCategory").value("Music"));
	    }

	    // ---------------------------
	    //      CREATE (POST)
	    // ---------------------------
	    @Test
	    void testCreateEvent() throws Exception {
	        when(service.create(any(Event.class))).thenReturn(event);

	        mockMvc.perform(post("/api/v1/event")
	                .contentType(MediaType.APPLICATION_JSON)
	                .content(mapper.writeValueAsString(dto)))
	                .andExpect(status().isCreated())
	                .andExpect(jsonPath("$.title").value("Test Event"));
	    }

	    // ---------------------------
	    //      UPDATE (PUT)
	    // ---------------------------
	    @Test
	    void testUpdateEvent() throws Exception {
	        when(service.update(eq(1), any(Event.class))).thenReturn(event);

	        mockMvc.perform(put("/api/v1/event/1")
	                .contentType(MediaType.APPLICATION_JSON)
	                .content(mapper.writeValueAsString(dto)))
	                .andExpect(status().isOk())
	                .andExpect(jsonPath("$.title").value("Test Event"));
	    }

	    // ---------------------------
	    //      DELETE
	    // ---------------------------
	    @Test
	    void testDeleteEvent() throws Exception {
	        doNothing().when(service).deleteEvent(1);

	        mockMvc.perform(delete("/api/v1/event/1"))
	                .andExpect(status().isOk());

	        verify(service, times(1)).deleteEvent(1);
	    }
	

}
