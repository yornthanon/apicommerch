package com.springboot.relationship.DTO.ResponeDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponeDTO {
    private Long id;
    private LocalDateTime dateTime;
    private String status;
    private BigDecimal totalAmount;
    private List<OrderItemResponseDTO> items;

}
