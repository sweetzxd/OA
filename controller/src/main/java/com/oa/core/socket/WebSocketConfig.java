package com.oa.core.socket;

/**
 * @ClassName:WebSocketConfig
 * @author:zxd
 * @Date:2018/10/19
 * @Time:下午 1:37
 * @Version V1.0
 * @Explain
 */

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;


@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(myHandler(), "/websocket.do").addInterceptors(new HandshakeInterceptor());
    }

    @Bean
    public WebSocketHandler myHandler() {
        return new WebsocketEndPoint();
    }
}
