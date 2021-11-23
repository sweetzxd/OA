package com.oa.core.example;

/**
 * @ClassName:JPushAllService
 * @author:zxd
 * @Date:2019/04/10
 * @Time:下午 5:19
 * @Version V1.0
 * @Explain 手机消息推送类
 */

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
import com.google.gson.JsonObject;
import com.oa.core.util.ConfParseUtil;
import org.json.JSONObject;
import org.junit.Test;

import java.util.*;

/**
 * @param: type:1.普通推送，2.跳转页面，3.跳转原生页面
 * @author: zxd
 * @date: 2019/04/23
 * @description: 方法说明
 */
public class JPushAllService {
    private static String key = "bb007199fece9fd9d0316a45";
    private static String secret = "0355577ba2145a6dd10d55de";

    public static void jpush(String user, String msg, String url) {
        Vector<String> users = new Vector<>();
        users.add(user);
        jpush(users, msg, url);
    }

    /**
     * @method:
     * @param: user 推送账号
     * @param: msg 消息标题
     * @param: url 点击跳转路径
     * @return:
     * @author: zxd
     * @date: 2019/04/23
     * @description: 方法说明
     */
    public static void jpush(Vector<String> user, String msg, String url) {
        JsonObject json = new JsonObject();
        if (url != null) {
            json.addProperty("type", 2);
            json.addProperty("url", url);
        } else {
            json.addProperty("type", 1);
        }
        jpushAll(user, msg, json);
    }
    public static void jpushAll(Vector<String> user, String msg, JsonObject json) {
        ConfParseUtil cpu = new ConfParseUtil();
        boolean srvicemsg = true;
        try {
            JPushClient jpushClient = new JPushClient(secret, key);
            PushPayload payload = PushPayload.newBuilder()
                    .setPlatform(Platform.all())
                    .setAudience(Audience.alias(user))
                    .setNotification(Notification.newBuilder()
                            .addPlatformNotification(AndroidNotification.newBuilder()
                                    .setAlert(msg)
                                    .addExtra("param", json)
                                    .build())
                            .addPlatformNotification(IosNotification.newBuilder()
                                    .setAlert(msg)
                                    .setBadge(+1)
                                    .setSound("happy")
                                    .addExtra("param", json)
                                    .build())
                            .build())
                    .setOptions(Options.newBuilder().setApnsProduction(srvicemsg).build())
                    .setMessage(Message.content(msg))
                    .build();

            PushResult pu = jpushClient.sendPush(payload);
            System.out.println(pu);
        } catch (APIConnectionException e) {
            e.printStackTrace();
        } catch (APIRequestException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test(){
        Vector<String> l = new Vector<>();
        l.add("yangning");
        l.add("zhenxudong");
        jpush(l,"11111111",null);
        //jpush("yangning","11111111111111",null);
    }
}
