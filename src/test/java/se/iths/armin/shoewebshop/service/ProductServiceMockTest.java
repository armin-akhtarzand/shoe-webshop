package se.iths.armin.shoewebshop.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.iths.armin.shoewebshop.entity.Product;
import se.iths.armin.shoewebshop.repository.ProductRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceMockTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    void createProduct_shouldSaveProduct() {
        Product product = createProduct();

        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product result = productService.createProduct(product);

        assertEquals("Test Shoe", result.getProductName());
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void getAllProducts_shouldReturnList() {
        when(productRepository.findAll()).thenReturn(List.of(createProduct()));

        List<Product> result = productService.getAllProducts();

        assertEquals(1, result.size());
        verify(productRepository).findAll();
    }

    @Test
    void findById_shouldReturnProduct() {
        Product product = createProduct();

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Product result = productService.findById(1L);

        assertEquals("Test Shoe", result.getProductName());
        verify(productRepository).findById(1L);
    }

    @Test
    void getProductsByCategory_shouldReturnProducts() {
        when(productRepository.findByCategory("Sneakers"))
                .thenReturn(List.of(createProduct()));

        List<Product> result = productService.getProductsByCategory("Sneakers");

        assertEquals(1, result.size());
        verify(productRepository).findByCategory("Sneakers");
    }

    private Product createProduct() {
        Product product = new Product();
        product.setProductName("Test Shoe");
        product.setCategory("Sneakers");
        product.setPrice(new BigDecimal("99.99"));
        product.setProductImageURL("http://image.com");
        return product;
    }
}