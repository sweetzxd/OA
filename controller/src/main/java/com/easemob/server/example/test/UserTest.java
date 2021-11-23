package com.easemob.server.example.test;

import com.easemob.server.example.api.impl.EasemobIMUsers;
import io.swagger.client.model.NewPassword;
import io.swagger.client.model.RegisterUsers;
import io.swagger.client.model.User;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

/**
 * Created by easemob on 2017/3/21.
 */
public class UserTest {

    private static final Logger logger = LoggerFactory.getLogger(UserTest.class);
    private EasemobIMUsers easemobIMUsers = new EasemobIMUsers();

    @Test
    public void createUser() {
        RegisterUsers users = new RegisterUsers();
        User user = new User().username("aaaa123456" + new Random().nextInt(500)).password("123456");
        User user1 = new User().username("aaa123456" + new Random().nextInt(500)).password("123456");
        users.add(user);
        users.add(user1);
        Object result = easemobIMUsers.createNewIMUserSingle(users);
        logger.info(result.toString());
        Assert.assertNotNull(result);
    }

    @Test
    public void getUserByName() {
        String userName = "stringa";
        Object result = easemobIMUsers.getIMUserByUserName(userName);
        logger.info(result.toString());
    }

    @Test
    public void gerUsers() {
        Object result = easemobIMUsers.getIMUsersBatch(null, null);
        logger.info(result.toString());
    }

    @Test
    public void changePassword() {
        String userName = "stringa";
        NewPassword psd = new NewPassword().newpassword("123");
        Object result = easemobIMUsers.modifyIMUserPasswordWithAdminToken(userName, psd);
        logger.info(result.toString());
    }

    @Test
    public void getFriend() {
        String userName = "zhangxu";
        Object result = easemobIMUsers.getFriends(userName);
        logger.info(result.toString());
    }

    /**
    * @method: getStatus
    * @param:
    * @return:
    * @author: zxd
    * @date: 2019/04/20
    * @description: 查询用户在线状态
    */
    @Test
    public void getStatus(){
        String userName = "zhangxu";
        Object result = easemobIMUsers.getIMUserStatus(userName);
        logger.info(result.toString());
    }
}
