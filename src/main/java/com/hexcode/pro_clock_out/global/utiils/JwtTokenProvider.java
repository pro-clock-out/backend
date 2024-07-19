package com.hexcode.pro_clock_out.global.utiils;


import com.hexcode.pro_clock_out.member.domain.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenProvider {

//    @Value("${jwt.secret}")
//    private String SECRET_KEY;
//
//    private final String TOKEN_SUBJECT = "pro-clock-out";
//
//    public String generateAccessToken(Member member) {
//        Map<String, Object> claims = new HashMap<>(); // claim 생성
//        claims.put("email", member.getEmail());
//        long ACCESS_TOKEN_EXPIRE = 1000 * 60 * 60 * 2;
//        return Jwts.builder()
//                .setClaims(claims)
//                .setSubject(TOKEN_SUBJECT)
//                .setIssuedAt(new Date(System.currentTimeMillis()))
//                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRE))
//                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
//                .compact();
//    }
//
//    public String generateRefreshToken(Member member) {
//        Map<String, Object> claims = new HashMap<>(); // claim 생성
//        claims.put("email", member.getEmail());
//        long REFRESH_TOKEN_EXPIRE = 1000 * 60 * 60 * 24;
//        return Jwts.builder()
//                .setClaims(claims)
//                .setSubject(TOKEN_SUBJECT)
//                .setIssuedAt(new Date(System.currentTimeMillis()))
//                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRE))
//                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
//                .compact();
//    }
//
//    public String extractEmail(String token){
//        Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
//        return (String) claims.getOrDefault("email", "");
//    }
//
//    public Date extractExpiration(String token){
//        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody()
//                .getExpiration();
//    }
//
//    public Boolean isTokenExpired(String token){
//        return extractExpiration(token).before(new Date());
//    }
//
//    public Boolean validateToken(String token){
//        final String email = extractEmail(token);
//        return (!email.isBlank() && !isTokenExpired(token));
//    }
//
//    // access_token 가져오기
//    public String getAccessToken(String header) {
//        return header.split(" ")[1].trim(); // Bearer 제외
//    }
//
//    // refresh_token 가져오기
//    private String getRefreshToken(Cookie cookie){
//        return cookie.getValue();
//    }
}