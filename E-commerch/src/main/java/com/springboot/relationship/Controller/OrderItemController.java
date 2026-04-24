package com.springboot.relationship.Controller;

import com.springboot.relationship.DTO.RequestDTO.OrderItemRequestDTO;
import com.springboot.relationship.DTO.ResponeDTO.OrderItemResponseDTO;
import com.springboot.relationship.Service.OrderItemService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders/{orderId}/items")
public class OrderItemController {

    private final OrderItemService orderItemService;

    public OrderItemController(OrderItemService orderItemService) {
        this.orderItemService = orderItemService;
    }

    @PostMapping("/add")
    public ResponseEntity<OrderItemResponseDTO> addItemToOrder(
            @PathVariable Long orderId,
            @Valid @RequestBody OrderItemRequestDTO orderItemRequestDTO
    ) {
        OrderItemResponseDTO dto = orderItemService.addItemToOrder(orderId, orderItemRequestDTO);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/update/{itemId}")
    public ResponseEntity<OrderItemResponseDTO> updateOrderItem(
            @PathVariable Long orderId,
            @PathVariable Long itemId,
            @Valid @RequestBody OrderItemRequestDTO orderItemRequestDTO
    ) {
        OrderItemResponseDTO dto = orderItemService.updateOrderItem(orderId, itemId, orderItemRequestDTO);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/delete/{itemId}")
    public ResponseEntity<Void> deleteItem(
            @PathVariable Long orderId,
            @PathVariable Long itemId
    ) {
        orderItemService.deleteItem(orderId, itemId);
        return ResponseEntity.noContent().build();
    }
}

