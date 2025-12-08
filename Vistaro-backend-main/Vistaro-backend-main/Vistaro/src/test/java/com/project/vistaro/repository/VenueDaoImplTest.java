package com.project.vistaro.repository;

import com.project.vistaro.model.Venue;
import com.project.vistaro.model.VenueType;
import com.project.vistaro.util.VenueRowMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.KeyHolder;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class VenueDaoImplTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private VenueDaoImpl venueDao;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    // ------------------------------------------
    // Helper method
    // ------------------------------------------
    private Venue createVenue() {
        Venue v = new Venue();
        v.setVenueId(1);
        v.setName("PVR Cinema");
        v.setAddress("MG Road");
        v.setCity("Mumbai");
        v.setVenueType(VenueType.CINEMA);
        v.setScreenName("Screen 1");
        v.setSeatLayoutJson("{\"rows\":10}");
        return v;
    }

    // ----------------------------------------------------
    // TEST: save()
    // ----------------------------------------------------
    @Test
    void testSave() {

        Venue venue = createVenue();

        when(jdbcTemplate.update(any(), any(KeyHolder.class)))
                .thenAnswer(inv -> {
                    KeyHolder kh = inv.getArgument(1);

                    // KeyHolder stores keys in keyList: List<Map<String, Object>>
                    var keyListField = kh.getClass().getDeclaredField("keyList");
                    keyListField.setAccessible(true);

                    List<Map<String, Object>> keyList = new java.util.ArrayList<>();
                    keyList.add(Map.of("GENERATED_KEY", 1));  // Fake generated ID

                    keyListField.set(kh, keyList);

                    return 1;
                });

        Venue saved = venueDao.save(venue);

        assertEquals(1, saved.getVenueId());
        verify(jdbcTemplate, times(1)).update(any(), any(KeyHolder.class));
    }


    // ----------------------------------------------------
    // TEST: findById() - Found
    // ----------------------------------------------------
    @Test
    void testFindByIdFound() {

        when(jdbcTemplate.query(anyString(), any(VenueRowMapper.class), eq(1)))
                .thenReturn(List.of(createVenue()));

        Optional<Venue> result = venueDao.findById(1);

        assertTrue(result.isPresent());
        assertEquals("PVR Cinema", result.get().getName());
    }

    // ----------------------------------------------------
    // TEST: findById() - Not Found
    // ----------------------------------------------------
    @Test
    void testFindByIdNotFound() {

        when(jdbcTemplate.query(anyString(), any(VenueRowMapper.class), eq(1)))
                .thenReturn(List.of());

        Optional<Venue> result = venueDao.findById(1);

        assertTrue(result.isEmpty());
    }

    // ----------------------------------------------------
    // TEST: findAll()
    // ----------------------------------------------------
    @Test
    void testFindAll() {

        when(jdbcTemplate.query(anyString(), any(VenueRowMapper.class)))
                .thenReturn(List.of(createVenue()));

        List<Venue> result = venueDao.findAll();

        assertEquals(1, result.size());
        assertEquals("PVR Cinema", result.get(0).getName());
    }

    // ----------------------------------------------------
    // TEST: update()
    // ----------------------------------------------------
    @Test
    void testUpdate() {

        Venue venue = createVenue();

        when(jdbcTemplate.update(anyString(),
                eq(venue.getName()),
                eq(venue.getAddress()),
                eq(venue.getCity()),
                eq(venue.getVenueType().name()),
                eq(venue.getVenueId())
        )).thenReturn(1);

        Venue updated = venueDao.update(venue);

        assertEquals("PVR Cinema", updated.getName());
        verify(jdbcTemplate, times(1))
                .update(anyString(),
                        eq(venue.getName()),
                        eq(venue.getAddress()),
                        eq(venue.getCity()),
                        eq(venue.getVenueType().name()),
                        eq(venue.getVenueId())
                );
    }

    // ----------------------------------------------------
    // TEST: searchByName()
    // ----------------------------------------------------
    @Test
    void testSearchByName() {

        when(jdbcTemplate.query(anyString(), any(VenueRowMapper.class), anyString()))
                .thenReturn(List.of(createVenue()));

        List<Venue> result = venueDao.searchByName("PVR");

        assertEquals(1, result.size());
    }

    // ----------------------------------------------------
    // TEST: searchByCity()
    // ----------------------------------------------------
    @Test
    void testSearchByCity() {

        when(jdbcTemplate.query(anyString(), any(VenueRowMapper.class), anyString()))
                .thenReturn(List.of(createVenue()));

        List<Venue> result = venueDao.searchByCity("Mumbai");

        assertEquals(1, result.size());
    }

    // ----------------------------------------------------
    // TEST: searchByType()
    // ----------------------------------------------------
    @Test
    void testSearchByType() {

        when(jdbcTemplate.query(anyString(), any(VenueRowMapper.class), anyString()))
                .thenReturn(List.of(createVenue()));

        List<Venue> result = venueDao.searchByType("CINEMA");

        assertEquals(1, result.size());
    }

    // ----------------------------------------------------
    // TEST: searchByScreen()
    // ----------------------------------------------------
    @Test
    void testSearchByScreen() {

        when(jdbcTemplate.query(anyString(), any(VenueRowMapper.class), anyString()))
                .thenReturn(List.of(createVenue()));

        List<Venue> result = venueDao.searchByScreen("Screen 1");

        assertEquals(1, result.size());
    }
}
