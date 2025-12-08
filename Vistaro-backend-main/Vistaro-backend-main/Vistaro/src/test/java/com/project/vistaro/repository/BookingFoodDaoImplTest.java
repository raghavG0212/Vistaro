package com.project.vistaro.repository;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import com.project.vistaro.model.BookingFood;
import com.project.vistaro.util.BookingFoodRowMapper;

class BookingFoodDaoImplTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private BookingFoodDaoImpl bookingFoodDao;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    // Helper
    private BookingFood createEntity() {
        BookingFood bf = new BookingFood();
        bf.setBookingFoodId(1);
        bf.setBookingId(1001);
        bf.setFoodId(10);
        bf.setQuantity(3);
        return bf;
    }

    // ---------------------------------------------------------
    // TEST: save()
    // ---------------------------------------------------------
    @Test
    void testSave() {
        BookingFood bf = createEntity();
        bf.setBookingFoodId(null);  // ID auto-generated

        // Mock KeyHolder behavior
        doAnswer(invocation -> {
            GeneratedKeyHolder kh = invocation.getArgument(1);
            kh.getKeyList().add(Map.of("GENERATED_KEY", 1));
            return 1;
        }).when(jdbcTemplate).update(any(), any(GeneratedKeyHolder.class));

        BookingFood result = bookingFoodDao.save(bf);

        assertEquals(1, result.getBookingFoodId());
    }

    // ---------------------------------------------------------
    // TEST: findById()
    // ---------------------------------------------------------
    @Test
    void testFindById() {

        BookingFood bf = createEntity();

        when(jdbcTemplate.queryForObject(anyString(), any(BookingFoodRowMapper.class), eq(1)))
                .thenReturn(bf);

        BookingFood result = bookingFoodDao.findById(1);

        assertNotNull(result);
        assertEquals(1, result.getBookingFoodId());
        assertEquals(10, result.getFoodId());
        verify(jdbcTemplate).queryForObject(anyString(), any(BookingFoodRowMapper.class), eq(1));
    }

    // ---------------------------------------------------------
    // TEST: findByBookingId()
    // ---------------------------------------------------------
    @Test
    void testFindByBookingId() {

        when(jdbcTemplate.query(anyString(), any(BookingFoodRowMapper.class), eq(1001)))
                .thenReturn(List.of(createEntity()));

        List<BookingFood> list = bookingFoodDao.findByBookingId(1001);

        assertEquals(1, list.size());
        verify(jdbcTemplate).query(anyString(), any(BookingFoodRowMapper.class), eq(1001));
    }

    // ---------------------------------------------------------
    // TEST: findAll()
    // ---------------------------------------------------------
    @Test
    void testFindAll() {

        when(jdbcTemplate.query(anyString(), any(BookingFoodRowMapper.class)))
                .thenReturn(List.of(createEntity()));

        List<BookingFood> list = bookingFoodDao.findAll();

        assertEquals(1, list.size());
        verify(jdbcTemplate).query(anyString(), any(BookingFoodRowMapper.class));
    }

    // ---------------------------------------------------------
    // TEST: update()
    // ---------------------------------------------------------
    @Test
    void testUpdate() {

        BookingFood bf = createEntity();

        when(jdbcTemplate.update(anyString(), anyInt(), anyInt(), anyInt(), anyInt()))
                .thenReturn(1);

        BookingFood result = bookingFoodDao.update(bf);

        assertEquals(bf, result);
        verify(jdbcTemplate).update(anyString(),
                eq(bf.getBookingId()),
                eq(bf.getFoodId()),
                eq(bf.getQuantity()),
                eq(bf.getBookingFoodId()));
    }

    // ---------------------------------------------------------
    // TEST: delete()
    // ---------------------------------------------------------
    @Test
    void testDelete() {

        when(jdbcTemplate.update(anyString(), eq(1))).thenReturn(1);

        bookingFoodDao.delete(1);

        verify(jdbcTemplate).update(anyString(), eq(1));
    }

    // ---------------------------------------------------------
    // TEST: findByBookingIdDetailed()
    // ---------------------------------------------------------
    @Test
    void testFindByBookingIdDetailed() {

        List<Map<String, Object>> mockList = List.of(
                Map.of("booking_food_id", 1, "food_name", "Popcorn", "line_total", 300)
        );

        when(jdbcTemplate.queryForList(anyString(), eq(1001))).thenReturn(mockList);

        List<Map<String, Object>> result = bookingFoodDao.findByBookingIdDetailed(1001);

        assertEquals(1, result.size());
        assertEquals("Popcorn", result.get(0).get("food_name"));

        verify(jdbcTemplate).queryForList(anyString(), eq(1001));
    }
}
