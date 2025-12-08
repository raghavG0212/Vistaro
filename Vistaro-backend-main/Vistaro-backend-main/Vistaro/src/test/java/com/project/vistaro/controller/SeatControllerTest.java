package com.project.vistaro.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.vistaro.model.Seat;
import com.project.vistaro.service.SeatService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SeatController.class)
@AutoConfigureMockMvc(addFilters = false)
class SeatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SeatService seatService;

    @Autowired
    private ObjectMapper mapper;

    private Seat createSeat() {
        Seat s = new Seat();
        s.setSeatId(1);
        s.setSlotId(101);
        s.setRowLabel("A");
        s.setSeatNumber("10");   // FIXED — String not int
        s.setSeatType("VIP");
        s.setPrice(BigDecimal.valueOf(600));
        s.setIsBooked(false);
        s.setIsLocked(false);
        s.setLockedUntil(LocalDateTime.now());
        return s;
    }

    @Test
    void testGetSeatsForSlot() throws Exception {
        when(seatService.getSeatsBySlot(101))
                .thenReturn(List.of(createSeat()));

        mockMvc.perform(get("/api/v1/seat/slot/101"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].seatId").value(1))
                .andExpect(jsonPath("$[0].seatNumber").value("10"));
    }

    @Test
    void testLockSeats() throws Exception {
        Map<String, List<Integer>> req = Map.of("seatIds", List.of(1, 2));

        mockMvc.perform(post("/api/v1/seat/lock")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(content().string("Seats locked"));

        verify(seatService).lockSeats(List.of(1, 2));
    }

    @Test
    void testUnlockSeats() throws Exception {
        Map<String, List<Integer>> req = Map.of("seatIds", List.of(4, 5));

        mockMvc.perform(post("/api/v1/seat/unlock")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(content().string("Seats unlocked"));

        verify(seatService).unlockSeats(List.of(4, 5));
    }
}
