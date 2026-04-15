package se.iths.armin.shoewebshop.repository;

import jakarta.persistence.criteria.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import se.iths.armin.shoewebshop.entity.CustomerOrder;

import java.util.List;

public interface OrderRepository extends JpaRepository<CustomerOrder, Long> {
    List<CustomerOrder> findByUsername(String username);
}
