package com.project.vistaro.repository;

import java.util.List;
import java.util.Optional;
import java.util.Map;

import com.project.vistaro.model.Booking;

public interface BookingDao {

    Booking save(Booking booking);

    Optional<Booking> findById(int bookingId);

    List<Booking> findByUserId(int userId);

    List<Booking> findBySlotId(int slotId);

    int deleteById(int bookingId);

    Map<String, Object> getBookingFullDetails(int bookingId);
}
