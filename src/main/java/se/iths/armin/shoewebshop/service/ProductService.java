package se.iths.armin.shoewebshop.service;

import org.springframework.stereotype.Service;
import se.iths.armin.shoewebshop.entity.Product;
import se.iths.armin.shoewebshop.repository.ProductRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository)
    {
        this.productRepository =  productRepository;
    }
    public Product createProduct(Product product)
    {
        if (product == null){
            throw new IllegalArgumentException("Product cannot be null");
        }
        try {
            return productRepository.save(product);
        } catch (Exception e) {
            throw new RuntimeException("Failed to save product to database", e);
        }
    }

    public List<Product> getAllProducts()
    {
        return productRepository.findAll();
    }

    public Product findById(Long id)
    {
        if(id == null || id <= 0){
            throw new IllegalArgumentException("Invalid product ID");
        }
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product with ID: " + id + " not found"));
    }

    public List<Product> getProductsByCategory(String category)
    {
        if(category == null || category.isBlank()){
            throw new IllegalArgumentException("Category cannot be null or empty");
        }
        return productRepository.findByCategory(category);
    }
}
