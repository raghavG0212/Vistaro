package com.project.vistaro.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class BookingFoodRequestDto {

    @NotNull(message = "Food ID is required")
    private Integer foodId;

    @Positive(message = "Quantity must be greater than 0")
    private Integer quantity;

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
