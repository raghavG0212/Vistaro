package com.project.vistaro.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Optional;

import com.project.vistaro.exception.ResourceNotFoundException;
import com.project.vistaro.model.Payment;
import com.project.vistaro.model.PaymentMode;
import com.project.vistaro.model.PaymentStatus;
import com.project.vistaro.repository.PaymentDao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

class PaymentServiceImplTest {

    @Mock
    private PaymentDao paymentDao;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    // Helper method to create a sample Payment object
    private Payment createPayment() {
        Payment p = new Payment();
        p.setPaymentId(1);
        p.setBookingId(1001);
        p.setPaymentMode(PaymentMode.UPI);
        p.setTransactionId("TX12345");
        p.setPaymentStatus(PaymentStatus.SUCCESS);
        p.setPaidAt(LocalDateTime.of(2025, 1, 1, 10, 0));
        return p;
    }

    // -------------------------------------------------------------------------
    // TEST 1: save()
    // -------------------------------------------------------------------------
    @Test
    void testSavePayment() {
        Payment payment = createPayment();

        when(paymentDao.save(any(Payment.class))).thenReturn(payment);

        Payment result = paymentService.save(payment);

        assertNotNull(result);
        assertEquals(1, result.getPaymentId());
        verify(paymentDao, times(1)).save(payment);
    }

    // -------------------------------------------------------------------------
    // TEST 2: findById() SUCCESS
    // -------------------------------------------------------------------------
    @Test
    void testFindById_Success() {
        Payment payment = createPayment();

        when(paymentDao.findById(1)).thenReturn(Optional.of(payment));

        Payment result = paymentService.findById(1);

        assertEquals(1, result.getPaymentId());
        assertEquals(1001, result.getBookingId());
        verify(paymentDao).findById(1);
    }

    // -------------------------------------------------------------------------
    // TEST 3: findById() NOT FOUND → throw exception
    // -------------------------------------------------------------------------
    @Test
    void testFindById_NotFound() {
        when(paymentDao.findById(1)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            paymentService.findById(1);
        });

        verify(paymentDao).findById(1);
    }
}
