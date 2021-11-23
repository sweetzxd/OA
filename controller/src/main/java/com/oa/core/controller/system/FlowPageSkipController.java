package com.oa.core.controller.system;

import com.oa.core.bean.Loginer;
import com.oa.core.bean.dd.FieldData;
import com.oa.core.bean.dd.TableData;
import com.oa.core.bean.dd.TaskData;
import com.oa.core.bean.system.FormCustomMade;
import com.oa.core.bean.system.TaskSender;
import com.oa.core.bean.work.WorkFlowNode;
import com.oa.core.helper.DateHelper;
import com.oa.core.helper.StringHelper;
import com.oa.core.interceptor.Logined;
import com.oa.core.service.dd.DictionaryService;
import com.oa.core.service.system.FormCustomMadeService;
import com.oa.core.service.system.TaskSenderService;
import com.oa.core.service.util.TableService;
import com.oa.core.service.work.WorkFlowLineService;
import com.oa.core.service.work.WorkFlowNodeService;
import com.oa.core.system.postposition.PostPosition;
import com.oa.core.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * @ClassName:WorkFlowPageConeroller
 * @author:zxd
 * @Date:2018/09/27
 * @Time:上午 10:47
 * @Version V1.0
 * @Explain 流程页面跳转
 */
@Controller
@RequestMapping("/flowpage")
public class FlowPageSkipController {

    @Autowired
    FormCustomMadeService formCustomMadeService;
    @Autowired
    DictionaryService dictionaryService;
    @Autowired
    TableService tableService;
    @Autowired
    TaskSenderService taskSenderService;
    @Autowired
    WorkFlowNodeService workFlowNodeService;
    @Autowired
    WorkFlowLineService workFlowLineService;

    @Logined
    @RequestMapping(value = "flowviewpage/{formid}", method = RequestMethod.GET)
    public ModelAndView gotoFlowViewpage(HttpServletRequest request, @PathVariable String formid) {
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        String userId = loginer.getId();
        String wkflwId = request.getParameter("wkflwId") == null ? "" : request.getParameter("wkflwId");
        FormCustomMade formCustomMade = formCustomMadeService.selectFormCMByID(formid);
        String formTask = formCustomMade.getFormTask();
        String title = formCustomMade.getFormcmTitle();
        String page = formCustomMade.getEditPage();

        TaskData taskData = dictionaryService.selectTaskName(formTask);
        String tableId = taskData.getTableName();
        StringToHtmlUtil sth = new StringToHtmlUtil();
        List<String> form = sth.getFormHtml(request,"", formid, "add", taskData, null,false);
        ModelAndView mode = new ModelAndView(page);
        mode.addObject("title", title);
        mode.addObject("type", "add");
        mode.addObject("formid", formid);
        mode.addObject("wkflwId", wkflwId);
        mode.addObject("tableName", tableId);
        if(!formid.equals("flowtable2019041900003")||!formid.equals("flowtable2019050900006")||!formid.equals("flowtable2019050900001")){
            mode.addObject("form", form);
        }
        System.out.println(form);
        mode.addObject("starnode", "true");
        return mode;
    }

    @RequestMapping(value = "infoviewpage/{formid}", method = RequestMethod.GET)
    public ModelAndView gotoInfoViewpage(HttpServletRequest request, @PathVariable String formid) {
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        String userId = loginer.getId() == null ? "" : loginer.getId();

        String wkflwId = request.getParameter("wkflwId") == null ? "" : request.getParameter("wkflwId");
        String recno = request.getParameter("recno") == null ? "" : request.getParameter("recno");
        String workOrderNO = request.getParameter("workOrderNO") == null ? "" : request.getParameter("workOrderNO");

        FormCustomMade formCustomMade = formCustomMadeService.selectFormCMByID(formid);
        String formtask = formCustomMade.getFormTask();
        String title = formCustomMade.getFormcmTitle();
        String page = formCustomMade.getEditPage();
        TaskData taskData = dictionaryService.selectTaskName(formtask);
        String fields = taskData.getTaskField();
        String tableId = taskData.getTableName();
        List<String> list = Arrays.asList(fields.split(";"));
        List<String> field = new ArrayList<String>(list);
        field.add("workflowProcID");
        field.add("workflowNodeID");
        List<String> where = new ArrayList<String>();
        where.add("recorderNO='" + recno + "'");
        String sql = MySqlUtil.getSql(field, tableId, where);
        Map<String, Object> sqlvalue = tableService.selectSqlMap(sql);
        StringToHtmlUtil sth = new StringToHtmlUtil();
        field.add("recorderNO");
        field.remove("workflowProcID");
        field.remove("workflowNodeID");
        List<String> form = new ArrayList<>();
        for(String f: field) {
            FieldData fieldData = sth.getFieldData(f);
            String div = "<div class='layui-row'>";
            if(f!=null && f.equals("recorderNO")){
                div = "<div class='layui-row' style='display:none'>";
            }
            div += sth.getInfoField(sqlvalue, fieldData, tableId, 12);
            div += "</div>";
            form.add(div);
        }

        ModelAndView mode = new ModelAndView(page);
        mode.addObject("title", title);
        mode.addObject("type", "info");
        mode.addObject("formid", formid);
        mode.addObject("wkflwId", wkflwId);
        mode.addObject("workOrderNO", workOrderNO);
        mode.addObject("table", sqlvalue);
        mode.addObject("tableName", tableId);
        mode.addObject("form", form);
        return mode;
    }

