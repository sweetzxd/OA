package com.oa.core.service.module;

import com.oa.core.bean.module.Schedule;
import org.apache.ibatis.annotations.Param;

import java.sql.Timestamp;
import java.util.List;

public interface ScheduleService {

    void insert(Schedule schedule);

    void delete(@Param("scheduleId") String scheduleId, @Param("deleteName") String deleteName, @Param("deleteTime") Timestamp deleteTime);

    void update(Schedule schedule);

    Schedule selectById(@Param("scheduleId") String scheduleId);

    List<Schedule> selectAllTerms(Schedule schedule);

    int selectAllTermsCont(Schedule schedule);

    void deletes(@Param("scheduleId") String scheduleId, @Param("deleteName") String deleteName, @Param("deleteTime") Timestamp deleteTime);

    List<Schedule> getUserSchedule(Schedule schedule);
}
