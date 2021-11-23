package com.oa.core.controller.attence;


import com.google.gson.JsonObject;
import com.oa.core.bean.Loginer;
import com.oa.core.helper.DateHelper;
import com.oa.core.helper.StringHelper;
import com.oa.core.interceptor.Logined;
import com.oa.core.listener.WebSocketListener;
import com.oa.core.scada.websocket.MyWebSocketClient;
import com.oa.core.service.module.FestivalService;
import com.oa.core.service.util.TableService;
import com.oa.core.util.*;
import com.oa.core.wxutil.WeatherUtil;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/weixin")
public class WxAttenceController {

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
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        String userId;
        if (loginer == null) {
            userId = request.getParameter("userid");
        } else {
            userId = loginer.getId();
        }
        return AttenceUtil.attence(userId, DateHelper.getYMDHMS());
    }

    @RequestMapping(value = "/getworkingcalendar", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String getWorkingCalendar(HttpServletRequest request, int year, int month) {
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        String userId = null;
        if (loginer == null) {
            userId = request.getParameter("userid");
        } else {
            userId = loginer.getId();
        }
        boolean weixin = false;
        String wx = request.getParameter("wx");
        if (wx != null && wx.equals("1")) {
            weixin = true;
        }
        JSONObject json = new JSONObject(true);
        JSONArray jsonArray = ScheduleUtil.getNewValue();
        List<Map<String, Object>> sqlval;
        try {

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
            StringBuffer sql = new StringBuffer();
            for(int i = 1,len = DateHelper.getDays(year,month);i <= len;i++){
                String ymd = "";
                String days = i<10?"0" + i:""+i;
                String months = month<10?"0"+month:""+month;
                ymd = year + "-" + months + "-" + days;
                String[] time = new String[]{ymd + " " + amtoworku, ymd + " " + amtowork, ymd + " " + amoffwork, ymd + " " + amoffworkd, ymd + " " + pmtoworku, ymd + " " + pmtowork, ymd + " " + pmoffwork, ymd + " " + pmoffworkd};
                sql.append("SELECT ");
                sql.append("(SELECT DISTINCT sbry190720001 FROM rlsbview WHERE curStatus=2 AND sbry190720001 = '"+userId+"' ) AS name,");
                sql.append("(SELECT '"+days+"') AS day,");
                sql.append("IFNULL((SELECT MIN(sbsj190720001) FROM rlsbview WHERE curStatus=2 AND sbry190720001='"+userId+"' AND sbsj190720001 BETWEEN '" + time[0] + "' AND '" + time[1] + "'),'') AS swsb,");
                sql.append("IFNULL((SELECT MIN(sbsj190720001) FROM rlsbview WHERE curStatus=2 AND sbry190720001='"+userId+"' AND sbsj190720001 BETWEEN '" + time[2] + "' AND '" + time[3] + "'),'') AS swxb,");
                sql.append("IFNULL((SELECT MIN(sbsj190720001) FROM rlsbview WHERE curStatus=2 AND sbry190720001='"+userId+"' AND sbsj190720001 BETWEEN '" + time[4] + "' AND '" + time[5] + "'),'') AS xwsb,");
                sql.append("IFNULL((SELECT MAX(sbsj190720001) FROM rlsbview WHERE curStatus=2 AND sbry190720001='"+userId+"' AND sbsj190720001 BETWEEN '" + time[6] + "' AND '" + time[7] + "'),'') AS xwxb,");
                sql.append("'"+ymd+"' AS recordTime");
                sql.append(" FROM dual");
                if(i<len){
                    sql.append("\n UNION \n");
                }

            }
            List<Map<String, Object>> list =  tableService.selectSqlMapList(sql.toString());
            Hashtable<String, String[]> sdata = new Hashtable<>();
            JSONObject jsonObject = new JSONObject();
            for (Map<String, Object> map : list) {
                String swsb = String.valueOf(map.get("swsb"));
                String swxb = String.valueOf(map.get("swxb"));
                String xwsb = String.valueOf(map.get("xwsb"));
                String xwxb = String.valueOf(map.get("xwxb"));
                String recordTime = String.valueOf(map.get("recordTime"));
                String[] s = new String[4];
                s[0] = DateHelper.getTime(swsb);
                s[1] = DateHelper.getTime(swxb);
                s[2] = DateHelper.getTime(xwsb);
                s[3] = DateHelper.getTime(xwxb);
                if(DateHelper.getExp(s)) {
                    JSONObject j2 = new JSONObject();
                    j2.put("exp", "班");
                    j2.put("am", s[0]);
                    j2.put("amx", s[1]);
                    j2.put("pm", s[3]);
                    j2.put("pmx", s[2]);
                    j2.put("type", "work");
                    String day = recordTime.substring(8, 10);
                    jsonObject.put(day, j2);
                    sdata.put(recordTime, s);
                }
                /*String[] s = new String[2];
                s[0] = DateHelper.getTime(swsb);
                s[1] = DateHelper.getTime(swxb);
                String d = DateHelper.getTime(swxb);
                if(!DateHelper.getTime(xwxb).equals("")){
                    s[1] = DateHelper.getTime(xwxb);
                }
                if((s[0]!=null && !s[0].equals("")) || (s[1]!=null && !s[1].equals(""))) {
                    JSONObject j2 = new JSONObject();
                    j2.put("exp", "班");
                    j2.put("am", s[0]);
                    j2.put("pm", s[1]);
                    j2.put("type", "work");
                    String day = recordTime.substring(8, 10);
                    jsonObject.put(day, j2);
                    sdata.put(recordTime, s);
                }*/
            }

            if (weixin) {
                jsonArray = ScheduleUtil.getgrwxjson(jsonArray, sdata, "work");
            }

            //休息日数据获取
            Hashtable<String, String> date = new Hashtable<>();
            date.putAll(ScheduleUtil.findDayOffs(year, month));
            if (weixin) {
                jsonArray = ScheduleUtil.getwxjson(jsonArray, date, "rest", jsonObject);
            } else {
                json = ScheduleUtil.getjson(json, date, "rest");
            }

            //节假日数据获取
            sqlval = festivalService.getAllByYearAndMonth(year, month);
            Hashtable<String, String> date0 = new Hashtable<>();
            for (Map<String, Object> map : sqlval) {
                String startTime = String.valueOf(map.get("startTime"));
                String endTime = String.valueOf(map.get("endTime"));
                String type = String.valueOf(map.get("festivalName"));
                date0.putAll(ScheduleUtil.findDates(DateHelper.getSqlDateTime(startTime), DateHelper.getSqlDateTime(endTime), "节", month));
            }
            //json = ScheduleUtil.getjson(json,date0,"rest");
            if (weixin) {
                jsonArray = ScheduleUtil.getwxjson(jsonArray, date0, "rest", jsonObject);
            } else {
                json = ScheduleUtil.getjson(json, date0, "rest");
            }

            List<String> where = new ArrayList<>();
            //外出台账数据获取
            where.add("(wcr1811200001='" + userId + "' OR txr1811200001 in ('%" + userId + "%'))");
            where.add("(YEAR(kssj181120001) = " + year + " and MONTH(kssj181120001) = " + month + " OR YEAR(jssj181120001) = " + year + " and MONTH(jssj181120001) = " + month + ")");
            sqlval = ScheduleUtil.getValue("wcsqt18112001", where);
            Hashtable<String, String> date2 = new Hashtable<>();
            for (Map<String, Object> map : sqlval) {
                String kssj181120001 = String.valueOf(map.get("kssj181120001"));
                String jssj181120001 = String.valueOf(map.get("jssj181120001"));
                date2.putAll(ScheduleUtil.findDates(DateHelper.getSqlDateTime(kssj181120001), DateHelper.getSqlDateTime(jssj181120001), "外", month));
            }
            //json = ScheduleUtil.getjson(json,date2,"out");
            if (weixin) {
                jsonArray = ScheduleUtil.getwxjson(jsonArray, date2, "out", jsonObject);
            } else {
                json = ScheduleUtil.getjson(json, date2, "out");
            }

            //请假台账数据获取
            where = new ArrayList<>();
            where.add("qjr1811200001='" + userId + "'");
            where.add("(YEAR(jhkss18112001) = " + year + " and MONTH(jhkss18112001) = " + month + " OR YEAR(jhjss18112001) = " + year + " and MONTH(jhjss18112001) = " + month + ")");
            sqlval = ScheduleUtil.getValue("qjsqt18112001", where);
            Hashtable<String, String> date1 = new Hashtable<>();
            for (Map<String, Object> map : sqlval) {
                String kssj181120001 = String.valueOf(map.get("jhkss18112001"));
                String jssj181120001 = String.valueOf(map.get("jhjss18112001"));
                String qjlx181120001 = String.valueOf(map.get("qjlx181120001"));
                date1.putAll(ScheduleUtil.findDates(DateHelper.getSqlDateTime(kssj181120001), DateHelper.getSqlDateTime(jssj181120001), "假", month));
            }
            //json = ScheduleUtil.getjson(json,date1,"leave");
            if (weixin) {
                jsonArray = ScheduleUtil.getwxjson(jsonArray, date1, "leave", jsonObject);
            } else {
                json = ScheduleUtil.getjson(json, date1, "leave");
            }

        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            e.printStackTrace();
        }
        if (weixin) {
            return jsonArray.toString();
        } else {
            return json.toString();
        }
    }

    @RequestMapping(value = "/getIsAttence", method = RequestMethod.POST)
    @ResponseBody
    public String getIsAttence(HttpServletRequest request, int year, int month, int day) {
        JSONObject object = new JSONObject();
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        String userId = null;
        if (loginer == null) {
            userId = request.getParameter("userid");
        } else {
            userId = loginer.getId();
        }
        try {
            ConfParseUtil cpu = new ConfParseUtil();
            String amtoworku = cpu.getSchedule("amtoworku");
            String amtowork = cpu.getSchedule("amtowork");
            String amoffwork = cpu.getSchedule("amoffwork");
            String amoffworkd = cpu.getSchedule("amoffworkd");
            String pmtoworku = cpu.getSchedule("pmtoworku");
            String pmtowork = cpu.getSchedule("pmtowork");
            String pmoffwork = cpu.getSchedule("pmoffwork");
            String pmoffworkd = cpu.getSchedule("pmoffworkd");

            String days = day<10?"0" + day:""+day;
            String months = month<10?"0"+month:""+month;
            String ymd = year + "-" + months + "-" + days;
            String[] time = new String[]{ymd + " " + amtoworku, ymd + " " + amtowork, ymd + " " + amoffwork, ymd + " " + amoffworkd, ymd + " " + pmtoworku, ymd + " " + pmtowork, ymd + " " + pmoffwork, ymd + " " + pmoffworkd};

            StringBuffer sql = new StringBuffer();
            sql.append("SELECT ");
            sql.append("(SELECT DISTINCT sbry190720001 FROM rlsbview WHERE curStatus=2 AND sbry190720001 = '"+userId+"' ) AS name,");
            sql.append("(SELECT '"+days+"') AS day,");
            sql.append("IFNULL((SELECT MIN(sbsj190720001) FROM rlsbview WHERE curStatus=2 AND sbry190720001='"+userId+"' AND sbsj190720001 BETWEEN '" + time[0] + "' AND '" + time[1] + "'),'') AS swsb,");
            sql.append("IFNULL((SELECT MIN(sbsj190720001) FROM rlsbview WHERE curStatus=2 AND sbry190720001='"+userId+"' AND sbsj190720001 BETWEEN '" + time[2] + "' AND '" + time[3] + "'),'') AS swxb,");
            sql.append("IFNULL((SELECT MIN(sbsj190720001) FROM rlsbview WHERE curStatus=2 AND sbry190720001='"+userId+"' AND sbsj190720001 BETWEEN '" + time[4] + "' AND '" + time[5] + "'),'') AS xwsb,");
            sql.append("IFNULL((SELECT MAX(sbsj190720001) FROM rlsbview WHERE curStatus=2 AND sbry190720001='"+userId+"' AND sbsj190720001 BETWEEN '" + time[6] + "' AND '" + time[7] + "'),'') AS xwxb,");
            sql.append("'"+ymd+"' AS recordTime");
            sql.append(" FROM dual;");

            /*String sql = "select recorderNO, jlr1811200001, " +
                    "DATE_FORMAT(swsb181120001,'%H:%i:%s') swsb181120001,\n" +
                    "DATE_FORMAT(swxb181120001,'%H:%i:%s') swxb181120001,\n" +
                    "DATE_FORMAT(xwsb181120001,'%H:%i:%s') xwsb181120001,\n" +
                    "DATE_FORMAT(xwxb181120001,'%H:%i:%s') xwxb181120001, zt18112000001, qt18112000001 from ygkqj18112001\n" +
                    "where jlr1811200001 = '" + userId + "' and curStatus = 2\n" +
                    "and DATE_FORMAT(IFNULL(IFNULL(IFNULL(swsb181120001,swxb181120001),xwsb181120001),xwxb181120001),'%Y-%m-%d') = '" + year + "-" + month + "-" + day + "'";*/
            List<Map<String, Object>> list = tableService.selectSqlMapList(sql.toString());
            if (list != null && list.size() > 0) {
                Map<String, Object> map = list.get(0);
                object.put("data", map);
                object.put("count", 1);
            } else {//今天未打卡
                object.put("count", 0);
            }
            object.put("success", true);
            Map<String, Object> map2 = new HashMap<>(4);
            map2.put("swsb", amtowork);
            map2.put("swxb", amoffwork);
            map2.put("xwsb", pmtowork);
            map2.put("xwxb", pmoffwork);
            object.put("map2", map2);
        } catch (Exception e) {
            e.printStackTrace();
            object.put("success", false);
        }
        return object.toString();
    }

    /**
     * @method: saveWxdaka
     * @param: name 打卡人姓名
     * @param: id 打卡人编号
     * @param: timestamp 打卡时间
     * @param: file 照片流
     * @return: object
     * @author: zhuchengyong
     * @date: 2019/08/03
     * @description: 存入app端的签到信息
     */
    @RequestMapping(value = "/saveWxdaka",method = RequestMethod.POST, produces = {"text/html;charset=UTF-8;", "application/json;"})
    @ResponseBody
    public String saveWxdaka(HttpServletRequest request,@RequestParam("file") MultipartFile file){
        try {
            String user = request.getParameter("user");
            String datetime = request.getParameter("datetime");
            Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
            if(user == null  || user == ""){
                user = loginer.getId();
            }
            String fields = "gh19062100001;reserveField;";
            List<String> where = new ArrayList();
            where.add("xm19062100001='"+user+"'");
            Map<String, Object> sqlMap = tableService.selectSqlMap(MySqlUtil.getSql(StringHelper.string2ArrayList(fields, ";"), "afxtx19062101", where));
            //获取照片流
            String src = "";
            String file2path = "";
            if(!file.isEmpty()){
                ConfParseUtil cp = new ConfParseUtil();
                String bendipath = cp.getProperty("upload_file");
                file2path = bendipath+"upload/temp/"+datetime+file.getOriginalFilename();
                File file2 = new File(file2path);
                String file3path = bendipath+"upload/temp/1/"+datetime+file.getOriginalFilename();
                FileUtils.copyInputStreamToFile(file.getInputStream(), file2);
                /*file.transferTo(new File(file2));*/
                Thumbnails.of(file2).scale(1f).outputQuality(0.05f).toFile(file3path);
                src = BaseImageUtil.getImageStr(file3path);
            }
            //将app端传过来的打卡参数转换成json对象，调用MyWebSocketClient类中的settable方法将其存入数据库
            JSONObject person = new JSONObject();
            person.put("name",sqlMap.get("reserveField"));
            person.put("id",sqlMap.get("gh19062100001"));
            person.put("job_number",user);
            person.put("timestamp",datetime);
            person.put("src",src);
            MyWebSocketClient.settable(person);
            //将app端传过来的打卡参数存入另外的一个单独表
            /*String userName = WebSocketListener.getData(String.valueOf(person.getInt("id")));
            if (userName == null || userName.equals("")) {
                userName = person.getString("job_number");
            }*/
            Map<String, Object> map = new HashMap<>();
            PrimaryKeyUitl pk = new PrimaryKeyUitl();
            map.put("recorderNO", pk.getNextId("sjqdj19080302", "recorderNO"));
            map.put("sbrbh19080301", sqlMap.get("gh19062100001"));
            map.put("sbry190803001", user);
            map.put("sbrxm19080301", sqlMap.get("reserveField"));
            long time = Long.parseLong(datetime);
            Timestamp specifiedTime = DateHelper.Long2Timestamp(time, true);
            map.put("sbsj190803001", specifiedTime);
            map.put("sbrzp19080301", file2path);
            map.put("recordName", "admin");
            map.put("recordTime", DateHelper.now());
            map.put("modifyName", "admin");
            map.put("modifyTime", DateHelper.now());
            String sjqdj19080302 = MySqlUtil.getInsertSql("sjqdj19080302", map);
            tableService.insertSqlMap(sjqdj19080302);
            //返回数据
            JSONObject object = new JSONObject();
            object.put("message", "签到成功！");
            object.put("success", "1");
            return object.toString();
        }catch (Exception e){
            JSONObject object = new JSONObject();
            object.put("message", "签到失败！");
            object.put("success", "0");
            return object.toString();
        }
    }

    /**
     * @method: saveqdxinxi
     * @param: user 签到人姓名
     * @param: datatime 签到时间
     * @param: weizhi  位置信息
     * @return: object
     * @author: zhuchengyong
     * @date: 2019/08/05
     * @description: 存入app端的打卡位置信息
     */
    @RequestMapping(value = "/saveqdxinxi",method = RequestMethod.POST, produces = {"text/html;charset=UTF-8;"})
    @ResponseBody
    public String saveqdxinxi(HttpServletRequest request){
        try{
            String user = request.getParameter("user");
            Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
            if(user == null  || user == ""){
                user = loginer.getId();
            }
            JSONObject object = new JSONObject();
            /*String datetime = request.getParameter("datetime");*/
            String weizhi = request.getParameter("weizhi");
            String aa = new String(request.getParameter("weizhi").getBytes("ISO-8859-1"), "UTF-8");
            Map<String, Object> map = new HashMap<>();
            PrimaryKeyUitl pk = new PrimaryKeyUitl();
            map.put("recorderNO", pk.getNextId("qdwzx19080501", "recorderNO"));
            map.put("qdr1908050001", user);
            String datetime = request.getParameter("datetime");
            if(datetime != null && datetime != ""){
                long time = Long.parseLong(datetime);
                Timestamp specifiedTime = DateHelper.Long2Timestamp(time, true);
                map.put("qdsj190805001",specifiedTime);
            }else{
                object.put("message", "获取时间信息失败！");
                object.put("success", "0");
                return object.toString();
            }
            if(weizhi != null && weizhi != ""){
                map.put("qdwz190805001",aa);
            }else{
                object.put("message", "获取位置信息失败！");
                object.put("success", "0");
                return object.toString();
            }
            String qdwzx19080501 = MySqlUtil.getInsertSql("qdwzx19080501", map);
            tableService.insertSqlMap(qdwzx19080501);
            //返回数据
            object.put("message", "seccess!!!");
            object.put("success", "1");
            return object.toString();
        }catch (Exception e){
            JSONObject object = new JSONObject();
            object.put("message", "failed!!!");
            object.put("success", "0");
            return object.toString();
        }
    }
}