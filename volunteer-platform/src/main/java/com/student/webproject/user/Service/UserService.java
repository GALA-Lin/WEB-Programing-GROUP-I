package com.student.webproject.user.Service;

import com.student.webproject.user.dto.UserDetailDTO;
import com.student.webproject.user.dto.UserUpdateDTO;

public interface UserService {
    UserDetailDTO getUserByUsername(String username);
    UserDetailDTO updateUser(String username, UserUpdateDTO userUpdateDTO);
}