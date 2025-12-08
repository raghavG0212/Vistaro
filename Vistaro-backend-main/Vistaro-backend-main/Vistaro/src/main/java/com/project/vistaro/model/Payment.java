package com.project.vistaro.model;

import java.time.LocalDateTime;

public class Payment {
	
	 private Integer paymentId;
	 private Integer bookingId;
	 private PaymentMode paymentMode; // CARD, UPI, etc.
	 private String transactionId;
	 private PaymentStatus paymentStatus; // SUCCESS, FAILED, PENDING
	 private LocalDateTime paidAt;
	 
	 public Payment() {}
	 
	 public Payment(Integer paymentId, Integer bookingId, PaymentMode paymentMode, String transactionId,
			PaymentStatus paymentStatus, LocalDateTime paidAt) {
		super();
		this.paymentId = paymentId;
		this.bookingId = bookingId;
		this.paymentMode = paymentMode;
		this.transactionId = transactionId;
		this.paymentStatus = paymentStatus;
		this.paidAt = paidAt;
	 }
	 
	 public Payment(Integer paymentId, Integer bookingId, PaymentMode paymentMode, String transactionId,
			 PaymentStatus paymentStatus) {
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
