package se.iths.armin.shoewebshop.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import se.iths.armin.shoewebshop.entity.Cart;
import se.iths.armin.shoewebshop.entity.Product;
import se.iths.armin.shoewebshop.repository.ProductRepository;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class CartServiceIntegrationTest {

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductRepository productRepository;

    private MockHttpSession session;

    @BeforeEach
    void setUp() {
        // Vi skapar en ny tom session före varje test
        session = new MockHttpSession();
        // Vi tömmer databasen så vi startar rent i varje test
        productRepository.deleteAll();
    }

    @Test
    void shouldCreateNewCartIfNoneExistsInSession() {
        // Kontrollera att sessionen är tom från början
        assertThat(session.getAttribute("cart")).isNull();

        // Anropa tjänsten för att hämta varukorgen
        Cart cart = cartService.getCart(session);

        // Kontrollera att en ny varukorg skapades och sparades i sessionen
        assertThat(cart).isNotNull();
        assertThat(session.getAttribute("cart")).isEqualTo(cart);
    }

    @Test
    void shouldAddProductToCartAndVerifyContent() {
        // 1. Skapa och spara en produkt i databasen (H2)
        Product product = new Product();
        product.setProductName("Test Sko");
        product.setPrice(BigDecimal.valueOf(500.0));
        product.setCategory("Sneakers");
        product.setProductImageURL("http://example.com/image.png");
        product = productRepository.save(product);

        // 2. Lägg till produkten i varukorgen via tjänsten
        cartService.addProduct(session, product.getProductId());

        // 3. Hämta varukorgen och kontrollera att den innehåller produkten
        Cart cart = cartService.getCart(session);
        assertThat(cart.getCartItems()).hasSize(1);
        assertThat(cart.getCartItems().get(0).getProduct().getProductName()).isEqualTo("Test Sko");
    }

    @Test
    void shouldClearCartInSession() {
        // Hämta en varukorg (skapas automatiskt)
        cartService.getCart(session);

        // Töm varukorgen via tjänsten
        cartService.clearCart(session);

        // Verifiera att varukorgen är tom
        assertThat(cartService.isCartEmpty(session)).isTrue();
    }
}
