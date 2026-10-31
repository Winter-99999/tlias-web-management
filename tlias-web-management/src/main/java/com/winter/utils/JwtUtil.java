package com.winter.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.Map;

public class JwtUtil {

    private static final String SECRET_KEY = "d2ludGVy";           //密钥
    private static final long EXPIRATION_TIME = 2 * 3600 * 1000L;  //令牌有效期

    /**
     * 生成JWT令牌
     * @param claims 自定义载荷数据
     * @return jwt字符串
     */
    public static String generateJwt(Map<String, Object> claims) {
        return Jwts.builder()
                .addClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    /**
     * 解析JWT令牌
     * @param token jwt字符串
     * @return 载荷Claims
     */
    public static Claims parseJwt(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

}
