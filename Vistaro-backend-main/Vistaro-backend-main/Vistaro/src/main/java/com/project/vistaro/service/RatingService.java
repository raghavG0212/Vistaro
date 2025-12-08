package com.project.vistaro.service;

import java.util.Optional;

import com.project.vistaro.model.Rating;

public interface RatingService {

    Optional<Rating> getUserRating(Integer userId, Integer eventId);

    Rating submitRating(Integer userId, Integer eventId, Integer stars);

}
