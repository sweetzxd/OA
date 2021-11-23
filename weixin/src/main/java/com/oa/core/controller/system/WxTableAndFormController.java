package com.oa.core.controller.system;

import com.oa.core.bean.Loginer;
import com.oa.core.bean.dd.FieldData;
import com.oa.core.bean.dd.TaskData;
import com.oa.core.bean.system.FormCustomMade;
import com.oa.core.bean.system.MyUrlRegist;
import com.oa.core.controller.util.FormUtil;
import com.oa.core.controller.util.LoginUtil;
import com.oa.core.helper.StringHelper;
import com.oa.core.interceptor.Logined;
import com.oa.core.listener.InitDataListener;
import com.oa.core.service.dd.DictionaryService;
import com.oa.core.service.module.DepartmentService;
import com.oa.core.service.system.FormCustomMadeService;
import com.oa.core.service.system.MyUrlRegistService;
import com.oa.core.service.user.UserManagerService;
import com.oa.core.service.util.TableService;
import com.oa.core.util.AccessUtil;
import com.oa.core.util.DDUtil;
import com.oa.core.util.StringToHtmlUtil;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @ClassName:WxTableAndFormController
 * @author:zxd
 * @Date:2019/03/22
 * @Time:上午 11:47
 * @Version V1.0
 * @Explain 获取列表和表单字段
 */
@Controller
@RequestMapping("/weixin/tableform")
public class WxTableAndFormController {
    @Autowired
    MyUrlRegistService myUrlRegistService;
    @Autowired
    FormCustomMadeService formCustomMadeService;
    @Autowired
    DictionaryService dictionaryService;
    @Autowired
    TableService tableService;
    @Autowired
    UserManagerService userManagerService;
    @Autowired
    DepartmentService departmentService;

    private Vector<String> formids = StringHelper.string2Vector("gzrz2019040900001", ",");

