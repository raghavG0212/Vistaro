package com.project.vistaro.model;

public class City {
	private Integer cityId;
	private String name;
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public Integer getCityId() {
		return cityId;
	}
	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}
	
	
	public City(Integer cityId, String name) {
		super();
		this.cityId = cityId;
		this.name = name;
	}
	public City() {
		super();
	}
	
	

}
