package com.project.vistaro.repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import com.project.vistaro.model.BookingFood;
import com.project.vistaro.util.BookingFoodRowMapper;

@Repository
public class BookingFoodDaoImpl implements BookingFoodDao{
	
	private final JdbcTemplate jdbcTemplate;
    private final BookingFoodRowMapper mapper = new BookingFoodRowMapper();

    public BookingFoodDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public BookingFood save(BookingFood bf) {
        String sql = "INSERT INTO booking_food(booking_id, food_id, quantity) VALUES (?, ?, ?)";

        GeneratedKeyHolder kh = new GeneratedKeyHolder();

        jdbcTemplate.update(conn -> {
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, bf.getBookingId());
            ps.setInt(2, bf.getFoodId());
            ps.setInt(3, bf.getQuantity());
            return ps;
        }, kh);

        if (kh.getKey() != null)
            bf.setBookingFoodId(kh.getKey().intValue());

        return bf;
    }

    @Override
    public BookingFood findById(Integer id) {
        String sql = "SELECT * FROM booking_food WHERE booking_food_id=?";
        return jdbcTemplate.queryForObject(sql, mapper, id);
    }

    @Override
    public List<BookingFood> findByBookingId(Integer bookingId) {
        String sql = "SELECT * FROM booking_food WHERE booking_id=?";
        return jdbcTemplate.query(sql, mapper, bookingId);
    }

    @Override
    public List<BookingFood> findAll() {
        return jdbcTemplate.query("SELECT * FROM booking_food", mapper);
    }

    @Override
    public BookingFood update(BookingFood bf) {
        String sql = "UPDATE booking_food SET booking_id=?, food_id=?, quantity=? WHERE booking_food_id=?";
        jdbcTemplate.update(sql,
                bf.getBookingId(),
                bf.getFoodId(),
                bf.getQuantity(),
                bf.getBookingFoodId());
        return bf;
    }

    @Override
    public void delete(Integer id) {
        jdbcTemplate.update("DELETE FROM booking_food WHERE booking_food_id=?", id);
    }
    
    @Override
    public List<Map<String, Object>> findByBookingIdDetailed(Integer bookingId) {
        String sql = """
            SELECT bf.booking_food_id,
                   bf.booking_id,
                   bf.food_id,
                   bf.quantity,
                   f.name AS food_name,
                   f.price AS food_price,
                   (f.price * bf.quantity) AS line_total
            FROM booking_food bf
            JOIN food f ON bf.food_id = f.food_id
            WHERE bf.booking_id = ?
        """;

        return jdbcTemplate.queryForList(sql, bookingId);
    }


}
