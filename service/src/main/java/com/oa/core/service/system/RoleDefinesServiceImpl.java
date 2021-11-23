package com.oa.core.service.system;

import com.oa.core.bean.system.RoleDefines;
import com.oa.core.dao.system.RoleDefinesDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("roleDefinesService")
public class RoleDefinesServiceImpl implements RoleDefinesService {

    @Resource
    private RoleDefinesDao roleDefinesDao;
    @Override
    public void insert(RoleDefines roleDefines) {
        roleDefinesDao.insert(roleDefines);
    }

    @Override
    public void delete(RoleDefines roleDefines) {
        roleDefinesDao.delete(roleDefines);
    }

    @Override
    public void deletes(RoleDefines roleDefines) {
        roleDefinesDao.delete(roleDefines);
    }

    @Override
    public void update(RoleDefines roleDefines) {
        roleDefinesDao.update(roleDefines);
    }

    @Override
    public RoleDefines selectById(String roleId) {
        return roleDefinesDao.selectById(roleId);
    }

    @Override
    public List<String> getRoleIds(String userName){ return roleDefinesDao.getRoleIds(userName); }

    @Override
    public List<RoleDefines> selectAll() {
        return roleDefinesDao.selectAll();
    }

    @Override
    public List<RoleDefines> selectTerms(RoleDefines roleDefines) {
        return roleDefinesDao.selectTerms(roleDefines);
    }

    @Override
    public int selectAllCount() {
        return roleDefinesDao.selectAllCount();
    }

    @Override
    public int selectTermsCount(RoleDefines roleDefines) {
        return roleDefinesDao.selectTermsCount(roleDefines);
    }
}
