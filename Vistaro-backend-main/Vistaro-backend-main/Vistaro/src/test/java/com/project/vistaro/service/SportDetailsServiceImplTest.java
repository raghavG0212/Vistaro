package com.project.vistaro.service;

import com.project.vistaro.exception.ResourceNotFoundException;
import com.project.vistaro.model.SportDetails;
import com.project.vistaro.repository.SportDetailsDao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class SportDetailsServiceImplTest {

    private SportDetailsDao sportDao;
    private SportDetailsServiceImpl sportService;

    @BeforeEach
    void setUp() {
        sportDao = Mockito.mock(SportDetailsDao.class);
        sportService = new SportDetailsServiceImpl(sportDao);
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

    // ------------------------------ ADD ------------------------------
    @Test
    void testAddSportDetails() {
        SportDetails sport = createSport();

        when(sportDao.addSportDetails(any(SportDetails.class)))
                .thenReturn(sport);

        SportDetails result = sportService.addSportDetails(sport);

        assertEquals(1, result.getSportDetailsId());
        verify(sportDao, times(1)).addSportDetails(sport);
    }

    // ---------------------------- UPDATE ----------------------------
    @Test
    void testUpdateSportDetails() {
        SportDetails sport = createSport();

        when(sportDao.updateSportDetails(any(SportDetails.class)))
                .thenReturn(sport);

        SportDetails result = sportService.updateSportDetails(sport);

        assertEquals("Cricket", result.getSportType());
        verify(sportDao, times(1)).updateSportDetails(sport);
    }

    // ---------------------------- DELETE ----------------------------
    @Test
    void testDeleteSportDetails() {
        sportService.deleteSportDetails(1);

        verify(sportDao, times(1)).deleteSportDetails(1);
    }

    // ----------------------------- LIST -----------------------------
    @Test
    void testListAllSportDetails() {
        when(sportDao.listAllSportDetails())
                .thenReturn(List.of(createSport()));

        List<SportDetails> result = sportService.listAllSportDetails();

        assertEquals(1, result.size());
        verify(sportDao).listAllSportDetails();
    }

    // ----------------------- FIND BY ID (SUCCESS) -----------------------
    @Test
    void testFindByIdSuccess() {
        SportDetails sport = createSport();

        when(sportDao.findById(1))
                .thenReturn(Optional.of(sport));

        SportDetails result = sportService.findById(1);

        assertNotNull(result);
        assertEquals(1, result.getSportDetailsId());
        verify(sportDao).findById(1);
    }

    // ----------------------- FIND BY ID (NOT FOUND) -----------------------
    @Test
    void testFindByIdNotFound() {
        when(sportDao.findById(1))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> sportService.findById(1));

        verify(sportDao).findById(1);
    }

    // ----------------------------- GET BY TYPE -----------------------------
    @Test
    void testGetBySportType() {
        when(sportDao.getBySportType("Cricket"))
                .thenReturn(List.of(createSport()));

        List<SportDetails> result = sportService.getBySportType("Cricket");

        assertEquals(1, result.size());
        assertEquals("Cricket", result.get(0).getSportType());
        verify(sportDao).getBySportType("Cricket");
    }

    // ----------------------------- FIND BY EVENT ID -----------------------------
    @Test
    void testFindByEventId() {
        SportDetails sport = createSport();

        when(sportDao.findByEventId(200))
                .thenReturn(sport);

        SportDetails result = sportService.findByEventId(200);

        assertEquals(200, result.getEventId());
        verify(sportDao).findByEventId(200);
    }
}
