package org.example.pahanaedu.testcases.item;

import org.example.pahanaedu.dto.ItemRequest;
import org.example.pahanaedu.entity.Item;
import org.example.pahanaedu.repository.ItemRepository;
import org.example.pahanaedu.repository.OrderRepository;
import org.example.pahanaedu.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ItemFunctionTests implements CommandLineRunner {
    @Autowired
    private ItemService itemService;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private OrderRepository orderRepository;

    @Override
    public void run(String... args) {
        System.out.println("\n--- Running Item Function Tests ---");
        testAddItem();
        testUpdateItem();
        testGetAllItems();
        testDeleteItem();
        testUpdateItemNotFound();
        testDeleteItemNotFound();
        System.out.println("--- Item Function Tests Complete ---\n");
    }

    private void testAddItem() {
        clearData();
        ItemRequest req = new ItemRequest();
        req.setName("Book");
        req.setDescription("Math Book");
        req.setPrice(new BigDecimal("1000.00"));
        req.setStock(10);
        Item item = itemService.addItem(req);
        System.out.println("testAddItem: " + (item != null && item.getId() != null ? "PASS" : "FAIL"));
    }

    private void testUpdateItem() {
        clearData();
        ItemRequest req = new ItemRequest();
        req.setName("Pen");
        req.setDescription("Blue Pen");
        req.setPrice(new BigDecimal("50.00"));
        req.setStock(100);
        Item item = itemService.addItem(req);
        req.setName("Pen Updated");
        Item updated = itemService.updateItem(item.getId(), req);
        System.out.println("testUpdateItem: " + (updated != null && "Pen Updated".equals(updated.getName()) ? "PASS" : "FAIL"));
    }

    private void testGetAllItems() {
        clearData();
        ItemRequest req = new ItemRequest();
        req.setName("Pencil");
        req.setDescription("HB Pencil");
        req.setPrice(new BigDecimal("20.00"));
        req.setStock(200);
        itemService.addItem(req);
        boolean pass = itemService.getAllItems().size() == 1;
        System.out.println("testGetAllItems: " + (pass ? "PASS" : "FAIL"));
    }

    private void testDeleteItem() {
        clearData();
        ItemRequest req = new ItemRequest();
        req.setName("Eraser");
        req.setDescription("White Eraser");
        req.setPrice(new BigDecimal("30.00"));
        req.setStock(50);
        Item item = itemService.addItem(req);
        boolean deleted = itemService.deleteItem(item.getId());
        System.out.println("testDeleteItem: " + (deleted ? "PASS" : "FAIL"));
    }

    private void testUpdateItemNotFound() {
        clearData();
        ItemRequest req = new ItemRequest();
        req.setName("Ruler");
        req.setDescription("30cm Ruler");
        req.setPrice(new BigDecimal("40.00"));
        req.setStock(30);
        Item updated = itemService.updateItem(999L, req);
        System.out.println("testUpdateItemNotFound: " + (updated == null ? "PASS" : "FAIL"));
    }

    private void testDeleteItemNotFound() {
        clearData();
        boolean deleted = itemService.deleteItem(999L);
        System.out.println("testDeleteItemNotFound: " + (!deleted ? "PASS" : "FAIL"));
    }

    private void clearData() {
        orderRepository.deleteAll();
        itemRepository.deleteAll();
    }
} 