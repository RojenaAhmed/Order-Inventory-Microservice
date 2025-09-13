package com.example.order.web;

import com.example.order.dto.CreateOrderRequest;
import com.example.order.dto.OrderResponse;
import com.example.order.repo.OrderRepository;
import com.example.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService service;
    private final OrderRepository repo;

    public OrderController(OrderService service, OrderRepository repo) {
        this.service = service;
        this.repo = repo;
    }

    @Operation(summary = "Create order after checking inventory")
    @PostMapping
    public OrderResponse create(@Valid @RequestBody CreateOrderRequest req) {
        return service.create(req);
    }

    @Operation(summary = "Get order by id")
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> get(@PathVariable Long id) {
        return repo.findById(id)
                .map(o -> ResponseEntity.ok(new OrderResponse(o.getId(), o.getSku(), o.getQuantity(), o.getStatus())))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "List orders paginated")
    @GetMapping
    public Page<OrderResponse> list(@RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "10") int size) {
        return repo.findAll(PageRequest.of(page, size))
                .map(o -> new OrderResponse(o.getId(), o.getSku(), o.getQuantity(), o.getStatus()));
    }
}
