package se.iths.armin.shoewebshop.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class CartTest {

    private Cart cart;
    private Product p1;
    private Product p2;

    @BeforeEach
    void setUp() {
        cart = new Cart();
        
        p1 = new Product();
        p1.setProductId(1L);
        p1.setProductName("Shoe A");
        p1.setPrice(new BigDecimal("100.00"));

        p2 = new Product();
        p2.setProductId(2L);
        p2.setProductName("Shoe B");
        p2.setPrice(new BigDecimal("200.00"));
    }

    @Test
    void addProductToEmptyCartShouldAddOneItem() {
        cart.addProduct(p1);
        assertEquals(1, cart.getCartItems().size());
        assertEquals(1, cart.getCartItems().get(0).getQuantity());
    }

    @Test
    void addSameProductTwiceShouldIncreaseQuantity() {
        cart.addProduct(p1);
        cart.addProduct(p1);
        assertEquals(1, cart.getCartItems().size());
        assertEquals(2, cart.getCartItems().get(0).getQuantity());
    }

    @Test
    void removeProductShouldDecreaseQuantity() {
        cart.addProduct(p1);
        cart.addProduct(p1);
        cart.removeProduct(p1);
        assertEquals(1, cart.getCartItems().get(0).getQuantity());
    }

    @Test
    void removeProductWithQuantityOneShouldRemoveItem() {
        cart.addProduct(p1);
        cart.removeProduct(p1);
        assertTrue(cart.isEmpty());
    }

    @Test
    void shouldDoNothingWhenRemovingProductNotInCart() {
        cart.addProduct(p1);
        cart.removeProduct(p2); // p2 finns inte i cart
        assertEquals(1, cart.getCartItems().size());
        assertEquals(p1, cart.getCartItems().get(0).getProduct());
    }

    @Test
    void getTotalPriceWithMultipleQuantity() {
        cart.addProduct(p1); // 100
        cart.addProduct(p1); // 100 till = 200
        cart.addProduct(p2); // 200 = 400 totalt
        assertEquals(0, new BigDecimal("400.00").compareTo(cart.getTotalPrice()));
    }

    @Test
    void getTotalPriceEmptyCart() {
        assertEquals(BigDecimal.ZERO, cart.getTotalPrice());
    }

    @Test
    void clearCartShouldEmptyList() {
        cart.addProduct(p1);
        cart.clearCart();
        assertTrue(cart.isEmpty());
    }

    @Test
    void isEmptyShouldReturnCorrectBoolean() {
        assertTrue(cart.isEmpty());
        cart.addProduct(p1);
        assertFalse(cart.isEmpty());
    }
}
