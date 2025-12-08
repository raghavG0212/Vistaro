package com.project.vistaro.service;

import java.util.List;


import com.project.vistaro.model.Event;

public interface EventService {

    Event create(Event e);

    Event findById(int eventId);

    List<Event> findAllByCity(String city);

    List<Event> searchByTitle(String city, String title);

    List<Event> searchByCategory(String city, String category);

    List<Event> searchBySubCategory(String city, String subCategory);

    Event update(int id, Event e);
    
    void deleteEvent(Integer eventId);
}
