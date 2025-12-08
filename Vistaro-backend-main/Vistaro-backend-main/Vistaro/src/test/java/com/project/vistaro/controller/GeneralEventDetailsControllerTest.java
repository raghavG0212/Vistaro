package com.project.vistaro.controller;
 
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.vistaro.dto.GeneralEventDetailsDto;
import com.project.vistaro.model.GeneralEventDetails;
import com.project.vistaro.service.GeneralEventDetailsService;
 
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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
@WebMvcTest(GeneralEventDetailsController.class)
@AutoConfigureMockMvc(addFilters = false)
class GeneralEventDetailsControllerTest {
 
    @Autowired
    private MockMvc mockMvc;
 
    @MockBean
    private GeneralEventDetailsService service;
 
    @Autowired
    private ObjectMapper mapper;
 
    // ----------------------------------------------------------
    // Helper Methods
    // ----------------------------------------------------------
 
    private GeneralEventDetails createEntity() {
        GeneralEventDetails e = new GeneralEventDetails();
        e.setGeneralDetailsId(1);
        e.setEventId(500);
        e.setArtist("Arijit Singh");
        e.setHost("Star Events");
        e.setGenre("Music");
        e.setStartTime(LocalDateTime.of(2025, 3, 5, 18, 0));
        e.setEndTime(LocalDateTime.of(2025, 3, 5, 22, 0));
        e.setAdditionalInfo("Live concert");
        return e;
    }
 
    private GeneralEventDetailsDto createDto() {
        GeneralEventDetailsDto dto = new GeneralEventDetailsDto();
        dto.setGeneralDetailsId(1);
        dto.setEventId(500);
        dto.setArtist("Arijit Singh");
        dto.setHost("Star Events");
        dto.setGenre("Music");
        dto.setStartTime(LocalDateTime.of(2025, 3, 5, 18, 0));
        dto.setEndTime(LocalDateTime.of(2025, 3, 5, 22, 0));
        dto.setAdditionalInfo("Live concert");
        return dto;
    }
 
    // ----------------------------------------------------------
    // TEST: Add Event Details
    // ----------------------------------------------------------
    @Test
    void testAddDetails() throws Exception {
        GeneralEventDetails saved = createEntity();
        GeneralEventDetailsDto dto = createDto();
        Mockito.when(service.addEventDetails(any(GeneralEventDetails.class)))
                .thenReturn(saved);
        mockMvc.perform(post("/api/v1/event-details")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.artist").value("Arijit Singh"))
                .andExpect(jsonPath("$.eventId").value(500));
    }  
 
    // ----------------------------------------------------------
    // TEST: Get By ID
    // ----------------------------------------------------------
    @Test
    void testGetById() throws Exception {
        Mockito.when(service.getDetailsById(1)).thenReturn(createEntity());
 
        mockMvc.perform(get("/api/v1/event-details/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.generalDetailsId").value(1));
    }
 
    // ----------------------------------------------------------
    // TEST: Get By Event ID
    // ----------------------------------------------------------
    @Test
    void testGetByEventId() throws Exception {
        Mockito.when(service.getDetailsByEventId(500)).thenReturn(createEntity());
 
        mockMvc.perform(get("/api/v1/event-details/event/500"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventId").value(500));
    }
 
    // ----------------------------------------------------------
    // TEST: Get All
    // ----------------------------------------------------------
    @Test
    void testGetAll() throws Exception {
        Mockito.when(service.getAllDetails()).thenReturn(List.of(createEntity()));
 
        mockMvc.perform(get("/api/v1/event-details"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].artist").value("Arijit Singh"));
    }
 
    // ----------------------------------------------------------
    // TEST: Delete Event Details
    // ----------------------------------------------------------
    @Test
    void testDeleteDetails() throws Exception {
 
        mockMvc.perform(delete("/api/v1/event-details/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Event details deleted successfully"));
 
        Mockito.verify(service).deleteDetails(1);
    }
 
    // ----------------------------------------------------------
    // TEST: Update Event Details
    // ----------------------------------------------------------
    @Test
    void testUpdate() throws Exception {
        GeneralEventDetails updated = createEntity();
        GeneralEventDetailsDto dto = createDto();
 
        Mockito.when(service.update(eq(1), any(GeneralEventDetails.class)))
                .thenReturn(updated);
 
        mockMvc.perform(put("/api/v1/event-details/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.artist").value("Arijit Singh"));
    }
}