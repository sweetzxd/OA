package com.easemob.server.example.util;

import com.easemob.server.example.api.impl.EasemobIMUsers;
import com.easemob.server.example.comm.TokenUtil;
import io.swagger.client.model.RegisterUsers;
import io.swagger.client.model.User;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

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

    public static void createUser(User user) {
        try {
            Object result = easemobIMUsers.createNewIMUserSingle(user);
            logger.info(result.toString());
            Assert.assertNotNull(result);
        }catch (Exception e){
            e.getLocalizedMessage();

        }
    }

    public static void createUser(RegisterUsers users) {
        try {
            Object result = easemobIMUsers.createNewIMUserSingle(users);
            logger.info(result.toString());
            Assert.assertNotNull(result);
        }catch (Exception e){
            e.getLocalizedMessage();

        }
    }

    public static void createUser(JSONObject user) {
        try {
            Object result = easemobIMUsers.createNewIMUserSingle(user);
            logger.info(result.toString());
            Assert.assertNotNull(result);
        }catch (Exception e){
            e.getLocalizedMessage();

        }
    }

    public static void createUsers(JSONArray users) {
        try {
            Object result = easemobIMUsers.createNewIMUserSingle(users);
            logger.info(result.toString());
            Assert.assertNotNull(result);
        }catch (Exception e){
            e.getLocalizedMessage();

        }
    }

    public static void getUserByName(String userName) {
        Object result = easemobIMUsers.getIMUserByUserName(userName);
        logger.info(result.toString());
    }

    @Test
    public void test(){
        TokenUtil.initTokenByProp();
        RegisterUsers users = new RegisterUsers();
        User user = new User().username("zhenxudong").password("zjy123");
        users.add(user);
        createUser(users);
    }
}
