package com.project.vistaro.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.project.vistaro.model.Food;
import com.project.vistaro.repository.FoodDao;

@Service
public class FoodServiceImpl implements FoodService{
	
	private final FoodDao foodDao;
	public FoodServiceImpl(FoodDao foodDao) {
		this.foodDao = foodDao;
	}
	
	@Override
	public Food addFood(Food food) {
		return foodDao.save(food);
	}
	
	@Override
	public Food getFoodById(Integer foodId) {
		return foodDao.findById(foodId);
	}
	
	@Override
	public List<Food> getAllFoods(){
		return foodDao.findAll();
	}
	
	@Override
	public List<Food> getFoodsBySlot(Integer slotId){
		return foodDao.findBySlotId(slotId);
	}
	
	@Override
	public void deleteFood(Integer foodId) {
		foodDao.delete(foodId);
	}
	
	@Override
	public Food updateFood(Integer id, Food f) {
		Food existing = foodDao.findById(id);
		if(existing == null) {
			throw new RuntimeException("Food with id: "+id + " not found");
		}
		existing.setName(f.getName());
		existing.setSlotId(f.getSlotId());
		existing.setPrice(f.getPrice());
		
		return foodDao.update(existing);
	}

}
