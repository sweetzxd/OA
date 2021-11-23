package com.oa.core.controller.module;

import com.oa.core.bean.Loginer;
import com.oa.core.bean.module.Department;
import com.oa.core.bean.module.Employees;
import com.oa.core.bean.module.File;
import com.oa.core.bean.module.Joblog;
import com.oa.core.bean.user.UserManager;
import com.oa.core.bean.util.PageUtil;
import com.oa.core.helper.FileHelper;
import com.oa.core.service.module.*;
import com.oa.core.service.system.RoleDefinesService;
import com.oa.core.service.user.UserManagerService;
import com.oa.core.util.ToNameUtil;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName:FileController
 * @author:wsx
 * @Date:2018/11/22
 * @Time:下午 4:22
 * @Version V1.0
 * @Explain
 */
@Controller
@RequestMapping("/weixin/file")
public class WxFileController {
    @Autowired
    private FileService fileService;
    @Autowired
    private RoleDefinesService roleDefinesService;
    @Autowired
    private FilepermissionService filepermissionService;
    @Autowired
    private JoblogService joblogService;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private UserManagerService userManagerService;
    @Autowired
    private EmployeesService employeesService;

    //获取常用文档列表
    @RequestMapping("/getUserFileList")
    @ResponseBody
    public String getUserFileList(HttpServletRequest request, String inputval, String option, int limit, int page){
        File file = new File();
        boolean optiontype = true;
        if (inputval != null && inputval != "") {
            if ("fileTypeId".equals(option)) {
                file.setFileTypeId(inputval);
            } else if ("fileName".equals(option)) {
                file.setFileName(inputval);
            }
        }
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        String userId = null;
        if(loginer == null){
            userId = request.getParameter("userid");
        }else{
            userId = loginer.getId();
        }
        List<String> userRoleList = roleDefinesService.getRoleIds(userId);
        List<String> filetypeList = filepermissionService.getUserFileType(userRoleList);
        file.setList(filetypeList);
        int count = fileService.selectAllTermsCont(file);
        PageUtil pageUtil = new PageUtil();
        pageUtil.setPageSize(limit);
        pageUtil.setCurrentPage(page);
        file.setStartRow(pageUtil.getStartRow());
        file.setEndRow(pageUtil.getEndRow() - pageUtil.getStartRow());
        List<File> fileList = fileService.selectAllTerms(file);
        for(File file1 : fileList){
            String uploadTimeStr = ToNameUtil.getName("datetime", String.valueOf(file1.getUploadTime()));
            file1.setUploadTimeStr(uploadTimeStr);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("count", count);
        jsonObject.put("data", fileList);
        jsonObject.put("msg", "");
        jsonObject.put("success",1);
        return jsonObject.toString();
    }

    /**
     * 获取工作日志信息
     * @param request
     * @return
     */
    @RequestMapping(value = "/gotoJoblogList", method = RequestMethod.GET)
    @ResponseBody
    public String gotoJoblogList(HttpServletRequest request) {
        List<Joblog> jobloglist = joblogService.selectAllJobLog();
        List<String> deptIds = new ArrayList<>();
        if(jobloglist.size()>0){
            for (Joblog joblog: jobloglist) {
                deptIds.add(joblog.getDeptId());
            }
        }
        List<Department> departmentList = departmentService.selectByIds(deptIds);
        List<UserManager> userManagerslist = userManagerService.selectAll();
        for (Department department:departmentList) {
            for(Joblog joblog:jobloglist){
                if(department.getDeptId().equals(joblog.getDeptId())){
                    joblog.setDeptStr(department.getDeptName());
                }
                for (UserManager userManager : userManagerslist) {
                    if(joblog.getLeader().equals(userManager.getUserName())){
                        joblog.setUserStr(userManager.getName());
                    }
                }
            }
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("count", jobloglist.size());
        jsonObject.put("data", jobloglist);
        jsonObject.put("msg", "");
        jsonObject.put("success",1);
        return jsonObject.toString();
    }

    /**
     * 获取人员信息
     * @param request
     * @return
     */
    @RequestMapping(value = "/selectallstaff", method = RequestMethod.GET)
    @ResponseBody
    public String getEmployeesAll(HttpServletRequest request) {
        //获取员工信息
        List<Employees> employeesList = employeesService.selectAll();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("count", employeesList.size());
        jsonObject.put("msg", "");
        jsonObject.put("data", employeesList);
        jsonObject.put("success",1);
        return jsonObject.toString();
    }

    @RequestMapping(value = "/selectalldepartment", method = RequestMethod.GET)
    @ResponseBody
    public String getDepartmentAll(HttpServletRequest request) {
        //获取员工信息
        List<Department> departmentList = departmentService.selectAll();
        List<UserManager> userManagerslist = userManagerService.selectAll();
        for (Department department:departmentList) {
            for (UserManager userManager : userManagerslist) {
                if(department.getDeptHead().equals(userManager.getUserName())){
                    if(userManager.getName()!=null && userManager.getName()!=""){
                        department.setHeadName(userManager.getName());
                    }else{
                        department.setHeadName("无");
                    }
                }
            }
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("count", departmentList.size());
        jsonObject.put("msg", "");
        jsonObject.put("data", departmentList);
        jsonObject.put("success",1);
        return jsonObject.toString();
    }
}
