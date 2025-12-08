package com.project.vistaro.model;

import java.time.LocalDateTime;

public class Event {
	private Integer eventId;
	private String title;
	private String description;
	private EventCategory category;
	private String subCategory;
	private String bannerUrl ;
	private String thumbnailUrl;
	private LocalDateTime startTime;
	private LocalDateTime endTime;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	
	public Event() {}
	
	public Event(Integer eventId, String title, String description, EventCategory category, String subCategory,
			String bannerUrl, String thumbnailUrl, LocalDateTime startTime, LocalDateTime endTime,
			LocalDateTime createdAt, LocalDateTime updatedAt) {
		super();
		this.eventId = eventId;
		this.title = title;
		this.description = description;
		this.category = category;
		this.subCategory = subCategory;
		this.bannerUrl = bannerUrl;
		this.thumbnailUrl = thumbnailUrl;
		this.startTime = startTime;
		this.endTime = endTime;
	}

	public Integer getEventId() {
		return eventId;
	}

	public void setEventId(Integer eventId) {
		this.eventId = eventId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public EventCategory getCategory() {
		return category;
	}

	public void setCategory(EventCategory category) {
		this.category = category;
	}

	public String getSubCategory() {
		return subCategory;
	}

	public void setSubCategory(String subCategory) {
		this.subCategory = subCategory;
	}

	public String getBannerUrl() {
		return bannerUrl;
	}

	public void setBannerUrl(String bannerUrl) {
		this.bannerUrl = bannerUrl;
	}

	public String getThumbnailUrl() {
		return thumbnailUrl;
	}

	public void setThumbnailUrl(String thumbnailUrl) {
		this.thumbnailUrl = thumbnailUrl;
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

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}
	
	
}
