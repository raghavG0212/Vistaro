package com.project.vistaro.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.project.vistaro.model.Payment;

public class CombinedBookingDetailsDto {

    private Integer bookingId;
    private Integer userId;
    private Integer slotId;

    private String userName;
    private String eventTitle;
    private String venueName;

    private String showStart;
    private String showEnd;

    private BigDecimal ticketTotal;
    private BigDecimal foodTotal;
    private BigDecimal finalAmount;

    private String offerApplied;
    private LocalDateTime createdAt;

    private List<Map<String, Object>> seats;
    private List<Map<String, Object>> foodItems;

    private PaymentResponseDto payment;


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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEventTitle() {
		return eventTitle;
	}

	public void setEventTitle(String eventTitle) {
		this.eventTitle = eventTitle;
	}

	public String getVenueName() {
		return venueName;
	}

	public void setVenueName(String venueName) {
		this.venueName = venueName;
	}

	public String getShowStart() {
		return showStart;
	}

	public void setShowStart(String showStart) {
		this.showStart = showStart;
	}

	public String getShowEnd() {
		return showEnd;
	}

	public void setShowEnd(String showEnd) {
		this.showEnd = showEnd;
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

	public PaymentResponseDto getPayment() {
		return payment;
	}

	public void setPayment(PaymentResponseDto payment) {
		this.payment = payment;
	}

   
}
