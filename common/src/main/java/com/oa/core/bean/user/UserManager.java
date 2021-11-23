package com.oa.core.bean.user;

import java.io.Serializable;
import java.sql.Timestamp;
/**
 * @ClassName:UserManager
 * @author zxd
 * @date 2018/09/15
 * @Time:11:10
 * @Version V1.0
 **/
public class UserManager implements Serializable {
    /**
     * 登录名
     */
    private String userName;
    /**
     * 姓名
     */
    private String name;
    /**
     * 密码
     */
    private String password;
    /**
     * 登录电脑IP
     */
    private String computerIP;
    /**
     * 关联用户信息表ID
     */
    private String staffId;
    /**
     * 账号状态 1：停用 2：启用
     */
    private int accountStatus=2;
    /**
     * 用户状态 1：临时账号 2：可登录 3：只允许本机登录
     */
    private int userStatus=2;
    /**
     * 账号启用时间
     */
    private String startDate;
    /**
     * 账号停用时间
     */
    private String endDate;

    private int curStatus=2;
    private String recordName;
    private Timestamp recordTime;
    private String modifyName;
    private Timestamp modifyTime;
    private String deleteName;
    private Timestamp deleteTime;

    private int startRow;
    private int endRow;
    public UserManager(){
        super();
    }
    public UserManager(String type, String value){
        super();
        if ("userName".equals(type)) {
            this.userName=value;
        } else if ("name".equals(type)) {
            this.name=value;
        } else if ("computerIP".equals(type)) {
            this.computerIP=value;
        } else if ("accountStatus".equals(type)) {
            this.accountStatus=Integer.parseInt(value);
        } else if ("userStatus".equals(type)) {
            this.userStatus=Integer.parseInt(value);
        } else {
            this.name=value;
        }
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getComputerIP() {
        return computerIP;
    }

    public void setComputerIP(String computerIP) {
        this.computerIP = computerIP;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public int getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(int accountStatus) {
        this.accountStatus = accountStatus;
    }

    public int getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(int userStatus) {
        this.userStatus = userStatus;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        if(("").equals(startDate)){ startDate = null;}
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        if(("").equals(endDate)){ endDate = null;}
        this.endDate = endDate;
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
