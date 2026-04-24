package com.springboot.relationship.Service;

import com.springboot.relationship.DTO.RequestDTO.OrderRequestDTO;
import com.springboot.relationship.DTO.ResponeDTO.OrderResponeDTO;
import com.springboot.relationship.Entity.Order;

import java.util.List;

public interface OrderService {
    OrderResponeDTO createOrder(OrderRequestDTO orderRequestDTO);
    List<OrderResponeDTO> getAllOrders();
    OrderResponeDTO getOrderById(Long id);
    void deleteOrder(Long id);
    OrderResponeDTO updateOrder(Long id , OrderRequestDTO orderRequestDTO);

    OrderResponeDTO mappToResponseDTO(Order  order);

}
