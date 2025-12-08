package com.project.vistaro.util;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.project.vistaro.model.Food;

public class FoodRowMapper implements RowMapper<Food>{

	@Override
	public Food mapRow(ResultSet rs, int rowNum) throws SQLException {
		Food food = new Food();
		food.setFoodId(rs.getInt("food_id"));
		food.setSlotId(rs.getInt("slot_id"));
		food.setName(rs.getString("name"));
		food.setPrice(rs.getBigDecimal("price"));
		
		
		return food;
	}

}
