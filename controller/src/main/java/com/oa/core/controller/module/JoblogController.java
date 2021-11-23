package com.oa.core.controller.module;

import com.oa.core.bean.Loginer;
import com.oa.core.bean.module.Department;
import com.oa.core.bean.module.Joblog;
import com.oa.core.bean.system.TaskSender;
import com.oa.core.bean.util.PageUtil;
import com.oa.core.helper.DateHelper;
import com.oa.core.helper.FileHelper;
import com.oa.core.service.module.DepartmentService;
import com.oa.core.service.module.JoblogService;
import com.oa.core.service.system.TaskSenderService;
import com.oa.core.util.LogUtil;
import com.oa.core.util.PrimaryKeyUitl;
import com.oa.core.util.ToNameUtil;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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

    @RequestMapping(value = "/gotoJoblogList", method = RequestMethod.GET)
    public ModelAndView gotoJoblogList(HttpServletRequest request, String type, String joblogId) {
        ModelAndView mav = new ModelAndView("module/joblog");
        if(type.equals("edit")){
            Joblog joblog = joblogService.selectById(joblogId);
            Department department = departmentService.selectById(joblog.getDeptId());
            if(department!=null){
                joblog.setDeptStr(department.getDeptName());
            }
            String leaderStr = ToNameUtil.getName("user", joblog.getLeader());
            joblog.setLeaderStr(leaderStr);
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
    public String getJoblogList(HttpServletRequest request, String inputval, String option, int page, int limit,String leader) {
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
            }else if("deptId".equals(option)){
                joblog.setDeptId(inputval);
            }
        }
        if(leader !=null && leader !=""){
            joblog.setLeader(leader);
            joblog.setState(2);
        }else{
            joblog.setUser(loginer.getId());
        }
        joblog.setStartRow(pu.getStartRow());
        joblog.setEndRow(pu.getEndRow()-pu.getStartRow());
        int count = joblogService.selectAllTermsCont(joblog);
        List<Joblog> joblogList = joblogService.selectAllTerms(joblog);
        for(Joblog joblog1 : joblogList){
            Department department = departmentService.selectById(joblog1.getDeptId());
            if(department!=null){
                joblog1.setDeptStr(department.getDeptName());
            }
            String leaderStr = ToNameUtil.getName("user", joblog1.getLeader());
            joblog1.setLeaderStr(leaderStr);
            String userStr = ToNameUtil.getName("user", joblog1.getUser());
            joblog1.setUserStr(userStr);
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
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        String userId = loginer.getId();
        try {
            joblog.setModifyName(userId);
            joblog.setModifyTime(DateHelper.now());
            joblogService.update(joblog);
            TaskSender taskSender = new TaskSender();
            taskSender.setWkflwID("joblog");
            taskSender.setAccepter(joblog.getLeader());
            TaskSender joblogTaskSender = taskSenderService.selectJoblogTask(taskSender);
            if(joblog.getState()==1){
                Joblog joblog1 = new Joblog();
                joblog1.setLeader(joblog.getLeader());
                joblog1.setState(2);
                int count = joblogService.selectAllTermsCont(joblog1);
                if(count==0){
                    if(joblogTaskSender !=null){
                        if(joblogTaskSender.getMsgStatus()==2){
                            joblogTaskSender.setMsgStatus(4);
                            joblogTaskSender.setModifyName(userId);
                            joblogTaskSender.setModifyTime(DateHelper.now());
                            taskSenderService.update(joblogTaskSender);
                        }
                    }
                }
            }else if(joblog.getState()==2){
                if(joblogTaskSender !=null){
                    if(joblogTaskSender.getMsgStatus()==4){
                        joblogTaskSender.setMsgStatus(2);
                        joblogTaskSender.setModifyName(userId);
                        joblogTaskSender.setModifyTime(DateHelper.now());
                        taskSenderService.update(joblogTaskSender);
                    }
                }else{
                    PrimaryKeyUitl pk = new PrimaryKeyUitl();
                    String permissionId = pk.getNextId("tasksender", "workOrderNO");
                    taskSender.setWorkOrderNO(permissionId);
                    taskSender.setTaskTitle("日志审批");
                    taskSender.setRefLinkUrl("/joblog/gotoCheckJoblog.do");
                    taskSender.setMsgStatus(2);
                    taskSender.setRecordName(userId);
                    taskSender.setRecordTime(DateHelper.now());
                    taskSender.setModifyName(userId);
                    taskSender.setModifyTime(DateHelper.now());
                    taskSenderService.insert(taskSender);
                }
            }else if(joblog.getState()==3||joblog.getState()==4){
                Joblog joblog1 = new Joblog();
                joblog1.setLeader(userId);
                joblog1.setState(2);
                int count = joblogService.selectAllTermsCont(joblog1);
                if(count==0){
                    if(joblogTaskSender !=null){
                        if(joblogTaskSender.getMsgStatus()==2){
                            joblogTaskSender.setMsgStatus(4);
                            joblogTaskSender.setModifyName(userId);
                            joblogTaskSender.setModifyTime(DateHelper.now());
                            taskSenderService.update(joblogTaskSender);
                        }
                    }
                }
            }
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

}
