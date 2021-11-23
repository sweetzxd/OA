package com.oa.core.controller.module;

import com.google.gson.JsonObject;
import com.oa.core.bean.Loginer;
import com.oa.core.bean.module.Joblog;
import com.oa.core.bean.module.Message;
import com.oa.core.bean.system.TaskSender;
import com.oa.core.bean.util.PageUtil;
import com.oa.core.helper.DateHelper;
import com.oa.core.helper.FileHelper;
import com.oa.core.service.module.DepartmentService;
import com.oa.core.service.module.JoblogService;
import com.oa.core.service.module.MessageService;
import com.oa.core.service.system.TaskSenderService;
import com.oa.core.util.LogUtil;
import com.oa.core.util.PrimaryKeyUitl;
import com.oa.core.util.ToNameUtil;
import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName:JoblogController
 * @author:wsx
 * @Date:2018/11/29 0029
 * @Time:上午 10:24
 * @Version V1.0
 * @Explain
 */
@Controller
@RequestMapping("/weixin/joblog")
public class WxJoblogController {

    @Autowired
    JoblogService joblogService;
    @Autowired
    DepartmentService departmentService;
    @Autowired
    TaskSenderService taskSenderService;
    @Autowired
    MessageService messageService;

    /**
     * 移动端获取月日志信息 个人当前登录人的填写日志信息 按月查看记录
     * @param request
     * @param
     * @return String
     */
    @RequestMapping(value = "/selectjoblogbymonth", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String selectJobLogByMonth(HttpServletRequest request,String year,String month){
        String userId = request.getParameter("userId");
        if(userId==null || userId.equals("")) {
            Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
            userId = loginer.getId();
        }
        //定义查询条件,将其装到map中
        Map<String,Object> map = new HashMap<>();
        SimpleDateFormat df1 = new SimpleDateFormat("yyyy");
        SimpleDateFormat df2 = new SimpleDateFormat("MM");
        if(StringUtils.isBlank(year)){
            year = df1.format(new Date());
        }
        if(StringUtils.isBlank(month)){
            month = df2.format(new Date());
        }
        map.put("year",year);
        map.put("month",month);
        map.put("userid",userId);
        //定义返回值
        List<Map<String, Object>> list = joblogService.selectJobLogByMonth(map);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 0);
        jsonObject.put("msg", "");
        jsonObject.put("data", list);
        jsonObject.put("success",1);
        return jsonObject.toString();
    }

    /**
     * 获取每天的日志信息 个人(当前登录人) 按天查看日志信息
     * @param request
     * @param year
     * @param month
     * @param day
     * @return String
     */
    @RequestMapping(value = "/selectjoblogbyday", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String selectJoblogByDay(HttpServletRequest request,String year,String month,String day){
        String userId = request.getParameter("userId");
        if(userId==null || userId.equals("")) {
            Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
            userId = loginer.getId();
        }
        //定义查询条件,将其装到map中
        Map<String,Object> map = new HashMap<>();
        SimpleDateFormat df1 = new SimpleDateFormat("yyyy");
        SimpleDateFormat df2 = new SimpleDateFormat("MM");
        SimpleDateFormat df3 = new SimpleDateFormat("dd");
        if(StringUtils.isBlank(year)){
            year = df1.format(new Date());
        }
        if(StringUtils.isBlank(month)){
            month = df2.format(new Date());
        }
        if(StringUtils.isBlank(day)){
            day = df3.format(new Date());
        }
        map.put("year",year);
        map.put("month",month);
        map.put("day",day);
        map.put("userid",userId);
        //定义返回值
        List<Map<String, Object>> list = joblogService.selectJoblogByDay(map);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 0);
        jsonObject.put("msg", "");
        jsonObject.put("data", list);
        jsonObject.put("success",1);
        return jsonObject.toString();
    }

