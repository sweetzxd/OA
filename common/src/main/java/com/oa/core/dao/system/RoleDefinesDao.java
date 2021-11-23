package com.oa.core.dao.system;

import com.oa.core.bean.system.RoleDefines;

import java.util.List;

public interface RoleDefinesDao {
    public void insert(RoleDefines roleDefines);
    public void delete(RoleDefines roleDefines);
    public void deletes(RoleDefines roleDefines);
    public void update(RoleDefines roleDefines);
    public RoleDefines selectById(String roleId);
    public List<String> getRoleIds(String userName);
    public List<RoleDefines> selectAll();
    public List<RoleDefines> selectTerms(RoleDefines roleDefines);
    public int selectAllCount();
    public int selectTermsCount(RoleDefines roleDefines);
}
