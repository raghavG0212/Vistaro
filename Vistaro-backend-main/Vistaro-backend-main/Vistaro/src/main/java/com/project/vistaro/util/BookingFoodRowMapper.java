package com.project.vistaro.util;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.project.vistaro.model.BookingFood;

public class BookingFoodRowMapper implements RowMapper<BookingFood>{
	
	 @Override
	    public BookingFood mapRow(ResultSet rs, int rowNum) throws SQLException {
	        BookingFood bf = new BookingFood();
	        bf.setBookingFoodId(rs.getInt("booking_food_id"));
	        bf.setBookingId(rs.getInt("booking_id"));
	        bf.setFoodId(rs.getInt("food_id"));
	        bf.setQuantity(rs.getInt("quantity"));
	        return bf;
	    }

}
