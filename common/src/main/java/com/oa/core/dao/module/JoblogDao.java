package com.oa.core.dao.module;

import com.oa.core.bean.module.Joblog;
import org.apache.ibatis.annotations.Param;

import java.sql.Timestamp;
import java.util.List;

public interface JoblogDao {

    void insert(Joblog joblog);

    void delete(@Param("joblogId") String joblogId, @Param("deleteName") String deleteName, @Param("deleteTime") Timestamp deleteTime);

    void update(Joblog joblog);

    Joblog selectById(@Param("joblogId") String joblogId);

    List<Joblog> selectAllTerms(Joblog joblog);

    int selectAllTermsCont(Joblog joblog);

    void deletes(@Param("joblogId") String joblogId, @Param("deleteName") String deleteName, @Param("deleteTime") Timestamp deleteTime);

}
