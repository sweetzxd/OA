package com.oa.core.controller.attence;


import com.oa.core.bean.Loginer;
import com.oa.core.helper.DateHelper;
import com.oa.core.service.module.FestivalService;
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
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/weixin")
public class AttenceController {

    @Autowired
    TableService tableService;

    @Autowired
    FestivalService festivalService;

    /**
     * 用户打卡
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/attence", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String attence(HttpServletRequest request, HttpServletResponse response) {
        Loginer loginer = (Loginer)request.getSession().getAttribute("loginer");
        String userId = null;
        if(loginer == null){
            userId = request.getParameter("userid");
        }else{
            userId = loginer.getId();
        }
        JSONObject jsonObject = new JSONObject();
        try {
            ConfParseUtil cp = new ConfParseUtil();

            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
            String date = sdf.format(new Date());
            String swsb = date + " " + cp.getSchedule("amtowork");
            String swxb = date + " " + cp.getSchedule("amoffwork");
            String xwsb = date + " " + cp.getSchedule("pmtowork");
            String xwxb = date + " " + cp.getSchedule("pmoffwork");

            String sql = " select * from ygkqj18112001 " +
                    " where jlr1811200001 = '"+userId+"' and curStatus = 2 and DATE_FORMAT(recordTime,'%Y-%m-%d') = '"+date+"'; ";
            List<Map<String,Object>> list = tableService.selectSqlMapList(sql);
            SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date now = new Date();
            long d = now.getTime();
            if(list.size()>0){//打过一次卡
                Map<String,Object> map = list.get(0);
                String recorderNO = (String)map.get("recorderNO");
                String updateSql = " update ygkqj18112001 set ";
                if(d<= sdf1.parse(swsb).getTime()){   //
                    updateSql += " swsb181120001 = '"+sdf1.format(now)+"'";
                }else if(d>sdf1.parse(swsb).getTime() && d<sdf1.parse(swxb).getTime()){//在8:30--12:00之间 可能是上午上班打卡 上午下班打卡
                    if(map.get("swsb181120001")==null){//上午上班
                        updateSql += " swsb181120001 = '"+sdf1.format(now)+"'";
                    }else{                               //上午下班
                        updateSql += " swxb181120001 = '"+sdf1.format(now)+"'";
                    }
                }else if(d>=sdf1.parse(swxb).getTime() && d<=sdf1.parse(xwsb).getTime()){//在12:00--13:30之间 可能是上午下班   下午上班打卡
                    if(map.get("swxb181120001")==null){//上午下班
                        updateSql += " swxb181120001 = '"+sdf1.format(now)+"'";
                    }else{                                //下午上班
                        updateSql += " xwsb181120001 = '"+sdf1.format(now)+"'";
                    }
                }else if(d>sdf1.parse(xwsb).getTime() && d<sdf1.parse(xwxb).getTime()){//在13:30--18:00之间  课程是下午上班   下午下班
                    if(map.get("xwsb181120001")==null){
                        updateSql += " xwsb181120001 = '"+sdf1.format(now)+"'";
                    }else{
                        updateSql += " xwxb181120001 = '"+sdf1.format(now)+"'";
                    }
                }else {
                    updateSql += " xwxb181120001 = '"+sdf1.format(now)+"'";
                }
                updateSql += " where recorderNO = '"+recorderNO+"'";
                tableService.updateSqlMap(updateSql);
            }else{//第一次打卡
                PrimaryKeyUitl pk = new PrimaryKeyUitl();
                String table = "ygkqj18112001";
                String recorderNO = pk.getNextId(table, "recorderNO");

                Map<String, Object> map = new HashMap<>();
                map.put("recorderNO",recorderNO);
                map.put("jlr1811200001",userId);
                map.put("recordName",userId);
                map.put("recordTime",sdf1.format(now));
                if(d<= sdf1.parse(swsb).getTime()){   //
                    map.put("swsb181120001",sdf1.format(now));
                }else if(d>sdf1.parse(swsb).getTime() && d<sdf1.parse(swxb).getTime()){//在8:30--12:00之间
                    map.put("swsb181120001",sdf1.format(now));
                }else if(d>=sdf1.parse(swxb).getTime() && d<=sdf1.parse(xwsb).getTime()){//在12:00--13:30之间
                    map.put("xwsb181120001",sdf1.format(now));
                }else if(d>sdf1.parse(xwsb).getTime() && d<sdf1.parse(xwxb).getTime()){//在13:30--18:00之间  课程是下午上班   下午下班
                    map.put("xwsb181120001",sdf1.format(now));
                }else {
                    map.put("xwxb181120001",sdf1.format(now));
                }
                String insertSql = MySqlUtil.getInsertSql(table, map);
                tableService.insertSqlMap(insertSql);
            }
            jsonObject.put("msg","签到成功");
            jsonObject.put("date", sdf1.format(now));
            String weizhi = new String(request.getParameter("place").getBytes("iso-8859-1"), "UTF-8");
            String chengshi = new String(request.getParameter("city").getBytes("iso-8859-1"), "UTF-8");
            WeatherUtil wu = new WeatherUtil();
            jsonObject.put("weather", wu.getWeather(Const.LOCATION));
            jsonObject.put("success",1);
        }catch (Exception e){
            e.printStackTrace();
            jsonObject.put("msg","签到失败");
            jsonObject.put("success",0);
        }
        return jsonObject.toString();
    }

    @RequestMapping(value = "/getworkingcalendar", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String getWorkingCalendar(HttpServletRequest request,int year,int month) {
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        String userId = null;
        if(loginer == null){
            userId = request.getParameter("userid");
        }else{
            userId = loginer.getId();
        }
        boolean weixin = false;
        String wx = request.getParameter("wx");
        if(wx!=null && wx.equals("1")){
            weixin = true;
        }
        JSONObject json = new JSONObject(true);
        JSONArray jsonArray = new JSONArray();
        List<Map<String,Object>> sqlval;
        try{
            //休息日数据获取
            Hashtable<String,String> date = new Hashtable<>();
            date.putAll(ScheduleUtil.findDayOffs(year,month));
            if(weixin){
                jsonArray = ScheduleUtil.getwxjson(jsonArray, date, "rest");
            }else {
                json = ScheduleUtil.getjson(json, date, "rest");
            }

            //节假日数据获取
            sqlval = festivalService.getAllByYearAndMonth(year,month);
            Hashtable<String,String> date0 = new Hashtable<>();
            for(Map<String,Object> map : sqlval){
                String startTime = String.valueOf(map.get("startTime"));
                String endTime = String.valueOf(map.get("endTime"));
                String type = String.valueOf(map.get("festivalName"));
                date0.putAll(ScheduleUtil.findDates(DateHelper.getSqlDateTime(startTime),DateHelper.getSqlDateTime(endTime),type,month));
            }
            //json = ScheduleUtil.getjson(json,date0,"rest");
            if(weixin){
                jsonArray = ScheduleUtil.getwxjson(jsonArray,date0,"rest");
            }else {
                json = ScheduleUtil.getjson(json,date0,"rest");
            }

            List<String> where = new ArrayList<>();
            //外出台账数据获取
            where.add("(wcr1811200001='"+userId+"' OR txr1811200001 in ('%"+userId+"%'))");
            where.add("(YEAR(kssj181120001) = "+year+" and MONTH(kssj181120001) = "+month+" OR YEAR(jssj181120001) = "+year+" and MONTH(jssj181120001) = "+month+")");
            sqlval = ScheduleUtil.getValue("wcsqt18112001",where);
            Hashtable<String,String> date2 = new Hashtable<>();
            for(Map<String,Object> map : sqlval){
                String kssj181120001 = String.valueOf(map.get("kssj181120001"));
                String jssj181120001 = String.valueOf(map.get("jssj181120001"));
                date2.putAll(ScheduleUtil.findDates(DateHelper.getSqlDateTime(kssj181120001),DateHelper.getSqlDateTime(jssj181120001),"出差",month));
            }
            //json = ScheduleUtil.getjson(json,date2,"out");
            if(weixin){
                jsonArray = ScheduleUtil.getwxjson(jsonArray,date2,"out");
            }else {
                json = ScheduleUtil.getjson(json,date2,"out");
            }

            //请假台账数据获取
            where = new ArrayList<>();
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
            //json = ScheduleUtil.getjson(json,date1,"leave");
            if(weixin){
                jsonArray = ScheduleUtil.getwxjson(jsonArray,date1,"leave");
            }else {
                json = ScheduleUtil.getjson(json,date1,"leave");
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        if(weixin){
            return jsonArray.toString();
        }else {
            return json.toString();
        }
    }

    @RequestMapping(value = "/getIsAttence", method = RequestMethod.POST)
    @ResponseBody
    public String getIsAttence(HttpServletRequest request,int year,int month,int day) {
        JSONObject object = new JSONObject();
        Loginer loginer = (Loginer)request.getSession().getAttribute("loginer");
        String userId = null;
        if(loginer == null){
            userId = request.getParameter("userid");
        }else{
            userId = loginer.getId();
        }
        try{
            String sql = "select recorderNO, jlr1811200001, " +
                    "DATE_FORMAT(swsb181120001,'%H:%i:%s') swsb181120001,\n" +
                    "DATE_FORMAT(swxb181120001,'%H:%i:%s') swxb181120001,\n" +
                    "DATE_FORMAT(xwsb181120001,'%H:%i:%s') xwsb181120001,\n" +
                    "DATE_FORMAT(xwxb181120001,'%H:%i:%s') xwxb181120001, zt18112000001, qt18112000001 from ygkqj18112001\n" +
                    "where jlr1811200001 = '"+userId+"' and curStatus = 2\n" +
                    "and DATE_FORMAT(IFNULL(IFNULL(IFNULL(swsb181120001,swxb181120001),xwsb181120001),xwxb181120001),'%Y-%m-%d') = '"+year+"-"+month+"-"+day+"'";
            List<Map<String, Object>> list = tableService.selectSqlMapList(sql);
            if(list!=null && list.size()>0){
                Map<String, Object> map = list.get(0);
                /*String swsb181120001 = map.get("swsb181120001")==null?"":map.get("swsb181120001").toString();
                String swxb181120001 = map.get("swxb181120001")==null?"":map.get("swxb181120001").toString();
                String xwsb181120001 = map.get("xwsb181120001")==null?"":map.get("xwsb181120001").toString();
                String xwxb181120001 = map.get("xwxb181120001")==null?"":map.get("xwxb181120001").toString();*/
                object.put("data",map);
                object.put("count",1);
            }else{//今天未打卡
                object.put("count",0);
            }
            object.put("success",true);
            ConfParseUtil cp = new ConfParseUtil();
            String swsb = cp.getSchedule("amtowork");
            String swxb = cp.getSchedule("amoffwork");
            String xwsb = cp.getSchedule("pmtowork");
            String xwxb = cp.getSchedule("pmoffwork");
            Map<String,Object> map2 = new HashMap<>();
            map2.put("swsb",swsb);
            map2.put("swxb",swxb);
            map2.put("xwsb",xwsb);
            map2.put("xwxb",xwxb);
            object.put("map2",map2);
        }catch (Exception e){
            e.printStackTrace();
            object.put("success",false);
        }
        return object.toString();
    }
}
