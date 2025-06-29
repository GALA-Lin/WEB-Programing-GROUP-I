package com.student.webproject.user.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.student.webproject.user.Entity.User;
import org.apache.ibatis.annotations.Mapper;
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
