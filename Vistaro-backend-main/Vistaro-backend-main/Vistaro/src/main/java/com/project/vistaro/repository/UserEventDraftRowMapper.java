package com.project.vistaro.repository;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.springframework.jdbc.core.RowMapper;

import com.project.vistaro.model.UserEventDraft;

public class UserEventDraftRowMapper implements RowMapper<UserEventDraft> {

    @Override
    public UserEventDraft mapRow(ResultSet rs, int rowNum) throws SQLException {
        UserEventDraft d = new UserEventDraft();

        d.setDraftId(rs.getInt("draft_id"));
        d.setUserId(rs.getInt("user_id"));

        d.setTitle(rs.getString("title"));
        d.setDescription(rs.getString("description"));
        d.setSubCategory(rs.getString("sub_category"));
        d.setBannerUrl(rs.getString("banner_url"));
        d.setThumbnailUrl(rs.getString("thumbnail_url"));

        d.setArtist(rs.getString("artist"));
        d.setHost(rs.getString("host"));
        d.setGenre(rs.getString("genre"));

        Timestamp es = rs.getTimestamp("event_start");
        if (es != null) d.setEventStart(es.toLocalDateTime());

        Timestamp ee = rs.getTimestamp("event_end");
        if (ee != null) d.setEventEnd(ee.toLocalDateTime());

        d.setVenueId(rs.getInt("venue_id"));

        Timestamp ss = rs.getTimestamp("slot_start");
        if (ss != null) d.setSlotStart(ss.toLocalDateTime());

        Timestamp se = rs.getTimestamp("slot_end");
        if (se != null) d.setSlotEnd(se.toLocalDateTime());

        BigDecimal basePrice = rs.getBigDecimal("base_price");
        d.setBasePrice(basePrice);

        int evId = rs.getInt("event_id");
        d.setEventId(rs.wasNull() ? null : evId);

        int slId = rs.getInt("slot_id");
        d.setSlotId(rs.wasNull() ? null : slId);

        d.setApprovalStatus(rs.getString("approval_status"));
        d.setAdminComment(rs.getString("admin_comment"));

        Timestamp created = rs.getTimestamp("created_at");
        if (created != null) d.setCreatedAt(created.toLocalDateTime());

        return d;
    }
}
