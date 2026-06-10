package com.ShopSphere.e_commerce.Service.impl;

import com.ShopSphere.e_commerce.Entity.Category;
import com.ShopSphere.e_commerce.Exception.CategoryAlreadyExistsException;
import com.ShopSphere.e_commerce.Exception.CategoryNotFoundException;
import com.ShopSphere.e_commerce.Repository.CategoryRepository;
import com.ShopSphere.e_commerce.Service.CategoryService;
import com.ShopSphere.e_commerce.dto.CategoryRequestDto;
import com.ShopSphere.e_commerce.dto.CategoryResponseDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public CategoryResponseDto createCategory(CategoryRequestDto categoryRequestDto){
        Optional<Category> optionalCategory = categoryRepository.findByNameIgnoreCase( categoryRequestDto.getName());

        if(optionalCategory.isPresent()){
            throw new CategoryAlreadyExistsException("Category", categoryRequestDto.getName());
        }

        Category category = new Category();
        category.setName(categoryRequestDto.getName());
        category.setDescription(categoryRequestDto.getDescription());

        Category savedCategory =  categoryRepository.save(category);

        return new  CategoryResponseDto(
                savedCategory.getId(),
                savedCategory.getName(),
                savedCategory.getDescription()
        );

    }

    @Override
    public List<CategoryResponseDto> getAllCategories(){
        List<CategoryResponseDto> responseList = new ArrayList<>();

        List<Category> categories = categoryRepository.findAll();

        for(Category category : categories){
            responseList.add(new  CategoryResponseDto(
                    category.getId(),
                    category.getName(),
                    category.getDescription()
            ));
        }
        return responseList;
    }

    @Override
    public CategoryResponseDto getCategoryById(Long id){
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category With id " + id + " Not Found"));

        return new  CategoryResponseDto(
                category.getId(),
                category.getName(),
                category.getDescription()
        );
    }

}
