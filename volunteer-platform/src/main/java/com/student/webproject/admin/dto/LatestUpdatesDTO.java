package com.student.webproject.admin.dto;

import com.student.webproject.admin.entity.Activity;
import com.student.webproject.user.Entity.User;
import lombok.Data;
import java.util.List;

@Data
public class LatestUpdatesDTO {
    private List<User> latestUsers;
    private List<Activity> latestActivities;
}