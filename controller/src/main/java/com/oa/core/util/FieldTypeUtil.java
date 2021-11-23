package com.oa.core.util;

import com.oa.core.bean.Loginer;
import com.oa.core.bean.dd.FieldData;
import com.oa.core.bean.dd.TaskData;
import com.oa.core.bean.system.FormCustomMade;
import com.oa.core.bean.system.MyUrlRegist;
import com.oa.core.bean.util.Table;
import com.oa.core.helper.DateHelper;
import com.oa.core.helper.FileHelper;
import com.oa.core.helper.StringHelper;
import com.oa.core.listener.InitDataListener;
import com.oa.core.service.dd.DictionaryService;
import com.oa.core.service.system.FormCustomMadeService;
import com.oa.core.service.system.MyUrlRegistService;
import com.oa.core.service.util.TableService;
import com.oa.core.tag.DeptDict;
import com.oa.core.tag.FormDict;
import com.oa.core.tag.UserDict;
import org.json.JSONObject;
import org.junit.Test;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.activation.DataHandler;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.*;

public class FieldTypeUtil {
    /**
     * 字段类型
     */
    static String[] fieldType = new String[]{"text", "textarea", "select", "selects", "date", "datetime", "int", "decimal", "user", "users", "dept", "depts", "form", "forms", "upload", "uploads", "child", "editor", "selectcity"};

    public static List<String> fieldType() {
        List<String> type = new ArrayList<>();
        for (int i = 0; i < fieldType.length; i++) {
            type.add(fieldType[i]);
        }
        return type;
    }

