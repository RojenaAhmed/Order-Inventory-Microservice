package com.example.order.service;

import com.example.order.dto.CreateOrderRequest;
import com.example.order.dto.OrderResponse;
import com.example.order.entity.CustomerOrder;
import com.example.order.integration.InventoryClient;
import com.example.order.repo.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final OrderRepository repo;
    private final InventoryClient inventory;

    public OrderService(OrderRepository repo, InventoryClient inventory) {
        this.repo = repo;
        this.inventory = inventory;
    }

    @Transactional
    public OrderResponse create(CreateOrderRequest req) {
        if (req.quantity() == null || req.quantity() <= 0) {
            throw new IllegalArgumentException("Quantity must be > 0");
        }
        if (req.sku() == null || req.sku().isBlank()) {
            throw new IllegalArgumentException("SKU must not be blank");
        }

        var avail = inventory.getAvailability(req.sku());
        if (avail == null || !avail.available() || avail.stock() < req.quantity()) {
            var failed = repo.save(CustomerOrder.builder()
                    .sku(req.sku()).quantity(req.quantity()).status("FAILED").build());
            return new OrderResponse(failed.getId(), failed.getSku(), failed.getQuantity(), failed.getStatus());
        }

        inventory.decrease(req.sku(), req.quantity());

        var saved = repo.save(CustomerOrder.builder()
                .sku(req.sku()).quantity(req.quantity()).status("CREATED").build());

        return new OrderResponse(saved.getId(), saved.getSku(), saved.getQuantity(), saved.getStatus());
    }
}
