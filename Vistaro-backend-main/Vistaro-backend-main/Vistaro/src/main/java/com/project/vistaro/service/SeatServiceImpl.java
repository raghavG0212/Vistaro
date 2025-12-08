package com.project.vistaro.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.project.vistaro.model.Seat;
import com.project.vistaro.repository.SeatDao;

@Service
public class SeatServiceImpl implements SeatService {

    private final SeatDao seatDao;

    public SeatServiceImpl(SeatDao seatDao) {
        this.seatDao = seatDao;
    }

    @Override
    public List<Seat> getSeatsBySlot(int slotId) {
        return seatDao.findBySlot(slotId);
    }

    @Override
    public void lockSeats(List<Integer> seatIds) {
        seatDao.lockSeats(seatIds);
    }

    @Override
    public void unlockSeats(List<Integer> seatIds) {
        seatDao.unlockSeats(seatIds);
    }

    @Override
    public void bookSeats(List<Integer> seatIds) {
        seatDao.bookSeats(seatIds);
    }

    @Override
    public void unlockExpiredSeats() {
        seatDao.unlockExpiredSeats();
    }
}
