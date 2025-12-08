package com.project.vistaro.dto;

import java.time.LocalDateTime;

import com.project.vistaro.model.EventCategory;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class EventDto {
	
	private Integer eventId;
	
	@NotBlank(message = "Title is required")
	private String title;
	
	private String description;
	
	@NotNull(message = "Category is required")
	private EventCategory category;
	
	private String subCategory;
	
	private String bannerUrl ;
	
	private String thumbnailUrl;
	
	@NotNull(message = "Start time is required")
	private LocalDateTime startTime;
	
	@NotNull(message = "End time is required")
	private LocalDateTime endTime;
	
	public EventDto() {}

	public EventDto(Integer eventId, @NotBlank(message = "Title is required") String title, String description,
			@NotNull(message = "Category is required") EventCategory category, String subCategory, String bannerUrl,
			String thumbnailUrl, @NotNull(message = "Start time is required") LocalDateTime startTime,
			@NotNull(message = "End time is required") LocalDateTime endTime) {
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
	
	
	
}
