package com.project.vistaro.model;

public class BookingFood {
	
	 private Integer bookingFoodId;
	    private Integer bookingId;
	    private Integer foodId;
	    private Integer quantity;

	    public Integer getBookingFoodId() { return bookingFoodId; }
	    public void setBookingFoodId(Integer bookingFoodId) { this.bookingFoodId = bookingFoodId; }

	    public Integer getBookingId() { return bookingId; }
	    public void setBookingId(Integer bookingId) { this.bookingId = bookingId; }

	    public Integer getFoodId() { return foodId; }
	    public void setFoodId(Integer foodId) { this.foodId = foodId; }

	    public Integer getQuantity() { return quantity; }
	    public void setQuantity(Integer quantity) { this.quantity = quantity; }
		
	    public BookingFood(Integer bookingFoodId, Integer bookingId, Integer foodId, Integer quantity) {
			super();
			this.bookingFoodId = bookingFoodId;
			this.bookingId = bookingId;
			this.foodId = foodId;
			this.quantity = quantity;
		}
		public BookingFood() {
			super();
		}

	    
	    
}
