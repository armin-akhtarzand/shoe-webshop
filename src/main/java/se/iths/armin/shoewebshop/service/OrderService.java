package se.iths.armin.shoewebshop.service;

import jakarta.persistence.criteria.Order;
import org.springframework.stereotype.Service;
import se.iths.armin.shoewebshop.repository.OrderRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order createOrder(String username, List<OrderItem> items, double total) {
        Order order = new Order();
        order.setUsername(username);
        order.setItems(items);
        order.setTotalPrice(total);
        order.setOrderDate(LocalDateTime.now());

        return orderRepository.save(order);
    }

    public List<Order> getOrdersForUser(String username) {
        return orderRepository.findByUsername(username);
    }
}
