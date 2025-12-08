package com.project.vistaro.repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.project.vistaro.model.Seat;
import com.project.vistaro.util.SeatRowMapper;

@Repository
public class SeatDaoImpl implements SeatDao {

	private final JdbcTemplate jdbcTemplate;

	public SeatDaoImpl(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public void save(Seat seat) {
		String sql = """
				    INSERT INTO Seat(slot_id, row_label, seat_number, seat_type, price, is_booked, is_locked, locked_until)
				    VALUES(?, ?, ?, ?, ?, ?, ?, ?)
				""";

		jdbcTemplate.update(sql, seat.getSlotId(), seat.getRowLabel(), seat.getSeatNumber(), seat.getSeatType(),
				seat.getPrice(), seat.getIsBooked(), seat.getIsLocked(),
				seat.getLockedUntil() == null ? null : Timestamp.valueOf(seat.getLockedUntil()));
	}

	@Override
	public List<Seat> findBySlot(int slotId) {
		String sql = "SELECT * FROM Seat WHERE slot_id = ?";
		return jdbcTemplate.query(sql, new SeatRowMapper(), slotId);
	}

	@Override
	public Optional<Seat> findById(int seatId) {
		String sql = "SELECT * FROM Seat WHERE seat_id = ?";
		List<Seat> list = jdbcTemplate.query(sql, new SeatRowMapper(), seatId);
		return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
	}

	@Override
	public List<Seat> findByIds(List<Integer> seatIds) {
		if (seatIds == null || seatIds.isEmpty())
			return List.of();

		String placeholders = seatIds.stream().map(id -> "?").collect(Collectors.joining(", "));

		String sql = "SELECT * FROM Seat WHERE seat_id IN (" + placeholders + ")";

		return jdbcTemplate.query(sql, new SeatRowMapper(), seatIds.toArray());
	}

	@Override
	public List<Map<String, Object>> findSeatsByBooking(int bookingId) {
		// Assumes booking.seats is a JSON array of seat_ids: [1,2,3]
		String sql = """
				    SELECT s.seat_id, s.row_label, s.seat_number, s.seat_type, s.price
				    FROM Seat s
				    JOIN Booking b ON JSON_CONTAINS(b.seats, CAST(s.seat_id AS JSON), '$')
				    WHERE b.booking_id = ?
				""";

		return jdbcTemplate.queryForList(sql, bookingId);
	}

	@Override
	public void lockSeats(List<Integer> seatIds) {
		String sql = """
				    UPDATE Seat
				    SET is_locked = true, locked_until = ?
				    WHERE seat_id = ?
				      AND is_booked = false
				""";

		LocalDateTime lockUntil = LocalDateTime.now().plusMinutes(5);

		for (Integer seatId : seatIds) {
			jdbcTemplate.update(sql, Timestamp.valueOf(lockUntil), seatId);
		}
	}

	@Override
	public void unlockSeats(List<Integer> seatIds) {
		String sql = """
				    UPDATE Seat
				    SET is_locked = false, locked_until = NULL
				    WHERE seat_id = ?
				""";

		for (Integer seatId : seatIds) {
			jdbcTemplate.update(sql, seatId);
		}
	}

	@Override
	public void bookSeats(List<Integer> seatIds) {
		String sql = """
				    UPDATE Seat
				    SET is_booked = true, is_locked = false, locked_until = NULL
				    WHERE seat_id = ?
				""";

		for (Integer seatId : seatIds) {
			jdbcTemplate.update(sql, seatId);
		}
	}

	@Override
	public void unlockExpiredSeats() {
		String sql = """
				    UPDATE Seat
				    SET is_locked = false, locked_until = NULL
				    WHERE locked_until IS NOT NULL
				      AND locked_until < NOW()
				""";

		jdbcTemplate.update(sql);
	}

	@Override
	public void releaseSeats(List<Integer> seatIds) {
		// TODO Auto-generated method stub
		String sql = """
				    UPDATE Seat
				    SET is_booked = false , is_locked = false, locked_until = NULL
				    where seat_id=?
				""";

		for (Integer seatId : seatIds) {
			jdbcTemplate.update(sql, seatId);
		}
	}
}
