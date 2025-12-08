package com.project.vistaro.dto;

import java.util.Map;

import com.project.vistaro.model.VenueType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class VenueDto {
	
	public Integer venueId;
	
	@NotBlank(message = "Venue name is required")
	public String name;
	
	@NotBlank(message = "Address is required")
	public String address;
	
	@NotBlank(message = "City is required")
	public String city;
	
	@NotNull(message = "Venue type is required")
	public VenueType venueType;
	
	public String screenName;
	
	private Map<String, Object> seatLayoutJson;
	
	public VenueDto() {}

	public VenueDto(Integer venueId, @NotBlank(message = "Venue name is required") String name,
			@NotBlank(message = "Address is required") String address,
			@NotBlank(message = "City is required") String city,
			@NotNull(message = "Venue type is required") VenueType venueType, String screenName,Map<String, Object> seatLayoutJson) {
		super();
		this.venueId = venueId;
		this.name = name;
		this.address = address;
		this.city = city;
		this.venueType = venueType;
		this.screenName = screenName;
		this.seatLayoutJson = seatLayoutJson;
	}

	public VenueDto(Integer venueId, @NotBlank(message = "Venue name is required") String name,
			@NotBlank(message = "Address is required") String address,
			@NotBlank(message = "City is required") String city,
			@NotNull(message = "Venue type is required") VenueType venueType) {
		super();
		this.venueId = venueId;
		this.name = name;
		this.address = address;
		this.city = city;
		this.venueType = venueType;
	}

	public Map<String, Object> getSeatLayoutJson() {
		return seatLayoutJson;
	}

	public void setSeatLayoutJson(Map<String, Object> seatLayoutJson) {
		this.seatLayoutJson = seatLayoutJson;
	}

	public Integer getVenueId() {
		return venueId;
	}

	public void setVenueId(Integer venueId) {
		this.venueId = venueId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public VenueType getVenueType() {
		return venueType;
	}

	public void setVenueType(VenueType venueType) {
		this.venueType = venueType;
	}

	public String getScreenName() {
		return screenName;
	}

	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}
	
	
	
	
}
