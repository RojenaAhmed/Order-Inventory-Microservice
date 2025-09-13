package com.example.order.integration;

import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class InventoryClient {

    private final RestClient client;

    public InventoryClient(RestClient client) {
        this.client = client;
    }

    public record Availability(String sku, boolean available, int stock) {}
    public record Decrease(String sku, int quantity) {}

    public Availability getAvailability(String sku) {
        return client.get().uri("/api/inventory/{sku}", sku)
                .retrieve().body(Availability.class);
    }

    public void decrease(String sku, int quantity) {
        var req = new Decrease(sku, quantity);
        client.post().uri("/api/inventory/decrease")
                .body(req)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (reqB,res)-> { throw new IllegalStateException("Inventory rejected: " + res.getStatusCode()); })
                .toBodilessEntity();
    }
}
