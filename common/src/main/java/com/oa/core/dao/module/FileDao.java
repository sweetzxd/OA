package com.oa.core.dao.module;

import com.oa.core.bean.module.File;
import org.apache.ibatis.annotations.Param;

import java.sql.Timestamp;
import java.util.List;

public interface FileDao {
    void insert(File file);

    void delete(@Param("fileId") String fileId, @Param("deleteName") String deleteName, @Param("deleteTime") Timestamp deleteTime);

    void update(File file);

    File selectById(@Param("fileId") String fileId);

    List<File> selectAllTerms(File file);

    int selectAllTermsCont(File file);

    void deletes(@Param("fileId") String fileId, @Param("deleteName") String deleteName, @Param("deleteTime") Timestamp deleteTime);
}