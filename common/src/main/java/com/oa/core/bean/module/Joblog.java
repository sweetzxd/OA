package com.oa.core.bean.module;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @ClassName:Joblog
 * @author:wsx
 * @Date:2018/11/29
 * @Time:上午 9:55
 * @Version V1.0
 * @Explain工作日志表
 */
public class Joblog implements Serializable {

    /**
     * 工作日志Id
     * */
    private String joblogId;
    /**
     * 填写人
     * */
    private String user;
    /**
     * 所属部门
     * */
    private String deptId;
    /**
     * 上报人
     * */
    private String leader;
    /**
     * 开始时间
     * */
    private String startTime;
    /**
     * 结束时间
     * */
    private String endTime;
    /**
     * 工作内容
     * */
    private String content;
    /**
     * 完成情况
     * */
    private String finish;
    /**
     * 附件
     * */
    private String file;
    /**
     * 备注
     * */
    private String remark;
    /**
     * 日志状态：1：填写；2：上报；3：修改
     * */
    private int state;
    /**
     * 审批状态：1：打回；2：通过
     * */
    private int status;
    /**
     * 打回原因
     * */
    private String reason;

    private int curStatus;
    private String linkRecorderNO;
    private  String reserveField;
    private String recordName;
    private Timestamp recordTime;
    private String modifyName;
    private Timestamp modifyTime;
    private String deleteName;
    private Timestamp deleteTime;

    private int startRow;
    private int endRow;

    private String deptStr;
    private String leaderStr;
    private String userStr;


    public String getJoblogId() {return joblogId;}

    public void setJoblogId(String joblogId) { this.joblogId = joblogId; }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getDeptId() {return deptId; }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getLeader() {return leader; }

    public void setLeader(String leader) {
        this.leader = leader;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFinish() {
        return finish;
    }

    public void setFinish(String finish) {
        this.finish = finish;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getRemark() { return remark; }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getReason() {return reason;}

    public void setReason(String reason) { this.reason = reason; }

    public int getCurStatus() {
        return curStatus;
    }

    public void setCurStatus(int curStatus) {
        this.curStatus = curStatus;
    }

    public String getLinkRecorderNO() {
        return linkRecorderNO;
    }

    public void setLinkRecorderNO(String linkRecorderNO) {
        this.linkRecorderNO = linkRecorderNO;
    }

    public String getReserveField() {
        return reserveField;
    }

    public void setReserveField(String reserveField) {
        this.reserveField = reserveField;
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

    public String getDeptStr() {
        return deptStr;
    }

    public void setDeptStr(String deptStr) {
        this.deptStr = deptStr;
    }

    public String getLeaderStr() {
        return leaderStr;
    }

    public void setLeaderStr(String leaderStr) {
        this.leaderStr = leaderStr;
    }

    public String getUserStr() {
        return userStr;
    }

    public void setUserStr(String userStr) {
        this.userStr = userStr;
    }

}
