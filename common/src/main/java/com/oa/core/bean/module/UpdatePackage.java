package com.oa.core.bean.module;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @ClassName:UpdatePackage
 * @author:zxd
 * @Date:2018/11/16
 * @Time:下午 3:45
 * @Version V1.0
 * @Explain 更新包储存表
 */
public class UpdatePackage implements Serializable {

    /**
     * 更新包台账主键
     */
    private String packId;
    /**
     * 更新包名称
     */
    private String packName;
    /**
     * 更新包类型 功能、BUG、其它
     */
    private String packType;
    /**
     * 更新包依赖
     */
    private String packRely;
    /**
     * 更新包上传时间
     */
    private String packUpTime;
    /**
     * 更新包上传路径
     */
    private String packUpFile;
    /**
     * 更新包上传人
     */
    private String packUpName;
    /**
     * 更新包版本
     */
    private String packVersion;
    /**
     * 更新状态 1:已更新、0:未更新
     */
    private int updateType;

    private int curStatus;
    private String recordName;
    private Timestamp recordTime;
    private String modifyName;
    private Timestamp modifyTime;
    private String deleteName;
    private Timestamp deleteTime;

    private int startRow;
    private int endRow;

    public String getPackId() {
        return packId;
    }

    public void setPackId(String packId) {
        this.packId = packId;
    }

    public String getPackName() {
        return packName;
    }

    public void setPackName(String packName) {
        this.packName = packName;
    }

    public String getPackType() {
        return packType;
    }

    public void setPackType(String packType) {
        this.packType = packType;
    }

    public String getPackRely() {
        return packRely;
    }

    public void setPackRely(String packRely) {
        this.packRely = packRely;
    }

    public String getPackUpTime() {
        return packUpTime;
    }

    public void setPackUpTime(String packUpTime) {
        this.packUpTime = packUpTime;
    }

    public String getPackUpFile() {
        return packUpFile;
    }

    public void setPackUpFile(String packUpFile) {
        this.packUpFile = packUpFile;
    }

    public String getPackUpName() {
        return packUpName;
    }

    public void setPackUpName(String packUpName) {
        this.packUpName = packUpName;
    }

    public String getPackVersion() {
        return packVersion;
    }

    public void setPackVersion(String packVersion) {
        this.packVersion = packVersion;
    }

    public int getUpdateType() {
        return updateType;
    }

    public void setUpdateType(int updateType) {
        this.updateType = updateType;
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
