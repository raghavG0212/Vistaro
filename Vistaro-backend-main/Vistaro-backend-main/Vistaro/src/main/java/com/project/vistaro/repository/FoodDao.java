package com.project.vistaro.repository;

import java.util.List;

import com.project.vistaro.model.Food;

public interface FoodDao {
	
	Food save(Food food);
	Food findById(Integer foodId);
	List<Food> findAll();
	List<Food> findBySlotId(Integer slotId);
	void delete(Integer foodId);
	Food update(Food food);
}
