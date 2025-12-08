package com.project.vistaro.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class EventSlot {
	private Integer slotId;
	private Integer eventId;
	private Integer venueId;
	private LocalDateTime startTime;
	private LocalDateTime endTime;
	private EventFormat format;
	private String language;
	private BigDecimal basePrice;
	private LocalDateTime createdAt;
	
	public EventSlot() {}
	
	public EventSlot(Integer slotId, Integer eventId, Integer venueId, LocalDateTime startTime, LocalDateTime endTime,
			EventFormat format, String language, BigDecimal basePrice,
			LocalDateTime createdAt) {
		super();
		this.slotId = slotId;
		this.eventId = eventId;
		this.venueId = venueId;
		this.startTime = startTime;
		this.endTime = endTime;
		this.format = format;
		this.language = language;
		this.basePrice = basePrice;
		this.createdAt = createdAt;
	}

	public EventSlot(Integer slotId, Integer eventId, Integer venueId, LocalDateTime startTime, LocalDateTime endTime,
			EventFormat format, String language, BigDecimal basePrice
			) {
		super();
		this.slotId = slotId;
		this.eventId = eventId;
		this.venueId = venueId;
		this.startTime = startTime;
		this.endTime = endTime;
		this.format = format;
		this.language = language;
		this.basePrice = basePrice;
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

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	
	
	
	
}
