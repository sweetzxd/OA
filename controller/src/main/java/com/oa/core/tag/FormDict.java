package com.oa.core.tag;

import com.oa.core.bean.dd.TaskData;
import com.oa.core.helper.StringHelper;
import com.oa.core.service.dd.DictionaryService;
import com.oa.core.service.util.TableService;
import com.oa.core.util.MySqlUtil;
import com.oa.core.util.SpringContextUtil;

import java.util.*;

/**
 * @ClassName:formDict
 * @author:zxd
 * @Date:2018/11/09
 * @Time:上午 11:13
 * @Version V1.0
 * @Explain 携带表单ID转Name
 */
public class FormDict {

    private static Map<String, Map<String, String>> datas = null;
    static {
        initData();
    }

    public static void resetData() {
        if (datas != null) {
            datas.clear();
        }
        datas = null;
    }

    public static void initData() {
        if (datas == null) {
            datas = new HashMap<>();
        }
        DictionaryService d = (DictionaryService) SpringContextUtil.getBean("dictionaryService");
        TaskData td = new TaskData();
        td.setTaskType("携带");
        List<TaskData> tl =  d.selectTaskList(td);
        TableService t = (TableService) SpringContextUtil.getBean("tableService");
        for(TaskData taskData : tl) {
            String taskId = taskData.getTaskName();
            String tableId = taskData.getTableName();
            String fieldIds = taskData.getTaskField();
            Vector<String> field = StringHelper.string2Vector(fieldIds,";");
            String sql = MySqlUtil.getSql(field, tableId);
            List<Map<String, Object>> mlist = t.selectSqlMapList(sql);
            putValue(taskId, mlist,field.get(0));
        }
    }

    /**
     * 重新从数据库中获取数据
     *
     * @param name 表单任务编号
     */
    public static void initFormData(String name) {
        DictionaryService d = (DictionaryService) SpringContextUtil.getBean("dictionaryService");
        TaskData taskData =  d.selectTaskName(name);
        TableService t = (TableService) SpringContextUtil.getBean("tableService");
        String taskId = taskData.getTaskName();
        String tableId = taskData.getTableName();
        String fieldIds = taskData.getTaskField();
        Vector<String> field = StringHelper.string2Vector(fieldIds,";");
        String sql = MySqlUtil.getSql(field, tableId);
        List<Map<String, Object>> mlist = t.selectSqlMapList(sql);
        putValue(taskId, mlist,field.get(0));
    }

    private static void putValue(String taskId, List<Map<String, Object>> mlist,String field) {
        if (mlist.size() > 0) {
            Map<String, String> data = new HashMap<>();
            for (Map<String, Object> m : mlist) {
                data.put((String) m.get("recorderNO"), String.valueOf(m.get(field)));
            }
            datas.put(taskId,data);
        }
    }

    public static String getData(String name,String id) {
        if (datas == null) {
            datas = new HashMap<>();
            initFormData(name);
        }
        if (datas != null) {
            Map<String, String> map = datas.get(name);
            if(map!=null){
                String result = map.get(id);
                return result;
            }else{
                initFormData(name);
                map = datas.get(name);
                if(map!=null){
                    String result = map.get(id);
                    return result;
                }else{
                    return id;
                }
            }
        }
        return null;
    }

    public static String getName(String values) {
        String[] value = values.split("_");
        String map = getData(value[0],value[1]);
        if(map!=null){
            return " "+map+" ";
        }else{
            return " "+value[1]+" ";
        }

    }

}
