package com.oa.core.service.module;

import com.oa.core.bean.module.Message;
import com.oa.core.dao.module.MessageDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * @ClassName:MessageServiceImpl
 * @author:zxd
 * @Date:2018/10/19
 * @Time:下午 5:16
 * @Version V1.0
 * @Explain
 */
@Service("messageService")
public class MessageServiceImpl implements MessageService {

    @Resource
    private MessageDao messageDao;

    @Override
    public void insert(Message message) {
        messageDao.insert(message);
    }

    @Override
    public void delete(String msgId, String deleteName, Timestamp deleteTime) {
        messageDao.delete(msgId,deleteName,deleteTime);
    }

    @Override
    public void update(Message message) {
        messageDao.update(message);
    }

    @Override
    public List<Message> selectAll() {
        return messageDao.selectAll();
    }

    @Override
    public Message selectById(String msgId) {
        return messageDao.selectById(msgId);
    }

    @Override
    public List<Message> selectBySendUser(String msgSendUser) {
        return messageDao.selectBySendUser(msgSendUser);
    }

    @Override
    public int selectBySendUserCont(String msgSendUser) {
        return messageDao.selectBySendUserCont(msgSendUser);
    }

    @Override
    public List<Message> selectByRecUser(String msgRecUser) {
        return messageDao.selectByRecUser(msgRecUser);
    }

    @Override
    public int selectByRecUserCont(String msgRecUser) {
        return messageDao.selectByRecUserCont(msgRecUser);
    }

    @Override
    public Message selectTerms(Message message) {
        return messageDao.selectTerms(message);
    }

    @Override
    public List<Message> selectAllTerms(Message message) {
        return messageDao.selectAllTerms(message);
    }

    @Override
    public int selectAllTermsCont(Message message) {
        return messageDao.selectAllTermsCont(message);
    }

    @Override
    public List<Map<String, Object>> selectAllMsgCont() {
        return messageDao.selectAllMsgCont();
    }

    @Override
    public void deleteMessage(String msgId, String deleteName, Timestamp deleteTime) {
        messageDao.deleteMessage(msgId,deleteName,deleteTime);
    }

    @Override
    public void insertList(Map<String,Message> messageMap){
        messageDao.insertList(messageMap);
    }
}