    @RequestMapping(value = "/gototask", method = RequestMethod.GET)
    public ModelAndView gotoTask(HttpServletRequest request,String wkflwId,String workOrderNO,String procId,String nodeId) {
        String type = "add";
        TaskSender ts = new TaskSender();
        ts.setWorkOrderNO(workOrderNO);
        ts.setMsgStatus(3);
        taskSenderService.update(ts);
        TaskSender taskSender = taskSenderService.selectById(workOrderNO);
        String user = taskSender.getAccepter();
        WorkFlowNode workFlowNode = workFlowNodeService.selectById(nodeId);
        String formid = workFlowNode.getFormId();
        FormCustomMade formCustomMade = formCustomMadeService.selectFormCMByID(formid);
        String formTask = formCustomMade.getFormTask();
        String title = formCustomMade.getFormcmTitle();
        String page = formCustomMade.getEditPage();
        TaskData taskData = dictionaryService.selectTaskName(formTask);
        String fields = taskData.getTaskField();
        String tableId = taskData.getTableName();
        List<String> list = Arrays.asList(fields.split(";"));
        List<String> field = new ArrayList<>(list);
        field.add("recorderNO");
        field.add("workflowProcID");
        field.add("workflowNodeID");
        List<String> where = new ArrayList<String>();
        where.add("workflowProcID='" + procId + "'");
        where.add("recordName='" + user + "'");
        String sql = MySqlUtil.getSql(field, tableId, where);
        Map<String, Object> sqlvalue = tableService.selectSqlMap(sql);

        TableData tableData = dictionaryService.selectTableName(tableId);
        field.remove("workflowProcID");
        field.remove("workflowNodeID");

        ModelAndView mode = new ModelAndView(page);
        mode.addObject("title", title);
        if (sqlvalue != null && sqlvalue.size() > 0) {
            type = "modi";
        }
        List<String> sn = workFlowLineService.selectSN(wkflwId);
        String starnode = sn.get(0);
        if(starnode!=null && starnode.equals(nodeId)){
            mode.addObject("starnode", "true");
        }else{
            mode.addObject("starnode", "false");
        }
        StringToHtmlUtil sth = new StringToHtmlUtil();
        List<String> form = new ArrayList<>();
        for(String f: field) {
            FieldData fieldData = sth.getFieldData(f);
            String div = "<div class='layui-row'>";
            if(f!=null && f.equals("recorderNO")){
                div = "<div class='layui-row' style='display:none'>";
            }
            div += sth.getSaveField(sqlvalue, fieldData, tableId, 12);
            div += "</div>";
            form.add(div);
        }
        mode.addObject("type", type);
        mode.addObject("formid", formid);
        mode.addObject("workOrderNO", workOrderNO);
        mode.addObject("wkflwId", wkflwId);
        mode.addObject("procId", procId);
        mode.addObject("nodeId", nodeId);
        mode.addObject("table", sqlvalue);
        mode.addObject("tableName", tableId);
        if(!formid.equals("flowtable2019050900006")){
        mode.addObject("form", form);
        }
        return mode;
    }

