package se.iths.armin.shoewebshop.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


public class Cart {

    private final List<CartItem> cartItems = new ArrayList<>();


    public void addProduct(Product product) {

        for (CartItem cartItem : cartItems) {
            if (product.getProductId().equals(cartItem.getProduct().getProductId())) {
                cartItem.increaseQuantity(1);
                return;
            }
        }
        cartItems.add(new CartItem(product, 1));
    }

    public void removeProduct(Product product) {

        for (int i = 0; i < cartItems.size(); i++) {
            CartItem cartItem = cartItems.get(i);

            if (product.getProductId().equals(cartItem.getProduct().getProductId())) {

                cartItem.decreaseQuantity(1);

                if (cartItem.getQuantity() <= 0) {
                    cartItems.remove(i);
                }
                return;
            }
        }
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public BigDecimal getTotalPrice() {
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (CartItem cartItem : cartItems) {
            totalPrice = totalPrice.add(cartItem.getTotalPrice());
        }
        return totalPrice;
    }

    public void clearCart() {
        cartItems.clear();
    }

    public boolean isEmpty() {
        return cartItems.isEmpty();
    }


}
