package com.oa.core.dao.work;

import com.oa.core.bean.work.WorkFlowDefine;

import java.util.List;

public interface WorkFlowDefineDao {

    void insert(WorkFlowDefine workFlowDefine);
    void delete(WorkFlowDefine workFlowDefine);
    void deletes(String wkflwIds);
    void update(WorkFlowDefine workFlowDefine);
    List<WorkFlowDefine> selectAll();
    List<WorkFlowDefine> selectTerms(WorkFlowDefine workFlowDefine);
    List<WorkFlowDefine> selectByIds(String wkflwIds);
    WorkFlowDefine selectById(String wkflwId);
    WorkFlowDefine selectByPageId(String pageId);
    int selectAllCount();
    int selectTermsCount(WorkFlowDefine workFlowDefine);
    int selectByIdCount(String wkflwIds);
}
