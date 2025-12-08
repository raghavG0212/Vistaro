package com.project.vistaro.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.project.vistaro.model.BookingFood;
import com.project.vistaro.repository.BookingFoodDao;

@Service
public class BookingFoodServiceImpl implements BookingFoodService{
	
	private final BookingFoodDao dao;

    public BookingFoodServiceImpl(BookingFoodDao dao) {
        this.dao = dao;
    }

    @Override
    public BookingFood save(BookingFood bf) {
        return dao.save(bf);
    }

    @Override
    public BookingFood findById(Integer id) {
        return dao.findById(id);
    }

    @Override
    public List<BookingFood> findByBooking(Integer bookingId) {
        return dao.findByBookingId(bookingId);
    }

    @Override
    public List<BookingFood> findAll() {
        return dao.findAll();
    }

    @Override
    public BookingFood update(Integer id, BookingFood bf) {
        bf.setBookingFoodId(id);
        return dao.update(bf);
    }

    @Override
    public void delete(Integer id) {
        dao.delete(id);
    }

}
