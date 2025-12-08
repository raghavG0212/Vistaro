package com.project.vistaro.service;

import com.project.vistaro.model.GeneralEventDetails;
import com.project.vistaro.repository.GeneralEventDetailsDao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class GeneralEventDetailsServiceImplTest {

    private GeneralEventDetailsDao dao;
    private GeneralEventDetailsServiceImpl service;

    @BeforeEach
    void setUp() {
        dao = Mockito.mock(GeneralEventDetailsDao.class);
        service = new GeneralEventDetailsServiceImpl(dao);
    }

    private GeneralEventDetails createEntity() {
        GeneralEventDetails e = new GeneralEventDetails();
        e.setGeneralDetailsId(1);
        e.setEventId(500);
        e.setArtist("Arijit Singh");
        e.setHost("Star Events");
        e.setGenre("Music");
        e.setStartTime(LocalDateTime.of(2025, 3, 5, 18, 0));
        e.setEndTime(LocalDateTime.of(2025, 3, 5, 22, 0));
        e.setAdditionalInfo("Live concert");
        return e;
    }

    // -------------------------- TEST: ADD --------------------------
    @Test
    void testAddEventDetails() {
        GeneralEventDetails entity = createEntity();

        when(dao.save(entity)).thenReturn(entity);

        GeneralEventDetails result = service.addEventDetails(entity);

        assertNotNull(result);
        assertEquals(1, result.getGeneralDetailsId());
        verify(dao, times(1)).save(entity);
    }

    // ---------------------- TEST: GET BY ID ------------------------
    @Test
    void testGetDetailsById() {
        GeneralEventDetails entity = createEntity();

        when(dao.findById(1)).thenReturn(entity);

        GeneralEventDetails result = service.getDetailsById(1);

        assertEquals(1, result.getGeneralDetailsId());
        verify(dao).findById(1);
    }

    // ------------------- TEST: GET BY EVENT ID ---------------------
    @Test
    void testGetDetailsByEventId() {
        GeneralEventDetails entity = createEntity();

        when(dao.findByEventId(500)).thenReturn(entity);

        GeneralEventDetails result = service.getDetailsByEventId(500);

        assertEquals(500, result.getEventId());
        verify(dao).findByEventId(500);
    }

    // ----------------------- TEST: GET ALL -------------------------
    @Test
    void testGetAllDetails() {
        when(dao.findAll()).thenReturn(List.of(createEntity()));

        List<GeneralEventDetails> list = service.getAllDetails();

        assertEquals(1, list.size());
        verify(dao).findAll();
    }

    // ----------------------- TEST: DELETE --------------------------
    @Test
    void testDeleteDetails() {
        service.deleteDetails(1);

        verify(dao, times(1)).delete(1);
    }

    // ------------------------ TEST: UPDATE -------------------------
    @Test
    void testUpdate() {

        GeneralEventDetails updated = createEntity();

        when(dao.update(eq(1), any(GeneralEventDetails.class))).thenReturn(updated);

        GeneralEventDetails result = service.update(1, updated);

        assertEquals(1, result.getGeneralDetailsId());
        verify(dao).update(eq(1), any(GeneralEventDetails.class));
    }
}
