package com.project.vistaro.controller;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.vistaro.dto.VenueDto;
import com.project.vistaro.model.Venue;
import com.project.vistaro.service.VenueService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/venue")
public class VenueController {

    private final VenueService venueService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public VenueController(VenueService venueService) {
        this.venueService = venueService;
    }

    private Venue dtoToEntity(VenueDto dto) {
        Venue v = new Venue();
        v.setVenueId(dto.getVenueId());
        v.setName(dto.getName());
        v.setAddress(dto.getAddress());
        v.setCity(dto.getCity());
        v.setVenueType(dto.getVenueType());
        v.setScreenName(dto.getScreenName());

        try {
            if (dto.getSeatLayoutJson() != null) {
                String json = objectMapper.writeValueAsString(dto.getSeatLayoutJson());
                v.setSeatLayoutJson(json);
            }
        } catch (Exception e) {
            throw new RuntimeException("Invalid seatLayoutJson format", e);
        }
        return v;
    }

    private VenueDto entityToDto(Venue v) {
        VenueDto dto = new VenueDto();
        dto.setVenueId(v.getVenueId());
        dto.setName(v.getName());
        dto.setAddress(v.getAddress());
        dto.setCity(v.getCity());
        dto.setVenueType(v.getVenueType());
        dto.setScreenName(v.getScreenName());

        try {
            if (v.getSeatLayoutJson() != null) {
                Map<String, Object> map =
                    objectMapper.readValue(v.getSeatLayoutJson(), Map.class);
                dto.setSeatLayoutJson(map);
            }
        } catch (Exception e) {
            dto.setSeatLayoutJson(null);
        }

        return dto;
    }

    @PostMapping
    public ResponseEntity<VenueDto> create(@Valid @RequestBody VenueDto dto) {
        Venue created = venueService.save(dtoToEntity(dto));
        return ResponseEntity.created(URI.create("/api/v1/venue/" + created.getVenueId()))
                .body(entityToDto(created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<VenueDto> update(@PathVariable int id, @Valid @RequestBody VenueDto dto) {
        Venue updated = venueService.update(id, dtoToEntity(dto));
        return ResponseEntity.ok(entityToDto(updated));
    }

    @GetMapping("/{id}")
    public ResponseEntity<VenueDto> getById(@PathVariable int id) {
        return ResponseEntity.ok(entityToDto(venueService.findById(id)));
    }

    @GetMapping("/all")
    public ResponseEntity<List<VenueDto>> getAll() {
        List<VenueDto> res =
            venueService.findAll().stream().map(this::entityToDto).collect(Collectors.toList());
        return ResponseEntity.ok(res);
    }

    @GetMapping("/search/name")
    public ResponseEntity<List<VenueDto>> getByName(@RequestParam String name) {
        List<VenueDto> res =
            venueService.searchByName(name).stream().map(this::entityToDto).collect(Collectors.toList());
        return ResponseEntity.ok(res);
    }

    @GetMapping("/search/city")
    public ResponseEntity<List<VenueDto>> getByCity(@RequestParam String city) {
        List<VenueDto> res =
            venueService.searchByCity(city).stream().map(this::entityToDto).collect(Collectors.toList());
        return ResponseEntity.ok(res);
    }

    @GetMapping("/search/type")
    public ResponseEntity<List<VenueDto>> getByType(@RequestParam String type) {
        List<VenueDto> res =
            venueService.searchByType(type).stream().map(this::entityToDto).collect(Collectors.toList());
        return ResponseEntity.ok(res);
    }

    @GetMapping("/search/screen")
    public ResponseEntity<List<VenueDto>> getByScreen(@RequestParam String screen) {
        List<VenueDto> res =
            venueService.searchByScreen(screen).stream().map(this::entityToDto).collect(Collectors.toList());
        return ResponseEntity.ok(res);
    }
}
