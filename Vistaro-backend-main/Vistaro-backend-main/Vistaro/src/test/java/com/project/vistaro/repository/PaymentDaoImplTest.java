package com.project.vistaro.repository;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.project.vistaro.model.Payment;
import com.project.vistaro.model.PaymentMode;
import com.project.vistaro.model.PaymentStatus;
import com.project.vistaro.util.PaymentRowMapper;

class PaymentDaoImplTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private PaymentDaoImpl paymentDao;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    // Helper method
    private Payment createPayment() {
        Payment p = new Payment();
        p.setPaymentId(1);
        p.setBookingId(1001);
        p.setPaymentMode(PaymentMode.UPI);
        p.setTransactionId("TX123");
        p.setPaymentStatus(PaymentStatus.SUCCESS);
        p.setPaidAt(LocalDateTime.of(2025, 1, 1, 10, 0));
        return p;
    }

    // ---------------------------------------------------------------------------------------
    // TEST: save()
    // ---------------------------------------------------------------------------------------
    @Test
    void testSave() throws Exception {
        Payment p = createPayment();

        // Mock key generation using reflection (KeyHolder stores a keyList field)
        doAnswer(invocation -> {
            KeyHolder keyHolder = invocation.getArgument(1);

            var field = GeneratedKeyHolder.class.getDeclaredField("keyList");
            field.setAccessible(true);
            field.set(keyHolder, List.of(Map.of("GENERATED_KEY", 1)));

            return 1; // update count
        }).when(jdbcTemplate).update(any(), any(KeyHolder.class));

        Payment result = paymentDao.save(p);

        assertEquals(1, result.getPaymentId());
        verify(jdbcTemplate, times(1)).update(any(), any(KeyHolder.class));
    }

    // ---------------------------------------------------------------------------------------
    // TEST: findById() FOUND
    // ---------------------------------------------------------------------------------------
    @Test
    void testFindById_Found() {
        Payment p = createPayment();
        when(jdbcTemplate.query(anyString(), any(PaymentRowMapper.class), eq(1)))
                .thenReturn(List.of(p));

        Optional<Payment> result = paymentDao.findById(1);

        assertTrue(result.isPresent());
        assertEquals(1001, result.get().getBookingId());
        verify(jdbcTemplate).query(anyString(), any(PaymentRowMapper.class), eq(1));
    }

    // ---------------------------------------------------------------------------------------
    // TEST: findById() NOT FOUND
    // ---------------------------------------------------------------------------------------
    @Test
    void testFindById_NotFound() {
        when(jdbcTemplate.query(anyString(), any(PaymentRowMapper.class), eq(1)))
                .thenReturn(List.of());

        Optional<Payment> result = paymentDao.findById(1);

        assertTrue(result.isEmpty());
    }

    // ---------------------------------------------------------------------------------------
    // TEST: findByBookingId() FOUND
    // ---------------------------------------------------------------------------------------
    @Test
    void testFindByBookingId_Found() {
        Payment p = createPayment();
        when(jdbcTemplate.query(anyString(), any(PaymentRowMapper.class), eq(1001)))
                .thenReturn(List.of(p));

        Optional<Payment> result = paymentDao.findByBookingId(1001);

        assertTrue(result.isPresent());
        assertEquals("TX123", result.get().getTransactionId());
        verify(jdbcTemplate).query(anyString(), any(PaymentRowMapper.class), eq(1001));
    }

    // ---------------------------------------------------------------------------------------
    // TEST: findByBookingId() NOT FOUND
    // ---------------------------------------------------------------------------------------
    @Test
    void testFindByBookingId_NotFound() {
        when(jdbcTemplate.query(anyString(), any(PaymentRowMapper.class), eq(1001)))
                .thenReturn(List.of());

        Optional<Payment> result = paymentDao.findByBookingId(1001);

        assertTrue(result.isEmpty());
    }

    // ---------------------------------------------------------------------------------------
    // TEST: update()
    // ---------------------------------------------------------------------------------------
    @Test
    void testUpdate() {
        Payment p = createPayment();
        when(jdbcTemplate.update(anyString(),
                eq(p.getBookingId()),
                eq(p.getPaymentMode().name()),
                eq(p.getTransactionId()),
                eq(p.getPaymentStatus().name()),
                eq(Timestamp.valueOf(p.getPaidAt())),
                eq(p.getPaymentId())
        )).thenReturn(1);

        int rows = paymentDao.update(p);

        assertEquals(1, rows);
        verify(jdbcTemplate, times(1)).update(anyString(),
                eq(p.getBookingId()),
                eq(p.getPaymentMode().name()),
                eq(p.getTransactionId()),
                eq(p.getPaymentStatus().name()),
                eq(Timestamp.valueOf(p.getPaidAt())),
                eq(p.getPaymentId())
        );
    }
}
