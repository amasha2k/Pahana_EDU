package org.example.pahanaedu.controller;

import org.example.pahanaedu.Routes;
import org.example.pahanaedu.dto.ItemRequest;
import org.example.pahanaedu.entity.Item;
import org.example.pahanaedu.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = "http://localhost:14192")
@RestController
@RequestMapping(Routes.ITEM_BASE)
public class ItemController {
    @Autowired
    private ItemService itemService;

    @PostMapping
    public ResponseEntity<?> addItem(@RequestBody ItemRequest request) {
        Item item = itemService.addItem(request);
        return ResponseEntity.ok(item);
    }

    @PutMapping(Routes.UPDATE_ITEM)
    public ResponseEntity<?> updateItem(@PathVariable Long id, @RequestBody ItemRequest request) {
        Item item = itemService.updateItem(id, request);
        if (item == null) {
            return ResponseEntity.status(404).body(Map.of("success", false, "message", "Item not found"));
        }
        return ResponseEntity.ok(item);
    }

    @DeleteMapping(Routes.DELETE_ITEM)
    public ResponseEntity<?> deleteItem(@PathVariable Long id) {
        boolean deleted = itemService.deleteItem(id);
        if (deleted) {
            return ResponseEntity.ok(Map.of("success", true, "message", "Item deleted successfully"));
        } else {
            return ResponseEntity.status(404).body(Map.of("success", false, "message", "Item not found"));
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllItems() {
        return ResponseEntity.ok(itemService.getAllItems());
    }
} 