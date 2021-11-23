package com.oa.core.controller.module;

import com.oa.core.bean.Loginer;
import com.oa.core.bean.module.Department;
import com.oa.core.bean.module.Employees;
import com.oa.core.bean.module.Message;
import com.oa.core.bean.system.RoleDefines;
import com.oa.core.bean.user.UserManager;
import com.oa.core.bean.util.PageUtil;
import com.oa.core.helper.DateHelper;
import com.oa.core.interceptor.Logined;
import com.oa.core.service.module.DepartmentService;
import com.oa.core.service.module.EmployeesService;
import com.oa.core.service.module.MessageService;
import com.oa.core.service.system.RoleDefinesService;
import com.oa.core.service.user.UserManagerService;
import com.oa.core.socket.WebsocketEndPoint;
import com.oa.core.util.LogUtil;
import com.oa.core.util.PrimaryKeyUitl;
import com.oa.core.util.ToNameUtil;
import org.json.JSONObject;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.socket.TextMessage;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName:MessageController
 * @author:zxd
 * @Date:2018/10/19
 * @Time:下午 3:31
 * @Version V1.0
 * @Explain
 */

@Controller
@RequestMapping("/message")
public class MessageController {
    @Autowired
    WebsocketEndPoint websocketEndPoint;
    @Autowired
    MessageService messageService;
    @Autowired
    RoleDefinesService roleDefinesService;
    @Autowired
    DepartmentService departmentService;
    @Autowired
    EmployeesService employeesService;
    @Autowired
    UserManagerService userManagerService;

    @RequestMapping("/external")
    @ResponseBody
    public String sendMessage(HttpServletRequest request, String userId, String message) {
        boolean hasSend = websocketEndPoint.sendMessageToUser(userId, new TextMessage(message));
        return "success";
    }

    @Logined
    @RequestMapping(value = "/getmymsg", method = RequestMethod.POST, produces = {"text/html;charset=UTF-8;", "application/json;"})
    @ResponseBody
    public String getMyMsg(HttpServletRequest request, String userId) {
        try {
            Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
            Message m = new Message();
            m.setMsgRecUser(loginer.getId());
            m.setMsgStatus(1);
            List<Message> messageList = messageService.selectAllTerms(m);
            if(messageList.size()>5){
                messageList = messageList.subList(0,5);
            }
            String html = "";
            for (int i = 0; i < messageList.size(); i++) {
                Message message = messageList.get(i);
                String msgTitle = message.getMsgTitle();
                String msgSendUser = ToNameUtil.getName("user", message.getMsgSendUser());
                String msgSendTime = ToNameUtil.getName("date", message.getMsgSendTime());
                String msgId = message.getMsgId();
                String msgType = message.getMsgType();
                String msgUrl = message.getMsgUrl();
                int msgLevel = message.getMsgLevel();
                html += "<div class=\"my-message\" onclick=\"checkmymsg('" + msgId + "'," + msgType + ",'"+msgUrl+"')\">\n " +
                        "<div class=\"my-message-msgtitle\">\n " +
                        "<span>";
                switch (msgLevel) {
                    case 1:
                        html += "<i class=\"layui-icon layui-icon-notice\" style=\"font-size: 15px; color: blue;\"></i>  ";
                        break;
                    case 2:
                        html += "<i class=\"layui-icon layui-icon-notice\" style=\"font-size: 15px; color: yellow;\"></i>  ";
                        break;
                    case 3:
                        html += "<i class=\"layui-icon layui-icon-notice\" style=\"font-size: 15px; color: red;\"></i>  ";
                        break;
                    default:
                        html += "<i class=\"layui-icon layui-icon-notice\" style=\"font-size: 15px; color: white;\"></i>  ";
                        break;
                }

                html += " " + msgTitle + "</span>\n" +
                        "</div> \n" +
                        "<div class=\"my-message-msgvalue\">\n " +
                        "<div class=\"my-message-msguser\"> " + msgSendUser + " </div> \n" +
                        "<div class=\"my-message-msgtime\"> " + msgSendTime + "</div>\n" +
                        "</div>\n" +
                        "</div>\n";
            }
            return html;
        } catch (Exception e) {
            return "";
        }
    }

    //查看消息详情
    @RequestMapping("/seeMessageInfo")
    public ModelAndView seeMessageInfo(String msgId){
        ModelAndView mav = new ModelAndView("module/messageInfo");
        Message m = messageService.selectById(msgId);
        String msgSendUserName = ToNameUtil.getName("user", m.getMsgSendUser());
        String msgSendTimeStr = ToNameUtil.getName("datetime", m.getMsgSendTime());
        List<Map<String,Object>> fileLsit = new ArrayList<>();
        if(m.getMsgFile()!=null&&!m.getMsgFile().equals("")){
            String[] msgFiles = m.getMsgFile().split("\\|");
            for(String file : msgFiles){
                Map<String,Object> map = new HashMap<>();
                String[] fileName = file.split("-");
                map.put("fileName",fileName[1]);
                map.put("file",file);
                fileLsit.add(map);
            }
        }
        mav.addObject("message",m);
        mav.addObject("msgSendUserName",msgSendUserName);
        mav.addObject("msgSendTimeStr",msgSendTimeStr);
        mav.addObject("fileLsit",fileLsit);
        return mav;
    }

