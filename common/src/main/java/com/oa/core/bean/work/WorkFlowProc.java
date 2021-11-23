package com.oa.core.bean.work;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @ClassName:WorkFlowProc
 * @author:zxd
 * @Date:2018/09/20
 * @Time:上午 11:27
 * @Version V1.0
 * @Explain 流程运行表
 */
public class WorkFlowProc implements Serializable {

    /**
     * 流程运行号
     */
    private String procId;
    /**
     * 流程定义号
     */
    private String wkflwID;
    /**
     * 起始节点
     */
    private String startNodeID;
    /**
     * 发起人
     */
    private String originator;
    /**
     * 流程参与人
     */
    private String flowpeoples;
    /**
     * 选择节点执行人
     */
    private String nodespeople;
    /**
     * 流程状态（0：启动；1：执行中:2：完成:3：取消:4：暂停:5：错误）
     */
    private int wkfStatus;
    /**
     * 当前节点
     */
    private String curNodeID;
    /**
     * 流程结果（0：完成；1：失败；2：取消）
     */
    private int wkfResult;
    /**
     * 终结节点
     */
    private String endNodeID;
    /**
     * 记录密级（0：公开:1：保密:2：机密:3：绝密）
     */
    private int security;
    /**
     * 紧急程度（0：普通；1：紧急）
     */
    private int runLevel;

    private int curStatus;
    private String recordName;
    private Timestamp recordTime;
    private String modifyName;
    private Timestamp modifyTime;
    private String deleteName;
    private Timestamp deleteTime;

    private int startRow;
    private int endRow;

    public String getProcId() {
        return procId;
    }

    public void setProcId(String procId) {
        this.procId = procId;
    }

    public String getWkflwID() {
        return wkflwID;
    }

    public void setWkflwID(String wkflwID) {
        this.wkflwID = wkflwID;
    }

    public String getStartNodeID() {
        return startNodeID;
    }

    public void setStartNodeID(String startNodeID) {
        this.startNodeID = startNodeID;
    }

    public String getOriginator() {
        return originator;
    }

    public void setOriginator(String originator) {
        this.originator = originator;
    }

    public String getFlowpeoples() {
        return flowpeoples;
    }

    public void setFlowpeoples(String flowpeoples) {
        this.flowpeoples = flowpeoples;
    }

    public String getNodespeople() {
        return nodespeople;
    }

    public void setNodespeople(String nodespeople) {
        this.nodespeople = nodespeople;
    }

    public int getWkfStatus() {
        return wkfStatus;
    }

    public void setWkfStatus(int wkfStatus) {
        this.wkfStatus = wkfStatus;
    }

    public String getCurNodeID() {
        return curNodeID;
    }

    public void setCurNodeID(String curNodeID) {
        this.curNodeID = curNodeID;
    }

    public int getWkfResult() {
        return wkfResult;
    }

    public void setWkfResult(int wkfResult) {
        this.wkfResult = wkfResult;
    }

    public String getEndNodeID() {
        return endNodeID;
    }

    public void setEndNodeID(String endNodeID) {
        this.endNodeID = endNodeID;
    }

    public int getSecurity() {
        return security;
    }

    public void setSecurity(int security) {
        this.security = security;
    }

    public int getRunLevel() {
        return runLevel;
    }

    public void setRunLevel(int runLevel) {
        this.runLevel = runLevel;
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
