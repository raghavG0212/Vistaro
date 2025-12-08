package com.project.vistaro.service;

import java.util.List;
import com.project.vistaro.model.Venue;

public interface VenueService {

    Venue save(Venue venue);
    Venue findById(int venueId);
    List<Venue> findAll();
    Venue update(int id, Venue venue);
    List<Venue> searchByName(String name);
    List<Venue> searchByCity(String city);
    List<Venue> searchByType(String type);
    List<Venue> searchByScreen(String screen);
}
