package com.project.vistaro.repository;

import java.util.List;
import java.util.Optional;

import com.project.vistaro.model.MovieDetails;

public interface MovieDetailsDao {
	 MovieDetails addMovieDetails(MovieDetails movieDetails);
	    MovieDetails updateMovieDetails(MovieDetails movieDetails);
	    void deleteMovieDetails(Integer id);
	    List<MovieDetails> listAllMovieDetails();
	    MovieDetails getByEventId(Integer eventid);
	    Optional<MovieDetails> findById(Integer id);
	    List<MovieDetails> getByGenre(String genre);
	    void updateRating(Integer eventId, Double newRating, Integer totalReviews);


}
