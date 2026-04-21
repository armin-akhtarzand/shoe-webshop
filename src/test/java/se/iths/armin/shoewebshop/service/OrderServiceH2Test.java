package se.iths.armin.shoewebshop.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import se.iths.armin.shoewebshop.dto.UserRegistrationDto;
import se.iths.armin.shoewebshop.entity.Cart;
import se.iths.armin.shoewebshop.entity.CustomerOrder;
import se.iths.armin.shoewebshop.entity.Product;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class OrderServiceH2Test {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductService productService;

    @Autowired
    private AppUserService appUserService;

    private UserRegistrationDto user;
    private Product product;

    @BeforeEach
    void setUp() {
        user = new UserRegistrationDto();
        user.setEmail("test@example.com");
        user.setPassword("password123");
        user.setConsent(true);

        appUserService.registerUser(user);

        product = new Product();
        product.setProductName("Test Shoe");
        product.setCategory("Sneakers");
        product.setPrice(new BigDecimal("60.00"));
        product.setProductImageURL("http://image.com");

        productService.createProduct(product);
    }

    @Test
    void checkout_shouldCreateOrderAndClearCart() {

        Cart cart = new Cart();
        cart.addProduct(product);
        cart.addProduct(product);

        CustomerOrder order = orderService.checkout(user.getEmail(), cart);

        assertNotNull(order.getId());
        assertEquals(user.getEmail(), order.getUsername());
        assertEquals(1, order.getItems().size());
        assertEquals(2, order.getItems().get(0).getQuantity());

        BigDecimal expected = product.getPrice().multiply(BigDecimal.valueOf(2));

        assertEquals(0, expected.compareTo(
                BigDecimal.valueOf(order.getTotalPrice())
        ));

        assertTrue(cart.isEmpty());
    }

    @Test
    void getOrdersForUser_shouldReturnOrders() {

        Cart cart = new Cart();
        cart.addProduct(product);

        orderService.checkout(user.getEmail(), cart);

        List<CustomerOrder> orders = orderService.getOrdersForUser(user.getEmail());

        assertTrue(orders.size() >= 1);
        assertTrue(
                orders.stream().allMatch(o -> o.getUsername().equals(user.getEmail()))
        );
    }
}