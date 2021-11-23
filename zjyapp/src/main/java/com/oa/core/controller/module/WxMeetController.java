package com.oa.core.controller.module;

import com.oa.core.helper.DateHelper;
import com.oa.core.service.util.TableService;
import com.oa.core.util.Meetutil;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName:WxMeetController
 * @author:zxd
 * @Date:2019/05/25
 * @Time:上午 10:20
 * @Version V1.0
 * @Explain
 */
@Controller
@RequestMapping("/weixin/meeting")
public class WxMeetController {

    @Autowired
    TableService tableService;

    @RequestMapping(value = "/getmeetnum", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String getmeetcalendar(HttpServletRequest request, String userid, String year, String month) {
        String date = year + "-" + month;
        JSONObject jsonObject = new JSONObject();
        try {
            JSONObject json = Meetutil.getmeetc(date, userid);
            jsonObject.put("success", 1);
            jsonObject.put("msg", "");
            jsonObject.put("data", json);
            return jsonObject.toString();
        } catch (Exception e) {
            e.getLocalizedMessage();
            jsonObject.put("success", 0);
            jsonObject.put("msg", "查询失败");
            return jsonObject.toString();
        }
    }

    /*会议室日历*/
    @RequestMapping(value = "/meetnum", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String meetcalendar(HttpServletRequest request, String userid, String year, String month, String day) {
        List<String> where = new ArrayList<>();
        where.add("b.sqr1904180001='" + userid + "'");
        String type = "%Y-%m-%d";
        String date = year + "-" + month + "-" + day;
        if (day == null || day.equals("")) {
            type = "%Y-%m";
            date = year + "-" + month;
        }
        JSONArray json = Meetutil.meetcdate(request, date, type, where);
        return getJson(request, year, where, json);
    }

    /*用车情况说明*/
    @RequestMapping(value = "/usecar", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String cardesc(HttpServletRequest request, String userid, String year, String month, String day) {
        List<String> where = new ArrayList<>();
        where.add("t.recordName='" + userid + "'");
        String type = "%Y-%m-%d";
        String date = year + "-" + month + "-" + day;
        if (day == null || day.equals("")) {
            type = "%Y-%m";
            date = year + "-" + month;
        }
        JSONArray json = Meetutil.Carcalendar(request, date, type, where);
        return getJson(request, year, where, json);
    }

    public String getJson(HttpServletRequest request, String year, List<String> where, JSONArray json) {
        if (year == null || year.equals("")) {
            year = DateHelper.getYM();
        }
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("success", 1);
            jsonObject.put("msg", "");
            jsonObject.put("data", json);
            return jsonObject.toString();
        } catch (Exception e) {
            e.getLocalizedMessage();
            jsonObject.put("success", 0);
            jsonObject.put("msg", "查询失败");
            return jsonObject.toString();
        }
    }
}
