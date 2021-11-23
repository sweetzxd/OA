package com.oa.core.controller.login;


import com.oa.core.bean.Loginer;
import com.oa.core.bean.module.Employees;
import com.oa.core.bean.user.UserManager;
import com.oa.core.controller.util.LoginUtil;
import com.oa.core.helper.DateHelper;
import com.oa.core.helper.LoginIpHelper;
import com.oa.core.service.module.EmployeesService;
import com.oa.core.service.system.RoleDefinesService;
import com.oa.core.service.user.UserManagerService;
import com.oa.core.util.ConfParseUtil;
import com.oa.core.util.MD5Util;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Date;
import java.util.List;

@Controller
@RequestMapping("/weixin")
public class WxLoginController {

    @Autowired
    private UserManagerService userManagerService;
    @Autowired
    private EmployeesService employeesService;
    @Autowired
    private RoleDefinesService roleDefinesService;

    /**
     * 用户登入
     *
     * @param userManager
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String login(UserManager userManager, HttpServletRequest request, HttpServletResponse response,String userName,String password) {
        JSONObject jsonObject = new JSONObject();
        String error = null;
        boolean login = false;
        int ret = 0;
        if(userName==null && password==null){
            if(userManager!=null){
                userName = userManager.getUserName();
                password = userManager.getPassword();
            }
        }
        UserManager user = userManagerService.selectUserById(userName);
        ConfParseUtil cp = new ConfParseUtil();
        if(user==null){
            error = cp.getPoa("error_user");
        }else{
            String pw = user.getPassword();
            String computerIP = user.getComputerIP();
            String loginIp = LoginIpHelper.getIpAddr(request);
            int accountStatus = user.getAccountStatus();
            int userStatus = user.getUserStatus();
            String startDate = user.getStartDate();
            String endDate = user.getEndDate();
            Date currentDate = DateHelper.getDate();
            if (!MD5Util.checkPassword(password, pw)) {
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
                    ret = 1;
                }
            } else {
                login = true;
                ret = 1;
            }
        }
        if(!login){
            jsonObject.put("msg",error);
        }else{
            List<String> roleList = roleDefinesService.getRoleIds(user.getUserName());
            String role = "notrole;";
            for (int i = 0; i < roleList.size(); i++) {
                role += roleList.get(i) + ";";
            }
            Loginer loginer = new Loginer(user);
            request.getSession().setAttribute("loginer", loginer);
            request.getSession().setAttribute("role", role);
            LoginUtil.setLoginer(userName,loginer);
            LoginUtil.setRole(userName,role);
            JSONObject data = new JSONObject();
            Employees employees = employeesService.selectByUserId(user.getUserName());
            data.put("userid",user.getUserName());
            data.put("username",user.getName());
            data.put("userimg",employees.getPhoto());
            jsonObject.put("data",data);
        }
        jsonObject.put("success",ret);
        return jsonObject.toString();
    }

    /**
    * @method: logout
    * @param:
    * @return:
    * @author: zxd
    * @date: 2019/03/20
    * @description: 注销用户
    */
    @RequestMapping("/logout")
    @ResponseBody
    public String logout(HttpServletRequest request, HttpServletResponse response){
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
        request.getSession().setAttribute("loginer", null);
        request.getSession().setAttribute("role", null);
        request.getSession().invalidate();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("success",1);
        jsonObject.put("msg","退出登录");
        return jsonObject.toString();
    }

    /**
    * @method: Loginerw
    * @param:
    * @return:
    * @author: zxd
    * @date: 2019/03/20
    * @description: 验证登陆
    */
    @RequestMapping(value = "/loginer", method = RequestMethod.GET)
    @ResponseBody
    public String Loginerw(HttpServletRequest request, HttpServletResponse response){
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        String userId = null;
        if(loginer == null){
            userId = request.getParameter("userid");
        }else{
            userId = loginer.getId();
        }
        if(userId==null){
            return "0";
        }else{
            return "1";
        }
    }
}
