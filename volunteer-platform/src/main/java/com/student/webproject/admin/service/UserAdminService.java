package com.student.webproject.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.student.webproject.admin.dto.UserCreateDTO;
import com.student.webproject.admin.dto.UserUpdateDTO;
import com.student.webproject.user.Entity.User;
import com.student.webproject.common.response.Result;

public interface UserAdminService {
    Result<IPage<User>> listUsers(Long page, Long pageSize);
    Result<User> createUser(UserCreateDTO dto);
    Result<User> updateUser(Long id, UserUpdateDTO dto);
    Result<Void> deleteUser(Long id);
}