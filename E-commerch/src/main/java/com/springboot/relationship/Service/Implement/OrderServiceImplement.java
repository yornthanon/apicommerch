package com.springboot.relationship.Service.Implement;

import com.springboot.relationship.DTO.RequestDTO.OrderItemRequestDTO;
import com.springboot.relationship.DTO.RequestDTO.OrderRequestDTO;
import com.springboot.relationship.DTO.ResponeDTO.OrderItemResponseDTO;
import com.springboot.relationship.DTO.ResponeDTO.OrderResponeDTO;
import com.springboot.relationship.Entity.Order;
import com.springboot.relationship.Entity.OrderItem;
import com.springboot.relationship.Entity.Product;
import com.springboot.relationship.Entity.User;
import com.springboot.relationship.Exception.ResourceNotFoundException;
import com.springboot.relationship.Repository.OrderItemRepository;
import com.springboot.relationship.Repository.OrderRepository;
import com.springboot.relationship.Repository.ProductRepository;
import com.springboot.relationship.Repository.UserRepository;
import com.springboot.relationship.Service.OrderService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImplement implements OrderService {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;

    public OrderServiceImplement(
            UserRepository userRepository,
            OrderRepository orderRepository,
            OrderItemRepository orderItemRepository,
            ProductRepository productRepository
    ) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.productRepository = productRepository;
    }

    @Override
    public OrderResponeDTO createOrder(OrderRequestDTO orderRequestDTO) {
        if (orderRequestDTO == null || orderRequestDTO.getUserId() == null) {
            throw new IllegalArgumentException("User ID is required");
        }
        if (orderRequestDTO.getOrderItemRequestDTOS() == null || orderRequestDTO.getOrderItemRequestDTOS().isEmpty()) {
            throw new IllegalArgumentException("Order items cannot be empty");
        }

        // ✓ ស្វែងរក User (Required)
        User user = userRepository.findById(orderRequestDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id: " + orderRequestDTO.getUserId()
                ));

        // ✓ បង្កើត Order ថ្មី
        // NOTE: With orphanRemoval=true, don't replace the collection instance.
        Order order = new Order();
        order.setUser(user);
        order.setStatus("PENDING");
        order.setTotalAmount(BigDecimal.ZERO);

        // ✓ ដំណើរការ OrderItems
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (OrderItemRequestDTO itemDTO : orderRequestDTO.getOrderItemRequestDTOS()) {
            if (itemDTO == null || itemDTO.getProductId() == null) {
                throw new IllegalArgumentException("Product id is required");
            }
            if (itemDTO.getQuantity() == null || itemDTO.getQuantity() < 1) {
                throw new IllegalArgumentException("Quantity must be at least 1");
            }

            // ឆ្នាក់ Product មាន
            Product product = productRepository.findById(itemDTO.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Product not found with id: " + itemDTO.getProductId()
                    ));

            // ឆ្នាក់ Stock គ្រប់គ្រាន់
            if (product.getStock() == null) {
                throw new IllegalArgumentException("Product stock is missing for product: " + product.getName());
            }
            if (product.getStock() < itemDTO.getQuantity()) {
                throw new IllegalArgumentException(
                        "Not enough stock for product: " + product.getName()
                );
            }

            // បង្កើត OrderItem
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(itemDTO.getQuantity());
            orderItem.setPrice(product.getPrice());
            order.getOrderItems().add(orderItem);

            // គណនា totalAmount
            BigDecimal itemTotal = product.getPrice()
                    .multiply(BigDecimal.valueOf(itemDTO.getQuantity()));
            totalAmount = totalAmount.add(itemTotal);
        }

        // ធ្វើបច្ចុប្បន្នភាព Order ដោយ totalAmount
        order.setTotalAmount(totalAmount);
        Order finalOrder = orderRepository.save(order);

        return mappToResponseDTO(finalOrder);
    }

    @Override
    public List<OrderResponeDTO> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(this::mappToResponseDTO)
                .toList();
    }

    @Override
    public OrderResponeDTO getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Order not found with id: " + id
                ));
        return mappToResponseDTO(order);
    }

    @Override
    public void deleteOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Order not found with id: " + id
                ));
        orderRepository.delete(order);
    }

    @Override
    public OrderResponeDTO updateOrder(Long id, OrderRequestDTO orderRequestDTO) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Order not found with id: " + id
                ));

        // ធ្វើបច្ចុប្បន្នភាព User (Required)
        User user = userRepository.findById(orderRequestDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id: " + orderRequestDTO.getUserId()
                ));

        order.setUser(user);

        // លុប OrderItems ចាស់
        orderItemRepository.deleteByOrderId(order.getId());
        order.getOrderItems().clear();

        // បង្កើត OrderItems ថ្មី
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (OrderItemRequestDTO itemDTO : orderRequestDTO.getOrderItemRequestDTOS()) {
            Product product = productRepository.findById(itemDTO.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Product not found with id: " + itemDTO.getProductId()
                    ));

            if (product.getStock() < itemDTO.getQuantity()) {
                throw new IllegalArgumentException(
                        "Not enough stock for product: " + product.getName()
                );
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(itemDTO.getQuantity());
            orderItem.setPrice(product.getPrice());

            order.getOrderItems().add(orderItem);

            BigDecimal itemTotal = product.getPrice()
                    .multiply(BigDecimal.valueOf(itemDTO.getQuantity()));
            totalAmount = totalAmount.add(itemTotal);
        }

        order.setTotalAmount(totalAmount);
        Order updatedOrder = orderRepository.save(order);

        return mappToResponseDTO(updatedOrder);
    }

    @Override
    public OrderResponeDTO mappToResponseDTO(Order order) {
        OrderResponeDTO dto = new OrderResponeDTO();
        dto.setId(order.getId());
        dto.setDateTime(order.getDate());
        dto.setStatus(order.getStatus());
        dto.setTotalAmount(order.getTotalAmount());

        List<OrderItemResponseDTO> itemDTOs = order.getOrderItems()
                .stream()
                .map(item -> {
                    OrderItemResponseDTO itemDTO = new OrderItemResponseDTO();
                    itemDTO.setProductId(item.getProduct().getId());
                    itemDTO.setProductName(item.getProduct().getName());
                    itemDTO.setQuantity(item.getQuantity());
                    itemDTO.setPrice(item.getPrice());
                    itemDTO.setTotalPrice(
                            item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()))
                    );
                    return itemDTO;
                })
                .toList();

        dto.setItems(itemDTOs);
        return dto;
    }
}
