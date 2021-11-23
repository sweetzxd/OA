package com.oa.core.controller.module.management;


import com.oa.core.helper.DateHelper;
import com.oa.core.service.util.TableService;
import com.oa.core.util.Meetutil;
import com.oa.core.util.ToNameUtil;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/meeting")
public class MeetingController {
    @Autowired
    TableService tableService;

    /*会议室日历*/
    @RequestMapping(value = "/meetnum", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String meetcalendar(HttpServletRequest request, String year) {
        if (year == null || year.equals("")) {
            year = DateHelper.getYM();
        }
        String jsonpCallback = request.getParameter("jsonpCallback");
        JSONArray json = Meetutil.meetcdate(request, year);
        return jsonpCallback + "(" + json.toString() + ")";
    }

    /*用车情况说明*/
    @RequestMapping(value = "/usecar", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String cardesc(HttpServletRequest request, String year) {
        if (year == null || year.equals("")) {
            year = DateHelper.getYM();
        }
        String jsonpCallback = request.getParameter("jsonpCallback");
        JSONArray json = Meetutil.Carcalendar(request, year);
        return jsonpCallback + "(" + json.toString() + ")";
    }
}
