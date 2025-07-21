package org.example.pahanaedu.controller;

import org.example.pahanaedu.Routes;
import org.example.pahanaedu.dto.OrderRequest;
import org.example.pahanaedu.entity.Order;
import org.example.pahanaedu.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(Routes.ORDER_BASE)
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody OrderRequest request) {
        Order order = orderService.createOrder(request);
        if (order == null) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Invalid customer or item"));
        }
        return ResponseEntity.ok(order);
    }

    @PutMapping(Routes.UPDATE_ORDER)
    public ResponseEntity<?> updateOrder(@PathVariable Long id, @RequestBody OrderRequest request) {
        Order order = orderService.updateOrder(id, request);
        if (order == null) {
            return ResponseEntity.status(404).body(Map.of("success", false, "message", "Order, customer, or item not found"));
        }
        return ResponseEntity.ok(order);
    }

    @DeleteMapping(Routes.DELETE_ORDER)
    public ResponseEntity<?> deleteOrder(@PathVariable Long id) {
        boolean deleted = orderService.deleteOrder(id);
        if (deleted) {
            return ResponseEntity.ok(Map.of("success", true, "message", "Order deleted successfully"));
        } else {
            return ResponseEntity.status(404).body(Map.of("success", false, "message", "Order not found"));
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping(Routes.GETORDERBYID_ORDER)
    public ResponseEntity<?> getOrder(@PathVariable Long id) {
        Order order = orderService.getOrder(id);
        if (order == null) {
            return ResponseEntity.status(404).body(Map.of("success", false, "message", "Order not found"));
        }
        return ResponseEntity.ok(order);
    }
} 