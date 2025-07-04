package com.student.webproject.user.Service.impl;

import com.student.webproject.activity.mapper.EnrollmentMapper;
import com.student.webproject.user.dto.EnrolledActivityDTO;
import com.student.webproject.user.dto.ServiceRecordDTO;
import com.student.webproject.user.mapper.ServiceRecordMapper;
import com.student.webproject.user.Service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProfileServiceImpl implements ProfileService {

    @Autowired
    private ServiceRecordMapper serviceRecordMapper;

    @Autowired
    private EnrollmentMapper enrollmentMapper;

    @Override
    public List<ServiceRecordDTO> getServiceRecordsByUserId(Long userId) {
        return serviceRecordMapper.findServiceRecordsByUserId(userId);
    }

    @Override
    public List<EnrolledActivityDTO> getEnrolledActivitiesByUserId(Long userId) {
        return enrollmentMapper.findEnrolledActivitiesByUserId(userId);
    }
}