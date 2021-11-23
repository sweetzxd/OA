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
        if (users != null && !users.equals("")) {
            try {
                Vector<String> strings = StringHelper.string2Vector(users, ";");
                if(strings!=null && strings.size()>0) {
                    JPushAllService.jpush(strings, msg, url);
                }
            }catch (Exception e){
                e.getLocalizedMessage();
                System.out.println(e.getLocalizedMessage());
            }
        }
        return "1";
    }

    @Test
    public void test() {
        String url = "/task/tasksendpage.do?procId=FlowTask2019041400004&wkflwId=tysplc2018110700002&nodeId=txspnr2018110700001&workOrderNO=ZA2019041400007";
        addArticle("yangning", "请处理管理员发起的流程审批！",url);
    }
}
