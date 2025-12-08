package com.project.vistaro.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.project.vistaro.dto.RatingDto;
import com.project.vistaro.service.RatingService;

@RestController
@RequestMapping("/api/v1/ratings")
@CrossOrigin
public class RatingController {

    private final RatingService service;

    public RatingController(RatingService service) {
        this.service = service;
    }

    @GetMapping("/check")
    public ResponseEntity<?> checkRating(
        @RequestParam Integer userId,
        @RequestParam Integer eventId
    ) {
        return service.getUserRating(userId, eventId)
            .map(r -> ResponseEntity.ok(Map.of(
                "exists", true,
                "stars", r.getRating()
            )))
            .orElse(ResponseEntity.ok(Map.of("exists", false)));
    }

    @PostMapping("/add")
    public ResponseEntity<?> addRating(@RequestBody RatingDto dto) {
        service.submitRating(dto.userId, dto.eventId, dto.stars);
        return ResponseEntity.ok(Map.of("message", "Rating submitted successfully"));
    }
}
