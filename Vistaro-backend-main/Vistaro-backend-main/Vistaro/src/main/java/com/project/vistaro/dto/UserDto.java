package com.project.vistaro.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UserDto {
	 @NotBlank(message = "Name is required")
	    @Size(max = 120, message = "Name must be less than 120 characters")
	    private String name;

	    @NotBlank(message = "Email is required")
	    @Email(message = "Email should be valid")
	    @Size(max = 150, message = "Email must be less than 150 characters")
	    private String email;

	    @Size(max = 20, message = "Phone number must be less than 20 characters")
	    private String phone;

	    @NotBlank(message = "Password is required")
	    @Size(min = 6, max = 255, message = "Password must be between 6 and 255 characters")
	    private String password;

	    @NotBlank(message = "City is required")
	    private String city;

	    private String role = "USER"; // default
	    
	    public UserDto() {}

		public UserDto(
				@NotBlank(message = "Name is required") @Size(max = 120, message = "Name must be less than 120 characters") String name,
				@NotBlank(message = "Email is required") @Email(message = "Email should be valid") @Size(max = 150, message = "Email must be less than 150 characters") String email,
				@Size(max = 20, message = "Phone number must be less than 20 characters") String phone,
				@NotBlank(message = "Password is required") @Size(min = 6, max = 255, message = "Password must be between 6 and 255 characters") String password,
				@NotBlank(message = "City is required") String city, String role) {
			super();
			this.name = name;
			this.email = email;
			this.phone = phone;
			this.password = password;
			this.city = city;
			this.role = role;
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

		public String getPhone() {
			return phone;
		}

		public void setPhone(String phone) {
			this.phone = phone;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public String getCity() {
			return city;
		}

		public void setCity(String city) {
			this.city = city;
		}

		public String getRole() {
			return role;
		}

		public void setRole(String role) {
			this.role = role;
		};


		
}


