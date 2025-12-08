package com.project.vistaro.dto;

import java.time.LocalDateTime;
import java.util.List;

public class SeatLockResponseDto {

    private String status;
    private List<Integer> seatIds;
    private LocalDateTime lockedUntil; // 5 minutes from now

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Integer> getSeatIds() {
        return seatIds;
    }

    public void setSeatIds(List<Integer> seatIds) {
        this.seatIds = seatIds;
    }

    public LocalDateTime getLockedUntil() {
        return lockedUntil;
    }

    public void setLockedUntil(LocalDateTime lockedUntil) {
        this.lockedUntil = lockedUntil;
    }
}