    public static boolean nodeActorField(String nodeFieldType) {
        List<String> type = new ArrayList<>();
        type.add("user");
        type.add("users");
        type.add("dept");
        type.add("depts");
        if (type.contains(nodeFieldType)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 字段数据检查
     */
    public static String fieldVerify(int num) {
        return fieldVerify("", num);
    }

    public static String fieldVerify(String type, int num) {
        String verify;
        switch (num) {
            /*必填*/
            case 1:
                //verify = "onblur=\"formverify(this,'" + type + "')\" lay-verify='required'";
                verify = " required ";
                break;
            default:
                verify = "";
                break;
        }
        return verify;
    }

    /**
     * 字符串类型
     */
    public static String fieldText(String tableId, FieldData fieldData) {
        return fieldText(tableId, fieldData, null);
    }

    public static String fieldText(String tableId, FieldData fieldData, String fieldValue) {
        String special = fieldData.getSpecial();
        Hashtable ht = getSpecial(special);
        fieldValue = valueOf(fieldValue);
        String fieldName = fieldData.getFieldName();
        String fieldTitle = fieldData.getFieldTitle();
        int fieldNum = fieldData.getFieldNum();
        String id = tableId + "_" + fieldName;
        String show = "";
        int required = fieldData.getRequired();

        String fieldHtml = "";
        fieldHtml += "<div class='layui-form-item component' data-method='offset'>";
        fieldHtml += "<label class='layui-form-label title' id='" + id + "_Title' data-type='text'>" + fieldTitle + "</label>";
        if (required == 1) {
            fieldHtml += "<span class='layui-form-mid required' style='display:block;'>*</span>";
        } else {
            fieldHtml += "<span class='layui-form-mid required' style='display:none;'>*</span>";
        }
        fieldHtml += "<div class='layui-input-block'>";
        fieldHtml += "<input type='text' id='" + id + "_Value' name='" + id + "_Value' maxlength='" + fieldNum + "'";
        fieldHtml += fieldVerify(required);
        fieldHtml += " autocomplete='off' class='layui-input' value='" + fieldValue + "'></div>";
        fieldHtml += "<p class='layui-form-mid layui-word-aux' id='" + id + "_Explain'>" + show + "</p></div>";
        return fieldHtml;
    }

    /**
     * 长文本类型
     */
    public static String fieldTextarea(String tableId, FieldData fieldData) {
        return fieldTextarea(tableId, fieldData, null);
    }

    public static String fieldTextarea(String tableId, FieldData fieldData, String fieldValue) {
        String special = fieldData.getSpecial();
        Hashtable ht = getSpecial(special);
        fieldValue = valueOf(fieldValue);
        String fieldName = fieldData.getFieldName();
        String fieldTitle = fieldData.getFieldTitle();
        int fieldNum = fieldData.getFieldNum();
        String id = tableId + "_" + fieldName;
        String show = "";
        int required = fieldData.getRequired();

        String fieldHtml = "";
        fieldHtml += "<div class='layui-form-item component' data-method='offset'>";
        fieldHtml += "<label class='layui-form-label title' id='" + id + "_Title' data-type='textarea'>" + fieldTitle + "</label>";
        if (required == 1) {
            fieldHtml += "<span class='layui-form-mid required' style='display:block;'>*</span>";
        } else {
            fieldHtml += "<span class='layui-form-mid required' style='display:none;'>*</span>";
        }
        fieldHtml += "<div class='layui-input-block'>";
        fieldHtml += "<textarea id='" + id + "_Value' name='" + id + "_Value' maxlength='" + fieldNum + "'";
        fieldHtml += fieldVerify(required);
        fieldHtml += " class='layui-textarea'>" + fieldValue + "</textarea></div>";
        fieldHtml += "<p class='layui-form-mid layui-word-aux' id='" + id + "_Explain'>" + show + "</p></div>";
        return fieldHtml;
    }

    /**
     * 数据选项（单）
     */
    public static String fieldSelect(String tableId, FieldData fieldData) {
        return fieldSelect(tableId, fieldData, null);
    }

    public static String fieldSelect(String tableId, FieldData fieldData, String fieldValue) {
        String special = fieldData.getSpecial();
        Hashtable ht = getSpecial(special);
        fieldValue = valueOf(fieldValue);
        String fieldName = fieldData.getFieldName();
        String fieldTitle = fieldData.getFieldTitle();
        int fieldNum = fieldData.getFieldNum();
        String id = tableId + "_" + fieldName;
        String show = "";
        int required = fieldData.getRequired();

        String fieldHtml = "";
        fieldHtml += "<div class='layui-form-item component' data-method='offset'>";
        fieldHtml += "<label class='layui-form-label title' id='" + id + "_Title' data-type='select'>" + fieldTitle + "</label>";
        if (required == 1) {
            fieldHtml += "<span class='layui-form-mid required' style='display:block;'>*</span>";
        } else {
            fieldHtml += "<span class='layui-form-mid required' style='display:none;'>*</span>";
        }
        fieldHtml += "<div class='layui-input-block'>";
        fieldHtml += "<select id='" + id + "_Value' name='" + id + "_Value' style='display: none'";
        fieldHtml += fieldVerify(required);
        fieldHtml += " lay-filter='selone' maxlength='" + fieldNum + "'>";
        String[] options = fieldData.getOptionVal().split("\n");
        fieldHtml += "<option value>请选择</option>";
        for (int i = 0; i < options.length; i++) {
            String option = options[i];
            if (option.contains(";")) {
                String[] k = option.split(";");
                fieldHtml += "<option value='" + k[0] + "'";
                if (fieldValue != null && fieldValue.equals(k[0])) {
                    fieldHtml += " selected ";
                }
                fieldHtml += " >" + k[1] + "</option>";
            } else {
                fieldHtml += "<option value='" + option + "'";
                if (fieldValue != null && fieldValue.equals(option)) {
                    fieldHtml += " selected ";
                }
                fieldHtml += " >" + option + "</option>";
            }
        }
        fieldHtml += "</select></div>";
        fieldHtml += "<p class='layui-form-mid layui-word-aux' id='" + id + "_Explain'>" + show + "</p></div>";
        return fieldHtml;
    }

    /**
     * 数据选项（多）
     */
    public static String fieldSelects(String tableId, FieldData fieldData) {
        return fieldSelects(tableId, fieldData, null);
    }

    public static String fieldSelects(String tableId, FieldData fieldData, String fieldValue) {
        String special = fieldData.getSpecial();
        Hashtable ht = getSpecial(special);
        fieldValue = valueOf(fieldValue);
        String fieldName = fieldData.getFieldName();
        String fieldTitle = fieldData.getFieldTitle();
        int fieldNum = fieldData.getFieldNum();
        String id = tableId + "_" + fieldName;
        String show = "";
        int required = fieldData.getRequired();

        String fieldHtml = "";
        fieldHtml += "<div class='layui-form-item component' data-method='offset'>";
        fieldHtml += "<label class='layui-form-label title' id='" + id + "_Title' data-type='selects'>" + fieldTitle + "</label>";
        if (required == 1) {
            fieldHtml += "<span class='layui-form-mid required' style='display:block;'>*</span>";
        } else {
            fieldHtml += "<span class='layui-form-mid required' style='display:none;'>*</span>";
        }
        fieldHtml += "<div class='layui-input-block'>";
        fieldHtml += "<select id='" + id + "_Value' name='" + id + "_Value' ";
        fieldHtml += fieldVerify(required);
        fieldHtml += "xm-select='field_selects' xm-select-show-count='2' lay-filter='selall' maxlength='" + fieldNum + "'>";
        String[] options = fieldData.getOptionVal().split("\n");
        fieldHtml += "<option value>请选择</option>";
        for (int i = 0; i < options.length; i++) {
            String option = options[i];
            if (option.contains(";")) {
                String[] k = option.split(";");
                fieldHtml += "<option value='" + k[0] + "'";
                if (fieldValue != null) {
                    Vector<String> val = StringHelper.string2Vector(fieldValue, ",");
                    if (val.contains(k[0])) {
                        fieldHtml += " selected='selected' ";
                    }
                }
                fieldHtml += " >" + k[1] + "</option>";
            } else {
                fieldHtml += "<option value='" + option + "'";
                if (fieldValue != null) {
                    Vector<String> val = StringHelper.string2Vector(fieldValue, ",");
                    if (val.contains(option)) {
                        fieldHtml += " selected='selected' ";
                    }
                }
                fieldHtml += " >" + option + "</option>";
            }
        }
        fieldHtml += "</select></div>";
        fieldHtml += "<p class='layui-form-mid layui-word-aux' id='" + id + "_Explain'>" + show + "</p></div>";
        return fieldHtml;
    }

    /**
     * 日期
     */
    public static String fieldDate(String tableId, FieldData fieldData) {
        return fieldDate(tableId, fieldData, null);
    }

    public static String fieldDate(String tableId, FieldData fieldData, Date fieldValue) {
        String special = fieldData.getSpecial();
        Hashtable<String, String> ht = getSpecial(special);
        String value = "";
        if (fieldValue != null && !fieldValue.equals("")) {
            value = DateHelper.getDateString(fieldValue, "yyyy-MM-dd");
        }else{
            value = ht.get("input");
        }
        String fieldName = fieldData.getFieldName();
        String fieldTitle = fieldData.getFieldTitle();
        int fieldNum = fieldData.getFieldNum();
        String id = tableId + "_" + fieldName;
        String show = "";
        int required = fieldData.getRequired();

        String validate = "";
        if (ht.get("laterTo") != null && !(ht.get("laterTo")).equals("")) {
            validate = " laterTo='" + tableId + "_" + ht.get("laterTo") + "_Value' ";
        } else if (ht.get("toLater") != null && !(ht.get("toLater")).equals("")) {
            validate = " toLater='" + tableId + "_" + ht.get("toLater") + "_Value' ";
        }
        String fieldHtml = "";
        fieldHtml += "<div class='layui-form-item component' data-method='offset'>";
        fieldHtml += "<label class='layui-form-label title' id='" + id + "_Title' data-type='date'>" + fieldTitle + "</label>";
        if (required == 1) {
            fieldHtml += "<span class='layui-form-mid required' style='display:block;'>*</span>";
        } else {
            fieldHtml += "<span class='layui-form-mid required' style='display:none;'>*</span>";
        }
        fieldHtml += "<div class='layui-input-block'>";
        fieldHtml += "<input type='text' id='" + id + "_Value' name='" + id + "_Value'  autocomplete='off' ";
        fieldHtml += fieldVerify(required) + validate;
        fieldHtml += "autocomplete='off' class='layui-input date' value='" + value + "' maxlength='10'>";
        fieldHtml += "</div>";
        fieldHtml += "<p class='layui-form-mid layui-word-aux' id='" + id + "_Explain'>" + show + "</p></div>";
        return fieldHtml;
    }

    /**
     * 日期+时间
     */
    public static String fieldDateTime(String tableId, FieldData fieldData) {
        return fieldDateTime(tableId, fieldData, null);
    }

    public static String fieldDateTime(String tableId, FieldData fieldData, Timestamp fieldValue) {
        String special = fieldData.getSpecial();
        Hashtable<String, String> ht = getSpecial(special);
        String value = "";
        if (fieldValue != null && !fieldValue.equals("")) {
            value = DateHelper.getDateString(fieldValue, "yyyy-MM-dd HH:mm:ss");
        } else {
            if (ht.get("dateStar") != null || ht.get("dateEnd") != null) {
                value = ht.get("dateStar") == null ? ht.get("dateEnd") : ht.get("dateStar");
            }
        }
        String fieldName = fieldData.getFieldName();
        String fieldTitle = fieldData.getFieldTitle();
        int fieldNum = fieldData.getFieldNum();
        String id = tableId + "_" + fieldName;
        String show = "";
        int required = fieldData.getRequired();

        String validate = "";
        if (ht.get("laterTo") != null && !(ht.get("laterTo")).equals("")) {
            validate = " laterTo='" + tableId + "_" + ht.get("laterTo") + "_Value' ";
        } else if (ht.get("toLater") != null && !(ht.get("toLater")).equals("")) {
            validate = " toLater='" + tableId + "_" + ht.get("toLater") + "_Value' ";
        }

        String fieldHtml = "";
        fieldHtml += "<div class='layui-form-item component' data-method='offset'>";
        fieldHtml += "<label class='layui-form-label title' id='" + id + "_Title' data-type='datetime'>" + fieldTitle + "</label>";
        if (required == 1) {
            fieldHtml += "<span class='layui-form-mid required' style='display:block;'>*</span>";
        } else {
            fieldHtml += "<span class='layui-form-mid required' style='display:none;'>*</span>";
        }
        fieldHtml += "<div class='layui-input-block'>";
        fieldHtml += "<input type='text' id='" + id + "_Value' name='" + id + "_Value'  autocomplete='off' ";
        fieldHtml += fieldVerify(required) + validate;
        fieldHtml += "autocomplete='off' class='layui-input datetime' value='" + value + "' maxlength='19'>";
        fieldHtml += "</div>";
        fieldHtml += "<p class='layui-form-mid layui-word-aux' id='" + id + "_Explain'>" + show + "</p></div>";
        return fieldHtml;
    }

    /**
     * 整数
     */
    public static String fieldInt(String tableId, FieldData fieldData) {
        return fieldInt(tableId, fieldData, 0);
    }

    public static String fieldInt(String tableId, FieldData fieldData, int fieldValue) {
        String special = fieldData.getSpecial();
        Hashtable ht = getSpecial(special);
        String fieldName = fieldData.getFieldName();
        String fieldTitle = fieldData.getFieldTitle();
        int fieldNum = fieldData.getFieldNum();
        String id = tableId + "_" + fieldName;
        String show = "";
        int required = fieldData.getRequired();


        String formula = "";
        StringBuffer sql = new StringBuffer("SELECT recorderNO FROM DesignFormulas WHERE curStatus=2 AND tableName ='"+tableId+"'");
        TableService tableService = (TableService)SpringContextUtil.getBean("tableService");
        List<Map<String, Object>> list = tableService.selectSqlMapList(sql.toString());
        if(list.size()>0){
            formula = " formula ";
        }

        /*ConfParseUtil cp = new ConfParseUtil();
        String gs = cp.getPoa("gs_gzjc");
        if(gs.contains(fieldName)){
            formula = " formula='" + gs + "' ";
        }*/

        String fieldHtml = "";
        fieldHtml += "<div class='layui-form-item component' data-method='offset'>";
        fieldHtml += "<label class='layui-form-label title' id='" + id + "_Title' data-type='int'>" + fieldTitle + "</label>";
        if (required == 1) {
            fieldHtml += "<span class='layui-form-mid required' style='display:block;'>*</span>";
        } else {
            fieldHtml += "<span class='layui-form-mid required' style='display:none;'>*</span>";
        }
        fieldHtml += "<div class='layui-input-block'>";
        fieldHtml += "<input type='number' id='" + id + "_Value' name='" + id + "_Value' ";
        fieldHtml += fieldVerify(required) + formula;
        fieldHtml += " autocomplete='off' class='layui-input' value='" + fieldValue + "' maxlength='" + fieldNum + "'>";
        fieldHtml += "</div>";
        fieldHtml += "<p class='layui-form-mid layui-word-aux' id='" + id + "_Explain'>" + show + "</p></div>";
        return fieldHtml;
    }

    /**
     * 小数
     */
    public static String fieldDecimal(String tableId, FieldData fieldData) {
        return fieldDecimal(tableId, fieldData, null);
    }

    public static String fieldDecimal(String tableId, FieldData fieldData, BigDecimal fieldValue) {
        String special = fieldData.getSpecial();
        Hashtable ht = getSpecial(special);
        String jsgs = (String)ht.get("formula");
        String value = valueOf(fieldValue);
        String fieldName = fieldData.getFieldName();

        String fieldTitle = fieldData.getFieldTitle();
        int fieldNum = fieldData.getFieldNum();
        int fieldDigit = fieldData.getFieldDigit();
        String id = tableId + "_" + fieldName;
        String show = "";
        int required = fieldData.getRequired();

        String formula = "Z2018120600001,Z2018120600002,Z2018120600003";
        formula = " formula='" + formula + "' ";
        /*ConfParseUtil cp = new ConfParseUtil();
        String gs = cp.getPoa("gzjc_hj");
        if(gs.contains(fieldName)){
            formula = " formula='" + gs + "' ";
        }*/

        String fieldHtml = "";
        fieldHtml += "<div class='layui-form-item component' data-method='offset'>";
        fieldHtml += "<label class='layui-form-label title' id='" + id + "_Title' data-type='decimal'>" + fieldTitle + "</label>";
        if (required == 1) {
            fieldHtml += "<span class='layui-form-mid required' style='display:block;'>*</span>";
        } else {
            fieldHtml += "<span class='layui-form-mid required' style='display:none;'>*</span>";
        }
        fieldHtml += "<div class='layui-input-block'>";
        fieldHtml += "<input type='number' id='" + id + "_Value' name='" + id + "_Value' ";
        fieldHtml += fieldVerify(required) + formula;
        fieldHtml += " autocomplete='off' class='layui-input' value='" + value + "' maxlength='" + fieldNum + "' decimal='" + fieldDigit + "'>";
        fieldHtml += "</div>";
        fieldHtml += "<p class='layui-form-mid layui-word-aux' id='" + id + "_Explain'>" + show + "</p></div>";
        return fieldHtml;
    }

    /**
     * 人员选择（单）
     */
    public static String fieldUser(String tableId, FieldData fieldData) {
        return fieldUser(tableId, fieldData, null);
    }

    public static String fieldUser(String tableId, FieldData fieldData, String fieldValue) {
        String special = fieldData.getSpecial();
        Hashtable ht = getSpecial(special);
        if(fieldValue !=null && !fieldValue.equals("")) {
            fieldValue = valueOf(fieldValue);
        }else{
            fieldValue = (String)ht.get("input");
            fieldValue = fieldValue==null?"":fieldValue;
        }
        String fieldName = fieldData.getFieldName();
        String fieldTitle = fieldData.getFieldTitle();
        int fieldNum = fieldData.getFieldNum();
        String id = tableId + "_" + fieldName;
        String val = id + "_Value";
        String text = id + "_Name";
        String show = "";
        int required = fieldData.getRequired();

        String fieldHtml = "";
        fieldHtml += "<div class='layui-form-item component' data-method='offset'>";
        fieldHtml += "<label class='layui-form-label title' id='" + id + "_Title' data-type='user'>" + fieldTitle + "</label>";
        if (required == 1) {
            fieldHtml += "<span class='layui-form-mid required' style='display:block;'>*</span>";
        } else {
            fieldHtml += "<span class='layui-form-mid required' style='display:none;'>*</span>";
        }
        fieldHtml += "<div class='layui-input-block'>";
        fieldHtml += "<div tabindex='0' id='" + id + "' name='" + id + "' class='layui-input select-input' onClick=\"selectuser(this,'user')\">";
        fieldHtml += "<div class='oa-fa oa-fa-lg oa-fa-input'><i class='iconfont iconfont-form icon-user1 oa-input-select'></i><i class='iconfont icon-tianjia1 oa-input-select-2x'></i></div>";
        if (fieldValue != null && fieldValue != "") {
            if (fieldValue != null && fieldValue.contains(";")) {
                fieldValue = fieldValue.substring(0, fieldValue.length() - 1);
            }
            String name = ToNameUtil.getName("user", fieldValue);
            fieldHtml += "<span value='" + fieldValue + "'><label>" + name + "</label>" +
                    "<i onClick='event.cancelBubble = true;removeval(this)' class='iconfont icon-shanchu1'></i></span>";
        }
        fieldHtml += "</div>";
        fieldHtml += "<input type='hidden' id='" + val + "' name='" + val + "' ";
        fieldHtml += fieldVerify(required);

        String deptfield = (String) ht.get("carrydept");
        if (ht != null && deptfield != null) {
            fieldHtml += " carrydept='" + tableId + "_" + deptfield + "'";
        }

        fieldHtml += "autocomplete='off' value='" + fieldValue + "' class='layui-input' maxlength='" + fieldNum + "'>";

        fieldHtml += "</div>";
        fieldHtml += "<p class='layui-form-mid layui-word-aux' id='" + id + "_Explain'>" + show + "</p></div>";
        return fieldHtml;
    }

    /**
     * 人员选择（多）
     */
    public static String fieldUsers(String tableId, FieldData fieldData) {
        return fieldUsers(tableId, fieldData, null);
    }

    public static String fieldUsers(String tableId, FieldData fieldData, String fieldValue) {
        String special = fieldData.getSpecial();
        Hashtable ht = getSpecial(special);
        if(fieldValue !=null && !fieldValue.equals("")) {
            fieldValue = valueOf(fieldValue);
        }else{
            fieldValue = (String)ht.get("input");
            fieldValue = fieldValue==null?"":fieldValue;
        }
        String fieldName = fieldData.getFieldName();
        String fieldTitle = fieldData.getFieldTitle();
        int fieldNum = fieldData.getFieldNum();
        String id = tableId + "_" + fieldName;
        String val = id + "_Value";
        String text = id + "_Name";
        String show = "";
        int required = fieldData.getRequired();

        String fieldHtml = "";
        fieldHtml += "<div class='layui-form-item component' data-method='offset'>";
        fieldHtml += "<label class='layui-form-label title' id='" + id + "_Title' data-type='users'>" + fieldTitle + "</label>";
        if (required == 1) {
            fieldHtml += "<span class='layui-form-mid required' style='display:block;'>*</span>";
        } else {
            fieldHtml += "<span class='layui-form-mid required' style='display:none;'>*</span>";
        }
        fieldHtml += "<div class='layui-input-block'>";
        fieldHtml += "<div tabindex='0' id='" + id + "' name='" + id + "' class='layui-input select-input' onClick=\"selectuser(this,'users')\">";
        fieldHtml += "<div class='oa-fa oa-fa-lg oa-fa-input'><i class='iconfont iconfont-form icon-users oa-input-select'></i><i class='iconfont icon-tianjia1 oa-input-select-2x'></i></div>";
        if (fieldValue != null && fieldValue.contains(";")) {
            String[] names = fieldValue.split(";");
            for (int i = 0; i < names.length; i++) {
                String name = ToNameUtil.getName("user", names[i]);
                fieldHtml += "<span value='" + names[i] + "'> <label>" + name + "</label>" +
                        "<i onClick='event.cancelBubble = true;removeval(this)' class='iconfont icon-shanchu1'></i></span>" + StringHelper.Space("k0");
            }
        }
        fieldHtml += "</div>";
        fieldHtml += "<input type='hidden' id='" + val + "' name='" + val + "' ";
        fieldHtml += fieldVerify(required);
        fieldHtml += "autocomplete='off' value='" + fieldValue + "' class='layui-input' maxlength='" + fieldNum + "'>";
        fieldHtml += "</div>";
        fieldHtml += "<p class='layui-form-mid layui-word-aux' id='" + id + "_Explain'>" + show + "</p></div>";
        return fieldHtml;
    }

    /**
     * 部门选择（单）
     */
    public static String fieldDept(String tableId, FieldData fieldData) {
        return fieldDept(tableId, fieldData, null);
    }

    public static String fieldDept(String tableId, FieldData fieldData, String fieldValue) {
        String special = fieldData.getSpecial();
        Hashtable ht = getSpecial(special);
        if(fieldValue !=null && !fieldValue.equals("")) {
            fieldValue = valueOf(fieldValue);
        }else{
            fieldValue = (String)ht.get("input");
            fieldValue = fieldValue==null?"":fieldValue;
        }
        String fieldName = fieldData.getFieldName();
        String fieldTitle = fieldData.getFieldTitle();
        int fieldNum = fieldData.getFieldNum();
        String id = tableId + "_" + fieldName;
        String val = id + "_Value";
        String text = id + "_Name";
        String show = "";
        int required = fieldData.getRequired();

        String fieldHtml = "";
        fieldHtml += "<div class='layui-form-item component' data-method='offset'>";
        fieldHtml += "<label class='layui-form-label title' id='" + id + "_Title' data-type='dept'>" + fieldTitle + "</label>";
        if (required == 1) {
            fieldHtml += "<span class='layui-form-mid required' style='display:block;'>*</span>";
        } else {
            fieldHtml += "<span class='layui-form-mid required' style='display:none;'>*</span>";
        }
        fieldHtml += "<div class='layui-input-block'>";
        fieldHtml += "<div tabindex='0' id='" + id + "' name='" + id + "' class='layui-input select-input' onClick=\"selectdept(this,'dept')\">";
        fieldHtml += "<div class='oa-fa oa-fa-lg oa-fa-input'><i class='iconfont iconfont-form icon-xuanbumen1 oa-input-select'></i><i class='iconfont icon-tianjia1 oa-input-select-2x'></i></div>";
        if (fieldValue != null && fieldValue != "") {
            if (fieldValue != null && fieldValue.contains(";")) {
                fieldValue = fieldValue.substring(0, fieldValue.length() - 1);
            }
            String name = ToNameUtil.getName("dept", fieldValue);
            fieldHtml += "<span value='" + fieldValue + "' style='background-color: #009688;color: #FFF;'> <label>" + name + "</label>" +
                    "<i onClick='event.cancelBubble = true;removeval(this)' class='iconfont icon-shanchu1'></i></span>";
        }
        fieldHtml += "</div>";
        fieldHtml += "<input type='hidden' id='" + val + "' name='" + val + "' ";
        fieldHtml += fieldVerify(required);
        fieldHtml += "autocomplete='off' value='" + fieldValue + "' class='layui-input' maxlength='" + fieldNum + "'>";
        fieldHtml += "</div>";
        fieldHtml += "<p class='layui-form-mid layui-word-aux' id='" + id + "_Explain'>" + show + "</p></div>";
        return fieldHtml;
    }

    /**
     * 部门选择（多）
     */
    public static String fieldDepts(String tableId, FieldData fieldData) {
        return fieldDepts(tableId, fieldData, null);
    }

    public static String fieldDepts(String tableId, FieldData fieldData, String fieldValue) {
        String special = fieldData.getSpecial();
        Hashtable ht = getSpecial(special);
        if(fieldValue !=null && !fieldValue.equals("")) {
            fieldValue = valueOf(fieldValue);
        }else{
            fieldValue = (String)ht.get("input");
            fieldValue = fieldValue==null?"":fieldValue;
        }
        String fieldName = fieldData.getFieldName();
        String fieldTitle = fieldData.getFieldTitle();
        int fieldNum = fieldData.getFieldNum();
        String id = tableId + "_" + fieldName;
        String val = id + "_Value";
        String text = id + "_Name";
        String show = "";
        int required = fieldData.getRequired();

        String fieldHtml = "";
        fieldHtml += "<div class='layui-form-item component' data-method='offset'>";
        fieldHtml += "<label class='layui-form-label title' id='" + id + "_Title' data-type='depts'>" + fieldTitle + "</label>";
        if (required == 1) {
            fieldHtml += "<span class='layui-form-mid required' style='display:block;'>*</span>";
        } else {
            fieldHtml += "<span class='layui-form-mid required' style='display:none;'>*</span>";
        }
        fieldHtml += "<div class='layui-input-block'>";
        fieldHtml += "<div tabindex='0' id='" + id + "' name='" + id + "' class='layui-input select-input' onClick=\"selectdept(this,'depts')\">";
        fieldHtml += "<div class='oa-fa oa-fa-lg oa-fa-input'><i class='iconfont iconfont-form icon-xuanduobumen1 oa-input-select'></i><i class='iconfont icon-tianjia1 oa-input-select-2x'></i></div>";
        if (fieldValue != null && fieldValue.contains(";")) {
            String[] depts = fieldValue.split(";");
            for (int i = 0; i < depts.length; i++) {
                fieldHtml += "<span value='" + depts[i] + "' style='background-color: #009688;color: #FFF;'>" +
                        "<label>" + ToNameUtil.getName("dept", depts[i]) + "</label>" +
                        "<i onClick='event.cancelBubble = true;removeval(this)' class='iconfont icon-shanchu1'></i></span>" + StringHelper.Space("k0");
            }
        }
        fieldHtml += "</div>";
        fieldHtml += "<input type='hidden' id='" + val + "' name='" + val + "' ";
        fieldHtml += fieldVerify(required);
        fieldHtml += "autocomplete='off' value='" + fieldValue + "' class='layui-input' maxlength='" + fieldNum + "'>";
        fieldHtml += "</div>";
        fieldHtml += "<p class='layui-form-mid layui-word-aux' id='" + id + "_Explain'>" + show + "</p></div>";
        return fieldHtml;
    }

    /**
     * 表单携带（单）
     */
    public static String fieldForm(String tableId, FieldData fieldData) {
        return fieldForm(tableId, fieldData, null);
    }

    public static String fieldForm(String tableId, FieldData fieldData, String fieldValue) {
        String special = fieldData.getSpecial();
        Hashtable ht = getSpecial(special);
        String taskName = (String) ht.get("carryform");
        fieldValue = valueOf(fieldValue);
        String fieldName = fieldData.getFieldName();
        String fieldTitle = fieldData.getFieldTitle();
        int fieldNum = fieldData.getFieldNum();
        String id = tableId + "_" + fieldName;
        String val = id + "_Value";
        String text = id + "_Name";
        String show = "";
        int required = fieldData.getRequired();
        String fieldHtml = "";
        fieldHtml += "<div class='layui-form-item component' data-method='offset'>";
        fieldHtml += "<label class='layui-form-label title' id='" + id + "_Title' data-type='form'>" + fieldTitle + "</label>";
        if (required == 1) {
            fieldHtml += "<span class='layui-form-mid required' style='display:block;'>*</span>";
        } else {
            fieldHtml += "<span class='layui-form-mid required' style='display:none;'>*</span>";
        }
        fieldHtml += "<div class='layui-input-block'>";
        fieldHtml += "<div id='" + id + "' name='" + id + "' class='layui-input select-input' onClick=\"selectform(this,'form','" + taskName + "')\">";
        fieldHtml += "<div class='oa-fa oa-fa-lg oa-fa-input'><i class='iconfont iconfont-form icon-biaodan2 oa-input-select'></i><i class='iconfont icon-tianjia1 oa-input-select-2x'></i></div>";

        if (fieldValue != null && fieldValue != "") {
            if (fieldValue != null && fieldValue.contains(";")) {
                fieldValue = fieldValue.substring(0, fieldValue.length() - 1);
            }
            String name = FormDict.getName(taskName + "_" + fieldValue);
            fieldHtml += "<span value='" + fieldValue + "' style='background-color: #009688;color: #FFF;'> <label>" + name + "</label>" +
                    "<i onClick='event.cancelBubble = true;removeval(this)' class='iconfont icon-shanchu1'></i></span>";
        }
        fieldHtml += "</div>";
        fieldHtml += "<input type='hidden' maxlength='" + fieldNum + "' id='" + val + "' name='" + val + "' autocomplete='off' class='layui-input'";
        fieldHtml += fieldVerify(required);
        fieldHtml += " value='" + fieldValue + "'>";
        fieldHtml += "</div>";
        fieldHtml += "<p class='layui-form-mid layui-word-aux' id='" + id + "_Explain'>" + show + "</p></div>";
        return fieldHtml;
    }

    /**
     * 表单携带（多）
     */
    public static String fieldForms(String tableId, FieldData fieldData) {
        return fieldForms(tableId, fieldData, null);
    }

    public static String fieldForms(String tableId, FieldData fieldData, String fieldValue) {
        String special = fieldData.getSpecial();
        Hashtable ht = getSpecial(special);
        String taskName = (String) ht.get("carryform");
        fieldValue = valueOf(fieldValue);
        String fieldName = fieldData.getFieldName();
        String fieldTitle = fieldData.getFieldTitle();
        int fieldNum = fieldData.getFieldNum();
        String id = tableId + "_" + fieldName;
        String val = id + "_Value";
        String text = id + "_Name";
        String show = "";
        int required = fieldData.getRequired();
        String fieldHtml = "";
        fieldHtml += "<div class='layui-form-item component' data-method='offset'>";
        fieldHtml += "<label class='layui-form-label title' id='" + id + "_Title' data-type='forms'>" + fieldTitle + "</label>";
        if (required == 1) {
            fieldHtml += "<span class='layui-form-mid required' style='display:block;'>*</span>";
        } else {
            fieldHtml += "<span class='layui-form-mid required' style='display:none;'>*</span>";
        }
        fieldHtml += "<div class='layui-input-block'>";
        fieldHtml += "<div id='" + id + "' name='" + id + "' class='layui-input select-input' onClick=\"selectform(this,'forms','" + taskName + "')\">";
        fieldHtml += "<div class='oa-fa oa-fa-lg oa-fa-input'><i class='iconfont iconfont-form icon-biaodan1 oa-input-select'></i><i class='iconfont icon-tianjia1 oa-input-select-2x'></i></div>";
        if (fieldValue != null && fieldValue.indexOf(";") >= 0) {
            String[] forms = fieldValue.split(";");
            for (int i = 0; i < forms.length; i++) {
                String name = FormDict.getName(taskName + "_" + forms[i]);
                fieldHtml += "<span value='" + forms[i] + "' style='background-color: #009688;color: #FFF;'> <label>" + name + "</label>" +
                        "<i onClick='event.cancelBubble = true;removeval(this)' class='iconfont icon-shanchu1'></i></span>" + StringHelper.Space("k0");
            }
        }
        fieldHtml += "</div>";
        fieldHtml += "<input type='hidden' maxlength='" + fieldNum + "' id='" + val + "' name='" + val + "' autocomplete='off' class='layui-input' ";
        fieldHtml += fieldVerify(required);
        fieldHtml += " value='" + fieldValue + "'>";
        fieldHtml += "</div>";
        fieldHtml += "<p class='layui-form-mid layui-word-aux' id='" + id + "_Explain'>" + show + "</p></div>";
        return fieldHtml;
    }

    /**
     * 附件上传（单）
     */
    public static String fieldUpload(String tableId, FieldData fieldData) {
        return fieldUpload(tableId, fieldData, null);
    }

    public static String fieldUpload(String tableId, FieldData fieldData, String fieldValue) {
        String special = fieldData.getSpecial();
        Hashtable ht = getSpecial(special);
        fieldValue = valueOf(fieldValue);
        String fieldName = fieldData.getFieldName();
        String fieldTitle = fieldData.getFieldTitle();
        int fieldNum = fieldData.getFieldNum();
        String id = tableId + "_" + fieldName;
        String val = id + "_Value";
        String text = id + "_Name";
        String show = "";
        int required = fieldData.getRequired();

        String fieldHtml = "";
        fieldHtml += "<div class='layui-upload upload component'>";
        File file = new File(fieldValue);
        fieldHtml += "<button type='button' id='" + id + "' class='layui-btn title' data-type='upload'>" + fieldTitle + "</button>" + StringHelper.Space("k1");
        if (required == 1) {
            fieldHtml += "<span class='layui-form-mid required' style='display:block;'>*</span>";
        } else {
            fieldHtml += "<span class='layui-form-mid required' style='display:none;'>*</span>";
        }
        String fileName = file.getName();
        fileName = fileName.substring(fileName.indexOf("-") + 1);
        fieldHtml += StringHelper.Space("k1") + "<a href='/" + fieldValue + "' data-value='" + fieldValue + "' download='" + fileName + "' title='点击下载 " + fileName + "'>" + fileName + "</a>";
        fieldHtml += "<div class='layui-upload-list'>";
        fieldHtml += "<input type='hidden' class='form-upload-ins' id='" + val + "' name='" + val + "' value='" + fieldValue + "' maxlength='" + fieldNum + "'>";
        fieldHtml += "<p class='upload-text'></p></div>";
        fieldHtml += "<p class='layui-form-mid layui-word-aux' id='" + id + "_Explain'>" + show + "</p></div>";
        return fieldHtml;
    }

    /**
     * 附件上传（多）
     */
    public static String fieldUploads(String tableId, FieldData fieldData) {
        return fieldUploads(tableId, fieldData, null);
    }

    public static String fieldUploads(String tableId, FieldData fieldData, String fieldValue) {
        String special = fieldData.getSpecial();
        Hashtable ht = getSpecial(special);
        fieldValue = valueOf(fieldValue);
        String fieldName = fieldData.getFieldName();
        String fieldTitle = fieldData.getFieldTitle();
        int fieldNum = fieldData.getFieldNum();
        String id = tableId + "_" + fieldName;
        String val = id + "_Value";
        String text = id + "_Name";
        String show = "";
        int required = fieldData.getRequired();

        String fieldHtml = "";

        fieldHtml += "<div class='layui-upload uploads component'>";
        fieldHtml += "<button type='button' id='" + id + "' class='layui-btn layui-btn-normal title uploadList' data-type='uploads'>" + fieldTitle + "</button>";
        if (required == 1) {
            fieldHtml += "<span class='layui-form-mid required' style='display:block;'>*</span>";
        } else {
            fieldHtml += "<span class='layui-form-mid required' style='display:none;'>*</span>";
        }
        fieldHtml += "<div class='layui-upload-list' id='" + id + "_Upload'>";
        fieldHtml += "<table class='layui-table'> <thead><tr><th>文件名</th><th>大小</th><th>状态</th><th>操作</th></tr></thead>";
        fieldHtml += "<tbody class='uploadFildList'>";
        if (fieldValue != null && fieldValue.contains("|")) {
            String[] files = fieldValue.split("\\|");
            for (int i = 0; i < files.length; i++) {
                File file = new File(files[0]);
                String fileName = file.getName();
                fileName = fileName.substring(fileName.indexOf("-") + 1);
                fieldHtml += "<tr id='upload-" + i + "'><td>";
                fieldHtml += "<a href='/" + files[0] + "' data-value='" + files[0] + "' download='" + fileName + "' title='点击下载 " + fileName + "'>" + fileName + "</a></td>";
                fieldHtml += "<td>" + FileHelper.getPrintSize(file.length()) + "</td>";
                fieldHtml += "<td></td><td><a class='layui-btn layui-btn-xs layui-btn-danger upload-delete' herf='javascript:void(0)' onclick=\"removefile('upload-" + i + "','" + id + "')\">删除</a></td></tr>";
            }
        }
        fieldHtml += "</tbody></table>";
        fieldHtml += "<input type='hidden' class='form-upload-ins' id='" + val + "' name='" + val + "' value='" + fieldValue + "' maxlength='" + fieldNum + "'>";
        fieldHtml += "</div>";
        fieldHtml += "<p class='layui-form-mid layui-word-aux' id='" + id + "_Explain'>" + show + "</p></div>";
        return fieldHtml;
    }

    /**
     * 富文本
     */
    public static String fieldEditor(String tableId, FieldData fieldData, String fieldValue) {
        String special = fieldData.getSpecial();
        Hashtable ht = getSpecial(special);
        fieldValue = valueOf(fieldValue);
        String fieldName = fieldData.getFieldName();
        String fieldTitle = fieldData.getFieldTitle();
        int fieldNum = fieldData.getFieldNum();
        String id = tableId + "_" + fieldName;
        String show = "";
        int required = fieldData.getRequired();

        String fieldHtml = "";
        fieldHtml += "<div class='layui-form-item component' data-method='offset'>";
        fieldHtml += "<label class='layui-form-label title' id='" + id + "_Title' data-type='textarea'>" + fieldTitle + "</label>";
        if (required == 1) {
            fieldHtml += "<span class='layui-form-mid required' style='display:block;'>*</span>";
        } else {
            fieldHtml += "<span class='layui-form-mid required' style='display:none;'>*</span>";
        }
        fieldHtml += "<div class='layui-input-block'>";
        fieldHtml += " <div class='textarea editor' id='" + id + "'></div>";
        fieldHtml += "</div>";
        fieldHtml += "<textarea id='" + id + "_Value' name='" + id + "_Value' maxlength='" + fieldNum + "'";
        fieldHtml += fieldVerify(required);
        fieldHtml += " class='layui-textarea' style='display: none;'>" + fieldValue + "</textarea>";
        fieldHtml += "</div>";
        return fieldHtml;
    }
//    三级联动

    public static String fieldSelectCity(String tableId, FieldData fieldData, String fieldValue) {
        String special = fieldData.getSpecial();
        Hashtable ht = getSpecial(special);
        fieldValue = valueOf(fieldValue);
        String fieldName = fieldData.getFieldName();
        String fieldTitle = fieldData.getFieldTitle();
        int fieldNum = fieldData.getFieldNum();
        String id = tableId + "_" + fieldName;
        String show = "";
        int required = fieldData.getRequired();

        String fieldHtml = "";
        fieldHtml += "<div class='layui-form-item component' data-method='offset'>";
        fieldHtml += "<label class='layui-form-label title' id='" + id + "_Title' data-type='selectcity'>" + fieldTitle + "</label>";

        if (required == 1) {
            fieldHtml += "<span class='layui-form-mid required' style='display:block;'>*</span>";
        } else {
            fieldHtml += "<span class='layui-form-mid required' style='display:none;'>*</span>";
        }
        fieldHtml += "<div class='layui-upload-list'>";
        fieldHtml += " <div class='layui-col-md3'>";
        fieldHtml += "<select class='ap' lay-filter='ap' id='ap' name='province'></select>";
        fieldHtml +="</div>";

        fieldHtml += " <div class='layui-col-md3'>";
        fieldHtml += "<select class='ac' lay-filter='ac' id='ac' name='city'></select>";
        fieldHtml +="</div>";

        fieldHtml += " <div class='layui-col-md3'>";
        fieldHtml += "<select class='aa' lay-filter='aa' id='aa' name='area'></select>";
        fieldHtml +="</div>";
        fieldHtml +="</div>";
        fieldHtml += "<p class='layui-form-mid layui-word-aux' id='" + id + "_Explain'>" + show + "</p>";
        fieldHtml +="<input type='hidden' class='form-upload-ins' id='" + id + "_Value' name='" + id + "_Value' maxlength='"+fieldNum+"' value='" + fieldValue + "'>";
        fieldHtml += "</div>";
        return fieldHtml;
    }
    /**
     * 子表
     */
    public static String fieldChild(String tableId, FieldData fieldData, String fieldValue) {
        String special = fieldData.getSpecial();
        Hashtable ht = getSpecial(special);
        String fieldName = fieldData.getFieldName();
        String fieldTitle = fieldData.getFieldTitle();
        String id = tableId + "_" + fieldName;
        String url = "/userpage/viewpage/childtable.do?formid=" + ht.get("formcmName") + "&linkId=" + fieldValue;
        String fieldHtml = "<div class='layui-form-item layui-form-text'><label class='layui-form-label'>" + fieldTitle + "</label><div class='layui-textarea'>";
        fieldHtml += "<iframe id='" + id + "' name='" + id + "' src='" + url + "' frameborder='no' style='overflow: visible;' width='100%' height='300px'></iframe></div></div>";
        return fieldHtml;
    }

    /**
     * 处理Info状态下的表单
     */
    public static String fieldInfo(String tableId, FieldData fieldData, Object fieldValue, String type) {
        String fieldName = fieldData.getFieldName();
        String fieldTitle = fieldData.getFieldTitle();
        String id = tableId + "_" + fieldName;
        String fieldHtml = "<div class='layui-form-item'><label class='layui-form-label'>" + fieldTitle + ":</label>";
        fieldHtml += "<div class='layui-input-block'>";
        String value = fieldInfo(fieldData, fieldValue, type);
        if ("upload".equals(type) || "uploads".equals(type)) {
            fieldHtml += "<label id="+id+">"+value+"</label>";
        } else {
            fieldHtml += "<input type='text' id='" + id + "' name='" + id + "' autocomplete='off' class='layui-input' value='" + value + "' readonly unselectable='on'>";
        }
        fieldHtml += "</div></div>";
        return fieldHtml;
    }

    /**
     * 处理Info状态下的表单数据
     */
    public static String fieldInfo(FieldData fieldData, Object fieldValue, String type) {
        String fieldName = fieldData.getFieldName();
        String value = "";
        if (type != null && fieldValue != null) {
            if ("date".equals(type)) {
                value = DateHelper.getSqlDate(fieldValue + "");
            } else if ("datetime".equals(type)) {
                value = DateHelper.getSqlDateTime(fieldValue + "");
            } else if ("int".equals(type)) {
                value = String.valueOf(fieldValue);
            } else if ("decimal".equals(type)) {
                value = String.valueOf(fieldValue);
            } else if ("select".equals(type) || "selects".equals(type)) {
                value = DDUtil.selectIdToName(fieldName, (String) fieldValue);
            } else if ("user".equals(type)) {
                String fv = (String) fieldValue;
                if (fv.indexOf(";") >= 0) {
                    fv = fv.substring(0, fv.length() - 1);
                }
                value = UserDict.getName(fv);
            } else if ("users".equals(type)) {
                value = UserDict.getNames((String) fieldValue, ";");
            } else if ("dept".equals(type)) {
                String fv = (String) fieldValue;
                if (fv.indexOf(";") >= 0) {
                    fv = fv.substring(0, fv.length() - 1);
                }
                value = ToNameUtil.getName("dept", fv);
            } else if ("depts".equals(type)) {
                value = ToNameUtil.getName("depts", (String) fieldValue);
            } else if ("form".equals(type)) {
                String fv = (String) fieldValue;
                if (fv.indexOf(";") >= 0) {
                    fv = fv.substring(0, fv.length() - 1);
                }
                JSONObject jsForm = new JSONObject();
                jsForm.put("special", fieldData.getSpecial());
                jsForm.put("value", (String) fv);
                value = ToNameUtil.getName("form", jsForm);
            } else if ("forms".equals(type)) {
                JSONObject jsForms = new JSONObject();
                jsForms.put("special", fieldData.getSpecial());
                jsForms.put("value", (String) fieldValue);
                value = ToNameUtil.getName("forms", jsForms);
            } else if ("upload".equals(type)) {
                String val = (String) fieldValue;
                if (val != null && !val.equals("")) {
                    File file = new File((String) fieldValue);
                    String ext = ext(file.getName());
                    value = "<a href='/" + value + "' download='" + file.getName() + "' title='点击下载 " + file.getName() + "'>";
                    value += "<div class='oa-file'>";
                    value += "<i class='layui-icon layui-icon-file'></i>";
                    value += "<p>" + ext + "</p>";
                    value += "</div></a>";
                }
            } else if ("uploads".equals(type)) {
                String val = (String) fieldValue;
                if (val != null && !val.equals("")) {
                    String[] files = val.split("\\|");
                    for (int i = 0; i < files.length; i++) {
                        File file = new File(files[i]);
                        String ext = ext(file.getName());
                        value += "<a href='/" + files[i] + "' download='" + file.getName() + "' title='点击下载 " + file.getName() + "'>";
                        value += "<div class='oa-file'>";
                        value += "<i class='layui-icon layui-icon-file'></i>";
                        value += "<p>" + ext + "</p>";
                        value += "</div></a>";
                    }
                }
            } else if ("textarea".equals(type)) {
                value = (String) fieldValue;
            } else {
                value = (String) fieldValue;
            }
        }
        return value;
    }

    /**
     * 字段特殊定义处理
     */
    public static Hashtable getSpecial(String special) {
        Hashtable val = new Hashtable();
        if (special != null) {
            try {
                HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
                List<String> speList = StringHelper.getMsg(special);
                if (speList != null && speList.size() > 0) {
                    for (String sp : speList) {
                        if (sp.equals("loginName")) {
                            Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
                            val.put("input", loginer.getId());
                        } else if (sp.equals("currentDate")) {
                            val.put("input", DateHelper.getDate());
                        } else if (sp.equals("currentDateTime")) {
                            val.put("input", DateHelper.now());
                        } else if (sp.equals("currentDept")) {
                            Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
                            String loginId = loginer.getId();
                            val.put("input", DeptDict.getUserDept(loginId));
                        } else if (sp.contains("carryform-")) {
                            val.put("carryform", sp.split("-")[1]);
                        } else if (sp.contains("laterTo-")) {
                            val.put("laterTo", sp.split("-")[1]);
                        } else if (sp.contains("toLater-")) {
                            val.put("toLater", sp.split("-")[1]);
                        } else if (sp.contains("dateStar")) {
                            ConfParseUtil cp = new ConfParseUtil();
                            val.put("dateStar", DateHelper.getDate() + " " + cp.getSchedule("amtowork"));
                        } else if (sp.contains("dateEnd")) {
                            ConfParseUtil cp = new ConfParseUtil();
                            val.put("dateEnd", DateHelper.getDate() + " " + cp.getSchedule("pmoffwork"));
                        } else if (sp.contains("child-")) {
                            val.put("formcmName", sp.split("-")[1]);
                        } else if (sp.contains("carrydept-")) {
                            val.put("carrydept", sp.split("-")[1]);
                        } else if (sp.contains("count-")) {
                            val.put("count", sp.split("-")[1]);
                        } else if (sp.contains("user-")) {
                            val.put("user", sp.split("-")[1]);
                        }
                    }
                }
            } catch (Exception e) {
                LogUtil.sysLog("Exception:" + e);
                e.printStackTrace();
            }
        }
        return val;
    }


    /**
     * 表字段选择处理
     */
    public static String getContextKey(String pageId) {
        String option = "";
        if (pageId != null && pageId.equals("Z2018101500002")) {
            option += "<option value='userName'>员工账号</option>";
        } else if (pageId != null && pageId.equals("Z2018101500003")) {
            option += "<option value='deptId'>部门编号</option>";
        } else {
            MyUrlRegistService m = (MyUrlRegistService) SpringContextUtil.getBean("myUrlRegistService");
            MyUrlRegist myurl = m.selectById(pageId);
            String formId = myurl.getFormId();
            FormCustomMadeService fc = (FormCustomMadeService) SpringContextUtil.getBean("formCustomMadeService");
            FormCustomMade form = fc.selectFormCMByID(formId);
            String formTask = form.getFormTask();
            DictionaryService d = (DictionaryService) SpringContextUtil.getBean("dictionaryService");
            TaskData task = d.selectTaskName(formTask);
            String fields = task.getTaskField();
            Vector<String> field = StringHelper.string2Vector(fields, ";");
            Hashtable<String, String> ht = InitDataListener.getMapData("fieldData");
            option = "<option value='recorderNO'>主键</option>";
            for (String f : field) {
                if (ht != null) {
                    option += "<option value='" + f + "'>" + ht.get(f) + "</option>\n";
                } else {
                    option += "<option value='" + f + "'>" + f + "</option>\n";
                }
            }
        }
        return option;
    }

    public static boolean isNull(Object val) {
        boolean b = true;
        if (val instanceof Integer) {
            int value = ((Integer) val).intValue();
            if (value != 0) {
                b = false;
            }
        } else if (val instanceof String) {
            String s = (String) val;
            if (s != null && !s.equals("") && !s.equals("null")) {
                b = false;
            }
        } else if (val instanceof BigDecimal) {
            BigDecimal bd = ((BigDecimal) val);
            if (bd != null) {
                b = false;
            }
        } else if (val instanceof Double) {
            double d = ((Double) val).doubleValue();
            if (d != 0) {
                b = false;
            }
        } else if (val instanceof Float) {
            float f = ((Float) val).floatValue();
            if (f != 0) {
                b = false;
            }
        } else if (val instanceof Long) {
            long l = ((Long) val).longValue();
            if (l != 0) {
                b = false;
            }
        } else if (val instanceof Date) {
            Date d = (Date) val;
            if (d != null) {
                b = false;
            }
        } else if (val instanceof Timestamp) {
            Timestamp t = (Timestamp) val;
            if (t != null) {
                b = false;
            }
        }
        return b;
    }

    public static String valueOf(Object obj) {
        return (obj == null) ? "" : obj.toString();
    }

    /**
     * 获取文件扩展名
     *
     * @return
     */
    public static String ext(String filename) {
        int index = filename.lastIndexOf(".");

        if (index == -1) {
            return "";
        }
        String result = filename.substring(index + 1);
        return result.toUpperCase();
    }
}