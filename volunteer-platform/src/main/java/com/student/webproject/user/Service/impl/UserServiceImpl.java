package com.student.webproject.user.Service.impl;

// 【新增】导入MyBatis-Plus的QueryWrapper
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
        // 【修改1】使用 QueryWrapper 来根据用户名查询用户
        // 不再需要一个自定义的 findByUsername 方法
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username); // 假设数据库中的用户名字段是 'username'
        User user = userMapper.selectOne(queryWrapper);

        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        UserDetailDTO userDetailDTO = new UserDetailDTO();
        BeanUtils.copyProperties(user, userDetailDTO);

        // TODO: 计算并设置总服务时长 totalServiceHours
        userDetailDTO.setTotalServiceHours(0.00);

        return userDetailDTO;
    }

    @Override
    @Transactional
    public UserDetailDTO updateUser(String username, UserUpdateDTO userUpdateDTO) {
        // 同样，先用 QueryWrapper 查询出完整的用户实体
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        User user = userMapper.selectOne(queryWrapper);

        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        // 将DTO中的要更新的字段复制到从数据库查出的user实体上
        BeanUtils.copyProperties(userUpdateDTO, user);

        // 【修改2】使用 MyBatis-Plus 提供的 updateById 方法进行更新
        // 此方法会根据 user 实体中的主键ID (需要有@TableId注解) 作为WHERE条件
        userMapper.updateById(user);

        // 返回更新后的完整信息
        return getUserByUsername(username);
    }
}