package com.example.order.web;

import com.example.order.OrderServiceApplication;
import com.example.order.dto.CreateOrderRequest;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = OrderServiceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OrderIntegrationTest {

    private static MockWebServer server;

    @DynamicPropertySource
    static void props(DynamicPropertyRegistry r) {
        r.add("app.inventory-base-url", () -> server.url("/").toString());
    }

    @BeforeAll
    static void start() throws Exception {
        server = new MockWebServer();
        server.start();
    }

    @AfterAll
    static void stop() throws Exception {
        server.shutdown();
    }

    @LocalServerPort
    int port;

    @Autowired
    TestRestTemplate rest;

    @Test
    void create_order_success_flow() {
        server.enqueue(new MockResponse()
                .setBody("{\"sku\":\"PEN-001\",\"available\":true,\"stock\":10}")
                .addHeader("Content-Type","application/json"));
        server.enqueue(new MockResponse().setResponseCode(204));

        var resp = rest.postForEntity("http://localhost:" + port + "/api/orders",
                new CreateOrderRequest("PEN-001", 2), String.class);

        assertThat(resp.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(resp.getBody()).contains("CREATED");
    }
}
