package com.project.vistaro.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class MovieDetailsDto {

	private Integer movieDetailsId; // optional, usually for responses

    private Integer eventId; // link to Event

    @NotNull(message = "Cast information is required")
    private Object castJson; // JSON string of cast list

    @Size(max = 50, message = "Director name must be less than 50 characters")
    private String director;

    @Size(max = 150, message = "Genre must be less than 150 characters")
    private String genre;

    @Size(max = 150, message = "Language must be less than 150 characters")
    private String language;

    @Min(value = 0, message = "Rating cannot be negative")
    @Max(value = 10, message = "Rating cannot exceed 10")
    private Double rating;
    
    @Min(value = 0, message = "Total reviews cannot be negative")
    private Integer totalReviews;

    private String trailerUrl;

   // private LocalDate releaseDate;
    private LocalDate releaseDate;
    
    public MovieDetailsDto() {}

	public MovieDetailsDto(Integer movieDetailsId, Integer eventId,
			@NotBlank(message = "Cast information is required") Object castJson,
			@Size(max = 50, message = "Director name must be less than 50 characters") String director,
			@Size(max = 150, message = "Genre must be less than 150 characters") String genre,
			@Size(max = 150, message = "Language must be less than 150 characters") String language,
			@Min(value = 0, message = "Rating cannot be negative") @Max(value = 10, message = "Rating cannot exceed 10") Double rating,
			@Min(value = 0, message = "Total reviews cannot be negative") Integer totalReviews, String trailerUrl,
			LocalDate releaseDate) {
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

	public Object getCastJson() {
		return castJson;
	}

	public void setCastJson(Object castJson) {
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
