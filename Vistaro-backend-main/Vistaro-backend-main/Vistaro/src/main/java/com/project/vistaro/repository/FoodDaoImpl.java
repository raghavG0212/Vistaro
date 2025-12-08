package com.project.vistaro.repository;


import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Types;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.project.vistaro.model.Food;
import com.project.vistaro.util.FoodRowMapper;

@Repository
public class FoodDaoImpl implements FoodDao{
	
	private final JdbcTemplate jdbcTemplate;
	private final FoodRowMapper mapper = new FoodRowMapper();
	
	public FoodDaoImpl(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
		
	}
	
	

	@Override 
	public Food save(Food food) {
		String sql = "INSERT INTO food(slot_id, name, price) VALUES (?, ?, ?)";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(conn -> {
			PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			
			
			ps.setInt(1, food.getSlotId());
			ps.setString(2, food.getName());
			ps.setBigDecimal(3, food.getPrice());
			
			return ps;
			
		}, keyHolder);
		if(keyHolder.getKey() != null) {
			food.setFoodId(keyHolder.getKey().intValue());
		}
		return food;
	}
	
	@Override
	public Food update(Food food) {
		String sql = "UPDATE food SET slot_id = ?, name = ?, price = ? WHERE food_id = ?";
		
		jdbcTemplate.update(sql,
				food.getSlotId(),
				food.getName(),
				food.getPrice(),
				food.getFoodId());
		
		return food;
	}

	@Override
	public Food findById(Integer foodId) {
		String sql = "SELECT * FROM food WHERE food_id = ?";
		
		return jdbcTemplate.queryForObject(sql, mapper, foodId);
	}
	
	@Override
	public List<Food> findAll(){
		String sql = "SELECT * FROM food";
		return jdbcTemplate.query(sql, mapper);
	}
	
	
	
	@Override
	public void delete(Integer foodId) {
		String sql = "DELETE FROM food WHERE food_id = ?";
		jdbcTemplate.update(sql, foodId);
	}

	@Override
	public List<Food> findBySlotId(Integer slotId) {
		String sql = "SELECT * FROM food WHERE slot_id = ?";
		
		return jdbcTemplate.query(sql, mapper, slotId);
	}
	
	
}
