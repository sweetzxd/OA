package com.oa.core.controller.system;

import com.easemob.server.example.api.impl.EasemobChatGroup;
import com.easemob.server.example.api.impl.EasemobIMUsers;
import com.easemob.server.example.util.UserUtil;
import com.oa.core.bean.Loginer;
import com.oa.core.bean.module.Employees;
import com.oa.core.bean.user.UserManager;
import com.oa.core.helper.StringHelper;
import com.oa.core.listener.InitDataListener;
import com.oa.core.service.module.DepartmentService;
import com.oa.core.service.module.EmployeesService;
import com.oa.core.service.user.UserManagerService;
import com.oa.core.service.util.TableService;
import com.oa.core.util.CommonUtil;
import com.oa.core.util.SpringContextUtil;
import com.oa.core.util.ToNameUtil;
import io.swagger.client.model.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @ClassName:ChitChatController
 * @author:zxd
 * @Date:2019/04/16
 * @Time:上午 9:35
 * @Version V1.0
 * @Explain
 */
@Controller
@RequestMapping("/cc")
public class ChitChatController {
    @Autowired
    UserManagerService userManagerService;
    @Autowired
    EmployeesService employeesService;
    @Autowired
    DepartmentService departmentService;
    @Autowired
    TableService tableService;

    private EasemobIMUsers easemobIMUsers = new EasemobIMUsers();
    private EasemobChatGroup easemobChatGroup = new EasemobChatGroup();



    @RequestMapping(value = "/chathome", method = RequestMethod.GET)
    public ModelAndView gotoChatHome(HttpServletRequest request) {
        String userid = request.getParameter("userid");
        String username = ToNameUtil.getName("user", userid);
        ModelAndView model = new ModelAndView("chitchat/chathome");
        model.addObject("userid",userid);
        model.addObject("username",username);
        return model;
    }

    @RequestMapping(value = "/moblie", method = RequestMethod.GET)
    public ModelAndView gotoMoblie(HttpServletRequest request) {
        String userid = request.getParameter("userid");
        String username = ToNameUtil.getName("user", userid);
        ModelAndView model = new ModelAndView("chitchat/moblie");
        model.addObject("userid",userid);
        model.addObject("username",username);
        return model;
    }

    @RequestMapping("/getuserlist")
    @ResponseBody
    public String getUserList(HttpServletRequest request, String userid) {
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        String username = "";
        if (loginer == null) {
            username = ToNameUtil.getName("user", userid);
        } else {
            userid = loginer.getId();
            username = loginer.getName();
        }

        JSONObject json = new JSONObject();
        try {
            JSONObject data = new JSONObject();
            JSONObject mine = new JSONObject();
            Employees employees = employeesService.selectByUserId(userid);
            mine.put("username", username);
            mine.put("id", userid);
            mine.put("status", "online");
            mine.put("sign", "努力工作快乐生活");
            String photo = null;
            if (employees != null) {
                photo = employees.getPhoto();
            }
            if (photo != null && !photo.equals("")) {
                mine.put("avatar", photo);
            } else {
                mine.put("avatar", "/upload/photo/touxiang/头像"+ CommonUtil.getRandomNumber(10)+".png");
            }
            data.put("mine", mine);

            Hashtable<String, String> ht = InitDataListener.getMapData("chat");

            TableService tableService = (TableService) SpringContextUtil.getBean("tableService");
            List<Map<String, Object>> maps = tableService.selectSqlMapList("select recorderNO,qzmc190422001,qztp190422001,reserveField,qzry190422001 from ltqz190422001 where curStatus=2");
            JSONArray groups = new JSONArray();
            for (int i = 0, len = maps.size(); i < len; i++) {
                Map<String, Object> stringObjectMap = maps.get(i);
                String users = (String) stringObjectMap.get("qzry190422001");
                Vector<String> vector = StringHelper.string2Vector(users, ";");
                if(vector.contains(userid)) {
                    String avatar = (String) stringObjectMap.get("qztp190422001");
                    if (avatar == null || avatar.equals("")) {
                        avatar = "/upload/photo/touxiang/头像"+ CommonUtil.getRandomNumber(10)+".png";
                    }
                    JSONObject group = new JSONObject();
                    group.put("groupname", stringObjectMap.get("qzmc190422001"));
                    group.put("id", stringObjectMap.get("reserveField"));
                    group.put("avatar", avatar);
                    groups.put(i, group);
                }
            }

            JSONObject list = new JSONObject();
            list.put("mine", mine);
            list.put("friend", ht.get("friend"));
            list.put("group", groups);
            json.put("code", 0);
            json.put("success", 1);
            json.put("msg", "");
            json.put("data", list);
        } catch (Exception e) {
            e.getLocalizedMessage();
            json.put("code", 1);
            json.put("success", 0);
            json.put("msg", "获取失败");
        }
        return json.toString();
    }

