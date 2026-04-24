package com.springboot.relationship.Service.Implement;

import com.springboot.relationship.DTO.RequestDTO.PaymentRequestDTO;
import com.springboot.relationship.DTO.ResponeDTO.PaymentResponseDTO;
import com.springboot.relationship.Entity.Order;
import com.springboot.relationship.Entity.Payment;
import com.springboot.relationship.Exception.ResourceNotFoundException;
import com.springboot.relationship.Repository.OrderRepository;
import com.springboot.relationship.Repository.PaymentRepository;
import com.springboot.relationship.Service.PaymentService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentServiceImplement implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    public PaymentServiceImplement(
            PaymentRepository paymentRepository,
            OrderRepository orderRepository
    ) {
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public PaymentResponseDTO createPayment(PaymentRequestDTO paymentRequestDTO) {
        // ✓ ស្វែងរក Order
        Order order = orderRepository.findById(paymentRequestDTO.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Order not found with id: " + paymentRequestDTO.getOrderId()
                ));

        // ✓ ឆ្នាក់ amount ត្រូវគ្នា Order totalAmount
        if (paymentRequestDTO.getTotalamount().compareTo(order.getTotalAmount()) != 0) {
            throw new IllegalArgumentException(
                    "Payment amount must equal order total amount: " + order.getTotalAmount()
            );
        }

        // ✓ បង្កើត Payment
        Payment payment = new Payment();
        payment.setTotalamount(paymentRequestDTO.getTotalamount());
        payment.setPaymentMethod(paymentRequestDTO.getPaymentMethod());
        payment.setStatus("COMPLETED");

        // ✓ attach payment to order (Order owns payment_id)
        order.setPayment(payment);
        Order savedOrder = orderRepository.save(order);
        Payment savedPayment = savedOrder.getPayment();

        // ✓ ម្ាបលែង ទៅ DTO
        return mappToResponseDTO(savedPayment);
    }

    @Override
    public PaymentResponseDTO getPaymentById(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Payment not found with id: " + id
                ));
        return mappToResponseDTO(payment);
    }

    @Override
    public List<PaymentResponseDTO> getAllPayments() {
        return paymentRepository.findAll()
                .stream()
                .map(this::mappToResponseDTO)
                .toList();
    }

    @Override
    public PaymentResponseDTO updatePaymentStatus(Long id, String status) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Payment not found with id: " + id
                ));

        // ✓ Validate status
        if (!status.matches("PENDING|COMPLETED|FAILED")) {
            throw new IllegalArgumentException(
                    "Invalid status. Must be PENDING, COMPLETED, or FAILED"
            );
        }

        payment.setStatus(status);
        Payment updatedPayment = paymentRepository.save(payment);

        return mappToResponseDTO(updatedPayment);
    }

    @Override
    public void deletePayment(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Payment not found with id: " + id
                ));
        paymentRepository.delete(payment);
    }

    @Override
    public PaymentResponseDTO mappToResponseDTO(Payment payment) {
        PaymentResponseDTO dto = new PaymentResponseDTO();
        dto.setId(payment.getId());
        dto.setOrderId(payment.getOrder() != null ? payment.getOrder().getId() : null);
        dto.setTotalamount(payment.getTotalamount());
        dto.setStatus(payment.getStatus());
        dto.setPaymentMethod(payment.getPaymentMethod());
        dto.setPaymentDate(payment.getPaymentDate());
        return dto;
    }
}

