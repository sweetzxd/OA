package com.oa.core.dao.module;

import com.oa.core.bean.module.Filepermission;
import org.apache.ibatis.annotations.Param;

import java.sql.Timestamp;
import java.util.List;

public interface FilepermissionDao {
    void insert(Filepermission filepermission);

    void delete(@Param("permissionId") String permissionId, @Param("deleteName") String deleteName, @Param("deleteTime") Timestamp deleteTime);

    void update(Filepermission filepermission);

    Filepermission selectById(@Param("permissionId") String permissionId);

    List<Filepermission> selectAllTerms(Filepermission filepermission);

    int selectAllTermsCont(Filepermission filepermission);

    void deletes(@Param("permissionId") String permissionId, @Param("deleteName") String deleteName, @Param("deleteTime") Timestamp deleteTime);

    List<String> getUserFileType(List<String> list);
}