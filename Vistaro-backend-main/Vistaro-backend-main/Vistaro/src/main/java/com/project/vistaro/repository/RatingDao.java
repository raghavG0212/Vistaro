package com.project.vistaro.repository;

import java.util.Optional;

import com.project.vistaro.model.Rating;

public interface RatingDao {

    Optional<Rating> findByUserAndEvent(Integer userId, Integer eventId);

    Rating save(Rating rating); // insert only

}
