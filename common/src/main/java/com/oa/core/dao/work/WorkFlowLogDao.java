package com.oa.core.dao.work;

import com.oa.core.bean.work.WorkFlowLog;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WorkFlowLogDao {

    void insert(WorkFlowLog workFlowLog);
    void delete(WorkFlowLog workFlowLog);
    void deletes(String wkflogIds);
    void update(WorkFlowLog workFlowLog);
    List<WorkFlowLog> selectAll();
    List<WorkFlowLog> selectTerms(WorkFlowLog workFlowLog);
    List<WorkFlowLog> selectById(String wkflogId);
    List<WorkFlowLog> selectByIds(List wkflogIds);
    List<WorkFlowLog> selectByProcId(@Param("procId") String procId, @Param("wkflowId")String wkflowId);
    int selectAllCount();
    int selectTermsCount(WorkFlowLog workFlowLog);
    int selectByIdCount(String wkflogIds);
}
