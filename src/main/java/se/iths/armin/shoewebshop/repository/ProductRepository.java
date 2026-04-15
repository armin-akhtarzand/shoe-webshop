package se.iths.armin.shoewebshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.iths.armin.shoewebshop.entity.Product;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByCategory(String category);

    List<Product> findByProductName(String productName);
}