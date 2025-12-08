package com.project.vistaro.repository;

import com.project.vistaro.model.GeneralEventDetails;
import com.project.vistaro.util.GeneralEventDetailsRowMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.KeyHolder;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class GeneralEventDetailsDaoImplTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private GeneralEventDetailsDaoImpl dao;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
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

    // ---------------------------- TEST: SAVE ----------------------------
    @Test
    void testSave() {

        GeneralEventDetails entity = createEntity();

        // Mock KeyHolder behavior to set generated ID = 1
        doAnswer(invocation -> {
            KeyHolder keyHolder = invocation.getArgument(1);
            keyHolder.getKeyList().add(java.util.Map.of("GENERATED_KEY", 1));
            return 1;
        }).when(jdbcTemplate).update(any(), any(KeyHolder.class));

        GeneralEventDetails result = dao.save(entity);

        assertEquals(1, result.getGeneralDetailsId());
        verify(jdbcTemplate, times(1)).update(any(), any(KeyHolder.class));
    }

    // ----------------------- TEST: FIND BY ID -----------------------
    @Test
    void testFindById() {

        when(jdbcTemplate.queryForObject(anyString(),
                any(GeneralEventDetailsRowMapper.class),
                eq(1)))
                .thenReturn(createEntity());

        GeneralEventDetails result = dao.findById(1);

        assertEquals(1, result.getGeneralDetailsId());
        assertEquals("Arijit Singh", result.getArtist());
    }

    // ----------------------- TEST: FIND BY EVENT ID -----------------------
    @Test
    void testFindByEventId() {

        when(jdbcTemplate.queryForObject(anyString(),
                any(GeneralEventDetailsRowMapper.class),
                eq(500)))
                .thenReturn(createEntity());

        GeneralEventDetails result = dao.findByEventId(500);

        assertEquals(500, result.getEventId());
    }

    // ----------------------- TEST: FIND ALL -----------------------
    @Test
    void testFindAll() {

        when(jdbcTemplate.query(anyString(),
                ArgumentMatchers.<GeneralEventDetailsRowMapper>any()))
                .thenReturn(List.of(createEntity()));

        List<GeneralEventDetails> list = dao.findAll();

        assertEquals(1, list.size());
        assertEquals("Music", list.get(0).getGenre());
    }

    // ----------------------- TEST: DELETE -----------------------
    @Test
    void testDelete() {

        dao.delete(1);

        verify(jdbcTemplate, times(1))
                .update(eq("DELETE FROM generaleventdetails WHERE general_details_id=?"), eq(1));
    }

    // ----------------------- TEST: UPDATE -----------------------
    @Test
    void testUpdate() {

        GeneralEventDetails entity = createEntity();

        when(jdbcTemplate.update(anyString(),
                any(), any(), any(), any(), any(), any(), eq(1)))
                .thenReturn(1);

        GeneralEventDetails updated = dao.update(1, entity);

        assertEquals(1, updated.getGeneralDetailsId());
        verify(jdbcTemplate, times(1))
                .update(anyString(),
                        any(), any(), any(),
                        any(), any(), any(),
                        eq(1));
    }
}
