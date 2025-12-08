package com.project.vistaro.repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.project.vistaro.model.Venue;
import com.project.vistaro.util.VenueRowMapper;

@Repository
public class VenueDaoImpl implements VenueDao {

    private final JdbcTemplate jdbcTemplate;

    public VenueDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Venue save(Venue venue) {
        String sql = """
            INSERT INTO venue(name, address, city, venue_type, screen_name, seat_layout_json)
            VALUES (?, ?, ?, ?, ?, ?)
        """;

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps =
                connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, venue.getName());
            ps.setString(2, venue.getAddress());
            ps.setString(3, venue.getCity());
            ps.setString(4, venue.getVenueType().name());
            ps.setString(5, venue.getScreenName());
            ps.setString(6, venue.getSeatLayoutJson());
            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();
        if (key != null) venue.setVenueId(key.intValue());

        return venue;
    }

    @Override
    public Optional<Venue> findById(int venueId) {
        String sql = "SELECT * FROM venue WHERE venue_id = ?";
        List<Venue> list = jdbcTemplate.query(sql, new VenueRowMapper(), venueId);
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }

    @Override
    public List<Venue> findAll() {
        String sql = "SELECT * FROM venue";
        return jdbcTemplate.query(sql, new VenueRowMapper());
    }

    @Override
    public Venue update(Venue venue) {
        String sql = """
            UPDATE venue
            SET name = ?, address = ?, city = ?, venue_type = ?
            WHERE venue_id = ?
        """;

        jdbcTemplate.update(sql,
                venue.getName(),
                venue.getAddress(),
                venue.getCity(),
                venue.getVenueType().name(),
                venue.getVenueId()
        );

        return venue;
    }

    @Override
    public List<Venue> searchByName(String name) {
        String sql = "SELECT * FROM venue WHERE LOWER(name) LIKE LOWER(?)";
        return jdbcTemplate.query(sql, new VenueRowMapper(), "%" + name + "%");
    }

    @Override
    public List<Venue> searchByCity(String city) {
        String sql = "SELECT * FROM venue WHERE LOWER(city) LIKE LOWER(?)";
        return jdbcTemplate.query(sql, new VenueRowMapper(), "%" + city + "%");
    }

    @Override
    public List<Venue> searchByType(String type) {
        String sql = "SELECT * FROM venue WHERE LOWER(venue_type) LIKE LOWER(?)";
        return jdbcTemplate.query(sql, new VenueRowMapper(), "%" + type + "%");
    }

    @Override
    public List<Venue> searchByScreen(String screen) {
        String sql = "SELECT * FROM venue WHERE LOWER(screen_name) LIKE LOWER(?)";
        return jdbcTemplate.query(sql, new VenueRowMapper(), "%" + screen + "%");
    }
}
