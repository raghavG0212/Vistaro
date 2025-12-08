package com.project.vistaro.dto;

import java.math.BigDecimal;

/**
 * Simple DTO (no Lombok) for UserEventDraft.
 * Date/time fields are ISO-8601 strings: "2025-03-15T18:00:00".
 */
public class UserEventDraftDto {

    public Integer draftId;
    public Integer userId;

    public String title;
    public String description;
    public String subCategory;
    public String bannerUrl;
    public String thumbnailUrl;

    public String artist;
    public String host;
    public String genre;

    public String eventStart;
    public String eventEnd;

    public Integer venueId;

    public String slotStart;
    public String slotEnd;

    public BigDecimal basePrice;

    public Integer eventId;
    public Integer slotId;

    public String approvalStatus;
    public String adminComment;

    public String createdAt;
}
