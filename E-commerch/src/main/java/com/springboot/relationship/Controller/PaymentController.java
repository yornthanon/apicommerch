package com.springboot.relationship.Controller;

import com.springboot.relationship.DTO.RequestDTO.PaymentRequestDTO;
import com.springboot.relationship.DTO.ResponeDTO.PaymentResponseDTO;
import com.springboot.relationship.Service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/create")
    public ResponseEntity<PaymentResponseDTO> createPayment(
            @Valid @RequestBody PaymentRequestDTO paymentRequestDTO
    ) {
        PaymentResponseDTO dto = paymentService.createPayment(paymentRequestDTO);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/getall")
    public ResponseEntity<List<PaymentResponseDTO>> getAllPayments() {
        List<PaymentResponseDTO> payments = paymentService.getAllPayments();
        return ResponseEntity.ok(payments);
    }

    @GetMapping("/getbyid/{id}")
    public ResponseEntity<PaymentResponseDTO> getPaymentById(@PathVariable Long id) {
        PaymentResponseDTO dto = paymentService.getPaymentById(id);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/update-status/{id}")
    public ResponseEntity<PaymentResponseDTO> updatePaymentStatus(
            @PathVariable Long id,
            @RequestParam String status
    ) {
        PaymentResponseDTO dto = paymentService.updatePaymentStatus(id, status);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deletePayment(@PathVariable Long id) {
        paymentService.deletePayment(id);
        return ResponseEntity.noContent().build();
    }
}

