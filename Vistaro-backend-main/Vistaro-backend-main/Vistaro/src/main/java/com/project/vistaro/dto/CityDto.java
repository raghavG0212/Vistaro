package com.project.vistaro.dto;

import jakarta.validation.constraints.NotBlank;

public class CityDto {
	private Integer cityId;
	
	@NotBlank(message = "City name is required")
	private String name;

	public Integer getCityId() {
		return cityId;
	}

	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	

}
