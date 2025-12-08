package com.project.vistaro.service;

import com.project.vistaro.exception.ResourceNotFoundException;
import com.project.vistaro.model.Venue;
import com.project.vistaro.model.VenueType;
import com.project.vistaro.repository.VenueDao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class VenueServiceImplTest {

    @Mock
    private VenueDao venueDao;

    @InjectMocks
    private VenueServiceImpl venueService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    // -----------------------------------------------
    // Helper method
    // -----------------------------------------------
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

    // ------------------------------------------------
    // Test save()
    // ------------------------------------------------
    @Test
    void testSave() {
        Venue venue = createVenue();

        when(venueDao.save(any(Venue.class))).thenReturn(venue);

        Venue saved = venueService.save(venue);

        assertEquals("PVR Cinema", saved.getName());
        verify(venueDao, times(1)).save(venue);
    }

    // ------------------------------------------------
    // Test findById() - success
    // ------------------------------------------------
    @Test
    void testFindByIdSuccess() {
        Venue venue = createVenue();

        when(venueDao.findById(1)).thenReturn(Optional.of(venue));

        Venue result = venueService.findById(1);

        assertEquals(1, result.getVenueId());
        verify(venueDao, times(1)).findById(1);
    }

    // ------------------------------------------------
    // Test findById() - not found
    // ------------------------------------------------
    @Test
    void testFindByIdNotFound() {

        when(venueDao.findById(1)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> venueService.findById(1));

        verify(venueDao, times(1)).findById(1);
    }

    // ------------------------------------------------
    // Test findAll()
    // ------------------------------------------------
    @Test
    void testFindAll() {

        when(venueDao.findAll()).thenReturn(List.of(createVenue()));

        List<Venue> result = venueService.findAll();

        assertEquals(1, result.size());
        assertEquals("PVR Cinema", result.get(0).getName());
    }

    // ------------------------------------------------
    // Test update()
    // ------------------------------------------------
    @Test
    void testUpdate() {

        Venue existing = createVenue();
        Venue newData = new Venue();
        newData.setName("New Name");
        newData.setCity("Delhi");
        newData.setAddress("New Address");
        newData.setVenueType(VenueType.AUDITORIUM);

        when(venueDao.findById(1)).thenReturn(Optional.of(existing));
        when(venueDao.update(any(Venue.class))).thenAnswer(inv -> inv.getArgument(0));

        Venue updated = venueService.update(1, newData);

        assertEquals("New Name", updated.getName());
        assertEquals("Delhi", updated.getCity());
        assertEquals("New Address", updated.getAddress());
        assertEquals(VenueType.AUDITORIUM, updated.getVenueType());

        verify(venueDao, times(1)).update(existing);
    }

    // ------------------------------------------------
    // Test searchByName()
    // ------------------------------------------------
    @Test
    void testSearchByName() {

        when(venueDao.searchByName("PVR"))
                .thenReturn(List.of(createVenue()));

        List<Venue> result = venueService.searchByName("PVR");

        assertEquals(1, result.size());
    }

    // ------------------------------------------------
    // Test searchByCity()
    // ------------------------------------------------
    @Test
    void testSearchByCity() {

        when(venueDao.searchByCity("Mumbai"))
                .thenReturn(List.of(createVenue()));

        List<Venue> result = venueService.searchByCity("Mumbai");

        assertEquals(1, result.size());
    }

    // ------------------------------------------------
    // Test searchByType()
    // ------------------------------------------------
    @Test
    void testSearchByType() {

        when(venueDao.searchByType("CINEMA"))
                .thenReturn(List.of(createVenue()));

        List<Venue> result = venueService.searchByType("CINEMA");

        assertEquals(1, result.size());
    }

    // ------------------------------------------------
    // Test searchByScreen()
    // ------------------------------------------------
    @Test
    void testSearchByScreen() {

        when(venueDao.searchByScreen("Screen 1"))
                .thenReturn(List.of(createVenue()));

        List<Venue> result = venueService.searchByScreen("Screen 1");

        assertEquals(1, result.size());
    }
}
