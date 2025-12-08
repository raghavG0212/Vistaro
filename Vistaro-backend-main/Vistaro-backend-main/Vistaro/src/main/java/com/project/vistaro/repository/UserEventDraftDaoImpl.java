package com.project.vistaro.repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.project.vistaro.model.UserEventDraft;

@Repository
public class UserEventDraftDaoImpl implements UserEventDraftDao {

    private final JdbcTemplate jdbcTemplate;
    private final UserEventDraftRowMapper rowMapper = new UserEventDraftRowMapper();

    public UserEventDraftDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Integer save(UserEventDraft d) {
        String sql = "INSERT INTO UserEventDraft (" +
                "user_id, title, description, sub_category, banner_url, thumbnail_url, " +
                "artist, host, genre, event_start, event_end, venue_id, " +
                "slot_start, slot_end, base_price, event_id, slot_id, " +
                "approval_status, admin_comment" +
                ") VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        KeyHolder kh = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            int idx = 1;
            ps.setInt(idx++, d.getUserId());
            ps.setString(idx++, d.getTitle());
            ps.setString(idx++, d.getDescription());
            ps.setString(idx++, d.getSubCategory());
            ps.setString(idx++, d.getBannerUrl());
            ps.setString(idx++, d.getThumbnailUrl());

            ps.setString(idx++, d.getArtist());
            ps.setString(idx++, d.getHost());
            ps.setString(idx++, d.getGenre());

            ps.setTimestamp(idx++, timestampOrNull(d.getEventStart()));
            ps.setTimestamp(idx++, timestampOrNull(d.getEventEnd()));

            ps.setInt(idx++, d.getVenueId());

            ps.setTimestamp(idx++, timestampOrNull(d.getSlotStart()));
            ps.setTimestamp(idx++, timestampOrNull(d.getSlotEnd()));

            if (d.getBasePrice() != null) {
                ps.setBigDecimal(idx++, d.getBasePrice());
            } else {
                ps.setNull(idx++, Types.DECIMAL);
            }

            if (d.getEventId() != null) {
                ps.setInt(idx++, d.getEventId());
            } else {
                ps.setNull(idx++, Types.INTEGER);
            }

            if (d.getSlotId() != null) {
                ps.setInt(idx++, d.getSlotId());
            } else {
                ps.setNull(idx++, Types.INTEGER);
            }

            ps.setString(idx++, d.getApprovalStatus() != null ? d.getApprovalStatus() : "PENDING");
            ps.setString(idx++, d.getAdminComment());

            return ps;
        }, kh);

        Number key = kh.getKey();
        return key != null ? key.intValue() : null;
    }

    @Override
    public Optional<UserEventDraft> findById(Integer draftId) {
        String sql = "SELECT * FROM UserEventDraft WHERE draft_id = ?";
        List<UserEventDraft> list = jdbcTemplate.query(sql, rowMapper, draftId);
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }

    @Override
    public List<UserEventDraft> findByUserId(Integer userId) {
        String sql = "SELECT * FROM UserEventDraft WHERE user_id = ? ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, rowMapper, userId);
    }

    @Override
    public List<UserEventDraft> findAllByStatus(String status) {
        String sql = "SELECT * FROM UserEventDraft WHERE approval_status = ? ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, rowMapper, status);
    }

    @Override
    public void update(UserEventDraft d) {
        String sql = "UPDATE UserEventDraft SET " +
                "user_id=?, title=?, description=?, sub_category=?, banner_url=?, thumbnail_url=?, " +
                "artist=?, host=?, genre=?, event_start=?, event_end=?, venue_id=?, " +
                "slot_start=?, slot_end=?, base_price=?, event_id=?, slot_id=?, " +
                "approval_status=?, admin_comment=? " +
                "WHERE draft_id=?";

        jdbcTemplate.update(sql,
                d.getUserId(),
                d.getTitle(),
                d.getDescription(),
                d.getSubCategory(),
                d.getBannerUrl(),
                d.getThumbnailUrl(),
                d.getArtist(),
                d.getHost(),
                d.getGenre(),
                timestampOrNull(d.getEventStart()),
                timestampOrNull(d.getEventEnd()),
                d.getVenueId(),
                timestampOrNull(d.getSlotStart()),
                timestampOrNull(d.getSlotEnd()),
                d.getBasePrice(),
                d.getEventId(),
                d.getSlotId(),
                d.getApprovalStatus(),
                d.getAdminComment(),
                d.getDraftId()
        );
    }

    @Override
    public void delete(Integer draftId) {
        String sql = "DELETE FROM UserEventDraft WHERE draft_id = ?";
        jdbcTemplate.update(sql, draftId);
    }

    private Timestamp timestampOrNull(LocalDateTime dt) {
        return dt == null ? null : Timestamp.valueOf(dt);
    }
}
