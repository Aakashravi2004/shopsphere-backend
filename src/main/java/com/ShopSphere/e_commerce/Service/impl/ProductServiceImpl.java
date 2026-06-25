package com.ShopSphere.e_commerce.Service.impl;

import com.ShopSphere.e_commerce.Entity.Category;
import com.ShopSphere.e_commerce.Entity.Product;
import com.ShopSphere.e_commerce.Entity.Review;
import com.ShopSphere.e_commerce.Exception.CategoryAlreadyExistsException;
import com.ShopSphere.e_commerce.Exception.CategoryNotFoundException;
import com.ShopSphere.e_commerce.Exception.ProductNotFoundException;
import com.ShopSphere.e_commerce.Repository.CategoryRepository;
import com.ShopSphere.e_commerce.Repository.ProductRepository;
import com.ShopSphere.e_commerce.Repository.ReviewRepository;
import com.ShopSphere.e_commerce.Service.ProductService;
import com.ShopSphere.e_commerce.Service.RatingService;
import com.ShopSphere.e_commerce.Service.ReviewService;
import com.ShopSphere.e_commerce.dto.ProductPatchRequestDto;
import com.ShopSphere.e_commerce.dto.ProductRequestDto;
import com.ShopSphere.e_commerce.dto.ProductResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final RatingService ratingService;

    public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository, RatingService ratingService) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.ratingService = ratingService;
    }

    @Override
    public ProductResponseDto createProduct(ProductRequestDto productRequestDto) {
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
                savedProduct.getCategory().getName(),
                ratingService.calculateAverageRating(savedProduct.getId())
        );

    }

    @Override
    public Page<ProductResponseDto> getAllProducts(int page, int size, String sortBy, String sortOrder) {

        //Validate the sortBy
        Map<String, String> allowedFields = Map.of(
                "id", "id",
                "name", "name",
                "price", "price",
                "stockquantity", "stockQuantity"
        );

        sortBy = sortBy.trim().toLowerCase();
        String entityField = allowedFields.get(sortBy);

        if (entityField == null) {
            throw new IllegalArgumentException(
                    "Invalid sort field: " + sortBy);
        }

        //create sort object to pass inside the of method
        Sort sort;

        //Validate the sortOrder and sort either asc or desc
        if (sortOrder.equalsIgnoreCase("asc")) {
            sort = Sort.by(entityField).ascending();
        } else if (sortOrder.equalsIgnoreCase("desc")) {
            sort = Sort.by(entityField).descending();
        } else {
            throw new IllegalArgumentException("Invalid sort Order");
        }

        //Create pageable object with pageNum, pageSize, sort object (sorted value)
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Product> products = productRepository.findAll(pageable);

        return products.map(product -> new ProductResponseDto(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStockQuantity(),
                product.getImageUrl(),
                product.getCategory().getId(),
                product.getCategory().getName(),
                ratingService.calculateAverageRating(product.getId())
        ));
    }

    @Override
    public ProductResponseDto getProductById(Long id) {
        Product productFromDb = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("Product with id " + id + " Not Found"));
        return new ProductResponseDto(
                productFromDb.getId(),
                productFromDb.getName(),
                productFromDb.getDescription(),
                productFromDb.getPrice(),
                productFromDb.getStockQuantity(),
                productFromDb.getImageUrl(),
                productFromDb.getCategory().getId(),
                productFromDb.getCategory().getName(),
                ratingService.calculateAverageRating(productFromDb.getId())
        );
    }

    @Override
    public List<ProductResponseDto> getProductsByCategory(Long categoryId) {

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
                        product.getCategory().getName(),
                        ratingService.calculateAverageRating(product.getId())
                ))
                .toList();
    }

    @Override
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product with id " + id + " Not Found"));
        productRepository.delete(product);
    }

    @Override
    public ProductResponseDto updateProduct(Long id,
                                            ProductRequestDto productRequestDto) {
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
                .orElseThrow(() -> new CategoryNotFoundException("Category with id " + productRequestDto.getCategoryId() + " Not Found"));

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
                product.getCategory().getName(),
                ratingService.calculateAverageRating(product.getId())
        );
    }

    @Override
    public ProductResponseDto patchProduct(Long id, ProductPatchRequestDto dto) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product with id " + id + " Not Found"));

        if (dto.getName() != null) {
            product.setName(dto.getName());
        }
        if (dto.getDescription() != null) {
            product.setDescription(dto.getDescription());
        }
        if (dto.getPrice() != null) {
            product.setPrice(dto.getPrice());
        }
        if (dto.getStockQuantity() != null) {
            product.setStockQuantity(dto.getStockQuantity());
        }
        if (dto.getImageUrl() != null) {
            product.setImageUrl(dto.getImageUrl());
        }
        if (dto.getCategoryId() != null) {
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
                product.getCategory().getName(),
                ratingService.calculateAverageRating(product.getId())
        );
    }

    @Override
    public Page<ProductResponseDto> searchProducts(String keyword, int page, int size, String sortBy, String sortOrder) {

        //Validate the sortBy
        Map<String, String> allowedFields = Map.of(
                "id", "id",
                "name", "name",
                "price", "price",
                "stockquantity", "stockQuantity"
        );

        sortBy = sortBy.trim().toLowerCase();
        String entityField = allowedFields.get(sortBy);

        if (entityField == null) {
            throw new IllegalArgumentException(
                    "Invalid sort field: " + sortBy);
        }

        //create sort object to pass inside the of method
        Sort sort;

        //Validate the sortOrder and sort either asc or desc
        if (sortOrder.equalsIgnoreCase("asc")) {
            sort = Sort.by(entityField).ascending();
        } else if (sortOrder.equalsIgnoreCase("desc")) {
            sort = Sort.by(entityField).descending();
        } else {
            throw new IllegalArgumentException("Invalid sort Order");
        }
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Product> products = productRepository.findByNameContainingIgnoreCase(keyword, pageable);
        return products.map(product -> new ProductResponseDto(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStockQuantity(),
                product.getImageUrl(),
                product.getCategory().getId(),
                product.getCategory().getName(),
                ratingService.calculateAverageRating(product.getId())
        ));
    }

    @Override
    public List<ProductResponseDto> filterProductsByPriceBetween(Double minPrice, Double maxPrice) {
        if (minPrice > maxPrice) {
            throw new IllegalArgumentException("Minimum price cannot be greater than maximum price");
        }
        List<Product> products = productRepository.findByPriceBetween(minPrice, maxPrice);
        return products.stream().map(product -> new ProductResponseDto(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStockQuantity(),
                product.getImageUrl(),
                product.getCategory().getId(),
                product.getCategory().getName(),
                ratingService.calculateAverageRating(product.getId())
        )).toList();
    }

    @Override
    public List<ProductResponseDto> filterProductsByCategoryAndPriceBetween(Long categoryId, Double minPrice, Double maxPrice) {

        categoryRepository.findById(categoryId).orElseThrow(() -> new CategoryNotFoundException("Category with id " + categoryId + " Not Found"));

        if (minPrice > maxPrice) {
            throw new IllegalArgumentException("Minimum price cannot be greater than maximum price");
        }
        List<Product> products = productRepository.findByCategoryIdAndPriceBetween(categoryId, minPrice, maxPrice);

        return products.stream().map(product -> new ProductResponseDto(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStockQuantity(),
                product.getImageUrl(),
                product.getCategory().getId(),
                product.getCategory().getName(),
                ratingService.calculateAverageRating(product.getId())
        )).toList();
    }

}
