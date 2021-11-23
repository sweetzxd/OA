package com.oa.core.service.work;

import com.oa.core.bean.work.WorkFlowLog;

import java.util.List;

public interface WorkFlowLogService {

    void insert(WorkFlowLog workFlowLog);
    void delete(WorkFlowLog workFlowLog);
    void deletes(String wkflogIds);
    void update(WorkFlowLog workFlowLog);
    List<WorkFlowLog> selectAll();
    List<WorkFlowLog> selectTerms(WorkFlowLog workFlowLog);
    List<WorkFlowLog> selectById(String wkflogId);
    List<WorkFlowLog> selectByIds(List wkflogIds);
    List<WorkFlowLog> selectByProcId(String procId,String wkflowId);
    int selectAllCount();
    int selectTermsCount(WorkFlowLog workFlowLog);
    int selectByIdCount(String wkflogIds);
}
