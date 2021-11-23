package com.oa.core.controller.module;

import com.oa.core.bean.Loginer;
import com.oa.core.service.util.TableService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @ClassName:ScheduleController
 * @author:zxd
 * @Date:2018/11/19
 * @Time:下午 4:24
 * @Version V1.0
 * @Explain 作息时间、例外日历、考勤
 */
@Controller
@RequestMapping("/weixin/schedule")
public class WxScheduleController {
    @Autowired
    TableService tableService;

    @RequestMapping(value = "/getScheduleList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String getDetailList(HttpServletRequest request,String date,int page,int limit) {
        JSONObject jsonObject = new JSONObject();
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        String userId = null;
        if(loginer == null){
            userId = request.getParameter("userid");
        }else{
            userId = loginer.getId();
        }
        //查询日程列表
        String sql = " select * from schedule \n" +
                "               where user = '"+userId+"' and curStatus = 2\n" +
                "               and DATE_FORMAT(startTime,'%Y-%m-%d') <= '"+date+"'\n" +
                "               and DATE_FORMAT(endTime,'%Y-%m-%d') >= '"+date+"' ORDER BY startTime limit "+(page-1)*limit+","+limit+"  ;";
        List<Map<String, Object>> taskList = tableService.selectSqlMapList(sql);
        //jsonObject.put("taskList",taskList);
        jsonObject.put("count", taskList.size());
        jsonObject.put("data", taskList);
        jsonObject.put("msg", "");
        jsonObject.put("success",1);
        return jsonObject.toString();
    }
}
