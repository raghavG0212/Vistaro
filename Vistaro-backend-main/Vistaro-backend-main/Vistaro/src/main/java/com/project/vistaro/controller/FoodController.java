package com.project.vistaro.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.vistaro.dto.FoodDto;
import com.project.vistaro.model.Food;
import com.project.vistaro.service.FoodService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/food")
public class FoodController {
	
	@Autowired 
	private FoodService foodService;
	
	@PostMapping
	public FoodDto addFood(@Valid @RequestBody FoodDto dto) {
		Food entity = dtoToEntity(dto);
		Food saved = foodService.addFood(entity);
		
		return entityToDto(saved);
	}
	
	@GetMapping("/{id}")
	public FoodDto getFood(@PathVariable Integer id) {
		return entityToDto(foodService.getFoodById(id));
	}
	
	@GetMapping
	public List<FoodDto> getAllFood() {
		return foodService.getAllFoods().stream().map(this::entityToDto).collect(Collectors.toList());
	}
	
	@GetMapping("/slot/{slotId}")
	public List<FoodDto> getBySlot(@PathVariable Integer slotId){
		return foodService.getFoodsBySlot(slotId).stream().map(this::entityToDto).collect(Collectors.toList());
	}
	
	@DeleteMapping("/{id}")
	public String deleteFood(@PathVariable Integer id) {
		foodService.deleteFood(id);
		return "Food deleted successfully";
	}
	
	@PutMapping("/{id}")
	public FoodDto updateFood(@PathVariable Integer id, @Valid @RequestBody FoodDto dto) {
		Food updated = foodService.updateFood(id, dtoToEntity(dto));
		return entityToDto(updated);
	}
	
	//Mapping functions
	
	private Food dtoToEntity(FoodDto dto) {
		Food f = new Food();
		f.setFoodId(dto.getFoodId());
		f.setSlotId(dto.getSlotId());
		f.setName(dto.getName());
		f.setPrice(dto.getPrice());
		
		return f;
	}
	
	private FoodDto entityToDto(Food f) {
		FoodDto dto = new FoodDto();
		dto.setFoodId(f.getFoodId());
		dto.setSlotId(f.getSlotId());
		dto.setName(f.getName());
		dto.setPrice(f.getPrice());
		
		return dto;
	}

}
