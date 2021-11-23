package com.oa.core.service.util;

import com.oa.core.bean.util.Table;
import com.oa.core.dao.util.TableDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service("tableService")
public class TableServiceImpl implements TableService {
    @Resource
    private TableDao tableDao;

    @Override
    public String getMaxId(Table table) {
        return tableDao.getMaxId(table);
    }

    @Override
    public List<String> selectSql(@Param("sql") String sql) {
        return tableDao.selectSql(sql);
    }

    @Override
    public List<Map<String, Object>> selectSqlMapList(String sql) {
        return tableDao.selectSqlMapList(sql);
    }

    @Override
    public int selectSqlCount(String sql) {
        return tableDao.selectSqlCount(sql);
    }

    @Override
    public Map<String, Object> selectSqlMap(String sql) {
        return tableDao.selectSqlMap(sql);
    }

    @Override
    public String creatTable(String sql) {
        return tableDao.creatTable(sql);
    }

    @Override
    public String implementSql(String sql) {
        return tableDao.implementSql(sql);
    }

    @Override
    public int updateSqlMap(String sql) {
        return tableDao.updateSqlMap(sql);
    }

    @Override
    public void insertSqlMap(@Param("sql") String sql) {
        tableDao.insertSqlMap(sql);
    }

    @Override
    public List<Map<String, Object>> selectCheckList(Map<String,Object> map) {
        return tableDao.selectCheckList(map);
    }

    @Override
    public List<Map<String, Object>> selectCheckListold(Map<String, Object> map) {
        return tableDao.selectCheckListold(map);
    }

}
