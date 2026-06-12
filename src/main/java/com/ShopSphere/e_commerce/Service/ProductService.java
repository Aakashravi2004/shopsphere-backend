package com.ShopSphere.e_commerce.Service;

import com.ShopSphere.e_commerce.dto.ProductPatchRequestDto;
import com.ShopSphere.e_commerce.dto.ProductRequestDto;
import com.ShopSphere.e_commerce.dto.ProductResponseDto;

import java.util.List;

public interface ProductService {

    ProductResponseDto createProduct(ProductRequestDto productRequestDto);
    List<ProductResponseDto> getAllProducts();
    ProductResponseDto getProductById(Long id);
    List<ProductResponseDto> getProductsByCategory(Long categoryId);
    void deleteProduct(Long id);
    ProductResponseDto updateProduct(Long id, ProductRequestDto productRequestDto);
    ProductResponseDto patchProduct(Long id, ProductPatchRequestDto dto);

}
