package org.example.pahanaedu.service;

import org.example.pahanaedu.dto.OrderRequest;
import org.example.pahanaedu.dto.OrderResponse;
import org.example.pahanaedu.dto.OrderItemResponse;
import org.example.pahanaedu.entity.Customer;
import org.example.pahanaedu.entity.Item;
import org.example.pahanaedu.entity.Order;
import org.example.pahanaedu.entity.OrderItem;
import org.example.pahanaedu.repository.CustomerRepository;
import org.example.pahanaedu.repository.ItemRepository;
import org.example.pahanaedu.repository.OrderRepository;
import org.example.pahanaedu.repository.OrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;

    public OrderResponse createOrder(OrderRequest request) {
        Customer customer = customerRepository.findById(request.getCustomerId()).orElse(null);
        if (customer == null) return null;
        
        Order order = new Order();
        order.setCustomer(customer);
        
        // Parse order date
        LocalDateTime orderDate = LocalDateTime.now(); // default to current time
        if (request.getOrderDate() != null && !request.getOrderDate().isEmpty()) {
            try {
                // Try different date formats
                DateTimeFormatter[] formatters = {
                    DateTimeFormatter.ISO_LOCAL_DATE_TIME,
                    DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd")
                };
                
                for (DateTimeFormatter formatter : formatters) {
                    try {
                        if (request.getOrderDate().contains("T")) {
                            orderDate = LocalDateTime.parse(request.getOrderDate(), formatter);
                        } else {
                            orderDate = LocalDateTime.parse(request.getOrderDate() + "T00:00:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                        }
                        break;
                    } catch (Exception e) {
                        continue;
                    }
                }
            } catch (Exception e) {
                // If parsing fails, use current time
                orderDate = LocalDateTime.now();
            }
        }
        order.setOrderDate(orderDate);
        
        // Save the order first to get the ID
        order = orderRepository.save(order);
        
        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal orderTotal = BigDecimal.ZERO;
        
        // Process each item in the order
        for (var itemRequest : request.getItems()) {
             Item item = itemRepository.findById(itemRequest.getItemId()).orElse(null);
            if (item == null) continue; // Skip invalid items
            
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setItem(item);
            orderItem.setQuantity(itemRequest.getQuantity());
            orderItem.setPrice(itemRequest.getPrice());
            orderItem.setDiscount(itemRequest.getDiscount() != null ? itemRequest.getDiscount() : BigDecimal.ZERO);
            
            // Calculate item total: (price * qty) - discount
            BigDecimal itemTotal = itemRequest.getPrice().multiply(BigDecimal.valueOf(itemRequest.getQuantity()));
            if (itemRequest.getDiscount() != null) {
                itemTotal = itemTotal.subtract(itemRequest.getDiscount());
            }
            orderItem.setTotal(itemTotal);
            
            orderItems.add(orderItem);
            orderTotal = orderTotal.add(itemTotal);
        }
        
        // If no valid items were found, return null
        if (orderItems.isEmpty()) {
            return null;
        }
        
        // Save all order items
        orderItemRepository.saveAll(orderItems);
        
        // Set the order total and save
        order.setTotal(orderTotal);
        order.setOrderItems(orderItems);
        
        order = orderRepository.save(order);
        
        return convertToOrderResponse(order);
    }

    public OrderResponse updateOrder(Long id, OrderRequest request) {
        Order order = orderRepository.findById(id).orElse(null);
        if (order == null) return null;
        
        Customer customer = customerRepository.findById(request.getCustomerId()).orElse(null);
        if (customer == null) return null;
        
        order.setCustomer(customer);
        
        // Parse order date
        LocalDateTime orderDate = LocalDateTime.now();
        if (request.getOrderDate() != null && !request.getOrderDate().isEmpty()) {
            try {
                DateTimeFormatter[] formatters = {
                    DateTimeFormatter.ISO_LOCAL_DATE_TIME,
                    DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd")
                };
                
                for (DateTimeFormatter formatter : formatters) {
                    try {
                        if (request.getOrderDate().contains("T")) {
                            orderDate = LocalDateTime.parse(request.getOrderDate(), formatter);
                        } else {
                            orderDate = LocalDateTime.parse(request.getOrderDate() + "T00:00:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                        }
                        break;
                    } catch (Exception e) {
                        continue;
                    }
                }
            } catch (Exception e) {
                orderDate = LocalDateTime.now();
            }
        }
        order.setOrderDate(orderDate);
        
        // Delete existing order items
        orderItemRepository.deleteByOrder(order);
        
        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal orderTotal = BigDecimal.ZERO;
        
        // Process each item in the order
        for (var itemRequest : request.getItems()) {
            Item item = itemRepository.findById(itemRequest.getItemId()).orElse(null);
            if (item == null) continue;
            
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setItem(item);
            orderItem.setQuantity(itemRequest.getQuantity());
            orderItem.setPrice(itemRequest.getPrice());
            orderItem.setDiscount(itemRequest.getDiscount() != null ? itemRequest.getDiscount() : BigDecimal.ZERO);
            
            // Calculate item total
            BigDecimal itemTotal = itemRequest.getPrice().multiply(BigDecimal.valueOf(itemRequest.getQuantity()));
            if (itemRequest.getDiscount() != null) {
                itemTotal = itemTotal.subtract(itemRequest.getDiscount());
            }
            orderItem.setTotal(itemTotal);
            
            orderItems.add(orderItem);
            orderTotal = orderTotal.add(itemTotal);
        }
        
        // If no valid items were found, return null
        if (orderItems.isEmpty()) {
            return null;
        }
        
        // Save all order items
        orderItemRepository.saveAll(orderItems);
        
        // Set the order total and save
        order.setTotal(orderTotal);
        order.setOrderItems(orderItems);
        
        order = orderRepository.save(order);
        
        return convertToOrderResponse(order);
    }

    public boolean deleteOrder(Long id) {
        Order order = orderRepository.findById(id).orElse(null);
        if (order == null) {
            return false;
        }
        
        // Delete order items first to avoid foreign key constraint violations
        orderItemRepository.deleteByOrder(order);
        
        // Then delete the order
        orderRepository.deleteById(id);
        return true;
    }

    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::convertToOrderResponse)
                .collect(Collectors.toList());
    }

    public OrderResponse getOrder(Long id) {
        Order order = orderRepository.findById(id).orElse(null);
        if (order == null) return null;
        return convertToOrderResponse(order);
    }

    private OrderResponse convertToOrderResponse(Order order) {
        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setCustomerId(order.getCustomer().getId());
        response.setCustomerName(order.getCustomer().getName());
        response.setOrderDate(order.getOrderDate());
        response.setTotal(order.getTotal());
        
        if (order.getOrderItems() != null) {
            List<OrderItemResponse> itemResponses = order.getOrderItems().stream()
                    .map(this::convertToOrderItemResponse)
                    .collect(Collectors.toList());
            response.setItems(itemResponses);
        }
        
        return response;
    }

    private OrderItemResponse convertToOrderItemResponse(OrderItem orderItem) {
        OrderItemResponse response = new OrderItemResponse();
        response.setId(orderItem.getId());
        response.setItemId(orderItem.getItem().getId());
        response.setItemName(orderItem.getItem().getName());
        response.setQuantity(orderItem.getQuantity());
        response.setPrice(orderItem.getPrice());
        response.setDiscount(orderItem.getDiscount());
        response.setTotal(orderItem.getTotal());
        return response;
    }
} 