package com.student.webproject.user.Service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.student.webproject.user.dto.UserDetailDTO;
import com.student.webproject.user.dto.UserUpdateDTO;
import com.student.webproject.user.Entity.User;
import com.student.webproject.user.mapper.UserMapper;
import com.student.webproject.user.Service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDetailDTO getUserByUsername(String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        User user = userMapper.selectOne(queryWrapper);

        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        UserDetailDTO userDetailDTO = new UserDetailDTO();
        BeanUtils.copyProperties(user, userDetailDTO);

        return userDetailDTO;
    }

    @Override
    @Transactional
    public UserDetailDTO updateUser(String username, UserUpdateDTO userUpdateDTO) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        User user = userMapper.selectOne(queryWrapper);

        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        BeanUtils.copyProperties(userUpdateDTO, user);
        userMapper.updateById(user);

        return getUserByUsername(username);
    }
}