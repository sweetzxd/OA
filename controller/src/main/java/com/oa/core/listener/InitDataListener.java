package com.oa.core.listener;

import com.oa.core.bean.Loginer;
import com.oa.core.bean.dd.FieldData;
import com.oa.core.bean.dd.TableData;
import com.oa.core.bean.dd.TaskData;
import com.oa.core.bean.module.Department;
import com.oa.core.bean.module.Employees;
import com.oa.core.bean.system.RoleDefines;
import com.oa.core.bean.user.UserManager;
import com.oa.core.bean.util.PageUtil;
import com.oa.core.scada.common.Common;
import com.oa.core.scada.system.LoginAuth;
import com.oa.core.scada.util.URIEncoder;
import com.oa.core.scada.websocket.MyWebSocketClient;
import com.oa.core.service.ListenerService;

import com.oa.core.service.module.DepartmentService;
import com.oa.core.service.module.EmployeesService;
import com.oa.core.service.util.TableService;
import com.oa.core.util.*;
import org.apache.http.client.CookieStore;
import org.java_websocket.WebSocket;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.net.URISyntaxException;
import java.util.*;

/**
 * @method:
 * @param:
 * @return:
 * @author: zxd
 * @date: 2019/04/22
 * @description: 方法说明
 */
public class InitDataListener implements InitializingBean, ServletContextAware {

    @Autowired
    ListenerService listenerService;
    private static Map<String, Hashtable> dicMap = new HashMap<String, Hashtable>();
    private static Map<String, List> idMap = new HashMap<String, List>();
    private static Map<String, Object> idObj = new HashMap<String, Object>();
    public InitDataListener() {

    }

    public InitDataListener(String key) {
        listenerService = (ListenerService) SpringContextUtil.getBean("listenerService");
        try {
            switch (key) {
                case "fieldData":
                    initFieldData();
                    break;
                case "tableDate":
                    initTableData();
                    break;
                case "taskData":
                    initTaskData();
                    break;
                case "user":
                    initUser();
                    break;
                case "role":
                    initRole();
                    break;
                case "chat":
                    chatUsers();
                    break;
                case "employees":
                    initEmployees();
                    break;
                case "department":
                    initDepartment();
                    break;
                default:
            }
        } catch (Exception e) {
            LogUtil.sysLog(">系统初始化出错:"+key);
            LogUtil.sysLog(e);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            initFieldData();
        } catch (Exception e) {
            LogUtil.sysLog("1>系统初始化出错");
            LogUtil.sysLog(e);
        }
        try {
            initTableData();
        } catch (Exception e) {
            LogUtil.sysLog("2>系统初始化出错");
            LogUtil.sysLog(e);
        }
        try {
            initTaskData();
        } catch (Exception e) {
            LogUtil.sysLog("3>系统初始化出错");
            LogUtil.sysLog(e);
        }
        try {
            initUser();
        } catch (Exception e) {
            LogUtil.sysLog("4>系统初始化出错");
            LogUtil.sysLog(e);
        }
        try {
            initRole();
        } catch (Exception e) {
            LogUtil.sysLog("5>系统初始化出错");
            LogUtil.sysLog(e);
        }
        try {
            chatUsers();
        } catch (Exception e) {
            LogUtil.sysLog("6>系统初始化出错");
            LogUtil.sysLog(e);
        }
        try {
            initEmployees();
        } catch (Exception e) {
            LogUtil.sysLog("7>系统初始化出错");
            LogUtil.sysLog(e);
        }
        try {
            initDepartment();
        } catch (Exception e) {
            LogUtil.sysLog("8>系统初始化出错");
            LogUtil.sysLog(e);
        }
    }

    public static Hashtable<String, String> getMapData(String key) {
        if (dicMap.get(key) == null) {
            new InitDataListener(key);
        }
        return dicMap.get(key);
    }

    public static List<String> getData(String key) {
        if (idMap.get(key) == null) {
            new InitDataListener(key);
        }
        return idMap.get(key);
    }

    public static List<String> putData(String key, List<String> list) {
        return idMap.put(key, list);
    }

    public static List getListData(String key) {
        if (idMap.get(key) == null) {
            new InitDataListener(key);
        }
        return idMap.get(key);
    }

    public static Object getObjData(String key) {
        if (idObj.get(key) == null) {
            new InitDataListener(key);
        }
        return idObj.get(key);
    }

    /**
     * 1.初始化字段定义
     */
    private void initFieldData() throws Exception {
        Hashtable<String, String> hs = new Hashtable<String, String>();
        LogUtil.sysLog("1.初始化字段定义到缓存中开始");
        hs = getNoField(hs);
        List<FieldData> tableList = listenerService.listenerField();
        List<String> idList = new ArrayList<String>();
        for (FieldData fieldData : tableList) {
            hs.put(fieldData.getFieldName(), fieldData.getFieldTitle());
            idList.add(fieldData.getFieldName());
        }
        dicMap.put("fieldData", hs);
        idMap.put("field", idList);
        LogUtil.sysLog("1.初始化字段定义到缓存中完成");
    }

