package com.oa.core.controller.util;

import com.oa.core.bean.dd.FieldData;
import com.oa.core.bean.dd.TaskData;
import com.oa.core.helper.StringHelper;
import com.oa.core.service.dd.DictionaryService;
import com.oa.core.service.util.TableService;
import com.oa.core.util.FieldTypeUtil;
import com.oa.core.util.MySqlUtil;
import com.oa.core.util.SpringContextUtil;
import com.oa.core.util.StringToHtmlUtil;

import java.util.*;

/**
 * @ClassName:FormUtil
 * @author:zxd
 * @Date:2019/03/22
 * @Time:下午 1:44
 * @Version V1.0
 * @Explain
 */
public class FormUtil {

    private DictionaryService d = (DictionaryService) SpringContextUtil.getBean("dictionaryService");
    private TableService t = (TableService) SpringContextUtil.getBean("tableService");

    public List<HashMap> getFormHtml(String userId,TaskData taskData){
        return getFormHtml(userId,taskData,null);
    }
    public List<HashMap> getFormHtml(String userId,TaskData taskData,String recorderNO){
        return getFormHtml(userId,taskData,recorderNO,"recorderNO");
    }
    public List<HashMap> getFormHtml(String userId,TaskData taskData,String recorderNO,String pkid){
        List<HashMap> list = new ArrayList<>();

        String taskField = taskData.getTaskField();
        Vector<String> fields = StringHelper.string2Vector(taskField, ";");
        Map<String, Object> sqlvalue = null;
        String tableId= taskData.getTableName();
        boolean sqlval = false;
        if (recorderNO != null && !recorderNO.equals("newformdata")) {
            List<String> where = new ArrayList<String>();
            where.add(" "+pkid+"='" + recorderNO + "'");
            if(pkid!=null){
                sqlvalue = t.selectSqlMap(MySqlUtil.getFieldSql2(fields, tableId, where,"",""));
            }else {
                sqlvalue = t.selectSqlMap(MySqlUtil.getSql(fields, tableId, where));
            }
            if (sqlvalue != null) {
                sqlval = true;
            }
        }
        StringToHtmlUtil sth = new StringToHtmlUtil();
        if (!fields.contains(pkid)) {
            String value = "";
            if(sqlvalue!=null && sqlvalue.size()>0) {
                value = String.valueOf(sqlvalue.get(pkid));
            }
            FieldData fieldData = sth.getFieldData(pkid);
            HashMap<String,Object> hm = new HashMap();
            hm.put("name",tableId+"_"+fieldData.getFieldName());
            hm.put("title",fieldData.getFieldTitle());
            hm.put("type","pkid");
            hm.put("value",value);
            hm.put("length",fieldData.getFieldNum());
            hm.put("required",fieldData.getRequired());
            hm.put("placeholder",fieldData.getDefaultVal());
            hm.put("option",fieldData.getOptionVal());
            hm.put("decimal",fieldData.getFieldDigit());
            list.add(hm);
        }
        List<FieldData> childbl = new ArrayList<>();
        String taskName =null;
        for (int i = 0, n = fields.size(); i < n; i++) {
            String field = fields.get(i);
            FieldData fieldData = sth.getFieldData(field);
            String fieldType = fieldData.getFieldType();
            if (fieldType != null && fieldType.equals("child")) {
                childbl.add(fieldData);
            } else {
                String value = "";
                if (sqlval) {
                    value = String.valueOf(sqlvalue.get(field));
                }

                String special = fieldData.getSpecial();
                if (FieldTypeUtil.isNull(value) && !FieldTypeUtil.isNull(special)) {
                    Hashtable ht = FieldTypeUtil.getSpecial(special);
                    String input = String.valueOf(ht.get("input"));
                    if (!FieldTypeUtil.isNull(input)) {
                        value = input;
                    }
                    if(ht!=null){
                        taskName = (String) ht.get("carryform");
                    }
                }
                HashMap<String, Object> hm = new HashMap();

                hm.put("name", tableId + "_" + fieldData.getFieldName());
                hm.put("title", fieldData.getFieldTitle());
                hm.put("type", fieldData.getFieldType());
                hm.put("value", value);
                hm.put("length", fieldData.getFieldNum());
                hm.put("required", fieldData.getRequired());
                hm.put("placeholder", fieldData.getDefaultVal());
                hm.put("option", fieldData.getOptionVal());
                hm.put("decimal", fieldData.getFieldDigit());
                hm.put("taskName", taskName);

                list.add(hm);
            }
        }
        if (childbl != null && childbl.size() > 0) {
            String fieldValue = userId;
            if (sqlvalue != null && sqlvalue.size() > 0) {
                fieldValue = (String) sqlvalue.get(pkid);
            }
            for (FieldData fieldData : childbl) {
                String special = fieldData.getSpecial();
                Hashtable ht = FieldTypeUtil.getSpecial(special);
                String value = "/views/formpage/ChildTable.html?formid=" + ht.get("formcmName") + "&linkId=" + fieldValue;
                HashMap<String, Object> hm = new HashMap();
                hm.put("name", tableId + "_" + fieldData.getFieldName());
                hm.put("title", fieldData.getFieldTitle());
                hm.put("type", fieldData.getFieldType());
                hm.put("value", value);
                hm.put("length", fieldData.getFieldNum());
                hm.put("required", fieldData.getRequired());
                hm.put("placeholder", fieldData.getDefaultVal());
                hm.put("option", fieldData.getOptionVal());
                hm.put("decimal", fieldData.getFieldDigit());
                list.add(hm);
            }
        }
        return list;
    }

    public List<HashMap> getTableField(TaskData taskData){
        List<HashMap> list = new ArrayList<>();

        String tableId= taskData.getTableName();
        Vector<String> fields = StringHelper.string2Vector(taskData.getTaskField(), ";");

        StringToHtmlUtil sth = new StringToHtmlUtil();
        for (int i = 0, n = fields.size(); i < n; i++) {
            String field = fields.get(i);
            FieldData fieldData = sth.getFieldData(field);
            HashMap hm = new HashMap();
            hm.put("name",fieldData.getFieldName());
            hm.put("title",fieldData.getFieldTitle());
            hm.put("type",fieldData.getFieldType());
            list.add(hm);
        }
        return list;
    }

    public List<Map<String,Object>> getTableValue(TaskData taskData){
        return getTableValue(taskData,null);
    }
    public List<Map<String,Object>> getTableValue(TaskData taskData,List<String> where){
        return getTableValue(taskData,where,null);
    }

    public List<Map<String,Object>> getTableValue(TaskData taskData,List<String> where, String limit){
        return getTableValue(taskData,where,limit,null);
    }

    public List<Map<String,Object>> getTableValue(TaskData taskData,List<String> where, String limit,String pkid){
        String tableId= taskData.getTableName();
        String taskField = taskData.getTaskField();
        Vector<String> fields = StringHelper.string2Vector(taskField, ";");
        String order = "order by recordTime desc";
        if(pkid!=null){
            fields.add(pkid+" as recorderNO");
            return t.selectSqlMapList(MySqlUtil.getFieldSql2(fields, tableId, where, order, limit));
        }else {
            return t.selectSqlMapList(MySqlUtil.getSql(fields, tableId, where, order, limit));
        }
    }
}
