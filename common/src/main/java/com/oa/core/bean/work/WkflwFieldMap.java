package com.oa.core.bean.work;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Hashtable;

/**
 * @ClassName:WkflwFieldMap
 * @author:wsx
 * @Date:2018/11/12
 * @Time:上午 9:53
 * @Version V1.0
 * @Explain 流程节点字段映射表
 */
public class WkflwFieldMap implements Serializable {

    /**
     * 字段映射主键
     */
    private String wkflwfieldmapId;
    /**
     * 流程定义号
     */
    private String wkflwId;
    /**
     *节点表名
     */
    private String nodeTableName;
    /**
     *节点字段名
     */
    private String nodeFieldName;
    /**
     *表单表名
     */
    private String formTableName;
    /**
     * 表单字段名
     */
    private String formFieldName;

    private int curStatus;
    private String recordName;
    private Timestamp recordTime;
    private String modifyName;
    private Timestamp modifyTime;
    private String deleteName;
    private Timestamp deleteTime;

    private int startRow;
    private int endRow;


    public String getWkflwfieldmapId() {
        return wkflwfieldmapId;
    }

    public void setWkflwfieldmapId(String wkflwfieldmapId) {
        this.wkflwfieldmapId = wkflwfieldmapId;
    }

    public String getWkflwId() {
        return wkflwId;
    }

    public void setWkflwId(String wkflwId) {
        this.wkflwId = wkflwId;
    }

    public String getNodeTableName() {
        return nodeTableName;
    }

    public void setNodeTableName(String nodeTableName) {
        this.nodeTableName = nodeTableName;
    }

    public String getNodeFieldName() {
        return nodeFieldName;
    }

    public void setNodeFieldName(String nodeFieldName) {
        this.nodeFieldName = nodeFieldName;
    }

    public String getFormTableName() {
        return formTableName;
    }

    public void setFormTableName(String formTableName) {
        this.formTableName = formTableName;
    }

    public String getFormFieldName() {
        return formFieldName;
    }

    public void setFormFieldName(String formFieldName) {
        this.formFieldName = formFieldName;
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

        String field = "wkflwfieldmapId,wkflwId,nodeTableName,nodeFieldName,formTableName,formFieldName" +
                ",curStatus,recordName,recordTime,modifyName,modifyTime,deleteName,deleteTime";
        ht.put("field",field);

        String wkflwfieldmapId = getStringValue(getWkflwfieldmapId());
        String wkflwId = getStringValue(getWkflwId());
        String nodeTableName = getStringValue(getNodeTableName());
        String nodeFieldName = getStringValue(getNodeFieldName());
        String formTableName = getStringValue(getFormTableName());
        String formFieldName = getStringValue(getFormFieldName());

        int curStatus = 2;
        String recordName = getStringValue(getRecordName());
        String recordTime = getStringValue(getRecordTime());
        String modifyName = getStringValue(getModifyName());
        String modifyTime = getStringValue(getModifyTime());
        String deleteName = getStringValue(getDeleteName());
        String deleteTime = getStringValue(getDeleteTime());

        String value = wkflwfieldmapId+","+wkflwId+","+nodeTableName+","+nodeFieldName+","+formTableName+","+formFieldName+"" +
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
