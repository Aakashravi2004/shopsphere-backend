package com.ShopSphere.e_commerce.Service.impl;

import com.ShopSphere.e_commerce.Entity.Address;
import com.ShopSphere.e_commerce.Entity.User;
import com.ShopSphere.e_commerce.Exception.AddressNotFoundException;
import com.ShopSphere.e_commerce.Exception.UserNotFoundException;
import com.ShopSphere.e_commerce.Repository.AddressRepository;
import com.ShopSphere.e_commerce.Repository.UserRepository;
import com.ShopSphere.e_commerce.Service.AddressService;
import com.ShopSphere.e_commerce.dto.AddressRequestDto;
import com.ShopSphere.e_commerce.dto.AddressResponseDto;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    public AddressServiceImpl(AddressRepository addressRepository, UserRepository userRepository) {
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public AddressResponseDto addAddress(AddressRequestDto addressRequestDto){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with email "+ email + " not found"));

        Address address = new Address();
        address.setFullName(addressRequestDto.getFullName());
        address.setMobileNumber(addressRequestDto.getMobileNumber());
        address.setHouseNo(addressRequestDto.getHouseNo());
        address.setStreet(addressRequestDto.getStreet());
        address.setCity(addressRequestDto.getCity());
        address.setState(addressRequestDto.getState());
        address.setPincode(addressRequestDto.getPincode());
        address.setCountry(addressRequestDto.getCountry());

        List<Address> addresses = addressRepository.findByUser(user);

        if(addresses.isEmpty()){
            address.setDefaultAddress(true);
        }else {
            if(addressRequestDto.getDefaultAddress()){

                Optional<Address> optionalAddress = addressRepository.findByUserAndDefaultAddressTrue(user);

                optionalAddress.ifPresent(oldAddress -> {
                    oldAddress.setDefaultAddress(false);
                    addressRepository.save(oldAddress);
                });
            }
            address.setDefaultAddress(addressRequestDto.getDefaultAddress());
        }


        address.setCreatedAt(LocalDateTime.now());
        address.setUser(user);

        Address savedAddress = addressRepository.save(address);

        return new AddressResponseDto(
                savedAddress.getId(),
                savedAddress.getFullName(),
                savedAddress.getMobileNumber(),
                savedAddress.getHouseNo(),
                savedAddress.getStreet(),
                savedAddress.getCity(),
                savedAddress.getState(),
                savedAddress.getPincode(),
                savedAddress.getCountry(),
                savedAddress.getDefaultAddress(),
                savedAddress.getCreatedAt()
        );
    }

    @Override
    public List<AddressResponseDto> getMyAddresses(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with email "+ email + " not found"));

        List<Address> addresses = addressRepository.findByUser(user);

        return addresses.stream()
                .map(address -> new AddressResponseDto(
                    address.getId(),
                    address.getFullName(),
                    address.getMobileNumber(),
                    address.getHouseNo(),
                    address.getStreet(),
                    address.getCity(),
                    address.getState(),
                    address.getPincode(),
                    address.getCountry(),
                    address.getDefaultAddress(),
                    address.getCreatedAt()
            )).toList();
    }

    @Override
    @Transactional
    public AddressResponseDto updateAddress(Long addressId, AddressRequestDto addressRequestDto){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with email "+ email + " not found"));

        //ownerShip validation
        Address address = addressRepository.findByIdAndUser(addressId, user)
                .orElseThrow(() -> new AddressNotFoundException("Address not found"));

        address.setFullName(addressRequestDto.getFullName());
        address.setMobileNumber(addressRequestDto.getMobileNumber());
        address.setHouseNo(addressRequestDto.getHouseNo());
        address.setStreet(addressRequestDto.getStreet());
        address.setCity(addressRequestDto.getCity());
        address.setState(addressRequestDto.getState());
        address.setPincode(addressRequestDto.getPincode());
        address.setCountry(addressRequestDto.getCountry());

        if (addressRequestDto.getDefaultAddress()) {

            Optional<Address> optionalAddress =
                    addressRepository.findByUserAndDefaultAddressTrue(user);

            optionalAddress.ifPresent(oldAddress -> {

                if (!oldAddress.getId().equals(address.getId())) {
                    oldAddress.setDefaultAddress(false);
                    addressRepository.save(oldAddress);
                }

            });

            address.setDefaultAddress(true);

        } else {

            address.setDefaultAddress(false);

        }

        Address savedAddress = addressRepository.save(address);

        return new AddressResponseDto(
                savedAddress.getId(),
                savedAddress.getFullName(),
                savedAddress.getMobileNumber(),
                savedAddress.getHouseNo(),
                savedAddress.getStreet(),
                savedAddress.getCity(),
                savedAddress.getState(),
                savedAddress.getPincode(),
                savedAddress.getCountry(),
                savedAddress.getDefaultAddress(),
                savedAddress.getCreatedAt()
        );

    }

    @Override
    @Transactional
    public void deleteAddress(Long addressId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with email "+ email + " not found"));

        Address address = addressRepository.findByIdAndUser(addressId, user)
                .orElseThrow(() -> new AddressNotFoundException("Address not found"));


        if(address.getDefaultAddress()){

            List<Address> addresses = addressRepository.findByUser(user);

            for(Address address1 : addresses){
                // skip the address that is being deleted
                if(!address1.getId().equals(address.getId())){
                    address1.setDefaultAddress(true);
                    addressRepository.save(address1);
                    break;
                }
            }
        }
        // delete the requested address
        addressRepository.delete(address);
    }
}
