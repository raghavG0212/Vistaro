package com.project.vistaro.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class GeneralEventDetailsDto {
	
	private Integer generalDetailsId;
	
	@NotNull(message = "Event ID is required")
	private Integer eventId;
	
	@NotBlank(message = "Artist name is required")
	private String artist;
	
	private String host;
	
	@NotBlank(message = "Genre is required")
	private String genre;
	
	@NotNull (message = "Start time is required")
	private LocalDateTime startTime;

	@NotNull (message = "End time is required")
	private LocalDateTime endTime;
	
	private String additionalInfo;

	public Integer getGeneralDetailsId() {
		return generalDetailsId;
	}

	public void setGeneralDetailsId(Integer generalDetailsId) {
		this.generalDetailsId = generalDetailsId;
	}

	public Integer getEventId() {
		return eventId;
	}

	public void setEventId(Integer eventId) {
		this.eventId = eventId;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
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

	public String getAdditionalInfo() {
		return additionalInfo;
	}

	public void setAdditionalInfo(String additionalInfo) {
		this.additionalInfo = additionalInfo;
	}
	
	
	

}
