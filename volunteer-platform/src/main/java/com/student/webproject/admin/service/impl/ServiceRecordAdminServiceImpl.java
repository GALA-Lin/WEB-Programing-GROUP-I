// volunteer-platform/src/main/java/com/student/webproject/admin/service/impl/ServiceRecordAdminServiceImpl.java

package com.student.webproject.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.student.webproject.admin.dto.ServiceRecordCreateDTO;
import com.student.webproject.admin.dto.ServiceRecordViewDTO;
import com.student.webproject.admin.service.ServiceRecordAdminService;
import com.student.webproject.common.response.Result;
import com.student.webproject.user.Entity.User;
import com.student.webproject.user.Entity.ServiceRecord;
import com.student.webproject.user.mapper.UserMapper;
import com.student.webproject.user.mapper.ServiceRecordMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class ServiceRecordAdminServiceImpl implements ServiceRecordAdminService {

    @Autowired
    private ServiceRecordMapper serviceRecordMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    @Transactional
    public Result<ServiceRecord> createServiceRecord(ServiceRecordCreateDTO dto) {
        //增加一个前置检查
        QueryWrapper<ServiceRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", dto.getUserId());
        queryWrapper.eq("activity_id", dto.getActivityId());
        if (serviceRecordMapper.exists(queryWrapper)) {
            // 如果记录已存在，则抛出带有明确提示的异常
            throw new RuntimeException("操作失败：该用户已存在此活动的志愿时长记录，请使用“编辑”功能进行修改。");
        }
        User user = userMapper.selectById(dto.getUserId());
        if (user == null) {
            throw new RuntimeException("操作失败，ID为 " + dto.getUserId() + " 的用户不存在");
        }

        ServiceRecord newRecord = new ServiceRecord();
        BeanUtils.copyProperties(dto, newRecord); // 使用 BeanUtils 简化代码
        newRecord.setRecordedAt(LocalDateTime.now());
        newRecord.setRecordedBy(1L); // 暂时硬编码
        newRecord.setRecordMethod("manual");
        serviceRecordMapper.insert(newRecord);

        // 更新用户总时长
        BigDecimal currentTotalHours = user.getTotalServiceHours() == null ? BigDecimal.ZERO : user.getTotalServiceHours();
        BigDecimal newTotalHours = currentTotalHours.add(dto.getServiceHours());
        user.setTotalServiceHours(newTotalHours);
        userMapper.updateById(user);

        return Result.created(newRecord, "时长登记成功，用户总时长已更新");
    }

    /**
     * 【新增】更新时长记录的实现
     */
    @Override
    @Transactional
    public Result<ServiceRecord> updateServiceRecord(Long recordId, ServiceRecordCreateDTO dto) {
        // 1. 查找旧的记录
        ServiceRecord oldRecord = serviceRecordMapper.selectById(recordId);
        if (oldRecord == null) {
            throw new RuntimeException("更新失败，找不到ID为 " + recordId + " 的时长记录");
        }
        User user = userMapper.selectById(oldRecord.getUserId());

        if (user == null) {
            throw new RuntimeException("数据关联错误，找不到记录对应的用户");
        }

        // 2. 计算时长差值
        BigDecimal oldHours = oldRecord.getServiceHours();
        BigDecimal newHours = dto.getServiceHours();
        BigDecimal difference = newHours.subtract(oldHours); // 新时长 - 旧时长

        // 3. 更新用户总时长
        BigDecimal currentTotalHours = user.getTotalServiceHours() == null ? BigDecimal.ZERO : user.getTotalServiceHours();
        BigDecimal updatedTotalHours = currentTotalHours.add(difference);
        user.setTotalServiceHours(updatedTotalHours);
        userMapper.updateById(user);

        // 4. 更新时长记录本身
        BeanUtils.copyProperties(dto, oldRecord);
        oldRecord.setId(recordId); // 确保ID不变
        serviceRecordMapper.updateById(oldRecord);

        return Result.success(oldRecord, "时长记录更新成功");
    }

    /**
     * 【新增】删除时长记录的实现
     */
    @Override
    @Transactional
    public Result<Void> deleteServiceRecord(Long recordId) {
        // 1. 查找要删除的记录
        ServiceRecord recordToDelete = serviceRecordMapper.selectById(recordId);
        if (recordToDelete == null) {
            throw new RuntimeException("删除失败，找不到ID为 " + recordId + " 的时长记录");
        }
        User user = userMapper.selectById(recordToDelete.getUserId());
        if (user == null) {
            throw new RuntimeException("数据关联错误，找不到记录对应的用户");
        }

        // 2. 从用户总时长中减去要删除的时长
        BigDecimal hoursToDelete = recordToDelete.getServiceHours();
        BigDecimal currentTotalHours = user.getTotalServiceHours() == null ? BigDecimal.ZERO : user.getTotalServiceHours();
        BigDecimal updatedTotalHours = currentTotalHours.subtract(hoursToDelete);
        user.setTotalServiceHours(updatedTotalHours);
        userMapper.updateById(user);

        // 3. 删除这条时长记录
        serviceRecordMapper.deleteById(recordId);

        return Result.success(null, "时长记录删除成功");
    }
    /**
     * 【新增】获取时长记录列表的实现
     */
    @Override
    public Result<IPage<ServiceRecordViewDTO>> listServiceRecords(Long page, Long pageSize) {
        Page<ServiceRecordViewDTO> pageRequest = new Page<>(page, pageSize);
        IPage<ServiceRecordViewDTO> pageResult = serviceRecordMapper.selectServiceRecordViewPage(pageRequest);
        return Result.success(pageResult, "时长记录查询成功");
    }
}