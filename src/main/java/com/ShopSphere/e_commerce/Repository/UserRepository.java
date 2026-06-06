package com.ShopSphere.e_commerce.Repository;

import com.ShopSphere.e_commerce.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

//    save(user);
//    findById(id);
//    findAll();
//    deleteById(id);
//    existsById(id);

    //    Custom query
    Optional<User> findByEmail(String email);

}
