package com.example.onlineshopping.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret.key}")
    private String secretKey;

    public String generateToken(String username) {
        final long accessValidityToken = 60 * 60;
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("username", username);
        Date validateDate = new Date(System.currentTimeMillis() + accessValidityToken);
        return Jwts
                .builder()
                .setClaims(claims)
                .setExpiration(validateDate)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }
}
