package org.example.pahanaedu.service;

import org.example.pahanaedu.dto.CustomerRequest;
import org.example.pahanaedu.entity.Customer;
import org.example.pahanaedu.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    public Customer addCustomer(CustomerRequest request) {
        Customer customer = new Customer();
        customer.setAccountNumber(request.getAccountNumber());
        customer.setName(request.getName());
        customer.setAddress(request.getAddress());
        customer.setTelephone(request.getTelephone());
        customer.setUnitsConsumed(request.getUnitsConsumed());
        return customerRepository.save(customer);
    }

    public Customer editCustomer(Long id, CustomerRequest request) {
        Customer customer = customerRepository.findById(id).orElse(null);
        if (customer == null) return null;
        customer.setAccountNumber(request.getAccountNumber());
        customer.setName(request.getName());
        customer.setAddress(request.getAddress());
        customer.setTelephone(request.getTelephone());
        customer.setUnitsConsumed(request.getUnitsConsumed());
        return customerRepository.save(customer);
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public boolean deleteCustomer(Long id) {
        if (!customerRepository.existsById(id)) {
            return false;
        }
        customerRepository.deleteById(id);
        return true;
    }
    public String getlatestId() {
        Customer customer = customerRepository.findTopByOrderByIdDesc();
        return (customer != null) ? customer.getAccountNumber() : null;
    }

} 