package com.student.webproject.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.student.webproject.admin.dto.UserCreateDTO;
import com.student.webproject.admin.dto.UserPasswordUpdateDTO;
import com.student.webproject.admin.dto.UserUpdateDTO;
import com.student.webproject.common.response.Result;
import com.student.webproject.user.Entity.User;

import java.security.Principal;

public interface UserAdminService {

    Result<IPage<User>> listUsers(Long page, Long pageSize);

    Result<User> createUser(Principal principal, UserCreateDTO dto);

    Result<User> updateUser(Principal principal, Long id, UserUpdateDTO dto);

    Result<Void> deleteUser(Long id);

    Result<Void> updateUserPassword(Long id, UserPasswordUpdateDTO dto);
}