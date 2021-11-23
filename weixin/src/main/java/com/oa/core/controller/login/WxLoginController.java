package com.oa.core.controller.login;


import com.alibaba.fastjson.JSON;
import com.oa.core.bean.Loginer;
import com.oa.core.bean.module.Employees;
import com.oa.core.bean.user.UserComputer;
import com.oa.core.bean.user.UserManager;
import com.oa.core.controller.util.LoginUtil;
import com.oa.core.helper.DateHelper;
import com.oa.core.helper.LoginIpHelper;
import com.oa.core.service.module.EmployeesService;
import com.oa.core.service.system.RoleDefinesService;
import com.oa.core.service.user.UserComputerService;
import com.oa.core.service.user.UserManagerService;
import com.oa.core.service.util.TableService;
import com.oa.core.util.AccessUtil;
import com.oa.core.util.ConfParseUtil;
import com.oa.core.util.MD5Util;
import com.oa.core.util.PrimaryKeyUitl;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/weixin")
public class WxLoginController {

    @Autowired
    private UserManagerService userManagerService;
    @Autowired
    private UserComputerService userComputerService;
    @Autowired
    private EmployeesService employeesService;
    @Autowired
    private RoleDefinesService roleDefinesService;
    @Autowired
    private TableService tableService;

    /**
     * 用户登入
     *
     * @param userManager
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String login(UserManager userManager, HttpServletRequest request, HttpServletResponse response, String userName, String password) {
        JSONObject jsonObject = new JSONObject();
        String error = null;
        boolean login = false;
        int ret = 0;
        if (userName == null && password == null) {
            if (userManager != null) {
                userName = userManager.getUserName();
                password = userManager.getPassword();
            }
        }
        UserManager user = userManagerService.selectUserById(userName);
        ConfParseUtil cp = new ConfParseUtil();
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
        if (!login) {
            jsonObject.put("msg", error);
        } else {
            List<String> roleList = roleDefinesService.getRoleIds(user.getUserName());
            String role = "notrole;";
            for (int i = 0; i < roleList.size(); i++) {
                role += roleList.get(i) + ";";
            }
            Loginer loginer = new Loginer(user);
            request.getSession().setAttribute("loginer", loginer);
            request.getSession().setAttribute("role", role);
            LoginUtil.setLoginer(userName, loginer);
            LoginUtil.setRole(userName, role);
            JSONObject data = new JSONObject();
            Employees employees = employeesService.selectByUserId(user.getUserName());
            data.put("userid", user.getUserName());
            data.put("username", user.getName());
            data.put("userimg", (employees.getPhoto() == null || employees.getPhoto() == "") ? "" : employees.getPhoto());
            jsonObject.put("data", data);
        }
        jsonObject.put("success", ret);
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
        request.getSession().setAttribute("loginer", null);
        request.getSession().setAttribute("role", null);
        request.getSession().invalidate();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("success", 1);
        jsonObject.put("msg", "退出登录");
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
    public String Loginerw(HttpServletRequest request, HttpServletResponse response) {
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        String userId = null;
        if (loginer == null) {
            userId = request.getParameter("userid");
        } else {
            userId = loginer.getId();
        }
        if (userId == null) {
            return "0";
        } else {
            return "1";
        }
    }

    /**
     * @method:
     * @param:
     * @return:
     * @author: zxd
     * @date: 2019/08/31
     * @description: 获取服务器列表
     */
    @RequestMapping(value = "/getserveraddress", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String getServerAddress(HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();
        try {
            List<Map<String, Object>> maps = tableService.selectSqlMapList("select fwqmc19083101 as name,fwqdz19083101 as url from fwqlj19083101 where curStatus=2 and sfqy190831001='是'");
            jsonObject.put("data", maps);
            jsonObject.put("success", 1);
            jsonObject.put("msg", "服务器地址");
        } catch (Exception e) {
            e.getLocalizedMessage();
            jsonObject.put("success", 0);
            jsonObject.put("msg", "未获取到服务器地址");
        }
        return jsonObject.toString();
    }

    /**
     * @method: getRegisterUrl
     * @param:
     * @return:
     * @author: zxd
     * @date: 2019/09/06
     * @description: 获取注册url
     */
    @RequestMapping(value = "/registerurl", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String getRegisterUrl(HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();
        try {
            Map<String, String> map = new HashMap<>();
            jsonObject.put("success", 1);
            jsonObject.put("msg", "服务器地址");
        } catch (Exception e) {
            e.getLocalizedMessage();
            jsonObject.put("success", 0);
            jsonObject.put("msg", "未获取到服务器地址");
        }
        return jsonObject.toString();
    }

    @RequestMapping(value = "/getusername", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String getUserName(HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();
        String userName = request.getParameter("userName");
        try {
            UserManager um = userManagerService.selectUserById(userName);
            if (um != null) {
                jsonObject.put("success", 2);
                jsonObject.put("msg", "账号已存在");
            } else {
                jsonObject.put("success", 1);
                jsonObject.put("msg", "验证通过");
            }
        } catch (Exception e) {
            e.getLocalizedMessage();
            jsonObject.put("success", 0);
            jsonObject.put("msg", "数据异常");
        }
        return jsonObject.toString();
    }

    @RequestMapping(value = "/postregister", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public String postRegister(HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();
        try {
            String userName = request.getParameter("userName");
            String password = request.getParameter("password");
            String phone = request.getParameter("phone");
            String staffName = new String(request.getParameter("staffName").getBytes("ISO-8859-1"), "UTF-8");
            Employees employees = new Employees();
            PrimaryKeyUitl pk = new PrimaryKeyUitl();
            String staffId = pk.getNextId("employees", "staffId");
            employees.setStaffId(staffId);
            employees.setStaffName(staffName);
            employees.setPhone(phone);
            employees.setUserName(userName);
            employees.setAge(0);
            employees.setRecordName(userName);
            employees.setRecordTime(DateHelper.now());
            employees.setModifyName(userName);
            employees.setModifyTime(DateHelper.now());

            UserManager userManager = new UserManager();
            userManager.setUserName(userName);
            userManager.setPassword(MD5Util.getMD5(password));
            userManager.setName(staffName);
            userManager.setStaffId(staffId);
            userManager.setAccountStatus(2);
            userManager.setUserStatus(2);
            userManager.setStartDate(DateHelper.getYMD());
            userManager.setRecordName(userName);
            userManager.setRecordTime(DateHelper.now());
            userManager.setModifyName(userName);
            userManager.setModifyTime(DateHelper.now());

            AccessUtil au = new AccessUtil();
            String menu = au.resetMenu(userName);
            UserComputer userComputer = new UserComputer();
            String computerId = pk.getNextId("UserComputer", "computerId");
            userComputer.setComputerId(computerId);
            userComputer.setUserName(userName);
            userComputer.setUserMenu(menu);
            userComputer.setCurStatus(2);
            userComputer.setRecordName(userName);
            userComputer.setRecordTime(DateHelper.now());
            userComputer.setModifyName(userName);
            userComputer.setModifyTime(DateHelper.now());

            employeesService.insert(employees);
            userManagerService.insertUser(userManager);
            userComputerService.insert(userComputer);
            jsonObject.put("success", 1);
            jsonObject.put("msg", "账号创建成功");
        } catch (Exception e) {
            e.getLocalizedMessage();
            jsonObject.put("success", 0);
            jsonObject.put("msg", "账号创建失败");
        }
        return jsonObject.toString();
    }
}
