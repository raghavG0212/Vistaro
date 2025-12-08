package com.project.vistaro.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.project.vistaro.model.GiftCard;

@Repository
public class GiftCardDaoImpl implements GiftCardDao {

    private final JdbcTemplate jdbcTemplate;

    public GiftCardDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static class GiftCardRowMapper implements RowMapper<GiftCard> {
        @Override
        public GiftCard mapRow(ResultSet rs, int rowNum) throws SQLException {
            GiftCard g = new GiftCard();
            g.setGiftcardId(rs.getInt("giftcard_id"));
            g.setCode(rs.getString("code"));
            g.setAmount(rs.getBigDecimal("amount"));
            g.setIsRedeemed(rs.getBoolean("is_redeemed"));
            g.setExpiryDate(rs.getDate("expiry_date").toLocalDate());
            return g;
        }
    }

    @Override
    public Optional<GiftCard> findByCode(String code) {
        String sql = "SELECT * FROM giftcard WHERE code = ?";
        List<GiftCard> list = jdbcTemplate.query(sql, new GiftCardRowMapper(), code);
        if (list.isEmpty()) return Optional.empty();
        return Optional.of(list.get(0));
    }

    @Override
    public int markRedeemed(Integer giftcardId) {
        String sql = "UPDATE giftcard SET is_redeemed = TRUE WHERE giftcard_id = ?";
        return jdbcTemplate.update(sql, giftcardId);
    }
}
