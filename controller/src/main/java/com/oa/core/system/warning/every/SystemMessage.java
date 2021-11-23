package com.oa.core.system.warning.every;

import com.oa.core.service.module.MessageService;
import com.oa.core.socket.WebsocketEndPoint;
import com.oa.core.system.warning.WarningSystem;
import com.oa.core.util.ConfParseUtil;
import com.oa.core.util.LogUtil;
import com.oa.core.util.SpringContextUtil;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.web.socket.TextMessage;

import java.util.List;
import java.util.Map;


/**
 * @ClassName:SystemMessage
 * @author:zxd
 * @Date:2018/10/19
 * @Time:下午 3:46
 * @Version V1.0
 * @Explain
 */
public class SystemMessage implements Job {

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        LogUtil.sysLog("开始执行消息检查");
        WebsocketEndPoint w = new WebsocketEndPoint();
        MessageService messageService = (MessageService) SpringContextUtil.getBean("messageService");
        List<Map<String, Object>> msgList = messageService.selectAllMsgCont();
        for (Map<String, Object> msgVal : msgList) {
            String msgRecUser = (String) msgVal.get("msgRecUser");
            LogUtil.toHtml(msgRecUser, msgVal.get("num"));
        }
        w.setOnlineNum();
        LogUtil.sysLog("消息检查结束");
    }

    public void close(){
        ConfParseUtil cp = new ConfParseUtil();
        String type = cp.getPoa("open_message");
        if (type != null && type.equals("false")) {
            WarningSystem.removeJob("systemMessage","jobGroup-SystemMessage","trigger-SystemMessage","triggerGroup-SystemMessage");
        }
    }

}



