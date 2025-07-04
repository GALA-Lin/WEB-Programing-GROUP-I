package com.student.webproject.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.student.webproject.admin.dto.UserCreateDTO;
import com.student.webproject.admin.dto.UserPasswordUpdateDTO;
import com.student.webproject.admin.dto.UserUpdateDTO;
import com.student.webproject.admin.service.UserAdminService;
import com.student.webproject.common.response.Result;
import com.student.webproject.user.Entity.User;
import com.student.webproject.user.mapper.UserMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public class UserAdminServiceImpl implements UserAdminService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserAdminServiceImpl(UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Result<IPage<User>> listUsers(Long page, Long pageSize) {
        IPage<User> pageRequest = new Page<>(page, pageSize);
        IPage<User> pageResult = userMapper.selectPage(pageRequest, null);
        return Result.success(pageResult, "用户列表查询成功");
    }

    private void checkSuperAdminPermission(Principal principal, String targetRole) {
        if (targetRole != null && (targetRole.equals("admin") || targetRole.equals("super_admin"))) {
            User operator = userMapper.selectOne(new QueryWrapper<User>().eq("username", principal.getName()));
            if (operator == null || !"super_admin".equals(operator.getRole())) {
                throw new RuntimeException("权限不足，只有超级管理员才能设置其他管理员。");
            }
        }
    }

    // --- ↓↓↓ 修改这两个方法的签名，加入 Principal 参数 ↓↓↓ ---
    @Override
    public Result<User> createUser(Principal principal, UserCreateDTO dto) {
        checkSuperAdminPermission(principal, dto.getRole());

        if (userMapper.selectOne(new QueryWrapper<User>().eq("username", dto.getUsername())) != null) {
            throw new RuntimeException("用户名已存在");
        }
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRealName(dto.getRealName());
        user.setStudentId(dto.getStudentId());
        user.setEmail(dto.getEmail());
        user.setRole(dto.getRole());
        user.setStatus(1);
        userMapper.insert(user);
        return Result.created(user, "用户创建成功");
    }

    @Override
    public Result<User> updateUser(Principal principal, Long id, UserUpdateDTO dto) {
        checkSuperAdminPermission(principal, dto.getRole());

        User user = userMapper.selectById(id);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        if (dto.getRealName() != null) user.setRealName(dto.getRealName());
        if (dto.getEmail() != null) user.setEmail(dto.getEmail());
        if (dto.getPhoneNumber() != null) user.setPhoneNumber(dto.getPhoneNumber());
        if (dto.getRole() != null) user.setRole(dto.getRole());
        if (dto.getStatus() != null) user.setStatus(dto.getStatus());
        userMapper.updateById(user);
        return Result.success(user, "用户信息更新成功");
    }
    // --- ↑↑↑ 修改结束 ↑↑↑ ---

    @Override
    public Result<Void> deleteUser(Long id) {
        if (userMapper.selectById(id) == null) {
            throw new RuntimeException("删除失败，找不到ID为 " + id + " 的用户。");
        }
        userMapper.deleteById(id);
        return Result.success(null, "用户删除成功");
    }

    @Override
    public Result<Void> updateUserPassword(Long id, UserPasswordUpdateDTO dto) {
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        String encodedPassword = passwordEncoder.encode(dto.getNewPassword());
        user.setPassword(encodedPassword);
        userMapper.updateById(user);
        return Result.success(null, "密码更新成功");
    }
}