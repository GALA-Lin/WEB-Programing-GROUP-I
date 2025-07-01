package com.student.webproject.user.Service.impl;


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
        User user = userMapper.findByUsername(username);
        if (user == null) {
            // 理论上，在经过Spring Security认证后，用户一定存在
            // 但作为防御性编程，可以抛出异常
            throw new RuntimeException("用户不存在");
        }

        UserDetailDTO userDetailDTO = new UserDetailDTO();
        BeanUtils.copyProperties(user, userDetailDTO);

        // TODO: 计算并设置总服务时长 totalServiceHours
        // 示例：userDetailDTO.setTotalServiceHours(calculateHours(user.getId()));
        userDetailDTO.setTotalServiceHours(0.00); // 暂时设置为0

        return userDetailDTO;
    }

    @Override
    @Transactional
    public UserDetailDTO updateUser(String username, UserUpdateDTO userUpdateDTO) {
        User user = userMapper.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        // 使用BeanUtils选择性地复制非空属性
        // 注意: 这需要你的UserUpdateDTO中的字段与User实体中的字段名称和类型匹配
        BeanUtils.copyProperties(userUpdateDTO, user);

        // 调用Mapper更新数据库
        userMapper.update(user); // 假设你的Mapper有update方法

        // 返回更新后的完整信息
        return getUserByUsername(username);
    }
}