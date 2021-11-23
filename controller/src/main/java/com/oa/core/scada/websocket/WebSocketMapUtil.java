package com.oa.core.scada.websocket;

/**
 * @ClassName:WebSocketMapUtil
 * @author:zxd
 * @Date:2019/06/15
 * @Time:下午 3:30
 * @Version V1.0
 * @Explain
 */

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class WebSocketMapUtil {

    public static ConcurrentMap<String, MyWebSocketServer> webSocketMap = new ConcurrentHashMap<>();

    public static void put(String key, MyWebSocketServer myWebSocketServer) {
        webSocketMap.put(key, myWebSocketServer);
    }

    public static MyWebSocketServer get(String key) {
        return webSocketMap.get(key);
    }

    public static void remove(String key) {
        webSocketMap.remove(key);
    }

    public static Collection<MyWebSocketServer> getValues() {
        return webSocketMap.values();
    }
}