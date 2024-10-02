package com.eCommerce.security.DTO;


import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDTO {
    private String username;
    private String password;
    private List<String> authorities; // List of authority names

}
