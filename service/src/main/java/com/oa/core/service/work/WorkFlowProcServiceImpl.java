package com.oa.core.service.work;

import com.oa.core.bean.work.WorkFlowProc;
import com.oa.core.dao.work.WorkFlowProcDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("workFlowProcService")
public class WorkFlowProcServiceImpl implements WorkFlowProcService {
    @Resource
    private WorkFlowProcDao workFlowProcDao;

    @Override
    public void insert(WorkFlowProc workFlowProc) {
        workFlowProcDao.insert(workFlowProc);
    }

    @Override
    public void delete(WorkFlowProc workFlowProc) {
        workFlowProcDao.delete(workFlowProc);
    }

    @Override
    public void deletes(String procIds,String wkflwId) {
        workFlowProcDao.deletes(procIds,wkflwId);
    }

    @Override
    public void update(WorkFlowProc workFlowProc) {
        workFlowProcDao.update(workFlowProc);
    }

    @Override
    public List<WorkFlowProc> selectAll() {
        return workFlowProcDao.selectAll();
    }

    @Override
    public List<WorkFlowProc> selectTerms(WorkFlowProc workFlowProc) {
        return workFlowProcDao.selectTerms(workFlowProc);
    }

    @Override
    public List<WorkFlowProc> selectByIds(String procIds,String wkflwId) {
        return workFlowProcDao.selectByIds(procIds,wkflwId);
    }

    @Override
    public WorkFlowProc selectById(String procId,String wkflwId) { return workFlowProcDao.selectById(procId,wkflwId); }

    @Override
    public List<WorkFlowProc> selectByWkflwId(String wkflwId) {
        return workFlowProcDao.selectByWkflwId(wkflwId);
    }

    @Override
    public int selectAllCount() {
        return workFlowProcDao.selectAllCount();
    }

    @Override
    public int selectTermsCount(WorkFlowProc workFlowProc) {
        return workFlowProcDao.selectTermsCount(workFlowProc);
    }

    @Override
    public int selectByIdCount(String procIds,String wkflwId) {
        return workFlowProcDao.selectByIdCount(procIds,wkflwId);
    }

    @Override
    public List<Integer> getWrkFlowProcNum(String wkflwId,String originator,String starnode){
        return workFlowProcDao.getWrkFlowProcNum(wkflwId,originator,starnode);
    }
}
