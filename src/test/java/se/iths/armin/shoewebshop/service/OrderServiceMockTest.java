package se.iths.armin.shoewebshop.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.iths.armin.mailservice.MailService;
import se.iths.armin.shoewebshop.entity.Cart;
import se.iths.armin.shoewebshop.entity.CustomerOrder;
import se.iths.armin.shoewebshop.entity.Product;
import se.iths.armin.shoewebshop.repository.OrderRepository;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceMockTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private MailService mailService;

    @InjectMocks
    private OrderService orderService;

    @Test
    void checkout_shouldCreateOrderAndClearCart() {

        String username = "test@example.com";

        Cart cart = createCart();

        CustomerOrder savedOrder = new CustomerOrder();
        savedOrder.setId(1L);
        savedOrder.setUsername(username);
        savedOrder.setTotalPrice(120.0);

        when(orderRepository.save(any(CustomerOrder.class)))
                .thenReturn(savedOrder);

        CustomerOrder result = orderService.checkout(username, cart);

        assertEquals(1L, result.getId());
        assertEquals(username, result.getUsername());

        assertEquals(1, result.getItems().size());
        assertEquals("Test Shoe", result.getItems().get(0).getProductName());
        assertEquals(2, result.getItems().get(0).getQuantity());

        assertTrue(cart.isEmpty());

        verify(orderRepository, times(1))
                .save(any(CustomerOrder.class));

        verify(mailService, times(1))
                .sendMail(
                        eq(username),
                        eq("Order Confirmation"),
                        any(String.class)
                );
    }

    @Test
    void getOrdersForUser_shouldReturnOrders() {

        String username = "test@example.com";

        List<CustomerOrder> orders = List.of(
                createOrder(username),
                createOrder(username)
        );

        when(orderRepository.findByUsername(username))
                .thenReturn(orders);

        List<CustomerOrder> result =
                orderService.getOrdersForUser(username);

        assertEquals(2, result.size());
        assertTrue(result.stream()
                .allMatch(o -> o.getUsername().equals(username)));

        verify(orderRepository, times(1))
                .findByUsername(username);
    }

    private Cart createCart() {
        Cart cart = new Cart();

        Product product = new Product();
        product.setProductName("Test Shoe");
        product.setPrice(new BigDecimal("60.00"));
        product.setProductId(1L);

        cart.addProduct(product);
        cart.addProduct(product);

        return cart;
    }

    private CustomerOrder createOrder(String username) {
        CustomerOrder order = new CustomerOrder();
        order.setUsername(username);
        order.setTotalPrice(120.0);
        return order;
    }
}