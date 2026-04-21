package se.iths.armin.shoewebshop.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import se.iths.armin.shoewebshop.entity.Product;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class ProductServiceH2Test {

    @Autowired
    private ProductService productService;

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setProductName("Test Shoe");
        product.setCategory("Sneakers");
        product.setPrice(new BigDecimal("99.99"));
        product.setProductImageURL("http://image.com");
    }

    @Test
    void createProduct_shouldSaveProduct() {
        Product saved = productService.createProduct(product);

        assertNotNull(saved);
        assertNotNull(saved.getProductId());
        assertEquals("Test Shoe", saved.getProductName());
    }

    @Test
    void getAllProducts_shouldReturnList() {
        productService.createProduct(product);

        List<Product> products = productService.getAllProducts();

        assertFalse(products.isEmpty());
    }

    @Test
    void findById_shouldReturnProduct() {
        Product saved = productService.createProduct(product);

        Product found = productService.findById(saved.getProductId());

        assertEquals(saved.getProductId(), found.getProductId());
    }

    @Test
    void getProductsByCategory_shouldReturnProducts() {
        productService.createProduct(product);

        List<Product> result = productService.getProductsByCategory("Sneakers");

        assertFalse(result.isEmpty());
        assertTrue(result.stream().allMatch(p -> p.getCategory().equals("Sneakers")));
    }
}