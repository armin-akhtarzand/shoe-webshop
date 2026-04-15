package se.iths.armin.shoewebshop.service;

import jakarta.persistence.criteria.Order;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;
import se.iths.armin.shoewebshop.entity.Cart;
import se.iths.armin.shoewebshop.entity.CartItem;
import se.iths.armin.shoewebshop.entity.CustomerOrder;
import se.iths.armin.shoewebshop.entity.OrderItem;
import se.iths.armin.shoewebshop.repository.OrderRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public CustomerOrder checkout(String username, Cart cart){
        List<OrderItem> orderItems = new ArrayList<>();

        for(CartItem cartItem : cart.getCartItems()){
            OrderItem orderItem = new OrderItem();
            orderItem.setProductName(cartItem.getProduct().getProductName());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getPrice().doubleValue());

            orderItems.add(orderItem);
        }
        CustomerOrder customerOrder = new CustomerOrder();
        customerOrder.setUsername(username);
        customerOrder.setOrderDate(LocalDateTime.now());
        customerOrder.setTotalPrice(cart.getTotalPrice().doubleValue());
        customerOrder.setItems(orderItems);

        CustomerOrder savedOrder = orderRepository.save(customerOrder);

        cart.clearCart();

        return savedOrder;
    }

    public List<CustomerOrder> getOrdersForUser(String username){
        return orderRepository.findByUsername(username);
    }
}
