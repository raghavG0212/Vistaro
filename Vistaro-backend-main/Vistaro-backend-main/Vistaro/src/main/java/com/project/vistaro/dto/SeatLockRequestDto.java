package com.project.vistaro.dto;

import java.util.List;
import jakarta.validation.constraints.NotEmpty;

public class SeatLockRequestDto {

    @NotEmpty(message = "Seat IDs must not be empty")
    private List<Integer> seatIds;

    public List<Integer> getSeatIds() {
        return seatIds;
    }

    public void setSeatIds(List<Integer> seatIds) {
        this.seatIds = seatIds;
    }
}
