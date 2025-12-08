package com.project.vistaro.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.project.vistaro.dto.SeatDto;
import com.project.vistaro.model.Seat;
import com.project.vistaro.service.SeatService;

@RestController
@RequestMapping("/api/v1/seat")
public class SeatController {

    private final SeatService seatService;

    public SeatController(SeatService seatService) {
        this.seatService = seatService;
    }

    private SeatDto entityToDto(Seat s) {
        SeatDto dto = new SeatDto();
        dto.setSeatId(s.getSeatId());
        dto.setSlotId(s.getSlotId());
        dto.setRowLabel(s.getRowLabel());
        dto.setSeatNumber(s.getSeatNumber());
        dto.setSeatType(s.getSeatType());
        dto.setPrice(s.getPrice());
        dto.setIsBooked(s.getIsBooked());
        dto.setIsLocked(s.getIsLocked());
        dto.setLockedUntil(s.getLockedUntil());
        return dto;
    }

    @GetMapping("/slot/{slotId}")
    public ResponseEntity<List<SeatDto>> getSeatsForSlot(@PathVariable int slotId) {
        List<SeatDto> res = seatService.getSeatsBySlot(slotId)
                .stream().map(this::entityToDto).collect(Collectors.toList());
        return ResponseEntity.ok(res);
    }

    @PostMapping("/lock")
    public ResponseEntity<String> lockSeats(@RequestBody Map<String, List<Integer>> body) {
        List<Integer> seatIds = body.get("seatIds");
        seatService.lockSeats(seatIds);
        return ResponseEntity.ok("Seats locked");
    }

    @PostMapping("/unlock")
    public ResponseEntity<String> unlockSeats(@RequestBody Map<String, List<Integer>> body) {
        List<Integer> seatIds = body.get("seatIds");
        seatService.unlockSeats(seatIds);
        return ResponseEntity.ok("Seats unlocked");
    }
}
