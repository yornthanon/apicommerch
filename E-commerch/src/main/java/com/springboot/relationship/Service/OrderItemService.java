package com.springboot.relationship.Service;

import com.springboot.relationship.DTO.RequestDTO.OrderItemRequestDTO;
import com.springboot.relationship.DTO.ResponeDTO.OrderItemResponseDTO;


public interface OrderItemService {
    OrderItemResponseDTO addItemToOrder(Long orderId,OrderItemRequestDTO orderItemRequestDTO);
    OrderItemResponseDTO updateOrderItem(Long orderId,Long itemId, OrderItemRequestDTO orderItemRequestDTO);
    void deleteItem(Long orderId, Long itemId);


}
