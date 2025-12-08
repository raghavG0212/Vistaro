package com.project.vistaro.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.project.vistaro.exception.ResourceNotFoundException;
import com.project.vistaro.model.MovieDetails;
import com.project.vistaro.repository.MovieDetailsDao;

@Service
public class MovieDetailsServiceImpl implements MovieDetailsService{

	private final MovieDetailsDao movieDetailsDao;
	
	public MovieDetailsServiceImpl(MovieDetailsDao movieDetailsDao) {
        this.movieDetailsDao = movieDetailsDao;
    }
	
	@Override
	public MovieDetails addMovieDetails(MovieDetails movieDetails) {
		// TODO Auto-generated method stub
		 return movieDetailsDao.addMovieDetails(movieDetails);
	}

	@Override
	public MovieDetails updateMovieDetails(MovieDetails movieDetails) {
		// TODO Auto-generated method stub
		return movieDetailsDao.updateMovieDetails(movieDetails);
	}

	@Override
	public void deleteMovieDetails(Integer id) {
		// TODO Auto-generated method stub
		 movieDetailsDao.deleteMovieDetails(id);
	}

	@Override
	public List<MovieDetails> listAllMovieDetails() {
		// TODO Auto-generated method stub
		return movieDetailsDao.listAllMovieDetails();
	}

	@Override
	public MovieDetails findById(Integer id) {
		// TODO Auto-generated method stub
		return movieDetailsDao.findById(id)
			       .orElseThrow(()->new ResourceNotFoundException("Movie details not found with id : "+id));
	}

	@Override
	public List<MovieDetails> getByGenre(String genre) {
		// TODO Auto-generated method stub
		 return movieDetailsDao.getByGenre(genre);
	}

	@Override
	public MovieDetails findByEventId(Integer eventId) {
		// TODO Auto-generated method stub
		return movieDetailsDao.getByEventId(eventId);
	}

}
