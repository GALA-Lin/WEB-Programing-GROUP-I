package com.student.webproject.user.Service;

import com.student.webproject.user.dto.EnrolledActivityDTO;
import com.student.webproject.user.dto.ServiceRecordDTO;
import java.util.List;

public interface ProfileService {
    List<ServiceRecordDTO> getServiceRecordsByUserId(Long userId);
    List<EnrolledActivityDTO> getEnrolledActivitiesByUserId(Long userId);
}