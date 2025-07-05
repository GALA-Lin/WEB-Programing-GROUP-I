// volunteer-platform/src/main/java/com/student/webproject/admin/service/ServiceRecordAdminService.java

package com.student.webproject.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.student.webproject.admin.dto.ServiceRecordCreateDTO;
import com.student.webproject.admin.dto.ServiceRecordViewDTO;
import com.student.webproject.common.response.Result;
import com.student.webproject.user.Entity.ServiceRecord;

public interface ServiceRecordAdminService {
    Result<IPage<ServiceRecordViewDTO>> listServiceRecords(Long page, Long pageSize);
   /**
     * 管理员为用户创建一条服务时长记录，并自动更新该用户的总时长
     * @param dto 包含了用户ID、活动ID和时长的信息包
     * @return 返回创建成功的服务记录实体
     */
    Result<ServiceRecord> createServiceRecord(ServiceRecordCreateDTO dto);

    /**
     * 【新增】更新服务时长记录
     * @param recordId 要更新的记录ID
     * @param dto 包含了更新信息的数据包
     * @return 返回更新后的记录实体
     */
    Result<ServiceRecord> updateServiceRecord(Long recordId, ServiceRecordCreateDTO dto);

    /**
     * 【新增】删除服务时长记录
     * @param recordId 要删除的记录ID
     * @return 返回空结果
     */
    Result<Void> deleteServiceRecord(Long recordId);
}