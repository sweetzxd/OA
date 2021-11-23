package com.oa.core.service.system;

import com.oa.core.bean.system.Attendance;
import com.oa.core.dao.system.AttendanceDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;

/**
 * @ClassName:AttendanceServiceImpl
 * @author:zxd
 * @Date:2019/03/19
 * @Time:下午 6:11
 * @Version V1.0
 * @Explain 考勤机签到
 */

@Service("attendanceService")
public class AttendanceServiceImpl implements AttendanceService {

    @Resource
    private AttendanceDao attendanceDao;

    @Override
    public void insert(Attendance attendance) {
        attendanceDao.insert(attendance);
    }

    @Override
    public void update(Attendance attendance) {
        attendanceDao.update(attendance);
    }

    @Override
    public void delete(String recorderNO, String deleteName, Timestamp deleteTime) {
        attendanceDao.delete(recorderNO,deleteName,deleteTime);
    }
}
