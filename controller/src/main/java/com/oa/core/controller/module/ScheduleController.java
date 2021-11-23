package com.oa.core.controller.module;

import com.oa.core.bean.Loginer;
import com.oa.core.bean.module.Schedule;
import com.oa.core.bean.system.AttCalendar;
import com.oa.core.bean.user.UserManager;
import com.oa.core.bean.util.PageUtil;
import com.oa.core.helper.DateHelper;
import com.oa.core.helper.StringHelper;
import com.oa.core.interceptor.Logined;
import com.oa.core.service.dd.DictionaryService;
import com.oa.core.service.module.FestivalService;
import com.oa.core.service.module.MessageService;
import com.oa.core.service.module.ScheduleService;
import com.oa.core.service.system.FormCustomMadeService;
import com.oa.core.service.system.MyUrlRegistService;
import com.oa.core.service.user.UserManagerService;
import com.oa.core.service.util.TableService;
import com.oa.core.util.*;
import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @ClassName:ScheduleController
 * @author:zxd
 * @Date:2018/11/19
 * @Time:下午 4:24
 * @Version V1.0
 * @Explain 作息时间、例外日历、考勤
 */
@Controller
@RequestMapping("/schedule")
public class ScheduleController {
    @Autowired
    MyUrlRegistService myUrlRegistService;
    @Autowired
    FormCustomMadeService formCustomMadeService;
    @Autowired
    DictionaryService dictionaryService;
    @Autowired
    TableService tableService;
    @Autowired
    FestivalService festivalService;
    @Autowired
    ScheduleService scheduleService;
    @Autowired
    UserManagerService userManagerService;

    @RequestMapping(value = "/gotoworkingcalendar", method = RequestMethod.GET)
    public ModelAndView gotoWorkingCalendar(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("module/workingcalendar");
        return mav;
    }

    /**
     * 查看台账信息
     *
     * @param request
     * @param year
     * @param month
     * @param day
     * @return
     */
    @RequestMapping(value = "/gotoDetailData", method = RequestMethod.GET)
    public ModelAndView gotoDetailData(HttpServletRequest request, String year, String month,String day,String name) {
        ModelAndView mav = new ModelAndView("module/detail");
        mav.addObject("year", year);
        mav.addObject("month", month);
        mav.addObject("day", day);
        mav.addObject("name", name);
        mav.addObject("flag", "flag");
        return mav;
    }

    /**
     * 查看台账信息
     *
     * @param request
     * @param flag
     * @return
     */
    @RequestMapping(value = "/gotoDetailDataold", method = RequestMethod.GET)
    public ModelAndView gotoDetailDataold(HttpServletRequest request, String flag, String id) {
        ModelAndView mav = new ModelAndView("module/detail");
        mav.addObject("flag", flag);
        mav.addObject("id", id);
        return mav;
    }

    @RequestMapping(value = "/getDetailList")
    @ResponseBody
    public String getDetailList(HttpServletRequest request, String year, String month,String day,String name) {
        String ymd = "";
        if (year != null && month != null && day != null) {
            if (month.length() < 2) {
                month = "0" + month;
            }
            if (day.length() < 2) {
                day = "0" + month;
            }
            ymd = year + "-" + month + "-" + day;
        }
        //获取规定的签到时间值
        ConfParseUtil cpu = new ConfParseUtil();
        String amtoworku = cpu.getSchedule("amtoworku");
        String amtowork = cpu.getSchedule("amtowork");
        String amoffwork = cpu.getSchedule("amoffwork");
        String amoffworkd = cpu.getSchedule("amoffworkd");
        String pmtoworku = cpu.getSchedule("pmtoworku");
        String pmtowork = cpu.getSchedule("pmtowork");
        String pmoffwork = cpu.getSchedule("pmoffwork");
        String pmoffworkd = cpu.getSchedule("pmoffworkd");
        String[] time = new String[]{ymd + " " + amtoworku, ymd + " " + amtowork, ymd + " " + amoffwork, ymd + " " + amoffworkd, ymd + " " + pmtoworku, ymd + " " + pmtowork, ymd + " " + pmoffwork, ymd + " " + pmoffworkd};
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT ");
        sql.append("(SELECT DISTINCT sbry190720001 FROM rlsbj19072001 WHERE curStatus=2 AND sbry190720001 = '"+name+"' ) AS name,");
        sql.append("IFNULL((SELECT MIN(sbsj190720001) FROM rlsbview WHERE curStatus=2 AND sbry190720001='"+name+"' AND sbsj190720001 BETWEEN '" + time[0] + "' AND '" + time[1] + "'),'') AS swsb,");
        sql.append("IFNULL((SELECT MIN(sbsj190720001) FROM rlsbview WHERE curStatus=2 AND sbry190720001='"+name+"' AND sbsj190720001 BETWEEN '" + time[1] + "' AND '" + time[2] + "'),'') AS swsbh,");
        sql.append("IFNULL((SELECT MAX(sbsj190720001) FROM rlsbview WHERE curStatus=2 AND sbry190720001='"+name+"' AND sbsj190720001 BETWEEN '" + time[1] + "' AND '" + time[2] + "'),'') AS swxbq,");
        sql.append("IFNULL((SELECT MIN(sbsj190720001) FROM rlsbview WHERE curStatus=2 AND sbry190720001='"+name+"' AND sbsj190720001 BETWEEN '" + time[2] + "' AND '" + time[3] + "'),'') AS swxb,");
        sql.append("IFNULL((SELECT MIN(sbsj190720001) FROM rlsbview WHERE curStatus=2 AND sbry190720001='"+name+"' AND sbsj190720001 BETWEEN '" + time[4] + "' AND '" + time[5] + "'),'') AS xwsb,");
        sql.append("IFNULL((SELECT MIN(sbsj190720001) FROM rlsbview WHERE curStatus=2 AND sbry190720001='"+name+"' AND sbsj190720001 BETWEEN '" + time[5] + "' AND '" + time[6] + "'),'') AS xwsbh,");
        sql.append("IFNULL((SELECT MAX(sbsj190720001) FROM rlsbview WHERE curStatus=2 AND sbry190720001='"+name+"' AND sbsj190720001 BETWEEN '" + time[5] + "' AND '" + time[6] + "'),'') AS xwxbq,");
        sql.append("IFNULL((SELECT MAX(sbsj190720001) FROM rlsbview WHERE curStatus=2 AND sbry190720001='"+name+"' AND sbsj190720001 BETWEEN '" + time[6] + "' AND '" + time[7] + "'),'') AS xwxb");
        sql.append(" FROM dual;");
        Map<String, Object> map = tableService.selectSqlMap(sql.toString());
        String xm = ToNameUtil.getName("user", name);

        map.put("xm",xm);
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        jsonArray.put(0,map);
        jsonObject.put("code", 0);
        jsonObject.put("msg", "");
        jsonObject.put("count", 1);
        jsonObject.put("data", jsonArray);
        jsonObject.put("success", 1);
        return jsonObject.toString();
    }

