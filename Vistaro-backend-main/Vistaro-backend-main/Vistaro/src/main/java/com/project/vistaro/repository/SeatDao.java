package com.project.vistaro.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.project.vistaro.model.Seat;

public interface SeatDao {

    void save(Seat seat);

    List<Seat> findBySlot(int slotId);

    Optional<Seat> findById(int seatId);

    List<Seat> findByIds(List<Integer> seatIds);

    List<Map<String, Object>> findSeatsByBooking(int bookingId);

    void lockSeats(List<Integer> seatIds);

    void unlockSeats(List<Integer> seatIds);

    void bookSeats(List<Integer> seatIds);

    void unlockExpiredSeats();
    
    void releaseSeats(List<Integer> seatIds);
}
