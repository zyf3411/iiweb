package com.sunnyz.iiwebapi.auth;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenUtil {

    private static final String CLAIM_KEY_USERID = "userId";
    private static final String CLAIM_KEY_USERNAME = "userName";
    private static final String CLAIM_KEY_CREATED = "created";

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Integer expSeconds;

    /**
     * 生成token
     *
     * @param jwtUser
     * @return
     */
    public String generateToken(JwtUser jwtUser) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_USERID, jwtUser.getUserId());
        claims.put(CLAIM_KEY_USERNAME, jwtUser.getUserName());
        claims.put(CLAIM_KEY_CREATED, new Date());
        Date expDate = new Date(System.currentTimeMillis() + expSeconds * 1000);
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(expDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    /**
     * 验证token是否合法，成功返回JwtUser
     *
     * @param token
     * @return
     */
    public JwtUser validateToken(String token) {
        if (StringUtils.isBlank(token)) {
            throw new RuntimeException("no token!");
        }
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
            if (claims.getExpiration().before(new Date())) {
                throw new RuntimeException("token expired!");
            }
            Integer userId = Integer.valueOf(claims.get(CLAIM_KEY_USERID).toString());
            String userName = claims.get(CLAIM_KEY_USERNAME).toString();
            return new JwtUser(userId, userName);
        } catch (Exception ex) {
            throw new RuntimeException("invalid token:" + ex.getMessage());
        }
    }
}
