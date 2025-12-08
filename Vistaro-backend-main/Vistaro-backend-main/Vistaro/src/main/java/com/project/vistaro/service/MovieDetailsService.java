package com.project.vistaro.service;

import java.util.List;
import com.project.vistaro.model.MovieDetails;

public interface MovieDetailsService {
	MovieDetails addMovieDetails(MovieDetails movieDetails);
    MovieDetails updateMovieDetails(MovieDetails movieDetails);
    void deleteMovieDetails(Integer id);
    List<MovieDetails> listAllMovieDetails();
    MovieDetails findById(Integer id);
    MovieDetails findByEventId(Integer eventId);
    List<MovieDetails> getByGenre(String genre);

}
