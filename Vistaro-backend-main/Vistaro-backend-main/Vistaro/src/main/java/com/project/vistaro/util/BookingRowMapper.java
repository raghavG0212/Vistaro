package com.project.vistaro.util;

import java.sql.*;
import com.project.vistaro.model.Booking;
import org.springframework.jdbc.core.RowMapper;

public class BookingRowMapper implements RowMapper<Booking> {

    @Override
    public Booking mapRow(ResultSet rs, int rowNum) throws SQLException {
        Booking b = new Booking();

        b.setBookingId(rs.getInt("booking_id"));
        b.setUserId(rs.getInt("user_id"));
        b.setSlotId(rs.getInt("slot_id"));
        b.setSeats(rs.getString("seats"));

        b.setTicketTotal(rs.getBigDecimal("ticket_total"));
        b.setFoodTotal(rs.getBigDecimal("food_total"));
        b.setFinalAmount(rs.getBigDecimal("final_amount"));
        b.setOfferApplied(rs.getString("offer_applied"));

        Timestamp created = rs.getTimestamp("created_at");
        if (created != null) b.setCreatedAt(created.toLocalDateTime());

        return b;
    }
}
