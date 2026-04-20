package se.iths.armin.shoewebshop.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.iths.armin.shoewebshop.entity.Cart;
import se.iths.armin.shoewebshop.entity.CustomerOrder;
import se.iths.armin.shoewebshop.entity.Product;
import se.iths.armin.shoewebshop.repository.OrderRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderServiceMockTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    @Test
    void checkout_shoudlCreateOrderAndClearCart() {
        String username = "test@example.com";
        Cart cart = createTestCart();

        CustomerOrder savedOrder = new  CustomerOrder();
        savedOrder.setId(1L);
        savedOrder.setUsername(username);
        savedOrder.setOrderDate(LocalDateTime.now());
        savedOrder.setTotalPrice(cart.getTotalPrice().doubleValue());

        when(orderRepository.save(any(CustomerOrder.class))).thenReturn(savedOrder);

        CustomerOrder result = orderService.checkout(username, cart);

        assertEquals(1L, result.getId());
        assertEquals(username, result.getUsername());
        assertEquals(1, result.getItems().size());
        assertEquals(2, result.getItems().get(0).getQuantity());
        assertTrue(cart.isEmpty());
    }

    @Test
    void getOrdersForUSer_shouldReturnUserOrders() {
        String username = "test@example.com";
        List<CustomerOrder> orders = Arrays.asList(
                createTestOrder(username),
                createTestOrder(username)
        );
        when(orderRepository.findByUsername(username)).thenReturn(orders);

        List<CustomerOrder> result = orderService.getOrdersForUser(username);

        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(order -> username.equals(order.getUsername())));
    }

    private Cart createTestCart() {
        Cart cart = new Cart();
        Product testProduct = createTestProduct();
        cart.addProduct(testProduct);
        cart.addProduct(testProduct);
        return cart;
    }

    private Product createTestProduct() {
        Product product = new Product();
        product.setProductName("Test Shoe");
        product.setCategory("Sneakers");
        product.setPrice(new BigDecimal("60.00"));
        product.setProductImageURL("http://example.com/test.jpg");
        return product;
    }

    private CustomerOrder createTestOrder(String username) {
        CustomerOrder order = new CustomerOrder();
        order.setUsername(username);
        order.setOrderDate(LocalDateTime.now());
        order.setTotalPrice(120.00);
        return order;
    }


}
