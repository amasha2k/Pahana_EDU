package org.example.pahanaedu.repository;

import org.example.pahanaedu.entity.Order;
import org.example.pahanaedu.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findByOrder(Order order);
    void deleteByOrder(Order order);
} 