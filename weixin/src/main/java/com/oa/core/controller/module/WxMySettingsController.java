package com.oa.core.controller.module;

import com.oa.core.bean.Loginer;
import com.oa.core.bean.module.Department;
import com.oa.core.bean.module.Employees;
import com.oa.core.bean.user.UserManager;
import com.oa.core.controller.util.LoginUtil;
import com.oa.core.helper.DateHelper;
import com.oa.core.helper.StringHelper;
import com.oa.core.service.module.DepartmentService;
import com.oa.core.service.module.EmployeesService;
import com.oa.core.service.user.UserManagerService;
import com.oa.core.service.util.TableService;
import com.oa.core.util.MD5Util;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.json.JSONObject;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Iterator;

/**
 * @ClassName:WxMySettingsController
 * @author:zxd
 * @Date:2019/04/08
 * @Time:上午 9:02
 * @Version V1.0
 * @Explain
 */
@Controller
@RequestMapping("/weixin/mysettings")
public class WxMySettingsController {
    @Autowired
    TableService tableService;

    @Autowired
    EmployeesService employeesService;

    @Autowired
    DepartmentService departmentService;

    @Autowired
    UserManagerService userManagerService;

    /**
     * 获取所有消息
     *
     * @return String
     */
    @RequestMapping(value = "/getuser", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String getUser(HttpServletRequest request, String userid) {
        String userId = request.getParameter("userid");
        JSONObject json = new JSONObject();
        try {
            JSONObject empjson = new JSONObject();
            Employees employees = employeesService.selectByUserId(userId);
            if (employees != null) {
                String staffid = employees.getStaffId();
                String name = employees.getStaffName();
                String phone = employees.getPhone();
                String photo = employees.getPhoto();
                String nativePlace = employees.getNativePlace();
                String address = employees.getAddress();
                String mailbox = employees.getMailbox();
                empjson.put("staffid", staffid);
                empjson.put("name", name == null ? userId : name);
                empjson.put("phone", phone == null ? "" : phone);
                empjson.put("photo", photo == null ? "" : photo);
                empjson.put("nativePlace", nativePlace == null ? "" : nativePlace);
                empjson.put("address", address == null ? "" : address);
                empjson.put("mailbox", mailbox == null ? "" : mailbox);
            }
            Department department = departmentService.selectById(employees.getDepartment());
            if (department != null) {
                String deptname = department.getDeptName();
                empjson.put("deptname", deptname == null ? "" : deptname);
            } else {
                empjson.put("deptname", "");
            }

            json.put("msg", "");
            json.put("success", 1);
            json.put("data", empjson);
        } catch (Exception e) {
            json.put("msg", "获取人员信息失败");
            json.put("success", 0);
            e.getLocalizedMessage();
        } finally {
            return json.toString();
        }
    }

    /**
     * 获取所有消息
     *
     * @return String
     */
    @RequestMapping(value = "/setpassword", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String setPassword(HttpServletRequest request, String userid) {
        String userId = request.getParameter("userid");
        String password = request.getParameter("passwd") == null ? "" : request.getParameter("passwd");
        UserManager userManager = new UserManager();
        userManager.setUserName(userId);
        userManager.setPassword(MD5Util.getMD5(password));
        userManager.setRecordName(userId);
        userManager.setModifyName(userId);
        userManager.setRecordTime(DateHelper.now());
        userManager.setModifyTime(DateHelper.now());

        JSONObject json = new JSONObject();
        try {
            userManagerService.updateUser(userManager);
            json.put("msg", "密码修改成功");
            json.put("success", 1);
        } catch (Exception e) {
            e.getLocalizedMessage();
            json.put("msg", "密码修改失败");
            json.put("success", 0);
        } finally {
            return json.toString();
        }
    }

    /**
     * 获取所有消息
     *
     * @return String
     */
    @RequestMapping(value = "/setuser", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String setUser(HttpServletRequest request) {
        String userid = request.getParameter("userid");
        JSONObject json = new JSONObject();
        try {
            Employees employees = new Employees();
            String staffid = request.getParameter("staffid");
            if (staffid != null && !staffid.equals("")) {
                System.out.println(staffid);
                employees.setStaffId(staffid);
            }
            String name = request.getParameter("name");
            if (name != null && !name.equals("")) {
                name = new String(name.getBytes("iso-8859-1"), "UTF-8");
                employees.setStaffName(name);
            }
            String phone = request.getParameter("phone");
            if (phone != null && !phone.equals("")) {
                employees.setPhone(phone);
            }
            String photo = request.getParameter("photo");
            System.out.println(photo+"11111111111111111111");
            if (photo != null && !photo.equals("")) {
                employees.setPhoto(photo);
            }
            String nativePlace = request.getParameter("nativePlace");
            if (nativePlace != null && !nativePlace.equals("")) {
                nativePlace = new String(nativePlace.getBytes("iso-8859-1"), "UTF-8");
                employees.setNativePlace(nativePlace);
            }
            String address = request.getParameter("address");
            if (address != null && !address.equals("")) {
                address = new String(address.getBytes("iso-8859-1"), "UTF-8");
                employees.setAddress(address);
            }
            String mailbox = request.getParameter("mailbox");
            if (mailbox != null && !mailbox.equals("")) {
                employees.setMailbox(mailbox);
            }

            employees.setModifyName(userid);
            employees.setModifyTime(DateHelper.now());
            employeesService.update(employees);

            Loginer loginer = LoginUtil.getLoginer(userid);
            loginer.setName(employees.getStaffName());
            request.getSession().setAttribute("loginer", loginer);
            LoginUtil.setLoginer(userid, loginer);

            json.put("msg", "");
            json.put("success", 1);
        } catch (Exception e) {
            json.put("msg", "人员信息保存失败");
            json.put("success", 0);
            e.getLocalizedMessage();
        } finally {
            return json.toString();
        }
    }
}
