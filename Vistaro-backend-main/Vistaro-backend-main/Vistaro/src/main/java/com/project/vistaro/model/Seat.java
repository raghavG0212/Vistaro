package com.project.vistaro.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Seat {

    private Integer seatId;
    private Integer slotId;
    private String rowLabel;
    private String seatNumber;
    private String seatType;
    private BigDecimal price;
    private Boolean isBooked = false;
    private Boolean isLocked = false;
    private LocalDateTime lockedUntil;

    public Seat() {}

    public Seat(Integer seatId, Integer slotId, String rowLabel, String seatNumber,
                String seatType, BigDecimal price,
                Boolean isBooked, Boolean isLocked, LocalDateTime lockedUntil) {
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

    public Integer getSeatId() { return seatId; }
    public void setSeatId(Integer seatId) { this.seatId = seatId; }

    public Integer getSlotId() { return slotId; }
    public void setSlotId(Integer slotId) { this.slotId = slotId; }

    public String getRowLabel() { return rowLabel; }
    public void setRowLabel(String rowLabel) { this.rowLabel = rowLabel; }

    public String getSeatNumber() { return seatNumber; }
    public void setSeatNumber(String seatNumber) { this.seatNumber = seatNumber; }

    public String getSeatType() { return seatType; }
    public void setSeatType(String seatType) { this.seatType = seatType; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public Boolean getIsBooked() { return isBooked; }
    public void setIsBooked(Boolean isBooked) { this.isBooked = isBooked; }

    public Boolean getIsLocked() { return isLocked; }
    public void setIsLocked(Boolean isLocked) { this.isLocked = isLocked; }

    public LocalDateTime getLockedUntil() { return lockedUntil; }
    public void setLockedUntil(LocalDateTime lockedUntil) { this.lockedUntil = lockedUntil; }
}
