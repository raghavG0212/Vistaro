package com.project.vistaro.repository;

import java.util.List;
import java.util.Optional;

import com.project.vistaro.model.Venue;

public interface VenueDao {

    Venue save(Venue venue);
    Optional<Venue> findById(int venueId);
    List<Venue> findAll();
    Venue update(Venue venue);
    List<Venue> searchByName(String name);
    List<Venue> searchByCity(String city);
    List<Venue> searchByType(String type);
    List<Venue> searchByScreen(String screen);
}
