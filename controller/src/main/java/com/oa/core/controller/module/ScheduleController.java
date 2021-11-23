package com.oa.core.controller.module;

import com.oa.core.bean.Loginer;
import com.oa.core.bean.module.Schedule;
import com.oa.core.bean.util.PageUtil;
import com.oa.core.helper.DateHelper;
import com.oa.core.interceptor.Logined;
import com.oa.core.service.dd.DictionaryService;
import com.oa.core.service.module.FestivalService;
import com.oa.core.service.module.ScheduleService;
import com.oa.core.service.system.FormCustomMadeService;
import com.oa.core.service.system.MyUrlRegistService;
import com.oa.core.service.util.TableService;
import com.oa.core.util.LogUtil;
import com.oa.core.util.PrimaryKeyUitl;
import com.oa.core.util.ScheduleUtil;
import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
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

    @RequestMapping(value = "/gotoworkingcalendar", method = RequestMethod.GET)
    public ModelAndView gotoWorkingCalendar(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("module/workingcalendar");
        return mav;
    }

    /**
     * 查看台账信息
     * @param request
     * @param flag
     * @return
     */
    @RequestMapping(value = "/gotoDetailData", method = RequestMethod.GET)
    public ModelAndView gotoDetailData(HttpServletRequest request,String flag,String id) {
        ModelAndView mav = new ModelAndView("module/detail");
        mav.addObject("flag",flag);
        mav.addObject("id",id);
        return mav;
    }
    @RequestMapping(value = "/getDetailList")
    @ResponseBody
    public String getDetailList(HttpServletRequest request,String id, String flag) {
        String sql = "";
        if("isLeave".equals(flag)){
            sql = "select * from\n" +
                    "(select * ,IFNULL(sjkss18112001,jhkss18112001) startTime,\n" +
                    "                IFNULL(sjjss18112001,jhjss18112001) endTime\n" +
                    "from qjsqt18112001) a where recorderNO = '"+id+"' ";
        }else if("isOut".equals(flag)){
            sql = "select * from wcsqt18112001 where recorderNO = '"+id+"'";
        }
        List<Map<String, Object>> list = tableService.selectSqlMapList(sql);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 0);
        jsonObject.put("msg", "");
        jsonObject.put("count", list.size());
        jsonObject.put("data", list);
        jsonObject.put("success",1);
        return jsonObject.toString();
    }
    /**
     *跳转到考勤列表页面
     * @param request
     * @return
     */
    @RequestMapping(value = "/gotoCheckList", method = RequestMethod.GET)
    public ModelAndView gotoCheckList(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("module/check");
        return mav;
    }

    /**
     * 查询考勤记录
     * @return
     */
    @RequestMapping(value = "/getCheckListByMonth")
    @ResponseBody
    public String getCheckListByMonth(HttpServletRequest request,String year,String month) {
        Loginer loginer = (Loginer)request.getSession().getAttribute("loginer");
        String userId = loginer.getId();
        Map<String,Object> map = new HashMap<>();
        SimpleDateFormat df1 = new SimpleDateFormat("yyyy");
        SimpleDateFormat df2 = new SimpleDateFormat("MM");
        if(StringUtils.isBlank(year)){
            year = df1.format(new Date());
        }
        if(StringUtils.isBlank(month)){
            month = df2.format(new Date());
        }
        map.put("userId",userId);
        map.put("year",year);
        map.put("month",month);
        //查询考勤列表
        List<Map<String, Object>> list1 = tableService.selectCheckList(map);       //所有条数
        //查询请假列表
        String sql2="select recorderNO recorderNOQJ,IFNULL(sjkss18112001,jhkss18112001) startTime,\n" +
                "IFNULL(sjjss18112001,jhjss18112001) endTime\n" +
                "from qjsqt18112001 where qjr1811200001 = '"+userId+"'";
        List<Map<String, Object>> list2 = tableService.selectSqlMapList(sql2);
        //存储请假集合
        Set<String> set2 = new HashSet<>();
        //存储外出集合
        Set<String> set3 = new HashSet<>();
        if(list2!=null && list2.size()>0){
            formateData(list1,list2,set2,"isLeave");
        }
        String sql3 = "select recorderNO recorderNOWCR,kssj181120001 startTime, jssj181120001 endTime from\n" +
                "(select recorderNO,kssj181120001,jssj181120001,wcr1811200001 wcr from wcsqt18112001 \n" +
                "where wcr1811200001 = '"+userId+"'\n" +
                "union all\n" +
                "select recorderNO,\n" +
                "kssj181120001,jssj181120001,\n" +
                "substring_index(substring_index(a.txr1811200001,';',b.help_topic_id+1),';',-1) as wcr\n" +
                "from wcsqt18112001 a\n" +
                "join mysql.help_topic b \n" +
                "on b.help_topic_id < (length(a.txr1811200001) - length(replace(a.txr1811200001,';','')))) aa where wcr = '"+userId+"' ";
        List<Map<String, Object>> list3 = tableService.selectSqlMapList(sql3);
        if(list3!=null && list3.size()>0){
            formateData(list1,list3,set3,"isOut");
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 0);
        jsonObject.put("msg", "");
        jsonObject.put("count", list1.size());
        jsonObject.put("data", list1);
        jsonObject.put("success",1);
        return jsonObject.toString();
    }

    /**
     * 判断是否有请假 外出
     * @param list1  考勤集合
     * @param listx  请假 外出 补卡 集合
     * @param setx   日期集合
     * @param flag
     */
    public void formateData(List<Map<String, Object>> list1,List<Map<String, Object>> listx,Set<String> setx,String flag){
        //key 是请假日期  value为请假人id
        Map<String,String> map = new HashMap<>();
        for(Map<String, Object> mapx : listx){
            String startTime = mapx.get("startTime").toString();
            String endTime = mapx.get("endTime").toString();
            String idValue = "";
            if("isLeave".equals(flag)){
                idValue = mapx.get("recorderNOQJ").toString();
            }else if("isOut".equals(flag)){
                idValue = mapx.get("recorderNOWCR").toString();
            }
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            try {
                Date dBegin = df.parse(startTime);
                Date dEnd = df.parse(endTime);
                setx = findDates(setx,dBegin,dEnd,map,idValue);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        //循环考勤列表  含有请假的 替换成1
        for(Map<String, Object> map1 : list1){
            if(map1.get("date")==null){
                continue;
            }
            String date = map1.get("date").toString();
            if(setx.contains(date)){
                map1.put(flag,"1");
                if("isLeave".equals(flag)){
                    map1.put("recorderNOQJ",map.get(date));
                }else if("isOut".equals(flag)){
                    map1.put("recorderNOWCR",map.get(date));
                }
            }
        }
    }
    /**
     * 根据时间范围  计算日期
     * @param set2
     * @param dBegin
     * @param dEnd
     * @return
     */
    public Set<String> findDates(Set<String> set2,Date dBegin, Date dEnd,Map<String,String> map,String value){
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
        set2.add(sd.format(dBegin));
        map.put(sd.format(dBegin),value);
        Calendar calBegin = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        calBegin.setTime(dBegin);
        Calendar calEnd = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        calEnd.setTime(dEnd);
        // 测试此日期是否在指定日期之后
        while (dEnd.after(calBegin.getTime()))
        {
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
            calBegin.add(Calendar.DAY_OF_MONTH, 1);
            set2.add(sd.format(calBegin.getTime()));
            map.put(sd.format(calBegin.getTime()),value);
        }
        return set2;
    }

    @RequestMapping(value = "/getworkingcalendar", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String getWorkingCalendar(HttpServletRequest request,int year,int month) {
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        String userId = loginer.getId();
        JSONObject json = new JSONObject(true);
        List<Map<String,Object>> sqlval = new ArrayList<>();

        //休息日数据获取
        sqlval = festivalService.getAllByYearAndMonth(year,month);
        Hashtable<String,String> date = new Hashtable<>();
        date.putAll(ScheduleUtil.findDayOffs(year,month));
        json = ScheduleUtil.getjson(json,date,"rest");

        //节假日数据获取
        sqlval = festivalService.getAllByYearAndMonth(year,month);
        Hashtable<String,String> date0 = new Hashtable<>();
        for(Map<String,Object> map : sqlval){
            String startTime = String.valueOf(map.get("startTime"));
            String endTime = String.valueOf(map.get("endTime"));
            String type = String.valueOf(map.get("festivalName"));
            date0.putAll(ScheduleUtil.findDates(DateHelper.getSqlDateTime(startTime),DateHelper.getSqlDateTime(endTime),type,month));
        }
        json = ScheduleUtil.getjson(json,date0,"rest");

        //请假台账数据获取
        List<String> where = new ArrayList<>();
        where.add("qjr1811200001='"+userId+"'");
        where.add("(YEAR(jhkss18112001) = "+year+" and MONTH(jhkss18112001) = "+month+" OR YEAR(jhjss18112001) = "+year+" and MONTH(jhjss18112001) = "+month+")");
        sqlval = ScheduleUtil.getValue("qjsqt18112001",where);
        Hashtable<String,String> date1 = new Hashtable<>();
        for(Map<String,Object> map : sqlval){
            String kssj181120001 = String.valueOf(map.get("jhkss18112001"));
            String jssj181120001 = String.valueOf(map.get("jhjss18112001"));
            String qjlx181120001 = String.valueOf(map.get("qjlx181120001"));
            date1.putAll(ScheduleUtil.findDates(DateHelper.getSqlDateTime(kssj181120001),DateHelper.getSqlDateTime(jssj181120001),qjlx181120001,month));
        }
        json = ScheduleUtil.getjson(json,date1,"leave");

        //外出台账数据获取
        where = new ArrayList<>();
        where.add("(wcr1811200001='"+userId+"' OR txr1811200001 in ('%"+userId+"%'))");
        where.add("(YEAR(kssj181120001) = "+year+" and MONTH(kssj181120001) = "+month+" OR YEAR(jssj181120001) = "+year+" and MONTH(jssj181120001) = "+month+")");
        sqlval = ScheduleUtil.getValue("wcsqt18112001",where);
        Hashtable<String,String> date2 = new Hashtable<>();
        for(Map<String,Object> map : sqlval){
            String kssj181120001 = String.valueOf(map.get("kssj181120001"));
            String jssj181120001 = String.valueOf(map.get("jssj181120001"));
            date2.putAll(ScheduleUtil.findDates(DateHelper.getSqlDateTime(kssj181120001),DateHelper.getSqlDateTime(jssj181120001),"出差",month));
        }
        json = ScheduleUtil.getjson(json,date2,"out");

        //考勤台账数据获取
        where = new ArrayList<>();
        where.add("jlr1811200001='"+userId+"'");
        where.add("(YEAR(swsb181120001) = "+year+" and MONTH(swsb181120001) = "+month+" OR YEAR(xwxb181120001) = "+year+" and MONTH(xwxb181120001) = "+month+")");
        sqlval = ScheduleUtil.getValue("ygkqj18112001",where);
        for(Map<String,Object> map : sqlval){
            String swsb181120001 = String.valueOf(map.get("swsb181120001"));
            String xwxb181120001 = String.valueOf(map.get("xwxb181120001"));
            String day = swsb181120001.substring(8,10);
            String am = swsb181120001.substring(11,19);
            String pm = xwxb181120001.substring(11,19);
            JSONObject j1 = new JSONObject(true);
            JSONObject j2 = new JSONObject(true);
            if(json.has(day)){
                j2 = json.getJSONObject(day);
                j1 = j2.getJSONObject("value");
                j1.put("am",am);
                j1.put("pm",pm);
                j2.put("value",j1);
            }else{
                j1.put("exp","工作");
                j1.put("am",am);
                j1.put("pm",pm);
                j2.put("type","work");
                j2.put("value",j1);
            }
            json.put(day,j2);
        }
        //工作安排数据获取
        sqlval = new ArrayList<>();
        sqlval = getScheduleListByMonth(year,month,userId,sqlval);
        Hashtable<String,String> date3 = new Hashtable<>();
        List<String> list3 = new ArrayList<>();
        for(Map<String,Object> map : sqlval){
            String startTime =  String.valueOf(map.get("startTime"));
            String endTime =  String.valueOf(map.get("endTime"));
            list3 = ScheduleUtil.findDates(DateHelper.getSqlDateTime(startTime),DateHelper.getSqlDateTime(endTime),month,list3);
            //date3.putAll(ScheduleUtil.findDates(DateHelper.getSqlDateTime(startTime),DateHelper.getSqlDateTime(endTime),"日程:",month));
        }
        for(int i=0; i<list3.size(); i++){
            String key = list3.get(i);
            if(date3.containsKey(key)){
                String val = date3.get(key);
                date3.put(key,"日程:"+(Integer.parseInt(val.substring(3,val.length()))+1));
            }else{
                date3.put(key,"日程:"+1);
            }
        }
        json = ScheduleUtil.getjson(json,date3,"schedule");
        return json.toString();
    }

    /**
     * 获取日程数据
     * @param year
     * @param month
     * @param userId
     * @param sqlval
     * @return
     */
    public List<Map<String,Object>> getScheduleListByMonth(int year,int month,String userId,List<Map<String,Object>> sqlval){
       String sql = " select * from schedule \n" +
               "where user = '"+userId+"' and curStatus = 2\n" +
               "and DATE_FORMAT(startTime,'%Y-%c') <= '"+year+"-"+month+"'\n" +
               "and DATE_FORMAT(endTime,'%Y-%c') >= '"+year+"-"+month+"' ";
        sqlval = tableService.selectSqlMapList(sql);
        return sqlval;
    }

    @RequestMapping(value = "/gotoScheduleList", method = RequestMethod.GET)
    public ModelAndView gotoScheduleList(HttpServletRequest request,String type,String scheduleId) {
        ModelAndView mav = new ModelAndView("module/schedule");
        if(type.equals("edit")){
            Schedule schedule = scheduleService.selectById(scheduleId);
            mav.addObject("schedule",schedule);
        }
        mav.addObject("type",type);
        String role = (String)request.getSession().getAttribute("role");
        mav.addObject("role",role);
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
        schedule.setEndRow(pu.getEndRow()-pu.getStartRow());
        schedule.setStartTime(startTime);
        schedule.setEndTime(endTime);
        int count = scheduleService.selectAllTermsCont(schedule);
        List<Schedule> scheduleList = scheduleService.selectAllTerms(schedule);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 0);
        jsonObject.put("msg", "");
        jsonObject.put("count", count);
        jsonObject.put("data", scheduleList);
        jsonObject.put("success",1);
        jsonObject.put("is", true);
        jsonObject.put("tip", "操作成功");

        return jsonObject.toString();
    }

    @RequestMapping(value = "/insert", method = RequestMethod.POST, produces = {"text/html;charset=UTF-8;", "application/json;"})
    @ResponseBody
    public String insertSave(HttpServletRequest request, Schedule schedule) {
        String userid = request.getParameter("userid");
        if(userid==null || userid.equals("")) {
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
            return "1";
        } catch (Exception e) {
            LogUtil.sysLog("Exception:"+e);
            return "0";
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
            return "1";
        } catch (Exception e) {
            LogUtil.sysLog("Exception:"+e);
            return "0";
        }
    }

    @RequestMapping(value = "/deletes", method = RequestMethod.POST, produces = {"text/html;charset=UTF-8;", "application/json;"})
    @ResponseBody
    public String deletes(HttpServletRequest request, String scheduleId) {
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        try {
            String userId = loginer.getId();
            scheduleId = scheduleId.replaceAll(";","','");
            scheduleId = "'"+scheduleId.substring(0,scheduleId.length()-2);
            scheduleService.deletes(scheduleId,userId,DateHelper.now());
            return "1";
        } catch (Exception e) {
            LogUtil.sysLog("Exception:"+e);
            return "0";
        }
    }

    //获取跟人每天的日程安排
    @RequestMapping("/getUserSchedule")
    @ResponseBody
    public List<Schedule> getUserSchedule(HttpServletRequest request,String date){
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        Schedule schedule = new Schedule();
        schedule.setUser(loginer.getId());
        schedule.setStartTime(date+" 23:59:59");
        schedule.setEndTime(date);
        return scheduleService.getUserSchedule(schedule);
    }

}
