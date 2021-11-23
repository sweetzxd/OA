package com.oa.core.bean.module;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * @ClassName:Department
 * @author:zxd
 * @Date:2018/10/19
 * @Time:上午 8:38
 * @Version V1.0
 * @Explain 文件表
 */
public class File implements Serializable {

    /**
     * 文件id
     * */
    private String fileId;
    /**
     * 文件名
     * */
    private String fileName;
    /**
     * 文件类型
     * */
    private String fileTypeId;
    /**
     * 上传时间
     * */
    private Timestamp uploadTime;
    /**
     * 简介
     * */
    private String intro;
    /**
     * 文件地址
     * */
    private String fileAdd;
    /**
     * 备注
     * */
    private String remark;

    private int curStatus;
    private String recordName;
    private Timestamp recordTime;
    private String modifyName;
    private Timestamp modifyTime;
    private String deleteName;
    private Timestamp deleteTime;

    private int startRow;
    private int endRow;

    private List<String> list;

    private String uploadTimeStr;


    public String getFileId() {return fileId;}

    public void setFileId(String fileId) { this.fileId = fileId; }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileTypeId() {return fileTypeId; }

    public void setFileTypeId(String fileTypeId) {
        this.fileTypeId = fileTypeId;
    }

    public Timestamp getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Timestamp uploadTime) {
        this.uploadTime = uploadTime;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getFileAdd() {
        return fileAdd;
    }

    public void setFileAdd(String fileAdd) {
        this.fileAdd = fileAdd;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public List<String> getList() { return list; }

    public void setList(List<String> list) {
        this.list = list;
    }

    public String getUploadTimeStr() {return uploadTimeStr;}

    public void setUploadTimeStr(String uploadTimeStr) { this.uploadTimeStr = uploadTimeStr; }
}