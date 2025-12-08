package com.project.vistaro.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.project.vistaro.exception.ResourceNotFoundException;
import com.project.vistaro.model.Venue;
import com.project.vistaro.repository.VenueDao;

@Service
public class VenueServiceImpl implements VenueService {

    private final VenueDao venueDao;

    public VenueServiceImpl(VenueDao venueDao) {
        this.venueDao = venueDao;
    }

    @Override
    public Venue save(Venue venue) {
        return venueDao.save(venue);
    }

    @Override
    public Venue findById(int venueId) {
        return venueDao.findById(venueId)
                .orElseThrow(() -> new ResourceNotFoundException("Venue not found with id : " + venueId));
    }

    @Override
    public List<Venue> findAll() {
        return venueDao.findAll();
    }

    @Override
    public Venue update(int id, Venue v) {
        Venue existing = findById(id);

        if (v.getName() != null) existing.setName(v.getName());
        if (v.getCity() != null) existing.setCity(v.getCity());
        if (v.getAddress() != null) existing.setAddress(v.getAddress());
        if (v.getVenueType() != null) existing.setVenueType(v.getVenueType());

        return venueDao.update(existing);
    }

    @Override
    public List<Venue> searchByName(String name) {
        return venueDao.searchByName(name);
    }

    @Override
    public List<Venue> searchByCity(String city) {
        return venueDao.searchByCity(city);
    }

    @Override
    public List<Venue> searchByType(String type) {
        return venueDao.searchByType(type);
    }

    @Override
    public List<Venue> searchByScreen(String screen) {
        return venueDao.searchByScreen(screen);
    }
}
