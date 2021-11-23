package com.oa.core.bean.module;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @ClassName:Employees
 * @author:zxd
 * @Date:2018/10/13
 * @Time:下午 2:37
 * @Version V1.0
 * @Explain 员工信息表
 */
public class Employees implements Serializable {

    /**
     * 基本信息
     *
     * 员工编号
     * */
    private String staffId;
    /**
     * 员工姓名
     * */
    private String staffName;
    /**
     * 员工性别
     * */
    private String sex;
    /**
     * 出生日期
     * */
    private String dateOfBirth=null;
    /**
     * 员工年龄
     * */
    private int age;
    /**
     * 民族
     * */
    private String nations;
    /**
     * 婚否
     * */
    private String married;
    /**
     * 籍贯
     * */
    private String nativePlace;
    /**
     * 住址
     * */
    private String address;
    /**
     * 联系电话
     * */
    private String phone;

    /**
     * 座机
     * */
    private String telephone;
    /**
     * 邮箱
     * */
    private String mailbox;
    /**
     * 头像
     * */
    private String photo;
    /**
     * 备注
     * */
    private String remark;

    /**
     * 工作信息
     *
     * 所属部门
     * */
    private String department;
    /**
     * 工作岗位
     * */
    private String post;
    /**
     * 入职日期
     * */
    private String hiredate=null;
    /**
     * 合同到期
     * */
    private String contractsExpire=null;
    /**
     * 离职日期
     * */
    private String leaveDate=null;
    /**
     * 登陆账号
     * 暂时默认姓名全拼
     * */
    private String userName;


    private int curStatus;
    private String recordName;
    private Timestamp recordTime;
    private String modifyName;
    private Timestamp modifyTime;
    private String deleteName;
    private Timestamp deleteTime;

    private int startRow;
    private int endRow;


    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        if(dateOfBirth==""){dateOfBirth=null;}
        this.dateOfBirth = dateOfBirth;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getNations() {
        return nations;
    }

    public void setNations(String nations) {
        this.nations = nations;
    }

    public String getMarried() {
        return married;
    }

    public void setMarried(String married) {
        this.married = married;
    }

    public String getNativePlace() {
        return nativePlace;
    }

    public void setNativePlace(String nativePlace) {
        this.nativePlace = nativePlace;
    }

    public String getAddress() {
        return address;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMailbox() {
        return mailbox;
    }

    public void setMailbox(String mailbox) {
        this.mailbox = mailbox;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getHiredate() {
        return hiredate;
    }

    public void setHiredate(String hiredate) {
        if(hiredate==""){hiredate=null;}
        this.hiredate = hiredate;
    }

    public String getContractsExpire() {
        return contractsExpire;
    }

    public void setContractsExpire(String contractsExpire) {
        if(contractsExpire==""){contractsExpire=null;}
        this.contractsExpire = contractsExpire;
    }

    public String getLeaveDate() {
        return leaveDate;
    }

    public void setLeaveDate(String leaveDate) {
        if(leaveDate==""){leaveDate=null;}
        this.leaveDate = leaveDate;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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
