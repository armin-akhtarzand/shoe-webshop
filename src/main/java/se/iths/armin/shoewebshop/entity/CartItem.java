package se.iths.armin.shoewebshop.entity;


import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class CartItem {

    private Product product;
    private int quantity;
    private BigDecimal price;

    public CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
        this.price = product.getPrice();
    }


    public BigDecimal getTotalPrice() {
        return price.multiply(BigDecimal.valueOf(quantity));
    }

    public void increaseQuantity(int amount) {
        this.quantity += amount;
    }

    public void decreaseQuantity(int amount) {
        this.quantity -= amount;
    }
}
