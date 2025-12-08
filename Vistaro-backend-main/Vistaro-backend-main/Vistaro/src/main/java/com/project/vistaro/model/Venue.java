package com.project.vistaro.model;

public class Venue {
	private Integer venueId;
	private String name;
	private String address;
	private String city;
	private VenueType venueType;
	private String seatLayoutJson;
	private String screenName;
	
	public Venue () {}
	
	public Venue(Integer venueId, String name, String address, String city, VenueType venueType, String screenName,String seatLayoutJson) {
		super();
		this.venueId = venueId;
		this.name = name;
		this.address = address;
		this.city = city;
		this.venueType = venueType;
		this.screenName = screenName;
		this.seatLayoutJson = seatLayoutJson;
	}
	
	public Venue(Integer venueId, String name, String address, String city, VenueType venueType,String seatLayoutJson) {
		super();
		this.venueId = venueId;
		this.name = name;
		this.address = address;
		this.city = city;
		this.venueType = venueType;
		
	}

	public String getSeatLayoutJson() {
		return seatLayoutJson;
	}

	public void setSeatLayoutJson(String seatLayoutJson) {
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
