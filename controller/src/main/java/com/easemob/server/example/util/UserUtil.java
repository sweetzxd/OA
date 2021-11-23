package com.easemob.server.example.util;

import com.easemob.server.example.api.impl.EasemobIMUsers;
import org.json.JSONArray;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName:UserUtil
 * @author:zxd
 * @Date:2019/04/19
 * @Time:上午 10:48
 * @Version V1.0
 * @Explain
 */
public class UserUtil {

    private static final Logger logger = LoggerFactory.getLogger(UserUtil.class);
    private static EasemobIMUsers easemobIMUsers = new EasemobIMUsers();


    public static void createUser(JSONArray users) {
        try {
            Object result = easemobIMUsers.createNewIMUserSingle(users);
            logger.info(result.toString());
            Assert.assertNotNull(result);
        }catch (Exception e){
            e.getLocalizedMessage();
            logger.info( e.getLocalizedMessage());
        }
    }

    public static void getUserByName(String userName) {
        Object result = easemobIMUsers.getIMUserByUserName(userName);
        logger.info(result.toString());
    }

    @Test
    public void test(){

    }
}
