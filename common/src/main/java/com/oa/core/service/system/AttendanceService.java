package com.oa.core.service.system;

import com.oa.core.bean.system.Attendance;

import java.sql.Timestamp;

public interface AttendanceService {

    void insert(Attendance attendance);

    void update(Attendance attendance);

    void delete(String recorderNO, String deleteName, Timestamp deleteTime);
}
