package com.oa.core.controller.user;

import com.oa.core.bean.Loginer;
import com.oa.core.helper.DateHelper;
import com.oa.core.interceptor.Logined;
import com.oa.core.listener.InitDataListener;
import com.oa.core.service.user.UserManagerService;
import com.oa.core.util.MD5Util;
import com.oa.core.util.PinyinUtil;
import com.oa.core.util.PrimaryKeyUitl;
import com.oa.core.bean.user.UserManager;
import com.oa.core.bean.util.PageUtil;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @ClassName:UserController
 * @author zxd
 * @date 2018/09/15
 * @Time:11:10
 * @Version V1.0
 **/
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserManagerService userManagerService;

    /**
     * 跳转用户页面
     * @param
     * @return String
     */
    @RequestMapping("/gotousermanager")
    public ModelAndView gotoUserManager(HttpServletRequest request){
        ModelAndView mav = new ModelAndView("manage/usermanager");
        mav.addObject("title", "人员管理");
        mav.addObject("type", "list");
        return mav;
    }

    @Logined
    @RequestMapping("/gotopwupdate")
    public ModelAndView gotoPwUpdate(HttpServletRequest request){
        ModelAndView mav = new ModelAndView("module/myinformation");
        mav.addObject("type", "user");
        return mav;
    }

    /**
     * 获取所有用户
     * @return String
     */
    @RequestMapping("/selectAll")
    @ResponseBody
    public String selectAll(HttpServletRequest request,String inputval, String option, int page, int limit){
        PageUtil pu = new PageUtil();
        pu.setPageSize(limit);
        pu.setCurrentPage(page);
        UserManager user = new UserManager();
        if (inputval != null && inputval != "") {
            user = new UserManager(option,inputval);
        }
        user.setStartRow(pu.getStartRow());
        user.setEndRow(pu.getEndRow()-pu.getStartRow());
        List<UserManager> userList = userManagerService.selectUserAll(user);
        int count = userManagerService.selectUserByCount(user);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 0);
        jsonObject.put("msg", "");
        jsonObject.put("count", count);
        jsonObject.put("data", userList);
        jsonObject.put("success",1);
        return jsonObject.toString();
    }

    /**
     * 根据ID获取用户
     * @param username
     * @return userManager
     */
    @RequestMapping("/selectUser")
    @ResponseBody
    public UserManager selectUserById(HttpServletRequest request,String username){
        UserManager userManager = userManagerService.selectUserById(username);
        return userManager;
    }

    /**
     * 新增或编辑用户信息
     * @param userManager
     * @return String
     */
    @RequestMapping("/gotoinsert")
    public ModelAndView gotoInsert(HttpServletRequest request,UserManager userManager){
        ModelAndView mav = new ModelAndView("manage/usermanager");
        mav.addObject("title", "新增账号信息");
        mav.addObject("type", "add");
        return mav;
    }

    @Logined
    @RequestMapping(value = "/insertuser", method = RequestMethod.POST, produces = {"text/html;charset=UTF-8;", "application/json;"})
    @ResponseBody
    public String insertUser(HttpServletRequest request, UserManager userManager) {
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        try {
            String password = userManager.getPassword();
            userManager.setPassword(MD5Util.getMD5(password));
            if (loginer != null) {
                String userId = loginer.getId();
                userManager.setRecordName(userId);
                userManager.setModifyName(userId);
            }
            userManager.setRecordTime(DateHelper.now());
            userManager.setModifyTime(DateHelper.now());
            userManagerService.insertUser(userManager);
            return "1";
        } catch (Exception e) {
            return "0";
        }
    }

    /**
     * 新增或编辑用户信息
     * @param username
     * @return String
     */
    @RequestMapping("/gotoupdate")
    public ModelAndView gotoUpdate(HttpServletRequest request,String username){
        ModelAndView mav = new ModelAndView("manage/usermanager");
        UserManager userManager = userManagerService.selectUserById(username);
        mav.addObject("user", userManager);
        mav.addObject("title", "【"+userManager.getName()+"】的账号信息修改");
        mav.addObject("type", "modi");
        return mav;
    }

    @Logined
    @RequestMapping(value = "/updateuser", method = RequestMethod.POST, produces = {"text/html;charset=UTF-8;", "application/json;"})
    @ResponseBody
    public String updateUser(HttpServletRequest request, UserManager userManager) {
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        String password = userManager.getPassword();
        userManager.setPassword(MD5Util.getMD5(password));
        if (loginer != null) {
            String userId = loginer.getId();
            userManager.setModifyName(userId);
        }
        try {
            userManager.setModifyTime(DateHelper.now());
            userManagerService.updateUser(userManager);
            return "1";
        } catch (Exception e) {
            return "0";
        }
    }

    /**
     * 删除用户
     * @param username 用户ID
     * @return
     */
    @RequestMapping("/deleteuser")
    @ResponseBody
    public String del(HttpServletRequest request,String username){
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        UserManager userManager = new UserManager();
        if (loginer != null) {
            String userId = loginer.getId();
            userManager.setDeleteName(userId);
        }
        try {
            userManager.setDeleteTime(DateHelper.now());
            if(username.indexOf(";")>0){
                username = username.replaceAll(";","','");
                username = "'"+username+"'";
                userManager.setUserName(username);
                userManagerService.deleteAllUser(userManager);
            }else{
                userManager.setUserName(username);
                userManagerService.deleteUser(userManager);
            }
            return "1";
        }catch (Exception e){
            e.printStackTrace();
            return "0";
        }
    }

    @RequestMapping("/userexist")
    @ResponseBody
    public String userExist(HttpServletRequest request,String username){
        UserManager user = userManagerService.selectUserById(username);
        if(user!=null){
            return "1";
        }else{
            return "0";
        }
    }

    @Logined
    @RequestMapping(value = "/pw_validation", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String passwordValidation(HttpServletRequest request){
        try {
            Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
            String userId = loginer.getId();
            String password = request.getParameter("password") == null ? "" : request.getParameter("password");
            UserManager user = userManagerService.selectUserById(userId);
            String pw = user.getPassword();
            if (MD5Util.checkPassword(password,pw)) {
                return "1";
            } else {
                return "0";
            }
        }catch (Exception e){
            return "0";
        }
    }

    @Logined
    @RequestMapping(value = "/updatepw", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String updatePassWord(HttpServletRequest request){
        try {
            Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
            String userId = loginer.getId();
            String new_password = request.getParameter("new_password") == null ? "" : request.getParameter("new_password");
            UserManager user = userManagerService.selectUserById(userId);
            user.setPassword(MD5Util.getMD5(new_password));
            user.setAccountStatus(2);
            user.setUserStatus(2);
            user.setModifyName(userId);
            user.setModifyTime(DateHelper.now());
            userManagerService.updateUser(user);
            return "1";
        }catch (Exception e){
            return "0";
        }
    }

    /**
     * 获取所有用户
     * @return String
     */
    @RequestMapping("/selectuserinfo")
    @ResponseBody
    public String selectUserInfo(HttpServletRequest request,String inputval, String option, int page, int limit){
        PageUtil pu = new PageUtil();
        pu.setPageSize(limit);
        pu.setCurrentPage(page);
        String userName=null,staffName=null,deptId=null,deptName=null;
        if (inputval != null && inputval != "") {
            if ("userName".equals(option)) {
                userName=inputval;
            } else if ("staffName".equals(option)) {
                staffName=inputval;
            } else if ("deptId".equals(option)) {
                deptId=inputval;
            } else if ("deptName".equals(option)) {
                deptName=inputval;
            }
        }
        int StartRow = pu.getStartRow();
        int EndRow = pu.getEndRow()-pu.getStartRow();
        List<Map<String,String>> userList = userManagerService.selectUserInfo(userName,staffName,deptId,deptName,StartRow,EndRow);
        int count = userManagerService.selectUserInfoByCount(userName,staffName,deptId,deptName);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 0);
        jsonObject.put("msg", "");
        jsonObject.put("count", count);
        jsonObject.put("data", userList);
        jsonObject.put("success",1);
        return jsonObject.toString();
    }

    /**
     * 获取所有用户，按部门选择人员用
     * @return String
     */
    @RequestMapping("/selectuserbydept")
    @ResponseBody
    public String selectUserByDept(HttpServletRequest request,String inputval, String option, int page, int limit){
        PageUtil pu = new PageUtil();
        pu.setPageSize(limit);
        pu.setCurrentPage(page);
        int StartRow = pu.getStartRow();
        int EndRow = pu.getEndRow()-pu.getStartRow();
        List<Map<String,String>> userList = userManagerService.selectUserByDept(inputval,StartRow,EndRow);
        int count = userManagerService.selectUserByDeptCount(inputval);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 0);
        jsonObject.put("msg", "");
        jsonObject.put("count", count);
        jsonObject.put("data", userList);
        jsonObject.put("success",1);
        return jsonObject.toString();
    }

    @Logined
    @RequestMapping(value = "/loaduser", method = RequestMethod.POST)
    public void loadUser(HttpServletRequest request) {
        new InitDataListener("user");
    }
}