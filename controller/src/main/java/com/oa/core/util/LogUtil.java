package com.oa.core.util;

import com.oa.core.bean.dd.FieldData;
import com.oa.core.bean.dd.TableData;
import com.oa.core.bean.dd.TaskData;
import com.oa.core.bean.module.Department;
import com.oa.core.bean.module.Employees;
import com.oa.core.bean.module.Message;
import com.oa.core.bean.system.RoleDefines;
import com.oa.core.bean.user.UserManager;
import com.oa.core.example.JPushAllService;
import com.oa.core.helper.DateHelper;
import com.oa.core.helper.EmailHelper;
import com.oa.core.helper.StringHelper;
import com.oa.core.service.dd.DictionaryService;
import com.oa.core.service.module.DepartmentService;
import com.oa.core.service.module.EmployeesService;
import com.oa.core.service.module.MessageService;
import com.oa.core.service.system.RoleDefinesService;
import com.oa.core.service.user.UserManagerService;
import com.oa.core.socket.WebsocketEndPoint;
import com.oa.core.tag.UserDict;
import org.springframework.web.socket.TextMessage;

import java.sql.Timestamp;
import java.util.*;

/**
 * @ClassName:TaskUtil
 * @author:zxd
 * @Date:2018/10/13
 * @Time:上午 9:15
 * @Version V1.0
 * @Explain 生成table的日志输入数据
 */
public class LogUtil {

    public static String getFlowTableLog(String formTask, String nodeTitle, String table, Map<String, Object> tableMap) {
        String name = UserDict.getName((String) tableMap.get("modifyName"));
        String time = DateHelper.getDateString((Timestamp) tableMap.get("modifyTime"), Const.YEAR_MONTH_DAY_HH_MM_SS);
        String fieldHtml = "<tr id='" + tableMap.get("recorderNO") + "' style='display:none'></tr>";
        DictionaryService d = (DictionaryService) SpringContextUtil.getBean("dictionaryService");
        TableData tableData = d.selectTableName(table);
        TaskData taskData = d.selectTaskName(formTask);
        Vector<String> fields = StringHelper.string2Vector(taskData.getTaskField(), ";");
        fieldHtml += "<tr>";
        if (tableMap != null && tableMap.size() > 0) {
            int size = fields.size();
            int ci = size % 2;
            for (int i = 0; i < size; i++) {
                String field = fields.get(i);
                int num = i + 1;
                if (i > 0 && i % 2 == 0) {
                    fieldHtml += "<tr>";
                }
                fieldHtml += getInfoField(d, tableMap, field);
                if (num == size) {
                    for (int n = 0; n < ci; n++) {
                        fieldHtml += "<td></td><td></td>";
                    }
                    num = ci + num;
                }
                if (num % 2 == 0) {
                    fieldHtml += "</tr>";
                }
            }
            fieldHtml += "<tr><td colspan='4'>";
            fieldHtml += "<div style='float:left; width:50%;text-align:center;'><label class='layui-form-text'>发送人:" + name + "</label></div>";
            fieldHtml += "<div style='float:right;width:50%;text-align:center;'><label class='layui-form-text'>发送时间:" + time + "</label></div>";
            fieldHtml += "</td></tr>";
        }

        String tableHtml = "";
        tableHtml += "<table class='layui-table' lay-skin='line' lay-size='sm'>";
        tableHtml += "<colgroup>";
        for (int i = 0; i < 2; i++) {
            tableHtml += "<col width='70'>";
            tableHtml += "<col width='120'>";
        }
        tableHtml += "</colgroup>";
        tableHtml += "<thead>";
        tableHtml += "<tr>";
        tableHtml += "<th colspan='4'>" + nodeTitle + "</th>";   //"-" + tableData.getTableTitle() +
        tableHtml += "</tr>";
        tableHtml += "</thead>";
        tableHtml += "<tbody>";
        tableHtml += fieldHtml;
        tableHtml += "</tbody>";
        tableHtml += "</table>";
        return tableHtml;
    }

