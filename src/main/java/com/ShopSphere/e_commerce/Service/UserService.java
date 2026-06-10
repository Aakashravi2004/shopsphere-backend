package com.ShopSphere.e_commerce.Service;

import com.ShopSphere.e_commerce.Entity.Category;
import com.ShopSphere.e_commerce.Entity.User;
import com.ShopSphere.e_commerce.dto.CategoryResponseDto;
import com.ShopSphere.e_commerce.dto.LoginRequestDto;
import com.ShopSphere.e_commerce.dto.LoginResponseDto;
import com.ShopSphere.e_commerce.dto.UserResponseDto;
import org.springframework.stereotype.Service;

public interface UserService {

     UserResponseDto registerUser(User user);
     LoginResponseDto login(LoginRequestDto loginRequestDto);

}
