package se.iths.armin.shoewebshop.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CartItemTest {

    private Product product;
    private CartItem cartItem;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setProductId(1L);
        product.setProductName("Test Shoe");
        product.setPrice(new BigDecimal("100.00"));

        // Sätter produktens antal i kundvagnen till 2
        cartItem = new CartItem(product, 2);
    }

    @Test
    void constructorShouldSetInitialQuantity() {
        assertEquals(2, cartItem.getQuantity());

        //korrekt antal av produkt vi satt i SetUp
    }

    @Test
    void snapshotPriceTest() {
        // Priset ska sättas vid skapande
        BigDecimal priceAtCreation = cartItem.getPrice();
        assertEquals(new BigDecimal("100.00"), priceAtCreation);

        // Om produktens pris ändras efteråt ska CartItem behålla det gamla priset
        product.setPrice(new BigDecimal("150.00"));
        assertEquals(new BigDecimal("100.00"), cartItem.getPrice());


    }

    @Test
    void increaseQuantityShouldIncreaseCount() {
        cartItem.increaseQuantity(1);
        assertEquals(3, cartItem.getQuantity());
    }

    @Test
    void decreaseQuantityShouldDecreaseCount() {
        cartItem.decreaseQuantity(1);
        assertEquals(1, cartItem.getQuantity());
    }

    @Test
    void totalPriceShouldBeCalculatedCorrectly() {
        // 100 * 2 = 200 (price * quantity = totalPrice)
        assertEquals(new BigDecimal("200.00"), cartItem.getTotalPrice());
    }
}
