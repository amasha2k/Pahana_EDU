package org.example.pahanaedu.dto;

import java.math.BigDecimal;

public class OrderItemRequest {
    private Long itemId;
    private Integer quantity;
    private BigDecimal price;
    private BigDecimal discount;

    // Getters and setters
    public Long getItemId() { return itemId; }
    public void setItemId(Long itemId) { this.itemId = itemId; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public BigDecimal getDiscount() { return discount; }
    public void setDiscount(BigDecimal discount) { this.discount = discount; }
} 