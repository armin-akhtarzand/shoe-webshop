package se.iths.armin.shoewebshop.service;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import se.iths.armin.shoewebshop.entity.Cart;
import se.iths.armin.shoewebshop.entity.Product;


@Service
public class CartService {

    private final ProductService productService;

    public CartService(ProductService productService) {
        this.productService = productService;
    }


    private Cart getOrCreateCart(HttpSession session) {
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) {
            cart = new Cart();
            session.setAttribute("cart", cart);
        }
        return cart;
    }


    public void addProduct(HttpSession session, Long id) {
        Cart cart = getOrCreateCart(session);
        Product product = productService.findById(id);
        cart.addProduct(product);
    }

    public void removeProduct(HttpSession session, Long id) {
        Cart cart = getOrCreateCart(session);
        Product product = productService.findById(id);
        cart.removeProduct(product);
    }

    public Cart getCart(HttpSession session) {
        return getOrCreateCart(session);

    }

    public void clearCart(HttpSession session) {
        Cart cart = getOrCreateCart(session);
        cart.clearCart();
    }

    public boolean isCartEmpty(HttpSession session) {
        Cart cart = getOrCreateCart(session);
        return cart.isEmpty();
    }
}
