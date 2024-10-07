package com.eCommerce.security.Service;

import com.eCommerce.base_domains.Entity.Role;
import com.eCommerce.base_domains.Entity.User;
import com.eCommerce.security.Repository.RoleRepository;
import com.eCommerce.security.Repository.UserRepository;
import com.eCommerce.security.DTO.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private RoleRepository roleRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;



    public User save(UserDTO userRegisteredDTO) {
        Role role = new Role();
        if(userRegisteredDTO.getRole().equals("USER"))
            role = roleRepo.findByRole("ROLE_USER");
        else if(userRegisteredDTO.getRole().equals("ADMIN"))
            role = roleRepo.findByRole("ROLE_ADMIN");
        User user = new User();
        user.setEmail(userRegisteredDTO.getEmail());
        user.setUserName(userRegisteredDTO.getUserName());
        user.setPassword(passwordEncoder.encode(userRegisteredDTO.getPassword()));
        user.setRoles(role);

        return userRepo.save(user);
    }
}
