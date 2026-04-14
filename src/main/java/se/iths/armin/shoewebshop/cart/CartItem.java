package se.iths.armin.shoewebshop.cart;


import se.iths.armin.shoewebshop.entity.Product;

import java.math.BigDecimal;

public class CartItem {

    private Product product;
    private int quantity;
    private BigDecimal price;

    public CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
        this.price = product.getPrice();
    }
}
