package com.project.vistaro.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import com.project.vistaro.model.Food;
import com.project.vistaro.repository.FoodDao;

class FoodServiceImplTest {

    @Mock
    private FoodDao foodDao;

    @InjectMocks
    private FoodServiceImpl foodService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    // Helper method to create a Food object
    private Food createFood() {
        Food f = new Food();
        f.setFoodId(1);
        f.setSlotId(10);
        f.setName("Popcorn");
        f.setPrice(BigDecimal.valueOf(150.0));
        return f;
    }

    // -------------------------------------------------------
    // TEST: addFood
    // -------------------------------------------------------
    @Test
    void testAddFood() {
        Food food = createFood();
        when(foodDao.save(food)).thenReturn(food);

        Food result = foodService.addFood(food);

        assertEquals(food, result);
        verify(foodDao, times(1)).save(food);
    }

    // -------------------------------------------------------
    // TEST: getFoodById
    // -------------------------------------------------------
    @Test
    void testGetFoodById() {
        Food food = createFood();
        when(foodDao.findById(1)).thenReturn(food);

        Food result = foodService.getFoodById(1);

        assertNotNull(result);
        assertEquals("Popcorn", result.getName());
        verify(foodDao, times(1)).findById(1);
    }

    // -------------------------------------------------------
    // TEST: getAllFoods
    // -------------------------------------------------------
    @Test
    void testGetAllFoods() {
        when(foodDao.findAll()).thenReturn(List.of(createFood()));

        List<Food> result = foodService.getAllFoods();

        assertEquals(1, result.size());
        verify(foodDao, times(1)).findAll();
    }

    // -------------------------------------------------------
    // TEST: getFoodsBySlot
    // -------------------------------------------------------
    @Test
    void testGetFoodsBySlot() {
        when(foodDao.findBySlotId(10)).thenReturn(List.of(createFood()));

        List<Food> result = foodService.getFoodsBySlot(10);

        assertEquals(1, result.size());
        verify(foodDao, times(1)).findBySlotId(10);
    }

    // -------------------------------------------------------
    // TEST: deleteFood
    // -------------------------------------------------------
    @Test
    void testDeleteFood() {
        doNothing().when(foodDao).delete(1);

        foodService.deleteFood(1);

        verify(foodDao, times(1)).delete(1);
    }

    // -------------------------------------------------------
    // TEST: updateFood (Success)
    // -------------------------------------------------------
    @Test
    void testUpdateFoodSuccess() {
        Food existing = createFood();
        Food updated = createFood();
        updated.setName("Nachos");

        when(foodDao.findById(1)).thenReturn(existing);
        when(foodDao.update(any(Food.class))).thenReturn(updated);

        Food result = foodService.updateFood(1, updated);

        assertEquals("Nachos", result.getName());
        verify(foodDao, times(1)).findById(1);
        verify(foodDao, times(1)).update(existing);
    }

    // -------------------------------------------------------
    // TEST: updateFood (Food not found)
    // -------------------------------------------------------
    @Test
    void testUpdateFoodNotFound() {
        when(foodDao.findById(1)).thenReturn(null);

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                foodService.updateFood(1, createFood())
        );

        assertEquals("Food with id: 1 not found", ex.getMessage());
        verify(foodDao, times(1)).findById(1);
        verify(foodDao, never()).update(any());
    }
}
