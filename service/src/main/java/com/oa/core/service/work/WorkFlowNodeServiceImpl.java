package com.oa.core.service.work;

import com.oa.core.bean.work.WorkFlowNode;
import com.oa.core.dao.work.WorkFlowNodeDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("workFlowNodeService")
public class WorkFlowNodeServiceImpl implements WorkFlowNodeService {
    @Resource
    private WorkFlowNodeDao workFlowNodeDao;

    @Override
    public void insert(WorkFlowNode workFlowNode) {
        workFlowNodeDao.insert(workFlowNode);
    }

    @Override
    public void delete(WorkFlowNode workFlowNode) {
        workFlowNodeDao.delete(workFlowNode);
    }

    @Override
    public void deletes(String nodeIds) {
        workFlowNodeDao.deletes(nodeIds);
    }

    @Override
    public void update(WorkFlowNode workFlowNode) {
        workFlowNodeDao.update(workFlowNode);
    }

    @Override
    public void updatePosition(String nodeId, int nodePosition) {
        workFlowNodeDao.updatePosition(nodeId,nodePosition);
    }

    @Override
    public List<WorkFlowNode> selectAll() {
        return workFlowNodeDao.selectAll();
    }

    @Override
    public List<WorkFlowNode> selectTerms(WorkFlowNode workFlowNode) {
        return workFlowNodeDao.selectTerms(workFlowNode);
    }

    @Override
    public WorkFlowNode selectById(String nodeId) {
        return workFlowNodeDao.selectById(nodeId);
    }

    @Override
    public List<WorkFlowNode> selectByIds(String nodeIds) {
        return workFlowNodeDao.selectByIds(nodeIds);
    }

    @Override
    public List<WorkFlowNode> selectByWkflwId(String wkflwId) {
        return workFlowNodeDao.selectByWkflwId(wkflwId);
    }

    @Override
    public WorkFlowNode selectTopNode(String wkflwId) {
        return workFlowNodeDao.selectTopNode(wkflwId);
    }

    @Override
    public int selectAllCount() {
        return workFlowNodeDao.selectAllCount();
    }

    @Override
    public int selectTermsCount(WorkFlowNode workFlowNode) {
        return workFlowNodeDao.selectTermsCount(workFlowNode);
    }

    @Override
    public int selectByIdCount(String nodeIds) {
        return workFlowNodeDao.selectByIdCount(nodeIds);
    }
}
