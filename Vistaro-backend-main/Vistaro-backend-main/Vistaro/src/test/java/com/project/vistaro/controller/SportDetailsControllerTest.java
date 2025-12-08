package com.project.vistaro.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.vistaro.dto.SportDetailsDto;
import com.project.vistaro.model.SportDetails;
import com.project.vistaro.service.SportDetailsService;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SportDetailsController.class)
@AutoConfigureMockMvc(addFilters = false)
class SportDetailsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SportDetailsService sportService;

    @Autowired
    private ObjectMapper mapper;

    private SportDetails createSport() {
        SportDetails s = new SportDetails();
        s.setSportDetailsId(1);
        s.setEventId(100);
        s.setSportType("Cricket");
        s.setTeam1("Team A");
        s.setTeam2("Team B");
        s.setMatchFormat("T20");
        s.setVenueInfo("Stadium XYZ");
        s.setStartTime(LocalDateTime.of(2025, 1, 1, 10, 0));
        s.setEndTime(LocalDateTime.of(2025, 1, 1, 13, 0));
        return s;
    }

    private SportDetailsDto createSportDto() {
        SportDetailsDto dto = new SportDetailsDto();
        dto.setSportDetailsId(1);
        dto.setEventId(100);
        dto.setSportType("Cricket");
        dto.setTeam1("Team A");
        dto.setTeam2("Team B");
        dto.setMatchFormat("T20");
        dto.setVenueInfo("Stadium XYZ");
        dto.setStartTime(LocalDateTime.of(2025, 1, 1, 10, 0));
        dto.setEndTime(LocalDateTime.of(2025, 1, 1, 13, 0));
        return dto;
    }

    // ----------------------- TEST: ADD SPORT -----------------------
    @Test
    void testAddSport() throws Exception {
        SportDetails saved = createSport();
        SportDetailsDto dto = createSportDto();

        Mockito.when(sportService.addSportDetails(any(SportDetails.class)))
                .thenReturn(saved);

        mockMvc.perform(post("/api/v1/sport")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sportDetailsId").value(1))
                .andExpect(jsonPath("$.sportType").value("Cricket"));
    }

    // ----------------------- TEST: UPDATE SPORT -----------------------
    @Test
    void testUpdateSport() throws Exception {
        SportDetails updated = createSport();
        SportDetailsDto dto = createSportDto();

        Mockito.when(sportService.updateSportDetails(any(SportDetails.class)))
                .thenReturn(updated);

        mockMvc.perform(put("/api/v1/sport/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sportDetailsId").value(1));
    }

    // ----------------------- TEST: DELETE SPORT -----------------------
    @Test
    void testDeleteSport() throws Exception {
        mockMvc.perform(delete("/api/v1/sport/1"))
                .andExpect(status().isOk());

        Mockito.verify(sportService).deleteSportDetails(1);
    }

    // ----------------------- TEST: GET SPORT BY ID -----------------------
    @Test
    void testGetSportById() throws Exception {
        Mockito.when(sportService.findById(1)).thenReturn(createSport());

        mockMvc.perform(get("/api/v1/sport/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.team1").value("Team A"));
    }

    // ----------------------- TEST: GET BY EVENT -----------------------
    @Test
    void testGetByEvent() throws Exception {
        Mockito.when(sportService.findByEventId(100)).thenReturn(createSport());

        mockMvc.perform(get("/api/v1/sport/event/100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventId").value(100));
    }

    // ----------------------- TEST: LIST ALL SPORTS -----------------------
    @Test
    void testListAllSports() throws Exception {
        Mockito.when(sportService.listAllSportDetails())
                .thenReturn(List.of(createSport()));

        mockMvc.perform(get("/api/v1/sport"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].sportDetailsId").value(1));
    }

    // ----------------------- TEST: GET BY SPORT TYPE -----------------------
    @Test
    void testGetBySportType() throws Exception {
        Mockito.when(sportService.getBySportType("Cricket"))
                .thenReturn(List.of(createSport()));

        mockMvc.perform(get("/api/v1/sport/type/Cricket"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].sportType").value("Cricket"));
    }
}
