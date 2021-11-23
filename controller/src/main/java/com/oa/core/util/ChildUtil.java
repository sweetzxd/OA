package com.oa.core.util;

import com.oa.core.bean.dd.FieldData;
import com.oa.core.bean.dd.TaskData;
import com.oa.core.bean.system.FormCustomMade;
import com.oa.core.helper.StringHelper;
import com.oa.core.service.dd.DictionaryService;
import com.oa.core.service.system.FormCustomMadeService;
import com.oa.core.service.util.TableService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName:childUtil
 * @author:zxd
 * @Date:2018/11/26
 * @Time:上午 10:28
 * @Version V1.0
 * @Explain
 */
public class ChildUtil {
    private static TableService tableService = (TableService)SpringContextUtil.getBean("tableService");
    private static FormCustomMadeService formCustomMadeService = (FormCustomMadeService)SpringContextUtil.getBean("formCustomMadeService");
    private static DictionaryService dictionaryService = (DictionaryService)SpringContextUtil.getBean("dictionaryService");

    public static boolean setChildUtil(FieldData fieldData,String linkId,String userId){
        try {
            String special = fieldData.getSpecial();
            String formcmName = special.substring(special.indexOf("-") + 1,special.length()-1);
            FormCustomMade formcm = formCustomMadeService.selectFormCMByID(formcmName);
            String formTask = formcm.getFormTask();
            TaskData taskData = dictionaryService.selectTaskName(formTask);
            String tableName = taskData.getTableName();
            Map<String, Object> field = new HashMap<>();
            field.put("linkRecorderNO", linkId);
            List<String> where = new ArrayList<>();
            where.add("linkRecorderNO = '" + userId + "'");
            String sql = MySqlUtil.getUpdateSql(tableName, field, where);
            tableService.updateSqlMap(sql);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
