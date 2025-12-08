package com.project.vistaro.dto;

import java.time.LocalDateTime;

import com.project.vistaro.model.PaymentMode;
import com.project.vistaro.model.PaymentStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class PaymentDto {
	
	public Integer paymentId;
	
	@NotNull(message = "Booking ID is required")
	public Integer bookingId;
	
	@NotNull(message = "Payment mode is required")
	public PaymentMode paymentMode;
	
	@NotBlank(message = "Transaction ID is required")
	public String transactionId;
	
	@NotNull(message = "Payment status is required")
	public PaymentStatus paymentStatus;
	
	public LocalDateTime paidAt;
	
	public PaymentDto() {}

	public PaymentDto(Integer paymentId, @NotNull(message = "Booking ID is required") Integer bookingId,
			@NotNull(message = "Payment mode is required") PaymentMode paymentMode,
			@NotBlank(message = "Transaction ID is required") String transactionId,
			@NotNull(message = "Payment status is required") PaymentStatus paymentStatus, LocalDateTime paidAt) {
		super();
		this.paymentId = paymentId;
		this.bookingId = bookingId;
		this.paymentMode = paymentMode;
		this.transactionId = transactionId;
		this.paymentStatus = paymentStatus;
		this.paidAt = paidAt;
	}

	public PaymentDto(Integer paymentId, @NotNull(message = "Booking ID is required") Integer bookingId,
			@NotNull(message = "Payment mode is required") PaymentMode paymentMode,
			@NotBlank(message = "Transaction ID is required") String transactionId,
			@NotNull(message = "Payment status is required") PaymentStatus paymentStatus) {
		super();
		this.paymentId = paymentId;
		this.bookingId = bookingId;
		this.paymentMode = paymentMode;
		this.transactionId = transactionId;
		this.paymentStatus = paymentStatus;
	}

	public Integer getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(Integer paymentId) {
		this.paymentId = paymentId;
	}

	public Integer getBookingId() {
		return bookingId;
	}

	public void setBookingId(Integer bookingId) {
		this.bookingId = bookingId;
	}

	public PaymentMode getPaymentMode() {
		return paymentMode;
	}

	public void setPaymentMode(PaymentMode paymentMode) {
		this.paymentMode = paymentMode;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public PaymentStatus getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(PaymentStatus paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public LocalDateTime getPaidAt() {
		return paidAt;
	}

	public void setPaidAt(LocalDateTime paidAt) {
		this.paidAt = paidAt;
	}
	
	
	
}
