package com.eCommerce.UserService;

import com.eCommerce.base_domains.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

}
