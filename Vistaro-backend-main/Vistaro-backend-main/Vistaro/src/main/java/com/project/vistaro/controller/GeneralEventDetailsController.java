package com.project.vistaro.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.vistaro.dto.GeneralEventDetailsDto;
import com.project.vistaro.model.GeneralEventDetails;
import com.project.vistaro.service.GeneralEventDetailsService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/event-details")
public class GeneralEventDetailsController {

	@Autowired
	private GeneralEventDetailsService service;
	
	@PostMapping
	public GeneralEventDetailsDto addDetails(@Valid @RequestBody GeneralEventDetailsDto dto) {
		GeneralEventDetails entity = dtoToEntity(dto);
		GeneralEventDetails saved = service.addEventDetails(entity);
		return entityToDto(saved);
	}
	
	@GetMapping("/{id}")
	public GeneralEventDetailsDto getById(@PathVariable Integer id) {
		return entityToDto(service.getDetailsById(id));
	}
	
	@GetMapping("/event/{eventId}")
	public GeneralEventDetailsDto getByEvent(@PathVariable Integer eventId) {
		return entityToDto(service.getDetailsByEventId(eventId));
	}
	
	@GetMapping
	public List<GeneralEventDetailsDto> getAll(){
		return service.getAllDetails().stream().map(this::entityToDto).collect(Collectors.toList());
	}
	
	@DeleteMapping("/{id}")
	public String delete(@PathVariable Integer id) {
		service.deleteDetails(id);
		return "Event details deleted successfully";
	}
	
	@PutMapping("/{id}")
	public GeneralEventDetailsDto update(@PathVariable Integer id, @Valid @RequestBody GeneralEventDetailsDto dto) {
		GeneralEventDetails entity = dtoToEntity(dto);
		GeneralEventDetails updated = service.update(id, entity);
		
		return entityToDto(updated);
	}
	
	private GeneralEventDetails dtoToEntity(GeneralEventDetailsDto dto) {
		GeneralEventDetails e = new GeneralEventDetails();
		e.setGeneralDetailsId(dto.getGeneralDetailsId());
		e.setEventId(dto.getEventId());
		e.setArtist(dto.getArtist());
		e.setHost(dto.getHost());
		e.setGenre(dto.getGenre());
		e.setStartTime(dto.getStartTime());
		e.setEndTime(dto.getEndTime());
		e.setAdditionalInfo(dto.getAdditionalInfo()); 
		
		
		return e;
	}
	
	private GeneralEventDetailsDto entityToDto(GeneralEventDetails e) {
		GeneralEventDetailsDto dto = new GeneralEventDetailsDto();
		dto.setGeneralDetailsId(e.getGeneralDetailsId());
		dto.setEventId(e.getEventId());
		dto.setArtist(e.getArtist());
		dto.setHost(e.getHost());
		dto.setGenre(e.getGenre());
		dto.setStartTime(e.getStartTime());
		dto.setEndTime(e.getEndTime());
		dto.setAdditionalInfo(e.getAdditionalInfo());
		
		
		return dto;
	}
}
