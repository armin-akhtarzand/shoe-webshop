package se.iths.armin.shoewebshop.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.iths.armin.mailservice.MailService;
import se.iths.armin.shoewebshop.entity.*;
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

        Product product = new Product();
        product.setProductName("Test Shoe");
        product.setPrice(BigDecimal.valueOf(100));

        Cart cart = new Cart();
        cart.addProduct(product);

        CustomerOrder savedOrder = new CustomerOrder();
        savedOrder.setId(1L);
        savedOrder.setUsername(username);

        OrderItem item = new OrderItem();
        item.setProductName("Test Shoe");
        item.setQuantity(1);

        savedOrder.setItems(List.of(item));

        when(orderRepository.save(any())).thenReturn(savedOrder);

        CustomerOrder result = orderService.checkout(username, cart);

        assertEquals(1L, result.getId());
        assertEquals(username, result.getUsername());
        assertEquals(1, result.getItems().size());
        assertEquals("Test Shoe", result.getItems().get(0).getProductName());

        assertTrue(cart.isEmpty());

        verify(orderRepository).save(any(CustomerOrder.class));
        verify(mailService).sendMail(eq(username), eq("Order Confirmation"), anyString());
    }

    @Test
    void getOrdersForUser_shouldReturnOrders() {

        String username = "test@example.com";

        when(orderRepository.findByUsername(username))
                .thenReturn(List.of(new CustomerOrder(), new CustomerOrder()));

        List<CustomerOrder> result = orderService.getOrdersForUser(username);

        assertEquals(2, result.size());
        verify(orderRepository).findByUsername(username);
    }
}