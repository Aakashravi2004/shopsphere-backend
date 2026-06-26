package com.ShopSphere.e_commerce.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddressResponseDto {

    private Long addressId;
    private String fullName;
    private String mobileNumber;
    private String houseNo;
    private String street;
    private String city;
    private String state;
    private String pincode;
    private String country;
    private Boolean defaultAddress;
    private LocalDateTime createdAt;

}
