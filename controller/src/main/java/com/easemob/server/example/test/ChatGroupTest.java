package com.easemob.server.example.test;

import com.easemob.server.example.api.impl.EasemobChatGroup;
import com.easemob.server.example.comm.TokenUtil;
import com.oa.core.helper.StringHelper;
import io.swagger.client.model.*;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Vector;

/**
 * Created by easemob on 2017/3/22.
 */
public class ChatGroupTest {
    private EasemobChatGroup easemobChatGroup = new EasemobChatGroup();
    private static final Logger logger = LoggerFactory.getLogger(ChatGroupTest.class);

    @Test
    public void getChatGroups() {
        Long limit = 5L;
        String cursor = "";
        Object result = easemobChatGroup.getChatGroups(limit, cursor);
        logger.info(result.toString());
    }

    @Test
    public void getGroupsInfo() {
        String[] grousIds = new String[1];
        grousIds[0] = "79874765160449";
        Object result = easemobChatGroup.getChatGroupDetails(grousIds);
        logger.info(result.toString());
    }

    @Test
    public void createGroup() {
        TokenUtil.initTokenByProp();
        Group group = new Group();
        group.groupname("groupA").desc("a new group")._public(true).maxusers(50).approval(false).owner("admin");
        Object result = easemobChatGroup.createChatGroup(group);
        logger.info(result.toString());
    }

    @Test
    public void changeGroupInfo() {
        ModifyGroup group = new ModifyGroup();
        String groupId = "11361107116036";
        group.description("change group info").groupname("changed group").maxusers(300);
        Object result = easemobChatGroup.modifyChatGroup(groupId, group);
        logger.info(result.toString());
    }

    @Test
    public void addUsers() {
        String groupId = "80234273636353";
        String users = "zhangmeng;zhouyang;zhangxu;niulitong;yanxiaokun;zhangmeng;wangchao;chenlongbo;liyanze;wangpengsen;wangshixiong;";

        Vector<String> u = StringHelper.string2Vector(users, ";");
        for(int i=0,len=u.size();i<len;i++){
            UserNames userNames = new UserNames();
            UserName userList = new UserName();
            userList.add(u.get(i));
            userNames.usernames(userList);
            Object result = easemobChatGroup.addBatchUsersToChatGroup(groupId, userNames);
        }


    }

    @Test
    public void removeUsersFromGroup() {
        String groupId = "11361107116036";
        String[] userIds = new String[2];
        userIds[0] = "qwqwqw";
        userIds[1] = "qwqwqww";
        Object result = easemobChatGroup.removeBatchUsersFromChatGroup(groupId, userIds);
        logger.info(result.toString());
    }

    @Test
    public void getUsersFromGroup() {
        String groupId = "79874765160449";
        Object result = easemobChatGroup.getChatGroupUsers(groupId);
        logger.info(result.toString());
    }

    @Test
    public void transferGroupOwner() {
        String groupId = "11361107116036";
        NewOwner newOwner = new NewOwner();
        newOwner.newowner("qwqwqww");
        Object result = easemobChatGroup.transferChatGroupOwner(groupId, newOwner);
        logger.info(result.toString());
    }

    @Test
    public void addBlockUsers() {
        String groupId = "11361107116036";
        UserNames userNames = new UserNames();
        UserName userList = new UserName();
        userList.add("qwqwqw");
        userList.add("qwqwqww");
        userNames.usernames(userList);
        Object result = easemobChatGroup.addBatchBlockUsersToChatGroup(groupId, userNames);
        logger.info(result.toString());
    }

    @Test
    public void removeBlockUser() {
        String groupId = "11361107116036";
        String[] userIds = new String[2];
        userIds[0] = "qwqwqw";
        userIds[1] = "qwqwqww";
        Object result = easemobChatGroup.removeBatchBlockUsersFromChatGroup(groupId, userIds);
        logger.info(result.toString());
    }
}
