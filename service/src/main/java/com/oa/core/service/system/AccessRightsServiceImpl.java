package com.oa.core.service.system;

import com.oa.core.bean.system.AccessRights;
import com.oa.core.dao.system.AccessRightsDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service("accessRightsService")
public class AccessRightsServiceImpl implements AccessRightsService {
    @Resource
    private AccessRightsDao AccessRightsDao;

    @Override
    public void insert(AccessRights accessRights) {
        AccessRightsDao.insert(accessRights);
    }

    @Override
    public void delete(String accessId) {
        AccessRightsDao.delete(accessId);
    }

    @Override
    public void deleteTerms(AccessRights accessRights) {
        AccessRightsDao.deleteTerms(accessRights);
    }

    @Override
    public void deletePageid(AccessRights accessRights) {
        AccessRightsDao.deletePageid(accessRights);
    }
    @Override
    public void update(AccessRights accessRights) {
        AccessRightsDao.update(accessRights);
    }

    @Override
    public AccessRights selectById(String accessId) {
        return AccessRightsDao.selectById(accessId);
    }

    @Override
    public List<AccessRights> selectByIds(String accessId) {
        return AccessRightsDao.selectByIds(accessId);
    }

    @Override
    public List<String> selectPageids(String roleId) { return AccessRightsDao.selectPageids(roleId); }

    @Override
    public List<AccessRights> selectAll() {
        return AccessRightsDao.selectAll();
    }

    @Override
    public AccessRights selectTerm(String pageid, String roleid, String accessType) {
        return AccessRightsDao.selectTerm(pageid,roleid,accessType);
    }

    @Override
    public List<AccessRights> selectTerms(AccessRights accessRights) { return AccessRightsDao.selectTerms(accessRights); }

    @Override
    public int selectAllCount() {
        return AccessRightsDao.selectAllCount();
    }

    @Override
    public int selectTermsCount(AccessRights accessRights) { return AccessRightsDao.selectTermsCount(accessRights); }

    @Override
    public List<Map<String, String>> selectSqlMapList(String accessType) { return AccessRightsDao.selectSqlMapList(accessType); }

    @Override
    public List<Map<String, String>> selectAccess(String pageid) { return AccessRightsDao.selectAccess(pageid); }

    @Override
    public Map<String, String> selectOneAccess(String pageid) {
        return AccessRightsDao.selectOneAccess(pageid);
    }
}
