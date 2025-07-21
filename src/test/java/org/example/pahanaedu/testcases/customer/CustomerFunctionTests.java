package org.example.pahanaedu.testcases.customer;

import org.example.pahanaedu.dto.CustomerRequest;
import org.example.pahanaedu.entity.Customer;
import org.example.pahanaedu.repository.CustomerRepository;
import org.example.pahanaedu.repository.OrderRepository;
import org.example.pahanaedu.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CustomerFunctionTests implements CommandLineRunner {
    @Autowired
    private CustomerService customerService;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private OrderRepository orderRepository;

    @Override
    public void run(String... args) {
        System.out.println("\n--- Running Customer Function Tests ---");
        testAddCustomer();
        testEditCustomer();
        testGetAllCustomers();
        testDeleteCustomer();
        testEditCustomerNotFound();
        testDeleteCustomerNotFound();
        System.out.println("--- Customer Function Tests Complete ---\n");
    }

    private void testAddCustomer() {
        clearData();
        CustomerRequest req = new CustomerRequest();
        req.setAccountNumber("CUST001");
        req.setName("Alice");
        req.setAddress("Colombo");
        req.setTelephone("0771111111");
        req.setUnitsConsumed(10);
        Customer customer = customerService.addCustomer(req);
        System.out.println("testAddCustomer: " + (customer != null && customer.getId() != null ? "PASS" : "FAIL"));
    }

    private void testEditCustomer() {
        clearData();
        CustomerRequest req = new CustomerRequest();
        req.setAccountNumber("CUST002");
        req.setName("Bob");
        req.setAddress("Kandy");
        req.setTelephone("0772222222");
        req.setUnitsConsumed(5);
        Customer customer = customerService.addCustomer(req);
        req.setName("Bob Updated");
        Customer updated = customerService.editCustomer(customer.getId(), req);
        System.out.println("testEditCustomer: " + (updated != null && "Bob Updated".equals(updated.getName()) ? "PASS" : "FAIL"));
    }

    private void testGetAllCustomers() {
        clearData();
        CustomerRequest req = new CustomerRequest();
        req.setAccountNumber("CUST003");
        req.setName("Charlie");
        req.setAddress("Galle");
        req.setTelephone("0773333333");
        req.setUnitsConsumed(7);
        customerService.addCustomer(req);
        boolean pass = customerService.getAllCustomers().size() == 1;
        System.out.println("testGetAllCustomers: " + (pass ? "PASS" : "FAIL"));
    }

    private void testDeleteCustomer() {
        clearData();
        CustomerRequest req = new CustomerRequest();
        req.setAccountNumber("CUST004");
        req.setName("David");
        req.setAddress("Matara");
        req.setTelephone("0774444444");
        req.setUnitsConsumed(3);
        Customer customer = customerService.addCustomer(req);
        boolean deleted = customerService.deleteCustomer(customer.getId());
        System.out.println("testDeleteCustomer: " + (deleted ? "PASS" : "FAIL"));
    }

    private void testEditCustomerNotFound() {
        clearData();
        CustomerRequest req = new CustomerRequest();
        req.setAccountNumber("CUST005");
        req.setName("Eve");
        req.setAddress("Jaffna");
        req.setTelephone("0775555555");
        req.setUnitsConsumed(2);
        Customer updated = customerService.editCustomer(999L, req);
        System.out.println("testEditCustomerNotFound: " + (updated == null ? "PASS" : "FAIL"));
    }

    private void testDeleteCustomerNotFound() {
        clearData();
        boolean deleted = customerService.deleteCustomer(999L);
        System.out.println("testDeleteCustomerNotFound: " + (!deleted ? "PASS" : "FAIL"));
    }

    private void clearData() {
        orderRepository.deleteAll();
        customerRepository.deleteAll();
    }
} 