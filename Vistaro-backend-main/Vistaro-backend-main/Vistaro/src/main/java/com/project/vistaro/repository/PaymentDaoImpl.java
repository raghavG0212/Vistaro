package com.project.vistaro.repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.project.vistaro.model.Payment;
import com.project.vistaro.util.PaymentRowMapper;

@Repository
public class PaymentDaoImpl implements PaymentDao {

    private final JdbcTemplate jdbcTemplate;

    public PaymentDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Payment save(Payment payment) {
        String sql = "INSERT INTO payment(booking_id,payment_mode,transaction_id,payment_status,paid_at) values"
                + "(?,?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, payment.getBookingId());
            ps.setString(2, payment.getPaymentMode().name());
            ps.setString(3, payment.getTransactionId());
            ps.setString(4, payment.getPaymentStatus().name());
            ps.setTimestamp(5, Timestamp.valueOf(payment.getPaidAt()));
            return ps;
        }, keyHolder);
        Number key = keyHolder.getKey();
        if (key != null) {
            payment.setPaymentId(key.intValue());
        }
        return payment;
    }

    @Override
    public Optional<Payment> findById(int paymentId) {
        String sql = "select * from payment where payment_id=?";
        List<Payment> list = jdbcTemplate.query(sql, new PaymentRowMapper(), paymentId);
        if (list.isEmpty())
            return Optional.empty();
        return Optional.of(list.get(0));
    }

    @Override
    public Optional<Payment> findByBookingId(int bookingId) {
        String sql = "SELECT * FROM payment WHERE booking_id=?";
        List<Payment> list = jdbcTemplate.query(sql, new PaymentRowMapper(), bookingId);
        if (list.isEmpty())
            return Optional.empty();
        return Optional.of(list.get(0));
    }

    @Override
    public int update(Payment payment) {
        String sql = """
            UPDATE payment
            SET booking_id = ?, payment_mode = ?, transaction_id = ?, payment_status = ?, paid_at = ?
            WHERE payment_id = ?
        """;

        return jdbcTemplate.update(sql,
                payment.getBookingId(),
                payment.getPaymentMode().name(),
                payment.getTransactionId(),
                payment.getPaymentStatus().name(),
                Timestamp.valueOf(payment.getPaidAt()),
                payment.getPaymentId());
    }
}
