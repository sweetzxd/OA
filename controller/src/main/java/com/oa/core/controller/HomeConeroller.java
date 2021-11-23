package com.oa.core.controller;

import com.oa.core.bean.Loginer;
import com.oa.core.bean.module.Message;
import com.oa.core.bean.module.Schedule;
import com.oa.core.bean.system.TaskSender;
import com.oa.core.bean.util.PageUtil;
import com.oa.core.helper.DateHelper;
import com.oa.core.interceptor.Logined;
import com.oa.core.service.module.MessageService;
import com.oa.core.service.module.ScheduleService;
import com.oa.core.service.system.TaskSenderService;
import com.oa.core.service.util.TableService;
import com.oa.core.util.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
public class HomeConeroller {
    @Autowired
    TaskSenderService taskSenderService;
    @Autowired
    MessageService messageService;
    @Autowired
    TableService tableService;
    @Autowired
    ScheduleService scheduleService;
    @Logined
    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public ModelAndView home(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("home");
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        String userId = null;
        String userMenu = null;
        if (loginer != null) {
            userId = loginer.getId();
            MenuUtil mu = new MenuUtil();
            userMenu = mu.getMenu(userId);
        }
        List<TaskSender> taskSender = taskSenderService.selectUser("'"+userId+"'");
        try {
            Message m = new Message();
            m.setMsgRecUser(userId);
            m.setMsgStatus(1);
            List<Message> msgList = messageService.selectAllTerms(m);
            int msgcont = messageService.selectAllTermsCont(m);
            mav.addObject("msglist", msgList);
            mav.addObject("msgNum", msgcont);
        }catch (Exception e){
            LogUtil.sysLog("Exception:"+e);
            mav.addObject("msgcont", 0);
        }
        mav.addObject("userMenu", userMenu);
        mav.addObject("taskNum", taskSender.size());
        return mav;
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView login(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("login");
        return mav;
    }

    @Logined
    @RequestMapping(value = "/adminManage", method = RequestMethod.GET)
    public ModelAndView adminManager(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("manage/main_new");
        return mav;
    }

    @RequestMapping(value = "/homepage", method = RequestMethod.GET)
    public ModelAndView homePage(HttpServletRequest request) {
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        String userId = loginer.getId();

        String date = DateHelper.getYMD();
        Schedule schedule = new Schedule();
        schedule.setUser(userId);
        schedule.setStartTime(date+" 23:59:59");
        schedule.setEndTime(date);
        List<Schedule> scheduleList = scheduleService.getUserSchedule(schedule);
        if(scheduleList.size()>8){
            scheduleList = scheduleList.subList(0,8);
        }
        //查询任务列表
        List<TaskSender> taskSenderList = taskSenderService.selectByHome(userId);
        //查询消息列表
        Message m = new Message();
        m.setMsgRecUser(loginer.getId());
        m.setMsgStatus(1);
        List<Message> messageList = messageService.selectAllTerms(m);
        if(messageList.size()>8){
            messageList = messageList.subList(0,8);
        }

        //查询通知公告列表
        List<String> field = new ArrayList<>();
        field.add("bt18111000001");
        field.add("nr18111000001");
        field.add("xzdwj18111002");
        field.add("tzggl18111002");
        field.add("recordTime");
        List<String> where = new ArrayList<String>();
        where.add("tzggl18111002='通知'");
        String sql = MySqlUtil.getSql(field, "tzgg181110001", where);
        List<Map<String,Object>> tzList = tableService.selectSqlMapList(sql);
        if(tzList.size()>8){
            tzList = tzList.subList(0,8);
        }
        List<String> where1 = new ArrayList<String>();
        where1.add("tzggl18111002='公告'");
        String sql1 = MySqlUtil.getSql(field, "tzgg181110001", where1);
        List<Map<String,Object>> ggList = tableService.selectSqlMapList(sql1);
        if(ggList.size()>8){
            ggList = ggList.subList(0,8);
        }

        List<Map<String,Object>> usermenu = AccessUtil.getUserMenuValue(userId);
        if(ggList.size()>12){
            ggList = ggList.subList(0,12);
        }
        ModelAndView mav = new ModelAndView("homepage");
        mav.addObject("taskSenderList", taskSenderList);
        mav.addObject("messageList", messageList);
        mav.addObject("scheduleList", scheduleList);
        mav.addObject("tzList", tzList);
        mav.addObject("ggList", ggList);
        mav.addObject("usermenu", usermenu);
        return mav;
    }

}
