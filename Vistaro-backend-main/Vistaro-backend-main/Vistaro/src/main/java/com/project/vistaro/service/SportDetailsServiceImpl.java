package com.project.vistaro.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.project.vistaro.exception.ResourceNotFoundException;
import com.project.vistaro.model.SportDetails;
import com.project.vistaro.repository.SportDetailsDao;

@Service
public class SportDetailsServiceImpl implements SportDetailsService {

	private final SportDetailsDao sportDetailsDao;

    public SportDetailsServiceImpl(SportDetailsDao sportDetailsDao) {
        this.sportDetailsDao = sportDetailsDao;
    }
    
	@Override
	public SportDetails addSportDetails(SportDetails sportDetails) {
		// TODO Auto-generated method stub
		 return sportDetailsDao.addSportDetails(sportDetails);
	}

	@Override
	public SportDetails updateSportDetails(SportDetails sportDetails) {
		// TODO Auto-generated method stub
		 return sportDetailsDao.updateSportDetails(sportDetails);
	}

	@Override
	public void deleteSportDetails(Integer id) {
		// TODO Auto-generated method stub
		sportDetailsDao.deleteSportDetails(id);
		
	}

	@Override
	public List<SportDetails> listAllSportDetails() {
		// TODO Auto-generated method stub
		return sportDetailsDao.listAllSportDetails();
	}

	@Override
	public SportDetails findById(Integer id) {
		return sportDetailsDao.findById(id)
			       .orElseThrow(()->new ResourceNotFoundException("Sport details not found with id : "+id));
		
	}

	@Override
	public List<SportDetails> getBySportType(String sportType) {
		// TODO Auto-generated method stub
		return sportDetailsDao.getBySportType(sportType);
	}

	@Override
	public SportDetails findByEventId(Integer eventId) {
		// TODO Auto-generated method stub
		return sportDetailsDao.findByEventId(eventId);
	}

}
