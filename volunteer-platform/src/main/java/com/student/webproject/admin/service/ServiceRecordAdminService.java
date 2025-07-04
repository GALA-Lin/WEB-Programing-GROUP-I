package com.student.webproject.admin.service;

import com.student.webproject.admin.dto.ServiceRecordCreateDTO;
import com.student.webproject.common.response.Result;
import com.student.webproject.user.Entity.ServiceRecord;

public interface ServiceRecordAdminService {
    /**
     * 管理员为用户创建一条服务时长记录，并自动更新该用户的总时长
     * @param dto 包含了用户ID、活动ID和时长的信息包
     * @return 返回创建成功的服务记录实体
     */
    Result<ServiceRecord> createServiceRecord(ServiceRecordCreateDTO dto);
}