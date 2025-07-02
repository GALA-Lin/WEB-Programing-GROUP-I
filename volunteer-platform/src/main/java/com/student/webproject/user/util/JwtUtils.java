// File path: src/main/java/com/student/webproject/user/util/JwtUtils.java
package com.student.webproject.user.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.student.webproject.user.Entity.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import java.util.Date;

@Component
public class JwtUtils {

    // 定义一个密钥，实际项目中不能硬编码，应放在配置文件中
    private static final String SECRET_KEY = "your-very-secret-key-for-jwt";
    // 定义过期时间，例如 7 天
    private static final long EXPIRE_TIME = 7 * 24 * 60 * 60 * 1000;

    /**
     * (已有方法) 根据UserDetails对象生成JWT Token
     * 我们的登录逻辑会使用这个方法
     */
    public String generateTokenByUserDetails(UserDetails userDetails) {
        Date expirationDate = new Date(System.currentTimeMillis() + EXPIRE_TIME);

        return JWT.create()
                // 我们在Token中放入username作为核心标识
                .withClaim("username", userDetails.getUsername())
                .withExpiresAt(expirationDate)
                .sign(Algorithm.HMAC256(SECRET_KEY));
    }

    // --- 【以下是新增的Token解析与验证方法】 ---

    /**
     * 从Token中提取用户名
     * @param token JWT Token
     * @return 用户名
     */
    public String extractUsername(String token) {
        try {
            return getDecodedJWT(token).getClaim("username").asString();
        } catch (JWTVerificationException e) {
            // 如果Token无效（例如，签名不匹配），则返回null
            return null;
        }
    }

    /**
     * 验证Token是否有效
     * @param token JWT Token
     * @param userDetails 从数据库中查询出的用户信息
     * @return 如果用户名匹配且Token未过期，则返回true
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        try {
            final String username = extractUsername(token);
            return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
        } catch (JWTVerificationException e) {
            return false;
        }
    }

    /**
     * 检查Token是否已过期
     * @param token JWT Token
     * @return 如果已过期返回true，否则返回false
     */
    private boolean isTokenExpired(String token) {
        Date expiration = getDecodedJWT(token).getExpiresAt();
        return expiration.before(new Date());
    }

    /**
     * 私有辅助方法：用于验证并解析Token
     * @param token JWT Token
     * @return 解析后的DecodedJWT对象
     */
    private DecodedJWT getDecodedJWT(String token) {
        // 创建一个验证器，指定加密算法和密钥
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET_KEY)).build();
        // 验证token，如果验证失败（如签名错误、格式错误），会直接抛出JWTVerificationException
        return verifier.verify(token);
    }


    /**
     * (已有方法) 你原来的根据User对象生成Token的方法，可以保留用于其他场景
     */
    public String generateToken(User user) {
        Date expirationDate = new Date(System.currentTimeMillis() + EXPIRE_TIME);

        return JWT.create()
                .withClaim("userId", user.getId())
                .withClaim("username", user.getUsername())
                .withClaim("avatarUrl", user.getAvatarUrl())
                .withExpiresAt(expirationDate)
                .sign(Algorithm.HMAC256(SECRET_KEY));
    }
}