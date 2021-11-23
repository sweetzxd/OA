package com.oa.core.service.work;

import com.oa.core.bean.work.WorkFlowLine;
import com.oa.core.bean.work.WorkFlowNode;

import java.util.List;
import java.util.Map;

public interface WorkFlowLineService {

    void insert(WorkFlowLine workFlowLine);
    void delete(WorkFlowLine workFlowLine);
    void deleteByNode(WorkFlowLine workFlowLine);
    void deletes(Map val);
    void update(WorkFlowLine workFlowLine);
    List<WorkFlowLine> selectAll();
    List<WorkFlowLine> selectTerms(WorkFlowLine workFlowLine);
    List<WorkFlowLine> selectByIds(String lineIds);
    WorkFlowLine selectById(String lineId);
    List<WorkFlowLine> selectByWkflwId(String wkflwId);
    List<WorkFlowLine> selectByNodeId(String nodeId);
    List<String> selectSN(String wkflwId);
    List<String> selectPathIds(String wkflwId);
    int selectAllCount();
    int selectTermsCount(WorkFlowLine workFlowLine);
    int selectByIdCount(String lineIds);

    List<String> selectAsynch(String toNodeId);
}

