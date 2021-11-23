package com.oa.core.bean.module;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * @ClassName:Schedule
 * @author:wsx
 * @Date:2018/11/27
 * @Time:下午 4:40
 * @Version V1.0
 * @Explain
 */
public class Schedule implements Serializable {

    /**
     * 日程Id
     * */
    private String scheduleId;
    /**
     * 日程标题
     * */
    private String scheduletTitle;
    /**
     * 日程内容
     * */
    private String content;
    /**
     * 开始时间
     * */
    private String startTime;
    /**
     * 结束
     * */
    private String endTime;
    /**
     * 日程种类：1工作 2业务
     * */
    private int type;
    /**
     * 填写人
     * */
    private String user;
    /**
     * 是否显示：1否2是（只有领导日程可选择是否显示）
     * */
    private int state;

    private int curStatus;
    private String linkRecorderNO;
    private  String reserveField;
    private String recordName;
    private Timestamp recordTime;
    private String modifyName;
    private Timestamp modifyTime;
    private String deleteName;
    private Timestamp deleteTime;

    private int startRow;
    private int endRow;


    public String getScheduleId() {return scheduleId;}

    public void setScheduleId(String scheduleId) { this.scheduleId = scheduleId; }

    public String getScheduletTitle() {
        return scheduletTitle;
    }

    public void setScheduletTitle(String scheduletTitle) {
        this.scheduletTitle = scheduletTitle;
    }

    public String getContent() {return content; }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) { this.state = state; }

    public int getCurStatus() {
        return curStatus;
    }

    public void setCurStatus(int curStatus) {
        this.curStatus = curStatus;
    }

    public String getLinkRecorderNO() {
        return linkRecorderNO;
    }

    public void setLinkRecorderNO(String linkRecorderNO) {
        this.linkRecorderNO = linkRecorderNO;
    }

    public String getReserveField() {
        return reserveField;
    }

    public void setReserveField(String reserveField) {
        this.reserveField = reserveField;
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
}
