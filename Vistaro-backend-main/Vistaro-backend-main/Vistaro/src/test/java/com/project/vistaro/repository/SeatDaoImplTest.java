package com.project.vistaro.repository;

import com.project.vistaro.model.Seat;
import com.project.vistaro.util.SeatRowMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class SeatDaoImplTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private SeatDaoImpl seatDao;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    private Seat createSeat() {
        Seat s = new Seat();
        s.setSeatId(1);
        s.setSlotId(101);
        s.setRowLabel("A");
        s.setSeatNumber("10");
        s.setSeatType("VIP");
        s.setPrice(java.math.BigDecimal.valueOf(600));
        s.setIsBooked(false);
        s.setIsLocked(false);
        s.setLockedUntil(LocalDateTime.of(2025, 1, 1, 10, 0));
        return s;
    }

    // -------------------------------------------------------
    // TEST: save()
    // -------------------------------------------------------
    @Test
    void testSave() {
        Seat seat = createSeat();

        when(jdbcTemplate.update(
                anyString(),
                any(), any(), any(), any(), any(), any(), any(), any()
        )).thenReturn(1);

        seatDao.save(seat);

        verify(jdbcTemplate, times(1))
                .update(anyString(),
                        any(), any(), any(), any(), any(), any(), any(), any());
    }

    // -------------------------------------------------------
    // TEST: findBySlot()
    // -------------------------------------------------------
    @Test
    void testFindBySlot() {

        when(jdbcTemplate.query(anyString(), any(SeatRowMapper.class), eq(101)))
                .thenReturn(List.of(createSeat()));

        List<Seat> result = seatDao.findBySlot(101);

        assertEquals(1, result.size());
        assertEquals(101, result.get(0).getSlotId());
    }

    // -------------------------------------------------------
    // TEST: findById() - Found
    // -------------------------------------------------------
    @Test
    void testFindByIdFound() {

        when(jdbcTemplate.query(anyString(), any(SeatRowMapper.class), eq(1)))
                .thenReturn(List.of(createSeat()));

        Optional<Seat> result = seatDao.findById(1);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getSeatId());
    }

    // -------------------------------------------------------
    // TEST: findById() - Not Found
    // -------------------------------------------------------
    @Test
    void testFindByIdNotFound() {

        when(jdbcTemplate.query(anyString(), any(SeatRowMapper.class), eq(1)))
                .thenReturn(List.of());

        Optional<Seat> result = seatDao.findById(1);

        assertTrue(result.isEmpty());
    }

    // -------------------------------------------------------
    // TEST: findByIds() (Dynamic IN Clause)
    // -------------------------------------------------------
    @Test
    void testFindByIds() {

        when(jdbcTemplate.query(
                startsWith("SELECT * FROM Seat WHERE seat_id IN"),
                any(SeatRowMapper.class),
                any(Object[].class)
        )).thenReturn(List.of(createSeat()));

        List<Seat> result = seatDao.findByIds(List.of(1, 2, 3));

        assertEquals(1, result.size());
    }

    // -------------------------------------------------------
    // TEST: findSeatsByBooking()
    // -------------------------------------------------------
    @Test
    void testFindSeatsByBooking() {

        when(jdbcTemplate.queryForList(anyString(), eq(777)))
                .thenReturn(List.of(Map.of("seat_id", 1, "seat_type", "VIP")));

        List<Map<String, Object>> result = seatDao.findSeatsByBooking(777);

        assertEquals(1, result.size());
        assertEquals("VIP", result.get(0).get("seat_type"));
    }

    // -------------------------------------------------------
    // TEST: lockSeats()
    // -------------------------------------------------------
    @Test
    void testLockSeats() {

        when(jdbcTemplate.update(
                anyString(),
                any(Timestamp.class),
                anyInt()
        )).thenReturn(1);

        seatDao.lockSeats(List.of(1, 2));

        verify(jdbcTemplate, times(2))
                .update(anyString(), any(Timestamp.class), anyInt());
    }

    // -------------------------------------------------------
    // TEST: unlockSeats()
    // -------------------------------------------------------
    @Test
    void testUnlockSeats() {

        when(jdbcTemplate.update(anyString(), anyInt())).thenReturn(1);

        seatDao.unlockSeats(List.of(10, 11));

        verify(jdbcTemplate, times(2)).update(anyString(), anyInt());
    }

    // -------------------------------------------------------
    // TEST: bookSeats()
    // -------------------------------------------------------
    @Test
    void testBookSeats() {

        when(jdbcTemplate.update(anyString(), anyInt())).thenReturn(1);

        seatDao.bookSeats(List.of(5, 6, 7));

        verify(jdbcTemplate, times(3)).update(anyString(), anyInt());
    }

    // -------------------------------------------------------
    // TEST: unlockExpiredSeats()
    // -------------------------------------------------------
    @Test
    void testUnlockExpiredSeats() {

        when(jdbcTemplate.update(anyString())).thenReturn(1);

        seatDao.unlockExpiredSeats();

        verify(jdbcTemplate, times(1)).update(anyString());
    }
}
