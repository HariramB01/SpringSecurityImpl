package com.eCommerce.security.Response;

import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    private String userName;
    private String jwtToken;
//    private List<String> roles;

}