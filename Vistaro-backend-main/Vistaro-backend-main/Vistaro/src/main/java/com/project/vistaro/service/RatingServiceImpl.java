package com.project.vistaro.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.project.vistaro.exception.BusinessRuleException;
import com.project.vistaro.model.MovieDetails;
import com.project.vistaro.model.Rating;
import com.project.vistaro.repository.MovieDetailsDao;
import com.project.vistaro.repository.RatingDao;

@Service
public class RatingServiceImpl implements RatingService {

    private final RatingDao ratingDao;
    private final MovieDetailsDao movieDetailsDao;

    public RatingServiceImpl(RatingDao ratingDao, MovieDetailsDao movieDetailsDao) {
        this.ratingDao = ratingDao;
        this.movieDetailsDao = movieDetailsDao;
    }

    @Override
    public Optional<Rating> getUserRating(Integer userId, Integer eventId) {
        return ratingDao.findByUserAndEvent(userId, eventId);
    }

    @Override
    public Rating submitRating(Integer userId, Integer eventId, Integer stars) {

        if (stars < 1 || stars > 5) {
            throw new BusinessRuleException("Stars must be between 1 and 5.");
        }

        // Check if already rated
        if (ratingDao.findByUserAndEvent(userId, eventId).isPresent()) {
            throw new BusinessRuleException("You have already rated this movie.");
        }

        // Save rating
        Rating r = new Rating();
        r.setUserId(userId);
        r.setEventId(eventId);
        r.setRating(stars);
        r.setCreatedAt(LocalDateTime.now());
        ratingDao.save(r);

        // Update movie details rating
        MovieDetails md = movieDetailsDao.getByEventId(eventId);

        int oldCount = md.getTotalReviews();
        double oldRating = md.getRating() != null ? md.getRating().doubleValue() : 0.0;

        // formula → newAverage = (oldRating*count + stars*2) / (count+1)
        double newRating = ((oldRating * oldCount) + (stars * 2)) / (oldCount + 1);

        md.setRating(newRating);
        md.setTotalReviews(oldCount + 1);
        movieDetailsDao.updateMovieDetails(md);

        return r;
    }
}
