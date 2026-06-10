package com.ShopSphere.e_commerce.Service;

import com.ShopSphere.e_commerce.dto.CategoryRequestDto;
import com.ShopSphere.e_commerce.dto.CategoryResponseDto;

import java.util.List;

public interface CategoryService {

    CategoryResponseDto createCategory(CategoryRequestDto categoryRequestDto);
    List<CategoryResponseDto> getAllCategories();
    CategoryResponseDto getCategoryById(Long id);

}
