package com.project.vistaro.service;

import com.project.vistaro.model.Seat;
import com.project.vistaro.repository.SeatDao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class SeatServiceImplTest {

    private SeatDao seatDao;
    private SeatServiceImpl seatService;

    @BeforeEach
    void setup() {
        seatDao = Mockito.mock(SeatDao.class);
        seatService = new SeatServiceImpl(seatDao);
    }

    private Seat createSeat() {
        Seat seat = new Seat();
        seat.setSeatId(1);
        seat.setSlotId(101);
        seat.setRowLabel("A");
        seat.setSeatNumber("10");   // ✔ correct type
        seat.setSeatType("VIP");
        seat.setPrice(BigDecimal.valueOf(600));
        seat.setIsBooked(false);
        seat.setIsLocked(false);
        return seat;
    }

    // -----------------------------------------------------------
    // TEST: getSeatsBySlot
    // -----------------------------------------------------------
    @Test
    void testGetSeatsBySlot() {
        when(seatDao.findBySlot(101)).thenReturn(List.of(createSeat()));

        List<Seat> result = seatService.getSeatsBySlot(101);

        assertEquals(1, result.size());
        verify(seatDao, times(1)).findBySlot(101);
    }

    // -----------------------------------------------------------
    // TEST: lockSeats
    // -----------------------------------------------------------
    @Test
    void testLockSeats() {
        doNothing().when(seatDao).lockSeats(List.of(1, 2, 3));

        seatService.lockSeats(List.of(1, 2, 3));

        verify(seatDao, times(1)).lockSeats(List.of(1, 2, 3));
    }

    // -----------------------------------------------------------
    // TEST: unlockSeats
    // -----------------------------------------------------------
    @Test
    void testUnlockSeats() {
        doNothing().when(seatDao).unlockSeats(List.of(4, 5));

        seatService.unlockSeats(List.of(4, 5));

        verify(seatDao, times(1)).unlockSeats(List.of(4, 5));
    }

    // -----------------------------------------------------------
    // TEST: bookSeats
    // -----------------------------------------------------------
    @Test
    void testBookSeats() {
        doNothing().when(seatDao).bookSeats(List.of(11, 12));

        seatService.bookSeats(List.of(11, 12));

        verify(seatDao, times(1)).bookSeats(List.of(11, 12));
    }

    // -----------------------------------------------------------
    // TEST: unlockExpiredSeats
    // -----------------------------------------------------------
    @Test
    void testUnlockExpiredSeats() {
        doNothing().when(seatDao).unlockExpiredSeats();

        seatService.unlockExpiredSeats();

        verify(seatDao, times(1)).unlockExpiredSeats();
    }
}
