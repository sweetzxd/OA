package com.oa.core.dao.work;

import com.oa.core.bean.work.WorkFlowProc;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WorkFlowProcDao {

    void insert(WorkFlowProc workFlowProc);

    void delete(WorkFlowProc workFlowProc);

    void deletes(@Param("procIds") String procIds, @Param("wkflwId") String wkflwId);

    void update(WorkFlowProc workFlowProc);

    List<WorkFlowProc> selectAll();

    List<WorkFlowProc> selectTerms(WorkFlowProc workFlowProc);

    WorkFlowProc selectById(@Param("procId") String procId, @Param("wkflwId") String wkflwId);

    List<WorkFlowProc> selectByIds(@Param("procIds") String procIds, @Param("wkflwId") String wkflwId);

    List<WorkFlowProc> selectByWkflwId(String wkflwId);

    int selectAllCount();

    int selectTermsCount(WorkFlowProc workFlowProc);

    int selectByIdCount(@Param("procIds") String procIds, @Param("wkflwId") String wkflwId);

    List<Integer> getWrkFlowProcNum(@Param("wkflwId") String wkflwId, @Param("originator") String originator, @Param("starnode") String starnode);
}
