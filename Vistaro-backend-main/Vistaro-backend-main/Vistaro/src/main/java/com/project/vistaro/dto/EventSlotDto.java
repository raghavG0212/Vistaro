package com.project.vistaro.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.project.vistaro.model.EventFormat;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public class EventSlotDto {
	
	private Integer slotId;

    @NotNull(message = "Event ID is required")
    private Integer eventId;

    @NotNull(message = "Venue ID is required")
    private Integer venueId;

    @NotNull(message = "Start time is required")
    private LocalDateTime startTime;

    @NotNull(message = "End time is required")
    private LocalDateTime endTime;

    @NotNull(message = "Format is required")
    private EventFormat format; // Allowed: NA, _2D, _3D, _4DX

    private String language;

    @NotNull(message = "Base price is required")
    @DecimalMin(value = "0.0", message = "Base price must be greater or equal to 0")
    private BigDecimal basePrice;
    
    private LocalDateTime createdAt;
    
    public EventSlotDto() {}

	public EventSlotDto(Integer slotId, @NotNull(message = "Event ID is required") Integer eventId,
			@NotNull(message = "Venue ID is required") Integer venueId,
			@NotNull(message = "Start time is required") LocalDateTime startTime,
			@NotNull(message = "End time is required") LocalDateTime endTime,
			@NotNull(message = "Format is required") EventFormat format, String language,
			@NotNull(message = "Base price is required") @DecimalMin(value = "0.0", message = "Base price must be greater or equal to 0") BigDecimal basePrice
			,LocalDateTime createdAt) {
		super();
		this.slotId = slotId;
		this.eventId = eventId;
		this.venueId = venueId;
		this.startTime = startTime;
		this.endTime = endTime;
		this.format = format;
		this.language = language;
		this.basePrice = basePrice;
		this.createdAt=createdAt;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public Integer getSlotId() {
		return slotId;
	}

	public void setSlotId(Integer slotId) {
		this.slotId = slotId;
	}

	public Integer getEventId() {
		return eventId;
	}

	public void setEventId(Integer eventId) {
		this.eventId = eventId;
	}

	public Integer getVenueId() {
		return venueId;
	}

	public void setVenueId(Integer venueId) {
		this.venueId = venueId;
	}

	public LocalDateTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}

	public LocalDateTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalDateTime endTime) {
		this.endTime = endTime;
	}

	public EventFormat getFormat() {
		return format;
	}

	public void setFormat(EventFormat format) {
		this.format = format;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public BigDecimal getBasePrice() {
		return basePrice;
	}

	public void setBasePrice(BigDecimal basePrice) {
		this.basePrice = basePrice;
	}
    
}
