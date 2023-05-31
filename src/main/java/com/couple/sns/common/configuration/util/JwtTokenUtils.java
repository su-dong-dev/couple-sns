package com.couple.sns.common.configuration.util;

import com.couple.sns.domain.user.dto.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

public class JwtTokenUtils {

    // get username
    public static String getUsername(String token, String key){
        return extractClaims(token, key).get("username", String.class);
    }

    // get user role
    public static String getUserRole(String token, String key){
        return extractClaims(token, key).get("role", String.class);
    }

    // check token isExpired
    public static boolean isExpired(String token, String key){
        Date expiredDate = extractClaims(token,key).getExpiration();

        return expiredDate.before(new Date());
    }

    // extract claim
    private static Claims extractClaims(String token, String key){
        return Jwts.parserBuilder().setSigningKey(getKey(key))
                .build().parseClaimsJws(token).getBody();
    }

    public static String createToken(String username, UserRole userRole, String key, long expiredTimeMs) {
        Claims claims = Jwts.claims();
        claims.put("username", username);
        claims.put("role", userRole.name());

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiredTimeMs))
                .signWith(getKey(key), SignatureAlgorithm.HS256)
                .compact();
    }

    public static String createRefreshToken(String username, String key, long refreshExpiredTimeMs) {
        Claims claims = Jwts.claims();
        claims.put("username", username);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + refreshExpiredTimeMs))
                .signWith(getKey(key), SignatureAlgorithm.HS256)
                .compact();
    }

    private static Key getKey(String key) {
        return Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8));
    }

}