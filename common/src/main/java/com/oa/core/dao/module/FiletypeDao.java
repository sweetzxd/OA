package com.oa.core.dao.module;

import com.oa.core.bean.module.Filetype;
import org.apache.ibatis.annotations.Param;

import java.sql.Timestamp;
import java.util.List;

public interface FiletypeDao {
    void insert(Filetype filetype);

    void delete(@Param("fileTypeId") String fileTypeId, @Param("deleteName") String deleteName, @Param("deleteTime") Timestamp deleteTime);

    void update(Filetype filetype);

    Filetype selectById(@Param("fileTypeId") String fileTypeId);

    List<Filetype> selectAllTerms(Filetype filetype);

    int selectAllTermsCont(Filetype filetype);

    List<Filetype> getFiletypesByRoleId(List<String> list);
}