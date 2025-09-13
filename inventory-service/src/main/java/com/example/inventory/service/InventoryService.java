package com.example.inventory.service;

import com.example.inventory.dto.AvailabilityResponse;
import com.example.inventory.dto.DecreaseStockRequest;
import com.example.inventory.repo.ItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InventoryService {

    private final ItemRepository repo;

    public InventoryService(ItemRepository repo) {
        this.repo = repo;
    }

    @Transactional(readOnly = true)
    public AvailabilityResponse availability(String sku) {
        var item = repo.findBySku(sku)
                .orElseThrow(() -> new IllegalArgumentException("SKU not found: " + sku));
        return new AvailabilityResponse(item.getSku(), item.getStock() > 0, item.getStock());
    }

    @Transactional
    public void decrease(DecreaseStockRequest req) {
        var item = repo.findBySku(req.sku())
                .orElseThrow(() -> new IllegalArgumentException("SKU not found: " + req.sku()));
        if (req.quantity() == null || req.quantity() <= 0) {
            throw new IllegalArgumentException("Quantity must be > 0");
        }
        if (item.getStock() < req.quantity()) {
            throw new IllegalStateException("Insufficient stock for " + req.sku());
        }
        item.setStock(item.getStock() - req.quantity());
        repo.save(item);
    }
}
