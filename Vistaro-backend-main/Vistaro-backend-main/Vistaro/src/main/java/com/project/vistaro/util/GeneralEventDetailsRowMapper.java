package com.project.vistaro.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.springframework.jdbc.core.RowMapper;

import com.project.vistaro.model.GeneralEventDetails;

public class GeneralEventDetailsRowMapper implements RowMapper<GeneralEventDetails>{
	
	

	@Override
	public GeneralEventDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
		GeneralEventDetails d = new GeneralEventDetails();
		d.setGeneralDetailsId(rs.getInt("general_details_id"));
		d.setEventId(rs.getInt("event_id"));
		d.setArtist(rs.getString("artist"));
		d.setHost(rs.getString("host"));
		d.setGenre(rs.getString("genre"));
		
		Timestamp st = rs.getTimestamp("start_time");
		Timestamp et = rs.getTimestamp("end_time");
		
		d.setStartTime(st!= null ? st.toLocalDateTime() :null);
		d.setEndTime(et!= null ? st.toLocalDateTime() :null);
		
		d.setAdditionalInfo(rs.getString("additional_info"));
		
		return d;
	}
	
	

}
