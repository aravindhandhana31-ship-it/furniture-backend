package com.ecommerce.furniture;

import com.ecommerce.furniture.controllers.ProductController;
import com.ecommerce.furniture.models.Category;
import com.ecommerce.furniture.models.Product;
import com.ecommerce.furniture.repository.CategoryRepository;
import com.ecommerce.furniture.security.services.ProductService;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
@AutoConfigureMockMvc(addFilters = false) 
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @MockBean
    private CategoryRepository categoryRepository;

    // ------------------------ GET ALL ------------------------
    @Test
    void testGetAllProducts() throws Exception {
        Mockito.when(productService.getAllProducts())
                .thenReturn(List.of(new Product(), new Product()));

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk());
    }

    // ------------------------ GET BY ID ------------------------
    @Test
    void testGetProductById() throws Exception {
        Product p = new Product();
        p.setId(1L);

        Mockito.when(productService.getProductById(1L))
                .thenReturn(Optional.of(p));

        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk());
    }

    // ------------------------ CREATE PRODUCT ------------------------
    @Test
    void testCreateProduct() throws Exception {
        Category category = new Category();
        category.setId(1L);

        Product created = new Product();
        created.setId(1L);

        MockMultipartFile image = new MockMultipartFile(
                "image",
                "file.png",
                "image/png",
                "test".getBytes()
        );

        Mockito.when(categoryRepository.findById(1L))
                .thenReturn(Optional.of(category));

        Mockito.when(productService.saveProductWithImage(anyString(), anyDouble(), any(), any()))
                .thenReturn(created);

        mockMvc.perform(multipart("/api/products")
                        .file(image)
                        .param("name", "Table")
                        .param("price", "500")
                        .param("categoryId", "1"))
                .andExpect(status().isOk());
    }

    // ------------------------ DELETE ------------------------
    @Test
    void testDeleteProduct() throws Exception {
        mockMvc.perform(delete("/api/products/1"))
                .andExpect(status().isOk());
    }
}
