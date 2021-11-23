package com.oa.core.dao.system;

import com.oa.core.bean.system.AccessRights;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface AccessRightsDao {
    void insert(AccessRights accessRights);

    void delete(String accessId);

    void deleteTerms(AccessRights accessRights);

    void deletePageid(AccessRights accessRights);

    void update(AccessRights accessRights);

    AccessRights selectById(String accessId);

    List<AccessRights> selectByIds(String accessId);

    List<String> selectPageids(String roleId);

    List<AccessRights> selectAll();

    AccessRights selectTerm(@Param("pageId") String pageId, @Param("roleId") String roleId, @Param("accessType") String accessType);

    List<AccessRights> selectTerms(AccessRights accessRights);

    int selectAllCount();

    int selectTermsCount(AccessRights accessRights);

    List<Map<String, String>> selectSqlMapList(String accessType);

    List<Map<String, String>> selectAccess(String pageid);

    Map<String, String> selectOneAccess(@Param("pageid") String pageid);
}
