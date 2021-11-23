package com.oa.core.controller.login;


import com.oa.core.bean.Loginer;
import com.oa.core.bean.module.Department;
import com.oa.core.bean.module.Employees;
import com.oa.core.bean.user.UserManager;
import com.oa.core.helper.DateHelper;
import com.oa.core.interceptor.Token;
import com.oa.core.service.module.DepartmentService;
import com.oa.core.service.module.EmployeesService;
import com.oa.core.service.system.RoleDefinesService;
import com.oa.core.service.user.UserManagerService;
import com.oa.core.helper.LoginIpHelper;
import com.oa.core.util.CommonUtil;
import com.oa.core.util.ConfParseUtil;
import com.oa.core.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Date;
import java.util.List;

@Controller
public class LoginController {

    @Autowired
    private UserManagerService userManagerService;
    @Autowired
    private RoleDefinesService roleDefinesService;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private EmployeesService employeesService;
    /**
     * 用户登入
     *
     * @param userManager
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView login(UserManager userManager, HttpServletRequest request, HttpServletResponse response) {
        String error = null;
        boolean login = false;
        ConfParseUtil cp = new ConfParseUtil();
        try {
            UserManager user = userManagerService.selectUserById(userManager.getUserName());
            if (user == null) {
                error = cp.getPoa("error_user");
            } else {
                String pw = user.getPassword();
                String computerIP = user.getComputerIP();
                String loginIp = LoginIpHelper.getIpAddr(request);
                int accountStatus = user.getAccountStatus();
                int userStatus = user.getUserStatus();
                String startDate = user.getStartDate();
                String endDate = user.getEndDate();
                Date currentDate = DateHelper.getDate();
                if (!MD5Util.checkPassword(userManager.getPassword(), pw)) {
                    error = cp.getPoa("error_pw");
                } else if (computerIP != null && !loginIp.equals(computerIP)) {
                    error = cp.getPoa("error_ip");
                } else if (accountStatus != 2) {
                    error = cp.getPoa("error_as");
                } else if (userStatus == 1) {
                    if ((endDate != null && endDate != "") && currentDate.compareTo(DateHelper.convert(endDate)) > 0) {
                        error = cp.getPoa("error_us");
                    } else {
                        login = true;
                    }
                } else {
                    login = true;
                }
            }
            if (!login) {
                ModelAndView mav = new ModelAndView("login");
                mav.addObject("error", error);
                mav.addObject("username", userManager.getUserName());
                mav.addObject("password", userManager.getPassword());
                return mav;
            } else {
                List<String> roleList = roleDefinesService.getRoleIds(user.getUserName());

                String role = "notrole;";
                for (int i = 0; i < roleList.size(); i++) {
                    role += roleList.get(i) + ";";
                }
                Loginer loginer = new Loginer(user);
                Employees employees = employeesService.selectByUserId(user.getUserName());
                if(employees!=null && !employees.equals("")){
                    loginer.setDid(employees.getDepartment());
                    String photo = employees.getPhoto();
                    if(photo==null || photo.equals("")){
                        photo = "/upload/photo/touxiang/头像"+ CommonUtil.getRandomNumber(10)+".png";
                    }
                    loginer.setPhoto(photo);
                    Department department = departmentService.selectById(employees.getDepartment());
                    if(department!=null && !department.equals("")) {
                        loginer.setDname(department.getDeptName());
                    }
                }
                request.getSession().setAttribute("loginer", loginer);
                request.getSession().setAttribute("role", role);
                String referer = request.getParameter("referer");
                if (referer == null || referer.length() == 0 || referer.endsWith("login")) {
                    return new ModelAndView("redirect:/home.do");
                } else {
                    return new ModelAndView("redirect:" + referer);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Cookie[] cookies = request.getCookies();
            if (null != cookies) {
                for (Cookie cookie : cookies) {
                    String name = cookie.getName();
                    String value = cookie.getValue();
                    if (name.equals("subUser") && value.equals("true")) {
                        cookie.setValue("false");
                        response.addCookie(cookie);
                    }
                }
            }
            ModelAndView mav = new ModelAndView("login");
            mav.addObject("error", cp.getPoa("error_catch"));
            mav.addObject("username", userManager.getUserName());
            mav.addObject("password", "");
            return mav;
        }

    }

    /**
     * 用户登出
     *
     * @return
     */
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (null == cookies) {
        } else {
            for (Cookie cookie : cookies) {
                String name = cookie.getName();
                String value = cookie.getValue();
                if (name.equals("subUser") && value.equals("true")) {
                    cookie.setValue("false");
                    response.addCookie(cookie);
                }
            }
        }
        request.getSession().invalidate();
        return "redirect:/login.do";
    }
}
