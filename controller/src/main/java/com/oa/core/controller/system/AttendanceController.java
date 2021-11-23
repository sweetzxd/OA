package com.oa.core.controller.system;

import com.alibaba.dubbo.common.extension.Activate;
import com.oa.core.bean.system.Attendance;
import com.oa.core.helper.DateHelper;
import com.oa.core.service.util.TableService;
import com.oa.core.util.AttenceUtil;
import com.oa.core.util.ConfParseUtil;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

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
        System.out.println(param);
        JSONObject jo = new JSONObject(param);
        String jobnumber = jo.getString("sdwEnrollNumber");
        Map<String, Object> map = tableService.selectSqlMap("select userId from jobtable where curStatus=2 and jobnumber='" + jobnumber + "'");
        String name = jo.getString("sName");
        if(!jo.isNull("VerifyMethod")) {
            String type = String.valueOf(jo.get("VerifyMethod"));
        }
        String year = String.valueOf(jo.get("Year"));
        String month = String.valueOf(jo.get("Month"));
        if(month.length()<2){
            month = "0"+month;
        }
        String day = String.valueOf(jo.get("Day"));
        if(day.length()<2){
            day = "0"+day;
        }
        String hour = String.valueOf(jo.get("Hour"));
        if(hour.length()<2){
            hour = "0"+hour;
        }
        String minute = String.valueOf(jo.get("Minute"));
        if(minute.length()<2){
            minute = "0"+minute;
        }
        String second = String.valueOf(jo.get("Second"));
        if(second.length()<2){
            second = "0"+second;
        }
        String datatime = year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
        return AttenceUtil.attence((String)map.get("userId"), datatime);
    }
}
