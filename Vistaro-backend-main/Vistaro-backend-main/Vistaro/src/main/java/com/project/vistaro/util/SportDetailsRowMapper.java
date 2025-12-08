package com.project.vistaro.util;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.project.vistaro.model.SportDetails;

public class SportDetailsRowMapper implements RowMapper<SportDetails> {
	
	@Override
    public SportDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
        SportDetails sport = new SportDetails();
        sport.setSportDetailsId(rs.getInt("sport_details_id"));
        sport.setSportType(rs.getString("sport_type"));
        sport.setTeam1(rs.getString("team1"));
        sport.setTeam2(rs.getString("team2"));
        sport.setMatchFormat(rs.getString("match_format"));
        sport.setVenueInfo(rs.getString("venue_info"));
        if (rs.getTimestamp("start_time") != null) {
            sport.setStartTime(rs.getTimestamp("start_time").toLocalDateTime());
        }
        if (rs.getTimestamp("end_time") != null) {
            sport.setEndTime(rs.getTimestamp("end_time").toLocalDateTime());
        }
        return sport;
    }
	

}

