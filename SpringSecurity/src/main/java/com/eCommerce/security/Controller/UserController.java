package com.eCommerce.security.Controller;


import com.eCommerce.base_domains.Entity.User;
import com.eCommerce.security.DTO.UserDTO;
import com.eCommerce.security.Request.LoginRequest;
import com.eCommerce.security.Response.LoginResponse;
import com.eCommerce.security.Service.UserService;
import com.eCommerce.security.jwt.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/security")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserService userService;

    @GetMapping("/hello")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String userController(){
        return "Hello";
    }

    @GetMapping("/ur")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public String user(){
        return "Hello! user";
    }

    @GetMapping("/ar")
    public String admin(){
        return "Hello! admin";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String getAllAdmin() {
        return "Get All By Admin";
    }

    @GetMapping("/user")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public String getAll() {
        return "Get All By User";
    }

    @PostMapping("/create")
    public ResponseEntity<Object> registerUser(@RequestBody UserDTO userDto) {
        User users = userService.save(userDto);
        if (users == null)
            return generateResponse("Not able to save user", HttpStatus.BAD_REQUEST, userDto);
        else
            return generateResponse("User saved successfully: " + users.getId(), HttpStatus.OK, users);
    }



    public ResponseEntity<Object> generateResponse(String message, HttpStatus st, Object responseobj) {

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("message", message);
        map.put("Status", st.value());
        map.put("data", responseobj);

        return new ResponseEntity<Object>(map, st);
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        logger.info("Checking the received login request data: {}", loginRequest.toString());

        Authentication authentication;
        try {
            // Attempt authentication
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUserName(), loginRequest.getPassword())
            );
        } catch (AuthenticationException e) {
            logger.error("Authentication failed for user: {}", loginRequest.getUserName(), e);
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("message", "Bad Credentials");
            responseMap.put("status", false);
            return new ResponseEntity<>(responseMap, HttpStatus.UNAUTHORIZED);
        }

//        SecurityContextHolder.getContext().setAuthentication(authentication);

        String username = authentication.getName();
//        Set<String> roles = authentication.getAuthorities()
//                .stream()
//                .map(GrantedAuthority::getAuthority)
//                .collect(Collectors.toSet());


        String role =authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.toSet()).iterator().next();

        // Generate JWT Token
        String jwtToken;
        try {
            jwtToken = jwtUtils.generateTokenFromUsernameAndRoles(username, role);
            logger.debug("JWT Token generated successfully for user: {}", username);
        } catch (Exception e) {
            logger.error("Failed to generate JWT Token for user: {}", username, e);
            return new ResponseEntity<>("Token generation failed", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // Build the response
        LoginResponse response = new LoginResponse(username, jwtToken);
        logger.info("User signed in successfully: {}", username);
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
