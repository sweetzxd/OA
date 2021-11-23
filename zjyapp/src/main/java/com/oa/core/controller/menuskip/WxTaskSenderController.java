package com.oa.core.controller.menuskip;

import com.oa.core.bean.Loginer;
import com.oa.core.bean.dd.TaskData;
import com.oa.core.bean.system.FormCustomMade;
import com.oa.core.bean.system.TaskSender;
import com.oa.core.bean.user.UserManager;
import com.oa.core.bean.util.PageUtil;
import com.oa.core.bean.work.WorkFlowNode;
import com.oa.core.controller.util.LoginUtil;
import com.oa.core.interceptor.Logined;
import com.oa.core.service.dd.DictionaryService;
import com.oa.core.service.system.FormCustomMadeService;
import com.oa.core.service.system.RoleDefinesService;
import com.oa.core.service.system.TaskSenderService;
import com.oa.core.service.user.UserManagerService;
import com.oa.core.service.util.TableService;
import com.oa.core.service.work.WorkFlowLineService;
import com.oa.core.service.work.WorkFlowNodeService;
import com.oa.core.system.workflow.ProcUtil;
import com.oa.core.util.MySqlUtil;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @ClassName:TaskSenderController
 * @author:zxd
 * @Date:2018/09/27
 * @Time:上午 9:05
 * @Version V1.0
 * @Explain 任务发送
 */

@Controller
@RequestMapping("/weixin/task")
public class WxTaskSenderController {
    @Autowired
    private UserManagerService userManagerService;
    @Autowired
    private RoleDefinesService roleDefinesService;

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

    @RequestMapping(value = "/gototasksender", method = RequestMethod.GET)
    public ModelAndView gotoTaskSender(HttpServletRequest request) {
        ModelAndView mode = new ModelAndView("manage/tasksender");
        return mode;
    }

    @RequestMapping(value = "/tasksendpage", method = RequestMethod.GET)
    public String taskSendPage(HttpServletRequest request, String nodeId, String procId, String wkflwId, String workOrderNO) {
        String userid = request.getParameter("userid");
        request.getSession().setAttribute("loginer", LoginUtil.getLoginer(userid));
        request.getSession().setAttribute("role", LoginUtil.getRole(userid));

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
        String recno = "";
        if (sqlvalue != null && sqlvalue.size() > 0) {
            recno = String.valueOf(sqlvalue.get("recorderNO"));
        }
        String url = "?userid=" + userid + "&procId=" + procId + "&wkflwID=" + wkflwId + "&wkfNode=" + nodeId + "&workOrderNO=" + workOrderNO + "&recno" + recno;
        String temp = request.getParameter("temp");
        String htmlurl="";
        if (wkflwId.equals("swlc2019050900001")) {
            htmlurl= "redirect:/views/task/taskshouwen.html" + url;
        } else if(wkflwId.equals("fwlc2019050900001")){
            htmlurl= "redirect:/views/task/taskDetail.html" + url;
        }else {
            htmlurl= "redirect:/views/task/taskDetail.html" + url;
        }
        System.out.println(htmlurl);
//        htmlurl= "redirect:/views/task/taskDetail.html" + url;
        return htmlurl.toString();
    }

    @Logined
    @RequestMapping("/selectuser")
    @ResponseBody
    public String selectUser(HttpServletRequest request, String inputval, String option, int limit, int page) {
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        String userId = loginer.getId();
        TaskSender taskSender = new TaskSender();
        if (inputval != null && inputval != "") {
            if ("taskTitle".equals(option)) {
                taskSender.setTaskTitle(inputval);
            } else if ("modifyName".equals(option)) {
                taskSender.setModifyName(inputval);
            }
        }
        taskSender.setAccepter(userId);
        int count = taskSenderService.selectTermsCount(taskSender);
        PageUtil pageUtil = new PageUtil();
        pageUtil.setPageSize(limit);
        pageUtil.setCurrentPage(page);
        taskSender.setStartRow(pageUtil.getStartRow());
        taskSender.setEndRow(pageUtil.getEndRow() - pageUtil.getStartRow());
        List<TaskSender> taskSenderList = taskSenderService.selectTerms(taskSender);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 0);
        jsonObject.put("msg", "");
        jsonObject.put("count", count);
        jsonObject.put("data", taskSenderList);
        jsonObject.put("success", 1);
        return jsonObject.toString();
    }

    @Logined
    @RequestMapping(value = "/flowtask.do", method = RequestMethod.POST)
    public ModelAndView gotoFormPage(HttpServletRequest request) {
        ProcUtil pu = new ProcUtil();
        return pu.WorkFlowProc(request);
    }

}
