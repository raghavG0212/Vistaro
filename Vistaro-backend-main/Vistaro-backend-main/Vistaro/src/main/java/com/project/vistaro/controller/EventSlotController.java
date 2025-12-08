package com.project.vistaro.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.project.vistaro.dto.EventSlotDto;
import com.project.vistaro.model.EventFormat;
import com.project.vistaro.model.EventSlot;
import com.project.vistaro.service.EventSlotService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/eventslot")
public class EventSlotController {

    private final EventSlotService eventSlotService;

    public EventSlotController(EventSlotService eventSlotService) {
        this.eventSlotService = eventSlotService;
    }

    // --- DTO <-> Entity Mappers ---

    private EventSlot dtoToEntity(EventSlotDto dto) {
        EventSlot slot = new EventSlot();
        slot.setSlotId(dto.getSlotId());
        slot.setEventId(dto.getEventId());
        slot.setVenueId(dto.getVenueId());
        slot.setStartTime(dto.getStartTime());
        slot.setEndTime(dto.getEndTime());
        slot.setFormat(dto.getFormat() != null ? dto.getFormat() : EventFormat.NA);
        slot.setLanguage(dto.getLanguage());
        slot.setBasePrice(dto.getBasePrice());
        slot.setCreatedAt(dto.getCreatedAt());
        return slot;
    }

    private EventSlotDto entityToDto(EventSlot slot) {
        EventSlotDto dto = new EventSlotDto();
        dto.setSlotId(slot.getSlotId());
        dto.setEventId(slot.getEventId());
        dto.setVenueId(slot.getVenueId());
        dto.setStartTime(slot.getStartTime());
        dto.setEndTime(slot.getEndTime());
        dto.setFormat(slot.getFormat());
        dto.setLanguage(slot.getLanguage());
        dto.setBasePrice(slot.getBasePrice());
        dto.setCreatedAt(slot.getCreatedAt());
        return dto;
    }

    // --- ENDPOINTS ---

    @PostMapping
    public ResponseEntity<EventSlotDto> createSlot(@RequestBody EventSlotDto dto) {
        EventSlot entity = dtoToEntity(dto);
        EventSlot created = eventSlotService.addEventSlot(entity);
        return ResponseEntity.ok(entityToDto(created));
    }

    @PutMapping("/{slotId}")
    public ResponseEntity<EventSlotDto> updateSlot(
            @PathVariable Integer slotId,
            @RequestBody EventSlotDto dto) {

        EventSlot entity = dtoToEntity(dto);
        EventSlot updated = eventSlotService.updateEventSlot(slotId, entity);
        return ResponseEntity.ok(entityToDto(updated));
    }

    @DeleteMapping("/{slotId}")
    public ResponseEntity<Void> deleteSlot(@PathVariable Integer slotId) {
        eventSlotService.deleteEventSlot(slotId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{slotId}")
    public ResponseEntity<EventSlotDto> getById(@PathVariable Integer slotId) {
        EventSlot slot = eventSlotService.getById(slotId);
        return ResponseEntity.ok(entityToDto(slot));
    }

    @GetMapping("/event/{eventId}")
    public ResponseEntity<List<EventSlotDto>> getByEvent(@PathVariable Integer eventId) {
        List<EventSlot> slots = eventSlotService.getByEventId(eventId);
        List<EventSlotDto> dtos = slots.stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("search/city")
    public ResponseEntity<List<EventSlotDto>> getByCity(@RequestParam String city) {
        List<EventSlot> slots = eventSlotService.getByCity(city);
        List<EventSlotDto> dtos = slots.stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping
    public ResponseEntity<List<EventSlotDto>> getAll() {
        List<EventSlot> slots = eventSlotService.getAllSlots();
        List<EventSlotDto> dtos = slots.stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }
    
    @PostMapping("/bulk")
    public ResponseEntity<List<EventSlotDto>> createSlots(@RequestBody List<EventSlotDto> dtos) {
        List<EventSlot> saved = dtos.stream()
            .map(this::dtoToEntity)
            .map(eventSlotService::addEventSlot)
            .collect(Collectors.toList());

        List<EventSlotDto> result = saved.stream()
            .map(this::entityToDto)
            .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

}

