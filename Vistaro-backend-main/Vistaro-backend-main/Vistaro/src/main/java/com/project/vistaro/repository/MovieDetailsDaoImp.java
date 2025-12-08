package com.project.vistaro.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import com.project.vistaro.model.MovieDetails;

@Repository
public class MovieDetailsDaoImp implements MovieDetailsDao {
	
private final JdbcTemplate jdbcTemplate;
	
	public MovieDetailsDaoImp(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	 // RowMapper to convert ResultSet → MovieDetails object
    private final RowMapper<MovieDetails> movieRowMapper = new RowMapper<>() {
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
    };

    @Override
    public MovieDetails addMovieDetails(MovieDetails movieDetails) {

        String sql = "INSERT INTO MovieDetails (event_id, cast_json, director, genre, language, rating, total_reviews, trailer_url, release_date) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, movieDetails.getEventId());
            ps.setString(2, movieDetails.getCastJson());
            ps.setString(3, movieDetails.getDirector());
            ps.setString(4, movieDetails.getGenre());
            ps.setString(5, movieDetails.getLanguage());
            ps.setObject(6, movieDetails.getRating());
            ps.setObject(7, movieDetails.getTotalReviews());
            ps.setString(8, movieDetails.getTrailerUrl());
            ps.setObject(9, movieDetails.getReleaseDate());
            return ps;
        }, keyHolder);

        Number id = keyHolder.getKey();
        if (id != null) {
            movieDetails.setMovieDetailsId(id.intValue());
        }

        return movieDetails;
    }


	@Override
	public MovieDetails updateMovieDetails(MovieDetails movieDetails) {
		// TODO Auto-generated method stub
		 String sql = "UPDATE MovieDetails SET cast_json=?, director=?, genre=?, language=?, rating=?, total_reviews=?, trailer_url=?, release_date=? " +
                 "WHERE movie_details_id=?";
    jdbcTemplate.update(sql,
            movieDetails.getCastJson(),
            movieDetails.getDirector(),
            movieDetails.getGenre(),
            movieDetails.getLanguage(),
            movieDetails.getRating(),
            movieDetails.getTotalReviews(),
            movieDetails.getTrailerUrl(),
            movieDetails.getReleaseDate(),
            movieDetails.getMovieDetailsId()
    );
    return movieDetails;
	}

	@Override
	public void deleteMovieDetails(Integer id) {
		// TODO Auto-generated method stub
		 String sql = "DELETE FROM MovieDetails WHERE movie_details_id = ?";
	        jdbcTemplate.update(sql, id);
	}

	@Override
	public List<MovieDetails> listAllMovieDetails() {
		// TODO Auto-generated method stub
		 String sql = "SELECT * FROM MovieDetails";
	     return jdbcTemplate.query(sql, movieRowMapper);
	}

	@Override
	public Optional<MovieDetails> findById(Integer id) {
		// TODO Auto-generated method stub
		String sql = "SELECT * FROM MovieDetails WHERE movie_details_id = ?";
        return jdbcTemplate.query(sql, movieRowMapper, id).stream().findFirst();
	}

	@Override
	public List<MovieDetails> getByGenre(String genre) {
		// TODO Auto-generated method stub
		 String sql = "SELECT * FROM MovieDetails WHERE genre = ?";
	     return jdbcTemplate.query(sql, movieRowMapper, genre);
	}

	@Override
	public MovieDetails getByEventId(Integer eventId) {
		// TODO Auto-generated method stub
		String sql = "Select * FROM MovieDetails WHERE event_id=?";
		return jdbcTemplate.queryForObject(sql, movieRowMapper, eventId);
	}
    
	@Override
	public void updateRating(Integer eventId, Double newRating, Integer totalReviews) {
	    String sql = "UPDATE MovieDetails SET rating=?, total_reviews=? WHERE event_id=?";
	    jdbcTemplate.update(sql, newRating, totalReviews, eventId);
	}
   

}
