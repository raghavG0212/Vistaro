package com.project.vistaro.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.springframework.jdbc.core.RowMapper;

import com.project.vistaro.model.EventFormat;
import com.project.vistaro.model.EventSlot;

public class EventSlotRowMapper implements RowMapper<EventSlot> {

    @Override
    public EventSlot mapRow(ResultSet rs, int rowNum) throws SQLException {
        EventSlot slot = new EventSlot();
        slot.setSlotId(rs.getInt("slot_id"));
        slot.setEventId(rs.getInt("event_id"));
        slot.setVenueId(rs.getInt("venue_id"));

        Timestamp startTs = rs.getTimestamp("start_time");
        Timestamp endTs = rs.getTimestamp("end_time");
        Timestamp createdTs = rs.getTimestamp("created_at");

        LocalDateTime start = startTs != null ? startTs.toLocalDateTime() : null;
        LocalDateTime end = endTs != null ? endTs.toLocalDateTime() : null;
        LocalDateTime created = createdTs != null ? createdTs.toLocalDateTime() : null;

        slot.setStartTime(start);
        slot.setEndTime(end);
        slot.setCreatedAt(created);

        String formatStr = rs.getString("format");
        if (formatStr != null) {
            slot.setFormat(EventFormat.valueOf(formatStr));
        }

        slot.setLanguage(rs.getString("language"));
        slot.setBasePrice(rs.getBigDecimal("base_price"));

        return slot;
    }
}
