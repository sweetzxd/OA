package com.oa.core.controller.module;

import com.oa.core.bean.Loginer;
import com.oa.core.bean.module.Department;
import com.oa.core.bean.module.Employees;
import com.oa.core.bean.util.PageUtil;
import com.oa.core.helper.DateHelper;
import com.oa.core.interceptor.Logined;
import com.oa.core.service.module.DepartmentService;
import com.oa.core.service.module.EmployeesService;
import com.oa.core.service.util.TableService;
import com.oa.core.util.DeptUtil;
import com.oa.core.util.LogUtil;
import com.oa.core.util.PrimaryKeyUitl;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @ClassName:DepartmentController
 * @author:zxd
 * @Date:2018/10/13
 * @Time:下午 4:14
 * @Version V1.0
 * @Explain
 */
@Controller
@RequestMapping("/department")
public class DepartmentController {
    @Autowired
    DepartmentService departmentService;
    @Autowired
    EmployeesService employeesService;
    @Autowired
    TableService tableService;

    @RequestMapping(value = "/gotodepartment", method = RequestMethod.GET)
    public ModelAndView gotoDepartment(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("module/department");
        mav.addObject("type", "list");
        return mav;
    }

    @RequestMapping(value = "/depttree", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String gotoAccessTree(HttpServletRequest request,String spread) {
        List<Department> myurllist = departmentService.selectAll();
        if(spread!=null && spread.equals("false")) {
            return DeptUtil.getMenu(myurllist,false);
        }else{
            return DeptUtil.getMenu(myurllist,true);
        }
    }

    @RequestMapping(value = "/deptemptree", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String gotoDeptEmpTree(HttpServletRequest request,String spread,String empnum,String empname) {
        List<Department> myurllist = departmentService.selectAll();
        boolean sp = true;
        boolean emnum = false;
        boolean emname = false;
        if(empnum!=null && empnum.equals("true")){
            emnum = true;
        }
        if(empname!=null && empname.equals("true")){
            emname = true;
        }
        if(spread!=null && spread.equals("false")) {
            sp = false;
        }
        return DeptUtil.getMenu(myurllist,sp,emname,emnum);
    }

    @RequestMapping(value = "/deptemponetree", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String gotoDeptEmpOneTree(HttpServletRequest request,String spread) {
        List<Department> myurllist = departmentService.selectAll();
        boolean sp = true;
        if(spread!=null && spread.equals("false")) {
            sp = false;
        }
        return DeptUtil.getMenu2(myurllist,sp,true,false);
    }

    @RequestMapping("/selectlist")
    @ResponseBody
    public String getDepartmentList(HttpServletRequest request, String inputval, String option, int limit, int page) {
        Department department = new Department();
        boolean optiontype = true;
        if (inputval != null && inputval != "") {
            if ("deptId".equals(option)) {
                department.setDeptId(inputval);
            } else if ("deptName".equals(option)) {
                department.setDeptName(inputval);
            } else if ("superiorDeptId".equals(option)) {
                department.setSuperiorDeptId(inputval);
            }
        }
        int count = departmentService.selectAllTermsCont(department);
        PageUtil pageUtil = new PageUtil();
        pageUtil.setPageSize(limit);
        pageUtil.setCurrentPage(page);
        department.setStartRow(pageUtil.getStartRow());
        department.setEndRow(pageUtil.getEndRow() - pageUtil.getStartRow());
        List<Department> departmentList = departmentService.selectAllTerms(department);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 0);
        jsonObject.put("msg", "");
        jsonObject.put("count", count);
        jsonObject.put("data", departmentList);
        jsonObject.put("success",1);
        jsonObject.put("is", true);
        jsonObject.put("tip", "操作成功");

        return jsonObject.toString();
    }

    @RequestMapping(value = "/deptadd", method = RequestMethod.GET)
    public ModelAndView gotoDeptAdd(HttpServletRequest request, String pid) {
        Department parent = departmentService.selectById(pid);
        if (parent == null) {
            parent = new Department();
            parent.setDeptId(pid);
            parent.setDeptName("综合办公管理系统");
        }
        ModelAndView mav = new ModelAndView("module/department");
        mav.addObject("dept", new Department());
        mav.addObject("parent", parent);
        mav.addObject("type", "add");
        return mav;
    }

    @RequestMapping(value = "/deptmodi/{deptId}", method = RequestMethod.GET)
    public ModelAndView gotoDeptModi(HttpServletRequest request, @PathVariable String deptId, String pid) {
        ModelAndView mav = new ModelAndView("module/department");
        Department parent = departmentService.selectById(pid);
        if (parent == null) {
            parent = new Department();
            parent.setDeptId(pid);
            parent.setDeptName("organize");
        }
        mav.addObject("dept", departmentService.selectById(deptId));
        mav.addObject("parent", parent);
        mav.addObject("type", "modi");
        return mav;
    }

    @RequestMapping(value = "/deptedit", method = RequestMethod.GET)
    public ModelAndView gotoDeptEdit(HttpServletRequest request, String deptId) {
        ModelAndView mav = new ModelAndView("module/department");
        mav.addObject("deptId", deptId);
        mav.addObject("dept", departmentService.selectById(deptId));
        mav.addObject("userlist", employeesService.selectAllDept(deptId));
        mav.addObject("notuserlist", employeesService.selectAllNotDept(deptId));
        mav.addObject("type", "edit");
        return mav;
    }

    @Logined
    @RequestMapping(value = "/insert", method = RequestMethod.POST, produces = {"text/html;charset=UTF-8;", "application/json;"})
    @ResponseBody
    public String insertSave(HttpServletRequest request, Department department) {
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        String userId = loginer.getId();
        try {
            PrimaryKeyUitl pk = new PrimaryKeyUitl();
            String deptId = pk.getNextId("department", "deptId");
            department.setDeptId(deptId);
            department.setRecordName(userId);
            department.setModifyName(userId);
            department.setRecordTime(DateHelper.now());
            department.setModifyTime(DateHelper.now());
            departmentService.insert(department);
            String deptstaff = request.getParameter("deptstaff");
            List<String> staffIds = new ArrayList<String>();
            if(deptstaff!=null && deptstaff!="") {
                Collections.addAll(staffIds, deptstaff.split(";"));
            }
            departmentService.updateEmp(deptId,staffIds,userId,DateHelper.now());
            tableService.updateSqlMap("update afxtx19062101 set bm19062100001='"+deptId+"' where curStatus=2 xm19062100001='"+userId+"'");
            return "1";
        } catch (Exception e) {
            return "0";
        }
    }

    @Logined
    @RequestMapping(value = "/update", method = RequestMethod.POST, produces = {"text/html;charset=UTF-8;", "application/json;"})
    @ResponseBody
    public String updateSave(HttpServletRequest request, Department department) {
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        String userId = loginer.getId();
        try {
            String deptId = department.getDeptId();
            department.setModifyName(userId);
            department.setModifyTime(DateHelper.now());
            departmentService.update(department);
            String deptstaff = request.getParameter("deptstaff");
            List<String> staffIds = new ArrayList<String>();
            departmentService.deleteEmp(deptId,userId,DateHelper.now());
            if(deptstaff!=null && deptstaff!="") {
                Collections.addAll(staffIds, deptstaff.split(";"));
                departmentService.updateEmp(deptId,staffIds,userId,DateHelper.now());
                tableService.updateSqlMap("update afxtx19062101 set bm19062100001='"+deptId+"' where curStatus=2 xm19062100001='"+userId+"'");
            }
            return "1";
        } catch (Exception e) {
            LogUtil.sysLog("Exception:"+e);
            return "0";
        }
    }

    @Logined
    @RequestMapping(value = "/empedit", method = RequestMethod.POST, produces = {"text/html;charset=UTF-8;", "application/json;"})
    @ResponseBody
    public String editSave(HttpServletRequest request, String staffId,String deptId) {
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        String userId = loginer.getId();
        try {
            Employees emp = new Employees();
            if(staffId!=null && staffId.indexOf(";")>=0) {
                emp.setDepartment(deptId);
                emp.setModifyName(userId);
                emp.setModifyTime(DateHelper.now());
                String[] staffIds = staffId.split(";");
                tableService.selectSqlMapList("UPDATE employees SET department = NULL WHERE curStatus = 2 AND department = '"+deptId+"'");
                for(String id: staffIds){
                    emp.setStaffId(id);
                    employeesService.update(emp);
                }
            }else{
                emp.setDepartment(deptId);
                emp.setModifyName(userId);
                emp.setModifyTime(DateHelper.now());
                emp.setStaffId(staffId);
                employeesService.update(emp);
            }
            return "1";
        } catch (Exception e) {
            LogUtil.sysLog("Exception:"+e);
            return "0";
        }
    }

    @Logined
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public ModelAndView deleteSave(HttpServletRequest request, String deptId) {
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        try {
            String userId = loginer.getId();
            departmentService.delete(deptId,userId,DateHelper.now());
            departmentService.deleteEmp(deptId,userId,DateHelper.now());
        } catch (Exception e) {
            LogUtil.sysLog("Exception:"+e);
        }
        return new ModelAndView("redirect:/department/gotodepartment.do");
    }


    @RequestMapping("/selectdeptinput")
    @ResponseBody
    public String getDeptList(HttpServletRequest request, String inputval, int limit, int page) {
        int count = departmentService.selectDeptInputByCont(inputval);
        PageUtil pu = new PageUtil();
        pu.setPageSize(limit);
        pu.setCurrentPage(page);
        int StartRow = pu.getStartRow();
        int EndRow = pu.getEndRow()-pu.getStartRow();
        List<Department> departmentList = departmentService.selectDeptInput(inputval,StartRow,EndRow);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 0);
        jsonObject.put("msg", "");
        jsonObject.put("count", count);
        jsonObject.put("data", departmentList);
        jsonObject.put("success",1);
        jsonObject.put("is", true);
        jsonObject.put("tip", "操作成功");
        return jsonObject.toString();
    }
}
