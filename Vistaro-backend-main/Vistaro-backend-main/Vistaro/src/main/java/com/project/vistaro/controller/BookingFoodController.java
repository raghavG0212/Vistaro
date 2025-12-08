package com.project.vistaro.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.vistaro.dto.BookingFoodDto;
import com.project.vistaro.model.BookingFood;
import com.project.vistaro.service.BookingFoodService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/v1/booking-food")
public class BookingFoodController {
	
	 @Autowired
	    private BookingFoodService service;

	    @PostMapping("/add")
	    public BookingFoodDto add(@Valid @RequestBody BookingFoodDto dto) {
	        BookingFood saved = service.save(dtoToEntity(dto));
	        return entityToDto(saved);
	    }

	    @GetMapping("/{id}")
	    public BookingFoodDto getById(@PathVariable Integer id) {
	        return entityToDto(service.findById(id));
	    }

	    @GetMapping("/booking/{bookingId}")
	    public List<BookingFoodDto> getByBooking(@PathVariable Integer bookingId) {
	        return service.findByBooking(bookingId)
	                .stream().map(this::entityToDto).toList();
	    }

	    @GetMapping
	    public List<BookingFoodDto> getAll() {
	        return service.findAll()
	                .stream().map(this::entityToDto).toList();
	    }

	    @PutMapping("/{id}")
	    public BookingFoodDto update(@PathVariable Integer id,
	                                 @Valid @RequestBody BookingFoodDto dto) {
	        BookingFood updated = service.update(id, dtoToEntity(dto));
	        return entityToDto(updated);
	    }

	    @DeleteMapping("/{id}")
	    public String delete(@PathVariable Integer id) {
	        service.delete(id);
	        return "Food item removed from booking.";
	    }

	    // DTO ↔ Entity
	    private BookingFood dtoToEntity(BookingFoodDto dto) {
	        BookingFood bf = new BookingFood();
	        bf.setBookingFoodId(dto.getBookingFoodId());
	        bf.setBookingId(dto.getBookingId());
	        bf.setFoodId(dto.getFoodId());
	        bf.setQuantity(dto.getQuantity());
	        return bf;
	    }

	    private BookingFoodDto entityToDto(BookingFood bf) {
	        BookingFoodDto dto = new BookingFoodDto();
	        dto.setBookingFoodId(bf.getBookingFoodId());
	        dto.setBookingId(bf.getBookingId());
	        dto.setFoodId(bf.getFoodId());
	        dto.setQuantity(bf.getQuantity());
	        return dto;
	    }

}
