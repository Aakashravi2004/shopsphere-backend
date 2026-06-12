package com.ShopSphere.e_commerce.Repository;

import com.ShopSphere.e_commerce.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

//    save()
//    findById()
//    findAll()
//    deleteById()
//    existsById()

    //custom query for fetch all product based on category
    List<Product> findByCategoryId(Long categoryId);
    boolean existsByCategoryId(Long categoryId);

}
