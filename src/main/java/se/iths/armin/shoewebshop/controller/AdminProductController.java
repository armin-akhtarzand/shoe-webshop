package se.iths.armin.shoewebshop.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import se.iths.armin.shoewebshop.entity.Product;
import se.iths.armin.shoewebshop.service.ProductService;

@Controller
@RequestMapping("/admin/products")
@PreAuthorize("hasRole('ADMIN')")
public class AdminProductController {
    private final ProductService productService;

    public AdminProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/new")
    public String showCreateForm(Model model){
        model.addAttribute("product", new Product());
        return "create-product";
    }

    @PostMapping
    public String createProduct(@ModelAttribute Product product){
        productService.createProduct(product);
        return "redirect:/products";
    }
}
