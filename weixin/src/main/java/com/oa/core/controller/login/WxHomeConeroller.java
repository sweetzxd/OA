package com.oa.core.controller.login;

import com.oa.core.bean.module.Message;
import com.oa.core.bean.system.TaskSender;
import com.oa.core.bean.util.PageUtil;
import com.oa.core.helper.DateHelper;
import com.oa.core.helper.StringHelper;
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

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.*;

@Controller
@RequestMapping("/weixin")
public class WxHomeConeroller {
    @Autowired
    TaskSenderService taskSenderService;
    @Autowired
    MessageService messageService;
    @Autowired
    TableService tableService;
    @Autowired
    ScheduleService scheduleService;

    @RequestMapping(value = "/home", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String home(HttpServletRequest request, String date) {
        JSONObject json = new JSONObject();
        JSONObject jsonObject = new JSONObject();
        String userId = request.getParameter("userid");
        //查看任务
        List<TaskSender> taskSender = taskSenderService.selectUser("'" + userId + "'");
        try {
            Message m = new Message();
            m.setMsgRecUser(userId);
            m.setMsgStatus(1);
            //查询消息
            List<Message> msgList = messageService.selectAllTerms(m);
            int msgcont = messageService.selectAllTermsCont(m);
            //查询通知
            List<String> where = new ArrayList<>();
            where.add("tzggl18111002='通知'");
            if (!userId.equals("admin")) {
                where.add("ckr1906060002 like ('%" + userId + ";%')");
                where.add("recorderNO not in (select glzj190606001 from ydzt190606001 where curStatus=2 and dxr1906060001='" + userId + "')");
            }
            String countSql1 = MySqlUtil.getCountSql("tzgg181110001", where);
            int count1 = tableService.selectSqlCount(countSql1);
            //查询公告
            where = new ArrayList<>();
            where.add("tzggl18111002='公告'");
            if (!userId.equals("admin")) {
                where.add("recorderNO not in (select glzj190606001 from ydzt190606001 where curStatus=2 and dxr1906060001='" + userId + "')");
            }
            String countSql2 = MySqlUtil.getCountSql("tzgg181110001", where);
            int count2 = tableService.selectSqlCount(countSql2);
            //查询日程列表
            String sql = " select * from schedule \n" +
                    "               where user = '" + userId + "' and curStatus = 2\n" +
                    "               and DATE_FORMAT(startTime,'%Y-%m-%d') <= '" + date + "'\n" +
                    "               and DATE_FORMAT(endTime,'%Y-%m-%d') >= '" + date + "' ORDER BY startTime LIMIT 5;";
            List<Map<String, Object>> taskList = tableService.selectSqlMapList(sql);

            json.put("taskList", taskList);
            json.put("taskNum", taskList.size());
            json.put("tzNum", count1);
            json.put("ggNum", count2);
            json.put("msgNum", msgcont);
            json.put("msglist", msgList);

            jsonObject.put("success", 1);
        } catch (Exception e) {
            LogUtil.sysLog("Exception:" + e);
            jsonObject.put("success", 0);
        }

        json.put("taskNum", taskSender.size());
        String time = "1分钟前";
        if (taskSender.size() > 0) {
            Timestamp recordTime = taskSender.get(0).getRecordTime();
            time = calculatetime(recordTime.toString());
        }
        json.put("time", time);
        JSONObject url = new JSONObject();
        ConfParseUtil cpu = new ConfParseUtil();
        boolean srvicehtml = "true".equals(cpu.getPoa("open_servicehtml"));
        url.put("wendang", !srvicehtml ? "" : "/module/wendang.do");
        url.put("rizhi", !srvicehtml ? "" : "/module/rizhi.do");
        url.put("gongzuo", !srvicehtml ? "" : "/module/gongzuo.do");
        List<Map<String, Object>> usermenu = new ArrayList<>();
        Map<String, Object> menu = new HashMap<>();
        menu.put("id", "kaoqin");
        menu.put("name", "考勤记录");
        menu.put("url", !srvicehtml ? "" : "/module/kaoqin.do");
        menu.put("menuImg", "/upload/menuimg/考勤记录.png");
        usermenu.add(menu);
        menu = new HashMap<>();
        menu.put("id", "rizhi");
        menu.put("name", "工作日志");
        menu.put("url", !srvicehtml ? "" : "/module/rizhi.do");
        menu.put("menuImg", "/upload/menuimg/工作日志.png");
        usermenu.add(menu);
        menu = new HashMap<>();
        menu.put("id", "wendang");
        menu.put("name", "常用文档");
        menu.put("url", !srvicehtml ? "" : "/module/wendang.do");
        menu.put("menuImg", "/upload/menuimg/常用文档.png");
        usermenu.add(menu);
        usermenu.addAll(AccessUtil.getUserMenuValue(userId));
        jsonObject.put("data", json);
        jsonObject.put("url", url);
        jsonObject.put("usermenu", usermenu);
        System.out.println(usermenu);
        return jsonObject.toString();
    }

    public String calculatetime(String time) {
        String msg = null;
        Date setTime = DateHelper.convert(time,"yyyy-MM-dd HH:mm:ss");
        long reset = setTime.getTime();
        long dateDiff = System.currentTimeMillis() - reset;
        if (dateDiff < 0) {
            msg = "输入的时间不对";
        } else {
            long dateTemp1 = dateDiff / 1000;
            long dateTemp2 = dateTemp1 / 60;
            long dateTemp3 = dateTemp2 / 60;
            long dateTemp4 = dateTemp3 / 24;
            long dateTemp5 = dateTemp4 / 30;
            long dateTemp6 = dateTemp5 / 12;
            if (dateTemp6 > 0) {
                msg = dateTemp6 + "年前";
            } else if (dateTemp5 > 0) {
                msg = dateTemp5 + "个月前";
            } else if (dateTemp4 > 0) {
                msg = dateTemp4 + "天前";
            } else if (dateTemp3 > 0) {
                msg = dateTemp3 + "小时前";
            } else if (dateTemp2 > 0) {
                msg = dateTemp2 + "分钟前";
            } else if (dateTemp1 > 0) {
                msg = "刚刚";
            }
        }
        return msg;
    }

    @RequestMapping(value = "/selectusertask", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String selectUserTask(HttpServletRequest request, String inputval, String option, int limit, int page) {
        String userId = request.getParameter("userid");
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
        ConfParseUtil cpu = new ConfParseUtil();
        boolean srvicehtml = "true".equals(cpu.getPoa("open_servicehtml"));
        for (TaskSender tasksend : taskSenderList) {
            Timestamp recordTime = tasksend.getRecordTime();
            if (recordTime.toString() != null || recordTime.toString() != "") {
                String s = recordTime.toString();
                String rt = s.substring(0, 16);
                tasksend.setRecordTimeShot(rt);
            }
            if(!srvicehtml) {
                tasksend.setRefLinkUrl("");
            }
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 0);
        jsonObject.put("msg", "");
        jsonObject.put("count", count);
        jsonObject.put("data", taskSenderList);
        jsonObject.put("success", 1);
        return jsonObject.toString();
    }

    @RequestMapping(value = "/getuserinfo", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String getUserInfo(HttpServletRequest request, String phone) {
        ConfParseUtil cpu = new ConfParseUtil();
        String company = StringHelper.decodeUnicode(cpu.getPoa("company"));
        String sql = "select u.deptName as deptName,u.staffName as staffName,e.post as post from userInfo u left join employees e on u.userName=e.userName where e.phone='" + phone + "'";
        JSONObject json = new JSONObject();
        try {
            Map<String, Object> map = tableService.selectSqlMap(sql);
            if (map == null) {
                map = setUserMap(company);
            }
            json.put("msg", "");
            json.put("data", map);
            json.put("success", 1);
        } catch (Exception e) {
            e.getLocalizedMessage();
            json.put("data", setUserMap(company));
            json.put("msg", "");
            json.put("success", 1);
        }
        return json.toString();
    }

    @RequestMapping(value = "/getalluserinfo", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String getAllUserInfo(HttpServletRequest request, String phone) {
        ConfParseUtil cpu = new ConfParseUtil();
        String company = StringHelper.decodeUnicode(cpu.getPoa("company"));
        String sql = "select '" + company + "' as company,u.deptName as deptName,u.staffName as staffName,e.post as post,phone,telephone from userInfo u left join employees e on u.userName=e.userName";
        JSONObject json = new JSONObject();
        try {
            List<Map<String, Object>> maps = tableService.selectSqlMapList(sql);
            json.put("msg", "");
            json.put("data", maps);
            json.put("success", 1);
        } catch (Exception e) {
            e.getLocalizedMessage();
            json.put("msg", "");
            json.put("success", 1);
        }
        return json.toString();
    }

    public Map<String, Object> setUserMap(String company) {
        Map<String, Object> map = new HashMap<>();
        map.put("company", company);
        map.put("deptName", "");
        map.put("deptName", "");
        map.put("staffName", "陌生人");
        map.put("post", "");
        return map;
    }
}
