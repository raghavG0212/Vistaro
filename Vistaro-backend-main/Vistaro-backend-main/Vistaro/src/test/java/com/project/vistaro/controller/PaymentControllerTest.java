package com.project.vistaro.controller;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.vistaro.dto.PaymentDto;
import com.project.vistaro.model.Payment;
import com.project.vistaro.model.PaymentMode;
import com.project.vistaro.model.PaymentStatus;
import com.project.vistaro.service.PaymentService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class PaymentControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private PaymentController paymentController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(paymentController).build();
    }

    // Helper method to create a sample Payment entity
    private Payment createPayment() {
        Payment p = new Payment();
        p.setPaymentId(1);
        p.setBookingId(1001);
        p.setPaymentMode(PaymentMode.UPI);
        p.setTransactionId("TXN12345");
        p.setPaymentStatus(PaymentStatus.SUCCESS);
        return p;
    }

    // Helper method to create DTO
    private PaymentDto createPaymentDto() {
        PaymentDto dto = new PaymentDto();
        dto.setPaymentId(1);
        dto.setBookingId(1001);
        dto.setPaymentMode(PaymentMode.UPI);
        dto.setTransactionId("TXN12345");
        dto.setPaymentStatus(PaymentStatus.SUCCESS);
        return dto;
    }

    // ---------------------------------------------------------------------------------------
    // TEST 1: POST /api/v1/payment  (create)
    // ---------------------------------------------------------------------------------------
    @Test
    void testCreatePayment() throws Exception {

        Payment payment = createPayment();
        PaymentDto dto = createPaymentDto();

        when(paymentService.save(any(Payment.class))).thenReturn(payment);

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                .post("/api/v1/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.paymentId").value(1))
                .andExpect(jsonPath("$.bookingId").value(1001))
                .andExpect(jsonPath("$.paymentMode").value("UPI"))
                .andExpect(jsonPath("$.transactionId").value("TXN12345"))
                .andExpect(jsonPath("$.paymentStatus").value("SUCCESS"));

        verify(paymentService, times(1)).save(any(Payment.class));
    }

    // ---------------------------------------------------------------------------------------
    // TEST 2: GET /api/v1/payment/{id}
    // ---------------------------------------------------------------------------------------
    @Test
    void testGetPaymentById() throws Exception {

        Payment payment = createPayment();
        when(paymentService.findById(1)).thenReturn(payment);

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                .get("/api/v1/payment/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paymentId").value(1))
                .andExpect(jsonPath("$.paymentMode").value("UPI"))
                .andExpect(jsonPath("$.transactionId").value("TXN12345"));

        verify(paymentService, times(1)).findById(1);
    }
}
