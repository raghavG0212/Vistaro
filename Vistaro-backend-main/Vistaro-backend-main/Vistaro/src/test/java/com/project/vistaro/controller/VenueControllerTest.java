package com.project.vistaro.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.vistaro.dto.VenueDto;
import com.project.vistaro.model.Venue;
import com.project.vistaro.model.VenueType;
import com.project.vistaro.service.VenueService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VenueController.class)
@AutoConfigureMockMvc(addFilters = false)
class VenueControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VenueService venueService;

    @Autowired
    private ObjectMapper mapper;

    // ----------------------------------------------
    // Helper methods
    // ----------------------------------------------
    private Venue createVenue() {
        Venue v = new Venue();
        v.setVenueId(1);
        v.setName("PVR Cinema");
        v.setAddress("MG Road");
        v.setCity("Mumbai");
        v.setVenueType(VenueType.CINEMA);   // FIXED: Enum, not String
        v.setScreenName("Screen 1");
        v.setSeatLayoutJson("{\"rows\":10}");  // FIXED: JSON as String
        return v;
    }

    private VenueDto createDto() {
        VenueDto dto = new VenueDto();
        dto.setVenueId(1);
        dto.setName("PVR Cinema");
        dto.setAddress("MG Road");
        dto.setCity("Mumbai");
        dto.setVenueType(VenueType.CINEMA);            // OK: DTO uses String
        dto.setScreenName("Screen 1");
        dto.setSeatLayoutJson(Map.of("rows", 10));  // OK: DTO uses Map
        return dto;
    }


    // ----------------------------------------------
    // TEST: Create Venue (POST)
    // ----------------------------------------------
    @Test
    void testCreateVenue() throws Exception {
        Venue saved = createVenue();
        VenueDto dto = createDto();

        when(venueService.save(any(Venue.class))).thenReturn(saved);

        mockMvc.perform(post("/api/v1/venue")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/v1/venue/1"))
                .andExpect(jsonPath("$.name").value("PVR Cinema"))
                .andExpect(jsonPath("$.city").value("Mumbai"));
    }

    // ----------------------------------------------
    // TEST: Update Venue (PUT)
    // ----------------------------------------------
    @Test
    void testUpdateVenue() throws Exception {
        Venue updated = createVenue();
        updated.setCity("Delhi");

        VenueDto dto = createDto();
        dto.setCity("Delhi");

        when(venueService.update(eq(1), any(Venue.class))).thenReturn(updated);

        mockMvc.perform(put("/api/v1/venue/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.city").value("Delhi"));
    }

    // ----------------------------------------------
    // TEST: Get Venue By ID
    // ----------------------------------------------
    @Test
    void testGetById() throws Exception {
        when(venueService.findById(1)).thenReturn(createVenue());

        mockMvc.perform(get("/api/v1/venue/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("PVR Cinema"))
                .andExpect(jsonPath("$.city").value("Mumbai"));
    }

    // ----------------------------------------------
    // TEST: Get All Venues
    // ----------------------------------------------
    @Test
    void testGetAll() throws Exception {
        when(venueService.findAll()).thenReturn(List.of(createVenue()));

        mockMvc.perform(get("/api/v1/venue/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].venueId").value(1))
                .andExpect(jsonPath("$[0].name").value("PVR Cinema"));
    }

    // ----------------------------------------------
    // TEST: Search by Name
    // ----------------------------------------------
    @Test
    void testSearchByName() throws Exception {
        when(venueService.searchByName("PVR")).thenReturn(List.of(createVenue()));

        mockMvc.perform(get("/api/v1/venue/search/name")
                .param("name", "PVR"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("PVR Cinema"));
    }

    // ----------------------------------------------
    // TEST: Search by City
    // ----------------------------------------------
    @Test
    void testSearchByCity() throws Exception {
        when(venueService.searchByCity("Mumbai")).thenReturn(List.of(createVenue()));

        mockMvc.perform(get("/api/v1/venue/search/city")
                .param("city", "Mumbai"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].city").value("Mumbai"));
    }

    // ----------------------------------------------
    // TEST: Search by Venue Type
    // ----------------------------------------------
    @Test
    void testSearchByType() throws Exception {
        when(venueService.searchByType("CINEMA")).thenReturn(List.of(createVenue()));

        mockMvc.perform(get("/api/v1/venue/search/type")
                .param("type", "CINEMA"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].venueType").value("CINEMA"));
    }

    // ----------------------------------------------
    // TEST: Search by Screen Name
    // ----------------------------------------------
    @Test
    void testSearchByScreen() throws Exception {
        when(venueService.searchByScreen("Screen 1")).thenReturn(List.of(createVenue()));

        mockMvc.perform(get("/api/v1/venue/search/screen")
                .param("screen", "Screen 1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].screenName").value("Screen 1"));
    }
}
