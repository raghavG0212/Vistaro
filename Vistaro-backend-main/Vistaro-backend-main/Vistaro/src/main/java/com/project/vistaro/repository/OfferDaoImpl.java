package com.project.vistaro.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.project.vistaro.model.Offer;

@Repository
public class OfferDaoImpl implements OfferDao {

    private final JdbcTemplate jdbcTemplate;

    public OfferDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static class OfferRowMapper implements RowMapper<Offer> {
        @Override
        public Offer mapRow(ResultSet rs, int rowNum) throws SQLException {
            Offer o = new Offer();
            o.setOfferId(rs.getInt("offer_id"));
            o.setCode(rs.getString("code"));
            o.setDescription(rs.getString("description"));
            o.setDiscountPercent(rs.getInt("discount_percent"));
            o.setMaxDiscount(rs.getBigDecimal("max_discount"));
            o.setValidFrom(rs.getDate("valid_from").toLocalDate());
            o.setValidTill(rs.getDate("valid_till").toLocalDate());
            o.setIsActive(rs.getBoolean("is_active"));
            return o;
        }
    }

    @Override
    public Optional<Offer> findByCode(String code) {
        String sql = "SELECT * FROM offer WHERE code = ?";
        List<Offer> list = jdbcTemplate.query(sql, new OfferRowMapper(), code);
        if (list.isEmpty()) return Optional.empty();
        return Optional.of(list.get(0));
    }
}
