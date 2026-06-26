package com.ShopSphere.e_commerce.Service;

import com.ShopSphere.e_commerce.dto.AddressRequestDto;
import com.ShopSphere.e_commerce.dto.AddressResponseDto;

import java.util.List;

public interface AddressService {

    AddressResponseDto addAddress(AddressRequestDto addressRequestDto);

    List<AddressResponseDto> getMyAddresses();

    AddressResponseDto updateAddress(Long addressId, AddressRequestDto addressRequestDto);

    void deleteAddress(Long addressId);
}
