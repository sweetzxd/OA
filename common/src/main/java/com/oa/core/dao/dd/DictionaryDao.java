package com.oa.core.dao.dd;

import com.oa.core.bean.dd.FieldData;
import com.oa.core.bean.dd.TableData;
import com.oa.core.bean.dd.TaskData;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DictionaryDao {

    /**
     * 字段定义
     * */
    /**
     * 新增字段
     */
    void insertField(FieldData fieldData);

    /**
     * 删除字段(注：修改状态字段curStatus=0)
     */
    int deleteField(FieldData fieldData);

    /**
     * 修改字段
     */
    int updateField(FieldData fieldData);

    /**
     * 获取所有字段信息
     */
    List<FieldData> selectAllField();

    /**
     * 根据fieldName获取字段信息
     */
    FieldData selectFieldName(String fieldName);

    /**
     * 根据fieldName获取字段信息
     */
    List<FieldData> selectNotFieldName(List<String> tabfl);

    /**
     * 根据查询条件获取所有字段
     */
    List<FieldData> selectFieldList(FieldData fieldData);

    /**
     * 获取所有字段的数量
     */
    int selectFieldCount(FieldData fieldData);

    /**
     * 根据关联数据表查询字段
     */
    List<FieldData> selectFieldByTable(List<String> list);

    List<FieldData> selectFieldByIds(List<String> list);
    /**
     * 表定义
     * */
    /**
     * 新增字段
     */
    void insertTable(TableData tableData);

    /**
     * 删除字段(注：修改状态字段curStatus=0)
     */
    int deleteTable(TableData tableData);

    /**
     * 修改字段
     */
    int updateTable(TableData tableData);

    /**
     * 获取所有字段信息
     */
    List<TableData> selectAllTable();

    /**
     * 获取字段信息
     */
    TableData selectTableName(String tableName);

    /**
     * 根据查询条件获取所有字段
     */
    List<TableData> selectTableList(TableData tableData);

    /**
     * 获取所有字段的数量
     */
    int selectTableCount(TableData tableData);

    List<TableData> selectTableByIds(List<String> list);

    /**
     * 任务定义
     * */
    /**
     * 新增字段
     */
    void insertTask(TaskData taskData);

    /**
     * 删除字段(注：修改状态字段curStatus=0)
     */
    int deleteTask(TaskData taskData);

    /**
     * 修改字段
     */
    int updateTask(TaskData taskData);

    /**
     * 获取所有字段信息
     */
    List<TaskData> selectAllTask();

    /**
     * 获取字段信息
     */
    TaskData selectTaskName(String taskName);

    /**
     * 列表字段查询
     */
    List<TaskData> selectTaskList(TaskData taskData);

    /**
     * 获取所有字段的数量
     */
    int selectTaskCount(TaskData taskData);

    List<TaskData> selectTaskByIds(List<String> list);

    TaskData listTaskByFormId(@Param("formcmName") String formcmName);

    TaskData formTaskByFormId(@Param("formcmName") String formcmName);

    TaskData listTaskByPageId(@Param("pageId") String pageId);

    TaskData formTaskByPageId(@Param("pageId") String pageId);
}
