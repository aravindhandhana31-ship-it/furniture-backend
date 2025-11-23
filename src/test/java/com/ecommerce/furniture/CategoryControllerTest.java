package com.ecommerce.furniture;

import com.ecommerce.furniture.controllers.CategoryController;
import com.ecommerce.furniture.models.Category;
import com.ecommerce.furniture.security.services.CategoryService;
import com.ecommerce.furniture.security.services.FileStorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CategoryControllerTest {

    @Mock
    private CategoryService categoryService;

    @Mock
    private FileStorageService fileStorageService;

    @InjectMocks
    private CategoryController categoryController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAll() {
        List<Category> categories = Arrays.asList(
                new Category("Sofas", "Comfortable sofas", "sofa.jpg")
        );

        when(categoryService.getAllCategories()).thenReturn(categories);

        List<Category> result = categoryController.getAll();

        assertEquals(1, result.size());
        verify(categoryService, times(1)).getAllCategories();
    }

    @Test
    void testCreateCategoryWithImage() throws IOException {
        MockMultipartFile file = new MockMultipartFile(
                "image",
                "image.jpg",
                "image/jpeg",
                "test image".getBytes()
        );

        when(fileStorageService.saveFile(file)).thenReturn("image.jpg");

        Category mockCategory = new Category("Lights", "Home lighting", "image.jpg");
        when(categoryService.saveCategory(any(Category.class))).thenReturn(mockCategory);

        Category result = categoryController.createCategory("Lights", "Home lighting", file);

        assertNotNull(result);
        assertEquals("Lights", result.getName());
        assertEquals("image.jpg", result.getImage());

        verify(fileStorageService, times(1)).saveFile(file);
        verify(categoryService, times(1)).saveCategory(any(Category.class));
    }

    @Test
    void testCreateCategoryWithoutImage() throws IOException {
        Category mockCategory = new Category("Mirrors", "Wall mirrors", null);

        when(categoryService.saveCategory(any(Category.class))).thenReturn(mockCategory);

        Category result = categoryController.createCategory("Mirrors", "Wall mirrors", null);

        assertNotNull(result);
        assertEquals("Mirrors", result.getName());
        assertNull(result.getImage());

        verify(categoryService, times(1)).saveCategory(any(Category.class));
        verify(fileStorageService, times(0)).saveFile(any());
    }

    @Test
    void testDeleteCategory() {
        Long id = 1L;

        doNothing().when(categoryService).deleteCategory(id);

        categoryController.deleteCategory(id);

        verify(categoryService, times(1)).deleteCategory(id);
    }

    @Test
    void testGetCategoryProductCounts() {
        List<Object[]> mockCounts = new ArrayList<>();
        mockCounts.add(new Object[]{"Chairs", 12L});

        when(categoryService.getCategoryProductCounts()).thenReturn(mockCounts);

        List<Map<String, Object>> result = categoryController.getCategoryProductCounts();

        assertEquals(1, result.size());
        assertEquals("Chairs", result.get(0).get("name"));
        assertEquals(12L, result.get(0).get("productCount"));

        verify(categoryService, times(1)).getCategoryProductCounts();
    }
}