    public static String getInfoField(DictionaryService d, Map<String, Object> sqlvalue, String field) {
        String fieldHtml = "", title = "字段", value = "";
        FieldData fieldData = d.selectFieldName(field);
        if (fieldData != null) {
            String type = fieldData.getFieldType();
            title = fieldData.getFieldTitle();
            value = FieldTypeUtil.fieldInfo(fieldData, sqlvalue.get(field), type);
        } else {
            value = (String) sqlvalue.get(field);
        }
        fieldHtml += "<td><div style='width:90%;text-align:right;'><label class='layui-form-text'>" + title + ":</label></div></td>";
        fieldHtml += "<td><label style='width:90%;text-align:left;' class='layui-form-text'>" + value + "</label></td>";
        return fieldHtml;
    }

    public static void sysLog(Object msg) {
        System.out.println(msg);
    }

    public static void sysLog(String type, String msg) {

    }



    public static void toHtml(String user, Object msgval) {
        WebsocketEndPoint websocketEndPoint = new WebsocketEndPoint();
        String msg = "{\"type\":\"mymsgnum\",\"value\":" + msgval + "}";
        websocketEndPoint.sendMessageToUser(user, new TextMessage(msg));
    }

    public static void toHtml(String type, Vector<String> users, Object msgval) {
        WebsocketEndPoint websocketEndPoint = new WebsocketEndPoint();
        String msg = "{\"type\":\"" + type + "\",\"value\":" + msgval + "}";
        for (String user : users) {
            websocketEndPoint.sendMessageToUser(user, new TextMessage(msg));
        }
    }

    public static void toHtml(String type, String user, Object msgval) {
        WebsocketEndPoint websocketEndPoint = new WebsocketEndPoint();
        String msg = "{\"type\":\"" + type + "\",\"value\":" + msgval + "}";
        websocketEndPoint.sendMessageToUser(user, new TextMessage(msg));
    }

    public static void toEmail(String user,String title){
        String msg = title;
        toEmail(user, title, msg);
    }
    public static void toEmail(Vector<String> users,String title){
        String msg = title;
        toEmail(users, title, msg);
    }
    public static void toEmail(Vector<String> users,String title,String msg){
        for (String user : users) {
            toEmail(user, title, msg);
        }
    }
    public static void toEmail(String user,String title,String msg){
        EmailHelper.email(user, title, msg);
    }

    public static void toPhone(Vector users, String title) {
        toPhone(users, title, null);
    }

    public static void toPhone(Vector users,String title,String url){
        JPushAllService.jpush(users, title,url);
    }

