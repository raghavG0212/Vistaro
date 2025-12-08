package com.project.vistaro.repository;

import java.util.List;
import java.util.Optional;

import com.project.vistaro.model.Event;

public interface EventDao {

    Event save(Event event);

    Optional<Event> findById(int eventId);

    List<Event> findAllByCity(String city);

    List<Event> searchByTitleAndCity(String title, String city);

    List<Event> searchByCategoryAndCity(String category, String city);

    List<Event> searchBySubCategoryAndCity(String subCategory, String city);

    Event update(Event event);
    
    int delete (Integer eventId);
}
