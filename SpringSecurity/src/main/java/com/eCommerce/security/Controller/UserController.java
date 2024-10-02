package com.eCommerce.security.Controller;


import com.eCommerce.base_domains.Entity.User;
import com.eCommerce.security.DTO.UserDTO;
import com.eCommerce.security.Request.LoginRequest;
import com.eCommerce.security.Response.LoginResponse;
import com.eCommerce.security.Service.UserService;
import com.eCommerce.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping
public class UserController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserService userService;

    @GetMapping("/hello")
    public String userController(){
        return "Hello";
    }

    @GetMapping("/ur")
    public String user(){
        return "Hello! user";
    }

    @GetMapping("/ar")
    public String admin(){
        return "Hello! admin";
    }

    @PostMapping("/create")
    public ResponseEntity<User> createUser(@RequestBody UserDTO userDTO) {
        User user = new User(userDTO.getUsername(), userDTO.getPassword());
        User createdUser = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }


    @PostMapping("/api/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest){
        Authentication authentication;
        try{
            authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken
                            (loginRequest.getUsername(), loginRequest.getPassword()));
        }
        catch (AuthenticationException e){
            Map<String, Object> map = new HashMap<>();
            map.put("message", "Bad Credentials");
            map.put("status", false);
            return new ResponseEntity<Object>(map, HttpStatus.NOT_FOUND);
        }
//
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//
//        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        String jwtToken = jwtUtils.generateTokenFromUsername(loginRequest.getUsername());

//        List<String> roles = userDetails.getAuthorities().stream()
//                .map(item -> item.getAuthority())
//                .collect(Collectors.toList());

//        List<String> roles = userDetails.getAuthorities().stream()
//                .map(GrantedAuthority::getAuthority)
//                .toList();

        LoginResponse response = new LoginResponse(loginRequest.getUsername(), jwtToken);

        return ResponseEntity.ok(response);

    }

    @GetMapping("/validate")
    public String validateToken(@RequestParam("token") String token) {
        if(jwtUtils.validateJwtToken(token)){
            return "Token is valid";
        }
        return "Invalid";
    }



}
