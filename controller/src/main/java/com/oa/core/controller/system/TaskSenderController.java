package com.oa.core.controller.system;

import com.oa.core.bean.Loginer;
import com.oa.core.bean.system.TaskSender;
import com.oa.core.bean.util.PageUtil;
import com.oa.core.interceptor.Logined;
import com.oa.core.service.system.TaskSenderService;
import com.oa.core.system.workflow.ProcUtil;
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
 * @ClassName:TaskSenderController
 * @author:zxd
 * @Date:2018/09/27
 * @Time:上午 9:05
 * @Version V1.0
 * @Explain 任务发送
 */

@Controller
@RequestMapping("/task")
public class TaskSenderController {

    @Autowired
    TaskSenderService taskSenderService;

    @RequestMapping(value = "/gototasksender", method = RequestMethod.GET)
    public ModelAndView gotoTaskSender(HttpServletRequest request) {
        ModelAndView mode = new ModelAndView("manage/tasksender");
        return mode;
    }

    @RequestMapping(value = "/tasksendpage", method = RequestMethod.GET)
    public ModelAndView taskSendPage(HttpServletRequest request, String nodeId, String procId, String wkflwId, String workOrderNO) {
        ModelAndView mode = new ModelAndView("manage/tasksendpage");
        mode.addObject("nodeId", nodeId);
        mode.addObject("procId", procId);
        mode.addObject("wkflwId", wkflwId);
        mode.addObject("workOrderNO", workOrderNO);
        return mode;
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
        jsonObject.put("success",1);
        return jsonObject.toString();
    }

    @Logined
    @RequestMapping(value = "/flowtask.do", method = RequestMethod.POST)
    public ModelAndView gotoFormPage(HttpServletRequest request) {
        ProcUtil pu = new ProcUtil();
        return pu.WorkFlowProc(request);
    }

}
