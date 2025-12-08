package com.project.vistaro.repository;

import java.util.List;
import java.util.Optional;

import com.project.vistaro.model.SportDetails;

public interface SportDetailsDao {
	SportDetails addSportDetails(SportDetails sportDetails);
    SportDetails updateSportDetails(SportDetails sportDetails);
    void deleteSportDetails(Integer id);
    List<SportDetails> listAllSportDetails();
    Optional<SportDetails> findById(Integer id);
    SportDetails findByEventId(Integer eventId);
    List<SportDetails> getBySportType(String sportType);

}
