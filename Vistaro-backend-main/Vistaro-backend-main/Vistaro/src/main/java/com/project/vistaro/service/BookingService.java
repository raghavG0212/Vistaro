package com.project.vistaro.service;

import java.util.List;

import com.project.vistaro.dto.BookingCreateDto;
import com.project.vistaro.dto.BookingResponseDto;
import com.project.vistaro.dto.CombinedBookingDetailsDto;
import com.project.vistaro.dto.RefundResponseDto;


public interface BookingService {

    BookingResponseDto createBooking(BookingCreateDto dto);
    CombinedBookingDetailsDto getBooking(int bookingId);
    List<CombinedBookingDetailsDto> getBookingsByUser(int userId);
    List<CombinedBookingDetailsDto> getBookingsBySlot(int slotId);
    RefundResponseDto deleteBooking(int bookingId);
}
