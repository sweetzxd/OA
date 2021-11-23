package com.oa.core.listener;

import com.oa.core.scada.common.Common;
import com.oa.core.scada.system.LoginAuth;
import com.oa.core.scada.util.URIEncoder;
import com.oa.core.scada.websocket.MyWebSocketClient;
import com.oa.core.service.ListenerService;
import com.oa.core.service.util.TableService;
import com.oa.core.util.LogUtil;
import com.oa.core.util.SpringContextUtil;
import org.apache.http.client.CookieStore;
import org.java_websocket.WebSocket;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName:WebSocketListener
 * @author:zxd
 * @Date:2019/07/20
 * @Time:上午 11:56
 * @Version V1.0
 * @Explain 监控服务的websocket
 */
public class WebSocketListener implements InitializingBean, ServletContextAware {
    private static Map<Object, Object> idtoval = new HashMap<>();
    public static CookieStore cookieStore;

    public WebSocketListener() {

    }

    public WebSocketListener(String key) {
        try {
            switch (key) {
                case "attmap":
                    attmap();
                    break;
                default:
            }
        } catch (Exception e) {
            LogUtil.sysLog(">系统初始化出错:" + key);
            LogUtil.sysLog(e);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        LogUtil.sysLog(">监控接口初始化开始");
        cookieStore = LoginAuth.authLogin(Common.SYSTEM + Common.SYSTEM_URL + Common.LOGIN, Common.SYSTEM_USERNAME, Common.SYSTEM_PASSWORD);

        /*new Thread(new Runnable() {
            @Override
            public void run() {
                websocket();
            }
        }).start();*/

    }

    @Override
    public void setServletContext(ServletContext servletContext) {

    }

    public void websocket() {
        System.out.println("建立websocket连接开始");
        MyWebSocketClient.clintsocket();
    }

    public static String getData(String key) {
        if (idtoval.get(key) == null) {
            new WebSocketListener("attmap");
        }
        return (String) idtoval.get(key);
    }

    public void attmap() {
        TableService tableService = (TableService) SpringContextUtil.getBean("tableService");
        List<Map<String, Object>> maps = tableService.selectSqlMapList("select gh19062100001,xm19062100001 from afxtx19062101 where curStatus=2 and gh19062100001!=0 and xm19062100001!='' and xm19062100001 is not null");
        for (int i = 0, len = maps.size(); i < len; i++) {
            Map<String, Object> map = maps.get(i);
            idtoval.put(map.get("gh19062100001"), map.get("xm19062100001"));
        }
    }
}