    //发送消息
    public static void sendMessage(Message message){
        UserManagerService userManagerService = (UserManagerService) SpringContextUtil.getBean("userManagerService");
        RoleDefinesService roleDefinesService = (RoleDefinesService) SpringContextUtil.getBean("roleDefinesService");
        DepartmentService departmentService = (DepartmentService) SpringContextUtil.getBean("departmentService");
        EmployeesService employeesService = (EmployeesService) SpringContextUtil.getBean("employeesService");
        MessageService messageService = (MessageService) SpringContextUtil.getBean("messageService");
        Map<String,Message> messageMap = new HashMap<>();
        if(message.getMsgRecUser()!=null&&!message.getMsgRecUser().equals("")){
            String[] msgRecUsers = message.getMsgRecUser().substring(0,message.getMsgRecUser().length()-1).split(",");
            for(String str : msgRecUsers){
                Message m = new Message();
                m.setMsgSendUser(message.getMsgSendUser());
                m.setMsgText(message.getMsgText());
                m.setMsgTitle(message.getMsgTitle());
                m.setMsgSendTime(DateHelper.now().toString());
                m.setMsgType(message.getMsgType());
                m.setMsgUrl(message.getMsgUrl());
                m.setMsgFile(message.getMsgFile());
                m.setMsgLevel(message.getMsgLevel());
                m.setMsgStatus(1);
                m.setRecordName(message.getMsgSendUser());
                m.setRecordTime(DateHelper.now());
                m.setModifyName(message.getMsgSendUser());
                m.setModifyTime(DateHelper.now());
                PrimaryKeyUitl pk = new PrimaryKeyUitl();
                String id = pk.getNextId("Message","msgId");
                m.setMsgId(id);
                String[] msgRecUser = str.split("-");
                if(msgRecUser[0].equals("roleactor")){
                    if(msgRecUser[1].equals("allemployees")){
                        List<UserManager> userManagerList = userManagerService.selectUserAll(null);
                        for(UserManager userManager : userManagerList){
                            Message m1 = new Message();
                            m1.setMsgSendUser(message.getMsgSendUser());
                            m1.setMsgText(message.getMsgText());
                            m1.setMsgTitle(message.getMsgTitle());
                            m1.setMsgSendTime(DateHelper.now().toString());
                            m1.setMsgType(message.getMsgType());
                            m1.setMsgUrl(message.getMsgUrl());
                            m1.setMsgFile(message.getMsgFile());
                            m1.setMsgLevel(message.getMsgLevel());
                            m1.setMsgStatus(1);
                            m1.setRecordName(message.getMsgSendUser());
                            m1.setRecordTime(DateHelper.now());
                            m1.setModifyName(message.getMsgSendUser());
                            m1.setModifyTime(DateHelper.now());
                            PrimaryKeyUitl pk1 = new PrimaryKeyUitl();
                            String id1 = pk1.getNextId("Message","msgId");
                            m1.setMsgId(id1);
                            m1.setMsgRecUser(userManager.getUserName());
                            messageMap.put(userManager.getUserName(),m1);
                        }
                    }else{
                        RoleDefines roleDefines = roleDefinesService.selectById(msgRecUser[1]);
                        String userNames = roleDefines.getUserName();
                        String[] userName = userNames.split(";");
                        for(String userName1 : userName){
                            Message m1 = new Message();
                            m1.setMsgSendUser(message.getMsgSendUser());
                            m1.setMsgText(message.getMsgText());
                            m1.setMsgTitle(message.getMsgTitle());
                            m1.setMsgSendTime(DateHelper.now().toString());
                            m1.setMsgType(message.getMsgType());
                            m1.setMsgUrl(message.getMsgUrl());
                            m1.setMsgFile(message.getMsgFile());
                            m1.setMsgLevel(message.getMsgLevel());
                            m1.setMsgStatus(1);
                            m1.setRecordName(message.getMsgSendUser());
                            m1.setRecordTime(DateHelper.now());
                            m1.setModifyName(message.getMsgSendUser());
                            m1.setModifyTime(DateHelper.now());
                            PrimaryKeyUitl pk1 = new PrimaryKeyUitl();
                            String id1 = pk1.getNextId("Message","msgId");
                            m1.setMsgId(id1);
                            m1.setMsgRecUser(userName1);
                            messageMap.put(userName1,m1);
                        }
                    }
                }else if(msgRecUser[0].equals("useractor")){
                    m.setMsgRecUser(msgRecUser[1]);
                    messageMap.put(msgRecUser[1],m);
                }else if(msgRecUser[0].equals("deptactor")){
                    String[] deptId = msgRecUser[1].split("_");
                    if(deptId[1].equals("deptHead")){
                        Department department = departmentService.selectById(deptId[0]);
                        m.setMsgRecUser(department.getDeptHead());
                        messageMap.put(department.getDeptHead(),m);
                    }else if(deptId[1].equals("deputyDeptHead")){
                        Department department = departmentService.selectById(deptId[0]);
                        if(department.getDeputyDeptHead()!=null&&!department.getDeputyDeptHead().equals("")){
                            String[] userNames = department.getDeputyDeptHead().split(";");
                            for(String userName2 :userNames){
                                Message m2 = new Message();
                                m2.setMsgSendUser(message.getMsgSendUser());
                                m2.setMsgText(message.getMsgText());
                                m2.setMsgTitle(message.getMsgTitle());
                                m2.setMsgSendTime(DateHelper.now().toString());
                                m2.setMsgType(message.getMsgType());
                                m2.setMsgUrl(message.getMsgUrl());
                                m2.setMsgFile(message.getMsgFile());
                                m2.setMsgLevel(message.getMsgLevel());
                                m2.setMsgStatus(1);
                                m2.setRecordName(message.getMsgSendUser());
                                m2.setRecordTime(DateHelper.now());
                                m2.setModifyName(message.getMsgSendUser());
                                m2.setModifyTime(DateHelper.now());
                                PrimaryKeyUitl pk2 = new PrimaryKeyUitl();
                                String id2 = pk2.getNextId("Message","msgId");
                                m2.setMsgId(id2);
                                m2.setMsgRecUser(userName2);
                                messageMap.put(userName2,m2);
                            }
                        }
                    }else if(deptId[1].equals("deptAllemp")){
                        Department department = departmentService.selectById(deptId[0]);
                        m.setMsgRecUser(department.getDeptHead());
                        messageMap.put(department.getDeptHead(),m);
                        if(department.getDeputyDeptHead()!=null&&!department.getDeputyDeptHead().equals("")){
                            String[] userNames = department.getDeputyDeptHead().split(";");
                            for(String userName2 :userNames){
                                Message m2 = new Message();
                                m2.setMsgSendUser(message.getMsgSendUser());
                                m2.setMsgText(message.getMsgText());
                                m2.setMsgTitle(message.getMsgTitle());
                                m2.setMsgSendTime(DateHelper.now().toString());
                                m2.setMsgType(message.getMsgType());
                                m2.setMsgUrl(message.getMsgUrl());
                                m2.setMsgFile(message.getMsgFile());
                                m2.setMsgLevel(message.getMsgLevel());
                                m2.setMsgStatus(1);
                                m2.setRecordName(message.getMsgSendUser());
                                m2.setRecordTime(DateHelper.now());
                                m2.setModifyName(message.getMsgSendUser());
                                m2.setModifyTime(DateHelper.now());
                                PrimaryKeyUitl pk2 = new PrimaryKeyUitl();
                                String id2 = pk2.getNextId("Message","msgId");
                                m2.setMsgId(id2);
                                m2.setMsgRecUser(userName2);
                                messageMap.put(userName2,m2);
                            }
                        }
                        List<Employees> listEmployees = employeesService.selectAllDept(deptId[0]);
                        for(Employees employees : listEmployees){
                            Message m3 = new Message();
                            m3.setMsgSendUser(message.getMsgSendUser());
                            m3.setMsgText(message.getMsgText());
                            m3.setMsgTitle(message.getMsgTitle());
                            m3.setMsgSendTime(DateHelper.now().toString());
                            m3.setMsgType(message.getMsgType());
                            m3.setMsgUrl(message.getMsgUrl());
                            m3.setMsgFile(message.getMsgFile());
                            m3.setMsgLevel(message.getMsgLevel());
                            m3.setMsgStatus(1);
                            m3.setRecordName(message.getMsgSendUser());
                            m3.setRecordTime(DateHelper.now());
                            m3.setModifyName(message.getMsgSendUser());
                            m3.setModifyTime(DateHelper.now());
                            PrimaryKeyUitl pk3 = new PrimaryKeyUitl();
                            String id2 = pk3.getNextId("Message","msgId");
                            m3.setMsgId(id2);
                            m3.setMsgRecUser(employees.getUserName());
                            messageMap.put(employees.getUserName(),m3);
                        }
                    }
                }
            }
        }
        messageService.insertList(messageMap);
        Vector a_users = new Vector();
        String title = "请查看消息";
        boolean st = true;
        for(Message m0 : messageMap.values()){
            System.out.println("------"+m0.getMsgRecUser());
            if(m0.getMsgRecUser()!=null&&!m0.getMsgRecUser().equals("")){
                toHtml("mymsg",m0.getMsgRecUser(),m0.getMsgTitle());
                toEmail(m0.getMsgRecUser(),m0.getMsgTitle(),m0.getMsgText());
                a_users.add(m0.getMsgRecUser());
                if(st){
                    title = m0.getMsgTitle();
                    st = false;
                }

            }
        }
        toPhone(a_users,title);
    }
}
