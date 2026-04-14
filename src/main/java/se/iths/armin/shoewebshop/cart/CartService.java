package se.iths.armin.shoewebshop.cart;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import se.iths.armin.shoewebshop.entity.Product;


@Service
public class CartService {


    private Cart getOrCreateCart(HttpSession session) {
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) {
            cart = new Cart();
            session.setAttribute("cart", cart);
        }
        return cart;
    }


    public void addProduct(HttpSession session, Product product) {
        Cart cart = getOrCreateCart(session);
        cart.addProduct(product);
    }

    public void removeProduct(HttpSession session, Product product) {
        Cart cart = getOrCreateCart(session);
        cart.removeProduct(product);
    }

    public Cart getCart(HttpSession session) {
        return getOrCreateCart(session);

    }

    public void clearCart(HttpSession session) {
        Cart cart = getOrCreateCart(session);
        cart.clearCart();
    }

    public boolean cartIsEmpty(HttpSession session) {
        Cart cart = getOrCreateCart(session);
        return cart.isEmpty();
    }


}
