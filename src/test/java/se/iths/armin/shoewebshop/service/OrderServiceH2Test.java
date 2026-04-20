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
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

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

        assertThat(order.getId()).isNotNull();
        assertThat(order.getUsername()).isEqualTo(userDto.getEmail());
        assertThat(order.getItems()).hasSize(1);
        assertThat(order.getItems().get(0).getQuantity()).isEqualTo(2);
        assertThat(order.getTotalPrice()).isEqualTo(product.getPrice().multiply(BigDecimal.valueOf(2)).doubleValue());
        assertThat(cart.isEmpty()).isTrue();
    }

    @Test
    void getOrdersForUser_shouldReturnOrders() {
        UserRegistrationDto userDto = new UserRegistrationDto();
        appUserService.registerUser(userDto);

        Product product = createTestProduct();
        productService.createProduct(product);

        Cart cart = new Cart();
        cart.addProduct(product);

        CustomerOrder order = orderService.checkout(userDto.getEmail(), cart);

        List<CustomerOrder> userOrders = orderService.getOrdersForUser(userDto.getEmail());

        assertThat(userOrders).hasSizeGreaterThanOrEqualTo(1);
        assertThat(userOrders).anyMatch(o -> o.getUsername().equals(userDto.getEmail()));
    }

    private UserRegistrationDto createTestUSerDto() {
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
