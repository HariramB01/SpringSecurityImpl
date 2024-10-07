package com.eCommerce.security.Request;

import lombok.*;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    private String userName;

    private String password;

}