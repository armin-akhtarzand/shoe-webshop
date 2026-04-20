package se.iths.armin.shoewebshop.service;

import org.springframework.stereotype.Service;
import se.iths.armin.mailservice.MailService;
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
    private final MailService mailService;

    public OrderService(OrderRepository orderRepository, MailService mailService) {
        this.orderRepository = orderRepository;
        this.mailService = mailService;
    }

    public CustomerOrder checkout(String username, Cart cart) {
        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem cartItem : cart.getCartItems()) {
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
        StringBuilder orderConfirmation = new StringBuilder();
        orderConfirmation.append("Thank you for your order\n\n");
        orderConfirmation.append("Order date: ").append(savedOrder.getOrderDate()).append("\n");
        orderConfirmation.append("Products:\n");

        for (OrderItem item : savedOrder.getItems()) {
            orderConfirmation
                    .append("- ")
                    .append(item.getProductName())
                    .append(" x")
                    .append(item.getQuantity())
                    .append(" - ")
                    .append(item.getPrice())
                    .append(" kr\n");
        }

        orderConfirmation.append("\nTotalpris: ")
                .append(savedOrder.getTotalPrice())
                .append(" kr");

        mailService.sendMail(username, "Order Confirmation", orderConfirmation.toString());


        cart.clearCart();

        return savedOrder;
    }

    public List<CustomerOrder> getOrdersForUser(String username) {
        return orderRepository.findByUsername(username);
    }
}
