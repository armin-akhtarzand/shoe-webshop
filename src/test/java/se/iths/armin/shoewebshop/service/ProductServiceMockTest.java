package se.iths.armin.shoewebshop.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.iths.armin.shoewebshop.entity.Product;
import se.iths.armin.shoewebshop.repository.ProductRepository;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceMockTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    void createProduct_shouldSaveAndReturnProduct() {
        Product product = createTestProduct("Test Shoe", "Sneakers");
        Product savedProduct = createTestProduct("Test Shoe", "Sneakers");
        savedProduct.setProductId(1L);

        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

        Product result = productService.createProduct(product);

        assertNotNull(result);
        assertEquals(1L, result.getProductId());
        assertEquals("Test Shoe", result.getProductName());
    }

    @Test
    void getAllProducts_shouldReturnAllProducts() {
        List<Product> products = Arrays.asList(
                createTestProduct("Shoe 1", "Sneakers"),
                createTestProduct("Shoe 2", "Boots")
        );
        when(productRepository.findAll()).thenReturn(products);

        List<Product> result = productService.getAllProducts();

        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(p -> "Shoe 1".equals(p.getProductName())));
        assertTrue(result.stream().anyMatch(p -> "Shoe 2".equals(p.getProductName())));
    }

    @Test
    void findById_shouldReturnProduct() {
        Product product = createTestProduct("Test Shoe", "Sneakers");
        product.setProductId(1L);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Product result = productService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getProductId());
        assertEquals("Test Shoe", result.getProductName());
    }

    @Test
    void findById_shouldThrowExceptionWhenNotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> productService.findById(1L));
    }

    @Test
    void getProductsByCategory_shouldReturnFilteredProducts() {
        List<Product> sneakers = Arrays.asList(
                createTestProduct("Sneakers A", "Sneakers"),
                createTestProduct("Sneakers B", "Sneakers")
        );
        when(productRepository.findByCategory("Sneakers")).thenReturn(sneakers);

        List<Product> result = productService.getProductsByCategory("Sneakers");

        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(p -> "Sneakers".equals(p.getCategory())));
    }

    @Test
    void getProductsByCategory_shouldThrowExceptionForNullCategory() {
        assertThrows(IllegalArgumentException.class, () -> productService.getProductsByCategory(null));
    }

    @Test
    void getProductsByCategory_shouldThrowExceptionForEmptyCategory() {
        assertThrows(IllegalArgumentException.class, () -> productService.getProductsByCategory(""));
    }

    private Product createTestProduct(String name, String category) {
        Product product = new Product();
        product.setProductName(name);
        product.setCategory(category);
        product.setPrice(new BigDecimal("49.99"));
        product.setProductImageURL("http://example.com/test.jpg");
        return product;
    }
}