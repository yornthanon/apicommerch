package com.springboot.relationship.Service.Implement;

import com.springboot.relationship.Configuration.UploadImage;
import com.springboot.relationship.DTO.RequestDTO.ProductRequestDTO;
import com.springboot.relationship.DTO.ResponeDTO.ProductResponseDTO;
import com.springboot.relationship.Entity.Category;
import com.springboot.relationship.Entity.Product;
import com.springboot.relationship.Exception.ResourceNotFoundException;
import com.springboot.relationship.Repository.CategoryRepository;
import com.springboot.relationship.Repository.ProductRepository;
import com.springboot.relationship.Service.ProductService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceImplement implements ProductService {
     CategoryRepository categoryRepository;
     ProductRepository productRepository;
     UploadImage uploadImage;
     public ProductServiceImplement (
             CategoryRepository categoryRepository,
             ProductRepository productRepository ,
             UploadImage uploadImage) {
         this.categoryRepository = categoryRepository;
         this.productRepository = productRepository;
         this.uploadImage = uploadImage;
     }

    @Override
    public ProductResponseDTO createProduct(ProductRequestDTO productRequestDTO) {
        Category category = categoryRepository.findById(productRequestDTO.getCategoryId())
                .orElseThrow(()-> new ResourceNotFoundException("Not found Category"));

        if (productRequestDTO.getImageUrl() == null || productRequestDTO.getImageUrl().isEmpty()) {
            throw new IllegalArgumentException("Product image is required");
        }

        Product product = new Product();
        product.setName(productRequestDTO.getName());
        product.setPrice(productRequestDTO.getPrice());
        product.setDescription(productRequestDTO.getDescription());
        product.setStock(productRequestDTO.getStock());
        String image = uploadImage.SaveImage(productRequestDTO.getImageUrl());
        product.setImageUrl(image);

        product.setCategory(category);
        Product savedProduct = productRepository.saveAndFlush(product);

        return mappToResponseDTO(savedProduct);
    }

    @Override
    public List<Product> getAllProducts() {
        List<Product> allProduct = productRepository.findAll().stream().toList();
        List<Product> responseDTOList = new ArrayList<>();
        for (Product product : allProduct) {
            ProductResponseDTO responseDTO = mappToResponseDTO(product);
            responseDTOList.add(product);
        }
        return responseDTOList;
    }
    @Override
    public ProductResponseDTO getProductById(Long id) {
         Product getById = productRepository.findById(id)
                 .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
         return mappToResponseDTO(getById);

    }

    @Override
    public List<ProductResponseDTO> getAllProductsByCategoryId(Long id) {
       return productRepository.findByCategoryId(id)
               .stream()
               .map(this::mappToResponseDTO)
               .toList();

    }

    @Override
    public ProductResponseDTO updateProduct(Long id, ProductRequestDTO productRequestDTO) {
         Product product = productRepository.findById(id)
                 .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
         Category category = categoryRepository.findById(productRequestDTO.getCategoryId())
                 .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + productRequestDTO.getCategoryId()));
         product.setName(productRequestDTO.getName());
         product.setPrice(productRequestDTO.getPrice());
         product.setDescription(productRequestDTO.getDescription());
         product.setStock(productRequestDTO.getStock());
         String image = uploadImage.SaveImage(productRequestDTO.getImageUrl());
         product.setImageUrl(image);
         product.setCategory(category);
         Product updatedProduct = productRepository.save(product);

        return mappToResponseDTO(updatedProduct);
    }


@Override
public void deleteProduct(Long id) {
    Product product = productRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

    productRepository.delete(product);


    }

    @Override
    public ProductResponseDTO mappToResponseDTO(Product product) {
         ProductResponseDTO productResponseDTO = new ProductResponseDTO();
         productResponseDTO.setId(product.getId());
         productResponseDTO.setName(product.getName());
         productResponseDTO.setPrice(product.getPrice());
         productResponseDTO.setDescription(product.getDescription());
         productResponseDTO.setImageUrl(product.getImageUrl());
         productResponseDTO.setStock(product.getStock());

         productResponseDTO.setCategoryId(product.getCategory().getId());
         productResponseDTO.setCategoryName(product.getCategory().getName());
        return productResponseDTO;
    }
}