    /**
     * 移动端获取月日志信息 抄送人是当前登录人  按月查看
     * @param request
     * @param
     * @return String
     */
    @RequestMapping(value = "/selectjoblogbycsusermonth", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String selectJoblogByCsuserMonth(HttpServletRequest request,String year,String month){
        String userId = request.getParameter("userId");
        if(userId==null || userId.equals("")) {
            Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
            userId = loginer.getId();
        }
        //定义查询条件,将其装到map中
        Map<String,Object> map = new HashMap<>();
        SimpleDateFormat df1 = new SimpleDateFormat("yyyy");
        SimpleDateFormat df2 = new SimpleDateFormat("MM");
        if(StringUtils.isBlank(year)){
            year = df1.format(new Date());
        }
        if(StringUtils.isBlank(month)){
            month = df2.format(new Date());
        }
        map.put("year",year);
        map.put("month",month);
        map.put("userid",userId);
        //定义返回值
        List<Map<String, Object>> list = joblogService.selectJoblogByCsuserMonth(map);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 0);
        jsonObject.put("msg", "");
        jsonObject.put("data", list);
        jsonObject.put("success",1);
        return jsonObject.toString();
    }

    /**
     * 按用户id获取日志信息,抄送人当前登录人是抄送人或者是审批人  按日查看抄送过得日志或者是自己审批过的日志
     */
    @RequestMapping(value = "/selectjoblogbycsuser", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String selectJobLogByCsuser(HttpServletRequest request,String year,String month,String day) {
        String userId = request.getParameter("userId");
        String user = request.getParameter("staffname");
        String deptname = request.getParameter("deptname");
        String rolename = request.getParameter("rolename");
        if(userId==null || userId.equals("")) {
            Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
            userId = loginer.getId();
        }
        //定义查询条件,将其装到map中
        Map<String,Object> map = new HashMap<>();
        SimpleDateFormat df1 = new SimpleDateFormat("yyyy");
        SimpleDateFormat df2 = new SimpleDateFormat("MM");
        SimpleDateFormat df3 = new SimpleDateFormat("dd");
        if(StringUtils.isBlank(year)){
            year = df1.format(new Date());
        }
        if(StringUtils.isBlank(month)){
            month = df2.format(new Date());
        }
        if(StringUtils.isBlank(day)){
            day = df3.format(new Date());
        }
        map.put("year",year);
        map.put("month",month);
        map.put("day",day);
        map.put("userid",userId);
        map.put("user",user);
        map.put("deptname",deptname);
        map.put("rolename",rolename);
        //定义返回值
        List<Map<String, Object>> list = joblogService.selectJobLogByCsuser(map);
       /* for (Map<String, Object> joblog : list) {
            String csUserStr = ToNameUtil.getName("user", joblog.getCsUserStr());
            joblog.setCsUserStr(csUserStr);
        }*/
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 0);
        jsonObject.put("msg", "");
        jsonObject.put("data", list);
        jsonObject.put("success",1);
        return jsonObject.toString();
    }

