package com.project.vistaro.service;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
 
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;
 
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
 
import com.project.vistaro.exception.ResourceNotFoundException;
import com.project.vistaro.model.Event;
import com.project.vistaro.model.EventCategory;
import com.project.vistaro.repository.EventDao;
import java.util.*;
 
public class EventServiceImplTest {
	@Mock
    private EventDao dao;
 
    @InjectMocks
    private EventServiceImpl service;
 
    private Event event;
 
    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
 
        event = new Event();
        event.setEventId(1);
        event.setTitle("Test Event");
        event.setDescription("Testing event");
        event.setCategory(EventCategory.EVENT);
        event.setSubCategory("Music");
        event.setBannerUrl("banner.jpg");
        event.setThumbnailUrl("thumb.jpg");
        event.setStartTime(LocalDateTime.now());
        event.setEndTime(LocalDateTime.now().plusHours(2));
    }

 
    // ---------------- CREATE ----------------
    @Test
    void testCreateEvent() {
        when(dao.save(event)).thenReturn(event);
 
        Event saved = service.create(event);
        assertNotNull(saved);
        assertEquals("Test Event", saved.getTitle());
        verify(dao, times(1)).save(event);
    }
 
    // ---------------- FIND BY ID SUCCESS ----------------
    @Test
    void testFindByIdSuccess() {
        when(dao.findById(1)).thenReturn(Optional.of(event));
 
        Event found = service.findById(1);
        assertEquals(1, found.getEventId());
        verify(dao).findById(1);
    }
 
    // ---------------- FIND BY ID FAILED ----------------
    @Test
    void testFindByIdNotFound() {
        when(dao.findById(1)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.findById(1));
        verify(dao).findById(1);
    }
 
    // ---------------- FIND ALL BY CITY ----------------
    @Test
    void testFindAllByCity() {
        List<Event> list = Arrays.asList(event);
        when(dao.findAllByCity("Mumbai")).thenReturn(list);
 
        List<Event> result = service.findAllByCity("Mumbai");
        assertEquals(1, result.size());
        verify(dao).findAllByCity("Mumbai");
    }
 
    // ---------------- SEARCH BY TITLE ----------------
    @Test
    void testSearchByTitle() {
        when(dao.searchByTitleAndCity("test", "Mumbai"))
                .thenReturn(Arrays.asList(event));
 
        List<Event> result = service.searchByTitle("Mumbai", "test");
        assertEquals(1, result.size());
        verify(dao).searchByTitleAndCity("test", "Mumbai");
    }
 
    // ---------------- SEARCH BY CATEGORY ----------------
    @Test
    void testSearchByCategory() {
        when(dao.searchByCategoryAndCity("EVENT", "Mumbai"))
                .thenReturn(Arrays.asList(event));
 
        List<Event> result = service.searchByCategory("Mumbai", "EVENT");
        assertEquals(1, result.size());
        verify(dao).searchByCategoryAndCity("EVENT", "Mumbai");
    }
 
    // ---------------- SEARCH BY SUB CATEGORY ----------------
    @Test
    void testSearchBySubCategory() {
        when(dao.searchBySubCategoryAndCity("music", "Mumbai"))
                .thenReturn(Arrays.asList(event));
 
        List<Event> result = service.searchBySubCategory("Mumbai", "music");
        assertEquals(1, result.size());
        verify(dao).searchBySubCategoryAndCity("music", "Mumbai");
    }
 
    // ---------------- UPDATE EVENT ----------------
    @Test
    void testUpdateEvent() {
        Event updatedInput = new Event();
        updatedInput.setTitle("Updated Event");
 
        when(dao.findById(1)).thenReturn(Optional.of(event));
        when(dao.update(any(Event.class))).thenReturn(event);
 
        Event updated = service.update(1, updatedInput);
 
        assertEquals("Updated Event", updated.getTitle());
        verify(dao).findById(1);
        verify(dao).update(any(Event.class));
    }
 
    // ---------------- DELETE SUCCESS ----------------
    @Test
    void testDeleteEventSuccess() {
        Event event = new Event();
        event.setEventId(1);
        // Make sure the event is already finished (end time in the past)
        event.setStartTime(LocalDateTime.now().minusDays(2));
        event.setEndTime(LocalDateTime.now().minusDays(1));
        when(dao.findById(1)).thenReturn(Optional.of(event));
        service.deleteEvent(1);
        verify(dao).findById(1);
        verify(dao).delete(1);
    }
 
    // ---------------- DELETE NOT FOUND ----------------
    @Test
    void testDeleteEventNotFound() {
        when(dao.findById(1)).thenReturn(Optional.empty());
 
        assertThrows(ResourceNotFoundException.class, () -> service.deleteEvent(1));
 
        verify(dao).findById(1);
        verify(dao, never()).delete(anyInt());
    }
 
}