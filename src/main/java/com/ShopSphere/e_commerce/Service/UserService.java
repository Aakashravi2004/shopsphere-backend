package com.ShopSphere.e_commerce.Service;

import com.ShopSphere.e_commerce.Entity.User;
import com.ShopSphere.e_commerce.dto.LoginRequestDto;
import com.ShopSphere.e_commerce.dto.LoginResponseDto;
import com.ShopSphere.e_commerce.dto.UserResponseDto;
import org.springframework.stereotype.Service;

public interface UserService {

    public UserResponseDto registerUser(User user);
    public LoginResponseDto login(LoginRequestDto loginRequestDto);
}
