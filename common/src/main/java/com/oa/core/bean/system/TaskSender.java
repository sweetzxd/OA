package com.oa.core.bean.system;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @ClassName:TaskSender
 * @author:zxd
 * @Date:2018/09/20
 * @Time:上午 11:28
 * @Version V1.0
 * @Explain 任务表
 */
public class TaskSender implements Serializable {

    /**
     * 任务号
     */
    private String workOrderNO;
    private String sameOrderNO;
    /**
     * 流程定义号
     */
    private String wkflwID;
    /**
     * 流程运行号
     */
    private String procID;
    /**
     * 当前任务节点
     */
    private String wkfNode;
    /**
     * 任务接受人
     */
    private String accepter;
    /**
     * 页面跳转路径
     */
    private String refLinkUrl;
    /**
     * 任务级别（待定）
     */
    private String stress;
    /**
     * 任务通知状态（0：删除，1：未读，2：已读，3：处理中，4：已处完毕，5：暂停）
     */
    private int msgStatus;
    /**
     * 任务名称
     */
    private String taskTitle;
    /**
     * 关联关键词
     */
    private String linkKey;
    /**
     * 关联记录号
     */
    private String linkRecordID;
    /**
     * 提前提醒时间
     */
    private Timestamp remindTime;
    /**
     * 要求完成时间
     */
    private Timestamp finishTime;
    /**
     * 提醒周期
     */
    private Double remindPeriod;

    private int curStatus;
    private String recordName;
    private Timestamp recordTime;
    private String modifyName;
    private Timestamp modifyTime;
    private String deleteName;
    private Timestamp deleteTime;

    private int startRow;
    private int endRow;

    public String getWorkOrderNO() {
        return workOrderNO;
    }

    public void setWorkOrderNO(String workOrderNO) {
        this.workOrderNO = workOrderNO;
    }

    public String getSameOrderNO() {
        return sameOrderNO;
    }

    public void setSameOrderNO(String sameOrderNO) {
        this.sameOrderNO = sameOrderNO;
    }

    public String getWkflwID() {
        return wkflwID;
    }

    public void setWkflwID(String wkflwID) {
        this.wkflwID = wkflwID;
    }

    public String getProcID() {
        return procID;
    }

    public void setProcID(String procID) {
        this.procID = procID;
    }

    public String getWkfNode() {
        return wkfNode;
    }

    public void setWkfNode(String wkfNode) {
        this.wkfNode = wkfNode;
    }

    public String getAccepter() {
        return accepter;
    }

    public void setAccepter(String accepter) {
        this.accepter = accepter;
    }

    public String getRefLinkUrl() {
        return refLinkUrl;
    }

    public void setRefLinkUrl(String refLinkUrl) {
        this.refLinkUrl = refLinkUrl;
    }

    public String getStress() {
        return stress;
    }

    public void setStress(String stress) {
        this.stress = stress;
    }

    public int getMsgStatus() {
        return msgStatus;
    }

    public void setMsgStatus(int msgStatus) {
        this.msgStatus = msgStatus;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public String getLinkKey() {
        return linkKey;
    }

    public void setLinkKey(String linkKey) {
        this.linkKey = linkKey;
    }

    public String getLinkRecordID() {
        return linkRecordID;
    }

    public void setLinkRecordID(String linkRecordID) {
        this.linkRecordID = linkRecordID;
    }

    public Timestamp getRemindTime() {
        return remindTime;
    }

    public void setRemindTime(Timestamp remindTime) {
        this.remindTime = remindTime;
    }

    public Timestamp getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Timestamp finishTime) {
        this.finishTime = finishTime;
    }

    public Double getRemindPeriod() {
        return remindPeriod;
    }

    public void setRemindPeriod(Double remindPeriod) {
        this.remindPeriod = remindPeriod;
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
}
