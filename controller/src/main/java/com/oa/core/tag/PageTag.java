package com.oa.core.tag;

import com.oa.core.bean.dd.TaskData;
import com.oa.core.bean.system.FormCustomMade;
import com.oa.core.helper.DateHelper;
import com.oa.core.service.dd.DictionaryService;
import com.oa.core.service.system.FormCustomMadeService;
import com.oa.core.service.util.TableService;
import com.oa.core.util.SpringContextUtil;
import com.oa.core.util.StringToHtmlUtil;

import java.util.*;

public class PageTag extends RootTag  {
    private static final long serialVersionUID = 1L;

    private String type;

    private String value = "";

    private String data;

    private String form;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getData() { return data; }

    public void setData(String data) { this.data = data; }

    public String getForm() {
        return form;
    }

    public void setForm(String form) {
        this.form = form;
    }

    @Override
    public int doStartTag() {
        super.init();
        return EVAL_BODY_INCLUDE;
    }
    @Override
    public int doEndTag() {
        try {
            String outHtml = "";
            StringToHtmlUtil sth = new StringToHtmlUtil();
            switch (type) {
                case "list_title":
                    outHtml = sth.getListTitle(value);
                    break;
                case "list_value":
                    outHtml = sth.getListValue(value);
                    break;
                case "list_select":
                    outHtml = sth.getListSearch(value);
                    break;
                case "list_button":
                    boolean lastsave = false;
                    if(data!=null && "true".equals(data)){
                        lastsave = true;
                    }
                    outHtml = sth.getListButton(value,lastsave,form);
                    break;
                case "list_modititle":
                    outHtml = sth.getListTitle(value,true);
                    break;
                case "birthday":
                    Calendar cale = Calendar.getInstance();
                    int month = cale.get(Calendar.MONTH) + 1;
                    TableService tableService = (TableService) SpringContextUtil.getBean("tableService");
                    List<Map<String,Object>> sqlvalue = tableService.selectSqlMapList("select * from employees where curStatus=2 and MONTH(dateOfBirth)="+month);
                    int len = sqlvalue.size();
                    if(len>0) {
                        for (int i = 0; i < len; i++) {
                            Map<String, Object> map = sqlvalue.get(i);
                            String staffName = (String) map.get("staffName");
                            String birthday = String.valueOf(map.get("dateOfBirth"));
                            if(birthday!=null && birthday.contains("-")){
                                String[] date = birthday.split("-");
                                birthday = "生日："+date[1]+"月"+date[2]+"日";
                            }
                            outHtml += "<span class='oa-homepage-birthday' title='"+birthday+"'>" + staffName + "</span>";
                        }
                    }
                    break;
                case "formula":
                    DictionaryService dictionaryService = (DictionaryService)SpringContextUtil.getBean("dictionaryService");
                    TaskData taskData = dictionaryService.formTaskByFormId(value);
                    StringBuffer sql = new StringBuffer("SELECT recorderNO,formulasValue,formulasResult FROM DesignFormulas WHERE curStatus=2 AND tableName ='"+taskData.getTableName()+"'");
                    TableService tableService1 = (TableService)SpringContextUtil.getBean("tableService");
                    List<Map<String, Object>> list = tableService1.selectSqlMapList(sql.toString());
                    for(Map<String,Object> map : list){
                        outHtml += "<input type='hidden' data='"+map.get("formulasResult")+"'  id='"+map.get("recorderNO")+"' value='"+map.get("formulasValue")+"'>";
                    }
                    break;
                default:
                    outHtml = value;
            }
            out.append(outHtml);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return EVAL_PAGE;
    }
}
