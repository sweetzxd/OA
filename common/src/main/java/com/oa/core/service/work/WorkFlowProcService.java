package com.oa.core.service.work;

import com.oa.core.bean.work.WorkFlowProc;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WorkFlowProcService {

    void insert(WorkFlowProc workFlowProc);
    void delete(WorkFlowProc workFlowProc);
    void deletes(String procIds,String wkflwId);
    void update(WorkFlowProc workFlowProc);
    List<WorkFlowProc> selectAll();
    List<WorkFlowProc> selectTerms(WorkFlowProc workFlowProc);
    WorkFlowProc selectById(String procId,String wkflwId);
    List<WorkFlowProc> selectByIds(String procIds,String wkflwId);
    List<WorkFlowProc> selectByWkflwId(String wkflwId);
    int selectAllCount();
    int selectTermsCount(WorkFlowProc workFlowProc);
    int selectByIdCount(String procIds,String wkflwId);
    List<Integer> getWrkFlowProcNum(String wkflwId, String originator,String starnode);
}
