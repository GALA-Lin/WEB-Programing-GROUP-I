package com.student.webproject.user.controller;

import com.student.webproject.common.response.Result; // 假设你有一个全局的Result封装类
import com.student.webproject.user.dto.UserDetailDTO;
import com.student.webproject.user.dto.UserUpdateDTO;
import com.student.webproject.user.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal; // 用于获取当前登录用户信息

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 获取当前用户信息
     * 请求方法: GET
     * 请求路径: /api/users/me
     */
    @GetMapping("/me")
    public Result<UserDetailDTO> getCurrentUser(Principal principal) {
        // Principal对象由Spring Security在用户认证后注入，其name通常是用户名
        String username = principal.getName();
        UserDetailDTO userDetail = userService.getUserByUsername(username);
        return Result.success(userDetail, "查询成功");
    }

    /**
     * 更新当前用户信息
     * 请求方法: PUT
     * 请求路径: /api/users/me
     */
    @PutMapping("/me")
    public Result<UserDetailDTO> updateCurrentUser(@RequestBody UserUpdateDTO userUpdateDTO, Principal principal) {
        String username = principal.getName();
        UserDetailDTO updatedUser = userService.updateUser(username, userUpdateDTO);
        return Result.success(updatedUser,"更新成功" );
    }
}