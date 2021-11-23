package com.oa.core.dao.work;

import com.oa.core.bean.work.WorkFlowNode;
import org.apache.ibatis.annotations.Param;

import java.util.List;
public interface WorkFlowNodeDao {

    void insert(WorkFlowNode workFlowNode);
    void delete(WorkFlowNode workFlowNode);
    void deletes(String nodeIds);
    void update(WorkFlowNode workFlowNode);
    void updatePosition(@Param("nodeId")String nodeId,@Param("nodePosition")int nodePosition);
    List<WorkFlowNode> selectAll();
    List<WorkFlowNode> selectTerms(WorkFlowNode workFlowNode);
    WorkFlowNode selectById(String nodeId);
    List<WorkFlowNode> selectByIds(String nodeIds);
    List<WorkFlowNode> selectByWkflwId(String wkflwId);
    WorkFlowNode selectTopNode(String wkflwId);
    int selectAllCount();
    int selectTermsCount(WorkFlowNode workFlowNode);
    int selectByIdCount(String nodeIds);
}
