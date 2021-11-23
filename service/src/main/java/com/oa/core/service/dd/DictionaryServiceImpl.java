package com.oa.core.service.dd;

import com.oa.core.bean.dd.FieldData;
import com.oa.core.bean.dd.TableData;
import com.oa.core.bean.dd.TaskData;
import com.oa.core.dao.dd.DictionaryDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("dictionaryService")
public class DictionaryServiceImpl implements DictionaryService{
    @Resource
    private DictionaryDao dictionaryDao;

    /**
     * 字段定义
     * */
    @Override
    public void insertField(FieldData fieldData) { dictionaryDao.insertField(fieldData); }

    @Override
    public int deleteField(FieldData fieldData) {
        return dictionaryDao.deleteField(fieldData);
    }
    @Override
    public int updateField(FieldData fieldData) { return dictionaryDao.updateField(fieldData); }
    @Override
    public List<FieldData> selectAllField() {
        return dictionaryDao.selectAllField();
    }
    @Override
    public FieldData selectFieldName(String fieldName) {
        return dictionaryDao.selectFieldName(fieldName);
    }
    @Override
    public List<FieldData> selectNotFieldName(List<String> tabfl) {
        return dictionaryDao.selectNotFieldName(tabfl);
    }
    @Override
    public List<FieldData> selectFieldList(FieldData fieldData) {
        return dictionaryDao.selectFieldList(fieldData);
    }
    @Override
    public int selectFieldCount(FieldData fieldData) {
        return dictionaryDao.selectFieldCount(fieldData);
    }
    @Override
    public List<FieldData> selectFieldByTable(List<String> list) { return dictionaryDao.selectFieldByTable(list); }
    @Override
    public List<FieldData> selectFieldByIds(List<String> list) {
        return dictionaryDao.selectFieldByIds(list);
    }

    /**
     * 表定义
     * */
    @Override
    public void insertTable(TableData tableData) { dictionaryDao.insertTable(tableData); }
    @Override
    public int deleteTable(TableData tableData) {
        return dictionaryDao.deleteTable(tableData);
    }
    @Override
    public int updateTable(TableData tableData) {
        return dictionaryDao.updateTable(tableData);
    }
    @Override
    public List<TableData> selectAllTable() { return dictionaryDao.selectAllTable(); }
    @Override
    public TableData selectTableName(String tableName) {
        return dictionaryDao.selectTableName(tableName);
    }
    @Override
    public List<TableData> selectTableList(TableData tableData) { return dictionaryDao.selectTableList(tableData); }
    @Override
    public int selectTableCount(TableData tableData) {
        return dictionaryDao.selectTableCount(tableData);
    }
    @Override
    public List<TableData> selectTableByIds(List<String> list) {
        return dictionaryDao.selectTableByIds(list);
    }

    /**
     * 任务定义
     * */
    @Override
    public void insertTask(TaskData taskData) { dictionaryDao.insertTask(taskData); }
    @Override
    public int deleteTask(TaskData taskData) { return dictionaryDao.deleteTask(taskData); }
    @Override
    public int updateTask(TaskData taskData) { return dictionaryDao.updateTask(taskData); }
    @Override
    public List<TaskData> selectAllTask() {
        return dictionaryDao.selectAllTask();
    }
    @Override
    public TaskData selectTaskName(String taskName) {
        return dictionaryDao.selectTaskName(taskName);
    }
    @Override
    public List<TaskData> selectTaskList(TaskData taskData) {
        return dictionaryDao.selectTaskList(taskData);
    }
    @Override
    public int selectTaskCount(TaskData taskData) {
        return dictionaryDao.selectTaskCount(taskData);
    }
    @Override
    public List<TaskData> selectTaskByIds(List<String> list) {
        return dictionaryDao.selectTaskByIds(list);
    }

    @Override
    public TaskData listTaskByFormId(String formcmName) {
        return dictionaryDao.listTaskByFormId(formcmName);
    }

    @Override
    public TaskData formTaskByFormId(String formcmName) {
        return dictionaryDao.formTaskByFormId(formcmName);
    }

    @Override
    public TaskData listTaskByPageId(String pageId) {
        return dictionaryDao.listTaskByPageId(pageId);
    }

    @Override
    public TaskData formTaskByPageId(String pageId) {
        return dictionaryDao.formTaskByPageId(pageId);
    }

}
