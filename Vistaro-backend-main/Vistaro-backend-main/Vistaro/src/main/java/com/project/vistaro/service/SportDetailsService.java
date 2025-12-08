package com.project.vistaro.service;

import java.util.List;
import com.project.vistaro.model.SportDetails;

public interface SportDetailsService {
    SportDetails addSportDetails(SportDetails sportDetails);
    SportDetails updateSportDetails(SportDetails sportDetails);
    void deleteSportDetails(Integer id);
    List<SportDetails> listAllSportDetails();
    SportDetails findById(Integer id);
    SportDetails findByEventId(Integer eventId);
    List<SportDetails> getBySportType(String sportType);

}
