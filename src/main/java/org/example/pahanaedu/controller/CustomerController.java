package org.example.pahanaedu.controller;

import org.example.pahanaedu.Routes;
import org.example.pahanaedu.dto.CustomerRequest;
import org.example.pahanaedu.entity.Customer;
import org.example.pahanaedu.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = "http://localhost:14192")
@RestController
@RequestMapping(Routes.CUSTOMER_BASE)
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @PostMapping
    public ResponseEntity<?> addCustomer(@RequestBody CustomerRequest request) {
        Customer customer = customerService.addCustomer(request);
        return ResponseEntity.ok(customer);
    }

    @PutMapping(Routes.UPDATE_CUSTOMER)
    public ResponseEntity<?> editCustomer(@PathVariable Long id, @RequestBody CustomerRequest request) {
        Customer customer = customerService.editCustomer(id, request);
        if (customer == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(customer);
    }

    @GetMapping
    public ResponseEntity<?> getAllCustomers() {
        return ResponseEntity.ok(customerService.getAllCustomers());
    }

    @DeleteMapping(Routes.DELETE_CUSTOMER)
    public ResponseEntity<?> deleteCustomer(@PathVariable Long id) {
        boolean deleted = customerService.deleteCustomer(id);
        if (deleted) {
            return ResponseEntity.ok(Map.of("success", true, "message", "Customer deleted successfully"));
        } else {
            return ResponseEntity.status(404).body(Map.of("success", false, "message", "Customer not found"));
        }
    }
    @GetMapping(Routes.GET_LATEST)
    public ResponseEntity<?> getLatest() {
        try {
            String id = customerService.getlatestId();
            if (id != null) {
                return ResponseEntity.ok(Map.of(
                        "success", true,
                        "id", id,
                        "latestId", id
                ));
            } else {
                return ResponseEntity.status(404).body(Map.of(
                        "success", false,
                        "message", "Customer not found"
                ));
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                    "success", false,
                    "message", "Something went wrong"
            ));
        }
    }

} 