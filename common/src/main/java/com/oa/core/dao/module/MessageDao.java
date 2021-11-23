package com.oa.core.dao.module;

import com.oa.core.bean.module.Message;
import org.apache.ibatis.annotations.Param;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

public interface MessageDao {

    void insert(Message message);

    void delete(@Param("msgId") String msgId, @Param("deleteName") String deleteName, @Param("deleteTime") Timestamp deleteTime);

    void update(Message message);

    List<Message> selectAll();

    Message selectById(@Param("msgId") String msgId);

    List<Message> selectBySendUser(@Param("msgSendUser") String msgSendUser);

    int selectBySendUserCont(@Param("msgSendUser") String msgSendUser);

    List<Message> selectByRecUser(@Param("msgRecUser") String msgRecUser);

    int selectByRecUserCont(@Param("msgSendUser") String msgSendUser);

    Message selectTerms(Message message);

    List<Message> selectAllTerms(Message message);

    int selectAllTermsCont(Message message);

    List<Map<String,Object>> selectAllMsgCont();

    void deleteMessage(@Param("msgId") String msgId, @Param("deleteName") String deleteName, @Param("deleteTime") Timestamp deleteTime);

    void insertList(@Param("messageMap") Map<String,Message> messageMap);
}