    @RequestMapping(value = "/getmembers", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String getMembers(HttpServletRequest request, @RequestParam String id) {
        JSONObject json = new JSONObject();
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        try {
            Map<String, Object> maps = tableService.selectSqlMap("select qzry190422001 from ltqz190422001 where reserveField = '" + id + "'");
            if (maps != null) {
                String users = (String) maps.get("qzry190422001");
                List<String> usernames = StringHelper.string2Vector(users, ";");
                List<Employees> employees = employeesService.selectByUserNames_emp(usernames);
                for (int i = 0, len = employees.size(); i < len; i++) {
                    Employees emp = employees.get(i);
                    String photo = emp.getPhoto();
                    if (photo == null || photo.equals("")) {
                        photo = "/upload/photo/touxiang/头像"+ CommonUtil.getRandomNumber(10)+".png";
                    }
                    JSONObject ejson = new JSONObject();
                    ejson.put("username", emp.getStaffName());
                    ejson.put("id", emp.getUserName());
                    ejson.put("avatar", photo);
                    ejson.put("sign", emp.getPhone());
                    jsonArray.put(i, ejson);

                }
            }

            jsonObject.put("list", jsonArray);
            json.put("code", 0);
            json.put("success", 1);
            json.put("msg", "");
            json.put("data", jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
            json.put("code", 1);
            json.put("success", 0);
            json.put("msg", "获取失败");
        }

        return json.toString();
    }


    @RequestMapping(value = "/getusers", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ModelAndView getUsers(HttpServletRequest request) {
        List<UserManager> userManagers = userManagerService.selectAll();
        JSONArray json = new JSONArray(userManagers);
        ModelAndView model = new ModelAndView("chitchat/account");
        model.addObject("users", json);
        return model;
    }


    @RequestMapping(value = "/userstatus", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String getUserStatus(HttpServletRequest request) {
        JSONObject json = new JSONObject();
        String status = "offline";
        String userName = request.getParameter("id");
        try {
            Object result = easemobIMUsers.getIMUserStatus(userName);
            System.out.println(result.toString());
            JSONObject jsonObject = new JSONObject(result.toString());
            System.out.println(jsonObject.getInt("count"));
            if (jsonObject.getInt("count") == 0) {
                JSONObject data = jsonObject.getJSONObject("data");
                status = (String) data.get(userName);
                json.put("data", status);
                json.put("success", 1);
            }
        } catch (Exception e) {
            json.put("msg", "获取状态失败");
            json.put("success", 0);
        }
        return json.toString();
    }

    @RequestMapping(value = "/addChatLog", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String addChatLog(HttpServletRequest request, @RequestParam HashMap<String, Object> hm) {
        JSONObject json = new JSONObject();
        try {

            json.put("msg", "保存失败");
            json.put("success", 1);
        } catch (Exception e) {
            json.put("msg", "保存成功");
            json.put("success", 0);
        }
        return json.toString();
    }

    @RequestMapping(value = "/addChatUsers", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String addChatUsers(HttpServletRequest request) {
        List<Map<String, Object>> maps = tableService.selectSqlMapList("select userName,staffName from userInfo");
        for (int i = 0, len = maps.size(); i < len; i++) {
            RegisterUsers users = new RegisterUsers();
            Map<String, Object> stringObjectMap = maps.get(i);
            String userName = (String) stringObjectMap.get("userName");
            String staffName = (String) stringObjectMap.get("staffName");
            User user = new User().username(userName).password("oa123456");

            users.add(user);

            try {
                Object result = easemobIMUsers.createNewIMUserSingle(users);
                System.out.println(result.toString());
                Assert.assertNotNull(result);
            }catch (Exception e){
                e.getLocalizedMessage();

            }
        }

        return "";
    }

    @Test
    public void test(){
        RegisterUsers users = new RegisterUsers();
        User user = new User().username("zhenxudong").password("oa123456");
        User user1 = new User().username("wangpengsen").password("oa123456");
        users.add(user);
        users.add(user1);
        try {
            Object result = easemobIMUsers.createNewIMUserSingle(users);
            System.out.println(result.toString());
            Assert.assertNotNull(result);
        }catch (Exception e){
            e.getLocalizedMessage();

        }
    }


    @RequestMapping(value = "/addChatGroup", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String addChatGroup(HttpServletRequest request) {
        List<Map<String, Object>> maps = tableService.selectSqlMapList("select recorderNO,qzmc190422001,qzry190422001,qztp190422001,reserveField from ltqz190422001 where curStatus=2");
        for (int i = 0, len = maps.size(); i < len; i++) {
            Map<String, Object> stringObjectMap = maps.get(i);
            String recorderNO = (String) stringObjectMap.get("recorderNO");
            String name = (String) stringObjectMap.get("qzmc190422001");
            String groupid = (String) stringObjectMap.get("reserveField");
            String users = (String) stringObjectMap.get("qzry190422001");
            if (groupid == null || groupid.equals("")) {
                Group group = new Group();
                group.groupname(name).desc(name)._public(true).maxusers(300).approval(false).owner("admin");
                String result = (String) easemobChatGroup.createChatGroup(group);
                result = result.replaceAll("\\n", "");
                if (result != null) {
                    JSONObject jsonObject = new JSONObject(result);
                    if (!jsonObject.isNull("data")) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        groupid = data.getString("groupid");
                        tableService.updateSqlMap("update ltqz190422001 set reserveField='" + groupid + "' where recorderNO='" + recorderNO + "'");
                    }
                }
            } else {
                ModifyGroup group = new ModifyGroup();
                group.description(name).groupname(name).maxusers(300);
                easemobChatGroup.modifyChatGroup(groupid, group);
            }
            if (users != null && !users.equals("")) {
                Vector<String> u = StringHelper.string2Vector(users, ";");
                for (String user : u) {
                    UserNames userNames = new UserNames();
                    UserName userList = new UserName();
                    userList.add(user);
                    userNames.usernames(userList);
                    easemobChatGroup.addBatchUsersToChatGroup(groupid, userNames);
                }
            }
        }
        new InitDataListener("chat");
        return "";
    }
}