    //查看消息列表页
    @RequestMapping(value = "/seeMessagepage", method = RequestMethod.GET)
    public ModelAndView seeMessagepage(HttpServletRequest request){
        ModelAndView mav = new ModelAndView("module/messagepage");
        mav.addObject("title", "我的消息");
        mav.addObject("type", "list");
        return mav;
    }

    /**
     * 获取所有消息
     * @return String
     */
    @RequestMapping("/selectAllMessage")
    @ResponseBody
    public String selectAllMessage(HttpServletRequest request,String inputval, String option, String msgStatus, int page, int limit){
        PageUtil pu = new PageUtil();
        pu.setPageSize(limit);
        pu.setCurrentPage(page);
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        Message m = new Message();
        m.setMsgSendUser(loginer.getId());
        if(msgStatus==null||msgStatus.equals("")){
            m.setMsgStatus(1);
        } else{
            m.setMsgStatus(Integer.parseInt(msgStatus));
        }
        if(loginer.getId().equals(m.getMsgSendUser())){
            m.setMsgStatus(0);
        }
        m.setStartRow(pu.getStartRow());
        m.setEndRow(pu.getEndRow()-pu.getStartRow());
        if ("msgTitle".equals(option)) {
            m.setMsgTitle(inputval);
        }
        List<Message> messageList = messageService.selectAllTerms(m);
        int count = messageService.selectAllTermsCont(m);
        for (Message message: messageList) {
            String msgSendUserName = ToNameUtil.getName("user", message.getMsgSendUser());
            message.setMsgSendUserName(msgSendUserName);
            String msgSendTimeStr = ToNameUtil.getName("datetime", message.getMsgSendTime());
            message.setMsgSendTimeStr(msgSendTimeStr);
            message.setMsgText("<a class=\"layui-btn layui-btn-xs\" href=javascript:checkmymsg('"+message.getMsgId()+"',"+message.getMsgType()+",'"+message.getMsgUrl()+"')>查看</a>");
            if(message.getMsgLevel()==0){
                message.setMsgLevelStr("正常");
            }else if(message.getMsgLevel()==1){
                message.setMsgLevelStr("急");
            }else if(message.getMsgLevel()==2){
                message.setMsgLevelStr("紧急");
            }else if(message.getMsgLevel()==3){
                message.setMsgLevelStr("特急");
            }
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 0);
        jsonObject.put("msg", "");
        jsonObject.put("count", count);
        jsonObject.put("data", messageList);
        jsonObject.put("success",1);
        return jsonObject.toString();
    }

    /**
     * 删除消息
     * @param msgId 消息ID
     * @return
     */
    @RequestMapping("/deletemessage")
    @ResponseBody
    public String deletemessage(HttpServletRequest request,String msgId){
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        Message message = new Message();
        String userId = "";
        if (loginer != null) {
            userId = loginer.getId();
            message.setDeleteName(userId);
        }
        try {
            msgId = msgId.replaceAll(";","','");
            msgId = "'"+msgId.substring(0,msgId.length()-2);
            messageService.deleteMessage(msgId,userId,DateHelper.now());
            return "1";
        }catch (Exception e){
            e.printStackTrace();
            return "0";
        }
    }

    /**
     * 发送消息页面
     * @return String
     */
    @RequestMapping("/messageInsert")
    public ModelAndView messageInsert(HttpServletRequest request){
        ModelAndView mav = new ModelAndView("module/messagepage");
        mav.addObject("title", "发送消息");
        mav.addObject("type", "send");
        return mav;
    }

    /**
     * 发送消息
     * @param message 消息
     * @return
     */
    @RequestMapping("/sendMessage")
    @ResponseBody
    public String sendMessage(HttpServletRequest request,Message message){
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        String userId = "";
        if (loginer != null) {
            userId = loginer.getId();
            message.setMsgSendUser(userId);
        }
        try {
            LogUtil.sendMessage(message);
            return "1";
        }catch (Exception e){
            e.printStackTrace();
            return "0";
        }
    }

    /**
     * 修改消息（目前只修改为已读状态）
     * @param message 消息实体
     * @return
     */
    @RequestMapping("/updateMessage")
    @ResponseBody
    public String updateMessage(HttpServletRequest request,Message message){
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        String userId = "";
       String msgsend= message.getMsgSendUser();
        String logname=loginer.getName();
        if (loginer != null ) {
            userId = loginer.getId();
            message.setModifyName(userId);
            message.setModifyTime(DateHelper.now());
        }
        try {
            messageService.update(message);
            return "1";
        }catch (Exception e){
            e.printStackTrace();
            return "0";
        }
    }

}

