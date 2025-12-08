package com.project.vistaro.util;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.project.vistaro.model.Venue;
import com.project.vistaro.model.VenueType;

public class VenueRowMapper implements RowMapper<Venue>{

	@Override
	public Venue mapRow(ResultSet rs, int rowNum) throws SQLException {
		Venue v = new Venue();
		v.setVenueId(rs.getInt("venue_id"));
		v.setName(rs.getString("name"));
		v.setAddress(rs.getString("address"));
		v.setCity(rs.getString("city"));
		v.setVenueType(VenueType.valueOf(rs.getString("venue_type")));
		v.setScreenName(rs.getString("screen_name"));
		v.setSeatLayoutJson(rs.getString("seat_layout_json"));
		return v;
	}
	
}
