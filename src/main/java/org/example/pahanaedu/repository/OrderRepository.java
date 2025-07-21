package org.example.pahanaedu.repository;

import org.example.pahanaedu.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
} 