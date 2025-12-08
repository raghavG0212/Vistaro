package com.project.vistaro.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class SeatDto {
	private Integer seatId;   // optional, usually for responses
    private Integer slotId;   // link to EventSlot

    @NotBlank(message = "Row label is required")
    @Size(max = 5, message = "Row label must be less than 5 characters")
    private String rowLabel;

    @NotBlank(message = "Seat number is required")
    @Size(max = 10, message = "Seat number must be less than 10 characters")
    private String seatNumber;

    @NotBlank(message = "Seat type is required")
    @Size(max = 50, message = "Seat type must be less than 50 characters")
    private String seatType;

    @NotNull(message = "Price is required")
    @Min(value = 0, message = "Price cannot be negative")
    private BigDecimal price;
    
    private Boolean isBooked = false;
    private Boolean isLocked = false;

    private LocalDateTime lockedUntil;

    public SeatDto() {}

	public Integer getSeatId() {
		return seatId;
	}

	public void setSeatId(Integer seatId) {
		this.seatId = seatId;
	}

	public Integer getSlotId() {
		return slotId;
	}

	public void setSlotId(Integer slotId) {
		this.slotId = slotId;
	}

	public String getRowLabel() {
		return rowLabel;
	}

	public void setRowLabel(String rowLabel) {
		this.rowLabel = rowLabel;
	}

	public String getSeatNumber() {
		return seatNumber;
	}

	public void setSeatNumber(String seatNumber) {
		this.seatNumber = seatNumber;
	}

	public String getSeatType() {
		return seatType;
	}

	public void setSeatType(String seatType) {
		this.seatType = seatType;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Boolean getIsBooked() {
		return isBooked;
	}

	public void setIsBooked(Boolean isBooked) {
		this.isBooked = isBooked;
	}

	public Boolean getIsLocked() {
		return isLocked;
	}

	public void setIsLocked(Boolean isLocked) {
		this.isLocked = isLocked;
	}

	public LocalDateTime getLockedUntil() {
		return lockedUntil;
	}

	public void setLockedUntil(LocalDateTime lockedUntil) {
		this.lockedUntil = lockedUntil;
	}

	public SeatDto(Integer seatId, Integer slotId,
			@NotBlank(message = "Row label is required") @Size(max = 5, message = "Row label must be less than 5 characters") String rowLabel,
			@NotBlank(message = "Seat number is required") @Size(max = 10, message = "Seat number must be less than 10 characters") String seatNumber,
			@NotBlank(message = "Seat type is required") @Size(max = 50, message = "Seat type must be less than 50 characters") String seatType,
			@NotNull(message = "Price is required") @Min(value = 0, message = "Price cannot be negative") BigDecimal price,
			Boolean isBooked, Boolean isLocked, LocalDateTime lockedUntil) {
		super();
		this.seatId = seatId;
		this.slotId = slotId;
		this.rowLabel = rowLabel;
		this.seatNumber = seatNumber;
		this.seatType = seatType;
		this.price = price;
		this.isBooked = isBooked;
		this.isLocked = isLocked;
		this.lockedUntil = lockedUntil;
	}

	

}
