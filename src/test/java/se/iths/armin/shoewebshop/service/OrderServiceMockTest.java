package se.iths.armin.shoewebshop.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import se.iths.armin.mailservice.MailService;
import se.iths.armin.shoewebshop.entity.*;
import se.iths.armin.shoewebshop.repository.OrderRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
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

        Cart cart = new Cart();


        OrderItem orderItem = new OrderItem();
        orderItem.setProductName("Test Shoe");
        orderItem.setQuantity(1);
        orderItem.setPrice(BigDecimal.valueOf(100.00));

        List<OrderItem> orderItems = new ArrayList<>();
        orderItems.add(orderItem);

        CustomerOrder savedOrder = new CustomerOrder();
        savedOrder.setId(1L);
        savedOrder.setUsername(username);
        savedOrder.setItems(orderItems);


        when(orderRepository.save(any(CustomerOrder.class)))
                .thenReturn(savedOrder);

        CustomerOrder result = orderService.checkout(username, cart);

        assertEquals(1L, result.getId());
        assertEquals(username, result.getUsername());

        assertEquals(1, result.getItems().size());
        assertEquals("Test Shoe", result.getItems().get(0).getProductName());
        assertEquals(1, result.getItems().get(0).getQuantity());

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

    private CustomerOrder createOrder(String username) {
        CustomerOrder order = new CustomerOrder();
        order.setUsername(username);
        order.setTotalPrice(BigDecimal.valueOf(120.00));
        return order;
    }
}