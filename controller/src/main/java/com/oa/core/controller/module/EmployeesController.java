package com.oa.core.controller.module;

import com.easemob.server.example.comm.TokenUtil;
import com.easemob.server.example.util.UserUtil;
import com.oa.core.bean.Loginer;
import com.oa.core.bean.module.Employees;
import com.oa.core.bean.system.MyUrlRegist;
import com.oa.core.bean.user.UserComputer;
import com.oa.core.bean.user.UserManager;
import com.oa.core.bean.util.PageUtil;
import com.oa.core.helper.DateHelper;
import com.oa.core.helper.StringHelper;
import com.oa.core.interceptor.Logined;
import com.oa.core.listener.InitDataListener;
import com.oa.core.service.module.EmployeesService;
import com.oa.core.service.system.MyUrlRegistService;
import com.oa.core.service.user.UserComputerService;
import com.oa.core.service.user.UserManagerService;
import com.oa.core.service.util.TableService;
import com.oa.core.util.MD5Util;
import com.oa.core.util.PinyinUtil;
import com.oa.core.util.PrimaryKeyUitl;
import io.swagger.client.model.RegisterUsers;
import io.swagger.client.model.User;
import org.json.JSONArray;
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
 * @ClassName:EmployeesController
 * @author:zxd
 * @Date:2018/10/13
 * @Time:下午 4:12
 * @Version V1.0
 * @Explain 员工信息表
 */
@Controller
@RequestMapping("/employees")
public class EmployeesController {
    @Autowired
    EmployeesService employeesService;
    @Autowired
    UserManagerService userManagerService;
    @Autowired
    UserComputerService userComputerService;
    @Autowired
    MyUrlRegistService myUrlRegistService;
    @Autowired
    TableService tableService;

    @RequestMapping(value = "/gotoemployees", method = RequestMethod.GET)
    public ModelAndView gotoEmployees(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("module/employees");
        mav.addObject("type", "list");
        return mav;
    }

