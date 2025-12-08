package com.project.vistaro.model;

import java.time.LocalDateTime;

import io.micrometer.observation.Observation.Event;

public class SportDetails {
	private Integer sportDetailsId;
    private Integer eventId;
    private String sportType;
    private String team1;
    private String team2;
    private String matchFormat;
    private String venueInfo;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    
    public SportDetails() {}

	public SportDetails(Integer sportDetailsId, Integer eventId, String sportType, String team1, String team2,
			String matchFormat, String venueInfo, LocalDateTime startTime, LocalDateTime endTime) {
		super();
		this.sportDetailsId = sportDetailsId;
		this.eventId = eventId;
		this.sportType = sportType;
		this.team1 = team1;
		this.team2 = team2;
		this.matchFormat = matchFormat;
		this.venueInfo = venueInfo;
		this.startTime = startTime;
		this.endTime = endTime;
	}

	public Integer getSportDetailsId() {
		return sportDetailsId;
	}

	public void setSportDetailsId(Integer sportDetailsId) {
		this.sportDetailsId = sportDetailsId;
	}

	public Integer getEventId() {
		return eventId;
	}

	public void setEventId(Integer eventId) {
		this.eventId = eventId;
	}

	public String getSportType() {
		return sportType;
	}

	public void setSportType(String sportType) {
		this.sportType = sportType;
	}

	public String getTeam1() {
		return team1;
	}

	public void setTeam1(String team1) {
		this.team1 = team1;
	}

	public String getTeam2() {
		return team2;
	}

	public void setTeam2(String team2) {
		this.team2 = team2;
	}

	public String getMatchFormat() {
		return matchFormat;
	}

	public void setMatchFormat(String matchFormat) {
		this.matchFormat = matchFormat;
	}

	public String getVenueInfo() {
		return venueInfo;
	}

	public void setVenueInfo(String venueInfo) {
		this.venueInfo = venueInfo;
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
