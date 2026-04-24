package com.springboot.relationship.Controller;

import com.springboot.relationship.DTO.RequestDTO.OrderRequestDTO;
import com.springboot.relationship.DTO.ResponeDTO.OrderResponeDTO;
import com.springboot.relationship.Service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/create")
    public ResponseEntity<OrderResponeDTO> createOrder(
            @Valid @RequestBody OrderRequestDTO orderRequestDTO
    ) {
        OrderResponeDTO dto = orderService.createOrder(orderRequestDTO);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/getall")
    public ResponseEntity<List<OrderResponeDTO>> getAllOrders() {
        List<OrderResponeDTO> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/getbyid/{id}")
    public ResponseEntity<OrderResponeDTO> getOrderById(@PathVariable Long id) {
        OrderResponeDTO dto = orderService.getOrderById(id);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<OrderResponeDTO> updateOrder(
            @PathVariable Long id,
            @Valid @RequestBody OrderRequestDTO orderRequestDTO
    ) {
        OrderResponeDTO dto = orderService.updateOrder(id, orderRequestDTO);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }
}

