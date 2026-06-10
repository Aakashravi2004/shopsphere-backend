package com.ShopSphere.e_commerce.Repository;

import com.ShopSphere.e_commerce.Entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

//    save()
//    findById()
//    findAll()
//    deleteById()
//    existsById()

    // own method to prevent category name conflict
    Optional<Category> findByNameIgnoreCase(String name);

}
