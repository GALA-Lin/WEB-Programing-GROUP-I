package com.student.webproject.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.student.webproject.admin.dto.UserCreateDTO;
import com.student.webproject.admin.dto.UserPasswordUpdateDTO;
import com.student.webproject.admin.dto.UserUpdateDTO;
import com.student.webproject.admin.service.UserAdminService;
import com.student.webproject.common.response.Result;
import com.student.webproject.user.Entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.security.Principal; // 引入 Principal

@RestController
@RequestMapping("/api/admin/users")
public class AdminUserController {

    @Autowired
    private UserAdminService userAdminService;

    @GetMapping
    public Result<IPage<User>> listUsers(@RequestParam(defaultValue = "1") Long page,
                                         @RequestParam(defaultValue = "10") Long pageSize) {
        return userAdminService.listUsers(page, pageSize);
    }

    @PostMapping
    public Result<User> createUser(Principal principal, @RequestBody UserCreateDTO userCreateDTO) {
        // 将 principal (当前登录用户) 传给 Service 层
        return userAdminService.createUser(principal, userCreateDTO);
    }

    @PutMapping("/{id}")
    public Result<User> updateUser(@PathVariable Long id, Principal principal, @RequestBody UserUpdateDTO userUpdateDTO) {
        // 将 principal (当前登录用户) 传给 Service 层
        return userAdminService.updateUser(principal, id, userUpdateDTO);
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteUser(@PathVariable Long id) {
        return userAdminService.deleteUser(id);
    }

    @PutMapping("/{id}/password")
    public Result<Void> updateUserPassword(@PathVariable Long id, @RequestBody UserPasswordUpdateDTO dto) {
        return userAdminService.updateUserPassword(id, dto);
    }
}