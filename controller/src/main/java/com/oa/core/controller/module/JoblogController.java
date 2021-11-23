package com.oa.core.controller.module;

import com.google.gson.JsonObject;
import com.oa.core.bean.Loginer;
import com.oa.core.bean.module.Department;
import com.oa.core.bean.module.Joblog;
import com.oa.core.bean.module.Message;
import com.oa.core.bean.system.TaskSender;
import com.oa.core.bean.util.PageUtil;
import com.oa.core.helper.DateHelper;
import com.oa.core.helper.FileHelper;
import com.oa.core.helper.JsonResult;
import com.oa.core.service.module.DepartmentService;
import com.oa.core.service.module.JoblogService;
import com.oa.core.service.module.MessageService;
import com.oa.core.service.system.TaskSenderService;
import com.oa.core.util.LogUtil;
import com.oa.core.util.PrimaryKeyUitl;
import com.oa.core.util.SpringContextUtil;
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
import java.util.*;

/**
 * @ClassName:JoblogController
 * @author:wsx
 * @Date:2018/11/29 0029
 * @Time:上午 10:24
 * @Version V1.0
 * @Explain
 */
@Controller
@RequestMapping("/joblog")
public class JoblogController {

    @Autowired
    JoblogService joblogService;
    @Autowired
    DepartmentService departmentService;
    @Autowired
    TaskSenderService taskSenderService;
    @Autowired
    MessageService messageService;

    @RequestMapping(value = "/gotoJoblogList", method = RequestMethod.GET)
    public ModelAndView gotoJoblogList(HttpServletRequest request, String type, String joblogId) {
        ModelAndView mav = new ModelAndView("module/joblog");
        if(type.equals("edit")){
            Joblog joblog = joblogService.selectById(joblogId);
            String leaderStr = ToNameUtil.getName("user", joblog.getLeader());
            joblog.setLeaderStr(leaderStr);
            String csUserStr = ToNameUtil.getName("user", joblog.getCsUser());
            joblog.setCsUserStr(csUserStr);
            mav.addObject("joblog",joblog);
            String fieldValue = joblog.getFile();
            String filesHtml = "";
            if (fieldValue != null && fieldValue.contains("|")) {
                String[] files = fieldValue.split("\\|");
                for (int i = 0; i < files.length; i++) {
                    java.io.File f = new java.io.File(files[i]);
                    String fileName = f.getName();
                    fileName = fileName.substring(fileName.indexOf("-") + 1);
                    filesHtml += "<tr id='upload-" + i + "'><td>";
                    filesHtml += "<a href='/" + files[i] + "' data-value='" + files[i] + "' download='" + fileName + "' title='点击下载 " + fileName + "'>" + fileName + "</a></td>";
                    filesHtml += "<td>" + FileHelper.getPrintSize(f.length()) + "</td>";
                    filesHtml +="<td><span style='color: #5FB878'>上传成功</span></td>";
                    filesHtml += "<td><a class='layui-btn layui-btn-xs layui-btn-danger upload-delete' herf='javascript:void(0)' onclick=\"removefile('upload-" + i + "','uploads_fileAdd')\">删除</a></td></tr>";
                }
            }
            mav.addObject("filesHtml",filesHtml);
        }
        mav.addObject("type",type);
        return mav;
    }

    @RequestMapping("/selectlist")
    @ResponseBody
    public String getJoblogList(HttpServletRequest request, String inputval, String option, int page, int limit,String leader,String csUser) {
        PageUtil pu = new PageUtil();
        pu.setPageSize(limit);
        pu.setCurrentPage(page);
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        Joblog joblog = new Joblog();
            if (inputval != null && inputval != "") {
            if ("state".equals(option)) {
                joblog.setState(Integer.valueOf(inputval));
            } else if ("status".equals(option)) {
                joblog.setStatus(Integer.valueOf(inputval));
            }
            /*else if("deptId".equals(option)){
                joblog.setDeptId(inputval);
            }*/
        }
        if(leader !=null && leader !=""){
            joblog.setLeader(leader);
            joblog.setState(2);
        }else if(csUser !=null && csUser !=""){
            joblog.setCsUser(csUser);
            joblog.setLeader(csUser);
        }else{
            joblog.setUser(loginer.getId());
        }
        joblog.setStartRow(pu.getStartRow());
        joblog.setEndRow(pu.getEndRow()-pu.getStartRow());
        int count = joblogService.selectAllTermsCont(joblog);
        List<Joblog> joblogList = joblogService.selectAllTerms(joblog);
        for(Joblog joblog1 : joblogList){
            String leaderStr = ToNameUtil.getName("user", joblog1.getLeader());
            joblog1.setLeaderStr(leaderStr);
            String userStr = ToNameUtil.getName("user", joblog1.getUser());
            joblog1.setUserStr(userStr);
            //获取抄送人并将其名字转换成中文
            String csuser = joblog1.getCsUser();
            String [ ] line=csuser.split(";");
            String csUserStr ="";
            for(String s:line) {
                String cs1 = ToNameUtil.getName("user", s);
                csUserStr += ";"+cs1;
            }
            joblog1.setCsUserStr(csUserStr.substring(1,csUserStr.length()));
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 0);
        jsonObject.put("msg", "");
        jsonObject.put("count", count);
        jsonObject.put("data", joblogList);
        jsonObject.put("success",1);
        jsonObject.put("is", true);
        jsonObject.put("tip", "操作成功");
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
        //type为app端的参数
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
            return "1";
        } catch (Exception e) {
            LogUtil.sysLog("Exception:"+e);
            return "0";
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

    @RequestMapping(value = "/gotocsuserjoblog", method = RequestMethod.GET)
    public ModelAndView gotoCsUserJoblog(HttpServletRequest request) {
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        ModelAndView mav = new ModelAndView("module/csUserJoblog");
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
