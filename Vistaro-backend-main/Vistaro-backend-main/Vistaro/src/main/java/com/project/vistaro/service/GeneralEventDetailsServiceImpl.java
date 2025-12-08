package com.project.vistaro.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.project.vistaro.model.GeneralEventDetails;
import com.project.vistaro.repository.GeneralEventDetailsDao;

@Service
public class GeneralEventDetailsServiceImpl implements GeneralEventDetailsService{
	
	private final GeneralEventDetailsDao dao;
	public GeneralEventDetailsServiceImpl(GeneralEventDetailsDao dao) {
		this.dao = dao;
	}
	
	@Override
	public GeneralEventDetails addEventDetails(GeneralEventDetails details) {
		return dao.save(details);
	}
	
	@Override
	public GeneralEventDetails getDetailsById(Integer id) {
		return dao.findById(id);
	}
	
	@Override
	public GeneralEventDetails getDetailsByEventId(Integer eventId) {
		return dao.findByEventId(eventId);
	}
	
	@Override
	public List<GeneralEventDetails> getAllDetails(){
		return dao.findAll();
	}
	
	@Override
	public void deleteDetails(Integer id) {
		dao.delete(id);
	}
	
	@Override
	public GeneralEventDetails update(Integer id, GeneralEventDetails d) {
		return dao.update(id, d);
	}

}
