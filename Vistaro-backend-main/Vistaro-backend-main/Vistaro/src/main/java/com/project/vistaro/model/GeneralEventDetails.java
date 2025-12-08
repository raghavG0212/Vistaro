package com.project.vistaro.model;

import java.time.LocalDateTime;

public class GeneralEventDetails {
	
	private Integer generalDetailsId;
	private Integer eventId;
	private String artist;
	private String host;
	private String genre;
	private LocalDateTime startTime;
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
	public String getAdditionalInfo() {
		return additionalInfo;
	}
	public void setAdditionalInfo(String additionalInfo) {
		this.additionalInfo = additionalInfo;
	}
	public LocalDateTime getEndTime() {
		return endTime;
	}
	public void setEndTime(LocalDateTime endTime) {
		this.endTime = endTime;
	}
	
	
	public GeneralEventDetails(Integer generalDetailsId, Integer eventId, String artist, String host, String genre,
			LocalDateTime startTime, LocalDateTime endTime, String additionalInfo) {
		super();
		this.generalDetailsId = generalDetailsId;
		this.eventId = eventId;
		this.artist = artist;
		this.host = host;
		this.genre = genre;
		this.startTime = startTime;
		this.endTime = endTime;
		this.additionalInfo = additionalInfo;
	}
	public GeneralEventDetails() {
		super();
	}
	public GeneralEventDetails(Integer generalDetailsId, Integer eventId, String artist, String host, String genre,
			String additionalInfo) {
		super();
		this.generalDetailsId = generalDetailsId;
		this.eventId = eventId;
		this.artist = artist;
		this.host = host;
		this.genre = genre;
		this.additionalInfo = additionalInfo;
	}
	
	
	

}
