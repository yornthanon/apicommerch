package com.springboot.relationship.DTO.RequestDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryRequestDTO {

    @NotBlank(message = "Category name cannot be blank")
    @Size(max = 50, message = "Category name must be less than 50 characters")
    private String name;

    @NotBlank(message = "Category description cannot be blank")
    @Size(max = 500, message = "Description must be less than 500 characters")
    private String description;


}
