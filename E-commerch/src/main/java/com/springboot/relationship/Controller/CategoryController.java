package com.springboot.relationship.Controller;

import com.springboot.relationship.DTO.RequestDTO.CategoryRequestDTO;
import com.springboot.relationship.DTO.ResponeDTO.CategoryResponseDTO;
import com.springboot.relationship.Service.CategoryService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // create

    @PostMapping("/create")
    public ResponseEntity<CategoryResponseDTO> createCategory(
            @Valid @RequestBody CategoryRequestDTO categoryRequestDTO){
        CategoryResponseDTO responseDTO = categoryService.createCategory(categoryRequestDTO);
        return ResponseEntity.ok(responseDTO);
    }
    // gettall
    @GetMapping("/getall")
    public ResponseEntity<List<CategoryResponseDTO>> getAll(){
        List<CategoryResponseDTO> responseDTOList =categoryService.getAllCategories();
        return ResponseEntity.ok(responseDTOList);

    }
    //gettbyid
    @GetMapping("/getbyid/{id}")
    public ResponseEntity<CategoryResponseDTO> getByID(
            @PathVariable long id
    ){
        CategoryResponseDTO responseDTO = categoryService.getCategoryById(id);
        return ResponseEntity.ok(responseDTO);

    }
    // delete
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteItem(
            @PathVariable long id

    ){
        categoryService.deleteCategory(id);
        return ResponseEntity.ok("Category  has been deleted successfully.");

    }
    //update
    @PutMapping("/update/{id}")
    public ResponseEntity<CategoryResponseDTO> updateItem(
            @PathVariable long id ,
            @Valid @RequestBody CategoryRequestDTO categoryRequestDTO
    ){
        CategoryResponseDTO responseDTO = categoryService.updateCategory(id,categoryRequestDTO);
        return ResponseEntity.ok(responseDTO);


    }


}
