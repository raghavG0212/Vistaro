package com.project.vistaro.controller;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.vistaro.dto.EventDto;
import com.project.vistaro.model.Event;
import com.project.vistaro.service.EventService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/event")
public class EventController {

    private final EventService service;

    public EventController(EventService service) {
        this.service = service;
    }

    private Event dtoToEntity(EventDto dto) {
        Event e = new Event();
        e.setEventId(dto.getEventId());
        e.setTitle(dto.getTitle());
        e.setDescription(dto.getDescription());
        e.setCategory(dto.getCategory());
        e.setSubCategory(dto.getSubCategory());
        e.setBannerUrl(dto.getBannerUrl());
        e.setThumbnailUrl(dto.getThumbnailUrl());
        e.setStartTime(dto.getStartTime());
        e.setEndTime(dto.getEndTime());
        return e;
    }

    private EventDto entityToDto(Event e) {
        EventDto dto = new EventDto();
        dto.setEventId(e.getEventId());
        dto.setTitle(e.getTitle());
        dto.setDescription(e.getDescription());
        dto.setCategory(e.getCategory());
        dto.setSubCategory(e.getSubCategory());
        dto.setBannerUrl(e.getBannerUrl());
        dto.setThumbnailUrl(e.getThumbnailUrl());
        dto.setStartTime(e.getStartTime());
        dto.setEndTime(e.getEndTime());
        return dto;
    }

    // -------- USER FILTERED BY CITY --------

    @GetMapping
    public ResponseEntity<List<EventDto>> getAll(@RequestParam String city) {
        List<EventDto> res = service.findAllByCity(city)
                .stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(res);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<EventDto> getById(@PathVariable int id) {
        Event event = service.findById(id);
        return ResponseEntity.ok(entityToDto(event));
    }


    @GetMapping("/search/title")
    public ResponseEntity<List<EventDto>> getByTitle(
            @RequestParam String city,
            @RequestParam String title) {

        List<EventDto> res = service.searchByTitle(city, title)
                .stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(res);
    }

    @GetMapping("/search/category")
    public ResponseEntity<List<EventDto>> getByCategory(
            @RequestParam String city,
            @RequestParam String category) {

        List<EventDto> res = service.searchByCategory(city, category)
                .stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(res);
    }

    @GetMapping("/search/sub-category")
    public ResponseEntity<List<EventDto>> getBySubCategory(
            @RequestParam String city,
            @RequestParam String subCategory) {

        List<EventDto> res = service.searchBySubCategory(city, subCategory)
                .stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(res);
    }

    // -------- ADMIN (NO CITY FILTER) --------

    @PostMapping
    public ResponseEntity<EventDto> create(@Valid @RequestBody EventDto dto) {
        Event created = service.create(dtoToEntity(dto));
        return ResponseEntity.created(URI.create("/api/v1/event/" + created.getEventId()))
                .body(entityToDto(created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventDto> update(
            @PathVariable int id, 
            @Valid @RequestBody EventDto dto) {

        Event updated = service.update(id, dtoToEntity(dto));
        return ResponseEntity.ok(entityToDto(updated));
    }
    
    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Integer eventId){
    	service.deleteEvent(eventId);
    	return ResponseEntity.ok().build();
    }
}
