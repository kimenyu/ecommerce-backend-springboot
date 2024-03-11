package com.kimenyu.ecommerce.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.kimenyu.ecommerce.dto.ResponseDto;
import com.kimenyu.ecommerce.dto.user.SignInDto;
import com.kimenyu.ecommerce.dto.user.SignInResponseDto;
import com.kimenyu.ecommerce.dto.user.SignupDto;
import com.kimenyu.ecommerce.entity.User;
import com.kimenyu.ecommerce.exceptions.AuthenticationFailException;
import com.kimenyu.ecommerce.exceptions.CustomException;
import com.kimenyu.ecommerce.repository.UserRepository;
import com.kimenyu.ecommerce.service.AuthenticationService;
import com.kimenyu.ecommerce.service.UserService;

import java.util.List;

@RequestMapping("user")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    UserService userService;

    @GetMapping("/all")
    public List<User> findAllUser(@RequestParam("token") String token) throws AuthenticationFailException {
        authenticationService.authenticate(token);
        return userRepository.findAll();
    }

    @PostMapping("/signup")
    public ResponseDto Signup(@RequestBody SignupDto signupDto) throws CustomException {
        return userService.signUp(signupDto);
    }

    //TODO token should be updated
    @PostMapping("/signIn")
    public SignInResponseDto Signup(@RequestBody SignInDto signInDto) throws CustomException {
        return userService.signIn(signInDto);
    }

//    @PostMapping("/updateUser")
//    public ResponseDto updateUser(@RequestParam("token") String token, @RequestBody UserUpdateDto userUpdateDto) {
//        authenticationService.authenticate(token);
//        return userService.updateUser(token, userUpdateDto);
//    }


//    @PostMapping("/createUser")
//    public ResponseDto updateUser(@RequestParam("token") String token, @RequestBody UserCreateDto userCreateDto)
//            throws CustomException, AuthenticationFailException {
//        authenticationService.authenticate(token);
//        return userService.createUser(token, userCreateDto);
//    }
}