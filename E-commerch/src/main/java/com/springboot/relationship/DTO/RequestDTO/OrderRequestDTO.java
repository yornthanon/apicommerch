package com.springboot.relationship.DTO.RequestDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequestDTO {
    @NotNull(message = "User ID is required")
    private Long userId;

    @NotEmpty(message = "Order items cannot be empty")
    @Valid
    private List<OrderItemRequestDTO> orderItemRequestDTOS;




}
