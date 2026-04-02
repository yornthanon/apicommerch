package com.springboot.relationship.Controller;

import com.springboot.relationship.DTO.RequestDTO.ProductRequestDTO;
import com.springboot.relationship.DTO.ResponeDTO.ProductResponseDTO;
import com.springboot.relationship.Entity.Product;
import com.springboot.relationship.Service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductResponseDTO> createProduct(
            @Valid @ModelAttribute ProductRequestDTO productRequestDTO,
            HttpServletRequest request
    ) {
        ProductResponseDTO dto = productService.createProduct(productRequestDTO);
        return ResponseEntity.ok(responseClient(dto, request));
    }

    @GetMapping("/getall")
    public ResponseEntity<List<ProductResponseDTO>> getAllProduct(HttpServletRequest request) {
        List<Product> productList = productService.getAllProducts();
        List<ProductResponseDTO> response = productList.stream()
                .map(productService::mappToResponseDTO)
                .map(dto -> responseClient(dto, request))
                .toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getbyid/{id}")
    public ResponseEntity<ProductResponseDTO> getProductById(
            @PathVariable Long id,
            HttpServletRequest request
    ) {
        ProductResponseDTO dto = productService.getProductById(id);
        return ResponseEntity.ok(responseClient(dto, request));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ProductResponseDTO>> findProductByCategoryId(
            @PathVariable Long categoryId,
            HttpServletRequest request
    ) {
        List<ProductResponseDTO> dtoList = productService.getAllProductsByCategoryId(categoryId);
        List<ProductResponseDTO> response = dtoList.stream()
                .map(dto -> responseClient(dto, request))
                .toList();
        return ResponseEntity.ok(response);
    }

    @PutMapping(value = "/update/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductResponseDTO> updateProduct(
            @PathVariable Long id,
            @Valid @ModelAttribute ProductRequestDTO productRequestDTO,
            HttpServletRequest request
    ) {
        ProductResponseDTO dto = productService.updateProduct(id, productRequestDTO);
        return ResponseEntity.ok(responseClient(dto, request));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    private ProductResponseDTO responseClient(ProductResponseDTO dto, HttpServletRequest request) {
        if (dto.getImageUrl() != null && !dto.getImageUrl().isBlank()) {
            dto.setImageUrl(
                    ServletUriComponentsBuilder.fromContextPath(request)
                            .path("/upload/")
                            .path(dto.getImageUrl())
                            .toUriString()
            );
        }
        return dto;
    }
}