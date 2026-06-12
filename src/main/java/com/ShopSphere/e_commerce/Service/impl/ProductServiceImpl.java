package com.ShopSphere.e_commerce.Service.impl;

import com.ShopSphere.e_commerce.Entity.Category;
import com.ShopSphere.e_commerce.Entity.Product;
import com.ShopSphere.e_commerce.Exception.CategoryNotFoundException;
import com.ShopSphere.e_commerce.Exception.ProductNotFoundException;
import com.ShopSphere.e_commerce.Repository.CategoryRepository;
import com.ShopSphere.e_commerce.Repository.ProductRepository;
import com.ShopSphere.e_commerce.Service.ProductService;
import com.ShopSphere.e_commerce.dto.ProductPatchRequestDto;
import com.ShopSphere.e_commerce.dto.ProductRequestDto;
import com.ShopSphere.e_commerce.dto.ProductResponseDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    private final CategoryRepository categoryRepository;

    public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public ProductResponseDto createProduct(ProductRequestDto productRequestDto){
        Category category = categoryRepository.findById(productRequestDto.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));

        Product product = new Product();

        product.setName(productRequestDto.getName());
        product.setDescription(productRequestDto.getDescription());
        product.setPrice(productRequestDto.getPrice());
        product.setStockQuantity(productRequestDto.getStockQuantity());
        product.setImageUrl(productRequestDto.getImageUrl());

        product.setCategory(category);

        Product savedProduct = productRepository.save(product);

        return new ProductResponseDto(
                savedProduct.getId(),
                savedProduct.getName(),
                savedProduct.getDescription(),
                savedProduct.getPrice(),
                savedProduct.getStockQuantity(),
                savedProduct.getImageUrl(),
                savedProduct.getCategory().getId(),
                savedProduct.getCategory().getName()
        );

    }

    @Override
    public List<ProductResponseDto> getAllProducts(){

        return productRepository.findAll()
                .stream()
                .map(product -> new ProductResponseDto(
                        product.getId(),
                        product.getName(),
                        product.getDescription(),
                        product.getPrice(),
                        product.getStockQuantity(),
                        product.getImageUrl(),
                        product.getCategory().getId(),
                        product.getCategory().getName()
                ))
                .toList();
    }

    @Override
    public ProductResponseDto getProductById(Long id){
        Product productFromDb  = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("Product with id " + id + " Not Found"));
        return new ProductResponseDto(
                productFromDb.getId(),
                productFromDb.getName(),
                productFromDb.getDescription(),
                productFromDb.getPrice(),
                productFromDb.getStockQuantity(),
                productFromDb.getImageUrl(),
                productFromDb.getCategory().getId(),
                productFromDb.getCategory().getName()
        );
    }

    @Override
    public List<ProductResponseDto> getProductsByCategory(Long categoryId){

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("Category With " + categoryId + " not found"));

       return productRepository.findByCategoryId(categoryId)
               .stream()
               .map(product -> new ProductResponseDto(
                       product.getId(),
                       product.getName(),
                       product.getDescription(),
                       product.getPrice(),
                       product.getStockQuantity(),
                       product.getImageUrl(),
                       product.getCategory().getId(),
                       product.getCategory().getName()
               ))
               .toList();
    }

    @Override
    public void deleteProduct(Long id){
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product with id " + id + " Not Found"));
        productRepository.delete(product);
    }

    @Override
    public ProductResponseDto updateProduct(Long id,
                                     ProductRequestDto productRequestDto){
        // step 1 : checking the product
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product with id " + id + " Not Found"));

        // step 2 : update the product
        existingProduct.setName(productRequestDto.getName());
        existingProduct.setDescription(productRequestDto.getDescription());
        existingProduct.setPrice(productRequestDto.getPrice());
        existingProduct.setStockQuantity(productRequestDto.getStockQuantity());
        existingProduct.setImageUrl(productRequestDto.getImageUrl());

        // step 3 : Validate that the category exists before assigning it to the product
        Category category = categoryRepository.findById(productRequestDto.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException("Category with id " + productRequestDto.getCategoryId() +  " Not Found"));

        // step 4 : If the category is available update that also
        existingProduct.setCategory(category);

        // step 5 : save the product to update the product in database
        Product product = productRepository.save(existingProduct);

        // step 6 : send proper response to controller
        return new ProductResponseDto(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStockQuantity(),
                product.getImageUrl(),
                product.getCategory().getId(),
                product.getCategory().getName()
        );
    }

    @Override
    public ProductResponseDto patchProduct(Long id, ProductPatchRequestDto dto){

        Product product = productRepository.findById(id)
                .orElseThrow(() ->  new ProductNotFoundException("Product with id " + id + " Not Found"));

        if(dto.getName() != null){
            product.setName(dto.getName());
        }
        if(dto.getDescription() != null){
            product.setDescription(dto.getDescription());
        }
        if(dto.getPrice() != null){
            product.setPrice(dto.getPrice());
        }
        if(dto.getStockQuantity() != null){
            product.setStockQuantity(dto.getStockQuantity());
        }
        if(dto.getImageUrl() != null){
            product.setImageUrl(dto.getImageUrl());
        }
        if(dto.getCategoryId() != null){
            Category category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new CategoryNotFoundException("Category with id " + dto.getCategoryId() + " Not Found"));

            product.setCategory(category);
        }

        productRepository.save(product);

        return new ProductResponseDto(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStockQuantity(),
                product.getImageUrl(),
                product.getCategory().getId(),
                product.getCategory().getName()
        );
    }

}
