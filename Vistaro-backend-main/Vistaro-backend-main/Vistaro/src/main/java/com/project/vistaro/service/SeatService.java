package com.project.vistaro.service;

import java.util.List;

import com.project.vistaro.model.Seat;

public interface SeatService {

    List<Seat> getSeatsBySlot(int slotId);

    void lockSeats(List<Integer> seatIds);

    void unlockSeats(List<Integer> seatIds);

    void bookSeats(List<Integer> seatIds);

    void unlockExpiredSeats();
}
