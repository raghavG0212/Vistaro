package com.project.vistaro.service;

import java.util.List;

import com.project.vistaro.model.BookingFood;

public interface BookingFoodService {
	
	 BookingFood save(BookingFood bf);

	    BookingFood findById(Integer id);

	    List<BookingFood> findByBooking(Integer bookingId);

	    List<BookingFood> findAll();

	    BookingFood update(Integer id, BookingFood bf);

	    void delete(Integer id);

}
