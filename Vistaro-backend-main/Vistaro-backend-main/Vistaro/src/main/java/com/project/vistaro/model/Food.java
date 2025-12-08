package com.project.vistaro.model;

import java.math.BigDecimal;

public class Food {
	
	private Integer foodId;
	private Integer slotId;
	private String name;
	private BigDecimal price;
	
	
	
	public Food(Integer foodId, Integer slotId, String name, BigDecimal price) {
		super();
		this.foodId = foodId;
		this.slotId = slotId;
		this.name = name;
		this.price = price;
	}



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


	public Food() {
		super();
	}
	
	

}
