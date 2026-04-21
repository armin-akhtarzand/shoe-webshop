package se.iths.armin.shoewebshop.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String productName;

    @Min(1)
    private int quantity;

    @Positive
    private BigDecimal price;

    public OrderItem() {}

    public OrderItem(String productName, int quantity, BigDecimal price) {
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
    }
}
