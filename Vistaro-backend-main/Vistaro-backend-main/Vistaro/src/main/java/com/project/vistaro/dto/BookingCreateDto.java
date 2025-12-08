package com.project.vistaro.dto;

import java.util.List;

import com.project.vistaro.model.PaymentMode;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class BookingCreateDto {

//    @NotNull(message = "User ID is required")
    private Integer userId;

    @NotNull(message = "Slot ID is required")
    private Integer slotId;

    @NotEmpty(message = "Seat IDs cannot be empty")
    private List<Integer> seatIds;
    
    @NotNull(message = "Payment mode is required")
    private PaymentMode paymentMode;


    private String offerCode;      // optional
    private String giftCardCode;   // optional

    private List<BookingFoodRequestDto> foodItems; // optional

    // --- getters/setters ---

    public Integer getUserId() {
        return userId;
    }
    public PaymentMode getPaymentMode() {
		return paymentMode;
	}
	public void setPaymentMode(PaymentMode paymentMode) {
		this.paymentMode = paymentMode;
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

    public List<Integer> getSeatIds() {
        return seatIds;
    }
    public void setSeatIds(List<Integer> seatIds) {
        this.seatIds = seatIds;
    }

    public String getOfferCode() {
        return offerCode;
    }
    public void setOfferCode(String offerCode) {
        this.offerCode = offerCode;
    }

    public String getGiftCardCode() {
        return giftCardCode;
    }
    public void setGiftCardCode(String giftCardCode) {
        this.giftCardCode = giftCardCode;
    }

    public List<BookingFoodRequestDto> getFoodItems() {
        return foodItems;
    }
    public void setFoodItems(List<BookingFoodRequestDto> foodItems) {
        this.foodItems = foodItems;
    }
}
