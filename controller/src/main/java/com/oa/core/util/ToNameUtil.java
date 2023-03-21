package com.oa.core.util;

import com.alibaba.fastjson.JSON;
import com.oa.core.bean.dd.FieldData;
import com.oa.core.bean.module.Department;
import com.oa.core.bean.system.MyUrlRegist;
import com.oa.core.bean.user.UserManager;
import com.oa.core.helper.DateHelper;
import com.oa.core.helper.FileHelper;
import com.oa.core.helper.StringHelper;
import com.oa.core.service.dd.DictionaryService;
import com.oa.core.tag.*;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Vector;

/**
 * @ClassName:IdToNameUtil
 * @author:zxd
 * @Date:2018/09/17
 * @Time:下午 5:27
 * @Version V1.0
 */
public class ToNameUtil {

    public static String getName(String type, Object value) {
        String value1 = "";
        Object value2 = value;
        boolean iii = (type.equals("form") || type.equals("forms") || type.equals("select")  || type.equals("selects"));
        if (value instanceof String) {
            if (((String) value).indexOf("|") >= 0 && iii) {
                String[] values = ((String) value).split("\\|");
                value1 = values[0];
                value2 = values[1];
                /*if(value1 != null && value1 != ""){
                    if(value2 != null && value2 != ""){
                        if("khmc190112001".equals(value1)){

                        }
                    }
                }*/
            }
        }
        return getName(type, value1, value2);
    }
    public static String getName(String type, String dt1, Object value) {
        try {
            if (type.equals("user")) {
                return UserDict.getName((String) value);
            } else if (type.equals("users")) {
                return UserDict.getNames((String) value, ";");
            } else if (type.equals("dept")) {
                return DeptDict.getName((String) value);
            } else if (type.equals("depts")) {
                return DeptDict.getNames((String) value, ";");
            } else if (type.equals("date")) {
                String data = DateHelper.getSqlDate((String) value);
                if (data != null) {
                    return data;
                }
            } else if (type.equals("datetime")) {
                String data = DateHelper.getSqlDateTime((String) value);
                if (data != null) {
                    return data;
                }
            } else if (type.equals("show_name")) {
                UserManager user = UserDict.getData((String) value);
                if (user != null) {
                    String name = user.getName();
                    if (name != null && name.trim().length() > 0) {
                        return name;
                    }
                }
            } else if (type.equals("node")) {
                String data = FlowDict.getName((String) value);
                if (data != null) {
                    return data;
                }
            } else if (type.equals("flow_LabFld")) {
                String data = FlowDict.getFlowLabFldData((String) value);
                if (data != null) {
                    return data;
                }
            } else if (type.equals("node_actor")) {
                String data = FlowDict.getActorData((String) value);
                if (data != null) {
                    return data;
                }
            } else if (type.equals("line_param")) {
                String data = FlowDict.getParam((String) value);
                if (data != null) {
                    return data;
                }
            } else if (type.equals("usertodept")) {
                String dept = DeptDict.getUserDept((String) value);
                return dept;

            } else if (type.equals("todeptid+name")) {
                Department dept = DeptDict.getDeptIdandName((String) value);
                if(dept!=null) {
                    String id = dept.getDeptId();
                    String name = dept.getDeptName();
                    return id + "-" + name;
                }
                return "";
            } else if (type.equals("form")) {
                if (value instanceof String) {
                    String taskName = dt1;
                    String name = "";
                    if(taskName!=null && !taskName.equals("")) {
                        if (taskName.contains("carryform-")) {
                            taskName = taskName.substring(11, taskName.length() - 1);
                        }
                        name = FormDict.getName(taskName + "_" + value);
                    }
                    return name;
                } else {
                    JSONObject jsForm = (JSONObject) value;
                    if (!jsForm.isNull("special") && !jsForm.isNull("value")) {
                        String taskName = (String) jsForm.get("special");
                        if (taskName.contains("carryform-")) {
                            taskName = taskName.substring(11, taskName.length() - 1);
                        }
                        String name = FormDict.getName(taskName + "_" + jsForm.get("value"));
                        return name;
                    }else{
                        return "";
                    }
                }
            } else if (type.equals("forms")) {
                if (value instanceof String) {
                    Vector val = StringHelper.string2Vector((String)value, ";");
                    String taskName = dt1;
                    String name = "";
                    if(taskName!=null && !taskName.equals("")){
                        if (taskName.contains("carryform-")) {
                            taskName = taskName.substring(11, taskName.length() - 1);
                        }
                        for (int i = 0; i < val.size(); i++) {
                            name += FormDict.getName(taskName + "_" + val.get(i)) + " ";
                        }
                    }
                    return name;
                } else {
                    JSONObject jsForm = (JSONObject) value;
                    if (!jsForm.isNull("special") && !jsForm.isNull("value")) {
                        String formval = (String) jsForm.get("value");
                        Vector val = StringHelper.string2Vector(formval, ";");
                        String taskName = (String) jsForm.get("special");
                        if (taskName.contains("carryform-")) {
                            taskName = taskName.substring(11, taskName.length() - 1);
                        }
                        String name = "";
                        for (int i = 0; i < val.size(); i++) {
                            name += FormDict.getName(taskName + "_" + val.get(i)) + " ";
                        }
                        return name;
                    }else{
                        return "";
                    }
                }
            } else if ("select".equals(type) || "selects".equals(type)) {
                if(dt1!=null && !dt1.equals("")) {
                    return DDUtil.selectIdToName(dt1, (String) value);
                }else{
                    return (String) value;
                }
            } else if ("upload".equals(type)) {
                String val = (String) value;
                String out = "";
                if (val != null && !val.equals("")) {
                    File file = new File(val);
                    String ext = FieldTypeUtil.ext(file.getName());
                    out = "<a href='/" + val + "' download='" + file.getName() + "' title='点击下载 " + file.getName() + "'>";
                    out += "<div class='oa-file'>";
                    out += "<i class='layui-icon layui-icon-file'></i>";
                    out += "<p>"+ext+"</p>";
                    out += "</div></a>";
                }
                return out;
            } else if ("uploads".equals(type)) {
                String val = (String) value;
                String out = "";
                if (val != null && !val.equals("")) {
                    String[] files = val.split("\\|");
                    for (int i = 0; i < files.length; i++) {
                        File file = new File(files[i]);
                        String ext = FieldTypeUtil.ext(file.getName());
                        out += " <a href='/" + files[i] + "' download='" + file.getName() + "' title='点击下载 " + file.getName() + "'>";
                        out += "<div class='oa-file'>";
                        out += "<i class='layui-icon layui-icon-file'></i>";
                        out += "<p>"+ext+"</p>";
                        out += "</div></a>";
                    }
                }
                return out;
            } else if (type.equals("filetype")) {
                return FiletypeDict.getName((String) value);
            } else if (type.equals("roles")) {
                return UserDict.getRoleNames((String) value, ";");
            } else if (type.equals("flowname")) {
                String data = FlowDict.getFlowName((String) value);
                if (data != null) {
                    return data;
                }
            }
            return String.valueOf(value);
        } catch (Exception e) {
            e.printStackTrace();
            return String.valueOf(value);
        }
    }