    @RequestMapping("/select")
    @ResponseBody
    public String getEmployeesList(HttpServletRequest request, String inputval, String option, int limit, int page) {
        Employees employees = new Employees();
        boolean optiontype = true;
        if (inputval != null && inputval != "") {
            if ("staffId".equals(option)) {
                employees.setStaffId(inputval);
            } else if ("staffName".equals(option)) {
                employees.setStaffName(inputval);
            } else if ("department".equals(option)) {
                employees.setDepartment(inputval);
                optiontype = false;
            }
        }
        int count = employeesService.selectAllTermsCont(employees);
        PageUtil pageUtil = new PageUtil();
        pageUtil.setPageSize(limit);
        pageUtil.setCurrentPage(page);
        employees.setStartRow(pageUtil.getStartRow());
        employees.setEndRow(pageUtil.getEndRow() - pageUtil.getStartRow());
        List<Employees> employeesList = employeesService.selectAllTerms(employees);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 0);
        jsonObject.put("msg", "");
        jsonObject.put("count", count);
        jsonObject.put("data", employeesList);
        jsonObject.put("success",1);
        return jsonObject.toString();
    }

    @RequestMapping(value = "/selectall", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String getEmployeesAll(HttpServletRequest request, String inputval, String option) {
        List employeesList = InitDataListener.getListData("emp");
        if(employeesList==null || employeesList.size()<1){
            Employees employees = new Employees();
            if (inputval != null && inputval != "") {
                if ("staffId".equals(option)) {
                    employees.setStaffId(inputval);
                } else if ("staffName".equals(option)) {
                    employees.setStaffName(inputval);
                } else if ("department".equals(option)) {
                    employees.setDepartment(inputval);
                }
            }
            PageUtil pageUtil = new PageUtil();
            pageUtil.setPageSize(1000);
            pageUtil.setCurrentPage(1);
            employees.setStartRow(pageUtil.getStartRow());
            employees.setEndRow(pageUtil.getEndRow() - pageUtil.getStartRow());
            employeesList = employeesService.selectAllTerms(employees);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 0);
        jsonObject.put("msg", "");
        jsonObject.put("data", employeesList);
        jsonObject.put("success",1);
        return jsonObject.toString();
    }

    @RequestMapping(value = "/addemployees", method = RequestMethod.GET)
    public ModelAndView addEmployees(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("module/employees");
        mav.addObject("type", "add");
        mav.addObject("conmenu", "add");
        return mav;
    }

    @RequestMapping(value = "/modiemployees", method = RequestMethod.GET)
    public ModelAndView modiEmployees(HttpServletRequest request, String staffId,String type) {
        Employees employees = employeesService.selectById(staffId);
        String conmenu = myUrlRegistService.selectContextMenu("Z2018101500002");
        //跳转修改页面
        if(type==null&&conmenu!=null&&!conmenu.equals("")&&!conmenu.equals("[]")){
            ModelAndView mav = new ModelAndView("manage/relevancemenu");
            MyUrlRegist myUrlRegist1 = new MyUrlRegist();
            myUrlRegist1.setParentId("Z2018101500002");
            myUrlRegist1.setFormType(5);
            List<MyUrlRegist> list = myUrlRegistService.selectTerms(myUrlRegist1);
            list.get(0).setContextmenu(list.get(0).getContextmenu()+staffId);
            for(int i=1;i<list.size();i++){
                if(list.get(i).getContextmenu().endsWith("=")){
                    String[] fields = list.get(i).getKeepFeild().split("_");
                    String sql = "select "+fields[1]+" from "+fields[0]+" where staffId='"+staffId+"' and curStatus=2";
                    String value = tableService.selectSql(sql).get(0);
                    list.get(i).setContextmenu(list.get(i).getContextmenu()+value);
                }
            }
            mav.addObject("list",list);
            mav.addObject("src",list.get(0).getContextmenu());
            return mav;
        }
       /* String contextmenu = "";
        if(conmenu!=null && conmenu!="") {
            JSONArray jsonArray = new JSONArray(conmenu);
            contextmenu = "<a href='/employees/modiemployees.do?staffId=" + staffId + "'>员工信息</a>";
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String pageId = jsonObject.getString("id");
                String[] asskey = jsonObject.getString("asskey").split("_");
                String[] mainasskey = jsonObject.getString("mainasskey").split("_");
                MyUrlRegist myUrlRegist = myUrlRegistService.selectById(pageId);
                String formid = "";
                String pagetitle = "";
                if (myUrlRegist != null) {
                    formid = myUrlRegist.getFormId();
                    pagetitle = myUrlRegist.getPageTitle();
                }
                contextmenu += "<a href='javascript:void(0);' onclick=\"addnav('/userpage/viewpage/" + pageId + ".do?formid=" + formid + "&field=" + asskey[1] + "&value=" + employees.getUserName() + "','" + pageId + "','" + pagetitle + "')\">" + pagetitle + "</a>";
            }
        }*/
        ModelAndView mav = new ModelAndView("module/employees");
        mav.addObject("type", "modi");
        //mav.addObject("contextmenu", contextmenu);
        mav.addObject("emp", employees);
        return mav;
    }

    @RequestMapping(value = "/modimyemployees", method = RequestMethod.GET)
    public ModelAndView modiMyEmployees(HttpServletRequest request) {
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        String userId = loginer.getId();
        Employees employees = employeesService.selectByUserId(userId);
        ModelAndView mav = new ModelAndView("module/myinformation");
        mav.addObject("type", "emp");
        mav.addObject("emp", employees);
        return mav;
    }

    @RequestMapping(value = "/modimyphoto", method = RequestMethod.GET)
    public ModelAndView modiMyPhoto(HttpServletRequest request) {
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        String userId = loginer.getId();
        Employees employees = employeesService.selectByUserId(userId);
        ModelAndView mav = new ModelAndView("module/myinformation");
        mav.addObject("type", "photo");
        mav.addObject("emp", employees);
        return mav;
    }

    @Logined
    @RequestMapping(value = "/insert", method = RequestMethod.POST, produces = {"text/html;charset=UTF-8;", "application/json;"})
    @ResponseBody
    public String insertSave(HttpServletRequest request, Employees employees) {
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        UserManager user = new UserManager();
        UserComputer computer = new UserComputer();
        try {
            PrimaryKeyUitl pk = new PrimaryKeyUitl();
            String staffId = pk.getNextId("employees", "staffId");
            String userName = PinyinUtil.getPingYin(employees.getStaffName());
            UserManager userManager = userManagerService.selectUserById(userName);
            if(userManager!=null){
                userName = userName + StringHelper.get2Num();
            }
            employees.setStaffId(staffId);
            employees.setUserName(userName);
            if (loginer != null) {
                String userId = loginer.getId();
                employees.setRecordName(userId);
                employees.setModifyName(userId);
                user.setRecordName(userId);
                user.setModifyName(userId);
                computer.setRecordName(userId);
                computer.setModifyName(userId);
            }
            employees.setRecordTime(DateHelper.now());
            employees.setModifyTime(DateHelper.now());
            employeesService.insert(employees);

            user.setUserName(userName);
            user.setName(employees.getStaffName());
            user.setPassword(MD5Util.getMD5(userName));
            user.setAccountStatus(2);
            user.setUserStatus(2);
            user.setRecordTime(DateHelper.now());
            user.setModifyTime(DateHelper.now());
            userManagerService.insertUser(user);

            String computerId = pk.getNextId("UserComputer", "computerId");
            computer.setComputerId(computerId);
            computer.setUserName(userName);
            computer.setRecordTime(DateHelper.now());
            computer.setModifyTime(DateHelper.now());
            userComputerService.insert(computer);
            return "1";
        } catch (Exception e) {
            return "0";
        }
    }

    @Logined
    @RequestMapping(value = "/update", method = RequestMethod.POST, produces = {"text/html;charset=UTF-8;", "application/json;"})
    @ResponseBody
    public String updateSave(HttpServletRequest request, Employees employees) {
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        try {
            if (loginer != null) {
                String userId = loginer.getId();
                employees.setModifyName(userId);
            }
            employees.setModifyTime(DateHelper.now());
            employeesService.update(employees);
            loginer.setName(employees.getStaffName());
            request.getSession().setAttribute("loginer", loginer);
            return "1";
        } catch (Exception e) {
            return "0";
        }
    }

    @Logined
    @RequestMapping(value = "/delete", method = RequestMethod.POST, produces = {"text/html;charset=UTF-8;", "application/json;"})
    @ResponseBody
    public String deleteSave(HttpServletRequest request, String staffId) {
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        try {
            String userId = loginer.getId();
            if (staffId != null && staffId.indexOf(";") >= 0) {
                String[] staffIds = staffId.split(";");
                for (String id : staffIds) {
                    Employees employees = employeesService.selectById(id);
                    employeesService.delete(id, userId, DateHelper.now());
                    userManagerService.delete(employees.getUserName(), userId, DateHelper.now());
                    userComputerService.delete(employees.getUserName(), userId, DateHelper.now());
                }
            } else {
                Employees employees = employeesService.selectById(staffId);
                employeesService.delete(staffId, userId, DateHelper.now());
                userManagerService.delete(employees.getUserName(), userId, DateHelper.now());
                userComputerService.delete(employees.getUserName(), userId, DateHelper.now());
            }

            return "1";
        } catch (Exception e) {
            return "0";
        }
    }

    /**
    * @method: addressList
    * @param:
    * @return:
    * @author: zxd
    * @date: 2019/05/29
    * @description: 方法说明
    */
    @RequestMapping(value = "/addresslist", method = RequestMethod.GET)
    public ModelAndView addressList(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("module/addresslist");
        return mav;
    }

    /**
    * @method:
    * @param:
    * @return:
    * @author: zxd
    * @date: 2019/05/29
    * @description: 方法说明
    */
        @RequestMapping(value = "/insertHX", method = RequestMethod.GET)
    public String insertHX(HttpServletRequest request){
        List<Employees> employeesList = employeesService.selectAll();

        for(Employees employees : employeesList){
            String userName = employees.getUserName();
            String staffName = employees.getStaffName();
            TokenUtil.initTokenByProp();
            RegisterUsers users = new RegisterUsers();
            User user = new User().username(userName).password("zjy123");
            users.add(user);
            UserUtil.createUser(users);
        }

        return "";
    }
}
