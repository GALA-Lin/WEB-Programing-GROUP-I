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
    public Result<User> createUser(@RequestBody UserCreateDTO userCreateDTO) {
        return userAdminService.createUser(userCreateDTO);
    }

    @PutMapping("/{id}")
    public Result<User> updateUser(@PathVariable Long id, @RequestBody UserUpdateDTO userUpdateDTO) {
        return userAdminService.updateUser(id, userUpdateDTO);
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