    /**
     * 根据formid获取对应的form中的内容
     * @param request
     * @param pageid
     * @param formid
     * @return
     */
    @RequestMapping(value = "/formfield", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String getFormField(HttpServletRequest request, String pageid, String formid) {
        String userId = request.getParameter("userid");

        String status = request.getParameter("status");
        String recno = request.getParameter("recno");
        if (formid == null || formid.equals("") || formid.equals("undefined")) {
            MyUrlRegist myUrlRegist = myUrlRegistService.selectById(pageid);
            formid = myUrlRegist.getFormId();
        }
        if (formid != null) {
            JSONObject json = new JSONObject();

            FormCustomMade formCustomMade = formCustomMadeService.selectFormCMByID(formid);
            String formTask = formCustomMade.getFormTask();
            String title = formCustomMade.getFormcmTitle();
            json.put("title", title);
            int formType = formCustomMade.getFormType();
            if (formTask != null && !formTask.equals("")) {
                TaskData taskData = dictionaryService.selectTaskName(formTask);
                String num;
                if (pageid != null && pageid.equals("childtable")) {
                    num = "000000";
                } else {
                    num = AccessUtil.getDataNum(userId, pageid);
                }
                FormUtil fu = new FormUtil();
                List<HashMap> list = new ArrayList<>();
                if (formid.equals("gzrz2019040900001")) {
                    list = fu.getFormHtml(userId, taskData, recno, "joblogId");
                } else if (formid.equals("rcap2019040900001")) {
                    list = fu.getFormHtml(userId, taskData, recno, "scheduleId");
                } else {
                    if (formType != 3) {
                        list = fu.getFormHtml(userId, taskData, recno);
                    }
                }
                json.put("field", list);
                json.put("add", AccessUtil.getAdd(num));
                json.put("modi", AccessUtil.getModi(num));
                json.put("del", AccessUtil.getDelete(num));
            }
            return json.toString();
        } else {
            return "";
        }
    }

    /**
     * 获取app端列表中的数据
     * @param request
     * @param pageid
     * @param formid
     * @return
     */
    @RequestMapping(value = "/tablefield", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String getTableField(HttpServletRequest request, String pageid, String formid) {
        String userId = request.getParameter("userid");
        String key = request.getParameter("key");
        String start = request.getParameter("start");
        String end = request.getParameter("end");
        request.getSession().setAttribute("loginer", LoginUtil.getLoginer(userId));
        request.getSession().setAttribute("role", LoginUtil.getRole(userId));
        if (formid == null || formid.equals("") || formid.equals("undefined")) {
            MyUrlRegist myUrlRegist = myUrlRegistService.selectById(pageid);
            formid = myUrlRegist.getFormId();
        }
        if (formid != null) {
            JSONObject json = new JSONObject();

            FormCustomMade formCustomMade = formCustomMadeService.selectFormCMByID(formid);
            String listTask = formCustomMade.getListTask();
            String title = formCustomMade.getFormcmTitle();
            json.put("title", title);
            int formType = formCustomMade.getFormType();
            if (listTask != null && !listTask.equals("")) {
                TaskData taskData = dictionaryService.selectTaskName(listTask);
                String num;
                if (pageid != null && pageid.equals("childtable")) {
                    num = "000000";
                } else {
                    num = AccessUtil.getDataNum(userId, pageid);
                }
                FormUtil fu = new FormUtil();
                String page = request.getParameter("page");
                if (page == null || page == "") {
                    page = null;
                }
                if (formids.contains(formid)) {
                    json = getNoTable(formid, json);
                    List<String> where = new ArrayList<>();
                    where.add("user='" + userId + "'");
                    String tableId= taskData.getTableName();
                    String taskField = taskData.getTaskField();
                    Vector<String> fields = StringHelper.string2Vector(taskField, ";");
                    List<Map<String, Object>> tbody = fu.getTableValue2(tableId,fields, where, page, "joblogId");
                    json.put("tbody", tbody);
                } else {
                    if (formType != 3) {
                        List<String> where = new ArrayList<>();
                        List<HashMap> thead = fu.getTableField(taskData);
                        List fields = new ArrayList();
                        String fwhere = null;
                        for (int i = 0; i < 2; i++) {
                            fwhere = fwhere==null?"":fwhere;
                            HashMap fhm = new HashMap();
                            fhm.put("name", thead.get(i).get("name"));
                            fhm.put("type", thead.get(i).get("type"));
                            fields.add(fhm);
                            String field = (String)thead.get(i).get("name");
                            fwhere += DDUtil.getTypeWhere(field, key) +" or ";
                        }
                        if (key != null && !key.equals("")) {
                            if(fwhere !=null && fwhere.length()>4){
                                fwhere = fwhere.substring(0,fwhere.length()-4);
                                where.add("( "+ fwhere +" )");
                            }
                        }
                        if (start != null && !start.equals("") && end != null && !end.equals("")) {
                            where.add("recordTime between'" + start + "' and '" + end + "'");
                        }
                        List<Map<String, Object>> tbody = fu.getTableValue(taskData, where, page);
                        json.put("thead", thead);
                        json.put("field", fields);
                        json.put("tbody", tbody);
                    }
                }
            }
            return json.toString();
        } else {
            return "";
        }
    }

    @RequestMapping(value = "/formtablefield", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String getFormTableField(HttpServletRequest request) {
        String userId = request.getParameter("userid");
        String key = request.getParameter("key");
        String start = request.getParameter("start");
        String end = request.getParameter("end");
        request.getSession().setAttribute("loginer", LoginUtil.getLoginer(userId));
        request.getSession().setAttribute("role", LoginUtil.getRole(userId));
        String taskName = request.getParameter("pageid");
        if(taskName == null || taskName == ""){
            taskName = "khxxb19010901";
        }
        if (taskName != null) {
            JSONObject json = new JSONObject();
            if (taskName != null && !taskName.equals("")) {
                TaskData taskData = dictionaryService.selectTaskName(taskName);
                FormUtil fu = new FormUtil();
                String page = request.getParameter("page");
                if (page == null || page == "") {
                    page = null;
                }
                List<String> where = new ArrayList<>();
                List<HashMap> thead = fu.getTableField(taskData);
                List fields = new ArrayList();
                for (int i = 0; i < 2; i++) {
                    HashMap fhm = new HashMap();
                    fhm.put("name", thead.get(i).get("name"));
                    fhm.put("type", thead.get(i).get("type"));
                    fields.add(fhm);
                    if (key != null && !key.equals("")) {
                        where.add(thead.get(i).get("name") + "='" + key + "'");
                    }
                    if (start != null && !start.equals("") && end != null && !end.equals("")) {
                        where.add("recordTime between'" + start + "' and '" + end + "'");
                    }
                }
                List<Map<String, Object>> tbody = fu.getTableValue(taskData, where, page);
                json.put("thead", thead);
                json.put("field", fields);
                json.put("tbody", tbody);

            }
            return json.toString();
        } else {
            return "";
        }
    }

    public JSONObject getNoTable(String formid, JSONObject json) {
        HashMap hm;
        switch (formid) {
            case "gzrz2019040900001":

                List<HashMap> thead = new ArrayList<>();
                hm = new HashMap();
                hm.put("name", "joblogTitle");
                hm.put("title", "标题");
                hm.put("type", "text");
                thead.add(hm);
                hm = new HashMap();
                hm.put("name", "leader");
                hm.put("title", "上报人");
                hm.put("type", "text");
                thead.add(hm);
                hm = new HashMap();
                hm.put("name", "joblogType");
                hm.put("title", "日志类型");
                hm.put("type", "text");
                thead.add(hm);
                hm = new HashMap();
                hm.put("name", "content");
                hm.put("title", "工作情况");
                hm.put("type", "textarea");
                thead.add(hm);
                json.put("thead", thead);

                List fields = new ArrayList();
                hm = new HashMap();
                hm.put("name", "joblogTitle");
                hm.put("type", "text");
                fields.add(hm);
                hm = new HashMap();
                hm.put("name", "leader");
                hm.put("type", "text");
                fields.add(hm);
                hm = new HashMap();
                hm.put("name", "joblogType");
                hm.put("type", "text");
                fields.add(hm);
                hm = new HashMap();
                hm.put("name", "content");
                hm.put("type", "textarea");
                fields.add(hm);
                json.put("field", fields);
                break;
            default:
                break;
        }

        return json;
    }
}
