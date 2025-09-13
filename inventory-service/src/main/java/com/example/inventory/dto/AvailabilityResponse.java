package com.example.inventory.dto;

public record AvailabilityResponse(String sku, boolean available, int stock) {}
