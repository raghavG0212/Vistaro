package com.project.vistaro.service;

import java.util.List;

import com.project.vistaro.model.GeneralEventDetails;

public interface GeneralEventDetailsService {
	
	GeneralEventDetails addEventDetails(GeneralEventDetails details);
	GeneralEventDetails getDetailsById(Integer id);
	GeneralEventDetails getDetailsByEventId(Integer eventId);
	List<GeneralEventDetails> getAllDetails();
	void deleteDetails(Integer id);
	GeneralEventDetails update(Integer id, GeneralEventDetails d);
}
