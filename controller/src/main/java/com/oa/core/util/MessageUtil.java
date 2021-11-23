package com.oa.core.util;

import com.oa.core.example.JPushAllService;
import com.oa.core.helper.StringHelper;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Vector;

/**
 * @ClassName:MessageUtil
 * @author:zxd
 * @Date:2019/04/11
 * @Time:下午 4:17
 * @Version V1.0
 * @Explain 消息推送工具类
 */
public class MessageUtil {

    public static String addArticle(String users, String msg,String url) {
        ArrayList<String> list = new ArrayList<String>(0);
        Vector<String> vuser = StringHelper.string2Vector(users, ";");
        if (users != null && !users.equals("")) {
            for (int i = 0, len = vuser.size(); i < len; i++) {
                list.add(vuser.get(i));
            }
        }
        try {
            JPushAllService.jpushAll(list, msg, url);
        }catch (Exception e){
            e.getLocalizedMessage();
        }
        return "1";
    }

    @Test
    public void test() {
        String url = "/task/tasksendpage.do?procId=FlowTask2019041400004&wkflwId=tysplc2018110700002&nodeId=txspnr2018110700001&workOrderNO=ZA2019041400007";
        addArticle("zhenxudong;", "请处理管理员发起的流程审批！",url);
    }
}
