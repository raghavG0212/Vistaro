package com.project.vistaro.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class BookingResponseDto {

    private Integer bookingId;
    private Integer userId;
    private Integer slotId;

    private BigDecimal ticketTotal;
    private BigDecimal foodTotal;
    private BigDecimal finalAmount;

    private String offerApplied;
    private LocalDateTime createdAt;

    // getters/setters

    public Integer getBookingId() {
        return bookingId;
    }
    public void setBookingId(Integer bookingId) {
        this.bookingId = bookingId;
    }

    public Integer getUserId() {
        return userId;
    }
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getSlotId() {
        return slotId;
    }
    public void setSlotId(Integer slotId) {
        this.slotId = slotId;
    }

    public BigDecimal getTicketTotal() {
        return ticketTotal;
    }
    public void setTicketTotal(BigDecimal ticketTotal) {
        this.ticketTotal = ticketTotal;
    }

    public BigDecimal getFoodTotal() {
        return foodTotal;
    }
    public void setFoodTotal(BigDecimal foodTotal) {
        this.foodTotal = foodTotal;
    }

    public BigDecimal getFinalAmount() {
        return finalAmount;
    }
    public void setFinalAmount(BigDecimal finalAmount) {
        this.finalAmount = finalAmount;
    }

    public String getOfferApplied() {
        return offerApplied;
    }
    public void setOfferApplied(String offerApplied) {
        this.offerApplied = offerApplied;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
