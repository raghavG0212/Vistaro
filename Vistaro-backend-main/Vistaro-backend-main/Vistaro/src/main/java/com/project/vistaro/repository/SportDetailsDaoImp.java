
package com.project.vistaro.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.project.vistaro.model.SportDetails;

@Repository
public class SportDetailsDaoImp implements SportDetailsDao{
	
private final JdbcTemplate jdbcTemplate;
	
	public SportDetailsDaoImp(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	private final RowMapper<SportDetails> sportRowMapper = new RowMapper<>() {
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
    };

    @Override
    public SportDetails addSportDetails(SportDetails sportDetails) {

        String sql = "INSERT INTO SportDetails (event_id, sport_type, team1, team2, match_format, venue_info, start_time, end_time) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, sportDetails.getEventId());
            ps.setString(2, sportDetails.getSportType());
            ps.setString(3, sportDetails.getTeam1());
            ps.setString(4, sportDetails.getTeam2());
            ps.setString(5, sportDetails.getMatchFormat());
            ps.setString(6, sportDetails.getVenueInfo());
            ps.setObject(7, sportDetails.getStartTime());
            ps.setObject(8, sportDetails.getEndTime());
            return ps;
        }, keyHolder);

        Number generatedId = keyHolder.getKey();
        if (generatedId != null) {
            sportDetails.setSportDetailsId(generatedId.intValue());
        }

        return sportDetails;
    }


	@Override
	public SportDetails updateSportDetails(SportDetails sportDetails) {
		// TODO Auto-generated method stub
		String sql = "UPDATE SportDetails SET sport_type=?, team1=?, team2=?, match_format=?, venue_info=?, start_time=?, end_time=? " +
                "WHERE sport_details_id=?";
   jdbcTemplate.update(sql,
           sportDetails.getSportType(),
           sportDetails.getTeam1(),
           sportDetails.getTeam2(),
           sportDetails.getMatchFormat(),
           sportDetails.getVenueInfo(),
           sportDetails.getStartTime(),
           sportDetails.getEndTime(),
           sportDetails.getSportDetailsId()
   );
   return sportDetails;
	}

	@Override
	public void deleteSportDetails(Integer id) {
		// TODO Auto-generated method stub
		String sql = "DELETE FROM SportDetails WHERE sport_details_id = ?";
        jdbcTemplate.update(sql, id);
		
	}

	@Override
	public List<SportDetails> listAllSportDetails() {
		// TODO Auto-generated method stub
		 String sql = "SELECT * FROM SportDetails";
	        return jdbcTemplate.query(sql, sportRowMapper);
	
	}

	@Override
	public Optional<SportDetails> findById(Integer id) {
		// TODO Auto-generated method stub
		 String sql = "SELECT * FROM SportDetails WHERE sport_details_id = ?";
	        return jdbcTemplate.query(sql, sportRowMapper, id).stream().findFirst();
	}

	@Override
	public List<SportDetails> getBySportType(String sportType) {
		// TODO Auto-generated method stub
		 String sql = "SELECT * FROM SportDetails WHERE sport_type = ?";
	        return jdbcTemplate.query(sql, sportRowMapper, sportType);
	}

	@Override
	public SportDetails findByEventId(Integer eventId) {
		// TODO Auto-generated method stub
		String sql=  "Select * from sportdetails where event_id=?";
		return jdbcTemplate.queryForObject(sql, sportRowMapper,eventId);
	}

}
