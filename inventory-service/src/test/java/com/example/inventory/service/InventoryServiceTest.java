package com.example.inventory.service;

import com.example.inventory.dto.DecreaseStockRequest;
import com.example.inventory.entity.Item;
import com.example.inventory.repo.ItemRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class InventoryServiceTest {

    @Test
    void decrease_ok() {
        var repo = Mockito.mock(ItemRepository.class);
        var item = Item.builder().id(1L).sku("X").name("X").stock(5).build();
        Mockito.when(repo.findBySku("X")).thenReturn(java.util.Optional.of(item));
        var svc = new InventoryService(repo);
        svc.decrease(new DecreaseStockRequest("X", 3));
        assertEquals(2, item.getStock());
    }

    @Test
    void decrease_insufficient() {
        var repo = Mockito.mock(ItemRepository.class);
        var item = Item.builder().id(1L).sku("X").name("X").stock(2).build();
        Mockito.when(repo.findBySku("X")).thenReturn(java.util.Optional.of(item));
        var svc = new InventoryService(repo);
        assertThrows(IllegalStateException.class, () -> svc.decrease(new DecreaseStockRequest("X", 3)));
    }
}
