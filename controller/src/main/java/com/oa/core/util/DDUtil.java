package com.oa.core.util;

import com.oa.core.bean.dd.FieldData;
import com.oa.core.bean.dd.TaskData;
import com.oa.core.bean.module.Department;
import com.oa.core.helper.StringHelper;
import com.oa.core.listener.InitDataListener;
import com.oa.core.service.ListenerService;
import com.oa.core.service.dd.DictionaryService;
import com.oa.core.service.module.DepartmentService;
import com.oa.core.service.util.TableService;
import com.oa.core.tag.UserDict;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

public class DDUtil {

    private static Map<String, FieldData> data = null;

    public boolean fieldCheck(String type, String value) {
        return checkWeight("fieldData", type, value);
    }

    public boolean tableCheck(String type, String value) {
        return checkWeight("tableData", type, value);
    }

    public boolean taskCheck(String type, String value) {
        return checkWeight("taskData", type, value);
    }

    static {
        initFieldData();
    }

    public static void resetData() {
        if (data != null) {
            data.clear();
        }
        data = null;
    }

    public static void initFieldData() {
        ListenerService listenerService = (ListenerService) SpringContextUtil.getBean("listenerService");
        List<FieldData> tableList = listenerService.listenerField();
        if (tableList.size() > 0) {
            data = new HashMap<String, FieldData>();
            for (FieldData f : tableList) {
                data.put(f.getFieldName(), f);
            }
        }
    }

    public static void initFieldData(String name) {
        DictionaryService d = (DictionaryService) SpringContextUtil.getBean("dictionaryService");
        FieldData fieldData = d.selectFieldName(name);
        if (fieldData != null) {
            data.put(fieldData.getFieldName(), fieldData);
        }
    }

    public static FieldData getFieldData(String name) {
        if (data == null) {
            initFieldData();
        }
        if (data != null) {
            FieldData result = data.get(name);
            if (result == null) {
                initFieldData(name);
                return data.get(name);
            } else {
                return result;
            }
        }
        return null;
    }

    public boolean checkWeight(String key, String type, String value) {
        Hashtable<String, String> fd = InitDataListener.getMapData(key);
        if (type.equals("name")) {
            if (fd.containsKey(value)) {
                return true;
            } else {
                return false;
            }
        } else if (type.equals("title")) {
            if (fd.containsValue(value)) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    public String fieldIDtoName(String id) {
        return idtoname("fieldData", id);
    }

    public String tableIDtoName(String id) {
        return idtoname("tableData", id);
    }

    public String taskIDtoName(String id) {
        return idtoname("taskData", id);
    }

    public String userIDtoName(String id) {
        return idtoname("user", id);
    }

    public String idtoname(String key, String id) {
        Hashtable<String, String> fd = InitDataListener.getMapData(key);
        return fd.get(id);
    }

    public static String selectIdToName(String name, String fieldValue) {
        FieldData fieldData = getFieldData(name);
        String value = "";
        if (fieldData != null) {
            String optionVal = fieldData.getOptionVal();
            if (optionVal != null) {
                String[] option = optionVal.split("\n");
                if (fieldValue.indexOf(",") >= 0) {
                    for (String o : option) {
                        if (o.contains(";")) {
                            String[] op = o.split(";");
                            Vector<String> val = StringHelper.string2Vector(fieldValue, ",");
                            if (val.contains(op[0])) {
                                value += op[1] + " ";
                            }
                        } else {
                            value += fieldValue + " ";
                        }
                    }
                } else {
                    for (String o : option) {
                        if (o.contains(";")) {
                            String[] op = o.split(";");
                            if (fieldValue.equals(op[0])) {
                                value += op[1] + " ";
                            }
                        } else {
                            value += fieldValue + " ";
                        }
                    }
                }
                return value;
            } else {
                return fieldValue;
            }
        } else {
            return fieldValue;
        }

    }

    public static JSONObject getDept(JSONObject jsonObject, int num) {
        JSONObject group = new JSONObject();
        group.put("groupname", jsonObject.getString("name"));
        group.put("id", jsonObject.getString("id"));
        group.put("online", num);
        group.put("list", getEmp(jsonObject.getJSONArray("empName")));
        return group;
    }

    public static JSONArray getEmp(JSONArray empname) {
        JSONArray emplist = new JSONArray();
        if (empname.length() > 0) {
            for (int i = 0, len = empname.length(); i < len; i++) {
                JSONObject e = empname.getJSONObject(i);
                String photo = "/upload/photo/touxiang/头像" + CommonUtil.getRandomNumber(10) + ".png";
                if (!e.isNull("photo")) {
                    photo = e.getString("photo");
                }
                JSONObject emp = new JSONObject();
                emp.put("username", e.getString("staffName"));
                emp.put("id", e.getString("userName"));
                emp.put("status", "online");
                emp.put("sign", "努力工作快乐生活");
                emp.put("avatar", photo);
                emplist.put(i, emp);
            }
        }
        return emplist;
    }
    public static String getTypeWhere(String field, String value) {
        return getTypeWhere(field,value,"in");
    }
    public static String getTypeWhere(String field, String value,String term) {
        String w = null;
        if (value != null && !value.equals("")) {
            FieldData fieldData = DDUtil.getFieldData(field);
            String type = fieldData.getFieldType();
            String special = fieldData.getSpecial();
            switch (type) {
                case "user":
                    w = field + " "+term+" (select userName from userinfo where staffName like '%" + value + "%')";
                    break;
                case "users":
                    w = field + " "+term+" (select userName from userinfo where staffName like '%" + value + "%')";
                    break;
                case "dept":
                    w = field + " "+term+" (select deptId from userinfo where deptName like '%" + value + "%')";
                    break;
                case "depts":
                    w = field + " "+term+" (select deptId from userinfo where deptName like '%" + value + "%')";
                    break;
                case "form":
                    w = getFormWhere(special, value, field,term);
                    break;
                case "forms":
                    w = getFormWhere(special, value, field,term);
                    break;
                default:
                    w = field + " like '%" + value + "%'";
                    break;
            }
        }
        return w;
    }
    private static String getFormWhere(String special, String value, String field) {
        return getFormWhere(special,value,field,"in");
    }
    private static String getFormWhere(String special, String value, String field,String term) {
        String fvalue = null;
        if (special != null && !special.equals("")) {
            if (special.contains("carryform-")) {
                TableService tableService = (TableService) SpringContextUtil.getBean("tableService");
                DictionaryService dictionaryService = (DictionaryService) SpringContextUtil.getBean("dictionaryService");
                String taskName = special.substring(11, special.length() - 1);
                TaskData taskData = dictionaryService.selectTaskName(taskName);

                String tableId = taskData.getTableName();
                String taskField = taskData.getTaskField();
                if(taskField.indexOf("recorderNO")<0){
                    taskField = "recorderNO;"+taskField;
                }
                List<String> fields = StringHelper.string2ArrayList(taskField, ";");
                String sql = "select " + fields.get(0) + " from " + tableId + " where curStatus=2 AND " + fields.get(1) + " like('%" + value + "%')";
                List<String> mlist = tableService.selectSql(sql);
                fvalue = StringHelper.list2String(mlist, "','");
            }
            if (fvalue != null && fvalue.length() > 4) {
                fvalue = fvalue.substring(0, fvalue.length() - 3);
                return field + " "+term+"( '" + fvalue + "')";
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
}
