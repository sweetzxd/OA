package com.oa.core.dao.module;

import com.oa.core.bean.module.Joblog;
import org.apache.ibatis.annotations.Param;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

public interface JoblogDao {

    void insert(Joblog joblog);

    void delete(@Param("joblogId") String joblogId, @Param("deleteName") String deleteName, @Param("deleteTime") Timestamp deleteTime);

    void update(Joblog joblog);

    Joblog selectById(@Param("joblogId") String joblogId);

    List<Joblog> selectAllTerms(Joblog joblog);

    int selectAllTermsCont(Joblog joblog);

    void deletes(@Param("joblogId") String joblogId, @Param("deleteName") String deleteName, @Param("deleteTime") Timestamp deleteTime);

    List<Joblog> selectAllJobLog();

    List<Map<String, Object>> selectJobLogByMonth(Map<String, Object> map);

    List<Map<String, Object>> selectJoblogByDay(Map<String, Object> map);

    List<Map<String, Object>> selectJobLogByCsuser(Map<String, Object> map);

    List<Map<String, Object>> selectJoblogByCsuserMonth(Map<String, Object> map);

    List<Map<String, Object>> selectJobLogByLeader(Map<String, Object> map);

    List<Map<String, Object>> selectJobLogByLeaderMonth(Map<String, Object> map);

    List<Map<String, Object>> selectJoblogByLeaderDay(Map<String, Object> map);
}
