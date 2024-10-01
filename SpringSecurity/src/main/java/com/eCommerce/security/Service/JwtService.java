package com.eCommerce.security.Service;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Service;

@Service
public class JwtService {


    public static final String SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";

//    public void validateToken(final String token) {
//        Jwts.builder().signWith(getSignKey()
//    }


}
