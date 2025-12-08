package com.project.vistaro.repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import com.project.vistaro.model.Booking;
import com.project.vistaro.util.BookingRowMapper;

@Repository
public class BookingDaoImpl implements BookingDao {

    private final JdbcTemplate jdbc;
    private final BookingRowMapper mapper = new BookingRowMapper();

    public BookingDaoImpl(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public Booking save(Booking b) {
        String sql = "INSERT INTO booking(user_id, slot_id, seats, ticket_total, food_total, final_amount, offer_applied, created_at) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        GeneratedKeyHolder kh = new GeneratedKeyHolder();

        jdbc.update(conn -> {
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, b.getUserId());
            ps.setInt(2, b.getSlotId());
            ps.setString(3, b.getSeats());
            ps.setBigDecimal(4, b.getTicketTotal());
            ps.setBigDecimal(5, b.getFoodTotal());
            ps.setBigDecimal(6, b.getFinalAmount());
            ps.setString(7, b.getOfferApplied());
            ps.setTimestamp(8, Timestamp.valueOf(b.getCreatedAt()));
            return ps;
        }, kh);

        if (kh.getKey() != null) b.setBookingId(kh.getKey().intValue());
        return b;
    }

    @Override
    public Optional<Booking> findById(int bookingId) {
        List<Booking> list = jdbc.query("SELECT * FROM booking WHERE booking_id=?",
                mapper, bookingId);
        return list.stream().findFirst();
    }

    @Override
    public List<Booking> findByUserId(int userId) {
        return jdbc.query("SELECT * FROM booking WHERE user_id=?", mapper, userId);
    }

    @Override
    public List<Booking> findBySlotId(int slotId) {
        return jdbc.query("SELECT * FROM booking WHERE slot_id=?", mapper, slotId);
    }

    @Override
    public int deleteById(int bookingId) {
        return jdbc.update("DELETE FROM booking WHERE booking_id=?", bookingId);
    }

    @Override
    public Map<String, Object> getBookingFullDetails(int bookingId) {
        String sql =
                "SELECT b.*, u.name AS user_name, e.title AS event_title, v.name AS venue_name, " +
                "es.start_time AS show_start, es.end_time AS show_end " +
                "FROM booking b " +
                "JOIN user u ON b.user_id = u.user_id " +
                "JOIN eventslot es ON b.slot_id = es.slot_id " +
                "JOIN event e ON es.event_id = e.event_id " +
                "JOIN venue v ON es.venue_id = v.venue_id " +
                "WHERE b.booking_id = ?";

        return jdbc.queryForMap(sql, bookingId);
    }
}
