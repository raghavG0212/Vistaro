package com.project.vistaro.repository;

import com.project.vistaro.model.SportDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.KeyHolder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class SportDetailsDaoImpTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private SportDetailsDaoImp sportDao;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    private SportDetails createSport() {
        SportDetails s = new SportDetails();
        s.setSportDetailsId(1);
        s.setEventId(200);
        s.setSportType("Cricket");
        s.setTeam1("India");
        s.setTeam2("Australia");
        s.setMatchFormat("ODI");
        s.setVenueInfo("Wankhede Stadium");
        s.setStartTime(LocalDateTime.of(2025, 1, 20, 10, 0));
        s.setEndTime(LocalDateTime.of(2025, 1, 20, 17, 0));
        return s;
    }

    // -------------------- TEST: ADD SPORT --------------------
    @Test
    void testAddSportDetails() {

        SportDetails sport = createSport();

        doAnswer(invocation -> {
            KeyHolder keyHolder = invocation.getArgument(1);
            keyHolder.getKeyList().add(java.util.Map.of("GENERATED_KEY", 1));
            return 1;
        }).when(jdbcTemplate).update(any(), any(KeyHolder.class));

        SportDetails result = sportDao.addSportDetails(sport);

        assertEquals(1, result.getSportDetailsId());
        verify(jdbcTemplate, times(1)).update(any(), any(KeyHolder.class));
    }

    // -------------------- TEST: UPDATE SPORT --------------------
    @Test
    void testUpdateSportDetails() {
        SportDetails sport = createSport();

        when(jdbcTemplate.update(anyString(),
                any(), any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(1);

        SportDetails result = sportDao.updateSportDetails(sport);

        assertEquals(1, result.getSportDetailsId());
        verify(jdbcTemplate, times(1)).update(anyString(),
                any(), any(), any(), any(), any(), any(), any(), any());
    }

    // -------------------- TEST: DELETE SPORT --------------------
    @Test
    void testDeleteSportDetails() {

        sportDao.deleteSportDetails(1);

        verify(jdbcTemplate, times(1))
                .update("DELETE FROM SportDetails WHERE sport_details_id = ?", 1);
    }

    // -------------------- TEST: LIST ALL --------------------
    @Test
    void testListAllSportDetails() {

        when(jdbcTemplate.query(anyString(),
                ArgumentMatchers.<RowMapper<SportDetails>>any()))
                .thenReturn(List.of(createSport()));

        List<SportDetails> list = sportDao.listAllSportDetails();

        assertEquals(1, list.size());
        assertEquals("Cricket", list.get(0).getSportType());
    }

    // -------------------- TEST: FIND BY ID (FOUND) --------------------
    @Test
    void testFindByIdSuccess() {

        when(jdbcTemplate.query(anyString(),
                ArgumentMatchers.<RowMapper<SportDetails>>any(),
                eq(1)))
                .thenReturn(List.of(createSport()));

        Optional<SportDetails> result = sportDao.findById(1);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getSportDetailsId());
    }

    // -------------------- TEST: FIND BY ID (NOT FOUND) --------------------
    @Test
    void testFindByIdNotFound() {

        when(jdbcTemplate.query(anyString(),
                ArgumentMatchers.<RowMapper<SportDetails>>any(),
                eq(1)))
                .thenReturn(List.of());

        Optional<SportDetails> result = sportDao.findById(1);

        assertTrue(result.isEmpty());
    }

    // -------------------- TEST: GET BY SPORT TYPE --------------------
    @Test
    void testGetBySportType() {

        when(jdbcTemplate.query(anyString(),
                ArgumentMatchers.<RowMapper<SportDetails>>any(),
                eq("Cricket")))
                .thenReturn(List.of(createSport()));

        List<SportDetails> result = sportDao.getBySportType("Cricket");

        assertEquals(1, result.size());
        assertEquals("Cricket", result.get(0).getSportType());
    }

    // -------------------- TEST: FIND BY EVENT ID --------------------
    @Test
    void testFindByEventId() {

        when(jdbcTemplate.queryForObject(anyString(),
                ArgumentMatchers.<RowMapper<SportDetails>>any(),
                eq(200)))
                .thenReturn(createSport());

        SportDetails result = sportDao.findByEventId(200);

        assertEquals(200, result.getEventId());
        verify(jdbcTemplate).queryForObject(anyString(),
                ArgumentMatchers.<RowMapper<SportDetails>>any(),
                eq(200));
    }
}
