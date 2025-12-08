package com.project.vistaro.dto;

import com.project.vistaro.model.PaymentMode;

import jakarta.validation.constraints.NotNull;

public class PaymentRequestDto {

    @NotNull(message = "Booking ID is required")
    private Integer bookingId;

    @NotNull(message = "Payment mode is required")
    private PaymentMode paymentMode;

    // (optional) you can add dummy fields like cardNumber, upiId etc, but we won't store them.

    public PaymentRequestDto() {}

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
}
