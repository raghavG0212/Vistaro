package com.project.vistaro.service;

import com.project.vistaro.exception.ResourceNotFoundException;
import com.project.vistaro.model.MovieDetails;
import com.project.vistaro.repository.MovieDetailsDao;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class MovieDetailsServiceImplTest {

    private MovieDetailsDao movieDetailsDao;
    private MovieDetailsServiceImpl movieDetailsService;

    @BeforeEach
    void setUp() {
        movieDetailsDao = Mockito.mock(MovieDetailsDao.class);
        movieDetailsService = new MovieDetailsServiceImpl(movieDetailsDao);
    }

    private MovieDetails createMovie() {
        MovieDetails m = new MovieDetails();
        m.setMovieDetailsId(1);
        m.setEventId(101);
        m.setCastJson("{\"actor\":\"John\"}");
        m.setDirector("Director1");
        m.setGenre("Action");
        m.setLanguage("English");
        m.setRating(9.0);
        m.setTotalReviews(1500);
        m.setTrailerUrl("http://youtube.com");
        m.setReleaseDate(LocalDate.of(2025, 1, 1));
        return m;
    }

    // --------------------- TEST addMovieDetails ---------------------
    @Test
    void testAddMovieDetails() {
        MovieDetails movie = createMovie();

        when(movieDetailsDao.addMovieDetails(any(MovieDetails.class)))
                .thenReturn(movie);

        MovieDetails result = movieDetailsService.addMovieDetails(movie);

        Assertions.assertEquals(1, result.getMovieDetailsId());
        verify(movieDetailsDao, times(1)).addMovieDetails(movie);
    }

    // --------------------- TEST updateMovieDetails ---------------------
    @Test
    void testUpdateMovieDetails() {
        MovieDetails movie = createMovie();

        when(movieDetailsDao.updateMovieDetails(any(MovieDetails.class)))
                .thenReturn(movie);

        MovieDetails result = movieDetailsService.updateMovieDetails(movie);

        Assertions.assertEquals(1, result.getMovieDetailsId());
        verify(movieDetailsDao).updateMovieDetails(movie);
    }

    // --------------------- TEST deleteMovieDetails ---------------------
    @Test
    void testDeleteMovieDetails() {
        movieDetailsService.deleteMovieDetails(1);

        verify(movieDetailsDao, times(1)).deleteMovieDetails(1);
    }

    // --------------------- TEST listAllMovieDetails ---------------------
    @Test
    void testListAllMovieDetails() {
        when(movieDetailsDao.listAllMovieDetails())
                .thenReturn(List.of(createMovie()));

        List<MovieDetails> list = movieDetailsService.listAllMovieDetails();

        Assertions.assertEquals(1, list.size());
        verify(movieDetailsDao).listAllMovieDetails();
    }

    // --------------------- TEST findById (Success) ---------------------
    @Test
    void testFindByIdSuccess() {
        MovieDetails movie = createMovie();

        when(movieDetailsDao.findById(1))
                .thenReturn(Optional.of(movie));

        MovieDetails result = movieDetailsService.findById(1);

        Assertions.assertEquals(1, result.getMovieDetailsId());
        verify(movieDetailsDao).findById(1);
    }

    // --------------------- TEST findById (Not Found) ---------------------
    @Test
    void testFindByIdNotFound() {
        when(movieDetailsDao.findById(1))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> movieDetailsService.findById(1));

        verify(movieDetailsDao).findById(1);
    }

    // --------------------- TEST getByGenre ---------------------
    @Test
    void testGetByGenre() {
        when(movieDetailsDao.getByGenre("Action"))
                .thenReturn(List.of(createMovie()));

        List<MovieDetails> list = movieDetailsService.getByGenre("Action");

        Assertions.assertEquals(1, list.size());
        verify(movieDetailsDao).getByGenre("Action");
    }

    // --------------------- TEST findByEventId ---------------------
    @Test
    void testFindByEventId() {
        MovieDetails movie = createMovie();

        when(movieDetailsDao.getByEventId(101))
                .thenReturn(movie);

        MovieDetails result = movieDetailsService.findByEventId(101);

        Assertions.assertEquals(101, result.getEventId());
        verify(movieDetailsDao).getByEventId(101);
    }
}