    @RequestMapping(value = "/getDetailListold")
    @ResponseBody
    public String getDetailListold(HttpServletRequest request, String id, String flag) {
        String sql = "";
        if ("isLeave".equals(flag)) {
            sql = "select * from\n" +
                    "(select * ,IFNULL(sjkss18112001,jhkss18112001) startTime,\n" +
                    "                IFNULL(sjjss18112001,jhjss18112001) endTime\n" +
                    "from qjsqt18112001) a where recorderNO = '" + id + "' ";
        } else if ("isOut".equals(flag)) {
            sql = "select * from wcsqt18112001 where recorderNO = '" + id + "'";
        } else if ("recorderNO".equals(flag)) {
            sql = "select * from checkview where recorderNO = '" + id + "'";
        }
        List<Map<String, Object>> list = tableService.selectSqlMapList(sql);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 0);
        jsonObject.put("msg", "");
        jsonObject.put("count", list.size());
        jsonObject.put("data", list);
        jsonObject.put("success", 1);
        return jsonObject.toString();
    }

    /**
     * 查询所有人的考勤记录即公司月考勤记录
     *
     * @param year
     * @param month
     * @return
     */
    @RequestMapping(value = "/getCheckListByMonthAll")
    @ResponseBody
    public String getCheckListByMonthAll(String year, String month) {
        //1.先将所有人的信息查出来,用来通过姓名去查询每个人的签到情况
        List<UserManager> userManagers = userManagerService.selectAll();
        //2.定义查询条件,将其装到map中
        Map<String, Object> map = new HashMap<>();
        SimpleDateFormat df1 = new SimpleDateFormat("yyyy");
        SimpleDateFormat df2 = new SimpleDateFormat("MM");
        if (StringUtils.isBlank(year)) {
            year = df1.format(new Date());
        }
        if (StringUtils.isBlank(month)) {
            month = df2.format(new Date());
        }
        map.put("year", year);
        map.put("month", month);
        //定义返回值
        List<Map<String, Object>> data = new ArrayList<>();
        for (UserManager userManager1 : userManagers) {
            String userId = userManager1.getUserName();
            List<Map<String, Object>> list = bbb(year, month, userId);
            Map<String, Object> newmap = newmap(userId);
            int day = 1;
            for (int i = 0; i < list.size(); i++, day++) {
                Map<String, Object> umap = list.get(i);
                String recorderNO = (String) umap.get("recorderNO");
                String swsb = (String) umap.get("swsb181120001");
                String swxb = (String) umap.get("swxb181120001");
                String xwsb = (String) umap.get("xwsb181120001");
                String xwxb = (String) umap.get("xwxb181120001");
                /*Integer isLeave = (Integer) umap.get("isLeave");
                *//*Integer isOut = (Integer)umap.get("isOut");
                Integer isCard = (Integer)umap.get("isCard");*/
                if (!(("").equals(swsb)) && !(("").equals(xwxb)) && swsb != null && xwxb != null) {
                    newmap.put("DAY_" + day, "1");
                } else if ((!(("").equals(swsb)) && !(("").equals(swxb)) && swsb != null && swxb != null) || (!(("").equals(xwsb)) && !(("").equals(xwxb)) && xwsb != null && xwxb != null)) {
                    newmap.put("DAY_" + day, "0.5");
                } else if ((!(("").equals(swsb)) && ("").equals(swxb) && ("").equals(xwxb)) || (swsb != null && swxb == null && xwxb == null)) {
                    newmap.put("DAY_" + day, "红1");
                } else if (((!("").equals(swsb) && ("").equals(swxb)) || (swsb != null && swxb == null)) || ((!("").equals(xwsb) && ("").equals(xwxb)) || (xwsb != null && xwxb == null))) {
                    newmap.put("DAY_" + day, "红.5");
                }
                newmap.put("RECNO_" + day, recorderNO);
            }
            data.add(newmap);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 0);
        jsonObject.put("msg", "");
        jsonObject.put("count", data.size());
        jsonObject.put("data", data);
        jsonObject.put("success", 1);
        return jsonObject.toString();
    }

    /**
     * 查询所有人的考勤记录即公司月考勤记录  变更后
     *
     * @param year
     * @param month
     * @return
     */
    @RequestMapping(value = "/getCheckListByMonthAll2")
    @ResponseBody
    public String getCheckListByMonthAll2(String year, String month) {
        //1.先将所有人的信息查出来,用来通过姓名去查询每个人的签到情况
        List<UserManager> userManagers = userManagerService.selectAll();
        //2.定义查询条件,将其装到map中
        Map<String, Object> map = new HashMap<>();
        SimpleDateFormat df1 = new SimpleDateFormat("yyyy");
        SimpleDateFormat df2 = new SimpleDateFormat("MM");
        if (StringUtils.isBlank(year)) {
            year = df1.format(new Date());
        }
        if (StringUtils.isBlank(month)) {
            month = df2.format(new Date());
        }
        map.put("year", year);
        map.put("month", month);
        //定义返回值
        List<Map<String, Object>> data = new ArrayList<>();
        for (UserManager userManager1 : userManagers) {
            String userId = userManager1.getUserName();
            List<Map<String, Object>> list = aaa(year, month, userId);
            Map<String, Object> newmap = newmap(userId);
            newmap.put("nian", year);
            newmap.put("yue", month);
            for (int i = 0; i < list.size(); i++) {
                Map<String, Object> umap = list.get(i);
                String datas = (String)umap.get("datas");//数据
                String ri = (String)umap.get("day");//日
                if (datas != null && datas != "" && datas.length() > 0) {
                    int day = Integer.parseInt(ri);
                    newmap.put("DAY_" + day, datas);
                    newmap.put("ri_" + day, ri);
                }
            }
            data.add(newmap);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 0);
        jsonObject.put("msg", "");
        jsonObject.put("count", data.size());
        jsonObject.put("data", data);
        jsonObject.put("success", 1);
        return jsonObject.toString();
    }

    private Map<String, Object> newmap(String name) {
        Map<String, Object> map = new HashMap<>();
        String xm = ToNameUtil.getName("user", name);
        map.put("xm", xm);
        map.put("name", name);
        for (int i = 1; i <= 31; i++) {
            map.put("DAY_" + i, "");
            map.put("ri_" + i, "");
        }
        return map;
    }

    /**
     * 跳转到个人考勤查看页面
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/gotoCheckList", method = RequestMethod.GET)
    public ModelAndView gotogsykqji(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("module/check");
        return mav;
    }
    /**
     * 查询考勤记录
     *
     * @return
     */
    @RequestMapping(value = "/getCheckListByMonth")
    @ResponseBody
    public String getCheckListByMonth(HttpServletRequest request, String year, String month) {
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        String userId = loginer.getId();
        List<Map<String, Object>> data = new ArrayList<>();
        if(year==null || year.equals("")){
            year = DateHelper.getYear();
        }
        if(month==null || month.equals("")){
            month = DateHelper.getMonth();
        }
        List<Map<String, Object>> list = aaa(year, month, userId);
        Map<String, Object> newmap = newmap(userId);
        newmap.put("nian", year);
        newmap.put("yue", month);
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> umap = list.get(i);
            String datas = (String)umap.get("datas");
            String ri = (String)umap.get("day");
            if(datas != null && datas != "" && datas.length()>0){
                int day = Integer.parseInt(ri);
                newmap.put("DAY_" + day, datas);
                newmap.put("ri_" + day, ri);
            }
        }
        data.add(newmap);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 0);
        jsonObject.put("msg", "");
        jsonObject.put("count", list.size());
        jsonObject.put("data", data);
        jsonObject.put("success", 1);
        return jsonObject.toString();
    }

    @RequestMapping(value = "/getCheckListByMonth2")
    @ResponseBody
    public String getCheckListByMonth2(HttpServletRequest request, String year, String month) {
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        String userId = loginer.getId();
        List<Map<String, Object>> data = getUserKQ(tableService,year,month,userId);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 0);
        jsonObject.put("msg", "");
        jsonObject.put("count", data.size());
        jsonObject.put("data", data);
        jsonObject.put("success", 1);
        return jsonObject.toString();
    }

    public static List<Map<String, Object>> getUserKQ(TableService tableService,String year,String month,String userId){
        List<Map<String, Object>> data = new ArrayList<>();
        ConfParseUtil cpu = new ConfParseUtil();
        String amtoworku = cpu.getSchedule("amtoworku");
        String amtowork = cpu.getSchedule("amtowork");
        String amoffwork = cpu.getSchedule("amoffwork");
        String amoffworkd = cpu.getSchedule("amoffworkd");
        String pmtoworku = cpu.getSchedule("pmtoworku");
        String pmtowork = cpu.getSchedule("pmtowork");
        String pmoffwork = cpu.getSchedule("pmoffwork");
        String pmoffworkd = cpu.getSchedule("pmoffworkd");
        if(year==null || year.equals("")){
            year = DateHelper.getYear();
        }
        if(month==null || month.equals("")){
            month = DateHelper.getMonth();
        }
        /*for(int i = 1;i <= 31;i++){
            String ymd = "";
            String days = "";
            if (year != null && month != null) {
                if (month.length() < 2) {
                    month = "0" + month;
                }
                if (i < 10) {
                    days = "0" + i;
                }else{
                    days = ""+i;
                }
                ymd = year + "-" + month + "-" + days;
            }
            String[] time = new String[]{ymd + " " + amtoworku, ymd + " " + amtowork, ymd + " " + amoffwork, ymd + " " + amoffworkd, ymd + " " + pmtoworku, ymd + " " + pmtowork, ymd + " " + pmoffwork, ymd + " " + pmoffworkd};
            StringBuffer sql = new StringBuffer();
            sql.append("SELECT ");
            sql.append("(SELECT DISTINCT sbry190720001 FROM rlsbview WHERE curStatus=2 AND sbry190720001 = '"+userId+"' ) AS name,");
            sql.append("(SELECT '"+days+"') AS day,");
            sql.append("IFNULL((SELECT MIN(sbsj190720001) FROM rlsbview WHERE curStatus=2 AND sbry190720001='"+userId+"' AND sbsj190720001 BETWEEN '" + time[0] + "' AND '" + time[1] + "'),'') AS swsb,");
            sql.append("IFNULL((SELECT MIN(sbsj190720001) FROM rlsbview WHERE curStatus=2 AND sbry190720001='"+userId+"' AND sbsj190720001 BETWEEN '" + time[1] + "' AND '" + time[2] + "'),'') AS swsbh,");
            sql.append("IFNULL((SELECT MAX(sbsj190720001) FROM rlsbview WHERE curStatus=2 AND sbry190720001='"+userId+"' AND sbsj190720001 BETWEEN '" + time[1] + "' AND '" + time[2] + "'),'') AS swxbq,");
            sql.append("IFNULL((SELECT MIN(sbsj190720001) FROM rlsbview WHERE curStatus=2 AND sbry190720001='"+userId+"' AND sbsj190720001 BETWEEN '" + time[2] + "' AND '" + time[3] + "'),'') AS swxb,");
            sql.append("IFNULL((SELECT MIN(sbsj190720001) FROM rlsbview WHERE curStatus=2 AND sbry190720001='"+userId+"' AND sbsj190720001 BETWEEN '" + time[4] + "' AND '" + time[5] + "'),'') AS xwsb,");
            sql.append("IFNULL((SELECT MIN(sbsj190720001) FROM rlsbview WHERE curStatus=2 AND sbry190720001='"+userId+"' AND sbsj190720001 BETWEEN '" + time[5] + "' AND '" + time[6] + "'),'') AS xwsbh,");
            sql.append("IFNULL((SELECT MAX(sbsj190720001) FROM rlsbview WHERE curStatus=2 AND sbry190720001='"+userId+"' AND sbsj190720001 BETWEEN '" + time[5] + "' AND '" + time[6] + "'),'') AS xwxbq,");
            sql.append("IFNULL((SELECT MAX(sbsj190720001) FROM rlsbview WHERE curStatus=2 AND sbry190720001='"+userId+"' AND sbsj190720001 BETWEEN '" + time[6] + "' AND '" + time[7] + "'),'') AS xwxb");
            sql.append(" FROM dual;");
            Map<String, Object> umaps = tableService.selectSqlMap(sql.toString());
            data.add(umaps);
        }*/
        data = tableService.selectSqlMapList("call select_userkq('" + userId + "','" + year + "','" + month + "','" + amtoworku + "','" + amtowork + "','" + amoffwork + "','" + amoffworkd + "','" + pmtoworku + "','" + pmtowork + "','" + pmoffwork + "','" + pmoffworkd + "');");
        return data;
    }

    /**
     * 查询考勤记录
     *
     * @return
     */
    @RequestMapping(value = "/getCheckListByMonthold")
    @ResponseBody
    public String getCheckListByMonthold(HttpServletRequest request, String year, String month) {
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        String userId = loginer.getId();
        List<Map<String, Object>> list = bbb(year, month, userId);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 0);
        jsonObject.put("msg", "");
        jsonObject.put("count", list.size());
        jsonObject.put("data", list);
        jsonObject.put("success", 1);
        return jsonObject.toString();
    }

    private Map<String, Object> newmap2(String names) {
        Map<String, Object> map = new HashMap<>();
        String name = ToNameUtil.getName("user", names);
        map.put("name", name);
        for (int i = 1; i <= 31; i++) {
            map.put("num_" + i, "");
            map.put("swsb_" + i, "");
            map.put("swxb_" + i, "");
            map.put("xwsb_" + i, "");
            map.put("xwxb_" + i, "");
        }
        return map;
    }

    public List<Map<String, Object>> aaa(String year, String month, String userId) {
        Map<String, Object> map = new HashMap<>();
        SimpleDateFormat df1 = new SimpleDateFormat("yyyy");
        SimpleDateFormat df2 = new SimpleDateFormat("MM");
        if (StringUtils.isBlank(year)) {
            year = df1.format(new Date());
        }
        if (StringUtils.isBlank(month)) {
            month = df2.format(new Date());
        }
        map.put("userId", userId);
        map.put("year", year);
        map.put("month", month);
        //1.查询考勤列表
        List<Map<String, Object>> list1 = tableService.selectCheckList(map);       //所有条数
        //2.查询请假列表
        String sql2 = "select recorderNO recorderNOQJ,IFNULL(sjkss18112001,jhkss18112001) startTime,\n" +
                "IFNULL(sjjss18112001,jhjss18112001) endTime\n" +
                "from qjsqt18112001 where qjr1811200001 = '" + userId + "'";
        List<Map<String, Object>> list2 = tableService.selectSqlMapList(sql2);
        //存储请假集合
       /* Set<String> set2 = new HashSet<>();
        if (list2 != null && list2.size() > 0) {
            formateData(list1, list2, set2, "isLeave", year, month);
        }*/
        //3.查询外出列表
        /*String sql3 = "select recorderNO recorderNOWCR,kssj181120001 startTime, jssj181120001 endTime from\n" +
                "(select recorderNO,kssj181120001,jssj181120001,wcr1811200001 wcr from wcsqt18112001 \n" +
                "where wcr1811200001 = '"+userId+"'\n" +
                "union all\n" +
                "select recorderNO,\n" +
                "kssj181120001,jssj181120001,\n" +
                "substring_index(substring_index(a.txr1811200001,';',b.help_topic_id+1),';',0) as wcr\n" +
                "from wcsqt18112001 a\n" +
                "join mysql.help_topic b \n" +
                "on b.help_topic_id < (length(a.txr1811200001) - length(replace(a.txr1811200001,';','')))) aa where wcr = '"+userId+"' ";*/
        String sql3 = "select recorderNO recorderNOWCR, kssj181120001 startTime, jssj181120001 endTime  from wcsqt18112001 where  (wcr1811200001 = '" + userId + "' or locate(\"" + userId + "\",txr1811200001))";
        List<Map<String, Object>> list3 = tableService.selectSqlMapList(sql3);
        //存储外出集合
        /*Set<String> set3 = new HashSet<>();
        if (list3 != null && list3.size() > 0) {
            formateData(list1, list3, set3, "isOut", year, month);
        }*/
        return list1;
    }

    public List<Map<String, Object>> bbb(String year, String month, String userId) {
        Map<String, Object> map = new HashMap<>();
        SimpleDateFormat df1 = new SimpleDateFormat("yyyy");
        SimpleDateFormat df2 = new SimpleDateFormat("MM");
        if (StringUtils.isBlank(year)) {
            year = df1.format(new Date());
        }
        if (StringUtils.isBlank(month)) {
            month = df2.format(new Date());
        }
        map.put("userId", userId);
        map.put("year", year);
        map.put("month", month);
        //1.查询考勤列表
        List<Map<String, Object>> list1 = tableService.selectCheckListold(map);       //所有条数
        //2.查询请假列表
        String sql2 = "select recorderNO recorderNOQJ,IFNULL(sjkss18112001,jhkss18112001) startTime,\n" +
                "IFNULL(sjjss18112001,jhjss18112001) endTime\n" +
                "from qjsqt18112001 where qjr1811200001 = '" + userId + "'";
        List<Map<String, Object>> list2 = tableService.selectSqlMapList(sql2);
        //存储请假集合
        Set<String> set2 = new HashSet<>();
        if (list2 != null && list2.size() > 0) {
            formateData(list1, list2, set2, "isLeave", year, month);
        }
        //3.查询外出列表
        /*String sql3 = "select recorderNO recorderNOWCR,kssj181120001 startTime, jssj181120001 endTime from\n" +
                "(select recorderNO,kssj181120001,jssj181120001,wcr1811200001 wcr from wcsqt18112001 \n" +
                "where wcr1811200001 = '"+userId+"'\n" +
                "union all\n" +
                "select recorderNO,\n" +
                "kssj181120001,jssj181120001,\n" +
                "substring_index(substring_index(a.txr1811200001,';',b.help_topic_id+1),';',0) as wcr\n" +
                "from wcsqt18112001 a\n" +
                "join mysql.help_topic b \n" +
                "on b.help_topic_id < (length(a.txr1811200001) - length(replace(a.txr1811200001,';','')))) aa where wcr = '"+userId+"' ";*/
        String sql3 = "select recorderNO recorderNOWCR, kssj181120001 startTime, jssj181120001 endTime  from wcsqt18112001 where  (wcr1811200001 = '" + userId + "' or locate(\"" + userId + "\",txr1811200001))";
        List<Map<String, Object>> list3 = tableService.selectSqlMapList(sql3);
        //存储外出集合
        Set<String> set3 = new HashSet<>();
        if (list3 != null && list3.size() > 0) {
            formateData(list1, list3, set3, "isOut", year, month);
        }
        return list1;
    }

    @RequestMapping(value = "/getAllUserDate")
    @ResponseBody
    public Map<String, Map<String,String>> getAllUserDate(String year, String month) {

        String sql1 = "select jlr1811200001 as jlr,swsb181120001 as swsb,swxb181120001 as swxb,xwsb181120001 as xwsb,xwxb181120001 as xwxb from ygkqj18112001 where curStatus=2 and (YEAR(swsb181120001) = " + year + " and MONTH(swsb181120001) = " + month + " OR YEAR(xwxb181120001) = " + year + " and MONTH(xwxb181120001) = " + month + ")";
        List<Map<String, Object>> list1 = tableService.selectSqlMapList(sql1);

        Map<String, Map<String,AttCalendar>> maps = getUserDateMap(year, month);
        if(maps!=null) {
            for (int i = 0, len = list1.size(); i < len; i++) {
                Map<String, Object> map = list1.get(i);
                String jlr = (String) map.get("jlr");
                String swsb = map.get("swsb") == null ? "" : map.get("swsb") + "";
                String swxb = map.get("swxb") == null ? "" : map.get("swxb") + "";
                String xwsb = map.get("xwsb") == null ? "" : map.get("xwsb") + "";
                String xwxb = map.get("xwxb") == null ? "" : map.get("xwxb") + "";
                String date = getAttDate(swsb, swxb, xwsb, xwxb);
                Map<String, AttCalendar> la = maps.get(jlr);
                if (la != null) {
                    AttCalendar attc = la.get(date);
                    attc.setSwsb(swsb);
                    attc.setSwxb(swxb);
                    attc.setXwsb(xwsb);
                    attc.setXwxb(xwxb);
                    la.put(date, attc);
                    maps.put(jlr, la);
                }
            }
        }

        Map<String,Map<String,String>> ret = new HashMap<>();
        Set<Map.Entry<String, Map<String, AttCalendar>>> entries = maps.entrySet();
        for (Map.Entry entry : entries) {
            Map<String,String> date = new HashMap<>();
            String user = (String)entry.getKey();
            Map<String, AttCalendar> map = (Map<String, AttCalendar>) entry.getValue();

            List<Map<String, Object>> maps1 = tableService.selectSqlMapList(getSqlN(user, year, month));
            Set<Map.Entry<String, AttCalendar>> set = map.entrySet();
            int i=0;
            for(Map.Entry entryd : set){
                String key = (String)entryd.getKey();
                AttCalendar tc = (AttCalendar)entryd.getValue();
                if(i<maps1.size()) {
                    Map<String, Object> mapv = maps1.get(i);
                    if (mapv != null) {
                        tc.setWc(String.valueOf(mapv.get("wai")));
                        tc.setQj(String.valueOf(mapv.get("jia")));
                        tc.setBk(String.valueOf(mapv.get("bu")));
                    }
                    i++;
                }

                date.put(key,tc.getType());
            }
            ret.put(user,date);
        }

        return ret;
    }

    public String getSqlN(String user,String year,String month){
        String date = year+"-"+month+"-01";
        String sql = "select c.date as daye,EXISTS(SELECT '1' FROM festival WHERE  c.date between startTime and endTime) as xiu,EXISTS(SELECT '1' FROM wcsqt18112001 WHERE curStatus=2 and c.date between DATE_FORMAT(kssj181120001, \"%Y-%m-%d\") and DATE_FORMAT(kssj181120001, \"%Y-%m-%d\") and (wcr1811200001 = '"+user+"' or locate('"+user+"',txr1811200001))) as wai,EXISTS(SELECT '1' FROM qjsqt18112001 WHERE curStatus=2 and c.date between DATE_FORMAT(jhkss18112001, \"%Y-%m-%d\") and DATE_FORMAT(jhjss18112001, \"%Y-%m-%d\") and qjr1811200001 = '"+user+"' ) as jia,EXISTS(SELECT '1' FROM bqlct19020101 WHERE curStatus=2 and c.date = DATE_FORMAT(bqsj190201001, \"%Y-%m-%d\") and sqr1902010001 = '"+user+"' ) as bu from (SELECT date_add( '"+date+"', INTERVAL ( cast( help_topic_id AS signed INTEGER ) ) DAY ) as date FROM mysql.help_topic WHERE help_topic_id < DAY ( last_day( '"+date+"' ) )) c";

        return sql;
    }

    public Map<String, Map<String,AttCalendar>> getUserDateMap(String year, String month){
        String sql = "select userName from userInfo";
        List<String> userNames = tableService.selectSql(sql);;
        Map<String, Map<String,AttCalendar>> maps = new HashMap<>();
        for(String user : userNames){
            Map<String,AttCalendar> mapdate= new Hashtable<>();
            for(int i=1;i<=31;i++){
                String day = String.valueOf(i);
                if(i<10){
                    day = "0"+i;
                }
                String date = year+"-"+month+"-"+day;
                mapdate.put(date,new AttCalendar());
            }
            maps.put(user,mapdate);
        }
        return maps;
    }

    public String getAttDate(String swsb, String swxb, String xwsb, String xwxb) {
        String date = "";
        if (swsb != null && swsb.length()>=10) {
            date = swsb.substring(0,10);
        } else if (swxb != null && swxb.length()>=10) {
            date = swxb.substring(0,10);
        } else if (xwsb != null && xwsb.length()>=10) {
            date = xwsb.substring(0,10);
        } else if (xwxb != null && xwxb.length()>=10) {
            date = xwxb.substring(0,10);
        }
        return date;
    }

    /**
     * 判断是否有请假 外出
     *
     * @param list1 考勤集合
     * @param listx 请假 外出 补卡 集合
     * @param setx  日期集合
     * @param flag
     */
    public void formateData(List<Map<String, Object>> list1, List<Map<String, Object>> listx, Set<String> setx, String flag, String year, String month) {
        //key 是请假日期  value为请假人id
        Map<String, String> map = new HashMap<>();
        for (Map<String, Object> mapx : listx) {
            String startTime = mapx.get("startTime").toString();
            String endTime = mapx.get("endTime").toString();
            String idValue = "";
            if ("isLeave".equals(flag)) {
                idValue = mapx.get("recorderNOQJ").toString();
            } else if ("isOut".equals(flag)) {
                idValue = mapx.get("recorderNOWCR").toString();
            }
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date dBegin = df.parse(startTime);
                Date dEnd = df.parse(endTime);
                setx = findDates(setx, dBegin, dEnd, map, idValue);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        //将map中的请假日期和外出日期添加到list1中对应的位置
        if (list1 != null && map != null && list1.size() > 0) {
            for (Map<String, Object> map1 : list1) {
                for (String key : map.keySet()) {
                    String num = map1.get("num").toString();
                    num = num.substring(0, num.indexOf("."));//截取.之前的字符串
                    String ri = key.substring(8, key.length());
                    String nian = key.substring(0, 4);
                    String yue = key.substring(5, 7);
                    if (Integer.parseInt(nian, 10) == Integer.parseInt(year, 10)) {
                        if (Integer.parseInt(yue, 10) == Integer.parseInt(month, 10)) {
                            if (Integer.parseInt(num, 10) == Integer.parseInt(ri, 10)) {
                                map1.put("date", key);
                            }
                        }
                    }
                }
            }
        }
        //循环考勤列表  含有请假的或者外出的 替换成1
        for (Map<String, Object> map1 : list1) {
            if (map1.get("date") == null) {
                continue;
            }
            String date = map1.get("date").toString();
            if (setx.contains(date)) {
                map1.put(flag, "1");
                if ("isLeave".equals(flag)) {
                    map1.put("recorderNOQJ", map.get(date));
                } else if ("isOut".equals(flag)) {
                    map1.put("recorderNOWCR", map.get(date));
                }
            }
        }
    }

    /**
     * 根据时间范围  计算日期
     *
     * @param set2
     * @param dBegin
     * @param dEnd
     * @return
     */
    public Set<String> findDates(Set<String> set2, Date dBegin, Date dEnd, Map<String, String> map, String value) {
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
        set2.add(sd.format(dBegin));
        map.put(sd.format(dBegin), value);
        Calendar calBegin = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的开始时间
        calBegin.setTime(dBegin);
        Calendar calEnd = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的结束时间
        calEnd.setTime(dEnd);
        // 测试此日期是否在指定日期之后
        while (dEnd.after(calBegin.getTime())) {
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
            calBegin.add(Calendar.DAY_OF_MONTH, 1);
            set2.add(sd.format(calBegin.getTime()));
            map.put(sd.format(calBegin.getTime()), value);
        }
        return set2;
    }

    @RequestMapping(value = "/getworkingcalendar", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String getWorkingCalendar(HttpServletRequest request, int year, int month) {
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        String userId = loginer.getId();
        JSONObject json = new JSONObject(true);
        List<Map<String, Object>> sqlval = new ArrayList<>();

        //休息日数据获取
        sqlval = festivalService.getAllByYearAndMonth(year, month);
        Hashtable<String, String> date = new Hashtable<>();
        date.putAll(ScheduleUtil.findDayOffs(year, month));
        json = ScheduleUtil.getjson(json, date, "rest");

        //节假日数据获取
        sqlval = festivalService.getAllByYearAndMonth(year, month);
        Hashtable<String, String> date0 = new Hashtable<>();
        for (Map<String, Object> map : sqlval) {
            String startTime = String.valueOf(map.get("startTime"));
            String endTime = String.valueOf(map.get("endTime"));
            String type = String.valueOf(map.get("festivalName"));
            date0.putAll(ScheduleUtil.findDates(DateHelper.getSqlDateTime(startTime), DateHelper.getSqlDateTime(endTime), type, month));
        }
        json = ScheduleUtil.getjson(json, date0, "rest");

        //请假台账数据获取
        List<String> where = new ArrayList<>();
        where.add("qjr1811200001='" + userId + "'");
        where.add("(YEAR(jhkss18112001) = " + year + " and MONTH(jhkss18112001) = " + month + " OR YEAR(jhjss18112001) = " + year + " and MONTH(jhjss18112001) = " + month + ")");
        sqlval = ScheduleUtil.getValue("qjsqt18112001", where);
        Hashtable<String, String> date1 = new Hashtable<>();
        for (Map<String, Object> map : sqlval) {
            String kssj181120001 = String.valueOf(map.get("jhkss18112001"));
            String jssj181120001 = String.valueOf(map.get("jhjss18112001"));
            String qjlx181120001 = String.valueOf(map.get("qjlx181120001"));
            date1.putAll(ScheduleUtil.findDates(DateHelper.getSqlDateTime(kssj181120001), DateHelper.getSqlDateTime(jssj181120001), qjlx181120001, month));
        }
        json = ScheduleUtil.getjson(json, date1, "leave");

        //外出台账数据获取
        where = new ArrayList<>();
        where.add("(wcr1811200001='" + userId + "' OR txr1811200001 in ('%" + userId + "%'))");
        where.add("(YEAR(kssj181120001) = " + year + " and MONTH(kssj181120001) = " + month + " OR YEAR(jssj181120001) = " + year + " and MONTH(jssj181120001) = " + month + ")");
        sqlval = ScheduleUtil.getValue("wcsqt18112001", where);
        Hashtable<String, String> date2 = new Hashtable<>();
        for (Map<String, Object> map : sqlval) {
            String kssj181120001 = String.valueOf(map.get("kssj181120001"));
            String jssj181120001 = String.valueOf(map.get("jssj181120001"));
            date2.putAll(ScheduleUtil.findDates(DateHelper.getSqlDateTime(kssj181120001), DateHelper.getSqlDateTime(jssj181120001), "出差", month));
        }
        json = ScheduleUtil.getjson(json, date2, "out");

        //考勤台账数据获取
        where = new ArrayList<>();
        where.add("jlr1811200001='" + userId + "'");
        where.add("(YEAR(swsb181120001) = " + year + " and MONTH(swsb181120001) = " + month + " OR YEAR(xwxb181120001) = " + year + " and MONTH(xwxb181120001) = " + month + ")");
        sqlval = ScheduleUtil.getValue("ygkqj18112001", where);
        for (Map<String, Object> map : sqlval) {
            String swsb181120001 = String.valueOf(map.get("swsb181120001"));
            String xwxb181120001 = String.valueOf(map.get("xwxb181120001"));
            String day ="",am="",pm="";
            if( swsb181120001.length()>=19){
                 day = swsb181120001.substring(8, 10);
                 am = swsb181120001.substring(11, 19);
            }
            if (xwxb181120001.length()>=19){
                pm = xwxb181120001.substring(11, 19);
            }

            JSONObject j1 = new JSONObject(true);
            JSONObject j2 = new JSONObject(true);
            if (json.has(day)) {
                j2 = json.getJSONObject(day);
                j1 = j2.getJSONObject("value");
                j1.put("am", am);
                j1.put("pm", pm);
                j2.put("value", j1);
            } else {
                j1.put("exp", "工作");
                j1.put("am", am);
                j1.put("pm", pm);
                j2.put("type", "work");
                j2.put("value", j1);
            }
            json.put(day, j2);
        }
        //工作安排数据获取
        sqlval = new ArrayList<>();
        sqlval = getScheduleListByMonth(year, month, userId, sqlval);
        Hashtable<String, String> date3 = new Hashtable<>();
        List<String> list3 = new ArrayList<>();
        for (Map<String, Object> map : sqlval) {
            String startTime = String.valueOf(map.get("startTime"));
            String endTime = String.valueOf(map.get("endTime"));
            list3 = ScheduleUtil.findDates(DateHelper.getSqlDateTime(startTime), DateHelper.getSqlDateTime(endTime), month, list3);
            //date3.putAll(ScheduleUtil.findDates(DateHelper.getSqlDateTime(startTime),DateHelper.getSqlDateTime(endTime),"日程:",month));
        }
        for (int i = 0; i < list3.size(); i++) {
            String key = list3.get(i);
            if (date3.containsKey(key)) {
                String val = date3.get(key);
                date3.put(key, "日程:" + (Integer.parseInt(val.substring(3, val.length())) + 1));
            } else {
                date3.put(key, "日程:" + 1);
            }
        }
        json = ScheduleUtil.getjson(json, date3, "schedule");
        return json.toString();
    }

    /**
     * 获取日程数据
     *
     * @param year
     * @param month
     * @param userId
     * @param sqlval
     * @return
     */
    public List<Map<String, Object>> getScheduleListByMonth(int year, int month, String userId, List<Map<String, Object>> sqlval) {
        String sql = " select * from schedule \n" +
                "where user = '" + userId + "' and curStatus = 2\n" +
                "and DATE_FORMAT(startTime,'%Y-%c') <= '" + year + "-" + month + "'\n" +
                "and DATE_FORMAT(endTime,'%Y-%c') >= '" + year + "-" + month + "' ";
        sqlval = tableService.selectSqlMapList(sql);
        return sqlval;
    }

    @RequestMapping(value = "/gotoScheduleList", method = RequestMethod.GET)
    public ModelAndView gotoScheduleList(HttpServletRequest request, String type, String scheduleId) {
        ModelAndView mav = new ModelAndView("module/schedule");
        if (type.equals("edit")) {
            Schedule schedule = scheduleService.selectById(scheduleId);
            mav.addObject("schedule", schedule);
        }
        mav.addObject("type", type);
        String role = (String) request.getSession().getAttribute("role");
        mav.addObject("role", role);
        return mav;
    }

    @Logined
    @RequestMapping("/selectlist")
    @ResponseBody
    public String getScheduleList(HttpServletRequest request, String startTime, String endTime, int page, int limit) {
        PageUtil pu = new PageUtil();
        pu.setPageSize(limit);
        pu.setCurrentPage(page);
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        Schedule schedule = new Schedule();
        schedule.setUser(loginer.getId());
        schedule.setStartRow(pu.getStartRow());
        schedule.setEndRow(pu.getEndRow() - pu.getStartRow());
        schedule.setStartTime(startTime);
        schedule.setEndTime(endTime);
        int count = scheduleService.selectAllTermsCont(schedule);
        List<Schedule> scheduleList = scheduleService.selectAllTerms(schedule);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 0);
        jsonObject.put("msg", "");
        jsonObject.put("count", count);
        jsonObject.put("data", scheduleList);
        jsonObject.put("success", 1);
        jsonObject.put("is", true);
        jsonObject.put("tip", "操作成功");

        return jsonObject.toString();
    }

    @RequestMapping(value = "/insert", method = RequestMethod.POST, produces = {"text/html;charset=UTF-8;", "application/json;"})
    @ResponseBody
    public String insertSave(HttpServletRequest request, Schedule schedule) {
        String userid = request.getParameter("userid");
        if (userid == null || userid.equals("")) {
            Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
            userid = loginer.getId();
        }
        try {
            PrimaryKeyUitl pk = new PrimaryKeyUitl();
            String permissionId = pk.getNextId("schedule", "scheduleId");
            schedule.setScheduleId(permissionId);
            schedule.setUser(userid);
            schedule.setRecordName(userid);
            schedule.setModifyName(userid);
            schedule.setRecordTime(DateHelper.now());
            schedule.setModifyTime(DateHelper.now());
            scheduleService.insert(schedule);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("message", "保存成功!");
            jsonObject.put("success", 1);
            return jsonObject.toString();
        } catch (Exception e) {
            LogUtil.sysLog("Exception:" + e);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("message", "保存失败!");
            jsonObject.put("success", 0);
            return jsonObject.toString();
        }
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST, produces = {"text/html;charset=UTF-8;", "application/json;"})
    @ResponseBody
    public String updateSave(HttpServletRequest request, Schedule schedule) {
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        String userId = loginer.getId();
        try {
            schedule.setModifyName(userId);
            schedule.setModifyTime(DateHelper.now());
            scheduleService.update(schedule);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("message", "修改成功!");
            jsonObject.put("success", 1);
            return jsonObject.toString();
        } catch (Exception e) {
            LogUtil.sysLog("Exception:" + e);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("message", "修改失败!");
            jsonObject.put("success", 0);
            return jsonObject.toString();
        }
    }

    @RequestMapping(value = "/deletes", method = RequestMethod.POST, produces = {"text/html;charset=UTF-8;", "application/json;"})
    @ResponseBody
    public String deletes(HttpServletRequest request, String scheduleId) {
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        try {
            String userId = loginer.getId();
            scheduleId = scheduleId.replaceAll(";", "','");
            scheduleId = "'" + scheduleId.substring(0, scheduleId.length() - 2);
            scheduleService.deletes(scheduleId, userId, DateHelper.now());
            return "1";
        } catch (Exception e) {
            LogUtil.sysLog("Exception:" + e);
            return "0";
        }
    }

    //获取跟人每天的日程安排
    @RequestMapping("/getUserSchedule")
    @ResponseBody
    public List<Schedule> getUserSchedule(HttpServletRequest request, String date) {
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        Schedule schedule = new Schedule();
        schedule.setUser(loginer.getId());
        schedule.setStartTime(date + " 23:59:59");
        schedule.setEndTime(date);
        return scheduleService.getUserSchedule(schedule);
    }

    /**
    * @method: getKQexcel
    * @param: year:年
    * @param month:月
    * @return: 
    * @author: zxd
    * @date: 2019/10/15
    * @description: 员工月考勤信息导出
    */
    @RequestMapping(value = "/getkqexcel", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String getKQexcel(HttpServletRequest request, String year,String month){
        List<String> strings = tableService.selectSql("select xm19062100001 from afxtx19062101 where curStatus=2 and gh19062100001!=0");
        LinkedHashMap<String,Object> map = new LinkedHashMap<>();
        List<List<String>> list = new ArrayList<>();
        List<String> listTitle = new ArrayList<>();
        listTitle.add(0,"姓名");
        for(int a=1;a<=31;a++){
            listTitle.add(a,a+"号");
        }
        list.add(0,listTitle);
        for(int i=0,n=1,len=strings.size();i<len;i++,n++){
            List<Map<String, Object>> data = getUserKQ(tableService,year,month,strings.get(i));
            List<String> thislist = new ArrayList<>();
            int num = 0;
            thislist.add(num,String.valueOf(strings.get(i)));
            for(Map<String, Object> m : data){
                num ++;
                String swsb = String.valueOf(m.get("swsb"));
                String swsbh = String.valueOf(m.get("swsbh"));
                String swxbq = String.valueOf(m.get("swxbq"));
                String swxb = String.valueOf(m.get("swxb"));
                String xwsb = String.valueOf(m.get("xwsb"));
                String xwsbh = String.valueOf(m.get("xwsbh"));
                String xwxbq = String.valueOf(m.get("xwxbq"));
                String xwxb = String.valueOf(m.get("xwxb"));
                String val = swsb+" "+ swxb + " " + xwsb+" "+xwxb;
                thislist.add(num,val);
            }
            list.add(n,thislist);
        }
        map.put("员工月考勤信息",list);
        ConfParseUtil cp = new ConfParseUtil();
        String upload_file = cp.getProperty("upload_file") + "excel/exportTable/";
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        String date = df.format(new Date());
        String filePath = upload_file + "员工月考勤信息" + date + ".xls";
        ExcelUtil.exportTable(filePath, map);
        return "redirect:/userpage/downloadTable.do?file=" + filePath + "&fileName=员工月考勤信息";
    }
}
