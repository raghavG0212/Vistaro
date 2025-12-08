package com.project.vistaro.dto;

public class AuthResponse {

    private String token;
    private String name;
    private String email;
    private String role;
    private String city;
    private Integer userId;   // ⭐ new field added
	public AuthResponse(String token, String name, String email, String role, String city, Integer userId) {
		super();
		this.token = token;
		this.name = name;
		this.email = email;
		this.role = role;
		this.city = city;
		this.userId = userId;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	
}
