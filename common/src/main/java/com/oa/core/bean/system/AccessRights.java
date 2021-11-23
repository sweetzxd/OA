package com.oa.core.bean.system;

import java.io.Serializable;
import java.sql.Timestamp;

public class AccessRights implements Serializable {

    private String accessId;
    private String pageId;
    private String roleId;
    private String accessType;
    private String accessValue;

    private int curStatus;
    private String recordName;
    private Timestamp recordTime;
    private String modifyName;
    private Timestamp modifyTime;
    private String deleteName;
    private Timestamp deleteTime;

    private int startRow;
    private int endRow;

    public AccessRights(){
        super();
    }

    public AccessRights(String accessId){
        super();
        this.accessId = accessId;
    }

    public AccessRights(String pageId,String accessType){
        super();
        this.pageId = pageId;
        this.accessType = accessType;
    }
    public AccessRights(String accessId,String pageId,String roleId){
        super();
        this.accessId = accessId;
        this.pageId = pageId;
        this.roleId = roleId;
    }
    public AccessRights(String accessId,String pageId,String roleId,String accessType){
        super();
        this.accessId = accessId;
        this.pageId = pageId;
        this.roleId = roleId;
        this.accessType = accessType;
    }

    public AccessRights(String accessId,String pageId,String roleId,String accessType,String accessValue){
        super();
        this.accessId = accessId;
        this.pageId = pageId;
        this.roleId = roleId;
        this.accessType = accessType;
        this.accessValue = accessValue;
    }

    public String getAccessId() {
        return accessId;
    }

    public void setAccessId(String accessId) {
        this.accessId = accessId;
    }

    public String getPageId() {
        return pageId;
    }

    public void setPageId(String pageId) {
        this.pageId = pageId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getAccessType() {
        return accessType;
    }

    public void setAccessType(String accessType) {
        this.accessType = accessType;
    }

    public String getAccessValue() {
        return accessValue;
    }

    public void setAccessValue(String accessValue) {
        this.accessValue = accessValue;
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
