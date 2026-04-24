package com.springboot.relationship.Service.Implement;

import com.springboot.relationship.DTO.RequestDTO.OrderItemRequestDTO;
import com.springboot.relationship.DTO.ResponeDTO.OrderItemResponseDTO;
import com.springboot.relationship.Entity.Order;
import com.springboot.relationship.Entity.OrderItem;
import com.springboot.relationship.Entity.Product;
import com.springboot.relationship.Exception.ResourceNotFoundException;
import com.springboot.relationship.Repository.OrderItemRepository;
import com.springboot.relationship.Repository.OrderRepository;
import com.springboot.relationship.Repository.ProductRepository;
import com.springboot.relationship.Service.OrderItemService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class OrderItemServiceImplement implements OrderItemService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;

    public OrderItemServiceImplement(
            OrderRepository orderRepository,
            OrderItemRepository orderItemRepository,
            ProductRepository productRepository
    ) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.productRepository = productRepository;
    }

    @Override
    public OrderItemResponseDTO addItemToOrder(Long orderId, OrderItemRequestDTO orderItemRequestDTO) {
        // ស្វែងរក Order
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Order not found with id: " + orderId
                ));

        // ស្វែងរក Product
        Product product = productRepository.findById(orderItemRequestDTO.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product not found with id: " + orderItemRequestDTO.getProductId()
                ));

        // ឆ្នាក់ Stock
        if (product.getStock() < orderItemRequestDTO.getQuantity()) {
            throw new IllegalArgumentException(
                    "Not enough stock for product: " + product.getName()
            );
        }

        // បង្កើត OrderItem
        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setProduct(product);
        orderItem.setQuantity(orderItemRequestDTO.getQuantity());
        orderItem.setPrice(product.getPrice());

        // រក្សាទុក OrderItem
        OrderItem savedItem = orderItemRepository.save(orderItem);

        // ធ្វើបច្ចុប្បន្នភាព Order totalAmount
        BigDecimal newItemTotal = product.getPrice()
                .multiply(BigDecimal.valueOf(orderItemRequestDTO.getQuantity()));
        BigDecimal newTotal = order.getTotalAmount().add(newItemTotal);
        order.setTotalAmount(newTotal);
        orderRepository.save(order);

        return mappToResponseDTO(savedItem);
    }

    @Override
    public OrderItemResponseDTO updateOrderItem(Long orderId, Long itemId, OrderItemRequestDTO orderItemRequestDTO) {
        // ស្វែងរក Order
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Order not found with id: " + orderId
                ));

        // ស្វែងរក OrderItem
        OrderItem orderItem = orderItemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "OrderItem not found with id: " + itemId
                ));

        // ឆ្នាក់ OrderItem ស្ថិតក្នុង Order នេះ
        if (!orderItem.getOrder().getId().equals(orderId)) {
            throw new ResourceNotFoundException(
                    "OrderItem " + itemId + " does not belong to Order " + orderId
            );
        }

        // ស្វែងរក Product
        Product product = productRepository.findById(orderItemRequestDTO.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product not found with id: " + orderItemRequestDTO.getProductId()
                ));

        // ឆ្នាក់ Stock
        if (product.getStock() < orderItemRequestDTO.getQuantity()) {
            throw new IllegalArgumentException(
                    "Not enough stock for product: " + product.getName()
            );
        }

        // រក្សាទុក តម្លៃចាស់
        BigDecimal oldItemTotal = orderItem.getPrice()
                .multiply(BigDecimal.valueOf(orderItem.getQuantity()));

        // ធ្វើបច្ចុប្បន្នភាព OrderItem
        orderItem.setProduct(product);
        orderItem.setQuantity(orderItemRequestDTO.getQuantity());
        orderItem.setPrice(product.getPrice());

        OrderItem updatedItem = orderItemRepository.save(orderItem);

        // គណនាឡើងវិញ Order totalAmount
        BigDecimal newItemTotal = product.getPrice()
                .multiply(BigDecimal.valueOf(orderItemRequestDTO.getQuantity()));
        BigDecimal newTotal = order.getTotalAmount()
                .subtract(oldItemTotal)
                .add(newItemTotal);
        order.setTotalAmount(newTotal);
        orderRepository.save(order);

        return mappToResponseDTO(updatedItem);
    }

    @Override
    public void deleteItem(Long orderId, Long itemId) {
        // ស្វែងរក Order
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Order not found with id: " + orderId
                ));

        // ស្វែងរក OrderItem
        OrderItem orderItem = orderItemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "OrderItem not found with id: " + itemId
                ));

        // ឆ្នាក់ OrderItem ស្ថិតក្នុង Order នេះ
        if (!orderItem.getOrder().getId().equals(orderId)) {
            throw new ResourceNotFoundException(
                    "OrderItem " + itemId + " does not belong to Order " + orderId
            );
        }

        // គណនា item total
        BigDecimal itemTotal = orderItem.getPrice()
                .multiply(BigDecimal.valueOf(orderItem.getQuantity()));

        // លុប OrderItem
        orderItemRepository.delete(orderItem);

        // គណនាឡើងវិញ Order totalAmount
        BigDecimal newTotal = order.getTotalAmount().subtract(itemTotal);
        order.setTotalAmount(newTotal);
        orderRepository.save(order);
    }

    private OrderItemResponseDTO mappToResponseDTO(OrderItem orderItem) {
        OrderItemResponseDTO dto = new OrderItemResponseDTO();
        dto.setProductId(orderItem.getProduct().getId());
        dto.setProductName(orderItem.getProduct().getName());
        dto.setQuantity(orderItem.getQuantity());
        dto.setPrice(orderItem.getPrice());
        dto.setTotalPrice(
                orderItem.getPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity()))
        );
        return dto;
    }
}
