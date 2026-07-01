package com.ShopSphere.e_commerce.Controller;

import com.ShopSphere.e_commerce.Service.ProductService;
import com.ShopSphere.e_commerce.dto.ProductPatchRequestDto;
import com.ShopSphere.e_commerce.dto.ProductRequestDto;
import com.ShopSphere.e_commerce.dto.ProductResponseDto;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<ProductResponseDto> createProduct(@Valid @RequestBody ProductRequestDto productRequestDto) {
        ProductResponseDto productResponseDto = productService.createProduct(productRequestDto);
        return  ResponseEntity.status(HttpStatus.CREATED).body(productResponseDto);
    }

    @GetMapping
    public ResponseEntity<Page<ProductResponseDto>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortOrder ) {
        Page<ProductResponseDto> productResponse = productService.getAllProducts(page, size, sortBy, sortOrder);
        return ResponseEntity.ok(productResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ProductResponseDto>> getAllProductsByCategory(@PathVariable Long categoryId) {
        return ResponseEntity.ok(productService.getProductsByCategory(categoryId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id){
        productService.deleteProduct(id);
        return ResponseEntity.ok("Product deleted");
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDto> updateProduct(@PathVariable Long id,
                                           @Valid @RequestBody ProductRequestDto productRequestDto) {
        System.out.println("UPDATE API HIT");
        return  ResponseEntity.ok(productService.updateProduct(id, productRequestDto));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ProductResponseDto> patchProduct(@PathVariable Long id,
                                                           @RequestBody ProductPatchRequestDto productPatchRequestDto){
        return ResponseEntity.ok(productService.patchProduct(id, productPatchRequestDto));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<ProductResponseDto>> searchProducts(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortOrder) {

        return ResponseEntity.ok(
                productService.searchProducts(keyword, page, size, sortBy, sortOrder)
        );
    }

    @GetMapping("/filter")
    public ResponseEntity<List<ProductResponseDto>> filterProductsByPrice(@RequestParam Double minPrice, @RequestParam Double maxPrice) {
        return ResponseEntity.ok(productService.filterProductsByPriceBetween(minPrice, maxPrice));
    }

    @GetMapping("/filter/category")
    public ResponseEntity<List<ProductResponseDto>> filterProductsByCategory(@RequestParam Long categoryId, @RequestParam Double minPrice, @RequestParam Double maxPrice) {
        return ResponseEntity.ok(productService.filterProductsByCategoryAndPriceBetween(categoryId, minPrice, maxPrice));
    }

    @PostMapping("/{productId}/image")
    public ResponseEntity<ProductResponseDto> uploadProductImage(
            @PathVariable Long productId,
            @RequestParam("file") MultipartFile file) {

        return ResponseEntity.ok(
                productService.uploadProductImage(productId, file));
    }


}
