package com.oa.core.bean.module;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * @ClassName:Festival
 * @author:wsx
 * @Date:2018/11/24
 * @Time:上午 11:12
 * @Version V1.0
 * @Explain 节假日表
 */
public class Festival implements Serializable {

    /**
     * 节假日id
     * */
    private String festivalId;
    /**
     * 节假日名称
     * */
    private String festivalName;
    /**
     * 类型：1阳历；2阴历；3例外
     * */
    private int type;
    /**
     * 开始时间
     * */
    private String startTime;
    /**
     * 结束时间
     * */
    private String endTime;

    private int curStatus;
    private String recordName;
    private Timestamp recordTime;
    private String modifyName;
    private Timestamp modifyTime;
    private String deleteName;
    private Timestamp deleteTime;

    private int startRow;
    private int endRow;

    private String typeStr;
    private String operation;

    public String getFestivalId() {return festivalId;}

    public void setFestivalId(String festivalId) { this.festivalId = festivalId; }

    public String getFestivalName() {
        return festivalName;
    }

    public void setFestivalName(String festivalName) {
        this.festivalName = festivalName;
    }

    public int getType() {return type; }

    public void setType(int type) {
        this.type = type;
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

    public String getTypeStr() {return typeStr;}

    public void setTypeStr(String typeStr) { this.typeStr = typeStr; }

    public String getOperation() {return operation;}

    public void setOperation(String operation) { this.operation = operation; }
}
