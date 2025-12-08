package com.project.vistaro.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.vistaro.dto.EventDto;
import com.project.vistaro.dto.PaymentDto;
import com.project.vistaro.model.Event;
import com.project.vistaro.model.Payment;
import com.project.vistaro.service.PaymentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/payment")
public class PaymentController {
	
	private final PaymentService paymentService;

	public PaymentController(PaymentService paymentService) {
		super();
		this.paymentService = paymentService;
	}
	
	private Payment dtoToEntity(PaymentDto dto) {
		Payment p = new Payment();
		p.setPaymentId(dto.getPaymentId());
		p.setBookingId(dto.getBookingId());
		p.setPaymentMode(dto.getPaymentMode());
		p.setTransactionId(dto.getTransactionId());
		p.setPaymentStatus(dto.getPaymentStatus());
		return p;
	}
	
	private PaymentDto entityToDto(Payment p) {
		PaymentDto dto = new PaymentDto();
		dto.setPaymentId(p.getPaymentId());
		dto.setBookingId(p.getBookingId());
		dto.setPaymentMode(p.getPaymentMode());
		dto.setTransactionId(p.getTransactionId());
		dto.setPaymentStatus(p.getPaymentStatus());
		return dto;
	}
	
	@PostMapping
	public ResponseEntity<PaymentDto> create(@Valid @RequestBody PaymentDto dto){
		Payment p= dtoToEntity(dto);
		Payment created = paymentService.save(p);
		return ResponseEntity.created(URI.create("/api/v1/payment/" + created.getPaymentId()))
    .body(entityToDto(created));
		
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<PaymentDto> getById(@PathVariable int id) {
		PaymentDto res = entityToDto(paymentService.findById(id));
		return ResponseEntity.ok(res);
	}
	

}