    @RequestMapping(value = "/moditask", method = RequestMethod.GET)
    public ModelAndView modiTask(HttpServletRequest request) {
        String sql = "", user ="",page="",tableId="",fields="",title="";
        String formid = request.getParameter("formid");
        String wkflwId = request.getParameter("wkflwId");
        String workOrderNO = request.getParameter("workOrderNO");
        String procId = request.getParameter("workflowProcID");
        String nodeId = request.getParameter("workflowNodeID");
        List<String> field;
        if(workOrderNO!=null && !workOrderNO.equals("") && procId!=null && !procId.equals("")){
            TaskSender ts = new TaskSender();
            ts.setWorkOrderNO(workOrderNO);
            ts.setMsgStatus(3);
            taskSenderService.update(ts);
            TaskSender taskSender = taskSenderService.selectById(workOrderNO);
            user = taskSender.getAccepter();
            FormCustomMade formCustomMade = formCustomMadeService.selectFormCMByID(formid);
            String formTask = formCustomMade.getFormTask();
            title = formCustomMade.getFormcmTitle();
            page = formCustomMade.getEditPage();
            TaskData taskData = dictionaryService.selectTaskName(formTask);
            fields = taskData.getTaskField();
            tableId = taskData.getTableName();
            List<String> list = Arrays.asList(fields.split(";"));
            field = new ArrayList<>(list);
            field.add("workflowProcID");
            field.add("workflowNodeID");
            List<String> where = new ArrayList<String>();
            where.add("workflowProcID='" + procId + "'");
            where.add("recordName='" + user + "'");
            sql = MySqlUtil.getSql(field, tableId, where);
        }else{
            String recno = request.getParameter("recorderNO");
            Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
            user = loginer.getId() == null ? "" : loginer.getId();
            formid = request.getParameter("formid");
            FormCustomMade formCustomMade = formCustomMadeService.selectFormCMByID(formid);
            String formTask = formCustomMade.getFormTask();
            title = formCustomMade.getFormcmTitle();
            page = formCustomMade.getEditPage();
            TaskData taskData = dictionaryService.selectTaskName(formTask);
            fields = taskData.getTaskField();
            tableId = taskData.getTableName();
            List<String> list = Arrays.asList(fields.split(";"));
            field = new ArrayList<>(list);
            field.add("workflowProcID");
            field.add("workflowNodeID");
            List<String> where = new ArrayList<String>();
            where.add("recorderNO='" + recno + "'");
            where.add("recordName='" + user + "'");
            sql = MySqlUtil.getSql(field, tableId, where);
        }
        Map<String, Object> sqlvalue = tableService.selectSqlMap(sql);

        TableData tableData = dictionaryService.selectTableName(tableId);
        field.add("recorderNO");
        field.remove("workflowProcID");
        field.remove("workflowNodeID");

        ModelAndView mode = new ModelAndView(page);
        mode.addObject("title", title);
        if (sqlvalue != null && sqlvalue.size() > 0) {
            mode.addObject("type", "modi");
        } else {
            mode.addObject("type", "add");
        }
        List<String> sn = workFlowLineService.selectSN(wkflwId);
        String starnode = sn.get(0);
        if(starnode!=null && starnode.equals(nodeId)){
            mode.addObject("starnode", "true");
        }else{
            mode.addObject("starnode", "false");
        }
        StringToHtmlUtil sth = new StringToHtmlUtil();
        List<String> form = new ArrayList<>();
        for(String f: field) {
            FieldData fieldData = sth.getFieldData(f);
            String div = "<div class='layui-row'>";
            if(f!=null && f.equals("recorderNO")){
                div = "<div class='layui-row' style='display:none'>";
            }
            div += sth.getSaveField(sqlvalue, fieldData, tableId, 12);
            div += "</div>";
            form.add(div);
        }
        mode.addObject("formid", formid);
        mode.addObject("workOrderNO", workOrderNO);
        mode.addObject("wkflwId", wkflwId);
        mode.addObject("procId", procId);
        mode.addObject("nodeId", nodeId);
        mode.addObject("table", sqlvalue);
        mode.addObject("tableName", tableId);
        if(!formid.equals("flowtable2019041900003")){
            mode.addObject("form", form);
        }
        return mode;
    }
    @Logined
    @RequestMapping(value = "/flowaddsave/{formid}", method = RequestMethod.POST)
    public ModelAndView formAddSave(HttpServletRequest request, @PathVariable("formid") String formid) {
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        String userId = loginer.getId();

        Map<String, Object> map = new HashMap<String, Object>();
        FormCustomMade formCustomMade = formCustomMadeService.selectFormCMByID(formid);
        String formtask = formCustomMade.getFormTask();
        TaskData taskData = dictionaryService.selectTaskName(formtask);
        String fields = taskData.getTaskField();
        String table = taskData.getTableName();
        String procId = request.getParameter(table + "_workflowProcID") == null ? "" : request.getParameter(table + "_workflowProcID");
        String nodeId = request.getParameter(table + "_workflowNodeID") == null ? "" : request.getParameter(table + "_workflowNodeID");
        String workOrderNO = request.getParameter("workOrderNO") == null ? "" : request.getParameter("workOrderNO");
        String wkflwId = request.getParameter("wkflwId") == null ? "" : request.getParameter("wkflwId");
        boolean modi = false;
        String recorderNO = request.getParameter(table + "_recorderNO_Value");
        if (recorderNO != null && recorderNO != "") {
            modi = true;
        } else {
            String title = null;
            if (procId == null || procId == "") {
                title = "FlowTask";
            }
            PrimaryKeyUitl pk = new PrimaryKeyUitl();
            recorderNO = pk.getNextId(table, "recorderNO", title, Const.PREFIXTYPE_YEAR_MONTH_DAY);
        }
        ModelAndView mode;
        if (recorderNO == null || recorderNO == "") {
            mode = new ModelAndView("redirect:/flowpage/error.do");
            mode.addObject("msg", "主键为空");
            return mode;
        }
        boolean child = true;
        try {
            if (fields.length() > 0) {
                Vector<String> field = StringHelper.string2Vector(fields,";");
                for (String fieldName :field) {
                    FieldData fieldData = DDUtil.getFieldData(fieldName);
                    String fieldType = fieldData.getFieldType();
                    if(fieldType!=null && fieldType.equals("child")){
                        child = ChildUtil.setChildUtil(fieldData,recorderNO,userId);
                    }else {
                        String name = table + "_" + fieldName + "_Value";
                        String value = request.getParameter(name);
                        if (value != null && !value.equals("")) {
                            value = new String(value.getBytes("iso-8859-1"), "UTF-8");
                            map.put(fieldName, value);
                        }
                    }
                }
            } else {
                mode = new ModelAndView("redirect:/flowpage/error.do");
                mode.addObject("msg", "未找到添加字段");
                return mode;
            }
            map.put("workflowProcID", procId);
            map.put("workflowNodeID", nodeId);

            map.put("modifyName", userId);
            map.put("modifyTime", DateHelper.now());
            if (table != null && table != "") {
                /*
                 * 节点后置处理
                 * */
                if(false) {
                    PostPosition post = new PostPosition("form");
                    post.setProcId(procId);
                    post.setWorkOrderNO(workOrderNO);
                    post.setRecorderNO(recorderNO);
                    post.setTableMap(map);
                    boolean postposition = post.getPostPosition(wkflwId + "PostPosition");
                }
                if (modi) {
                    tableService.updateSqlMap(MySqlUtil.getUpdateSql(table, recorderNO, map, null));
                } else {
                    map.put("recorderNO", recorderNO);
                    map.put("recordName", userId);
                    map.put("recordTime", DateHelper.now());
                    if(child) {
                        tableService.insertSqlMap(MySqlUtil.getInsertSql(table, map));
                    }
                }
                mode = new ModelAndView("redirect:/flowpage/infoviewpage/" + formid + ".do?recno=" + recorderNO + "&wkflwId=" + wkflwId + "&procId=" + procId + "&workOrderNO=" + workOrderNO);
                //mode = new ModelAndView("redirect:/task/tasksendpage.do?formid=" + formid + "&recno=" + recorderNO + "&wkflwId=" + wkflwId + "&procId=" + procId+"&workOrderNO="+workOrderNO);
                mode.addObject("msg", "保存成功");
                System.out.println(mode+"22222222");
                return mode;
            } else {
                mode = new ModelAndView("redirect:/flowpage/error.do");
                mode.addObject("msg", "未找到数据表");
                return mode;
            }
        } catch (Exception e) {
            e.printStackTrace();
            mode = new ModelAndView("redirect:/flowpage/error.do");
            mode.addObject("msg", "保存失败");
            return mode;
        }
    }

    @RequestMapping(value = "/tasksendmsg", method = RequestMethod.GET)
    public ModelAndView taskSendMsg(HttpServletRequest request) {
        ModelAndView mode = new ModelAndView("manage/tasksendmsg");
        String msg = request.getParameter("msg") == null ? "" : request.getParameter("msg");
        String type = request.getParameter("type") == null ? "" : request.getParameter("type");
        mode.addObject("msg", StringHelper.decodeUnicode(msg));
        mode.addObject("type", type);
        return mode;
    }
}
