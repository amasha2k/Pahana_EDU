package org.example.pahanaedu;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.pahanaedu.dto.OrderRequest;
import org.example.pahanaedu.entity.Customer;
import org.example.pahanaedu.entity.Item;
import org.example.pahanaedu.repository.CustomerRepository;
import org.example.pahanaedu.repository.ItemRepository;
import org.example.pahanaedu.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private OrderRepository orderRepository;

    private Customer customer;
    private Item item;

    @BeforeEach
    void setup() {
        orderRepository.deleteAll();
        itemRepository.deleteAll();
        customerRepository.deleteAll();
        customer = new Customer();
        customer.setAccountNumber("CUST001");
        customer.setName("John Doe");
        customer.setAddress("Colombo");
        customer.setTelephone("0771234567");
        customer.setUnitsConsumed(0);
        customer = customerRepository.save(customer);
        item = new Item();
        item.setName("Book");
        item.setDescription("Math Book");
        item.setPrice(new BigDecimal("1000.00"));
        item.setStock(10);
        item = itemRepository.save(item);
    }

    @Test
    void testCreateOrder() throws Exception {
        OrderRequest req = new OrderRequest();
        req.setCustomerId(customer.getId());
        req.setItemId(item.getId());
        req.setQuantity(2);
        req.setPrice(item.getPrice());
        req.setDiscount(new BigDecimal("100.00"));
        req.setOrderDate(LocalDateTime.now());
        mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void testGetAllOrders() throws Exception {
        testCreateOrder();
        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void testGetOrderById() throws Exception {
        OrderRequest req = new OrderRequest();
        req.setCustomerId(customer.getId());
        req.setItemId(item.getId());
        req.setQuantity(1);
        req.setPrice(item.getPrice());
        req.setDiscount(BigDecimal.ZERO);
        req.setOrderDate(LocalDateTime.now());
        String response = mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andReturn().getResponse().getContentAsString();
        Long orderId = objectMapper.readTree(response).get("id").asLong();
        mockMvc.perform(get("/api/orders/" + orderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(orderId));
    }

    @Test
    void testUpdateOrder() throws Exception {
        OrderRequest req = new OrderRequest();
        req.setCustomerId(customer.getId());
        req.setItemId(item.getId());
        req.setQuantity(1);
        req.setPrice(item.getPrice());
        req.setDiscount(BigDecimal.ZERO);
        req.setOrderDate(LocalDateTime.now());
        String response = mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andReturn().getResponse().getContentAsString();
        Long orderId = objectMapper.readTree(response).get("id").asLong();
        req.setQuantity(5);
        mockMvc.perform(put("/api/orders/" + orderId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity").value(5));
    }

    @Test
    void testDeleteOrder() throws Exception {
        OrderRequest req = new OrderRequest();
        req.setCustomerId(customer.getId());
        req.setItemId(item.getId());
        req.setQuantity(1);
        req.setPrice(item.getPrice());
        req.setDiscount(BigDecimal.ZERO);
        req.setOrderDate(LocalDateTime.now());
        String response = mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andReturn().getResponse().getContentAsString();
        Long orderId = objectMapper.readTree(response).get("id").asLong();
        mockMvc.perform(delete("/api/orders/" + orderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void testCreateOrderInvalidCustomer() throws Exception {
        OrderRequest req = new OrderRequest();
        req.setCustomerId(999L);
        req.setItemId(item.getId());
        req.setQuantity(1);
        req.setPrice(item.getPrice());
        req.setDiscount(BigDecimal.ZERO);
        req.setOrderDate(LocalDateTime.now());
        mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    void testCreateOrderInvalidItem() throws Exception {
        OrderRequest req = new OrderRequest();
        req.setCustomerId(customer.getId());
        req.setItemId(999L);
        req.setQuantity(1);
        req.setPrice(new BigDecimal("100.00"));
        req.setDiscount(BigDecimal.ZERO);
        req.setOrderDate(LocalDateTime.now());
        mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    void testDeleteOrderNotFound() throws Exception {
        mockMvc.perform(delete("/api/orders/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    void testUpdateOrderNotFound() throws Exception {
        OrderRequest req = new OrderRequest();
        req.setCustomerId(customer.getId());
        req.setItemId(item.getId());
        req.setQuantity(1);
        req.setPrice(item.getPrice());
        req.setDiscount(BigDecimal.ZERO);
        req.setOrderDate(LocalDateTime.now());
        mockMvc.perform(put("/api/orders/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    void testOrderTotalCalculation() throws Exception {
        OrderRequest req = new OrderRequest();
        req.setCustomerId(customer.getId());
        req.setItemId(item.getId());
        req.setQuantity(2);
        req.setPrice(new BigDecimal("1000.00"));
        req.setDiscount(new BigDecimal("200.00"));
        req.setOrderDate(LocalDateTime.now());
        String response = mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andReturn().getResponse().getContentAsString();
        BigDecimal total = new BigDecimal("1000.00").multiply(BigDecimal.valueOf(2)).subtract(new BigDecimal("200.00"));
        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].total").value(total));
    }
} 