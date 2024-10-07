package com.eCommerce.security.DTO;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDTO {

    private String userName;
    private String password;
    private String email;
    private String role;

}