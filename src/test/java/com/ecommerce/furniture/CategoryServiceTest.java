package com.ecommerce.furniture;

import com.ecommerce.furniture.models.Category;
import com.ecommerce.furniture.repository.CategoryRepository;
import com.ecommerce.furniture.security.services.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllCategories() {
        List<Category> mockCategories = Arrays.asList(
                new Category("Chairs", "All types of chairs", "chair.jpg"),
                new Category("Tables", "Dining & work tables", "table.jpg")
        );

        when(categoryRepository.findAll()).thenReturn(mockCategories);

        List<Category> categories = categoryService.getAllCategories();

        assertEquals(2, categories.size());
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    void testSaveCategory() {
        Category category = new Category("Beds", "Comfortable beds", "bed.jpg");

        when(categoryRepository.save(category)).thenReturn(category);

        Category saved = categoryService.saveCategory(category);

        assertNotNull(saved);
        assertEquals("Beds", saved.getName());
        verify(categoryRepository, times(1)).save(category);
    }

    @Test
    void testDeleteCategory() {
        Long id = 1L;

        doNothing().when(categoryRepository).deleteById(id);

        categoryService.deleteCategory(id);

        verify(categoryRepository, times(1)).deleteById(id);
    }

    @Test
    void testGetCategoryProductCounts() {
        List<Object[]> mockData = new ArrayList<>();
        mockData.add(new Object[]{"Chairs", 10L});
        mockData.add(new Object[]{"Tables", 5L});

        when(categoryRepository.findCategoryProductCounts()).thenReturn(mockData);

        List<Object[]> results = categoryService.getCategoryProductCounts();

        assertEquals(2, results.size());
        verify(categoryRepository, times(1)).findCategoryProductCounts();
    }
}
