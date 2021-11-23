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
 * @Explain 文件类型表
 */
public class Filetype implements Serializable {
    /**
     * 文件类型id
     * */
    private String fileTypeId;
    /**
     * 文件类型名
     * */
    private String fileTypeName;
    /**
     * 上级类型
     * */
    private String superiorFileTypeId;
    /**
     * 角色id
     * */
    private String roleId;

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

    public String getFileTypeId() { return fileTypeId; }

    public void setFileTypeId(String fileTypeId) {
        this.fileTypeId = fileTypeId;
    }

    public String getFileTypeName() {
        return fileTypeName;
    }

    public void setFileTypeName(String fileTypeName) {this.fileTypeName = fileTypeName; }

    public String getSuperiorFileTypeId() {
        return superiorFileTypeId;
    }

    public void setSuperiorFileTypeId(String superiorFileTypeId) {this.superiorFileTypeId = superiorFileTypeId; }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
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
}