package com.oa.core.util;

import com.oa.core.bean.Loginer;
import com.oa.core.helper.DateHelper;
import com.oa.core.helper.StringHelper;
import com.oa.core.listener.InitDataListener;
import com.oa.core.service.system.FormCustomMadeService;
import com.oa.core.service.system.MyUrlRegistService;
import com.oa.core.service.util.TableService;
import com.oa.core.bean.dd.FieldData;
import com.oa.core.bean.dd.TaskData;
import com.oa.core.service.dd.DictionaryService;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

/**
 * @ClassName:FinanceController
 * @author:zxd
 * @Date:2018/12/05
 * @Time:下午 4:19
 * @Version V1.0
 * @Explain 页面生成
 */

public class StringToHtmlUtil {

    private DictionaryService d = (DictionaryService) SpringContextUtil.getBean("dictionaryService");
    private FormCustomMadeService f = (FormCustomMadeService) SpringContextUtil.getBean("formCustomMadeService");

    public String getListTitle(String formid) {
        return getListTitle(formid, false);
    }

    public String getListTitle(String formid, boolean finance) {
        ConfParseUtil cputil = new ConfParseUtil();
        boolean record = Boolean.valueOf(cputil.getPoa("record"));
        boolean modify = Boolean.valueOf(cputil.getPoa("modify"));
        try {
            if (formid != null) {
                TaskData taskData = d.listTaskByFormId(formid);
                String tableId = taskData.getTableName();
                String field = taskData.getTaskField();
                String listHtml = "<th lay-data=\"{type:'checkbox', fixed: 'left',align:'center',field:'" + tableId + "_recorderNO'}\"></th>";
                listHtml += "<th lay-data=\"{field:'" + tableId + "_num',fixed: 'left',align:'center', unresize:true,width:80, templet: '#tableNum'}\">序号</th>";
                String fields[] = field.split(";");
                String edit = "";
                if (finance) {
                    edit = ", edit: 'text'";
                }
                for (int i = 0; i < fields.length; i++) {
                    FieldData fieldData = d.selectFieldName(fields[i]);
                    String fieldTpye = fieldData.getFieldType();
                    String name = tableId + "_" + fields[i];
                    String special = fieldData.getSpecial();
                    String templet = getTemplet(fields, i, fieldTpye, name, special);
                    if (fieldTpye != null && fieldTpye.equals("editor")) {
                        String dname = tableId + "_" + fields[i];
                        templet = ",templet: function(d){ return oa.editor('" + dname + "',d);}";
                        listHtml += "<th lay-data=\"{align:'center',unresize:true, sort: true " + templet + "}\">" + fieldData.getFieldTitle() + "</th>";
                    } else if (fieldTpye != null && (fieldTpye.equals("int") || fieldTpye.equals("decimal") || fieldTpye.equals("text"))) {
                        if(finance){
                            //edit = ",templet: function (d){ return textTool(d,'"+tableId + "_" + fields[i]+"','"+fieldTpye+"');}";
                        }
                        listHtml += "<th lay-data=\"{field:'" + tableId + "_" + fields[i] + "', unresize:true " + edit + "}\">" + fieldData.getFieldTitle() + "</th>";
                    } else if (fieldTpye != null && (fieldTpye.equals("select") || fieldTpye.equals("selects"))) {
                        if(finance){
                            templet = ",templet: function (d){ return selectTool(d,'"+tableId + "_" + fields[i]+"','"+fieldTpye+"');}";
                        }
                        listHtml += "<th lay-data=\"{field:'" + tableId + "_" + fields[i] + "', unresize:true " + templet + "}\">" + fieldData.getFieldTitle() + "</th>";
                    } else {
                        listHtml += "<th lay-data=\"{field:'" + tableId + "_" + fields[i] + "', unresize:true, sort: true " + templet + "}\">" + fieldData.getFieldTitle() + "</th>";
                    }
                }
                if (record) {
                    listHtml += "<th lay-data=\"{field:'" + tableId + "_recordName_name', unresize:true, sort: true}\">创建人</th>";
                    listHtml += "<th lay-data=\"{field:'" + tableId + "_recordTime', unresize:true, sort: true, templet:function(d){return oa.formatdate('datetime',d." + tableId + "_recordTime);}}\">创建时间</th>";
                }
                if (modify) {
                    listHtml += "<th lay-data=\"{field:'" + tableId + "_modifyName_name', unresize:true, sort: true}\">修改人</th>";
                    listHtml += "<th lay-data=\"{field:'" + tableId + "_modifyTime', unresize:true, sort: true, templet:function(d){return oa.formatdate('datetime',d." + tableId + "_modifyTime);}}\">修改时间</th>";
                }
                return listHtml;
            } else {
                return "请先设置菜单1";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getTemplet(String[] fields, int i, String fieldTpye, String name, String special) {
        String templet = ",templet:function(d){return oa.decipher('" + fieldTpye + "',d." + name + ");}";
        if (fieldTpye != null && (fieldTpye.equals("form") || fieldTpye.equals("forms"))) {
            templet = ",templet:function(d){return oa.decipher('" + fieldTpye + "','" + special + "',d." + name + ");}";
        } else if (fieldTpye != null && (fieldTpye.equals("select") || fieldTpye.equals("selects"))) {
            templet = ",templet:function(d){return oa.decipher('" + fieldTpye + "','" + fields[i] + "',d." + name + ");}";
        } else if (fieldTpye != null && (fieldTpye.equals("date") || fieldTpye.equals("datetime"))) {
            templet = ",templet:function(d){return oa.formatdate('" + fieldTpye + "',d." + name + ");}";
        }
        return templet;
    }

    public String getListSearch(String tableId) {
        TaskData task = new TaskData();
        task.setTableName(tableId);
        task.setTaskType("查询");
        List<TaskData> tl = d.selectTaskList(task);
        if (tl == null || tl.size() <= 0) {
            task.setTaskType("列表");
            tl = d.selectTaskList(task);
        }
        String html = "";
        if (tl != null && tl.size() > 0) {
            task = tl.get(0);
            Vector<String> fields = StringHelper.string2Vector(task.getTaskField(), ";");
            for (String field : fields) {
                Hashtable<String, String> ht = InitDataListener.getMapData("fieldData");
                String name = ht.get(field);
                html += "<option value='" + field + "'>" + name + "</option>";
            }
        }
        return html;
    }

    public String getListValue(String pageid) {
        try {
            return getListValue(pageid, null);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public String getListValue(String pageid, Vector<String> wheres) {
        MyUrlRegistService m = (MyUrlRegistService) SpringContextUtil.getBean("myUrlRegistService");
        try {
            TaskData taskData = d.listTaskByPageId(pageid);
            String tableId = taskData.getTableName();
            String field = taskData.getTaskField();
            TableService t = (TableService) SpringContextUtil.getBean("tableService");
            String where = "";
            if (wheres != null) {
                for (String w : wheres) {
                    where += " and " + w;
                }
            }
            String sql = "select recorderNO," + field.replaceAll(";", ",") + "recordName,recordTime,modifyName,modifyTime from " + tableId + " where curStatus=2 " + where;
            List<Map<String, Object>> sqlvalue = t.selectSqlMapList(sql);
            if (sqlvalue.size() > 0) {
                String fields[] = field.split(";");
                HashMap<String, String> hm = new HashMap();
                List<Map<String, Object>> fe = t.selectSqlMapList("select fieldName,fieldType from FieldData where fieldName in ('" + field.replaceAll(";", "','") + "') and curStatus=2");
                for (Map<String, Object> map : fe) {
                    hm.put((String) map.get("fieldName"), (String) map.get("fieldType"));
                }
                int num = 1;
                String html = "";
                for (Map<String, Object> map : sqlvalue) {
                    StringBuffer listHtml = new StringBuffer();
                    listHtml.append("<tr>");
                    listHtml.append("<td>" + map.get("recorderNO") + "</td>");
                    listHtml.append("<td><a class=\"layui-btn layui-btn-sm\" style=\"text-decoration:none;\" href=\"/userpage/pageinfo/" + map.get("recorderNO") + ".do?pageid=" + pageid + "\">" + num + "</a></td>");
                    for (int n = 0; n < fields.length; n++) {
                        String type = hm.get(fields[n]);
                        listHtml.append("<td>" + ToNameUtil.tableIdToName(fields[n], map.get(fields[n]), type) + "</td>");
                    }
                    listHtml.append("<td>" + ToNameUtil.tableIdToName("recordName", map.get("recordName"), "user") + "</td>");
                    listHtml.append("<td>" + ToNameUtil.tableIdToName("recordTime", map.get("recordTime"), "datetime") + "</td>");
                    listHtml.append("<td>" + ToNameUtil.tableIdToName("modifyName", map.get("modifyName"), "user") + "</td>");
                    listHtml.append("<td>" + ToNameUtil.tableIdToName("modifyTime", map.get("modifyTime"), "datetime") + "</td>");
                    listHtml.append("</tr>");
                    num++;
                    html += listHtml.toString() + "\n";
                }
                return html;
            } else {
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public String getListButton(String num) {
        return getListButton(num, false);
    }
    public String getListButton(String num, boolean listsave) {
        return getListButton(num, false,null);
    }

    public String getListButton(String num, boolean listsave,String formId) {
        String buttonHtml = "";
        if (AccessUtil.getAdd(num)) {
            buttonHtml += "<a class='layui-btn layui-btn-xs' lay-event='add' title='添加'>添加</a>"; //layui-btn-primary
        }
        if (AccessUtil.getModi(num)) {
            if (!listsave) {
                buttonHtml += "<a class='layui-btn layui-btn-xs' lay-event='update' title='修改'>修改</a>";
            } else {
                buttonHtml += "<a class='layui-btn layui-btn-xs' lay-event='save' title='保存'>保存</a>";
            }
        }
        if (AccessUtil.getDelete(num)) {
            buttonHtml += "<a class='layui-btn layui-btn-danger layui-btn-xs' lay-event='delete' title='删除'>删除</a>";
        }
        if (AccessUtil.getImport(num)) {
            buttonHtml += "<a data-method='titlebtn' class='layui-btn layui-btn-xs'  onclick=\"optExcel('" + formId + "')\">导入Excel</a>";
        }
        if (AccessUtil.getExport(num)) {
            buttonHtml += "<a data-method='titlebtn' class='layui-btn layui-btn-xs'  onclick=\"export1('" + formId + "')\">导出Excel</a>";
        }
        /*if(formId!=null && !formId.equals("")) {
            buttonHtml += "<a data-method='titlebtn' class='layui-btn layui-btn-xs'  onclick=\"optExcel('" + formId + "')\">导入Excel</a>";
            buttonHtml += "<a data-method='titlebtn' class='layui-btn layui-btn-xs'  onclick=\"export1('" + formId + "')\">导出Excel</a>";
        }*/
        return buttonHtml;
    }

    public FieldData getFieldData(String field) {
        FieldData fieldData = d.selectFieldName(field);
        if (fieldData == null) {
            fieldData = new FieldData();
            fieldData.setFieldName(field);
            fieldData.setFieldTitle("未名字段");
            fieldData.setFieldType("text");
            fieldData.setFieldNum(30);
            fieldData.setRequired(0);
            fieldData.setSpecial("");
        }
        return fieldData;
    }

    public static BigDecimal getBigDecimal(Object value) {
        BigDecimal ret = null;
        if (value != null && !value.equals("")) {
            if (value instanceof BigDecimal) {
                ret = (BigDecimal) value;
            } else if (value instanceof String) {
                ret = new BigDecimal((String) value);
            } else if (value instanceof BigInteger) {
                ret = new BigDecimal((BigInteger) value);
            } else if (value instanceof Number) {
                ret = new BigDecimal(((Number) value).doubleValue());
            } else {
                throw new ClassCastException("类型不匹配");
            }
        }
        return ret;
    }

    /**
     * 获取表单字段
     */
    public List<String> getFormHtml(HttpServletRequest request, String pageid, String formid, String type, TaskData taskData, String recorderNO) {
        return getFormHtml(request, pageid, formid, type, taskData, recorderNO, true);
    }

    public List<String> getFormHtml(HttpServletRequest request, String pageid, String formid, String type, TaskData taskData, String recorderNO, boolean button) {
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        String userId = loginer.getId();
        String tableId = taskData.getTableName();
        String taskField = taskData.getTaskField();
        Vector<String> fields = StringHelper.string2Vector(taskField, ";");
        Map<String, Object> sqlvalue = null;
        if (recorderNO != null && !recorderNO.equals("newformdata")) {
            List<String> where = new ArrayList<String>();
            where.add("recorderNO='" + recorderNO + "'");
            String sql = MySqlUtil.getSql(fields, tableId, where);
            TableService t = (TableService) SpringContextUtil.getBean("tableService");
            sqlvalue = t.selectSqlMap(sql);
        }
        //计算表单生成列数
        int column = 1;
        int num = 12;
        ConfParseUtil cp = new ConfParseUtil();
        int listNum = Integer.parseInt(cp.getPoa("form_list"));
        if ((fields.size() / listNum) == 1) {
            column = 2;
            num = 6;
        } else if ((fields.size() / listNum) > 1) {
            column = 3;
            num = 4;
        }
        List<String> htmlList = new ArrayList<>();
        if (!fields.contains("recorderNO")) {
            FieldData fieldData = getFieldData("recorderNO");
            String div = "<div class='layui-row' style='display:none'>";
            if ("info".equals(type)) {
                htmlList.add(div + getInfoField(sqlvalue, fieldData, tableId, 12) + "</div>");
            } else {
                htmlList.add(div + getSaveField(sqlvalue, fieldData, tableId, 12) + "</div>");
            }
        }

        List<FieldData> childbl = new ArrayList<>();
        int ci = 0;
        String divhtml = "";
        for (int i = 0, n = fields.size(); i < n; i++) {
            String field = fields.get(i);
            FieldData fieldData = getFieldData(field);
            String fieldType = fieldData.getFieldType();
            if (fieldType != null && fieldType.equals("child")) {
                childbl.add(fieldData);
            } else {
                if (ci == 0) {
                    divhtml = "<div class='layui-row layui-col-space40'>";
                }
                if ("info".equals(type)) {
                    divhtml += getInfoField(sqlvalue, fieldData, tableId, num);
                } else {
                    divhtml += getSaveField(sqlvalue, fieldData, tableId, num);
                }
                ci++;
                if (ci == column) {
                    divhtml += "</div>";
                    htmlList.add(divhtml);
                    ci = 0;
                }
                if (i == (n - 1) && column > 1) {
                    if (ci != column && ci!=0) {
                        switch (column - ci) {
                            case 2:
                                divhtml += "<div class='layui-col-xs4'> </div><div class='layui-col-xs4'> </div>";
                                break;
                            default:
                                divhtml += "<div class='layui-col-xs6'> </div>";
                        }
                        divhtml += "</div>";
                        htmlList.add(divhtml);
                    }
                }
            }
        }
        if (childbl != null && childbl.size() > 0) {
            String fieldValue = userId;
            if (sqlvalue != null && sqlvalue.size() > 0) {
                fieldValue = (String) sqlvalue.get("recorderNO");
            }
            for (FieldData fieldData : childbl) {
                String div = "<div class='layui-row'><div class='layui-col-xs12'>";
                htmlList.add(div + FieldTypeUtil.fieldChild(tableId, fieldData, fieldValue) + "</div></div>");
            }
        }
        if (button) {
            if ("info".equals(type)) {
                htmlList.add(getInfoButton(userId, pageid, formid, recorderNO));
            } else {
                htmlList.add(getSaveButton(pageid, formid));
            }
        }
        return htmlList;
    }

    public String getInfoField(Map<String, Object> sqlvalue, FieldData fieldData, String tableId, int num) {
        String field = fieldData.getFieldName();
        String type = fieldData.getFieldType();
        Object fieldValue = sqlvalue.get(field);
        String div = "<div class='layui-col-xs" + num + "'>";
        if ("recorderNO".equals(field)) {
            div = "<div class='layui-col-xs12'>";
        }
        String fieldHtml = div + FieldTypeUtil.fieldInfo(tableId, fieldData, fieldValue, type) + "</div>";
        return fieldHtml;
    }

    public String getSaveField(Map<String, Object> sqlvalue, FieldData fieldData, String tableId, int num) {
        String field = fieldData.getFieldName();
        String fieldHtml = "<div class='layui-col-xs" + num + "'>";
        if ("recorderNO".equals(field)) {
            fieldHtml = "<div class='layui-col-xs12' style='display:none'>";
        }
        try {
            String value = "";
            if (sqlvalue != null) {
                value = String.valueOf(sqlvalue.get(field));
            }
            String special = fieldData.getSpecial();
            if (FieldTypeUtil.isNull(value) && !FieldTypeUtil.isNull(special)) {
                Hashtable ht = FieldTypeUtil.getSpecial(special);
                String input = String.valueOf(ht.get("input"));
                if (!FieldTypeUtil.isNull(input)) {
                    value = input;
                }
            }
            fieldHtml += getFieldValue(tableId, fieldData, value);
        } catch (Exception e) {
            LogUtil.sysLog("Exception:" + e);
            LogUtil.sysLog("Exception:" + field);
            e.printStackTrace();
        }
        fieldHtml += "</div>";
        return fieldHtml;
    }

    public String getFieldValue(String tableId, String fieldName, String value) {
        return getFieldValue(tableId, getFieldData(fieldName), value);
    }

    public String getFieldValue(String tableId, FieldData fieldData, String value) {
        String type = fieldData.getFieldType();
        String fieldHtml;
        switch (type) {
            case "date":
                fieldHtml = FieldTypeUtil.fieldDate(tableId, fieldData, DateHelper.convert(value));
                break;
            case "datetime":
                fieldHtml = FieldTypeUtil.fieldDateTime(tableId, fieldData, DateHelper.convertTimestamp(value));
                break;
            case "int":
                fieldHtml = FieldTypeUtil.fieldInt(tableId, fieldData, Integer.parseInt((value == null || value.equals("")) ? "0" : value));
                break;
            case "decimal":
                fieldHtml = FieldTypeUtil.fieldDecimal(tableId, fieldData, getBigDecimal(value));
                break;
            case "select":
                fieldHtml = FieldTypeUtil.fieldSelect(tableId, fieldData, value);
                break;
            case "selects":
                fieldHtml = FieldTypeUtil.fieldSelects(tableId, fieldData, value);
                break;
            case "user":
                fieldHtml = FieldTypeUtil.fieldUser(tableId, fieldData, value);
                break;
            case "users":
                fieldHtml = FieldTypeUtil.fieldUsers(tableId, fieldData, value);
                break;
            case "dept":
                fieldHtml = FieldTypeUtil.fieldDept(tableId, fieldData, value);
                break;
            case "depts":
                fieldHtml = FieldTypeUtil.fieldDepts(tableId, fieldData, value);
                break;
            case "form":
                fieldHtml = FieldTypeUtil.fieldForm(tableId, fieldData, value);
                break;
            case "forms":
                fieldHtml = FieldTypeUtil.fieldForms(tableId, fieldData, value);
                break;
            case "upload":
                fieldHtml = FieldTypeUtil.fieldUpload(tableId, fieldData, value);
                break;
            case "uploads":
                fieldHtml = FieldTypeUtil.fieldUploads(tableId, fieldData, value);
                break;
            case "textarea":
                fieldHtml = FieldTypeUtil.fieldTextarea(tableId, fieldData, value);
                break;
            case "editor":
                fieldHtml = FieldTypeUtil.fieldEditor(tableId, fieldData, value);
                break;
            default:
                fieldHtml = FieldTypeUtil.fieldText(tableId, fieldData, value);
        }
        return fieldHtml;
    }

    public String getInfoButton(String user, String pageid, String formid, String recno) {
        String num = "";
        if ("admin".equals(user)) {
            num = "111111";
        } else {
            num = AccessUtil.getDataNum(user, pageid);
        }
        String buttonHtml = "";
        buttonHtml += "<div class='layui-row'><div class='layui-col-xs12'>";
        if (AccessUtil.getAdd(num)) {
            buttonHtml += "<div style='float:left; width:20%;text-align:center;'><a class='layui-btn layui-btn-radius' herf='javascript:void(0)' onclick=\"formadd(this,'" + pageid + "','" + formid + "')\">新增</a></div>";
        }
        if (AccessUtil.getModi(num)) {
            buttonHtml += "<div style='float:left; width:20%;text-align:center;'><a class='layui-btn layui-btn-radius' herf='javascript:void(0)' onclick=\"formmodi(this,'" + pageid + "','" + formid + "','" + recno + "')\">修改</a></div>";
        }
        if (AccessUtil.getDelete(num)) {
            buttonHtml += "<div style='float:left; width:20%;text-align:center;'><a class='layui-btn layui-btn-radius' herf='javascript:void(0)' onclick=\"formdel(this,'" + pageid + "','" + formid + "','" + recno + "')\">删除</a></div>";
        }
        if (AccessUtil.getSend(num)) {
            buttonHtml += "<div style='float:left; width:20%;text-align:center;'><a class='layui-btn layui-btn-radius' herf='javascript:void(0)' onclick=\"formsend('" + formid + "','" + recno + "')\">发送</a></div>";
        }
        buttonHtml += "<div style='float:right;width:20%;text-align:center;'><a class='layui-btn layui-btn-radius' herf='javascript:void(0)' onclick=\"fromcancel('" + pageid + "','" + formid + "')\">取消</a></div>";
        buttonHtml += "</div></div>";
        return buttonHtml;
    }

    public String getSaveButton(String pageid, String formid) {
        String buttonHtml = "";
        buttonHtml += "<div class='layui-row'><div class='layui-col-xs12'>";
        buttonHtml += "<div style='float:left; width:33%;text-align:center;'><a class='layui-btn layui-btn-radius' herf='javascript:void(0)' onclick=\"formsave(this,'" + formid + "')\">保存</a></div>";
        buttonHtml += "<div style='float:left; width:33%;text-align:center;'><a class='layui-btn layui-btn-radius' herf='javascript:void(0)' onclick=\"fromreset('" + formid + "')\">重置</a></div>";
        buttonHtml += "<div style='float:right;width:33%;text-align:center;'><a class='layui-btn layui-btn-radius' herf='javascript:void(0)' onclick=\"fromcancel('" + pageid + "','" + formid + "')\">取消</a></div>";
        buttonHtml += "</div></div>";
        return buttonHtml;
    }
}