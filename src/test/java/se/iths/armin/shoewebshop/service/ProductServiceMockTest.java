package se.iths.armin.shoewebshop.service;

import org.junit.jupiter.api.BeforeEach;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceMockTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product testProduct;

    private Product savedTestProduct;

    @BeforeEach
    void setUp() {
        testProduct = createTestProduct("Test Shoe","Sneakers");

        savedTestProduct = createTestProduct("Test Shoe","Sneakers");
        savedTestProduct.setProductId(1L);

        when(productRepository.save(any(Product.class))).thenReturn(savedTestProduct);
        when(productRepository.findById(1L)).thenReturn(Optional.of(savedTestProduct));
    }

    @Test
    void createProduct_shouldSaveAndReturnProduct() {

        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        Product result = productService.createProduct(testProduct);

        assertThat(result).isNotNull();
        assertThat(result.getProductId()).isEqualTo(1L);
        assertThat(result.getProductName()).isEqualTo("Test Shoe");
    }

    @Test
    void getAllProducts_shouldReturnAllProducts() {
        List<Product> products = Arrays.asList(
                createTestProduct("Shoe 1", "Sneakers"),
                createTestProduct("Shoe 2", "Boots")
        );
        when(productRepository.findAll()).thenReturn(products);

        List<Product> result = productService.getAllProducts();

        assertThat(result).hasSize(2);
        assertThat(result).extracting(Product::getProductName)
                .contains("Shoe 1", "Shoe 2");
    }

    @Test
    void findById_shouldReturnProduct() {
        Product result = productService.findById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getProductId()).isEqualTo(1L);
        assertThat(result.getProductName()).isEqualTo("Test Shoe");
    }

    @Test
    void findById_shouldThrowExceptionWhenNotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.findById(1L))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Product with ID 1 not found");
    }

    @Test
    void getProductsByCategory_shouldReturnFilteredProducts() {
        List<Product> sneakers = Arrays.asList(
                createTestProduct("Sneakers A", "Sneakers"),
                createTestProduct("Sneakers B", "Sneakers")
        );
        when(productRepository.findByCategory("Sneakers")).thenReturn(sneakers);

        List<Product> result = productService.getProductsByCategory("Sneakers");

        assertThat(result).hasSize(2);
        assertThat(result).allMatch(p -> p.getCategory().equals("Sneakers"));
    }

    @Test
    void getProductsByCategory_shouldThrowExceptionForNullCategory() {
        assertThatThrownBy(() -> productService.getProductsByCategory(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Category cannot be null or empty");
    }

    @Test
    void getProductsByCategory_shouldThrowExceptionForEmptyCategory() {
        assertThatThrownBy(() -> productService.getProductsByCategory(""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Category cannot be null or empty");
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