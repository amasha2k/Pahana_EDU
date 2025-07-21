package org.example.pahanaedu.service;

import org.example.pahanaedu.dto.ItemRequest;
import org.example.pahanaedu.entity.Item;
import org.example.pahanaedu.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService {
    @Autowired
    private ItemRepository itemRepository;

    public Item addItem(ItemRequest request) {
        Item item = new Item();
        item.setName(request.getName());
        item.setDescription(request.getDescription());
        item.setPrice(request.getPrice());
        item.setStock(request.getStock());
        return itemRepository.save(item);
    }

    public Item updateItem(Long id, ItemRequest request) {
        Item item = itemRepository.findById(id).orElse(null);
        if (item == null) return null;
        item.setName(request.getName());
        item.setDescription(request.getDescription());
        item.setPrice(request.getPrice());
        item.setStock(request.getStock());
        return itemRepository.save(item);
    }

    public boolean deleteItem(Long id) {
        if (!itemRepository.existsById(id)) {
            return false;
        }
        itemRepository.deleteById(id);
        return true;
    }

    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }
} 