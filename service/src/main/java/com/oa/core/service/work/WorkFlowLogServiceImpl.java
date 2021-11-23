package com.oa.core.service.work;

import com.oa.core.bean.work.WorkFlowLog;
import com.oa.core.dao.work.WorkFlowLogDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("workFlowLogService")
public class WorkFlowLogServiceImpl implements WorkFlowLogService {
    @Resource
    private WorkFlowLogDao workFlowLogDao;
    @Override
    public void insert(WorkFlowLog workFlowLog) {
        workFlowLogDao.insert(workFlowLog);
    }

    @Override
    public void delete(WorkFlowLog workFlowLog) {
        workFlowLogDao.delete(workFlowLog);
    }

    @Override
    public void deletes(String wkflogIds) {
        workFlowLogDao.deletes(wkflogIds);
    }

    @Override
    public void update(WorkFlowLog workFlowLog) {
        workFlowLogDao.update(workFlowLog);
    }

    @Override
    public List<WorkFlowLog> selectAll() {
        return workFlowLogDao.selectAll();
    }

    @Override
    public List<WorkFlowLog> selectTerms(WorkFlowLog workFlowLog) {
        return workFlowLogDao.selectTerms(workFlowLog);
    }

    @Override
    public List<WorkFlowLog> selectById(String wkflogIds) {
        return workFlowLogDao.selectById(wkflogIds);
    }

    @Override
    public List<WorkFlowLog> selectByIds(List wkflogIds) {
        return workFlowLogDao.selectByIds(wkflogIds);
    }

    @Override
    public List<WorkFlowLog> selectByProcId(String procId,String wkflowId) {
        return workFlowLogDao.selectByProcId(procId,wkflowId);
    }

    @Override
    public int selectAllCount() {
        return workFlowLogDao.selectAllCount();
    }

    @Override
    public int selectTermsCount(WorkFlowLog workFlowLog) {
        return workFlowLogDao.selectTermsCount(workFlowLog);
    }

    @Override
    public int selectByIdCount(String wkflogIds) {
        return 0;
    }
}
