package com.example.inventory.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record DecreaseStockRequest(
        @NotBlank String sku,
        @Min(1) int quantity
) {}
