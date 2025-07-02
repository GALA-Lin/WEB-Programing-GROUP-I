package com.student.webproject.user.Service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.student.webproject.user.Entity.User;
import com.student.webproject.user.mapper.UserMapper;
import com.student.webproject.user.Service.AuthService;
import com.student.webproject.user.dto.UserLoginDTO;
import com.student.webproject.user.dto.UserRegisterDTO;
import com.student.webproject.user.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public User register(UserRegisterDTO userRegisterDTO) {
        // 注册逻辑保持完全不变
        QueryWrapper<User> usernameWrapper = new QueryWrapper<>();
        usernameWrapper.eq("username", userRegisterDTO.getUsername());
        if (userMapper.selectCount(usernameWrapper) > 0) {
            throw new RuntimeException("用户名 '" + userRegisterDTO.getUsername() + "' 已被占用");
        }
        QueryWrapper<User> studentIdWrapper = new QueryWrapper<>();
        studentIdWrapper.eq("student_id", userRegisterDTO.getStudentId());
        if (userMapper.selectCount(studentIdWrapper) > 0) {
            throw new RuntimeException("学号 '" + userRegisterDTO.getStudentId() + "' 已被注册");
        }
        QueryWrapper<User> emailWrapper = new QueryWrapper<>();
        emailWrapper.eq("email", userRegisterDTO.getEmail());
        if (userMapper.selectCount(emailWrapper) > 0) {
            throw new RuntimeException("邮箱 '" + userRegisterDTO.getEmail() + "' 已被注册");
        }
        User user = new User();
        user.setUsername(userRegisterDTO.getUsername());
        user.setRealName(userRegisterDTO.getRealName());
        user.setStudentId(userRegisterDTO.getStudentId());
        user.setEmail(userRegisterDTO.getEmail());
        String encodedPassword = passwordEncoder.encode(userRegisterDTO.getPassword());
        user.setPassword(encodedPassword);
        user.setRole("volunteer");
        user.setStatus(1);
        userMapper.insert(user);
        return user;
    }

    @Override
    public String login(UserLoginDTO userLoginDTO) {
        // --- 1. 根据用户名查询用户 (逻辑不变) ---
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", userLoginDTO.getUsername());
        User user = userMapper.selectOne(queryWrapper);

        // --- 2. 校验用户是否存在 & 密码是否匹配 (逻辑不变) ---
        if (user == null || !passwordEncoder.matches(userLoginDTO.getPassword(), user.getPassword())) {
            throw new RuntimeException("用户名或密码错误");
        }

        // --- 3. 检查账户状态是否正常 (逻辑不变) ---
        if (user.getStatus() != 1) {
            throw new RuntimeException("该账户已被禁用，请联系管理员");
        }

        // --- 4. 【核心重构】登录成功，生成 JWT ---
        // 不再手动传递 id 和 username，而是直接传递整个 user 对象
        // 让 JwtUtils.java 内部去决定需要从 user 对象中提取哪些信息来生成 Token

        // --- 5. 返回 Token ---
        return jwtUtils.generateToken(user);
    }
    @Override
    public String adminLogin(UserLoginDTO userLoginDTO) {
        // --- 1. 根据用户名查询用户 (逻辑与普通登录相同) ---
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", userLoginDTO.getUsername());
        User user = userMapper.selectOne(queryWrapper);

        // --- 2. 校验用户是否存在 & 密码是否匹配 (逻辑与普通登录相同) ---
        if (user == null || !passwordEncoder.matches(userLoginDTO.getPassword(), user.getPassword())) {
            throw new RuntimeException("用户名或密码错误");
        }

        // --- 3. 【核心区别】校验用户角色 ---
        // 检查用户的角色是否是 'admin' 或 'super_admin'
        if (!"admin".equals(user.getRole()) && !"super_admin".equals(user.getRole())) {
            // 如果不是管理员角色，则拒绝登录
            throw new RuntimeException("权限不足，只有管理员才能登录");
        }

        // --- 4. 检查账户状态是否正常 (逻辑与普通登录相同) ---
        if (user.getStatus() != 1) {
            throw new RuntimeException("该账户已被禁用，请联系管理员");
        }

        // --- 5. 登录成功，生成 JWT ---
        return jwtUtils.generateToken(user);
    }
}