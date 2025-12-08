package com.project.vistaro.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.vistaro.dto.BookingFoodDto;
import com.project.vistaro.model.BookingFood;
import com.project.vistaro.service.BookingFoodService;

class BookingFoodControllerTest {

    private MockMvc mockMvc;

    @Mock
    private BookingFoodService service;

    @InjectMocks
    private BookingFoodController controller;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = standaloneSetup(controller).build();
    }

    // Helper to create entity
    private BookingFood createEntity() {
        BookingFood bf = new BookingFood();
        bf.setBookingFoodId(1);
        bf.setBookingId(1001);
        bf.setFoodId(10);
        bf.setQuantity(2);
        return bf;
    }

    // Helper to create DTO
    private BookingFoodDto createDto() {
        BookingFoodDto dto = new BookingFoodDto();
        dto.setBookingFoodId(1);
        dto.setBookingId(1001);
        dto.setFoodId(10);
        dto.setQuantity(2);
        return dto;
    }

    // ---------------------------------------------------------
    // TEST: POST /add
    // ---------------------------------------------------------
    @Test
    void testAdd() throws Exception {
        BookingFood entity = createEntity();
        BookingFoodDto dto = createDto();

        when(service.save(any())).thenReturn(entity);

        mockMvc.perform(post("/api/v1/booking-food/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookingFoodId").value(1))
                .andExpect(jsonPath("$.bookingId").value(1001))
                .andExpect(jsonPath("$.foodId").value(10))
                .andExpect(jsonPath("$.quantity").value(2));

        verify(service, times(1)).save(any());
    }

    // ---------------------------------------------------------
    // TEST: GET /{id}
    // ---------------------------------------------------------
    @Test
    void testGetById() throws Exception {
        BookingFood entity = createEntity();

        when(service.findById(1)).thenReturn(entity);

        mockMvc.perform(get("/api/v1/booking-food/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookingFoodId").value(1));

        verify(service).findById(1);
    }

    // ---------------------------------------------------------
    // TEST: GET /booking/{bookingId}
    // ---------------------------------------------------------
    @Test
    void testGetByBooking() throws Exception {
        when(service.findByBooking(1001))
                .thenReturn(List.of(createEntity()));

        mockMvc.perform(get("/api/v1/booking-food/booking/1001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].bookingFoodId").value(1));

        verify(service).findByBooking(1001);
    }

    // ---------------------------------------------------------
    // TEST: GET /
    // ---------------------------------------------------------
    @Test
    void testGetAll() throws Exception {
        when(service.findAll()).thenReturn(List.of(createEntity()));

        mockMvc.perform(get("/api/v1/booking-food"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].foodId").value(10));

        verify(service).findAll();
    }

    // ---------------------------------------------------------
    // TEST: PUT /{id}
    // ---------------------------------------------------------
    @Test
    void testUpdate() throws Exception {
        BookingFood updated = createEntity();
        updated.setQuantity(5); // updated qty
        BookingFoodDto dto = createDto();
        dto.setQuantity(5);

        when(service.update(eq(1), any())).thenReturn(updated);

        mockMvc.perform(put("/api/v1/booking-food/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity").value(5));

        verify(service).update(eq(1), any());
    }

    // ---------------------------------------------------------
    // TEST: DELETE /{id}
    // ---------------------------------------------------------
    @Test
    void testDelete() throws Exception {
        doNothing().when(service).delete(1);

        mockMvc.perform(delete("/api/v1/booking-food/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Food item removed from booking."));

        verify(service, times(1)).delete(1);
    }
}
