package com.eCommerce.security.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Data
@AllArgsConstructor
public class LoginResponse {

    private String username;
    private List<String> roles;
    private String jwtToken;


}
