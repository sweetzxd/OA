package com.oa.core.socket;

/**
 * @ClassName:WebsocketEndPoint
 * @author:zxd
 * @Date:2018/10/19
 * @Time:上午 11:30
 * @Version V1.0
 * @Explain
 */

import com.oa.core.util.LogUtil;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class WebsocketEndPoint extends TextWebSocketHandler {

    /**
     * 在线用户列表
     */
    private static final Map<String, WebSocketSession> users;
    /**
     * 用户标识
     */
    public static final String CLIENT_ID = "userId";

    static {
        users = new HashMap<>();
    }


    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        LogUtil.sysLog("成功建立连接");
        String userId = getClientId(session);
        if (userId != null) {
            users.put(userId, session);
            session.sendMessage(new TextMessage("你已成功建立socket连接"));
            LogUtil.sysLog(userId);
            LogUtil.sysLog(session);
            setOnlineNum();
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        super.handleTextMessage(session, message);
        LogUtil.sysLog("收到客户端消息:" + message.getPayload());
        WebSocketMessage message1 = new TextMessage("server:" + message);
        try {
            session.sendMessage(message1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        if (session.isOpen()) {
            session.close();
        }
        LogUtil.sysLog("连接出错");
        users.remove(getClientId(session));
        setOnlineNum();
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        LogUtil.sysLog("连接已关闭：" + status);
        users.remove(getClientId(session));
        setOnlineNum();
    }

    /**
     * 发送信息给指定用户
     *
     * @param clientId
     * @param message
     * @return
     */
    public boolean sendMessageToUser(String clientId, TextMessage message) {
        if (users.get(clientId) == null) {
            return false;
        }
        WebSocketSession session = users.get(clientId);
        LogUtil.sysLog("sendMessage:" + session + ",msg:" + message.getPayload());
        if (!session.isOpen()) {
            return false;
        }
        try {
            session.sendMessage(message);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 广播信息
     *
     * @param message
     * @return
     */
    public boolean sendMessageToAllUsers(TextMessage message) {
        boolean allSendSuccess = true;
        Set<String> clientIds = users.keySet();
        WebSocketSession session = null;
        for (String clientId : clientIds) {
            try {
                session = users.get(clientId);
                if (session.isOpen()) {
                    session.sendMessage(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
                allSendSuccess = false;
            }
        }

        return allSendSuccess;
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    /**
     * 获取用户标识
     *
     * @param session
     * @return
     */
    private String getClientId(WebSocketSession session) {
        try {
            String clientId = (String) session.getAttributes().get(CLIENT_ID);
            return clientId;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 更新登录人数
     *
     * @param
     * @return
     */
    public void setOnlineNum(){
        Set<String> clientIds = users.keySet();
        for (String clientId : clientIds) {
            sendMessageToUser(clientId, new TextMessage("{\"type\":\"online\",\"value\":" + clientIds.size() + "}"));
        }
    }
}
