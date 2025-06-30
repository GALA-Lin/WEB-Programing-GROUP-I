package com.student.webproject.user.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtils {

    // 定义一个密钥，实际项目中不能硬编码，应放在配置文件中
    private static final String SECRET_KEY = "your-very-secret-key-for-jwt";
    // 定义过期时间，例如 7 天
    private static final long EXPIRE_TIME = 7 * 24 * 60 * 60 * 1000;

    /**
     * 生成 JWT Token
     * @param userId 用户ID
     * @param username 用户名
     * @return 生成的 Token
     */
    public String generateToken(Long userId, String username) {
        Date expirationDate = new Date(System.currentTimeMillis() + EXPIRE_TIME);

        // 自定义 claims
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("username", username);

        return JWT.create()
                .withPayload(claims) // 设置自定义 claims
                .withExpiresAt(expirationDate) // 设置过期时间
                .sign(Algorithm.HMAC256(SECRET_KEY)); // 设置签名算法和密钥
    }

    // 你可以后续在这里添加验证 token 的方法
    public String generateTokenByUserDetails(UserDetails userDetails) {
        // 这里我们需要从 UserDetails 中获取到我们自己的用户ID
        // 但 UserDetails 默认只有用户名，所以我们需要先根据用户名查一次用户
        // 这是一个可以优化的点，但我们先这样实现

        // 为了简单，我们暂时只用用户名生成Token，因为ID需要再查一次数据库
        // 更好的做法是让 UserDetails 实现类携带ID
        Date expirationDate = new Date(System.currentTimeMillis() + EXPIRE_TIME);

        return JWT.create()
                .withClaim("username", userDetails.getUsername())
                .withExpiresAt(expirationDate)
                .sign(Algorithm.HMAC256(SECRET_KEY));
    }
}