package com.example.order.dto;

public record OrderResponse(Long id, String sku, int quantity, String status) {}
