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
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @param: type:1.普通推送，2.跳转页面，3.跳转原生页面
 * @author: zxd
 * @date: 2019/04/23
 * @description: 方法说明
 */
public class JPushAllService {

    public static void jpushAll(String user, String msg, String url) {
        ArrayList<String> users = new ArrayList<>();
        users.add(user);
        jpushAll(users, msg, url);
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
    public static void jpushAll(ArrayList<String> user, String msg, String url) {
        JsonObject json = new JsonObject();
        if(url!=null) {
            json.addProperty("type", 2);
            json.addProperty("url", url);
        }else{
            json.addProperty("type", 1);
        }
        jpushAll(user,msg,json);
    }

    public static void jpushAll(ArrayList<String> user, String msg, JsonObject json) {
        jpushIOS(user, msg, json);
        jpushAndroid(user, msg, json);
    }

    public static void jpushAndroid(ArrayList<String> user, String msg, JsonObject json) {
        String appKey = "ad52daffa166185c010ad493";
        String masterSecret = "835e785a1760b508a2817a91";
        JPushClient jpushClient = new JPushClient(masterSecret, appKey);
        PushPayload payload = PushPayload.newBuilder()
                .setPlatform(Platform.android())
                .setAudience(Audience.alias(user))
                .setNotification(Notification.newBuilder()
                        .addPlatformNotification(AndroidNotification.newBuilder()
                                .setAlert(msg)
                                .addExtra("param", json)
                                .build())
                        .build())
                .setOptions(Options.newBuilder().setApnsProduction(false).build())
                .setMessage(Message.content(msg))
                .build();
        try {
            PushResult pu = jpushClient.sendPush(payload);
        } catch (APIConnectionException e) {
            e.printStackTrace();
        } catch (APIRequestException e) {
            e.printStackTrace();
        }
    }

    public static void jpushIOS(ArrayList<String> user, String msg, JsonObject json) {
        String appKey = "ad52daffa166185c010ad493";
        String masterSecret = "835e785a1760b508a2817a91";
        JPushClient jpushClient = new JPushClient(masterSecret, appKey);
        PushPayload payload = PushPayload.newBuilder()
                .setPlatform(Platform.ios())
                .setAudience(Audience.alias(user))
                .setNotification(Notification.newBuilder()
                        .addPlatformNotification(IosNotification.newBuilder()
                                .setAlert(msg)
                                .setBadge(+1)
                                .setSound("happy")
                                .addExtra("param", json)
                                .build())
                        .build())
                .setOptions(Options.newBuilder().setApnsProduction(false).build())
                .setMessage(Message.content(msg))
                .build();

        try {
            PushResult pu = jpushClient.sendPush(payload);
        } catch (APIConnectionException e) {
            e.printStackTrace();
        } catch (APIRequestException e) {
            e.printStackTrace();
        }
    }
}
