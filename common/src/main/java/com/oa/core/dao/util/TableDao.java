package com.oa.core.dao.util;

import com.oa.core.bean.util.Table;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface TableDao {

    /**
     * 根据查询条件获取标最大id
     * @param table
     * @return id
     */
    public String getMaxId(Table table);

    /**
     * 执行sql语句
     * @param sql
     * @return List<String>
     */
    public List<String> selectSql(@Param("sql") String sql);

    /**
     * 执行sql语句
     * @param sql
     * @return List<Map<String,Object>>
     */
    public List<Map<String,Object>> selectSqlMapList(@Param("sql") String sql);

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
    public Map<String,Object> selectSqlMap(@Param("sql") String sql);

    /**
     * 执行建表语句
     * @param sql
     * @return
     */
    public String creatTable(@Param("sql") String sql);

    /**
     * 执行sql语句
     * @param sql
     * @return
     */
    public String implementSql(String sql);
    /**
     * 执行sql语句
     * @param sql
     * @return int
     */
    public int updateSqlMap(@Param("sql") String sql);
    /**
     * 执行sql语句
     * @param sql
     * @return
     */
    public void insertSqlMap(@Param("sql") String sql);

    public List<Map<String, Object>> selectCheckList(Map<String,Object> map);
}
