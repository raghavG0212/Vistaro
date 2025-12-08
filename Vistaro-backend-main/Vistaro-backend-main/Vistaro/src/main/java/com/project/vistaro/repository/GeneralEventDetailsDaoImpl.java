package com.project.vistaro.repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.project.vistaro.model.GeneralEventDetails;
import com.project.vistaro.util.GeneralEventDetailsRowMapper;

@Repository
public class GeneralEventDetailsDaoImpl implements GeneralEventDetailsDao{
	
	private final JdbcTemplate jdbcTemplate;
	private final GeneralEventDetailsRowMapper mapper = new GeneralEventDetailsRowMapper();
	
	public GeneralEventDetailsDaoImpl(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public GeneralEventDetails save(GeneralEventDetails d) {
		String sql = "INSERT INTO generaleventdetails (event_id, artist, host, genre, start_time, end_time, additional_info) VALUES (?, ?, ?, ?, ?, ?, ?)";
		
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(conn -> {
			PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			
			ps.setInt(1, d.getEventId());
			ps.setString(2, d.getArtist());
			ps.setString(3, d.getHost());
			ps.setString(4, d.getGenre());
			ps.setTimestamp(5, java.sql.Timestamp.valueOf(d.getStartTime()));
			ps.setTimestamp(6, java.sql.Timestamp.valueOf(d.getEndTime()));
			ps.setString(7, d.getAdditionalInfo());
			
			return ps;
			
		}, keyHolder);
		
		if(keyHolder.getKey()!= null ) d.setGeneralDetailsId(keyHolder.getKey().intValue());
		
		return d;
	}

	@Override
	public GeneralEventDetails findById(Integer id) {
		String sql = "SELECT * FROM generaleventdetails WHERE general_details_id = ?";
		
		return jdbcTemplate.queryForObject(sql, mapper, id);
	}

	@Override
	public GeneralEventDetails findByEventId(Integer eventId) {
		String sql = "SELECT * FROM generaleventdetails where event_id=?";
		return jdbcTemplate.queryForObject(sql, mapper, eventId);
	}

	@Override
	public List<GeneralEventDetails> findAll() {
		String sql = "SELECT * FROM generaleventdetails";
		return jdbcTemplate.query(sql, mapper);
	}

	@Override
	public void delete(Integer id) {
		String sql = "DELETE FROM generaleventdetails WHERE general_details_id=?";
		
		jdbcTemplate.update(sql, id);
		
		
	}
	
	@Override
	public GeneralEventDetails update(Integer id, GeneralEventDetails d) {
		String sql = "UPDATE generaleventdetails SET artist = ?, host = ?, genre = ?, start_time = ?, end_time = ?, additional_info = ? WHERE general_details_id = ?";
		
		jdbcTemplate.update(sql, d.getArtist(), d.getHost(), d.getGenre(), 
				java.sql.Timestamp.valueOf(d.getStartTime()),
				java.sql.Timestamp.valueOf(d.getEndTime()),
				d.getAdditionalInfo(),
				id);
		d.setGeneralDetailsId(id);
		return d;
	}
	
	
	

}
