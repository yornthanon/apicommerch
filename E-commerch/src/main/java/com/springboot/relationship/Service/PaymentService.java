package com.springboot.relationship.Service;

import com.springboot.relationship.DTO.RequestDTO.PaymentRequestDTO;
import com.springboot.relationship.DTO.ResponeDTO.PaymentResponseDTO;
import com.springboot.relationship.Entity.Payment;

import java.util.List;

public interface PaymentService {
    PaymentResponseDTO createPayment(PaymentRequestDTO paymentRequestDTO);
    PaymentResponseDTO getPaymentById(Long id);
    List<PaymentResponseDTO> getAllPayments();
    PaymentResponseDTO updatePaymentStatus(Long id, String status);
    void deletePayment(Long id);
    PaymentResponseDTO mappToResponseDTO(Payment payment);
}

