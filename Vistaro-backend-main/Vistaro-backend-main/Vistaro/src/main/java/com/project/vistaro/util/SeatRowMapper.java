package com.project.vistaro.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.springframework.jdbc.core.RowMapper;

import com.project.vistaro.model.Seat;

public class SeatRowMapper implements RowMapper<Seat> {

    @Override
    public Seat mapRow(ResultSet rs, int rowNum) throws SQLException {
        Seat s = new Seat();
        s.setSeatId(rs.getInt("seat_id"));
        s.setSlotId(rs.getInt("slot_id"));
        s.setRowLabel(rs.getString("row_label"));
        s.setSeatNumber(rs.getString("seat_number"));
        s.setSeatType(rs.getString("seat_type"));
        s.setPrice(rs.getBigDecimal("price"));
        s.setIsBooked(rs.getBoolean("is_booked"));
        s.setIsLocked(rs.getBoolean("is_locked"));

        Timestamp lockedTs = rs.getTimestamp("locked_until");
        s.setLockedUntil(lockedTs != null ? lockedTs.toLocalDateTime() : null);

        return s;
    }
}
