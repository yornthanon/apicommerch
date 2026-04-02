package com.springboot.relationship.Service;

import com.springboot.relationship.DTO.RequestDTO.CategoryRequestDTO;
import com.springboot.relationship.DTO.ResponeDTO.CategoryResponseDTO;
import com.springboot.relationship.Entity.Category;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
public interface CategoryService {

    CategoryResponseDTO createCategory(CategoryRequestDTO categoryRequestDTO);
    List<CategoryResponseDTO> getAllCategories();

    CategoryResponseDTO getCategoryById(Long id);
    void deleteCategory(Long id);
    CategoryResponseDTO updateCategory(Long id , CategoryRequestDTO categoryRequestDTO);

    CategoryResponseDTO mappToResponseDTO(Category category);
}
