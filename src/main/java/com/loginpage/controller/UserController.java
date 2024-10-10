package com.loginpage.controller;

import com.loginpage.dto.LoginDto;
import com.loginpage.dto.SignUpDto;
import com.loginpage.entity.User;
import com.loginpage.repository.UserRepository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserRepository userRepo;


    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignUpDto signUpDto){

        Boolean emailExist = userRepo.existsByEmail(signUpDto.getEmail());
        if(emailExist){
            return new ResponseEntity<>("Email id Exist", HttpStatus.BAD_REQUEST);
        }
        Boolean emailUserName = userRepo.existsByUsername(signUpDto.getUsername());
        if(emailUserName) {
            return new ResponseEntity<>("Username Exist", HttpStatus.BAD_REQUEST);
        }
        User user = new User();
        user.setName(signUpDto.getName());
        user.setEmail(signUpDto.getEmail());
        user.setUsername(signUpDto.getUsername());
        user.setPassword(signUpDto.getPassword());

        userRepo.save(user);
        return new ResponseEntity<>("User is registered", HttpStatus.CREATED);

    }
    @PostMapping("/signin")
    public ResponseEntity<String> authenticateUser(@RequestBody LoginDto loginDto) {

        User authentication = authenticate(loginDto.getEmail(), loginDto.getPassword());

        if (authentication != null) {

            return ResponseEntity.ok("Sign-in successful!");
        } else {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password!");
        }
    }

    private User authenticate(String email, String password) {
        return userRepo.findByEmailAndPassword(email, password).orElse(null);
    }

}

