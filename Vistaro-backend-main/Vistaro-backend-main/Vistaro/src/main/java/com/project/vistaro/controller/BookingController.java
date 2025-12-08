package com.project.vistaro.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.project.vistaro.dto.BookingCreateDto;
import com.project.vistaro.dto.BookingResponseDto;
import com.project.vistaro.dto.CombinedBookingDetailsDto;
import com.project.vistaro.dto.RefundResponseDto;
import com.project.vistaro.service.BookingService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/booking")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    // ---------------------------------------------------
    // CREATE BOOKING
    // ---------------------------------------------------
    @PostMapping
    public ResponseEntity<BookingResponseDto> create(@Valid @RequestBody BookingCreateDto dto) {
        BookingResponseDto res = bookingService.createBooking(dto);
        return ResponseEntity.ok(res);
    }

    // ---------------------------------------------------
    // GET BOOKING (FULL DETAILS)
    // ---------------------------------------------------
    @GetMapping("/{bookingId}")
    public ResponseEntity<CombinedBookingDetailsDto> getById(@PathVariable int bookingId) {
        return ResponseEntity.ok(bookingService.getBooking(bookingId));
    }

    // ---------------------------------------------------
    // LIST BOOKINGS BY USER
    // ---------------------------------------------------
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CombinedBookingDetailsDto>> getByUser(@PathVariable int userId) {
        return ResponseEntity.ok(bookingService.getBookingsByUser(userId));
    }

    // ---------------------------------------------------
    // LIST BOOKINGS BY SLOT (ADMIN)
    // ---------------------------------------------------
    @GetMapping("/slot/{slotId}")
    public ResponseEntity<List<CombinedBookingDetailsDto>> getBySlot(@PathVariable int slotId) {
        return ResponseEntity.ok(bookingService.getBookingsBySlot(slotId));
    }

    // ---------------------------------------------------
    // CANCEL BOOKING + REFUND CALCULATION
    // ---------------------------------------------------
    @DeleteMapping("/{bookingId}")
    public ResponseEntity<RefundResponseDto> cancel(@PathVariable int bookingId) {
        return ResponseEntity.ok(bookingService.deleteBooking(bookingId));
    }

}
