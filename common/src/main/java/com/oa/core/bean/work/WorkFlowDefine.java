package com.oa.core.bean.work;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Hashtable;

/**
 * @ClassName:WorkFlowDefing
 * @author:zxd
 * @Date:2018/09/20
 * @Time:上午 11:25
 * @Version V1.0
 * @Explain 流程定义表
 */
public class WorkFlowDefine implements Serializable {

    /**
     * 流程定义号
     */
    private String wkflwID;
    /**
     * 流程名称
     */
    private String wkfName;
    /**
     * 流程类别（市场经营；综合办公；人力资源；资产财务...）
     */
    private String wkfType;
    /**
     * 暂定关联表
     */
    private String tableName;
    /**
     * 流程图xml
     */
    private String wkfValue;
    /**
     * 邮件内容模板
     */
    private String mailContent;
    /**
     * 特殊处理字段
     */
    private String specialField;
    /**
     * 特殊处理字段
     */
    private String pageId;
    /**
     * 流程任务模板
     */
    private String flowLabFld;
    /**
     * 流程消息模板
     */
    private String flowLabFldliucheng;
    /**
     * 流程全局主键
     */
    private String pkFeilds;
    /**
     * 流程状态（0：取消，1：修改，2：发布）
     */
    private int wkfdefStatus;

    private int curStatus;
    private String recordName;
    private Timestamp recordTime;
    private String modifyName;
    private Timestamp modifyTime;
    private String deleteName;
    private Timestamp deleteTime;

    private int startRow;
    private int endRow;

    public String getWkflwID() {
        return wkflwID;
    }

    public void setWkflwID(String wkflwID) {
        this.wkflwID = wkflwID;
    }

    public String getWkfName() {
        return wkfName;
    }

    public void setWkfName(String wkfName) {
        this.wkfName = wkfName;
    }

    public String getWkfType() {
        return wkfType;
    }

    public void setWkfType(String wkfType) {
        this.wkfType = wkfType;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getWkfValue() {
        return wkfValue;
    }

    public void setWkfValue(String wkfValue) {
        this.wkfValue = wkfValue;
    }

    public String getMailContent() {
        return mailContent;
    }

    public void setMailContent(String mailContent) {
        this.mailContent = mailContent;
    }

    public String getSpecialField() {
        return specialField;
    }

    public void setSpecialField(String specialField) {
        this.specialField = specialField;
    }

    public String getPageId() {
        return pageId;
    }

    public void setPageId(String pageId) {
        this.pageId = pageId;
    }

    public String getFlowLabFld() {
        return flowLabFld;
    }

    public void setFlowLabFld(String flowLabFld) {
        this.flowLabFld = flowLabFld;
    }

    public String getFlowLabFldliucheng() {
        return flowLabFldliucheng;
    }

    public void setFlowLabFldliucheng(String flowLabFldliucheng) {
        this.flowLabFldliucheng = flowLabFldliucheng;
    }

    public String getPkFeilds() {
        return pkFeilds;
    }

    public void setPkFeilds(String pkFeilds) {
        this.pkFeilds = pkFeilds;
    }

    public int getWkfdefStatus() {
        return wkfdefStatus;
    }

    public void setWkfdefStatus(int wkfdefStatus) {
        this.wkfdefStatus = wkfdefStatus;
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
        String field = "wkflwID,wkfName,wkfType,tableName,wkfValue,mailContent,specialField,pageId,flowLabFld,pkFeilds,wkfdefStatus,curStatus,recordName,recordTime,modifyName,modifyTime,deleteName,deleteTime";
        ht.put("field",field);

        String wkflwID = getStringValue(getWkflwID());
        String wkfName = getStringValue(getWkfName());
        String wkfType = getStringValue(getWkfType());
        String tableName = getStringValue(getTableName());
        String wkfValue = getStringValue(getWkfValue());
        String mailContent = getStringValue(getMailContent());
        String specialField = getStringValue(getSpecialField());
        String pageId = getStringValue(getPageId());
        String flowLabFld = getStringValue(getFlowLabFld());
        String pkFeilds = getStringValue(getPkFeilds());
        int wkfdefStatus = getWkfdefStatus();

        int curStatus = 2;
        String recordName = getStringValue(getRecordName());
        String recordTime = getStringValue(getRecordTime());
        String modifyName = getStringValue(getModifyName());
        String modifyTime = getStringValue(getModifyTime());
        String deleteName = getStringValue(getDeleteName());
        String deleteTime = getStringValue(getDeleteTime());

        String value = wkflwID+","+wkfName+","+wkfType+","+tableName+","+wkfValue+","+mailContent+","+specialField+","+pageId+","+flowLabFld+"," +
                ""+pkFeilds+","+wkfdefStatus+","+curStatus+","+recordName+","+recordTime+","+modifyName+","+modifyTime+","+deleteName+","+deleteTime;
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
