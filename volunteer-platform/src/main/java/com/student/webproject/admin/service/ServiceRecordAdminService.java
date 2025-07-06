// volunteer-platform/src/main/java/com/student/webproject/admin/service/ServiceRecordAdminService.java

package com.student.webproject.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.student.webproject.admin.dto.ServiceRecordCreateDTO;
import com.student.webproject.admin.dto.ServiceRecordViewDTO;
import com.student.webproject.common.response.Result;
import com.student.webproject.user.Entity.ServiceRecord;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public interface ServiceRecordAdminService {
    Result<IPage<ServiceRecordViewDTO>> listServiceRecords(Long page, Long pageSize);
   /**
     * 管理员为用户创建一条服务时长记录，并自动更新该用户的总时长
     * @param dto 包含了用户ID、活动ID和时长的信息包
     * @return 返回创建成功的服务记录实体
     */
    Result<ServiceRecord> createServiceRecord(ServiceRecordCreateDTO dto);

    /**
     * 更新服务时长记录
     * @param recordId 要更新的记录ID
     * @param dto 包含了更新信息的数据包
     * @return 返回更新后的记录实体
     */
    Result<ServiceRecord> updateServiceRecord(Long recordId, ServiceRecordCreateDTO dto);

    /**
     * 删除服务时长记录
     * @param recordId 要删除的记录ID
     * @return 返回空结果
     */
    Result<Void> deleteServiceRecord(Long recordId);

    /**
     * 从Excel文件批量导入服务时长记录
     * @param file 上传的Excel文件
     * @param activityId 要关联的活动ID
     * @return 返回操作结果，例如成功导入了多少条记录
     * @throws IOException 文件读取异常
     */
    Result<String> importServiceRecordsFromExcel(MultipartFile file, Long activityId) throws IOException;
    /**
     * 下载批量导入服务时长的Excel模板
     * @return 包含Excel模板文件的字节输入流
     * @throws IOException
     */
    ByteArrayInputStream downloadExcelTemplate() throws IOException;
}