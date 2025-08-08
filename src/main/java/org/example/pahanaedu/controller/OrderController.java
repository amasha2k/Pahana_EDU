package org.example.pahanaedu.controller;

import org.example.pahanaedu.Routes;
import org.example.pahanaedu.dto.OrderRequest;
import org.example.pahanaedu.dto.OrderResponse;
import org.example.pahanaedu.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.List;

@CrossOrigin(origins = "http://localhost:14192")
@RestController
@RequestMapping(Routes.ORDER_BASE)
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody OrderRequest request) {
        OrderResponse order = orderService.createOrder(request);
        if (order == null) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Invalid customer or items"));
        }
        return ResponseEntity.ok(order);
    }

    @PutMapping(Routes.UPDATE_ORDER)
    public ResponseEntity<?> updateOrder(@PathVariable Long id, @RequestBody OrderRequest request) {
        OrderResponse order = orderService.updateOrder(id, request);
        if (order == null) {
            return ResponseEntity.status(404).body(Map.of("success", false, "message", "Order, customer, or items not found"));
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
        List<OrderResponse> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    @GetMapping(Routes.GETORDERBYID_ORDER)
    public ResponseEntity<?> getOrder(@PathVariable Long id) {
        OrderResponse order = orderService.getOrder(id);
        if (order == null) {
            return ResponseEntity.status(404).body(Map.of("success", false, "message", "Order not found"));
        }
        return ResponseEntity.ok(order);
    }
} 