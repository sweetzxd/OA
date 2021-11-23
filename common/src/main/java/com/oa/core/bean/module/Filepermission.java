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
 * @Explain 文件权限表
 */
public class Filepermission implements Serializable {
    /**
     * 权限id
     * */
    private String permissionId;
    /**
     * 文件类型
     * */
    private String fileTypeId;
    /**
     * 角色类别
     * */
    private String actorType;
    /**
     * 相关值
     * */
    private String contextValue;
    /**
     * 查看权限 0可查看1不可查看
     * */
    private int see;
    /**
     * 下载权限 0可下载 1不可下载
     * */
    private int download;

    private int curStatus;
    private String recordName;
    private Timestamp recordTime;
    private String modifyName;
    private Timestamp modifyTime;
    private String deleteName;
    private Timestamp deleteTime;

    private int startRow;
    private int endRow;

    private String seeStr;
    private String downloadStr;

    private List<String> list;

    public String getPermissionId() {return permissionId; }

    public void setPermissionId(String permissionId) {this.permissionId = permissionId; }

    public String getFileTypeId() {return fileTypeId; }

    public void setFileTypeId(String fileTypeId) {
        this.fileTypeId = fileTypeId;
    }

    public String getActorType() {
        return actorType;
    }

    public void setActorType(String actorType) {
        this.actorType = actorType;
    }

    public String getContextValue() {
        return contextValue;
    }

    public void setContextValue(String contextValue) { this.contextValue = contextValue; }

    public int getSee() {
        return see;
    }

    public void setSee(Integer see) {
        this.see = see;
    }

    public int getDownload() {
        return download;
    }

    public void setDownload(Integer download) {
        this.download = download;
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

    public String getSeeStr() {
        return seeStr;
    }

    public void setSeeStr(String seeStr) {
        this.seeStr = seeStr;
    }

    public String getDownloadStr() {
        return downloadStr;
    }

    public void setDownloadStr(String downloadStr) {
        this.downloadStr = downloadStr;
    }

    public List<String> getList() { return list; }

    public void setList(List<String> list) {
        this.list = list;
    }
}