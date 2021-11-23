package com.oa.core.service.system;

import com.oa.core.bean.system.AccessRights;

import java.util.List;
import java.util.Map;

public interface AccessRightsService {
    void insert(AccessRights accessRights);

    void delete(String accessId);

    void deleteTerms(AccessRights AccessRights);

    void deletePageid(AccessRights accessRights);

    void update(AccessRights accessRights);

    AccessRights selectById(String accessId);

    List<AccessRights> selectByIds(String accessId);

    List<String> selectPageids(String roleId);

    List<AccessRights> selectAll();

    AccessRights selectTerm(String pageid, String roleid, String accessType);

    List<AccessRights> selectTerms(AccessRights accessRights);

    int selectAllCount();

    int selectTermsCount(AccessRights accessRights);

    List<Map<String, String>> selectSqlMapList(String accessType);

    List<Map<String, String>> selectAccess(String pageid);

    Map<String, String> selectOneAccess(String pageid);
}
