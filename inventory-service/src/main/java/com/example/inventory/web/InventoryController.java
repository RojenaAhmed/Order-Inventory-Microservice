package com.example.inventory.web;

import com.example.inventory.dto.AvailabilityResponse;
import com.example.inventory.dto.DecreaseStockRequest;
import com.example.inventory.service.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryService service;

    public InventoryController(InventoryService service) {
        this.service = service;
    }

    @Operation(summary = "Get availability & stock by SKU")
    @GetMapping("/{sku}")
    public AvailabilityResponse availability(@PathVariable String sku) {
        return service.availability(sku);
    }

    @Operation(summary = "Decrease stock by SKU and quantity")
    @PostMapping("/decrease")
    public ResponseEntity<Void> decrease(@Valid @RequestBody DecreaseStockRequest req) {
        service.decrease(req);
        return ResponseEntity.noContent().build();
    }
}
