package com.project.vistaro.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class FoodDto {
	
	private Integer foodId;
	
	@NotNull(message = "Slot ID is required")
	private Integer slotId;
	
	@NotBlank(message = "Food name is required")
	private String name;
	
	@NotNull(message = "Price is required")
	@DecimalMin(value = "1.0", message = "Price must be >= 1")
	private BigDecimal price;
	
	public Integer getFoodId() {
		return foodId;
	}

	public void setFoodId(Integer foodId) {
		this.foodId = foodId;
	}

	public Integer getSlotId() {
		return slotId;
	}

	public void setSlotId(Integer slotId) {
		this.slotId = slotId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	
	
	

}
