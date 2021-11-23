package com.oa.core.socket;

/**
 * @ClassName:HandshakeInterceptor
 * @author:zxd
 * @Date:2018/10/19
 * @Time:上午 11:30
 * @Version V1.0
 * @Explain
 */

import java.util.HashMap;
import java.util.Map;

import com.oa.core.bean.Loginer;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import javax.servlet.http.HttpSession;

public class HandshakeInterceptor extends HttpSessionHandshakeInterceptor{

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        if (request instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest serverHttpRequest = (ServletServerHttpRequest) request;
            HttpSession session = serverHttpRequest.getServletRequest().getSession();
            if (session != null) {
                Loginer loginer = (Loginer) session.getAttribute("loginer");
                if(attributes==null){
                    attributes = new HashMap<>();
                }
                String uid = "admin";
                if(loginer!=null){
                    uid = loginer.getId();
                }
                attributes.put(WebsocketEndPoint.CLIENT_ID, uid);
            }
        }
        return super.beforeHandshake(request, response, wsHandler, attributes);
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception ex) {
        super.afterHandshake(request, response, wsHandler, ex);
    }
}

