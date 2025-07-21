package org.example.pahanaedu.service;

import org.example.pahanaedu.dto.OrderRequest;
import org.example.pahanaedu.entity.Customer;
import org.example.pahanaedu.entity.Item;
import org.example.pahanaedu.entity.Order;
import org.example.pahanaedu.repository.CustomerRepository;
import org.example.pahanaedu.repository.ItemRepository;
import org.example.pahanaedu.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ItemRepository itemRepository;

    public Order createOrder(OrderRequest request) {
        Customer customer = customerRepository.findById(request.getCustomerId()).orElse(null);
        Item item = itemRepository.findById(request.getItemId()).orElse(null);
        if (customer == null || item == null) return null;
        Order order = new Order();
        order.setCustomer(customer);
        order.setItem(item);
        order.setQuantity(request.getQuantity());
        order.setPrice(request.getPrice());
        order.setDiscount(request.getDiscount());
        order.setOrderDate(request.getOrderDate());
        // Calculate total: (price * qty) - discount
        BigDecimal total = request.getPrice().multiply(BigDecimal.valueOf(request.getQuantity()));
        if (request.getDiscount() != null) {
            total = total.subtract(request.getDiscount());
        }
        order.setTotal(total);
        return orderRepository.save(order);
    }

    public Order updateOrder(Long id, OrderRequest request) {
        Order order = orderRepository.findById(id).orElse(null);
        if (order == null) return null;
        Customer customer = customerRepository.findById(request.getCustomerId()).orElse(null);
        Item item = itemRepository.findById(request.getItemId()).orElse(null);
        if (customer == null || item == null) return null;
        order.setCustomer(customer);
        order.setItem(item);
        order.setQuantity(request.getQuantity());
        order.setPrice(request.getPrice());
        order.setDiscount(request.getDiscount());
        order.setOrderDate(request.getOrderDate());
        // Calculate total: (price * qty) - discount
        BigDecimal total = request.getPrice().multiply(BigDecimal.valueOf(request.getQuantity()));
        if (request.getDiscount() != null) {
            total = total.subtract(request.getDiscount());
        }
        order.setTotal(total);
        return orderRepository.save(order);
    }

    public boolean deleteOrder(Long id) {
        if (!orderRepository.existsById(id)) {
            return false;
        }
        orderRepository.deleteById(id);
        return true;
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order getOrder(Long id) {
        return orderRepository.findById(id).orElse(null);
    }
} 