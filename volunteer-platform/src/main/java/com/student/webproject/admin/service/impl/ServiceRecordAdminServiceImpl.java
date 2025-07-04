package com.student.webproject.admin.service.impl;

import com.student.webproject.admin.dto.ServiceRecordCreateDTO;
import com.student.webproject.admin.service.ServiceRecordAdminService;
import com.student.webproject.common.response.Result;
import com.student.webproject.user.Entity.User;
import com.student.webproject.user.Entity.ServiceRecord;
import com.student.webproject.user.mapper.UserMapper;
import com.student.webproject.user.mapper.ServiceRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class ServiceRecordAdminServiceImpl implements ServiceRecordAdminService {

    @Autowired
    private ServiceRecordMapper serviceRecordMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    @Transactional // **关键：确保后续的多个数据库操作在同一个事务中完成，要么都成功，要么都失败**
    public Result<ServiceRecord> createServiceRecord(ServiceRecordCreateDTO dto) {
        // 1. 检查要登记时长的用户是否存在
        User user = userMapper.selectById(dto.getUserId());
        if (user == null) {
            throw new RuntimeException("操作失败，ID为 " + dto.getUserId() + " 的用户不存在");
        }

        // 2. 创建并插入新的时长记录到 service_records 表
        ServiceRecord newRecord = new ServiceRecord();
        newRecord.setUserId(dto.getUserId());
        newRecord.setActivityId(dto.getActivityId());
        newRecord.setServiceHours(dto.getServiceHours());
        newRecord.setRemarks(dto.getRemarks());
        newRecord.setRecordedAt(LocalDateTime.now());
        // 暂时将操作者ID硬编码为1，未来可以从登录信息中获取
        newRecord.setRecordedBy(1L);
        newRecord.setRecordMethod("manual");
        serviceRecordMapper.insert(newRecord);

        // 3. 【核心逻辑】更新 users 表中的总时长
        BigDecimal currentTotalHours = user.getTotalServiceHours() == null ? BigDecimal.ZERO : user.getTotalServiceHours();
        BigDecimal newTotalHours = currentTotalHours.add(dto.getServiceHours());
        user.setTotalServiceHours(newTotalHours);
        userMapper.updateById(user);

        // 4. 返回成功的响应
        return Result.created(newRecord, "时长登记成功，用户总时长已更新");
    }
}