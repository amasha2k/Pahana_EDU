package org.example.pahanaedu.testcases;

import org.example.pahanaedu.dto.OrderRequest;
import org.example.pahanaedu.entity.Customer;
import org.example.pahanaedu.entity.Item;
import org.example.pahanaedu.entity.Order;
import org.example.pahanaedu.repository.CustomerRepository;
import org.example.pahanaedu.repository.ItemRepository;
import org.example.pahanaedu.repository.OrderRepository;
import org.example.pahanaedu.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
public class OrderFunctionTests implements CommandLineRunner {
    @Autowired
    private OrderService orderService;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private OrderRepository orderRepository;

    @Override
    public void run(String... args) {
        System.out.println("\n--- Running Order Function Tests ---");
        testCreateOrder();
        testCreateOrderInvalidCustomer();
        testCreateOrderInvalidItem();
        testGetAllOrders();
        testGetOrderById();
        testUpdateOrder();
        testUpdateOrderNotFound();
        testDeleteOrder();
        testDeleteOrderNotFound();
        testOrderTotalCalculation();
        System.out.println("--- Order Function Tests Complete ---\n");
    }

    private void testCreateOrder() {
        clearData();
        Customer customer = createCustomer();
        Item item = createItem();
        OrderRequest req = new OrderRequest();
        req.setCustomerId(customer.getId());
        req.setItemId(item.getId());
        req.setQuantity(2);
        req.setPrice(item.getPrice());
        req.setDiscount(new BigDecimal("100.00"));
        req.setOrderDate(LocalDateTime.now());
        Order order = orderService.createOrder(req);
        System.out.println("testCreateOrder: " + (order != null && order.getId() != null ? "PASS" : "FAIL"));
    }

    private void testCreateOrderInvalidCustomer() {
        clearData();
        Item item = createItem();
        OrderRequest req = new OrderRequest();
        req.setCustomerId(999L);
        req.setItemId(item.getId());
        req.setQuantity(1);
        req.setPrice(item.getPrice());
        req.setDiscount(BigDecimal.ZERO);
        req.setOrderDate(LocalDateTime.now());
        Order order = orderService.createOrder(req);
        System.out.println("testCreateOrderInvalidCustomer: " + (order == null ? "PASS" : "FAIL"));
    }

    private void testCreateOrderInvalidItem() {
        clearData();
        Customer customer = createCustomer();
        OrderRequest req = new OrderRequest();
        req.setCustomerId(customer.getId());
        req.setItemId(999L);
        req.setQuantity(1);
        req.setPrice(new BigDecimal("100.00"));
        req.setDiscount(BigDecimal.ZERO);
        req.setOrderDate(LocalDateTime.now());
        Order order = orderService.createOrder(req);
        System.out.println("testCreateOrderInvalidItem: " + (order == null ? "PASS" : "FAIL"));
    }

    private void testGetAllOrders() {
        clearData();
        Customer customer = createCustomer();
        Item item = createItem();
        OrderRequest req = new OrderRequest();
        req.setCustomerId(customer.getId());
        req.setItemId(item.getId());
        req.setQuantity(1);
        req.setPrice(item.getPrice());
        req.setDiscount(BigDecimal.ZERO);
        req.setOrderDate(LocalDateTime.now());
        orderService.createOrder(req);
        boolean pass = orderService.getAllOrders().size() == 1;
        System.out.println("testGetAllOrders: " + (pass ? "PASS" : "FAIL"));
    }

    private void testGetOrderById() {
        clearData();
        Customer customer = createCustomer();
        Item item = createItem();
        OrderRequest req = new OrderRequest();
        req.setCustomerId(customer.getId());
        req.setItemId(item.getId());
        req.setQuantity(1);
        req.setPrice(item.getPrice());
        req.setDiscount(BigDecimal.ZERO);
        req.setOrderDate(LocalDateTime.now());
        Order order = orderService.createOrder(req);
        boolean pass = orderService.getOrder(order.getId()) != null;
        System.out.println("testGetOrderById: " + (pass ? "PASS" : "FAIL"));
    }

    private void testUpdateOrder() {
        clearData();
        Customer customer = createCustomer();
        Item item = createItem();
        OrderRequest req = new OrderRequest();
        req.setCustomerId(customer.getId());
        req.setItemId(item.getId());
        req.setQuantity(1);
        req.setPrice(item.getPrice());
        req.setDiscount(BigDecimal.ZERO);
        req.setOrderDate(LocalDateTime.now());
        Order order = orderService.createOrder(req);
        req.setQuantity(5);
        Order updated = orderService.updateOrder(order.getId(), req);
        System.out.println("testUpdateOrder: " + (updated != null && updated.getQuantity() == 5 ? "PASS" : "FAIL"));
    }

    private void testUpdateOrderNotFound() {
        clearData();
        Customer customer = createCustomer();
        Item item = createItem();
        OrderRequest req = new OrderRequest();
        req.setCustomerId(customer.getId());
        req.setItemId(item.getId());
        req.setQuantity(1);
        req.setPrice(item.getPrice());
        req.setDiscount(BigDecimal.ZERO);
        req.setOrderDate(LocalDateTime.now());
        Order updated = orderService.updateOrder(999L, req);
        System.out.println("testUpdateOrderNotFound: " + (updated == null ? "PASS" : "FAIL"));
    }

    private void testDeleteOrder() {
        clearData();
        Customer customer = createCustomer();
        Item item = createItem();
        OrderRequest req = new OrderRequest();
        req.setCustomerId(customer.getId());
        req.setItemId(item.getId());
        req.setQuantity(1);
        req.setPrice(item.getPrice());
        req.setDiscount(BigDecimal.ZERO);
        req.setOrderDate(LocalDateTime.now());
        Order order = orderService.createOrder(req);
        boolean deleted = orderService.deleteOrder(order.getId());
        System.out.println("testDeleteOrder: " + (deleted ? "PASS" : "FAIL"));
    }

    private void testDeleteOrderNotFound() {
        clearData();
        boolean deleted = orderService.deleteOrder(999L);
        System.out.println("testDeleteOrderNotFound: " + (!deleted ? "PASS" : "FAIL"));
    }

    private void testOrderTotalCalculation() {
        clearData();
        Customer customer = createCustomer();
        Item item = createItem();
        OrderRequest req = new OrderRequest();
        req.setCustomerId(customer.getId());
        req.setItemId(item.getId());
        req.setQuantity(2);
        req.setPrice(new BigDecimal("1000.00"));
        req.setDiscount(new BigDecimal("200.00"));
        req.setOrderDate(LocalDateTime.now());
        Order order = orderService.createOrder(req);
        BigDecimal expected = new BigDecimal("1000.00").multiply(BigDecimal.valueOf(2)).subtract(new BigDecimal("200.00"));
        System.out.println("testOrderTotalCalculation: " + (order.getTotal().compareTo(expected) == 0 ? "PASS" : "FAIL"));
    }

    private Customer createCustomer() {
        Customer customer = new Customer();
        customer.setAccountNumber("CUST" + System.nanoTime());
        customer.setName("Test Customer");
        customer.setAddress("Colombo");
        customer.setTelephone("0771234567");
        customer.setUnitsConsumed(0);
        return customerRepository.save(customer);
    }

    private Item createItem() {
        Item item = new Item();
        item.setName("Item" + System.nanoTime());
        item.setDescription("Test Item");
        item.setPrice(new BigDecimal("1000.00"));
        item.setStock(10);
        return itemRepository.save(item);
    }

    private void clearData() {
        orderRepository.deleteAll();
        itemRepository.deleteAll();
        customerRepository.deleteAll();
    }
} 