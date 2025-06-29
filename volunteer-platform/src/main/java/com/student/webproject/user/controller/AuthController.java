package com.student.webproject.user.controller;

import com.student.webproject.common.response.Result;
import com.student.webproject.user.Entity.User;
import com.student.webproject.user.Service.AuthService;
import com.student.webproject.user.dto.UserLoginDTO;
import com.student.webproject.user.dto.UserRegisterDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    /**
     * 用户注册接口
     * @param userRegisterDTO 请求体中的JSON数据
     * @return 统一的响应结果
     */
    @PostMapping("/register") // 对应 POST /api/auth/register
    public Result<?> register(@RequestBody UserRegisterDTO userRegisterDTO) {
        // 1. 调用 Service 层处理注册逻辑
        User registeredUser = authService.register(userRegisterDTO);

        // 2. 准备返回给前端的数据 (不包含密码等敏感信息)
        // 注意：User 实体类里的 password 字段我们已经用 @JsonIgnore 忽略了

        // 3. 使用我们定义的 Result 类来封装成功的响应
        //    根据API文档，注册成功返回 201 Created
        //    这里我们直接用 success，因为状态码已在异常处理器中设置
        //    更严谨的方式是返回 ResponseEntity，但目前 Result 已足够
        return Result.created(registeredUser);
    }

    /**
     * 用户登录接口
     * @param userLoginDTO 请求体中的JSON数据
     * @return 统一的响应结果，data中包含token
     */
    @PostMapping("/login") // 对应 POST /api/auth/login
    public Result<?> login(@RequestBody UserLoginDTO userLoginDTO) {
        // 1. 调用 Service 层处理登录逻辑，获取 token
        String token = authService.login(userLoginDTO);

        // 2. 将 token 包装成 { "token": "xxx" } 的形式，与API文档一致
        Map<String, String> tokenMap = Collections.singletonMap("token", token);

        // 3. 封装成功的响应
        return Result.success(tokenMap, "登录成功");
    }
}