package com.oa.core.bean.module;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @ClassName:Department
 * @author:zxd
 * @Date:2018/10/13
 * @Time:下午 2:38
 * @Version V1.0
 * @Explain 部门表
 */
public class Department implements Serializable {

    /**
     * 部门编号
     * */
    private String deptId;
    /**
     * 部门名称
     * */
    private String deptName;
    /**
     * 上级部门编号
     * */
    private String superiorDeptId;
    /**
     * 部门主负责人
     * */
    private String deptHead;
    /**
     * 部门副负责人
     * */
    private String deputyDeptHead;
    /**
     * 上级部门负责人
     * */
    private String superiorDeptHead;
    /**
     * 部门排序
     * */
    private int deptSort;

    private int curStatus;
    private String recordName;
    private Timestamp recordTime;
    private String modifyName;
    private Timestamp modifyTime;
    private String deleteName;
    private Timestamp deleteTime;

    private int startRow;
    private int endRow;

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getSuperiorDeptId() {
        return superiorDeptId;
    }

    public void setSuperiorDeptId(String superiorDeptId) {
        this.superiorDeptId = superiorDeptId;
    }

    public String getDeptHead() {
        return deptHead;
    }

    public void setDeptHead(String deptHead) {
        this.deptHead = deptHead;
    }

    public String getDeputyDeptHead() {
        return deputyDeptHead;
    }

    public void setDeputyDeptHead(String deputyDeptHead) {
        this.deputyDeptHead = deputyDeptHead;
    }

    public String getSuperiorDeptHead() {
        return superiorDeptHead;
    }

    public void setSuperiorDeptHead(String superiorDeptHead) {
        this.superiorDeptHead = superiorDeptHead;
    }

    public int getDeptSort() {
        return deptSort;
    }

    public void setDeptSort(int deptSort) {
        this.deptSort = deptSort;
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
