package com.project.vistaro.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class FoodOrderDto {

	@NotNull(message = "Food ID is required")
	private Integer foodId;

	@Min(value = 1, message = "Quantity must be at least 1")
	private Integer quantity;

	public FoodOrderDto() {
	}

	public FoodOrderDto(Integer foodId, Integer quantity) {
		this.foodId = foodId;
		this.quantity = quantity;
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