    /**
     * 按用户id获取日志信息, 审批人当前登录人是审批人  分页查询所有审批人是自己的日志
     */
    @RequestMapping(value = "/selectjoblogbyleader", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String selectJobLogByLeader(HttpServletRequest request,int page,int limit) {
        String userId = request.getParameter("userId");
        String staffname = request.getParameter("staffname");
        String deptname = request.getParameter("deptname");
        String rolename = request.getParameter("rolename");
        if(userId==null || userId.equals("")) {
            Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
            userId = loginer.getId();
        }
        //定义查询条件,将其装到map中
        PageUtil pu = new PageUtil();
        pu.setPageSize(limit);
        pu.setCurrentPage(page);
        int StartRow = pu.getStartRow();
        int EndRow = pu.getEndRow() - pu.getStartRow();
        Map<String,Object> map = new HashMap<>();

        map.put("StartRow",StartRow);
        map.put("EndRow",EndRow);
        map.put("userId",userId);
        //定义返回值
        try{
            List<Map<String, Object>> list = joblogService.selectJobLogByLeader(map);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("msg", "");
            jsonObject.put("count", list.size());
            jsonObject.put("data", list);
            jsonObject.put("success",1);
            return jsonObject.toString();
        }catch (Exception e){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("msg", "检索失败");
            jsonObject.put("success",0);
            return jsonObject.toString();
        }
    }

    /**
     * 移动端获取月日志信息  审批人当前登录人是审批人  按月查看审批人是自己的日志
     * @param request
     * @param
     * @return String
     */
    @RequestMapping(value = "/selectjoblogbyleadermonth", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String selectJobLogByLeaderMonth(HttpServletRequest request,String year,String month){
        String userId = request.getParameter("userId");
        if(userId==null || userId.equals("")) {
            Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
            userId = loginer.getId();
        }
        //定义查询条件,将其装到map中
        Map<String,Object> map = new HashMap<>();
        SimpleDateFormat df1 = new SimpleDateFormat("yyyy");
        SimpleDateFormat df2 = new SimpleDateFormat("MM");
        if(StringUtils.isBlank(year)){
            year = df1.format(new Date());
        }
        if(StringUtils.isBlank(month)){
            month = df2.format(new Date());
        }
        map.put("year",year);
        map.put("month",month);
        map.put("userid",userId);
        //定义返回值
        List<Map<String, Object>> list = joblogService.selectJobLogByLeaderMonth(map);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 0);
        jsonObject.put("msg", "");
        jsonObject.put("data", list);
        jsonObject.put("success",1);
        return jsonObject.toString();
    }

    /**
     * 获取每天的日志信息 审批人当前登录人是审批   按天查看审批人是自己的所有日志
     * @param request
     * @param year
     * @param month
     * @param day
     * @return String
     */
    @RequestMapping(value = "/selectjoblogbyleaderday", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String selectJoblogByLeaderDay(HttpServletRequest request,String year,String month,String day){
        String userId = request.getParameter("userId");
        String user = request.getParameter("staffname");
        String deptname = request.getParameter("deptname");
        String rolename = request.getParameter("rolename");
        if(userId==null || userId.equals("")) {
            Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
            userId = loginer.getId();
        }
        //定义查询条件,将其装到map中
        Map<String,Object> map = new HashMap<>();
        SimpleDateFormat df1 = new SimpleDateFormat("yyyy");
        SimpleDateFormat df2 = new SimpleDateFormat("MM");
        SimpleDateFormat df3 = new SimpleDateFormat("dd");
        if(StringUtils.isBlank(year)){
            year = df1.format(new Date());
        }
        if(StringUtils.isBlank(month)){
            month = df2.format(new Date());
        }
        if(StringUtils.isBlank(day)){
            day = df3.format(new Date());
        }
        map.put("year",year);
        map.put("month",month);
        map.put("day",day);
        map.put("userid",userId);
        map.put("user",user);
        map.put("deptname",deptname);
        map.put("rolename",rolename);
        //定义返回值
        List<Map<String, Object>> list = joblogService.selectJoblogByLeaderDay(map);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 0);
        jsonObject.put("msg", "");
        jsonObject.put("data", list);
        jsonObject.put("success",1);
        return jsonObject.toString();
    }

    /**
     * 打回的日志信息展示
     * @param request
     * @return
     */
    @RequestMapping(value = "/selectjobrecno",  method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String selectJobRecno(HttpServletRequest request) {
        String recno = request.getParameter("recno");
        Joblog joblog = joblogService.selectById(recno);
        JSONObject jsonObject = new JSONObject(joblog);
        return jsonObject.toString();
    }

    /**
     * 添加日志
     * @param request
     * @param joblog
     * @return
     */
    @RequestMapping(value = "/insert", method = RequestMethod.POST, produces = {"text/html;charset=UTF-8;", "application/json;"})
    @ResponseBody
    public String insertSave(HttpServletRequest request, Joblog joblog) {
        String userid = request.getParameter("userid");
        if(userid==null || userid.equals("")) {
            Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
            userid = loginer.getId();
        }
        try {
            PrimaryKeyUitl pk = new PrimaryKeyUitl();
            String permissionId = pk.getNextId("joblog", "joblogId");
            joblog.setJoblogId(permissionId);
            joblog.setUser(userid);
            joblog.setState(1);
            joblog.setRecordName(userid);
            joblog.setModifyName(userid);
            joblog.setRecordTime(DateHelper.now());
            joblog.setModifyTime(DateHelper.now());
            joblogService.insert(joblog);
            return "1";
        } catch (Exception e) {
            LogUtil.sysLog("Exception:"+e);
            return "0";
        }
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST, produces = {"text/html;charset=UTF-8;", "application/json;"})
    @ResponseBody
    public String updateSave(HttpServletRequest request, Joblog joblog) {
        String userId = request.getParameter("userId");
        String type = request.getParameter("type");
        if(type!=null && type.equals("1")){
            String joblog3 = request.getParameter("joblog");
            String state = request.getParameter("state");
            String reason = request.getParameter("reason");
            int s = 1;
            if(state!=null){
                s = Integer.parseInt(state);
            }
            //{"csUser":"zhangxu","joblogId":"Z2019060600005","leader":"zhangxu","state":1}
            JSONObject json = new JSONObject(joblog3);
            joblog.setCsUser(json.getString("csUser"));
            joblog.setJoblogId(json.getString("joblogId"));
            joblog.setLeader(json.getString("leader"));
            joblog.setReason(reason==null?"":reason);
            joblog.setState(s);
        }

        if(userId==null || userId.equals("")) {
            Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
            userId = loginer.getId();
        }
        try {
            joblog.setModifyName(userId);
            joblog.setModifyTime(DateHelper.now());
            joblogService.update(joblog);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("code", 0);
            jsonObject.put("msg", "");
            jsonObject.put("success",1);
            return jsonObject.toString();
        } catch (Exception e) {
            LogUtil.sysLog("Exception:"+e);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("code", 0);
            jsonObject.put("msg", "");
            jsonObject.put("success",0);
            return jsonObject.toString();
        }
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST, produces = {"text/html;charset=UTF-8;", "application/json;"})
    @ResponseBody
    public String delete(HttpServletRequest request, String joblogId) {
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        try {
            String userId = loginer.getId();
            joblogService.delete(joblogId,userId,DateHelper.now());
            return "1";
        } catch (Exception e) {
            LogUtil.sysLog("Exception:"+e);
            return "0";
        }
    }
    @RequestMapping(value = "/gotoCheckJoblog", method = RequestMethod.GET)
    public ModelAndView gotoCheckJoblog(HttpServletRequest request) {
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        ModelAndView mav = new ModelAndView("module/checkJoblog");
        mav.addObject("user",loginer.getId());
        mav.addObject("type","list");
        return mav;
    }

    public static String getHtmlInformare(Joblog joblog){
        String tableHtml = "";
        tableHtml += "<table class='layui-table' lay-skin='line' lay-size='sm'>";
        tableHtml += "<colgroup>";
        for (int i = 0; i < 2; i++) {
            tableHtml += "<col width='70'>";
            tableHtml += "<col width='120'>";
        }
        tableHtml += "</colgroup>";
        /*tableHtml += "<thead>";*/
        tableHtml += "<tr>";
        tableHtml += "<th colspan='4' hight='auto'>" + joblog.getContent() + "</th>";
        tableHtml += "</tr>";
        /*tableHtml += "</thead>";*/
        tableHtml += "<tbody>";
        tableHtml += "</tbody>";
        tableHtml += "</table>";
        return tableHtml;
    }

}
