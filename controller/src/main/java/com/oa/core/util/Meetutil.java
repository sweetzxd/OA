package com.oa.core.util;

import com.google.gson.JsonObject;
import com.oa.core.service.util.TableService;
import org.checkerframework.checker.units.qual.C;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public class Meetutil {
    private static TableService tableService = (TableService) SpringContextUtil.getBean("tableService");

    public static JSONObject getmeetc(String date, String user) {
        JSONObject json = new JSONObject();
        List<String> cardata = tableService.selectSql("SELECT DATE_FORMAT( t.sqsj190418003, '%d' ) AS sterttime FROM txycs19041101 t,clgly19041101 c,clxx190517001 cx,jsrxx19051701 j WHERE t.workflowProcID = c.workflowProcID AND c.sjxm190517001 = j.recorderNO AND c.cph1905170002 = cx.recorderNO AND t.recordName = '" + user + "' AND DATE_FORMAT( sqsj190418003, '%Y-%m' ) = '" + date + "' GROUP BY sterttime");
        List<String> meetdata = tableService.selectSql("SELECT DATE_FORMAT( b.kssj1904180026, '%d' ) AS begintime FROM hysgl19041501 AS a,hystz19041801 AS b WHERE a.recorderNO = b.hysbh19041801 AND b.curStatus = 2 AND b.sqr1904180001='" + user + "' AND DATE_FORMAT( b.kssj1904180026, '%Y-%m' ) = '" + date + "' GROUP BY begintime ");
        json.put("car", cardata);
        json.put("meet", meetdata);
        return json;
    }

    /*
     * 会议日历
     * */
    public static JSONArray meetcdate(HttpServletRequest request, String date) {
        return meetcdate(request, date, "%Y-%m");
    }

    public static JSONArray meetcdate(HttpServletRequest request, String date, String type) {
        return meetcdate(request, date, type, null);
    }

    public static JSONArray meetcdate(HttpServletRequest request, String date, String type, List<String> wheres) {
        String where = "";
        if (where != null) {
            for (int i = 0, len = wheres.size(); i < len; i++) {
                where += wheres.get(i) + " AND ";
            }
        }
        List<Map<String, Object>> tableList = tableService.selectSqlMapList("SELECT b.recorderNO as recorderNO, b.hymc190418003 AS meetname,b.sqr1904180001 AS meetpeople,DATE_FORMAT(b.kssj1904180026,'%Y-%m-%d %H:%i') AS begintime,a.hysmc19041501 as meetnum,DATE_FORMAT(b.jssj1904180026,'%Y-%m-%d %H:%i') AS endtime,a.hysdz19041501 AS meetaddress,b.sywp190418003 AS meetwp,b.dwjsc19041805 AS fujian,b.chrs190418003 AS numpeople FROM hysgl19041501 AS a,hystz19041801 AS b WHERE a.recorderNO = b.hysbh19041801 AND b.curStatus = 2 AND " + where + " DATE_FORMAT(b.kssj1904180026, '" + type + "') = '" + date + "'");
        JSONArray json = new JSONArray();
        int i = 0;

        for (Map<String, Object> sp : tableList) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", sp.get("recorderNO"));
            String title = sp.get("meetname") + "【" + sp.get("meetnum") + "" + "】";
            jsonObject.put("title", title);
            jsonObject.put("start", sp.get("begintime"));
            jsonObject.put("starts", sp.get("begintime"));
            jsonObject.put("end", sp.get("endtime"));
            jsonObject.put("ends", sp.get("begintime"));
            jsonObject.put("meetpeople", ToNameUtil.getName("user", sp.get("meetpeople")));
            jsonObject.put("meetaddress", sp.get("meetaddress"));
            jsonObject.put("meetwp", sp.get("meetwp"));
            jsonObject.put("fujian", sp.get("fujian"));
            jsonObject.put("meetpersonnel",  sp.get("numpeople"));
            i = i + 1;
            if (i > 3) {
                i = 1;
            }
            jsonObject.put("type", i);
            json.put(jsonObject);
        }
        return json;
    }

    /*
     * 车辆日历
     * */
    public static JSONArray Carcalendar(HttpServletRequest request, String date) {
        return Carcalendar(request, date, "%Y-%m");
    }

    public static JSONArray Carcalendar(HttpServletRequest request, String date, String type) {
        return meetcdate(request, date, type, null);
    }

    public static JSONArray Carcalendar(HttpServletRequest request, String date, String type, List<String> wheres) {
        String where = "";
        if (where != null) {
            for (int i = 0, len = wheres.size(); i < len; i++) {
                where += wheres.get(i) + " AND ";
            }
        }
        List<Map<String, Object>> tableList = tableService.selectSqlMapList("SELECT t.recorderNo as recorderNo, cx.cph1905170001 as carnumber,cx.clys190517001 as carcolor,cx.cltp190517001 as carpicture,j.xm19051700001 as driver,j.xb19051700001 as sex,j.nl19051700001 as age,j.jsztp19051701 as drpicture,j.jl19051700001 as drage,t.mdd1904170001 as destination,DATE_FORMAT(t.sqsj190418003, '%Y-%m-%d %H:%i') as sterttime,t.ycsqsm as whycardesc,t.dcrs190418003 as peoplenum FROM txycs19041101 t,clgly19041101 c,clxx190517001 cx,jsrxx19051701 j WHERE t.workflowProcID = c.workflowProcID AND c.sjxm190517001 = j.recorderNO AND c.cph1905170002 = cx.recorderNO AND " + where + " DATE_FORMAT(sqsj190418003, '" + type + "') = '" + date + "'");
        JSONArray json = new JSONArray();
        int i = 0;

        for (Map<String, Object> sp : tableList) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", sp.get("recorderNO"));
//            String title=sp.get("meetname")+"【"+sp.get("meetnum")+""+"】";
            jsonObject.put("title", sp.get("carnumber"));
            jsonObject.put("start", sp.get("sterttime"));
            jsonObject.put("end", sp.get("sterttime"));
            jsonObject.put("sterttime", sp.get("sterttime"));
            jsonObject.put("whycardesc", sp.get("whycardesc"));
            jsonObject.put("peoplenum", sp.get("peoplenum"));
            jsonObject.put("destination", sp.get("destination"));
//            jsonObject.put("carnumber", sp.get("carnumber"));
            jsonObject.put("carcolor", sp.get("carcolor"));
            jsonObject.put("driver", sp.get("driver"));
            jsonObject.put("sex", sp.get("sex"));
            jsonObject.put("age", sp.get("age"));
            jsonObject.put("drage", sp.get("drage"));
            jsonObject.put("carpicture", sp.get("carpicture"));
            jsonObject.put("drpicture", sp.get("drpicture"));
            i = i + 1;
            if (i > 3) {
                i = 1;
            }
            jsonObject.put("type", i);
            json.put(jsonObject);
        }
        return json;
    }


}
