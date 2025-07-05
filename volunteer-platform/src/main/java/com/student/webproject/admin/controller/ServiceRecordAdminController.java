// volunteer-platform/src/main/java/com/student/webproject/admin/controller/ServiceRecordAdminController.java

package com.student.webproject.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.student.webproject.admin.dto.ServiceRecordCreateDTO;
import com.student.webproject.admin.dto.ServiceRecordViewDTO;
import com.student.webproject.admin.service.ServiceRecordAdminService;
import com.student.webproject.common.response.Result;
import com.student.webproject.user.Entity.ServiceRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/service-records")
public class ServiceRecordAdminController {

    @Autowired
    private ServiceRecordAdminService serviceRecordAdminService;

    @PostMapping
    public Result<ServiceRecord> createServiceRecord(@RequestBody ServiceRecordCreateDTO dto) {
        return serviceRecordAdminService.createServiceRecord(dto);
    }

    /**
     * 【新增】更新服务时长记录的API端点
     * @param id 要更新的记录ID，从URL路径中获取
     * @param dto 包含更新数据的请求体
     * @return 更新后的服务记录
     */
    @PutMapping("/{id}")
    public Result<ServiceRecord> updateServiceRecord(@PathVariable Long id, @RequestBody ServiceRecordCreateDTO dto) {
        return serviceRecordAdminService.updateServiceRecord(id, dto);
    }

    /**
     * 【新增】删除服务时长记录的API端点
     * @param id 要删除的记录ID，从URL路径中获取
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteServiceRecord(@PathVariable Long id) {
        return serviceRecordAdminService.deleteServiceRecord(id);
    }
    /**
     * 【新增】获取服务时长记录列表的API端点
     */
    @GetMapping
    public Result<IPage<ServiceRecordViewDTO>> listServiceRecords(
            @RequestParam(defaultValue = "1") Long page,
            @RequestParam(defaultValue = "10") Long pageSize
    ) {
        return serviceRecordAdminService.listServiceRecords(page, pageSize);
    }
}