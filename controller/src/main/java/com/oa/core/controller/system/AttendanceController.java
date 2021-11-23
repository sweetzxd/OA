package com.oa.core.controller.system;

import com.alibaba.dubbo.common.extension.Activate;
import com.oa.core.bean.system.Attendance;
import com.oa.core.helper.DateHelper;
import com.oa.core.listener.WebSocketListener;
import com.oa.core.service.util.TableService;
import com.oa.core.util.AttenceUtil;
import com.oa.core.util.ConfParseUtil;
import com.oa.core.util.MySqlUtil;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @ClassName:kaoqinController
 * @author:zxd
 * @Date:2019/03/19
 * @Time:下午 2:47
 * @Version V1.0
 * @Explain
 */
@Controller
@RequestMapping("/kaoqin")
public class AttendanceController {

    @Autowired
    TableService tableService;

    @RequestMapping(value = "/insert", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String setKaoQin(HttpServletRequest request, @RequestBody String param) {
        try {
            System.out.println(param);
            JSONObject jo = new JSONObject(param);
            String jobnumber = jo.getString("sdwEnrollNumber");
            Map<String, Object> map = tableService.selectSqlMap("select userId from jobtable where curStatus=2 and jobnumber='" + jobnumber + "'");
            String name = jo.getString("sName");
            if (!jo.isNull("VerifyMethod")) {
                String type = String.valueOf(jo.get("VerifyMethod"));
            }
            String year = String.valueOf(jo.get("Year"));
            String month = String.valueOf(jo.get("Month"));
            if (month.length() < 2) {
                month = "0" + month;
            }
            String day = String.valueOf(jo.get("Day"));
            if (day.length() < 2) {
                day = "0" + day;
            }
            String hour = String.valueOf(jo.get("Hour"));
            if (hour.length() < 2) {
                hour = "0" + hour;
            }
            String minute = String.valueOf(jo.get("Minute"));
            if (minute.length() < 2) {
                minute = "0" + minute;
            }
            String second = String.valueOf(jo.get("Second"));
            if (second.length() < 2) {
                second = "0" + second;
            }
            String datatime = year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
            return AttenceUtil.attence((String) map.get("userId"), datatime);
        } catch (Exception e) {
            JSONObject json = new JSONObject();
            json.put("success", 0);
            json.put("msg", "打卡失败");
            return json.toString();
        }
    }

    @RequestMapping(value = "/thisdayatt", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public String getUserAttendance(HttpServletRequest request) {
        String year = request.getParameter("year");
        String month = request.getParameter("month");
        String day = request.getParameter("day");
        String ymd = "";
        if (year != null && month != null && day != null) {
            if (month.length() < 2) {
                month = "0" + month;
            }
            if (day.length() < 2) {
                day = "0" + month;
            }
            ymd = year + "-" + month + "-" + day;
        } else {
            Date d = new Date(System.currentTimeMillis() - 1000 * 60 * 60 * 24);
            SimpleDateFormat sp = new SimpleDateFormat("yyyy-MM-dd");
            String[] time = sp.format(d).split("-");
            year = time[0];
            month = time[1];
            day = time[2];
            ymd = year + "-" + month + "-" + day;
        }
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
        sql.append("select userName,staffName,ifnull(t.num,'0000') as num from userinfo u left join (select qdr,CONCAT(num1,num2,num3,num4) as num from (");
        sql.append("select qdr,if(sum(num1)>0,1,0) as num1,if(sum(num2)>0,1,0) as num2,if(sum(num3)>0,1,0) as num3,if(sum(num4)>0,1,0) as num4 from ");
        sql.append("(select sbry190720001 as qdr,count(*) as num1,0 as num2,0 as num3,0 as num4 from rlsbj19072001 where curStatus=2 and sbsj190720001 BETWEEN '" + time[0] + "' and '" + time[1] + "' GROUP BY sbrbh19072001 UNION ");
        sql.append("select sbry190720001 as qdr,0 as num1,count(*) as num2,0 as num3,0 as num4 from rlsbj19072001 where curStatus=2 and sbsj190720001 BETWEEN '" + time[2] + "' and '" + time[3] + "'  GROUP BY sbrbh19072001 UNION ");
        sql.append("select sbry190720001 as qdr,0 as num1,0 as num2,count(*) as num3,0 as num4 from rlsbj19072001 where curStatus=2 and sbsj190720001 BETWEEN '" + time[4] + "' and '" + time[5] + "'  GROUP BY sbrbh19072001 UNION ");
        sql.append("select sbry190720001 as qdr,0 as num1,0 as num2,0 as num3,count(*) as num4 from rlsbj19072001 where curStatus=2 and sbsj190720001 BETWEEN '" + time[6] + "' and '" + time[7] + "' GROUP BY sbrbh19072001 )");
        sql.append(" att group by qdr");
        sql.append(" ) temptable) t on u.userName = t.qdr");
        List<Map<String, Object>> maps = tableService.selectSqlMapList(sql.toString());
        tableService.implementSql("delete attendancesum where year='"+year+"' and month='"+month+"'");
        for (int i = 0, len = maps.size(); i < len; i++) {
            Map<String, Object> map = maps.get(i);
            String userName = (String) map.get("userName");
            String num = (String) map.get("num");
            Map attendancesum = new HashMap();
            attendancesum.put("user", userName);
            attendancesum.put("year", year);
            attendancesum.put("month", month);
            attendancesum.put("day", day);
            double[] attencetype = AttenceUtil.attencetype(num);
            attendancesum.put("num", new JSONArray(attencetype));
            tableService.insertSqlMap(MySqlUtil.getInsertSql("attendancesum", attendancesum));
        }
        JSONArray usersAtt = new JSONArray();
        return usersAtt.toString();
    }

    @RequestMapping(value = "/selectykq", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public String getMonthValue(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String year = request.getParameter("year");
        String month = request.getParameter("month");
        if (year == null || month == null) {
            String[] time = DateHelper.getYMD().split("-");
            year = time[0];
            month = time[1];
        }
        StringBuffer sql = new StringBuffer();
        sql.append("select u.staffName,m.* from userInfo u left join (SELECT `user`");
        sql.append(",MAX( CASE `day` WHEN '01' THEN num ELSE '[0,0]' END ) 'day01'");
        sql.append(",MAX( CASE `day` WHEN '02' THEN num ELSE '[0,0]' END ) 'day02'");
        sql.append(",MAX( CASE `day` WHEN '03' THEN num ELSE '[0,0]' END ) 'day03'");
        sql.append(",MAX( CASE `day` WHEN '04' THEN num ELSE '[0,0]' END ) 'day04'");
        sql.append(",MAX( CASE `day` WHEN '05' THEN num ELSE '[0,0]' END ) 'day05'");
        sql.append(",MAX( CASE `day` WHEN '06' THEN num ELSE '[0,0]' END ) 'day06'");
        sql.append(",MAX( CASE `day` WHEN '07' THEN num ELSE '[0,0]' END ) 'day07'");
        sql.append(",MAX( CASE `day` WHEN '08' THEN num ELSE '[0,0]' END ) 'day08'");
        sql.append(",MAX( CASE `day` WHEN '09' THEN num ELSE '[0,0]' END ) 'day09'");
        sql.append(",MAX( CASE `day` WHEN '10' THEN num ELSE '[0,0]' END ) 'day10'");
        sql.append(",MAX( CASE `day` WHEN '11' THEN num ELSE '[0,0]' END ) 'day11'");
        sql.append(",MAX( CASE `day` WHEN '12' THEN num ELSE '[0,0]' END ) 'day12'");
        sql.append(",MAX( CASE `day` WHEN '13' THEN num ELSE '[0,0]' END ) 'day13'");
        sql.append(",MAX( CASE `day` WHEN '14' THEN num ELSE '[0,0]' END ) 'day14'");
        sql.append(",MAX( CASE `day` WHEN '15' THEN num ELSE '[0,0]' END ) 'day15'");
        sql.append(",MAX( CASE `day` WHEN '16' THEN num ELSE '[0,0]' END ) 'day16'");
        sql.append(",MAX( CASE `day` WHEN '17' THEN num ELSE '[0,0]' END ) 'day17'");
        sql.append(",MAX( CASE `day` WHEN '18' THEN num ELSE '[0,0]' END ) 'day18'");
        sql.append(",MAX( CASE `day` WHEN '19' THEN num ELSE '[0,0]' END ) 'day19'");
        sql.append(",MAX( CASE `day` WHEN '20' THEN num ELSE '[0,0]' END ) 'day20'");
        sql.append(",MAX( CASE `day` WHEN '21' THEN num ELSE '[0,0]' END ) 'day21'");
        sql.append(",MAX( CASE `day` WHEN '22' THEN num ELSE '[0,0]' END ) 'day22'");
        sql.append(",MAX( CASE `day` WHEN '23' THEN num ELSE '[0,0]' END ) 'day23'");
        sql.append(",MAX( CASE `day` WHEN '24' THEN num ELSE '[0,0]' END ) 'day24'");
        sql.append(",MAX( CASE `day` WHEN '25' THEN num ELSE '[0,0]' END ) 'day25'");
        sql.append(",MAX( CASE `day` WHEN '26' THEN num ELSE '[0,0]' END ) 'day26'");
        sql.append(",MAX( CASE `day` WHEN '27' THEN num ELSE '[0,0]' END ) 'day27'");
        sql.append(",MAX( CASE `day` WHEN '28' THEN num ELSE '[0,0]' END ) 'day28'");
        sql.append(",MAX( CASE `day` WHEN '29' THEN num ELSE '[0,0]' END ) 'day29'");
        sql.append(",MAX( CASE `day` WHEN '30' THEN num ELSE '[0,0]' END ) 'day30'");
        sql.append(",MAX( CASE `day` WHEN '31' THEN num ELSE '[0,0]' END ) 'day31'");
        sql.append("FROM attendancesum WHERE YEAR = '" + year + "' AND MONTH = '" + month + "'");

        sql.append("Group by `user`) m on u.userName = m.user ");
        try {
            List<Map<String, Object>> maps = tableService.selectSqlMapList(sql.toString());
            jsonObject.put("code", 0);
            jsonObject.put("msg", "");
            jsonObject.put("count", maps.size());
            jsonObject.put("data", maps);
        } catch (Exception e) {
            jsonObject.put("code", 1);
            jsonObject.put("msg", "无数据");
            jsonObject.put("count", 0);
            jsonObject.put("data", "");
        }
        return jsonObject.toString();
    }
}
