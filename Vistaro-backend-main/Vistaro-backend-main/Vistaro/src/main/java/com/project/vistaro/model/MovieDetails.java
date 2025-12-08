package com.project.vistaro.model;

import java.time.LocalDate;

import io.micrometer.observation.Observation.Event;

public class MovieDetails {
	
	private Integer movieDetailsId;
    private Integer eventId;
    private String castJson;
    private String director;
    private String genre;
    private String language;
    private Double rating;
    private Integer totalReviews;
    private String trailerUrl;
    private LocalDate releaseDate;
    
    public MovieDetails() {}

	public MovieDetails(Integer movieDetailsId, Integer eventId, String castJson, String director, String genre,
			String language, Double rating, Integer totalReviews, String trailerUrl, LocalDate releaseDate) {
		super();
		this.movieDetailsId = movieDetailsId;
		this.eventId = eventId;
		this.castJson = castJson;
		this.director = director;
		this.genre = genre;
		this.language = language;
		this.rating = rating;
		this.totalReviews = totalReviews;
		this.trailerUrl = trailerUrl;
		this.releaseDate = releaseDate;
	}

	public Integer getMovieDetailsId() {
		return movieDetailsId;
	}

	public void setMovieDetailsId(Integer movieDetailsId) {
		this.movieDetailsId = movieDetailsId;
	}

	public Integer getEventId() {
		return eventId;
	}

	public void setEventId(Integer eventId) {
		this.eventId = eventId;
	}

	public String getCastJson() {
		return castJson;
	}

	public void setCastJson(String castJson) {
		this.castJson = castJson;
	}

	public String getDirector() {
		return director;
	}

	public void setDirector(String director) {
		this.director = director;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public Double getRating() {
		return rating;
	}

	public void setRating(Double rating) {
		this.rating = rating;
	}

	public Integer getTotalReviews() {
		return totalReviews;
	}

	public void setTotalReviews(Integer totalReviews) {
		this.totalReviews = totalReviews;
	}

	public String getTrailerUrl() {
		return trailerUrl;
	}

	public void setTrailerUrl(String trailerUrl) {
		this.trailerUrl = trailerUrl;
	}

	public LocalDate getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(LocalDate releaseDate) {
		this.releaseDate = releaseDate;
	}


}
