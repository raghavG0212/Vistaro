package com.project.vistaro.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.vistaro.dto.FoodDto;
import com.project.vistaro.model.Food;
import com.project.vistaro.service.FoodService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


class FoodControllerTest {

    private MockMvc mockMvc;

    @Mock
    private FoodService foodService;

    @InjectMocks
    private FoodController foodController;

    private ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(foodController).build();
    }

    // Helper: Create Food entity
    private Food createFood() {
        Food f = new Food();
        f.setFoodId(1);
        f.setSlotId(10);
        f.setName("Popcorn");
        f.setPrice(BigDecimal.valueOf(150.0));
        return f;
    }

    // Helper: Create DTO
    private FoodDto createFoodDto() {
        FoodDto dto = new FoodDto();
        dto.setFoodId(1);
        dto.setSlotId(10);
        dto.setName("Popcorn");
        dto.setPrice(BigDecimal.valueOf(150.0));

        return dto;
    }

    // -----------------------------------------------------------------------
    // TEST: POST → addFood
    // -----------------------------------------------------------------------
    @Test
    void testAddFood() throws Exception {
        Food savedFood = createFood();

        when(foodService.addFood(any(Food.class))).thenReturn(savedFood);

        mockMvc.perform(post("/api/v1/food")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(createFoodDto())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.foodId").value(1))
                .andExpect(jsonPath("$.name").value("Popcorn"));

        verify(foodService, times(1)).addFood(any(Food.class));
    }

    // -----------------------------------------------------------------------
    // TEST: GET /food/{id}
    // -----------------------------------------------------------------------
    @Test
    void testGetFoodById() throws Exception {
        Food food = createFood();
        when(foodService.getFoodById(1)).thenReturn(food);

        mockMvc.perform(get("/api/v1/food/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Popcorn"));

        verify(foodService).getFoodById(1);
    }

    // -----------------------------------------------------------------------
    // TEST: GET /food (all)
    // -----------------------------------------------------------------------
    @Test
    void testGetAllFood() throws Exception {
        when(foodService.getAllFoods()).thenReturn(List.of(createFood()));

        mockMvc.perform(get("/api/v1/food"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].slotId").value(10));

        verify(foodService).getAllFoods();
    }

    // -----------------------------------------------------------------------
    // TEST: GET /food/slot/{slotId}
    // -----------------------------------------------------------------------
    @Test
    void testGetFoodBySlot() throws Exception {
        when(foodService.getFoodsBySlot(10)).thenReturn(List.of(createFood()));

        mockMvc.perform(get("/api/v1/food/slot/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].price").value(150.0));

        verify(foodService).getFoodsBySlot(10);
    }

    // -----------------------------------------------------------------------
    // TEST: DELETE /food/{id}
    // -----------------------------------------------------------------------
    @Test
    void testDeleteFood() throws Exception {

        mockMvc.perform(delete("/api/v1/food/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Food deleted successfully"));

        verify(foodService).deleteFood(1);
    }

    // -----------------------------------------------------------------------
    // TEST: PUT /food/{id}
    // -----------------------------------------------------------------------
    @Test
    void testUpdateFood() throws Exception {
        Food updated = createFood();
        updated.setName("Nachos");

        when(foodService.updateFood(eq(1), any(Food.class))).thenReturn(updated);

        FoodDto dto = createFoodDto();
        dto.setName("Nachos");

        mockMvc.perform(put("/api/v1/food/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Nachos"));

        verify(foodService).updateFood(eq(1), any(Food.class));
    }
}
