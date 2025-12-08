package com.project.vistaro.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.KeyHolder;

import com.project.vistaro.model.Event;
import com.project.vistaro.model.EventCategory;
import com.project.vistaro.util.EventRowMapper;

public class EventDaoImplTest {
	@Mock
    private JdbcTemplate jdbc;

    @InjectMocks
    private EventDaoImpl dao;

    private Event event;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        event = new Event();
        event.setEventId(1);
        event.setTitle("Test Event");
        event.setDescription("Testing DAO");
        event.setCategory(EventCategory.EVENT);
        event.setSubCategory("Music");
        event.setBannerUrl("banner.jpg");
        event.setThumbnailUrl("thumb.jpg");
        event.setStartTime(LocalDateTime.now());
        event.setEndTime(LocalDateTime.now().plusHours(2));
    }

    // -------------------- TEST SAVE --------------------
    @Test
    void testSaveEvent() {
        doAnswer(invocation -> {
            KeyHolder kh = invocation.getArgument(1);
            kh.getKeyList().add(Collections.singletonMap("GENERATED_KEY", 1));
            return null;
        }).when(jdbc).update(any(), any(KeyHolder.class));

        Event saved = dao.save(event);

        assertNotNull(saved);
        assertEquals(1, saved.getEventId());
    }

    // -------------------- TEST FIND BY ID --------------------
    @Test
    void testFindByIdSuccess() {
        when(jdbc.query(anyString(), any(EventRowMapper.class), eq(1)))
                .thenReturn(Arrays.asList(event));

        Optional<Event> result = dao.findById(1);

        assertTrue(result.isPresent());
        assertEquals("Test Event", result.get().getTitle());
    }

    @Test
    void testFindByIdNotFound() {
        when(jdbc.query(anyString(), any(EventRowMapper.class), eq(1)))
                .thenReturn(Arrays.asList());

        Optional<Event> result = dao.findById(1);

        assertFalse(result.isPresent());
    }

    // -------------------- TEST findAllByCity --------------------
    @Test
    void testFindAllByCity() {
        when(jdbc.query(anyString(), any(EventRowMapper.class), eq("Mumbai")))
                .thenReturn(Arrays.asList(event));

        List<Event> list = dao.findAllByCity("Mumbai");

        assertEquals(1, list.size());
        assertEquals("Test Event", list.get(0).getTitle());
    }

    // -------------------- TEST searchByTitleAndCity --------------------
    @Test
    void testSearchByTitleAndCity() {
        when(jdbc.query(anyString(), any(EventRowMapper.class), eq("Mumbai"), anyString()))
                .thenReturn(Arrays.asList(event));

        List<Event> list = dao.searchByTitleAndCity("Test", "Mumbai");

        assertEquals(1, list.size());
    }

    // -------------------- TEST searchByCategoryAndCity --------------------
    @Test
    void testSearchByCategoryAndCity() {
        when(jdbc.query(anyString(), any(EventRowMapper.class), eq("Mumbai"), eq("EVENT")))
                .thenReturn(Arrays.asList(event));

        List<Event> list = dao.searchByCategoryAndCity("EVENT", "Mumbai");

        assertEquals(1, list.size());
    }

    // -------------------- TEST searchBySubCategoryAndCity --------------------
    @Test
    void testSearchBySubCategoryAndCity() {
        when(jdbc.query(anyString(), any(EventRowMapper.class), eq("Mumbai"), anyString()))
                .thenReturn(Arrays.asList(event));

        List<Event> list = dao.searchBySubCategoryAndCity("Music", "Mumbai");

        assertEquals(1, list.size());
    }

    // -------------------- TEST UPDATE --------------------
    @Test
    void testUpdateEvent() {
        dao.update(event);

        verify(jdbc, times(1)).update(
                anyString(),
                eq(event.getTitle()),
                eq(event.getDescription()),
                eq(event.getCategory().name()),
                eq(event.getSubCategory()),
                eq(event.getBannerUrl()),
                eq(event.getThumbnailUrl()),
                any(Timestamp.class),
                any(Timestamp.class),
                eq(event.getEventId())
        );
    }

    // -------------------- TEST DELETE --------------------
    @Test
    void testDeleteEvent() {
        dao.delete(1);

        verify(jdbc, times(1)).update("Delete from event where event_id=?", 1);
    }
}
