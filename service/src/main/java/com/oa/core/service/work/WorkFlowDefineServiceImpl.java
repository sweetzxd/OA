package com.oa.core.service.work;

import com.oa.core.bean.work.WorkFlowDefine;
import com.oa.core.dao.work.WorkFlowDefineDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("workFlowDefineService")
public class WorkFlowDefineServiceImpl implements WorkFlowDefineService {
    @Resource
    private WorkFlowDefineDao workFlowDefineDao;

    @Override
    public void insert(WorkFlowDefine workFlowDefine) {
        workFlowDefineDao.insert(workFlowDefine);
    }

    @Override
    public void delete(WorkFlowDefine workFlowDefine) {
        workFlowDefineDao.delete(workFlowDefine);
    }

    @Override
    public void deletes(String wkflwIds) {
        workFlowDefineDao.deletes(wkflwIds);
    }

    @Override
    public void update(WorkFlowDefine workFlowDefine) {
        workFlowDefineDao.update(workFlowDefine);
    }

    @Override
    public List<WorkFlowDefine> selectAll() {
        return workFlowDefineDao.selectAll();
    }

    @Override
    public List<WorkFlowDefine> selectTerms(WorkFlowDefine workFlowDefine) {
        return workFlowDefineDao.selectTerms(workFlowDefine);
    }

    @Override
    public List<WorkFlowDefine> selectByIds(String wkflwIds) {
        return workFlowDefineDao.selectByIds(wkflwIds);
    }

    @Override
    public WorkFlowDefine selectById(String wkflwId) {
        return workFlowDefineDao.selectById(wkflwId);
    }

    @Override
    public WorkFlowDefine selectByPageId(String pageId) {
        return workFlowDefineDao.selectByPageId(pageId);
    }

    @Override
    public int selectAllCount() {
        return workFlowDefineDao.selectAllCount();
    }

    @Override
    public int selectTermsCount(WorkFlowDefine workFlowDefine) {
        return workFlowDefineDao.selectTermsCount(workFlowDefine);
    }

    @Override
    public int selectByIdCount(String wkflwIds) {
        return workFlowDefineDao.selectByIdCount(wkflwIds);
    }
}
