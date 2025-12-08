package com.project.vistaro.repository;

import com.project.vistaro.model.MovieDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.KeyHolder;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class MovieDetailsDaoImplTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private MovieDetailsDaoImp movieDetailsDao;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
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

    // ------------------- TEST: ADD MOVIE -------------------
    @Test
    void testAddMovieDetails() {

        MovieDetails movie = createMovie();

        // Mock KeyHolder behavior
        doAnswer(invocation -> {
            KeyHolder keyHolder = invocation.getArgument(1);
            keyHolder.getKeyList().add(java.util.Map.of("GENERATED_KEY", 1));
            return 1;
        }).when(jdbcTemplate).update(any(), any(KeyHolder.class));

        MovieDetails result = movieDetailsDao.addMovieDetails(movie);

        assertEquals(1, result.getMovieDetailsId());
        verify(jdbcTemplate, times(1)).update(any(), any(KeyHolder.class));
    }

    // ------------------- TEST: UPDATE MOVIE -------------------
    @Test
    void testUpdateMovieDetails() {
        MovieDetails movie = createMovie();

        when(jdbcTemplate.update(anyString(),
                any(), any(), any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(1);

        MovieDetails updated = movieDetailsDao.updateMovieDetails(movie);

        assertEquals(1, updated.getMovieDetailsId());
        verify(jdbcTemplate, times(1)).update(anyString(),
                any(), any(), any(), any(), any(), any(), any(), any(), any());
    }

    // ------------------- TEST: DELETE MOVIE -------------------
    @Test
    void testDeleteMovieDetails() {

        movieDetailsDao.deleteMovieDetails(1);

        verify(jdbcTemplate, times(1))
                .update(eq("DELETE FROM MovieDetails WHERE movie_details_id = ?"), eq(1));
    }

    // ------------------- TEST: LIST ALL MOVIES -------------------
    @Test
    void testListAllMovieDetails() {

        when(jdbcTemplate.query(anyString(), ArgumentMatchers.<RowMapper<MovieDetails>>any()))
                .thenReturn(List.of(createMovie()));

        List<MovieDetails> list = movieDetailsDao.listAllMovieDetails();

        assertEquals(1, list.size());
    }

    // ------------------- TEST: FIND BY ID -------------------
    @Test
    void testFindByIdSuccess() {

        when(jdbcTemplate.query(anyString(),
                ArgumentMatchers.<RowMapper<MovieDetails>>any(),
                eq(1)))
                .thenReturn(List.of(createMovie()));

        Optional<MovieDetails> result = movieDetailsDao.findById(1);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getMovieDetailsId());
    }

    @Test
    void testFindByIdNotFound() {

        when(jdbcTemplate.query(anyString(),
                ArgumentMatchers.<RowMapper<MovieDetails>>any(),
                eq(1)))
                .thenReturn(List.of());

        Optional<MovieDetails> result = movieDetailsDao.findById(1);

        assertTrue(result.isEmpty());
    }

    // ------------------- TEST: GET BY GENRE -------------------
    @Test
    void testGetByGenre() {

        when(jdbcTemplate.query(anyString(),
                ArgumentMatchers.<RowMapper<MovieDetails>>any(),
                eq("Action")))
                .thenReturn(List.of(createMovie()));

        List<MovieDetails> result = movieDetailsDao.getByGenre("Action");

        assertEquals(1, result.size());
        assertEquals("Action", result.get(0).getGenre());
    }

    // ------------------- TEST: GET BY EVENT ID -------------------
    @Test
    void testGetByEventId() {

        when(jdbcTemplate.queryForObject(anyString(),
                ArgumentMatchers.<RowMapper<MovieDetails>>any(),
                eq(101)))
                .thenReturn(createMovie());

        MovieDetails result = movieDetailsDao.getByEventId(101);

        assertEquals(101, result.getEventId());
        verify(jdbcTemplate).queryForObject(anyString(),
                ArgumentMatchers.<RowMapper<MovieDetails>>any(),
                eq(101));
    }
}
