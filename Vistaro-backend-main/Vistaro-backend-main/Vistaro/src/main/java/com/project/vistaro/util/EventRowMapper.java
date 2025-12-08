package com.project.vistaro.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.springframework.jdbc.core.RowMapper;

import com.project.vistaro.model.Event;
import com.project.vistaro.model.EventCategory;

public class EventRowMapper implements RowMapper<Event>{

	@Override
	public Event mapRow(ResultSet rs, int rowNum) throws SQLException {
		// TODO Auto-generated method stub
		Event e= new Event();
		e.setEventId(rs.getInt("event_id"));
		e.setTitle(rs.getString("title"));
		e.setDescription(rs.getString("description"));
		e.setCategory(EventCategory.valueOf(rs.getString("category")));
		e.setSubCategory(rs.getString("sub_category"));
		e.setBannerUrl(rs.getString("banner_url"));
		e.setThumbnailUrl(rs.getString("thumbnail_url"));
		Timestamp startTime = rs.getTimestamp("start_time");
		if(startTime!=null) e.setStartTime(startTime.toLocalDateTime());
		Timestamp endTime = rs.getTimestamp("end_time");
		if(endTime!=null) e.setEndTime(endTime.toLocalDateTime());
		Timestamp created = rs.getTimestamp("created_at");
		if(created != null) e.setCreatedAt(created.toLocalDateTime());
		Timestamp updated = rs.getTimestamp("updated_at");
		if(updated != null) e.setUpdatedAt(updated.toLocalDateTime());
		
		return e;
		
	}

}
