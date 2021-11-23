package com.oa.core.controller.login;

import com.oa.core.bean.module.Message;
import com.oa.core.bean.system.TaskSender;
import com.oa.core.bean.util.PageUtil;
import com.oa.core.controller.util.LoginUtil;
import com.oa.core.helper.StringHelper;
import com.oa.core.service.module.MessageService;
import com.oa.core.service.module.ScheduleService;
import com.oa.core.service.system.TaskSenderService;
import com.oa.core.service.util.TableService;
import com.oa.core.util.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
            String countSql1 = MySqlUtil.getCountSql("tzgg181110001", where);
            int count1 = tableService.selectSqlCount(countSql1);
            //查询公告
            where = new ArrayList<>();
            where.add("tzggl18111002='公告'");
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
            WeatherUtil weather = new WeatherUtil();
            json.put("weather", weather.getWeather(Const.LOCATION));
            RestrictUtil ru = new RestrictUtil();
            json.put("restrict", ru.getRestrict());
            jsonObject.put("success", 1);
        } catch (Exception e) {
            LogUtil.sysLog("Exception:" + e);
            jsonObject.put("success", 0);
        }

        json.put("taskNum", taskSender.size());
        if (taskSender.size() > 0) {
            Timestamp recordTime = taskSender.get(0).getRecordTime();
            String time = CalculateTime(recordTime.toString());
            json.put("time", time);
        }
        jsonObject.put("data", json);
        return jsonObject.toString();
    }

    @RequestMapping(value = "/homemenu", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String getHomeMenu(HttpServletRequest request) {
        String userid = request.getParameter("userid");
        JSONObject jsonObject = new JSONObject();
        try {
            request.getSession().setAttribute("loginer", LoginUtil.getLoginer(userid));
            request.getSession().setAttribute("role", LoginUtil.getRole(userid));
            JSONArray menu = new JSONArray();
            JSONObject dang = new JSONObject();
            dang.put("name", "党务工作");
            dang.put("url", "/module/dangwu.do");
            dang.put("img", "/resources/home/images/dang.png");
            menu.put(dang);
            dang = new JSONObject();
            dang.put("name", "会议日历");
            dang.put("url", "");
            dang.put("img", "/upload/menuimg/会议日历.png");
            menu.put(dang);
            dang = new JSONObject();
            dang.put("name", "用车情况");
            dang.put("url", "");
            dang.put("img", "/upload/menuimg/车辆使用台账.png");
            menu.put(dang);
            dang = new JSONObject();
            dang.put("name", "公文查看");
            dang.put("url", "");
            dang.put("img", "/upload/menuimg/发文流程台账.png");
            menu.put(dang);
            List<Map<String,Object>> usermenu = AccessUtil.getUserMenuValue("all");
            for (int i = 0, len = usermenu.size(); i < len; i++) {
                Map<String,Object> menudata = usermenu.get(i);
                JSONObject data = new JSONObject();
                data.put("name", menudata.get("cdmc181210001"));
                data.put("url", menudata.get("cddz181210001"));
                data.put("img", menudata.get("menuImg"));
                menu.put(data);
            }
            jsonObject.put("success", 1);
            jsonObject.put("msg", "");
            jsonObject.put("data", menu);
        } catch (Exception e) {
            e.getLocalizedMessage();
            jsonObject.put("success", 0);
            jsonObject.put("msg", "菜单获取失败");
        }
        return jsonObject.toString();
    }

    @RequestMapping(value = "/homemsg", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String getHomeMsg(HttpServletRequest request) {
        String userid = request.getParameter("userid");
        JSONObject jsonObject = new JSONObject();
        try {
            request.getSession().setAttribute("loginer", LoginUtil.getLoginer(userid));
            request.getSession().setAttribute("role", LoginUtil.getRole(userid));
            Message m = new Message();
            m.setMsgRecUser(userid);
            m.setMsgStatus(1);
            JSONObject data = new JSONObject();
            String limit = " limit 0 , 5";
            Vector<String> fields1 = StringHelper.string2Vector("recorderNO;xwbt190414001;fbt1904250001;dxwb190414001;dxr1904140001;rqxz190414001;yxj1904150001;xslx190425001;sfzd190425001;dwjsc19041401;xwgj190414001;",";");
            String sqlxinwen = MySqlUtil.getFieldSql3(fields1, "xwtz190414001", null, "order by recordTime desc",limit);
            Vector<String> fields2 = StringHelper.string2Vector("recorderNO;zxbt190414001;fbt1904250002;zxnr190414001;zxrq190414001;zxtp190414001;zxwj190414001;wwlj190424001;rdht190424001;xslx190424001;sfzd190415001;",";");
            String sqldangqun = MySqlUtil.getFieldSql3(fields2, "dnzx190414001", null, "order by sfzd190415001 desc,recordTime desc",limit);
            data.put("xinwen", tableService.selectSqlMapList(sqlxinwen));
            data.put("dangqun", tableService.selectSqlMapList(sqldangqun));
            jsonObject.put("success", 1);
            jsonObject.put("msg", "");
            jsonObject.put("data", data);
        } catch (Exception e) {
            e.getLocalizedMessage();
            jsonObject.put("success", 0);
            jsonObject.put("msg", "信息获取失败");
        }
        return jsonObject.toString();
    }

    @RequestMapping(value = "/homexinwen", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String getHomexinwen(HttpServletRequest request) {
        String userid = request.getParameter("userid");
        PageUtil pu = new PageUtil();
        int page=0,limit = 10;
        String rp = request.getParameter("page");
        if(rp!=null && !rp.equals("")){
            page = Integer.parseInt(rp);
        }
        pu.setPageSize(limit);
        pu.setCurrentPage(page);
        int startRow = pu.getStartRow();
        int endRow = pu.getEndRow() - pu.getStartRow();
        JSONObject jsonObject = new JSONObject();
        try {
            request.getSession().setAttribute("loginer", LoginUtil.getLoginer(userid));
            request.getSession().setAttribute("role", LoginUtil.getRole(userid));
            Message m = new Message();
            m.setMsgRecUser(userid);
            m.setMsgStatus(1);
            jsonObject.put("success", 1);
            jsonObject.put("msg", "");
            Vector<String> fields = StringHelper.string2Vector("xwbt190414001;fbt1904250001;dxwb190414001;dxr1904140001;rqxz190414001;yxj1904150001;xslx190425001;sfzd190425001;dwjsc19041401;xwgj190414001;",";");
            String sqlxinwen = MySqlUtil.getFieldSql(fields, "xwtz190414001", null,startRow,endRow);
            jsonObject.put("data", tableService.selectSqlMapList(sqlxinwen));
        } catch (Exception e) {
            e.getLocalizedMessage();
            jsonObject.put("success", 0);
            jsonObject.put("msg", "信息获取失败");
        }
        return jsonObject.toString();
    }

    @RequestMapping(value = "/homedangqun", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String getHomedangqun(HttpServletRequest request) {
        String userid = request.getParameter("userid");
        PageUtil pu = new PageUtil();
        int page=0,limit = 10;
        String rp = request.getParameter("page");
        if(rp!=null && !rp.equals("")){
            page = Integer.parseInt(rp);
        }
        pu.setPageSize(limit);
        pu.setCurrentPage(page);
        int startRow = pu.getStartRow();
        int endRow = pu.getEndRow() - pu.getStartRow();
        JSONObject jsonObject = new JSONObject();
        try {
            request.getSession().setAttribute("loginer", LoginUtil.getLoginer(userid));
            request.getSession().setAttribute("role", LoginUtil.getRole(userid));
            Message m = new Message();
            m.setMsgRecUser(userid);
            m.setMsgStatus(1);
            jsonObject.put("success", 1);
            Vector<String> fields2 = StringHelper.string2Vector("zxbt190414001;fbt1904250002;zxnr190414001;zxrq190414001;zxtp190414001;zxwj190414001;wwlj190424001;rdht190424001;xslx190424001;sfzd190415001;",";");
            String sqldangqun = MySqlUtil.getFieldSql(fields2, "dnzx190414001", null,startRow,endRow);
            jsonObject.put("data", tableService.selectSqlMapList(sqldangqun));
        } catch (Exception e) {
            e.getLocalizedMessage();
            jsonObject.put("success", 0);
            jsonObject.put("msg", "信息获取失败");
        }
        return jsonObject.toString();
    }

    public String CalculateTime(String time) {
        long nowTime = System.currentTimeMillis(); // 获取当前时间的毫秒数
        String msg = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 指定时间格式
        Date setTime = null; // 指定时间
        try {
            setTime = sdf.parse(time); // 将字符串转换为指定的时间格式
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long reset = setTime.getTime(); // 获取指定时间的毫秒数
        long dateDiff = nowTime - reset;
        if (dateDiff < 0) {
            msg = "输入的时间不对";
        } else {
            long dateTemp1 = dateDiff / 1000; // 秒
            long dateTemp2 = dateTemp1 / 60; // 分钟
            long dateTemp3 = dateTemp2 / 60; // 小时
            long dateTemp4 = dateTemp3 / 24; // 天数
            long dateTemp5 = dateTemp4 / 30; // 月数
            long dateTemp6 = dateTemp5 / 12; // 年数
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
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 0);
        jsonObject.put("msg", "");
        jsonObject.put("count", count);
        jsonObject.put("data", taskSenderList);
        jsonObject.put("success", 1);
        return jsonObject.toString();
    }
}
