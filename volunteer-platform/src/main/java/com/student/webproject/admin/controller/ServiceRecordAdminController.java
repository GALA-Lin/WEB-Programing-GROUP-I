package com.student.webproject.admin.controller;

import com.student.webproject.admin.dto.ServiceRecordCreateDTO;
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
}