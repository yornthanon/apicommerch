package com.springboot.relationship.Service.Implement;

import com.springboot.relationship.DTO.RequestDTO.CategoryRequestDTO;
import com.springboot.relationship.DTO.ResponeDTO.CategoryResponseDTO;
import com.springboot.relationship.Entity.Category;
import com.springboot.relationship.Exception.ResourceNotFoundException;
import com.springboot.relationship.Repository.CategoryRepository;
import com.springboot.relationship.Service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImplement implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImplement(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public CategoryResponseDTO createCategory(CategoryRequestDTO categoryRequestDTO) {
        validateCategoryRequest(categoryRequestDTO);

        Category category = new Category();
        category.setName(categoryRequestDTO.getName());
        category.setDescription(categoryRequestDTO.getDescription());
        Category savedCategory = categoryRepository.saveAndFlush(category);
        return mappToResponseDTO(savedCategory);


    }

    @Override
    public List<CategoryResponseDTO> getAllCategories() {
        return categoryRepository.findAll().stream().map(this::mappToResponseDTO).toList();
    }

    @Override
    public CategoryResponseDTO getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));

        return mappToResponseDTO(category);
    }

    @Override
    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Category not found with id: " + id);
        }

        categoryRepository.deleteById(id);
    }

    @Override
    public CategoryResponseDTO updateCategory(Long id, CategoryRequestDTO categoryRequestDTO) {
        validateCategoryRequest(categoryRequestDTO);

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));

        category.setName(categoryRequestDTO.getName());
        category.setDescription(categoryRequestDTO.getDescription());

        Category savedCategory = categoryRepository.saveAndFlush(category);
        return mappToResponseDTO(savedCategory);
    }

    @Override
    public CategoryResponseDTO mappToResponseDTO(Category category) {
        CategoryResponseDTO categoryResponseDTO = new CategoryResponseDTO();
        categoryResponseDTO.setId(category.getId());
        categoryResponseDTO.setName(category.getName());
        categoryResponseDTO.setDescription(category.getDescription());
        return categoryResponseDTO;
    }

    private void validateCategoryRequest(CategoryRequestDTO categoryRequestDTO) {
        if (categoryRequestDTO == null) {
            throw new IllegalArgumentException("Category request cannot be null");
        }

        if (categoryRequestDTO.getName() == null || categoryRequestDTO.getName().isBlank()) {
            throw new IllegalArgumentException("Category name cannot be blank");
        }

        if (categoryRequestDTO.getDescription() == null || categoryRequestDTO.getDescription().isBlank()) {
            throw new IllegalArgumentException("Category description cannot be blank");
        }
    }
}