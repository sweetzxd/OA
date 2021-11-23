package com.oa.core.dao.system;

import com.oa.core.bean.system.Attendance;
import org.apache.ibatis.annotations.Param;

import java.sql.Timestamp;

/**
 * Demo AttendanceDao
 *
 * @author zxd
 * @date 2019/03/20
 */
public interface AttendanceDao {

    void insert(Attendance attendance);
    void update(Attendance attendance);
    void delete(@Param("recorderNO")String recorderNO,@Param("deleteName")String deleteName,@Param("deleteTime") Timestamp deleteTime);
}
