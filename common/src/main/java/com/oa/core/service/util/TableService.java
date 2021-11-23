package com.oa.core.service.util;

import com.oa.core.bean.util.Table;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface TableService {

    /**
     * @param table
     * @return 最大主键值
     */
    String getMaxId(Table table);

    /**
     * 执行sql语句
     * @param sql
     * @return List<String>
     */
    List<String> selectSql(@Param("sql") String sql);

    /**
     * 执行sql语句
     * @param sql
     * @return List<Map<String,Object>>
     */
    List<Map<String,Object>> selectSqlMapList(@Param("sql") String sql);

    /**
     * 执行sql语句
     * @param sql
     * @return int
     */
    int selectSqlCount(@Param("sql") String sql);

    /**
     * 执行sql语句
     * @param sql
     * @return Map<String,Object>
     */
    Map<String,Object> selectSqlMap(@Param("sql") String sql);

    /**
     * 执行建表语句
     * @param sql
     * @return
     */
    String creatTable(String sql);

    /**
     * 执行sql语句
     * @param sql
     * @return
     */
    String implementSql(@Param("sql") String sql);

    /**
     * 执行sql语句
     * @param sql
     * @return Map<String,Object>
     */
    int updateSqlMap(@Param("sql") String sql);

    /**
     * 执行sql语句
     * @param sql
     * @return
     */
    void insertSqlMap(@Param("sql") String sql);

    /**
     * 查询考勤天数
     * @param map
     * @return
     */
    public List<Map<String, Object>> selectCheckList(Map<String,Object> map);
}
