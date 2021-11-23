package com.oa.core.bean.work;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Hashtable;

/**
 * @ClassName:wkflwNodeActor
 * @author:zxd
 * @Date:2018/09/28
 * @Time:上午 8:49
 * @Version V1.0
 * @Explain 流程节点执行人权限表
 */
public class WkflwNodeActor implements Serializable {

    /**
     * 节点权限主键
     */
    private String nodeActorID;
    /**
     * 节点主键
     */
    private String nodeID;
    /**
     * 角色主键（如果为空则获取相关值）
     */
    private String actorID;
    /**
     * 流程角色类别
     */
    private String wkfActorType;
    /**
     * 相关值（流程内部字段权限字段，如：[流程定义号_表名_字段名]，其中字段名是人员字段）
     */
    private String contextValue;
    /**
     * 节点角色顺序
     */
    private int nodeActrOrder;
    /**
     * 页面权限
     */
    private String formActions;

    private int curStatus;
    private String recordName;
    private Timestamp recordTime;
    private String modifyName;
    private Timestamp modifyTime;
    private String deleteName;
    private Timestamp deleteTime;

    private int startRow;
    private int endRow;

    public String getNodeActorID() {
        return nodeActorID;
    }

    public void setNodeActorID(String nodeActorID) {
        this.nodeActorID = nodeActorID;
    }

    public String getNodeID() {
        return nodeID;
    }

    public void setNodeID(String nodeID) {
        this.nodeID = nodeID;
    }

    public String getActorID() {
        return actorID;
    }

    public void setActorID(String actorID) {
        this.actorID = actorID;
    }

    public String getWkfActorType() {
        return wkfActorType;
    }

    public void setWkfActorType(String wkfActorType) {
        this.wkfActorType = wkfActorType;
    }

    public String getContextValue() {
        return contextValue;
    }

    public void setContextValue(String contextValue) {
        this.contextValue = contextValue;
    }

    public int getNodeActrOrder() {
        return nodeActrOrder;
    }

    public void setNodeActrOrder(int nodeActrOrder) {
        this.nodeActrOrder = nodeActrOrder;
    }

    public String getFormActions() {
        return formActions;
    }

    public void setFormActions(String formActions) {
        this.formActions = formActions;
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

    public Hashtable<String,String> getSqlField(){
        Hashtable<String,String> ht = new Hashtable<>();
        String field = "nodeActorID,nodeID,actorID,wkfActorType,contextValue,nodeActrOrder,formActions" +
                ",curStatus,recordName,recordTime,modifyName,modifyTime,deleteName,deleteTime";
        ht.put("field",field);

        String nodeActorID = getStringValue(getNodeActorID());
        String nodeID = getStringValue(getNodeID());
        String actorID = getStringValue(getActorID());
        String wkfActorType = getStringValue(getWkfActorType());
        String contextValue = getStringValue(getContextValue());
        int nodeActrOrder = getNodeActrOrder();
        String formActions = getStringValue(getFormActions());

        int curStatus = 2;
        String recordName = getStringValue(getRecordName());
        String recordTime = getStringValue(getRecordTime());
        String modifyName = getStringValue(getModifyName());
        String modifyTime = getStringValue(getModifyTime());
        String deleteName = getStringValue(getDeleteName());
        String deleteTime = getStringValue(getDeleteTime());

        String value = nodeActorID+","+nodeID+","+actorID+","+wkfActorType+","+contextValue+","+nodeActrOrder+","+formActions+"" +
                ","+curStatus+","+recordName+","+recordTime+","+modifyName+","+modifyTime+","+deleteName+","+deleteTime;
        ht.put("value",value);
        return ht;
    }

    public String getStringValue(Object val){
        if(val!=null){
            return "'"+val+"'";
        }else{
            return null;
        }
    }
}
