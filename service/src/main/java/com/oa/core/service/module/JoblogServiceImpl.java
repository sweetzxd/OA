package com.oa.core.service.module;

import com.oa.core.bean.module.Joblog;
import com.oa.core.dao.module.JoblogDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * @ClassName:JoblogServiceImpl
 * @author:wsx
 * @Date:2018/11/29
 * @Time:上午 10:19
 * @Version V1.0
 * @Explain
 */
@Service("joblogService")
public class JoblogServiceImpl implements  JoblogService{

    @Resource
    private JoblogDao joblogDao;

    @Override
    public void insert(Joblog joblog) {
        joblogDao.insert(joblog);
    }

    @Override
    public void delete(String joblogId, String deleteName, Timestamp deleteTime) {
        joblogDao.delete(joblogId,deleteName,deleteTime);
    }

    @Override
    public void update(Joblog joblog) {
        joblogDao.update(joblog);
    }

    @Override
    public Joblog selectById(String joblogId) {
        return joblogDao.selectById(joblogId);
    }

    @Override
    public List<Joblog> selectAllTerms(Joblog joblog) {
        return joblogDao.selectAllTerms(joblog);
    }

    @Override
    public int selectAllTermsCont(Joblog joblog) {
        return joblogDao.selectAllTermsCont(joblog);
    }

    @Override
    public void deletes(String joblogId, String deleteName, Timestamp deleteTime) {
        joblogDao.deletes(joblogId,deleteName,deleteTime);
    }

    @Override
    public List<Joblog> selectAllJobLog() {
        return joblogDao.selectAllJobLog();
    }

    @Override
    public List<Map<String, Object>> selectJobLogByMonth(Map<String, Object> map) {
        return joblogDao.selectJobLogByMonth(map);
    }

    @Override
    public List<Map<String, Object>> selectJoblogByDay(Map<String, Object> map) {
        return joblogDao.selectJoblogByDay(map);
    }

    @Override
    public List<Map<String, Object>> selectJobLogByCsuser(Map<String, Object> map) {
        return joblogDao.selectJobLogByCsuser(map);
    }

    @Override
    public List<Map<String, Object>> selectJoblogByCsuserMonth(Map<String, Object> map) {
        return joblogDao.selectJoblogByCsuserMonth(map);
    }

    @Override
    public List<Map<String, Object>> selectJobLogByLeader(Map<String, Object> map) {
        return joblogDao.selectJobLogByLeader(map);
    }

    @Override
    public List<Map<String, Object>> selectJobLogByLeaderMonth(Map<String, Object> map) {
        return joblogDao.selectJobLogByLeaderMonth(map);
    }

    @Override
    public List<Map<String, Object>> selectJoblogByLeaderDay(Map<String, Object> map) {
        return joblogDao.selectJoblogByLeaderDay(map);
    }

}
