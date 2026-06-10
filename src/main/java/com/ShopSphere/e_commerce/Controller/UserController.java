package com.ShopSphere.e_commerce.Controller;

import com.ShopSphere.e_commerce.Entity.User;
import com.ShopSphere.e_commerce.Service.UserService;
import com.ShopSphere.e_commerce.dto.LoginRequestDto;
import com.ShopSphere.e_commerce.dto.LoginResponseDto;
import com.ShopSphere.e_commerce.dto.UserRequestDto;
import com.ShopSphere.e_commerce.dto.UserResponseDto;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    // I will not use @AutoWired annotations i use constructor injection
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public UserResponseDto userRegister(@Valid @RequestBody UserRequestDto userRequestDto){
        User user = new User();
        user.setName(userRequestDto.getName());
        user.setEmail(userRequestDto.getEmail());
        user.setPassword(userRequestDto.getPassword());
        return userService.registerUser(user);
    }

    @PostMapping("/login")
    public LoginResponseDto login(@Valid @RequestBody LoginRequestDto loginRequestDto){
        return userService.login(loginRequestDto);

    }
}
