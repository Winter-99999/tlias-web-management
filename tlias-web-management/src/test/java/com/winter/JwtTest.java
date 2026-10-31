package com.winter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class JwtTest {
    @Test
    public void testGenerateJwt(){
        Map<String, Object> map = new HashMap<>();
        map.put("id",1);
        map.put("username","admin");
        String jwt = Jwts.builder().signWith(SignatureAlgorithm.HS256, "d2ludGVy")  // 设置签名算法，密钥
                .addClaims(map)  // 添加自定义数据
                .setExpiration(new Date(System.currentTimeMillis() + 3600 * 1000))  // 设置过期时间
                .compact();// 生成JWT
        System.out.println(jwt);
    }
    @Test
    public void testParseJwt(){
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJpZCI6MSwidXNlcm5hbWUiOiJhZG1pbiIsImV4cCI6MTc5MzM1NTYxNX0._G_7DN_fhn8SxDbwajm-fX8iFiEDhhax-Iqt_BQTEus";
        Claims msg = Jwts.parser()
                .setSigningKey("d2ludGVy")  // 设置密钥
                .parseClaimsJws(token)    // 解析JWT
                .getBody();// 获取自定义数据  {id=1, username=admin, exp=1793355615}
        System.out.println(msg);
    }
}
