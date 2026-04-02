package com.springboot.relationship.Service;

import com.springboot.relationship.DTO.RequestDTO.ProductRequestDTO;
import com.springboot.relationship.DTO.ResponeDTO.ProductResponseDTO;
import com.springboot.relationship.Entity.Product;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {

    ProductResponseDTO createProduct(ProductRequestDTO productRequestDTO);

    List<Product> getAllProducts();

    ProductResponseDTO getProductById(Long id);

    List<ProductResponseDTO> getAllProductsByCategoryId(Long id);

    ProductResponseDTO updateProduct(Long id, ProductRequestDTO productRequestDTO);

    void deleteProduct(Long id);

    ProductResponseDTO mappToResponseDTO(Product product);
}

