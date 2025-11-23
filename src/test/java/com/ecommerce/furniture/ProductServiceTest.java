package com.ecommerce.furniture;

import com.ecommerce.furniture.models.Category;
import com.ecommerce.furniture.models.Product;
import com.ecommerce.furniture.repository.ProductRepository;
import com.ecommerce.furniture.security.services.ProductService;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.MockedStatic;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.file.*;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private MultipartFile mockImage;

    private ProductService productService;
    private AutoCloseable closeable;

    @BeforeEach
    void setup() {
        closeable = MockitoAnnotations.openMocks(this);
        productService = new ProductService(productRepository, "test-uploads");
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void testGetAllProducts() {
        Product p1 = new Product();
        Product p2 = new Product();

        when(productRepository.findAll()).thenReturn(List.of(p1, p2));

        List<Product> result = productService.getAllProducts();

        assertEquals(2, result.size());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void testGetProductsByCategory() {
        Product p1 = new Product();
        when(productRepository.findByCategoryId(1L)).thenReturn(List.of(p1));

        List<Product> result = productService.getProductsByCategory(1L);

        assertEquals(1, result.size());
        verify(productRepository).findByCategoryId(1L);
    }

    @Test
    void testGetProductByIdFound() {
        Product p = new Product();
        when(productRepository.findById(10L)).thenReturn(Optional.of(p));

        Optional<Product> result = productService.getProductById(10L);

        assertTrue(result.isPresent());
    }

    @Test
    void testGetProductByIdNotFound() {
        when(productRepository.findById(5L)).thenReturn(Optional.empty());

        Optional<Product> result = productService.getProductById(5L);

        assertTrue(result.isEmpty());
    }

    @Test
    void testSaveProductWithImage() throws Exception {
        Category category = new Category();
        Product saved = new Product();
        saved.setId(1L);

        when(mockImage.isEmpty()).thenReturn(false);
        when(mockImage.getOriginalFilename()).thenReturn("chair.png");

        when(mockImage.getInputStream()).thenReturn(InputStream.nullInputStream());
        when(productRepository.save(any(Product.class))).thenReturn(saved);

        try (MockedStatic<Files> staticMock = mockStatic(Files.class)) {
            staticMock.when(() -> Files.copy(any(InputStream.class), any(Path.class), any(StandardCopyOption.class)))
                    .thenReturn(10L);

            Product result = productService.saveProductWithImage("Chair", 200.0, category, mockImage);

            assertNotNull(result);
            verify(productRepository).save(any(Product.class));
        }
    }

    @Test
    void testSaveProductWithoutImage() throws Exception {
        Category category = new Category();
        Product saved = new Product();
        saved.setId(1L);

        when(mockImage.isEmpty()).thenReturn(true);
        when(productRepository.save(any(Product.class))).thenReturn(saved);

        Product result = productService.saveProductWithImage("Table", 500.0, category, mockImage);

        assertNotNull(result);
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void testUpdateProductWithNewImage() throws Exception {
        Category category = new Category();
        Product existing = new Product();
        existing.setImage("old.png");

        when(productRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(productRepository.save(any(Product.class))).thenReturn(existing);

        when(mockImage.isEmpty()).thenReturn(false);
        when(mockImage.getOriginalFilename()).thenReturn("new.png");
        when(mockImage.getInputStream()).thenReturn(InputStream.nullInputStream());

        try (MockedStatic<Files> staticMock = mockStatic(Files.class)) {

            staticMock.when(() -> Files.deleteIfExists(any(Path.class))).thenReturn(true);
            staticMock.when(() -> Files.copy(any(InputStream.class), any(Path.class), any(StandardCopyOption.class)))
                    .thenReturn(20L);

            Product result = productService.updateProductWithImage(
                    1L, "NewName", 300.0, category, mockImage
            );

            assertNotNull(result);
            verify(productRepository).save(existing);
        }
    }


    @Test
    void testUpdateProductWithoutNewImage() throws Exception {
        Category category = new Category();
        Product existing = new Product();
        existing.setName("Old");
        existing.setPrice(100.0);

        when(productRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(productRepository.save(existing)).thenReturn(existing);

        when(mockImage.isEmpty()).thenReturn(true);

        Product result = productService.updateProductWithImage(
                1L, "Updated", 150.0, category, mockImage
        );

        assertEquals("Updated", existing.getName());
        verify(productRepository).save(existing);
    }

    @Test
    void testUpdateProductNotFound() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                productService.updateProductWithImage(99L, "Test", 100.0, null, null)
        );
    }

    @Test
    void testDeleteProductWithImage() throws Exception {
        Product existing = new Product();
        existing.setImage("delete.png");

        when(productRepository.findById(1L)).thenReturn(Optional.of(existing));

        try (MockedStatic<Files> staticMock = mockStatic(Files.class)) {
            staticMock.when(() -> Files.deleteIfExists(any(Path.class))).thenReturn(true);

            productService.deleteProduct(1L);

            verify(productRepository).deleteById(1L);
        }
    }
}
