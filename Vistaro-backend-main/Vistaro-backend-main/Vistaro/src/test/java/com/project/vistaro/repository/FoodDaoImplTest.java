package com.project.vistaro.repository;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import com.project.vistaro.model.Food;
import com.project.vistaro.util.FoodRowMapper;

class FoodDaoImplTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private FoodDaoImpl foodDao;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    // Helper method
    private Food createFood() {
        Food f = new Food();
        f.setFoodId(1);
        f.setSlotId(10);
        f.setName("Popcorn");
        f.setPrice(BigDecimal.valueOf(150.0));
        return f;
    }

    // -------------------------------------------------------------
    // TEST: SAVE
    // -------------------------------------------------------------
    @Test
    void testSave() {
        Food food = createFood();

        doAnswer(invocation -> {
            GeneratedKeyHolder keyHolder = invocation.getArgument(1);
            keyHolder.getKeyList().add(Map.of("GENERATED_KEY", 1));
            return 1; // update() returns int
        }).when(jdbcTemplate).update(any(), any(GeneratedKeyHolder.class));

        Food result = foodDao.save(food);

        assertEquals(1, result.getFoodId());
        verify(jdbcTemplate, times(1)).update(any(), any(GeneratedKeyHolder.class));
    }

    // -------------------------------------------------------------
    // TEST: UPDATE
    // -------------------------------------------------------------
    @Test
    void testUpdate() {
        Food food = createFood();

        when(jdbcTemplate.update(anyString(),
                eq(food.getSlotId()),
                eq(food.getName()),
                eq(food.getPrice()),
                eq(food.getFoodId())))
                .thenReturn(1);

        Food result = foodDao.update(food);

        assertEquals(food.getFoodId(), result.getFoodId());
        verify(jdbcTemplate, times(1))
                .update(anyString(), eq(10), eq("Popcorn"), eq(BigDecimal.valueOf(150.0)), eq(1));
    }

    // -------------------------------------------------------------
    // TEST: findById
    // -------------------------------------------------------------
    @Test
    void testFindById() {
        Food food = createFood();

        when(jdbcTemplate.queryForObject(anyString(), any(FoodRowMapper.class), eq(1)))
                .thenReturn(food);

        Food result = foodDao.findById(1);

        assertNotNull(result);
        assertEquals("Popcorn", result.getName());
        verify(jdbcTemplate, times(1))
                .queryForObject(anyString(), any(FoodRowMapper.class), eq(1));
    }

    // -------------------------------------------------------------
    // TEST: findAll
    // -------------------------------------------------------------
    @Test
    void testFindAll() {
        when(jdbcTemplate.query(anyString(), any(FoodRowMapper.class)))
                .thenReturn(List.of(createFood()));

        List<Food> result = foodDao.findAll();

        assertEquals(1, result.size());
        verify(jdbcTemplate).query(anyString(), any(FoodRowMapper.class));
    }

    // -------------------------------------------------------------
    // TEST: findBySlotId
    // -------------------------------------------------------------
    @Test
    void testFindBySlotId() {
        when(jdbcTemplate.query(anyString(), any(FoodRowMapper.class), eq(10)))
                .thenReturn(List.of(createFood()));

        List<Food> result = foodDao.findBySlotId(10);

        assertEquals(1, result.size());
        verify(jdbcTemplate).query(anyString(), any(FoodRowMapper.class), eq(10));
    }

    // -------------------------------------------------------------
    // TEST: DELETE
    // -------------------------------------------------------------
    @Test
    void testDelete() {
        when(jdbcTemplate.update(anyString(), eq(1))).thenReturn(1);

        foodDao.delete(1);

        verify(jdbcTemplate, times(1)).update(anyString(), eq(1));
    }
}
