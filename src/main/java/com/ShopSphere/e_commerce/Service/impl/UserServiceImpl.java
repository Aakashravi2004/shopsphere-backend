package com.ShopSphere.e_commerce.Service.impl;

import com.ShopSphere.e_commerce.Entity.User;
import com.ShopSphere.e_commerce.Exception.InvalidCredentialsException;
import com.ShopSphere.e_commerce.Exception.UserAlreadyExistsException;
import com.ShopSphere.e_commerce.Repository.UserRepository;
import com.ShopSphere.e_commerce.Service.UserService;
import com.ShopSphere.e_commerce.dto.LoginRequestDto;
import com.ShopSphere.e_commerce.dto.UserResponseDto;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // constructor injection for safer
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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

        UserResponseDto userResponseDto = new UserResponseDto(); // to hide password ,because it is sensitive data
        userResponseDto.setId(savedUser.getId());
        userResponseDto.setName(savedUser.getName());
        userResponseDto.setEmail(savedUser.getEmail());

        return userResponseDto;
    }

    @Override
    public String login(LoginRequestDto loginRequestDto) {
        Optional<User> optionalUser = userRepository.findByEmail(loginRequestDto.getEmail());
        if(optionalUser.isEmpty()){
            throw new InvalidCredentialsException("Invalid email or password");
        }
        User user = optionalUser.get();
        if(passwordEncoder.matches(loginRequestDto.getPassword(),user.getPassword() )){
            return "Login Successful";
        }else{
            throw  new InvalidCredentialsException("Invalid email or password");
        }
    }

}
