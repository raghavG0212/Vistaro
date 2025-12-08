package com.project.vistaro.repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.project.vistaro.model.Rating;

@Repository
public class RatingDaoImpl implements RatingDao {

    private final JdbcTemplate jdbc;

    public RatingDaoImpl(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public Optional<Rating> findByUserAndEvent(Integer userId, Integer eventId) {
        try {
            String sql = "SELECT * FROM Rating WHERE user_id=? AND event_id=?";
            return jdbc.query(sql, rs -> {
                if (rs.next()) {
                    Rating r = new Rating();
                    r.setRatingId(rs.getInt("rating_id"));
                    r.setUserId(rs.getInt("user_id"));
                    r.setEventId(rs.getInt("event_id"));
                    r.setRating(rs.getInt("rating"));
                    r.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                    return Optional.of(r);
                }
                return Optional.empty();
            }, userId, eventId);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Rating save(Rating r) {
        String sql = """
            INSERT INTO Rating (user_id, event_id, rating, created_at)
            VALUES (?, ?, ?, ?)
        """;

        jdbc.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, r.getUserId());
            ps.setInt(2, r.getEventId());
            ps.setInt(3, r.getRating());
            ps.setTimestamp(4, Timestamp.valueOf(r.getCreatedAt()));
            return ps;
        });

        return r;
    }
}
