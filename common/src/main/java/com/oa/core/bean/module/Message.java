package com.oa.core.bean.module;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @ClassName:Message
 * @author:zxd
 * @Date:2018/10/19
 * @Time:下午 3:22
 * @Version V1.0
 * @Explain
 */
public class Message implements Serializable {

    /**
     * 消息主键
     * */
    private String msgId;
    /**
     * 消息标题
     * */
    private String msgTitle;
    /**
     * 消息发出人
     * */
    private String msgSendUser;
    /**
     * 消息接收人
     * */
    private String msgRecUser;
    /**
     * 消息内容
     * */
    private String msgText;
    /**
     * 消息种类
     * */
    private String msgType;
    /**
     * 消息连接
     * */
    private String msgUrl;
    /**
     * 消息文件
     * */
    private String msgFile;
    /**
     * 消息等级
     * 0：正常，1：急，2：紧急，3：特急
     * */
    private int msgLevel;
    /**
     * 消息发送时间
     * */
    private String msgSendTime;
    /**
     * 消息到期时间
     * */
    private String msgEndTime;
    /**
     * 消息状态
     * 0：删除，1：未读，2：已读
     * 数据库默认：1
     * */
    private int msgStatus;

    private int curStatus;
    private String recordName;
    private Timestamp recordTime;
    private String modifyName;
    private Timestamp modifyTime;
    private String deleteName;
    private Timestamp deleteTime;

    private int startRow;
    private int endRow;

    private String msgSendUserName;
    private String msgSendTimeStr;
    private String msgLevelStr;

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getMsgTitle() {
        return msgTitle;
    }

    public void setMsgTitle(String msgTitle) {
        this.msgTitle = msgTitle;
    }

    public String getMsgSendUser() {
        return msgSendUser;
    }

    public void setMsgSendUser(String msgSendUser) {
        this.msgSendUser = msgSendUser;
    }

    public String getMsgRecUser() {
        return msgRecUser;
    }

    public void setMsgRecUser(String msgRecUser) {
        this.msgRecUser = msgRecUser;
    }

    public String getMsgText() {
        return msgText;
    }

    public void setMsgText(String msgText) {
        this.msgText = msgText;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getMsgUrl() {
        return msgUrl;
    }

    public void setMsgUrl(String msgUrl) {
        this.msgUrl = msgUrl;
    }

    public String getMsgFile() {
        return msgFile;
    }

    public void setMsgFile(String msgFile) {
        this.msgFile = msgFile;
    }

    public int getMsgLevel() {
        return msgLevel;
    }

    public void setMsgLevel(int msgLevel) {
        this.msgLevel = msgLevel;
    }

    public String getMsgSendTime() {
        return msgSendTime;
    }

    public void setMsgSendTime(String msgSendTime) {
        this.msgSendTime = msgSendTime;
    }

    public String getMsgEndTime() {
        return msgEndTime;
    }

    public void setMsgEndTime(String msgEndTime) {
        this.msgEndTime = msgEndTime;
    }

    public int getMsgStatus() {
        return msgStatus;
    }

    public void setMsgStatus(int msgStatus) {
        this.msgStatus = msgStatus;
    }

    public int getCurStatus() {
        return curStatus;
    }

    public void setCurStatus(int curStatus) {
        this.curStatus = curStatus;
    }

    public String getRecordName() {
        return recordName;
    }

    public void setRecordName(String recordName) {
        this.recordName = recordName;
    }

    public Timestamp getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(Timestamp recordTime) {
        this.recordTime = recordTime;
    }

    public String getModifyName() {
        return modifyName;
    }

    public void setModifyName(String modifyName) {
        this.modifyName = modifyName;
    }

    public Timestamp getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Timestamp modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getDeleteName() {
        return deleteName;
    }

    public void setDeleteName(String deleteName) {
        this.deleteName = deleteName;
    }

    public Timestamp getDeleteTime() {
        return deleteTime;
    }

    public void setDeleteTime(Timestamp deleteTime) {
        this.deleteTime = deleteTime;
    }

    public int getStartRow() {
        return startRow;
    }

    public void setStartRow(int startRow) {
        this.startRow = startRow;
    }

    public int getEndRow() {
        return endRow;
    }

    public void setEndRow(int endRow) {
        this.endRow = endRow;
    }

    public String getMsgSendUserName() {
        return msgSendUserName;
    }

    public void setMsgSendUserName(String msgSendUserName) {
        this.msgSendUserName = msgSendUserName;
    }

    public String getMsgSendTimeStr() {
        return msgSendTimeStr;
    }

    public void setMsgSendTimeStr(String msgSendTimeStr) {
        this.msgSendTimeStr = msgSendTimeStr;
    }

    public String getMsgLevelStr() {
        return msgLevelStr;
    }

    public void setMsgLevelStr(String msgLevelStr) {
        this.msgLevelStr = msgLevelStr;
    }
}
