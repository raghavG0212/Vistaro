package com.project.vistaro.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.vistaro.dto.MovieDetailsDto;
import com.project.vistaro.model.MovieDetails;
import com.project.vistaro.service.MovieDetailsService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MovieDetailsController.class)
@AutoConfigureMockMvc(addFilters = false)
class MovieDetailsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MovieDetailsService movieService;

    @Autowired
    private ObjectMapper mapper;

    // Helper to create test movie entity
    private MovieDetails createMovie() {
        MovieDetails m = new MovieDetails();
        m.setMovieDetailsId(1);
        m.setEventId(101);
        m.setCastJson("{\"actor\":\"John\"}");
        m.setDirector("Steven");
        m.setGenre("Action");
        m.setLanguage("English");
        m.setRating(8.5);
        m.setTotalReviews(250);
        m.setTrailerUrl("http://youtube.com/test");
        m.setReleaseDate(LocalDate.of(2025, 1, 1));
        return m;
    }

    private MovieDetailsDto createMovieDto() {
        MovieDetailsDto dto = new MovieDetailsDto();
        dto.setMovieDetailsId(1);
        dto.setEventId(101);
        dto.setCastJson(new Object() {
            public String actor = "John";
        });
        dto.setDirector("Steven");
        dto.setGenre("Action");
        dto.setLanguage("English");
        dto.setRating(8.5);
        dto.setTotalReviews(250);
        dto.setTrailerUrl("http://youtube.com/test");
        dto.setReleaseDate(LocalDate.of(2025, 1, 1));
        return dto;
    }

    // ------------------- TEST: CREATE --------------------
    @Test
    void testCreateMovie() throws Exception {
        MovieDetails movie = createMovie();
        MovieDetailsDto requestDto = createMovieDto();

        Mockito.when(movieService.addMovieDetails(any(MovieDetails.class)))
                .thenReturn(movie);

        mockMvc.perform(post("/api/v1/movie")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.movieDetailsId").value(1))
                .andExpect(jsonPath("$.director").value("Steven"));
    }

    // ------------------- TEST: UPDATE --------------------
    @Test
    void testUpdateMovie() throws Exception {
        MovieDetails updated = createMovie();
        MovieDetailsDto dto = createMovieDto();

        Mockito.when(movieService.updateMovieDetails(any(MovieDetails.class)))
                .thenReturn(updated);

        mockMvc.perform(put("/api/v1/movie/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.movieDetailsId").value(1));
    }

    // ------------------- TEST: GET BY ID --------------------
    @Test
    void testGetById() throws Exception {
        MovieDetails movie = createMovie();
        Mockito.when(movieService.findById(1)).thenReturn(movie);

        mockMvc.perform(get("/api/v1/movie/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.director").value("Steven"))
                .andExpect(jsonPath("$.genre").value("Action"));
    }

    // ------------------- TEST: GET BY EVENT --------------------
    @Test
    void testGetByEvent() throws Exception {
        MovieDetails movie = createMovie();
        Mockito.when(movieService.findByEventId(101)).thenReturn(movie);

        mockMvc.perform(get("/api/v1/movie/event/101"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventId").value(101));
    }

    // ------------------- TEST: GET ALL --------------------
    @Test
    void testGetAllMovies() throws Exception {
        Mockito.when(movieService.listAllMovieDetails())
                .thenReturn(List.of(createMovie()));

        mockMvc.perform(get("/api/v1/movie"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].movieDetailsId").value(1));
    }

    // ------------------- TEST: GET BY GENRE --------------------
    @Test
    void testGetByGenre() throws Exception {
        Mockito.when(movieService.getByGenre("Action"))
                .thenReturn(List.of(createMovie()));

        mockMvc.perform(get("/api/v1/movie/genre/Action"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].genre").value("Action"));
    }

    // ------------------- TEST: DELETE --------------------
    @Test
    void testDelete() throws Exception {
        mockMvc.perform(delete("/api/v1/movie/1"))
                .andExpect(status().isOk());
    }
}

