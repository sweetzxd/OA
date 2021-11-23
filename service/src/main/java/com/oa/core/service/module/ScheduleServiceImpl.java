package com.oa.core.service.module;

import com.oa.core.bean.module.Schedule;
import com.oa.core.dao.module.ScheduleDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.List;

/**
 * @ClassName:ScheduleServiceImpl
 * @author:wsx
 * @Date:2018/11/27
 * @Time:下午 4:57
 * @Version V1.0
 * @Explain
 */
@Service("scheduleService")
public class ScheduleServiceImpl implements ScheduleService {

    @Resource
    private ScheduleDao scheduleDao;

    @Override
    public void insert(Schedule schedule) {
        scheduleDao.insert(schedule);
    }

    @Override
    public void delete(String scheduleId, String deleteName, Timestamp deleteTime) {
        scheduleDao.delete(scheduleId,deleteName,deleteTime);
    }

    @Override
    public void update(Schedule schedule) {
        scheduleDao.update(schedule);
    }

    @Override
    public Schedule selectById(String scheduleId) {
        return scheduleDao.selectById(scheduleId);
    }

    @Override
    public List<Schedule> selectAllTerms(Schedule schedule) {
        return scheduleDao.selectAllTerms(schedule);
    }

    @Override
    public int selectAllTermsCont(Schedule schedule) {
        return scheduleDao.selectAllTermsCont(schedule);
    }

    @Override
    public void deletes(String scheduleId, String deleteName, Timestamp deleteTime) {
        scheduleDao.deletes(scheduleId,deleteName,deleteTime);
    }

    @Override
    public List<Schedule> getUserSchedule(Schedule schedule) {
        return scheduleDao.getUserSchedule(schedule);
    }
}
