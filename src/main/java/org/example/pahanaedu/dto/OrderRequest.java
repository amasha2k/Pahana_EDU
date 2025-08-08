package org.example.pahanaedu.dto;

import java.time.LocalDateTime;
import java.util.List;

public class OrderRequest {
    private Long customerId;
    private String orderDate;
    private List<OrderItemRequest> items;

    // Getters and setters
    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }
    public String getOrderDate() { return orderDate; }
    public void setOrderDate(String orderDate) { this.orderDate = orderDate; }
    public List<OrderItemRequest> getItems() { return items; }
    public void setItems(List<OrderItemRequest> items) { this.items = items; }
} 