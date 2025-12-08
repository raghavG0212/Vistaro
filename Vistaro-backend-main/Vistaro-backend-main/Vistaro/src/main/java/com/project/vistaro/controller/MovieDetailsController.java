package com.project.vistaro.controller;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.vistaro.dto.MovieDetailsDto;
import com.project.vistaro.model.MovieDetails;
import com.project.vistaro.service.MovieDetailsService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/movie")
public class MovieDetailsController {

    private final MovieDetailsService movieService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public MovieDetailsController(MovieDetailsService movieService) {
        this.movieService = movieService;
    }

    // -------------------- DTO → ENTITY --------------------

    private MovieDetails dtoToEntity(MovieDetailsDto dto) {
        MovieDetails entity = new MovieDetails();

        entity.setMovieDetailsId(dto.getMovieDetailsId());
        // event relationship should be handled inside service if needed
        entity.setEventId(dto.getEventId());

        // Convert JSON object/array → String
        try {
            if (dto.getCastJson() != null) {
                String json = objectMapper.writeValueAsString(dto.getCastJson());
                entity.setCastJson(json);
            }
        } catch (Exception e) {
            throw new RuntimeException("Invalid castJson format", e);
        }

        entity.setDirector(dto.getDirector());
        entity.setGenre(dto.getGenre());
        entity.setLanguage(dto.getLanguage());
        entity.setRating(dto.getRating());
        entity.setTotalReviews(dto.getTotalReviews());
        entity.setTrailerUrl(dto.getTrailerUrl());
        entity.setReleaseDate(dto.getReleaseDate());

        return entity;
    }

    // -------------------- ENTITY → DTO --------------------

    private MovieDetailsDto entityToDto(MovieDetails entity) {
        MovieDetailsDto dto = new MovieDetailsDto();

        dto.setMovieDetailsId(entity.getMovieDetailsId());
        dto.setEventId(entity.getEventId());

        // Convert stored JSON string → Object (Map/List)
        try {
            if (entity.getCastJson() != null) {
                Object castObj = objectMapper.readValue(entity.getCastJson(), Object.class);
                dto.setCastJson(castObj);
            }
        } catch (Exception e) {
            dto.setCastJson(null);
        }

        dto.setDirector(entity.getDirector());
        dto.setGenre(entity.getGenre());
        dto.setLanguage(entity.getLanguage());
        dto.setRating(entity.getRating());
        dto.setTotalReviews(entity.getTotalReviews());
        dto.setTrailerUrl(entity.getTrailerUrl());
        dto.setReleaseDate(entity.getReleaseDate());

        return dto;
    }

    // -------------------- REST ENDPOINTS --------------------

    @PostMapping
    public ResponseEntity<MovieDetailsDto> create(@Valid @RequestBody MovieDetailsDto dto) {
        MovieDetails created = movieService.addMovieDetails(dtoToEntity(dto));
        return ResponseEntity.created(URI.create("/api/v1/movie/" + created.getMovieDetailsId()))
                .body(entityToDto(created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MovieDetailsDto> update(@PathVariable int id, @Valid @RequestBody MovieDetailsDto dto) {
        MovieDetails entity = dtoToEntity(dto);
        entity.setMovieDetailsId(id);
        MovieDetails updated = movieService.updateMovieDetails(entity);
        return ResponseEntity.ok(entityToDto(updated));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovieDetailsDto> getById(@PathVariable int id) {
        return ResponseEntity.ok(entityToDto(movieService.findById(id)));
    }

    @GetMapping("/event/{eventId}")
    public ResponseEntity<MovieDetailsDto> getByEvent(@PathVariable int eventId) {
        return ResponseEntity.ok(entityToDto(movieService.findByEventId(eventId)));
    }

    @GetMapping
    public ResponseEntity<List<MovieDetailsDto>> getAll() {
        List<MovieDetailsDto> res =
                movieService.listAllMovieDetails().stream()
                        .map(this::entityToDto)
                        .collect(Collectors.toList());
        return ResponseEntity.ok(res);
    }

    @GetMapping("/genre/{genre}")
    public ResponseEntity<List<MovieDetailsDto>> getByGenre(@PathVariable String genre) {
        List<MovieDetailsDto> res =
                movieService.getByGenre(genre).stream()
                        .map(this::entityToDto)
                        .collect(Collectors.toList());
        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        movieService.deleteMovieDetails(id);
    }
}
