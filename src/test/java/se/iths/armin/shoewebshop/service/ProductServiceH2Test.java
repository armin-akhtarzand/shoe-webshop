package se.iths.armin.shoewebshop.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import se.iths.armin.shoewebshop.entity.Product;
import se.iths.armin.shoewebshop.repository.ProductRepository;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class ProductServiceH2Test {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Test
    void createProduct_shouldSaveProduct() {
        Product product = new Product();
        product.setProductName("Test Shoe");
        product.setCategory("Sneakers");
        product.setPrice(new BigDecimal("99.99"));
        product.setProductImageURL("http://example.com/image.jpg");

        Product savedProduct = productService.createProduct(product);

        assertThat(savedProduct.getProductId()).isNotNull();
        assertThat(savedProduct.getProductName()).isEqualTo("Test Shoe");
        assertThat(productRepository.findById(savedProduct.getProductId())).isPresent();
    }

    @Test
    void getAllProducts_shouldReturnAllProducts() {
        Product product1 = createTestProduct("Shoe 1", "Sneakers");
        Product product2 = createTestProduct("Shoe 2", "Boots");
        productRepository.save(product1);
        productRepository.save(product2);
        List<Product> products = productService.getAllProducts();

        assertThat(products).hasSizeGreaterThanOrEqualTo(2);
        assertThat(products).extracting(Product::getProductName)
                .contains("Shoe 1", "Shoe 2");
    }

    @Test
    void findById_shouldReturnProduct() {
        Product product = createTestProduct("Test Shoe", "Sneakers");
        Product savedProduct = productRepository.save(product);

        Product foundProduct = productService.findById(savedProduct.getProductId());

        assertThat(foundProduct).isNotNull();
        assertThat(foundProduct.getProductName()).isEqualTo("Test Shoe");
    }

    @Test
    void getProductsByCategory_shouldReturnFilteredProducts() {
        Product sneakers = createTestProduct("Sneakers A", "Sneakers");
        Product boots = createTestProduct("Boots A", "Boots");
        productRepository.save(sneakers);
        productRepository.save(boots);

        List<Product> sneakersList = productService.getProductsByCategory("Sneakers");

        assertThat(sneakersList).hasSize(1);
        assertThat(sneakersList.get(0).getCategory()).isEqualTo("Sneakers");
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