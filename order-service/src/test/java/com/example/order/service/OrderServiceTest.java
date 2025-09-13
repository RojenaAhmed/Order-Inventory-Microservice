package com.example.order.service;

import com.example.order.dto.CreateOrderRequest;
import com.example.order.integration.InventoryClient;
import com.example.order.repo.OrderRepository;
import com.example.order.entity.CustomerOrder;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class OrderServiceTest {

    @Test
    void create_success() {
        var repo = Mockito.mock(OrderRepository.class);
        var inv = Mockito.mock(InventoryClient.class);
        Mockito.when(inv.getAvailability("PEN-001"))
                .thenReturn(new InventoryClient.Availability("PEN-001", true, 10));
        Mockito.doNothing().when(inv).decrease("PEN-001", 2);
        Mockito.when(repo.save(Mockito.any(CustomerOrder.class))).thenAnswer(a -> {
            CustomerOrder o = a.getArgument(0);
            o.setId(1L);
            return o;
        });
        var svc = new OrderService(repo, inv);
        var resp = svc.create(new CreateOrderRequest("PEN-001", 2));
        assertEquals("CREATED", resp.status());
        assertEquals(1L, resp.id());
    }

    @Test
    void create_failed_when_insufficient() {
        var repo = Mockito.mock(OrderRepository.class);
        var inv = Mockito.mock(InventoryClient.class);
        Mockito.when(inv.getAvailability("PEN-001"))
                .thenReturn(new InventoryClient.Availability("PEN-001", true, 1));
        Mockito.when(repo.save(Mockito.any(CustomerOrder.class))).thenAnswer(a -> {
            CustomerOrder o = a.getArgument(0);
            o.setId(2L);
            return o;
        });
        var svc = new OrderService(repo, inv);
        var resp = svc.create(new CreateOrderRequest("PEN-001", 2));
        assertEquals("FAILED", resp.status());
    }
}
