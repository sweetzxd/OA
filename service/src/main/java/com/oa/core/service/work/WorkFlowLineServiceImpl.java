package com.oa.core.service.work;

import com.oa.core.bean.work.WorkFlowLine;
import com.oa.core.dao.work.WorkFlowLineDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service("workFlowLineService")
public class WorkFlowLineServiceImpl implements WorkFlowLineService{
    @Resource
    private WorkFlowLineDao workFlowLineDao;

    @Override
    public void insert(WorkFlowLine workFlowLine) {
        workFlowLineDao.insert(workFlowLine);
    }

    @Override
    public void delete(WorkFlowLine workFlowLine) {
        workFlowLineDao.delete(workFlowLine);
    }

    @Override
    public void deleteByNode(WorkFlowLine workFlowLine) { workFlowLineDao.deleteByNode(workFlowLine); }

    @Override
    public void deletes(Map val) {
        workFlowLineDao.deletes(val);
    }

    @Override
    public void update(WorkFlowLine workFlowLine) {
        workFlowLineDao.update(workFlowLine);
    }

    @Override
    public List<WorkFlowLine> selectAll() {
        return workFlowLineDao.selectAll();
    }

    @Override
    public List<WorkFlowLine> selectTerms(WorkFlowLine workFlowLine) {
        return workFlowLineDao.selectTerms(workFlowLine);
    }

    @Override
    public List<WorkFlowLine> selectByIds(String lineIds) {
        return workFlowLineDao.selectByIds(lineIds);
    }

    @Override
    public WorkFlowLine selectById(String lineId) {
        return workFlowLineDao.selectById(lineId);
    }

    @Override
    public List<WorkFlowLine> selectByWkflwId(String wkflwId) {
        return workFlowLineDao.selectByWkflwId(wkflwId);
    }

    @Override
    public List<WorkFlowLine> selectByNodeId(String nodeId) {
        return workFlowLineDao.selectByNodeId(nodeId);
    }

    @Override
    public List<String> selectSN(String wkflwId) {
        return workFlowLineDao.selectSN(wkflwId);
    }

    @Override
    public List<String> selectPathIds(String wkflwId) { return workFlowLineDao.selectPathIds(wkflwId); }

    @Override
    public int selectAllCount() {
        return workFlowLineDao.selectAllCount();
    }

    @Override
    public int selectTermsCount(WorkFlowLine workFlowLine) {
        return workFlowLineDao.selectTermsCount(workFlowLine);
    }

    @Override
    public int selectByIdCount(String lineIds) {
        return workFlowLineDao.selectByIdCount(lineIds);
    }


    @Override
    public List<String> selectAsynch(String toNodeIds) {
        return workFlowLineDao.selectAsynch(toNodeIds);
    }
}
