package com.project.vistaro.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class SportDetailsDto {
	private Integer sportDetailsId;   // optional, usually for responses
    private Integer eventId;          // link to Event entity

    @NotBlank(message = "Sport type is required")
    @Size(max = 100, message = "Sport type must be less than 100 characters")
    private String sportType;

    @NotBlank(message = "Team 1 name is required")
    @Size(max = 150, message = "Team 1 name must be less than 150 characters")
    private String team1;

    @NotBlank(message = "Team 2 name is required")
    @Size(max = 150, message = "Team 2 name must be less than 150 characters")
    private String team2;

    @Size(max = 50, message = "Match format must be less than 50 characters")
    private String matchFormat;
    
    @Size(max = 255, message = "Venue info must be less than 255 characters")
    private String venueInfo;
    
    @NotNull(message = "Start time is required")
    private LocalDateTime startTime;

    @NotNull(message = "End time is required")
    private LocalDateTime endTime;

    public SportDetailsDto() {}

	
	public SportDetailsDto(Integer sportDetailsId, Integer eventId,
			@NotBlank(message = "Sport type is required") @Size(max = 100, message = "Sport type must be less than 100 characters") String sportType,
			@NotBlank(message = "Team 1 name is required") @Size(max = 150, message = "Team 1 name must be less than 150 characters") String team1,
			@NotBlank(message = "Team 2 name is required") @Size(max = 150, message = "Team 2 name must be less than 150 characters") String team2,
			@Size(max = 50, message = "Match format must be less than 50 characters") String matchFormat,
			@Size(max = 255, message = "Venue info must be less than 255 characters") String venueInfo,
			@NotNull(message = "Start time is required") LocalDateTime startTime,
			@NotNull(message = "End time is required") LocalDateTime endTime) {
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
