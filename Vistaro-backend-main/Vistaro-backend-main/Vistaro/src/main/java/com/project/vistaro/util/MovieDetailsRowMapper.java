package com.project.vistaro.util;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.project.vistaro.model.MovieDetails;

public class MovieDetailsRowMapper implements RowMapper<MovieDetails>  {
	
	 @Override
	    public MovieDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
	        MovieDetails movie = new MovieDetails();
	        movie.setMovieDetailsId(rs.getInt("movie_details_id"));
	        movie.setCastJson(rs.getString("cast_json"));
	        movie.setDirector(rs.getString("director"));
	        movie.setGenre(rs.getString("genre"));
	        movie.setLanguage(rs.getString("language"));
	        movie.setRating(rs.getDouble("rating"));
	        movie.setTotalReviews(rs.getInt("total_reviews"));
	        movie.setTrailerUrl(rs.getString("trailer_url"));
	        if (rs.getDate("release_date") != null) {
	            movie.setReleaseDate(rs.getDate("release_date").toLocalDate());
	        }
	        return movie;
	    }

}
