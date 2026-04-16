package se.iths.armin.shoewebshop.service;

import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.iths.armin.shoewebshop.entity.Cart;
import se.iths.armin.shoewebshop.entity.Product;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    private ProductService productService;

    @Mock
    private HttpSession session;

    @InjectMocks
    private CartService cartService;

    private Product testProduct;

    @BeforeEach
    void setUp() {
        testProduct = new Product();
        testProduct.setProductId(1L);
        testProduct.setProductName("Test Shoe");
        testProduct.setPrice(new BigDecimal("100.00"));
    }

    @Test
    void getCartShouldCreateNewCartIfNoneExists() {
        when(session.getAttribute("cart")).thenReturn(null);
        Cart cart = cartService.getCart(session);
        assertNotNull(cart);
        verify(session).setAttribute(eq("cart"), any(Cart.class));
    }

    @Test
    void addProductShouldIncreaseQuantityWhenProductAlreadyExists() {
        Cart cart = new Cart();
        cart.addProduct(testProduct); // Lägg till första gången
        when(session.getAttribute("cart")).thenReturn(cart);
        when(productService.findById(1L)).thenReturn(testProduct);

        cartService.addProduct(session, 1L); // Lägg till andra gången

        assertEquals(1, cart.getCartItems().size());
        assertEquals(2, cart.getCartItems().get(0).getQuantity());
        verify(productService).findById(1L); // Verifiera att service användes
    }

    @Test
    void removeProductShouldDecreaseQuantityCorrectly() {
        Cart cart = new Cart();
        cart.addProduct(testProduct);
        cart.addProduct(testProduct); // Quantity 2
        when(session.getAttribute("cart")).thenReturn(cart);
        when(productService.findById(1L)).thenReturn(testProduct);

        cartService.removeProduct(session, 1L);

        assertEquals(1, cart.getCartItems().get(0).getQuantity());
        verify(productService).findById(1L);
    }

    @Test
    void removeProductShouldRemoveItemWhenQuantityReachesZero() {
        Cart cart = new Cart();
        cart.addProduct(testProduct); // Quantity 1
        when(session.getAttribute("cart")).thenReturn(cart);
        when(productService.findById(1L)).thenReturn(testProduct);

        cartService.removeProduct(session, 1L);

        assertTrue(cart.isEmpty());
    }

    @Test
    void removeProductShouldDoNothingWhenProductNotFound() {
        // findById kastar exception vid felaktig ID
        when(productService.findById(99L)).thenThrow(new RuntimeException("Not found"));

        assertThrows(RuntimeException.class, () -> {
            cartService.removeProduct(session, 99L);
        });
    }

    @Test
    void isCartEmptyShouldReturnCorrectStatus() {
        Cart emptyCart = new Cart();
        Cart fullCart = new Cart();
        fullCart.addProduct(testProduct);

        // Testa tom
        when(session.getAttribute("cart")).thenReturn(emptyCart);
        assertTrue(cartService.isCartEmpty(session));

        // Testa full
        when(session.getAttribute("cart")).thenReturn(fullCart);
        assertFalse(cartService.isCartEmpty(session));
    }

    @Test
    void clearCartShouldEmptyTheCart() {
        Cart cart = new Cart();
        cart.addProduct(testProduct);
        when(session.getAttribute("cart")).thenReturn(cart);

        cartService.clearCart(session);

        assertTrue(cart.isEmpty());
    }
}
