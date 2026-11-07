package com.winter.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
@Component
public class JwtUtil {


    @Value("${jwt.secret}")
    private String secretKey;
    @Value("${jwt.expiration}")
    private  long expiration;
    /**
     * 生成JWT令牌
     * @param claims 自定义载荷数据
     * @return jwt字符串
     */
    public String generateJwt(Map<String, Object> claims) {
        return Jwts.builder()
                .addClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    /**
     * 解析JWT令牌
     * @param token jwt字符串
     * @return 载荷Claims
     */
    public  Claims parseJwt(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }

}
