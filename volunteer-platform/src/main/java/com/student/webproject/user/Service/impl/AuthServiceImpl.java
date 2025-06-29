package com.student.webproject.user.Service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.student.webproject.user.Entity.User;
import com.student.webproject.user.Mapper.UserMapper;
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
    // --- 接口方法实现 ---

    @Override
    public User register(UserRegisterDTO userRegisterDTO) {
        // --- 1. 数据校验：检查用户名和学号是否已被占用 ---
        // a. 检查用户名
        QueryWrapper<User> usernameWrapper = new QueryWrapper<>();
        usernameWrapper.eq("username", userRegisterDTO.getUsername());
        if (userMapper.selectCount(usernameWrapper) > 0) {
            // 如果查询到的数量大于0，说明用户名已存在
            // 我们抛出一个运行时异常，后续会由全局异常处理器统一捕获并返回给前端
            throw new RuntimeException("用户名 '" + userRegisterDTO.getUsername() + "' 已被占用");
        }

        // b. 检查学号
        QueryWrapper<User> studentIdWrapper = new QueryWrapper<>();
        studentIdWrapper.eq("student_id", userRegisterDTO.getStudentId());
        if (userMapper.selectCount(studentIdWrapper) > 0) {
            // 学号同理
            throw new RuntimeException("学号 '" + userRegisterDTO.getStudentId() + "' 已被注册");
        }

        // --- 2. 创建实体对象并填充数据 ---
        User user = new User();
        user.setUsername(userRegisterDTO.getUsername());
        user.setRealName(userRegisterDTO.getRealName());
        user.setStudentId(userRegisterDTO.getStudentId());

        // --- 3. 密码加密 (安全核心) ---
        // 使用我们配置的 BCryptPasswordEncoder 来加密前端传来的原始密码
        String encodedPassword = passwordEncoder.encode(userRegisterDTO.getPassword());
        user.setPassword(encodedPassword);

        // --- 4. 设置新用户的默认属性 ---
        user.setRole("volunteer"); // 默认角色为 'volunteer'
        user.setStatus(1);         // 默认账户状态为 1 (正常)

        // --- 5. 将用户信息插入数据库 ---
        userMapper.insert(user);

        // --- 6. 返回创建好的用户对象 ---
        // 此时的 user 对象已经包含了数据库自动生成的 ID
        return user;
    }

    @Override
    public String login(UserLoginDTO userLoginDTO) {
        // --- 1. 根据用户名查询用户 ---
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", userLoginDTO.getUsername());
        User user = userMapper.selectOne(queryWrapper);

        // --- 2. 校验用户是否存在 & 密码是否匹配 ---
        // a. 检查用户是否存在
        // b. 使用 passwordEncoder.matches() 方法来安全地比对密码
        //    它会自动处理盐值，我们绝不能手动解密数据库的密码
        if (user == null || !passwordEncoder.matches(userLoginDTO.getPassword(), user.getPassword())) {
            // 为了安全，不明确提示是“用户名不存在”还是“密码错误”，统一返回模糊信息
            throw new RuntimeException("用户名或密码错误");
        }

        // --- 3. 检查账户状态是否正常 ---
        if (user.getStatus() != 1) {
            throw new RuntimeException("该账户已被禁用，请联系管理员");
        }

        // --- 4. 登录成功，生成 JWT ---
        // 使用我们之前创建的 JwtUtils 来生成 token
        String token = jwtUtils.generateToken(user.getId(), user.getUsername());

        // --- 5. 返回 Token ---
        return token;
    }
}