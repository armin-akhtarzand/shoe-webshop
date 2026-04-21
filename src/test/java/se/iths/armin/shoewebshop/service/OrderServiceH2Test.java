package se.iths.armin.shoewebshop.service;

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
public class OrderServiceH2Test {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductService productService;

    @Autowired
    private AppUserService appUserService;

    @Test
    void checkout_shouldCreateOrderAndClearCart(){
        UserRegistrationDto userDto = new UserRegistrationDto();
        appUserService.registerUser(userDto);

        Product product = createTestProduct();
        productService.createProduct(product);

        Cart cart = new Cart();
        cart.addProduct(product);
        cart.addProduct(product);

        CustomerOrder order = orderService.checkout(userDto.getEmail(), cart);

        assertNotNull(order.getId());
        assertEquals(userDto.getEmail(), order.getUsername());
        assertEquals(1, order.getItems().size());
        assertEquals(2, order.getItems().get(0).getQuantity());
        assertEquals(product.getPrice().multiply(BigDecimal.valueOf(2)).doubleValue(), order.getTotalPrice());
        assertTrue(cart.isEmpty());
    }

    @Test
    void getOrdersForUser_shouldReturnOrders() {
        UserRegistrationDto userDto = createTestUserDto();
        appUserService.registerUser(userDto);

        Product product = createTestProduct();
        productService.createProduct(product);

        Cart cart = new Cart();
        cart.addProduct(product);

        CustomerOrder order = orderService.checkout(userDto.getEmail(), cart);

        List<CustomerOrder> userOrders = orderService.getOrdersForUser(userDto.getEmail());

        assertTrue(userOrders.size() >= 1);
        assertTrue(userOrders.stream().anyMatch(o -> userDto.getEmail().equals(o.getUsername())));
    }

    private UserRegistrationDto createTestUserDto() {
        UserRegistrationDto dto = new UserRegistrationDto();
        dto.setEmail("test@example.com");
        dto.setPassword("password");
        dto.setConsent(true);
        return dto;
    }

    private Product createTestProduct() {
        Product product = new Product();
        product.setProductName("Test Shoe");
        product.setCategory("Sneakers");
        product.setPrice(new BigDecimal("99.99"));
        product.setProductImageURL("http://example.com/image.jpg");
        return product;
    }
}
