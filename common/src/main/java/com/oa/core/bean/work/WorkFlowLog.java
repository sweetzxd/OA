package com.oa.core.bean.work;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @ClassName:WorkFlowLog
 * @author:zxd
 * @Date:2018/09/20
 * @Time:上午 11:27
 * @Version V1.0
 * @Explain 流程运行日志记录表
 */
public class WorkFlowLog implements Serializable {

    /**
     * 流程日志表ID
     */
    private String wkflogId;
    /**
     * 流程定义号
     */
    private String wkflowId;
    /**
     * 流程运行号
     */
    private String procId;
    /**
     * 流程动作类型（运行节点；取消流程；改派角色；暂停流程；恢复继续流程；流程回退）
     */
    private String nodeAction;
    /**
     * 节点ID
     */
    private String nodeID;
    /**
     * 入口数据
     */
    private String inParams;
    /**
     * 输出数据
     */
    private String outParams;
    /**
     * 是否成功
     */
    private int success;
    /**
     * 成功结果/失败原因
     */
    private String reason;
    /**
     * 系统信息
     */
    private String addMessage;
    /**
     * 通知编号
     */
    private String msgID;
    /**
     * 主键字段
     */
    private String pkFeild;

    private int curStatus;
    private String recordName;
    private Timestamp recordTime;
    private String modifyName;
    private Timestamp modifyTime;
    private String deleteName;
    private Timestamp deleteTime;

    private int startRow;
    private int endRow;

    public String getWkflogId() {
        return wkflogId;
    }

    public void setWkflogId(String wkflogId) {
        this.wkflogId = wkflogId;
    }

    public String getWkflowId() {
        return wkflowId;
    }

    public void setWkflowId(String wkflowId) {
        this.wkflowId = wkflowId;
    }

    public String getProcId() {
        return procId;
    }

    public void setProcId(String procId) {
        this.procId = procId;
    }

    public String getNodeAction() {
        return nodeAction;
    }

    public void setNodeAction(String nodeAction) {
        this.nodeAction = nodeAction;
    }

    public String getNodeID() {
        return nodeID;
    }

    public void setNodeID(String nodeID) {
        this.nodeID = nodeID;
    }

    public String getInParams() {
        return inParams;
    }

    public void setInParams(String inParams) {
        this.inParams = inParams;
    }

    public String getOutParams() {
        return outParams;
    }

    public void setOutParams(String outParams) {
        this.outParams = outParams;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getAddMessage() {
        return addMessage;
    }

    public void setAddMessage(String addMessage) {
        this.addMessage = addMessage;
    }

    public String getMsgID() {
        return msgID;
    }

    public void setMsgID(String msgID) {
        this.msgID = msgID;
    }

    public String getPkFeild() {
        return pkFeild;
    }

    public void setPkFeild(String pkFeild) {
        this.pkFeild = pkFeild;
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
