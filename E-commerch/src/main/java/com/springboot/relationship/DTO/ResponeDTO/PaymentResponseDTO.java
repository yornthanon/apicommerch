package com.springboot.relationship.DTO.ResponeDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponseDTO {
    private Long id;
    private Long orderId;
    private BigDecimal totalamount;
    private String status;
    private String paymentMethod;
    private LocalDateTime paymentDate;
}

