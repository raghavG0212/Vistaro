package com.project.vistaro.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Booking {

    private Integer bookingId;
    private Integer userId;
    private Integer slotId;
    private String seats;

    private BigDecimal ticketTotal;
    private BigDecimal foodTotal;
    private BigDecimal finalAmount;

    private String offerApplied;
    private LocalDateTime createdAt;

    public Booking() {}

	public Booking(Integer bookingId, Integer userId, Integer slotId, String seats, BigDecimal ticketTotal,
			BigDecimal foodTotal, BigDecimal finalAmount, String offerApplied, LocalDateTime createdAt) {
		super();
		this.bookingId = bookingId;
		this.userId = userId;
		this.slotId = slotId;
		this.seats = seats;
		this.ticketTotal = ticketTotal;
		this.foodTotal = foodTotal;
		this.finalAmount = finalAmount;
		this.offerApplied = offerApplied;
		this.createdAt = createdAt;
	}

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

	public String getSeats() {
		return seats;
	}

	public void setSeats(String seats) {
		this.seats = seats;
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
