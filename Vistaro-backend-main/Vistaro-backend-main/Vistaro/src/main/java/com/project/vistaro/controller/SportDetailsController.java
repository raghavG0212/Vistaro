package com.project.vistaro.controller;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.vistaro.dto.GeneralEventDetailsDto;
import com.project.vistaro.dto.SportDetailsDto;
import com.project.vistaro.model.SportDetails;
import com.project.vistaro.service.SportDetailsService;

@RestController
@RequestMapping("/api/v1/sport")
public class SportDetailsController {

	private final SportDetailsService sportService;

	public SportDetailsController(SportDetailsService sportService) {
		this.sportService = sportService;
	}

	// Conversion methods inside controller
	private SportDetails dtoToEntity(SportDetailsDto dto) {
		SportDetails entity = new SportDetails();
		entity.setSportDetailsId(dto.getSportDetailsId());
		entity.setEventId(dto.getEventId());
		entity.setSportType(dto.getSportType());
		entity.setTeam1(dto.getTeam1());
		entity.setTeam2(dto.getTeam2());
		entity.setMatchFormat(dto.getMatchFormat());
		entity.setVenueInfo(dto.getVenueInfo());
		entity.setStartTime(dto.getStartTime());
		entity.setEndTime(dto.getEndTime());
		return entity;
	}

	private SportDetailsDto entityToDto(SportDetails entity) {
		SportDetailsDto dto = new SportDetailsDto();
		dto.setSportDetailsId(entity.getSportDetailsId());
		dto.setEventId(entity.getEventId());
		dto.setSportType(entity.getSportType());
		dto.setTeam1(entity.getTeam1());
		dto.setTeam2(entity.getTeam2());
		dto.setMatchFormat(entity.getMatchFormat());
		dto.setVenueInfo(entity.getVenueInfo());
		dto.setStartTime(entity.getStartTime());
		dto.setEndTime(entity.getEndTime());
		return dto;
	}

	@PostMapping
	public SportDetailsDto addSport(@RequestBody SportDetailsDto dto) {
		SportDetails saved = sportService.addSportDetails(dtoToEntity(dto));
		return entityToDto(saved);
	}

	@PutMapping("/{id}")
	public SportDetailsDto updateSport(@PathVariable Integer id, @RequestBody SportDetailsDto dto) {
		SportDetails entity = dtoToEntity(dto);
		entity.setSportDetailsId(id);
		SportDetails updated = sportService.updateSportDetails(entity);
		return entityToDto(updated);
	}

	@DeleteMapping("/{id}")
	public void deleteSport(@PathVariable Integer id) {
		sportService.deleteSportDetails(id);
	}

	@GetMapping("/{id}")
	public SportDetailsDto getSportById(@PathVariable Integer id) {
		SportDetails sport = sportService.findById(id); // service returns SportDetails directly
		return entityToDto(sport);
	}
	
	@GetMapping("/event/{eventId}")
	public SportDetailsDto getByEvent(@PathVariable Integer eventId) {
		return entityToDto(sportService.findByEventId(eventId));
	}
	
	@GetMapping
	public List<SportDetailsDto> listAllSports() {
		return sportService.listAllSportDetails().stream().map(this::entityToDto).collect(Collectors.toList());
	}

	@GetMapping("/type/{sportType}")
	public List<SportDetailsDto> getSportsByType(@PathVariable String sportType) {
		return sportService.getBySportType(sportType).stream().map(this::entityToDto).collect(Collectors.toList());
	}
}
