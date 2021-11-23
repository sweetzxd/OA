package com.oa.core.controller.module;

import com.oa.core.bean.Loginer;
import com.oa.core.bean.module.Message;
import com.oa.core.bean.util.PageUtil;
import com.oa.core.helper.DateHelper;
import com.oa.core.service.module.DepartmentService;
import com.oa.core.service.module.EmployeesService;
import com.oa.core.service.module.MessageService;
import com.oa.core.service.system.RoleDefinesService;
import com.oa.core.service.user.UserManagerService;
import com.oa.core.socket.WebsocketEndPoint;
import com.oa.core.util.ToNameUtil;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName:MessageController
 * @author:zxd
 * @Date:2018/10/19
 * @Time:下午 3:31
 * @Version V1.0
 * @Explain
 */

@Controller
@RequestMapping("/weixin/message")
public class WxMessageController {
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


    /**
     * 获取所有消息
     * @return String
     */
    @RequestMapping(value = "/selectAllMessage", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String selectAllMessage(HttpServletRequest request,String inputval, String option, String msgStatus, int page, int limit){
        PageUtil pu = new PageUtil();
        pu.setPageSize(limit);
        pu.setCurrentPage(page);
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        String userId = null;
        if(loginer == null){
            userId = request.getParameter("userid");
        }else{
            userId = loginer.getId();
        }
        Message m = new Message();
        m.setMsgRecUser(userId);
        if(msgStatus==null||msgStatus.equals("")){
            m.setMsgStatus(1);
        }else{
            m.setMsgStatus(Integer.parseInt(msgStatus));
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
        jsonObject.put("count", count);
        jsonObject.put("data", messageList);
        jsonObject.put("msg", "");
        jsonObject.put("success",1);
        return jsonObject.toString();
    }

    /**
     * 查看消息（并修改已读状态）
     * @param id 消息id
     * @return
     */
    @RequestMapping("/seeMessage")
    @ResponseBody
    public String seeMessage(HttpServletRequest request,String id){
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        String userId = "";
        Message message = null;
        JSONObject jsonObject = new JSONObject();
        if (loginer != null) {
            userId = loginer.getId();
            message = messageService.selectById(id);
            message.setModifyName(userId);
            message.setModifyTime(DateHelper.now());
            message.setMsgStatus(2);
        }
        try {
            messageService.update(message);
            List<Message> list = new ArrayList<>();
            list.add(message);
            jsonObject.put("success",1);
            jsonObject.put("data",list);
            jsonObject.put("msg", "");
            return jsonObject.toString();
        }catch (Exception e){
            e.printStackTrace();
            jsonObject.put("success",0);
            jsonObject.put("msg", "系统查询错误");
            return jsonObject.toString();
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
        if (loginer != null) {
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

