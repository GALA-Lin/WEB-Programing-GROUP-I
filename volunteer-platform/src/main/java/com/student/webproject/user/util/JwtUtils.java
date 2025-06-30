package com.student.webproject.user.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.student.webproject.user.Entity.User;
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
     * 根据用户对象生成JWT Token
     * @param user 包含用户信息的实体对象
     * @return 生成的Token
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