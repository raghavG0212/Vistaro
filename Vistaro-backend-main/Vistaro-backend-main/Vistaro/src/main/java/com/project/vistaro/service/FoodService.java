package com.project.vistaro.service;

import java.util.List;

import com.project.vistaro.model.Food;

public interface FoodService {
	
	Food addFood(Food food);
	Food getFoodById(Integer foodId);
	List<Food> getAllFoods();
	List<Food> getFoodsBySlot(Integer slotId);
	void deleteFood(Integer foodId);
	Food updateFood(Integer id, Food food);

}
