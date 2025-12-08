package com.project.vistaro.repository;

import java.util.List;

import com.project.vistaro.model.GeneralEventDetails;

public interface GeneralEventDetailsDao {
	
	GeneralEventDetails save(GeneralEventDetails details);
	GeneralEventDetails findById(Integer id);
	GeneralEventDetails findByEventId(Integer eventId);
	List<GeneralEventDetails> findAll();
	GeneralEventDetails update(Integer id, GeneralEventDetails d);
	void delete(Integer id);

}
