package org.example.pahanaedu.repository;

import org.example.pahanaedu.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Customer findByAccountNumber(String accountNumber);
    Customer findTopByOrderByIdDesc();
}

