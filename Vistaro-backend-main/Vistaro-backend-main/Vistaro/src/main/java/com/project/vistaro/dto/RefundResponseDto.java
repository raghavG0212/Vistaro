package com.project.vistaro.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class RefundResponseDto {

    private Integer bookingId;

    private BigDecimal finalAmount;
    private BigDecimal refundAmount;
    private String refundPercentage;

    private String paymentStatus;
    private String transactionId;

    private LocalDateTime refundedAt;

    private List<Map<String, Object>> seats;
    private List<Map<String, Object>> foodItems;
	public Integer getBookingId() {
		return bookingId;
	}
	public void setBookingId(Integer bookingId) {
		this.bookingId = bookingId;
	}
	public BigDecimal getFinalAmount() {
		return finalAmount;
	}
	public void setFinalAmount(BigDecimal finalAmount) {
		this.finalAmount = finalAmount;
	}
	public BigDecimal getRefundAmount() {
		return refundAmount;
	}
	public void setRefundAmount(BigDecimal refundAmount) {
		this.refundAmount = refundAmount;
	}
	public String getRefundPercentage() {
		return refundPercentage;
	}
	public void setRefundPercentage(String refundPercentage) {
		this.refundPercentage = refundPercentage;
	}
	public String getPaymentStatus() {
		return paymentStatus;
	}
	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public LocalDateTime getRefundedAt() {
		return refundedAt;
	}
	public void setRefundedAt(LocalDateTime refundedAt) {
		this.refundedAt = refundedAt;
	}
	public List<Map<String, Object>> getSeats() {
		return seats;
	}
	public void setSeats(List<Map<String, Object>> seats) {
		this.seats = seats;
	}
	public List<Map<String, Object>> getFoodItems() {
		return foodItems;
	}
	public void setFoodItems(List<Map<String, Object>> foodItems) {
		this.foodItems = foodItems;
	}

    
}
