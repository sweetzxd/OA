package com.easemob.server.example.util;

import com.easemob.server.example.api.impl.EasemobChatGroup;
import org.json.JSONObject;

/**
 * @ClassName:GroupUtil
 * @author:zxd
 * @Date:2019/04/22
 * @Time:下午 2:02
 * @Version V1.0
 * @Explain
 */
public class GroupUtil {
    private static EasemobChatGroup easemobChatGroup = new EasemobChatGroup();

    public static JSONObject getChatGroups() {
        Object result = easemobChatGroup.getChatGroups(50L, "");
        JSONObject json = new JSONObject(result);
        return json;
    }
}
