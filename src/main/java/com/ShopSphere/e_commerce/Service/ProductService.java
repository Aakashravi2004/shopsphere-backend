package com.ShopSphere.e_commerce.Service;

import com.ShopSphere.e_commerce.dto.ProductPatchRequestDto;
import com.ShopSphere.e_commerce.dto.ProductRequestDto;
import com.ShopSphere.e_commerce.dto.ProductResponseDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {

    ProductResponseDto createProduct(ProductRequestDto productRequestDto);
    Page<ProductResponseDto> getAllProducts(int page, int size, String sortBy,  String sortOrder);
    ProductResponseDto getProductById(Long id);
    List<ProductResponseDto> getProductsByCategory(Long categoryId);
    void deleteProduct(Long id);
    ProductResponseDto updateProduct(Long id, ProductRequestDto productRequestDto);
    ProductResponseDto patchProduct(Long id, ProductPatchRequestDto dto);
    List<ProductResponseDto> searchProducts(String keyword);

}
