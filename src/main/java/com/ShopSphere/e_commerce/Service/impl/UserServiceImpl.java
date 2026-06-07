package com.ShopSphere.e_commerce.Service.impl;

import com.ShopSphere.e_commerce.Entity.User;
import com.ShopSphere.e_commerce.Exception.InvalidCredentialsException;
import com.ShopSphere.e_commerce.Exception.UserAlreadyExistsException;
import com.ShopSphere.e_commerce.Repository.UserRepository;
import com.ShopSphere.e_commerce.Security.JwtService;
import com.ShopSphere.e_commerce.Service.UserService;
import com.ShopSphere.e_commerce.dto.LoginRequestDto;
import com.ShopSphere.e_commerce.dto.LoginResponseDto;
import com.ShopSphere.e_commerce.dto.UserResponseDto;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    // constructor injection for safer
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Override
    public UserResponseDto registerUser(User user) {
        Optional<User> optionalUser = userRepository.findByEmail(user.getEmail());
        if(optionalUser.isPresent()){
            throw new UserAlreadyExistsException("User" ,user.getEmail());
        }
        // password encoding using BCryptPasswordEncoder
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        User savedUser =  userRepository.save(user);

        // Object creation using Constructor call . here to hide password as response
        return new UserResponseDto(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail()
        );
    }

    @Override
    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        Optional<User> optionalUser = userRepository.findByEmail(loginRequestDto.getEmail());
        if(optionalUser.isEmpty()){
            throw new InvalidCredentialsException("Invalid email or password");
        }
        User user = optionalUser.get();
        if(passwordEncoder.matches(loginRequestDto.getPassword(),user.getPassword() )){
           String token = jwtService.generateToken(user.getEmail());

           // Object creation using Constructor call . here to send response clearly
           return new LoginResponseDto(
                   token,
                   "Login successful"
           );
        }else{
            throw  new InvalidCredentialsException("Invalid email or password");
        }
    }

}
