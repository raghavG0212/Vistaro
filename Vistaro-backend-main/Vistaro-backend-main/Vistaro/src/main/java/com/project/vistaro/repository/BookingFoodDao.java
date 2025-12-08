package com.project.vistaro.repository;

import com.project.vistaro.model.BookingFood;
import java.util.List;
import java.util.Map;

public interface BookingFoodDao {
    BookingFood save(BookingFood bf);

    BookingFood findById(Integer id);

    List<BookingFood> findByBookingId(Integer bookingId);

    List<BookingFood> findAll();

    BookingFood update(BookingFood bf);

    void delete(Integer id);

    List<Map<String, Object>> findByBookingIdDetailed(Integer bookingId);
}
