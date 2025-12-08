package com.project.vistaro.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.project.vistaro.model.EventSlot;
import com.project.vistaro.util.EventSlotRowMapper;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public class EventSlotDaoImpl implements EventSlotDao {

    private final JdbcTemplate jdbcTemplate;

    public EventSlotDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public EventSlot add(EventSlot slot) {
        String sql = """
            INSERT INTO EventSlot (event_id, venue_id, start_time, end_time, format, language, base_price)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """;

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, slot.getEventId());
            ps.setInt(2, slot.getVenueId());
            ps.setTimestamp(3, Timestamp.valueOf(slot.getStartTime()));
            ps.setTimestamp(4, Timestamp.valueOf(slot.getEndTime()));
            ps.setString(5, slot.getFormat().name());
            ps.setString(6, slot.getLanguage());
            ps.setBigDecimal(7, slot.getBasePrice());
            return ps;
        }, keyHolder);

        if (keyHolder.getKey() != null) {
            slot.setSlotId(keyHolder.getKey().intValue());
        }

        return slot;
    }

    @Override
    public int update(EventSlot slot) {
        String sql = """
            UPDATE EventSlot
            SET event_id = ?, venue_id = ?, start_time = ?, end_time = ?,
                format = ?, language = ?, base_price = ?
            WHERE slot_id = ?
        """;

        return jdbcTemplate.update(sql,
                slot.getEventId(),
                slot.getVenueId(),
                Timestamp.valueOf(slot.getStartTime()),
                Timestamp.valueOf(slot.getEndTime()),
                slot.getFormat().name(),
                slot.getLanguage(),
                slot.getBasePrice(),
                slot.getSlotId()
        );
    }

    @Override
    public int deleteById(Integer slotId) {
        String sql = "DELETE FROM EventSlot WHERE slot_id = ?";
        return jdbcTemplate.update(sql, slotId);
    }

    @Override
    public Optional<EventSlot> findById(Integer slotId) {
        String sql = "SELECT * FROM EventSlot WHERE slot_id = ?";
        List<EventSlot> list = jdbcTemplate.query(sql, new EventSlotRowMapper(), slotId);
        return list.stream().findFirst();
    }

    @Override
    public List<EventSlot> findByEventId(Integer eventId) {
        String sql = "SELECT * FROM EventSlot WHERE event_id = ?";
        return jdbcTemplate.query(sql, new EventSlotRowMapper(), eventId);
    }

    @Override
    public List<EventSlot> findByCity(String city) {
        String sql = """
            SELECT es.*
            FROM EventSlot es
            JOIN Venue v ON es.venue_id = v.venue_id
            WHERE v.city = ?
        """;
        // NOTE: still using EventSlotRowMapper, but no DTO / special rowmapper for join
        return jdbcTemplate.query(sql, new EventSlotRowMapper(), city);
    }

    @Override
    public List<EventSlot> findAll() {
        String sql = "SELECT * FROM EventSlot";
        return jdbcTemplate.query(sql, new EventSlotRowMapper());
    }
    
    
    @Override
    public int countOverlappingSlots(Integer venueId, Integer slotId, Timestamp start, Timestamp end) {

        String sql = """
            SELECT COUNT(*)
            FROM EventSlot
            WHERE venue_id = ?
            AND start_time < ?    -- existing.start < new.end
            AND end_time > ?      -- existing.end > new.start
        """;
        // INSERT CASE → no slotId yet
        if (slotId == null) {
            return jdbcTemplate.queryForObject(
                    sql,
                    Integer.class,
                    venueId,
                    end,
                    start
            );
        }
        // UPDATE CASE → exclude this slot
        sql += " AND slot_id <> ?";

        return jdbcTemplate.queryForObject(
                sql,
                Integer.class,
                venueId,
                end,
                start,
                slotId
        );
    }



}

