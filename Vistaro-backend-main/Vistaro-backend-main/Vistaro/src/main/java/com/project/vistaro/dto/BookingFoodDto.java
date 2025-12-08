package com.project.vistaro.dto;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
public class BookingFoodDto {
	
	 private Integer bookingFoodId;

	    @NotNull(message = "bookingId is required")
	    private Integer bookingId;

	    @NotNull(message = "foodId is required")
	    private Integer foodId;

	    @Positive(message = "quantity must be greater than 0")
	    private Integer quantity;

		public Integer getBookingFoodId() {
			return bookingFoodId;
		}

		public void setBookingFoodId(Integer bookingFoodId) {
			this.bookingFoodId = bookingFoodId;
		}

		public Integer getBookingId() {
			return bookingId;
		}

		public void setBookingId(Integer bookingId) {
			this.bookingId = bookingId;
		}

		public Integer getFoodId() {
			return foodId;
		}

		public void setFoodId(Integer foodId) {
			this.foodId = foodId;
		}

		public Integer getQuantity() {
			return quantity;
		}

		public void setQuantity(Integer quantity) {
			this.quantity = quantity;
		}
	    
	    
	    

}