    public static String idToName(String field, String value) {
        String getvalue = value;
        DictionaryService d = (DictionaryService) SpringContextUtil.getBean("dictionaryService");
        FieldData fieldData = d.selectFieldName(field);
        if (fieldData != null) {
            String fieldType = fieldData.getFieldType();
            if (fieldType.equals("user")) {
                getvalue = getName("user", value);
            } else if (fieldType.equals("users")) {
                getvalue = getName("users", value);
            }
        }
        return getvalue;
    }

    public static String tableIdToName(String field, Object value, String type) {
        DictionaryService d = (DictionaryService) SpringContextUtil.getBean("dictionaryService");
        Object val = StringHelper.isNull(value);
        if (val == null || val == "") {
            return "";
        }
        Object getvalue = "";
        if (type != null && type != "") {
            switch (type) {
                case "user":
                    getvalue = getName("user", val);
                    break;
                case "users":
                    getvalue = getName("users", val);
                    break;
                case "dept":
                    getvalue = getName("dept", val);
                    break;
                case "depts":
                    getvalue = getName("depts", val);
                    break;
                case "form":
                    JSONObject jsForm = new JSONObject();
                    jsForm.put("special", d.selectFieldName(field).getSpecial());
                    jsForm.put("value", (String) val);
                    getvalue = getName("form", jsForm);
                    break;
                case "forms":
                    JSONObject jsForms = new JSONObject();
                    jsForms.put("special", d.selectFieldName(field).getSpecial());
                    jsForms.put("value", (String) val);
                    getvalue = getName("forms", jsForms);
                    break;
                case "select":
                    getvalue = DDUtil.selectIdToName(field, (String) val);
                    break;
                case "selects":
                    getvalue = DDUtil.selectIdToName(field, (String) val);
                    break;
                case "date":
                    getvalue = DateHelper.getDateString((Date) val, "yyyy-MM-dd");
                    break;
                case "datetime":
                    getvalue = DateHelper.getDateString((Timestamp) val, "yyyy-MM-dd HH:mm:ss");
                    break;
                case "upload":
                    File f = new File((String) val);
                    getvalue += "<a href='" + (String) val + "' download='" + f.getName() + "' title='点击下载-" + f.getName() + "'><i class='layui-icon layui-icon-file' style='font-size: 20px; color: #1E9FFF;'></i></a>";
                    break;
                case "uploads":
                    String fileval = (String) val;
                    String[] files = fileval.split("\\|");
                    for (int i = 0; i < files.length; i++) {
                        File file = new File(files[0]);
                        getvalue += "<a href='" + files[0] + "' download='" + file.getName() + "' title='点击下载-" + file.getName() + "'><i class='layui-icon layui-icon-file' style='font-size: 20px; color: #1E9FFF;'></i></a>";
                    }
                    break;
                default:
                    getvalue = val;
                    break;
            }
        }
        return String.valueOf(getvalue);
    }
	
}
