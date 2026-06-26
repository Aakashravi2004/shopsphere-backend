package com.ShopSphere.e_commerce.Repository;

import com.ShopSphere.e_commerce.Entity.Address;
import com.ShopSphere.e_commerce.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Long> {

    List<Address> findByUser(User user);

    Optional<Address> findByIdAndUser(Long addressId, User user);

    Optional<Address> findByUserAndDefaultAddressTrue(User user);

}