    /**
     * 2.初始化表定义
     */
    private void initTableData() throws Exception {
        Hashtable<String, String> hs = new Hashtable<String, String>();
        LogUtil.sysLog("2.初始化表定义到缓存中开始");
        List<TableData> tableList = listenerService.listenerTable();
        List<String> idList = new ArrayList<String>();
        for (TableData tableData : tableList) {
            hs.put(tableData.getTableName(), tableData.getTableTitle());
            idList.add(tableData.getTableName());
        }
        dicMap.put("tableDate", hs);
        idMap.put("field", idList);
        LogUtil.sysLog("2.初始化表定义到缓存中完成");
    }

    /**
     * 3.初始化任务定义
     */
    private void initTaskData() throws Exception {
        Hashtable<String, String> hs = new Hashtable<String, String>();
        LogUtil.sysLog("3.初始化任务定义到缓存中开始");
        List<TaskData> taskList = listenerService.listenerTask();
        List<String> idList = new ArrayList<String>();
        for (TaskData taskData : taskList) {
            hs.put(taskData.getTaskName(), taskData.getTaskTitle());
            idList.add(taskData.getTaskName());
        }
        dicMap.put("taskData", hs);
        idMap.put("field", idList);
        LogUtil.sysLog("3.初始化任务定义到缓存中完成");
    }

    /**
     * 4.初始化账户信息
     */
    private void initUser() throws Exception {
        Hashtable<String, String> hs = new Hashtable<String, String>();
        LogUtil.sysLog("4.初始化账户信息到缓存中开始");
        List<UserManager> users = listenerService.listenerUser();
        for (UserManager user : users) {
            hs.put(user.getUserName(), user.getName());
        }
        dicMap.put("user", hs);
        LogUtil.sysLog("4.初始化账户信息到缓存中完成");
    }

    /**
     * 5.初始化角色信息
     */
    private void initRole() throws Exception {
        Hashtable<String, String> hs = new Hashtable<String, String>();
        LogUtil.sysLog("5.初始化角色信息到缓存中开始");
        List<RoleDefines> roles = listenerService.listenerRole();
        for (RoleDefines role : roles) {
            hs.put(role.getRoleId(), role.getUserName());
        }
        dicMap.put("role", hs);
        LogUtil.sysLog("5.初始化角色信息到缓存中完成");
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
    }

    public Hashtable<String, String> getNoField(Hashtable<String, String> hs) {
        hs.put("staffId", "员工编号");
        hs.put("staffName", "员工姓名");
        hs.put("userName", "员工账号");
        hs.put("deptId", "部门编号");
        return hs;
    }


    public void chatUsers() throws Exception {
        LogUtil.sysLog("6.初始化聊天组到缓存中开始");
        DepartmentService departmentService = (DepartmentService) SpringContextUtil.getBean("departmentService");
        List<Department> myurllist = departmentService.selectAll();
        JSONArray userList = new JSONArray(DeptUtil.getMenu(myurllist, true, true, true));
        JSONArray friends = new JSONArray();
        int num = 0;
        for (int i = 0, len = userList.length(); i < len; i++) {
            JSONObject jsonObject = userList.getJSONObject(i);
            friends.put(num, DDUtil.getDept(jsonObject, num));
            JSONArray children = jsonObject.getJSONArray("children");
            num++;
            if (children.length() > 0) {
                for (int c = 0, clen = children.length(); c < clen; c++) {
                    JSONObject cjsonObject = children.getJSONObject(c);
                    friends.put(num, DDUtil.getDept(cjsonObject, num));
                    num++;
                }
            }
        }
        Hashtable<String, Object> hs = new Hashtable<>();
        hs.put("friend", friends);
        dicMap.put("chat", hs);
        LogUtil.sysLog("6.初始化聊天组到缓存中结束");

    }

    public void initEmployees() throws Exception{
        LogUtil.sysLog("7.初始化人员结构到缓存中开始");
        Employees employees = new Employees();
        PageUtil pageUtil = new PageUtil();
        pageUtil.setPageSize(1000);
        pageUtil.setCurrentPage(1);
        employees.setStartRow(pageUtil.getStartRow());
        employees.setEndRow(pageUtil.getEndRow() - pageUtil.getStartRow());
        EmployeesService employeesService = (EmployeesService) SpringContextUtil.getBean("employeesService");
        List<Employees> employeesList = employeesService.selectAllTerms(employees);
        idMap.put("emp", employeesList);
        LogUtil.sysLog("7.初始化人员结构到到缓存中结束");
    }

    public void initDepartment() throws Exception{
        LogUtil.sysLog("8.初始化组织结构到缓存中开始");
        DepartmentService departmentService = (DepartmentService) SpringContextUtil.getBean("departmentService");
        List<Department> myurllist = departmentService.selectAll();
        String menu = DeptUtil.getMenu(myurllist, true);
        idObj.put("dept", menu);
        LogUtil.sysLog("8.初始化组织结构到缓存中结束");
    }
}
