package se.iths.armin.shoewebshop.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mock.web.MockHttpSession;
import se.iths.armin.shoewebshop.entity.Cart;
import se.iths.armin.shoewebshop.entity.Product;
import se.iths.armin.shoewebshop.repository.ProductRepository;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
public class CartServiceIntegrationTest {

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductRepository productRepository;

    private MockHttpSession session;

    @BeforeEach
    void setUp() {
        session = new MockHttpSession();
        productRepository.deleteAll();
    }

    private Product createProduct(String name, double price) {
        Product product = new Product();
        product.setProductName(name);
        product.setPrice(BigDecimal.valueOf(price));
        product.setCategory("Sneakers");
        product.setProductImageURL("http://example.com/image.png");
        return productRepository.save(product);
    }

    @Test
    void shouldCreateNewCartIfNoneExistsInSession() {
        assertThat(session.getAttribute("cart")).isNull();

        Cart cart = cartService.getCart(session);

        assertThat(cart).isNotNull();
        assertThat(session.getAttribute("cart")).isEqualTo(cart);
    }

    @Test
    void shouldReturnExistingCartFromSession() {
        Cart existingCart = new Cart();
        session.setAttribute("cart", existingCart);

        Cart returnedCart = cartService.getCart(session);

        assertThat(returnedCart).isSameAs(existingCart);
    }


    @Test
    void shouldAddProductToCartAndIncreaseQuantity() {
        Product product = createProduct("Test Sko", 500.0);

        cartService.addProduct(session, product.getProductId());
        cartService.addProduct(session, product.getProductId());

        Cart cart = cartService.getCart(session);

        assertThat(cart.getCartItems()).hasSize(1);
        assertThat(cart.getCartItems().get(0).getQuantity()).isEqualTo(2);
    }

    @Test
    void shouldCalculateTotalPriceCorrectly() {
        Product product = createProduct("Test Sko", 500.0);

        cartService.addProduct(session, product.getProductId());
        cartService.addProduct(session, product.getProductId());

        Cart cart = cartService.getCart(session);

        assertThat(cart.getTotalPrice()).isEqualByComparingTo(BigDecimal.valueOf(1000.0));
    }

    @Test
    void shouldThrowExceptionIfProductDoesNotExist() {
        assertThatThrownBy(() ->
                cartService.addProduct(session, 999L)
        ).isInstanceOf(RuntimeException.class);
    }

    @Test
    void shouldClearCartInSession() {
        Product product = createProduct("Test Sko", 500.0);

        cartService.addProduct(session, product.getProductId());

        assertThat(cartService.isCartEmpty(session)).isFalse();

        cartService.clearCart(session);

        assertThat(cartService.isCartEmpty(session)).isTrue();
    }

    @TestConfiguration
    static class TestMailConfig {
        @Bean
        JavaMailSender mailSender() {
            return new JavaMailSenderImpl();
        }
    }
}
