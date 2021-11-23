package com.oa.core.bean.work;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Hashtable;

/**
 * @ClassName:WorkFlowNode
 * @author:zxd
 * @Date:2018/09/20
 * @Time:上午 11:25
 * @Version V1.0
 * @Explain 流程定义-节点定义
 */
public class WorkFlowNode implements Serializable {

    /**
     * 节点ID
     */
    private String nodeID;
    /**
     * 节点名称
     */
    private String nodeTitle;
    /**
     * 是否为表单页面
     */
    private int needFormPage;
    /**
     * 页面关联键
     */
    private String formId;
    /**
     * 流程关联键
     */
    private String wkflwID;
    /**
     * 节点位置 （1：首节点；2：中间节点；3：结束节点）
     */
    private int nodePosition;
    /**
     * 预警天数
     */
    private int maxWorkHouses;
    /**
     * 级别（0：最低，1：较低，2：低，3：普通，4：高，5：较高，6：最高）
     */
    private int stress;
    /**
     * 工作类别
     */
    private String workType;
    /**
     * 使用标准查验判断页面
     */
    private int checkPageType;
    /**
     * 查验判断所需表单页面
     */
    private String showInfoPages;
    /**
     * 标准页面选项字段（同意，需修改，不同意）
     */
    private String flwNodeOpt;
    /**
     * 自动加入日程安排
     */
    private int toSchedule;
    /**
     * 允许接受人移交任务
     */
    private int allowOtherDoit;
    /**
     * 预警内容
     */
    private String warnContent;
    /**
     * 向本节点所有角色发送
     */
    private int isParellel;
    /**
     * 本节点只需一人接受并执行(0:否，1：是)
     */
    private int isORAction;
    /**
     * 对每一个执行角色按顺序依次执行
     */
    private int actor1ByOne;
    /**
     * 单一执行角色中只需一人处理
     */
    private int eachType1Only;
    /**
     * 记录密级（0：公开，1：保密，2：机密，3：绝密）
     */
    private int security;
    /**
     * 提醒人（[节点处理人]，[流程发起人]，[运行管理员]）
     */
    private String reminder;
    /**
     * 提醒周期
     */
    private double remindPeriod;
    /**
     * 提醒类型（系统信息，手机短信）
     */
    private String remindType;
    /**
     * 提醒提前值
     */
    private double remindTimeValue;

    private int curStatus;
    private String recordName;
    private Timestamp recordTime;
    private String modifyName;
    private Timestamp modifyTime;
    private String deleteName;
    private Timestamp deleteTime;

    private int startRow;
    private int endRow;

    public String getNodeID() {
        return nodeID;
    }

    public void setNodeID(String nodeID) {
        this.nodeID = nodeID;
    }

    public String getNodeTitle() {
        return nodeTitle;
    }

    public void setNodeTitle(String nodeTitle) {
        this.nodeTitle = nodeTitle;
    }

    public int getNeedFormPage() {
        return needFormPage;
    }

    public void setNeedFormPage(int needFormPage) {
        this.needFormPage = needFormPage;
    }

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public String getWkflwID() {
        return wkflwID;
    }

    public void setWkflwID(String wkflwID) {
        this.wkflwID = wkflwID;
    }

    public int getNodePosition() {
        return nodePosition;
    }

    public void setNodePosition(int nodePosition) {
        this.nodePosition = nodePosition;
    }

    public int getMaxWorkHouses() {
        return maxWorkHouses;
    }

    public void setMaxWorkHouses(int maxWorkHouses) {
        this.maxWorkHouses = maxWorkHouses;
    }

    public int getStress() {
        return stress;
    }

    public void setStress(int stress) {
        this.stress = stress;
    }

    public String getWorkType() {
        return workType;
    }

    public void setWorkType(String workType) {
        this.workType = workType;
    }

    public int getCheckPageType() {
        return checkPageType;
    }

    public void setCheckPageType(int checkPageType) {
        this.checkPageType = checkPageType;
    }

    public String getShowInfoPages() {
        return showInfoPages;
    }

    public void setShowInfoPages(String showInfoPages) {
        this.showInfoPages = showInfoPages;
    }

    public String getFlwNodeOpt() {
        return flwNodeOpt;
    }

    public void setFlwNodeOpt(String flwNodeOpt) {
        this.flwNodeOpt = flwNodeOpt;
    }

    public int getToSchedule() {
        return toSchedule;
    }

    public void setToSchedule(int toSchedule) {
        this.toSchedule = toSchedule;
    }

    public int getAllowOtherDoit() {
        return allowOtherDoit;
    }

    public void setAllowOtherDoit(int allowOtherDoit) {
        this.allowOtherDoit = allowOtherDoit;
    }

    public String getWarnContent() {
        return warnContent;
    }

    public void setWarnContent(String warnContent) {
        this.warnContent = warnContent;
    }

    public int getIsParellel() {
        return isParellel;
    }

    public void setIsParellel(int isParellel) {
        this.isParellel = isParellel;
    }

    public int getIsORAction() {
        return isORAction;
    }

    public void setIsORAction(int isORAction) {
        this.isORAction = isORAction;
    }

    public int getActor1ByOne() {
        return actor1ByOne;
    }

    public void setActor1ByOne(int actor1ByOne) {
        this.actor1ByOne = actor1ByOne;
    }

    public int getEachType1Only() {
        return eachType1Only;
    }

    public void setEachType1Only(int eachType1Only) {
        this.eachType1Only = eachType1Only;
    }

    public int getSecurity() {
        return security;
    }

    public void setSecurity(int security) {
        this.security = security;
    }

    public String getReminder() {
        return reminder;
    }

    public void setReminder(String reminder) {
        this.reminder = reminder;
    }

    public double getRemindPeriod() {
        return remindPeriod;
    }

    public void setRemindPeriod(double remindPeriod) {
        this.remindPeriod = remindPeriod;
    }

    public String getRemindType() {
        return remindType;
    }

    public void setRemindType(String remindType) {
        this.remindType = remindType;
    }

    public double getRemindTimeValue() {
        return remindTimeValue;
    }

    public void setRemindTimeValue(double remindTimeValue) {
        this.remindTimeValue = remindTimeValue;
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


    public Hashtable<String,String> getSqlField(){
        Hashtable<String,String> ht = new Hashtable<>();

        String field = "nodeID,nodeTitle,needFormPage,formId,wkflwID,nodePosition,maxWorkHouses,stress,workType,checkPageType,showInfoPages,flwNodeOpt,toSchedule,allowOtherDoit,warnContent,isParellel,isORAction,actor1ByOne,eachType1Only,security,reminder,remindPeriod,remindType,remindTimeValue" +
                ",curStatus,recordName,recordTime,modifyName,modifyTime,deleteName,deleteTime";
        ht.put("field",field);

        String nodeID  = getStringValue(getNodeID());
        String nodeTitle  = getStringValue(getNodeTitle());
        int needFormPage = getNeedFormPage();
        String formId  = getStringValue(getFormId());
        String wkflwID  = getStringValue(getWkflwID());
        int nodePosition = getNodePosition();
        int maxWorkHouses = getMaxWorkHouses();
        int stress = getStress();
        String workType  = getStringValue(getWorkType());
        int checkPageType = getCheckPageType();
        String showInfoPages  = getStringValue(getShowInfoPages());
        String flwNodeOpt  = getStringValue(getFlwNodeOpt());
        int toSchedule = getToSchedule();
        int allowOtherDoit = getAllowOtherDoit();
        String warnContent  = getStringValue(getWarnContent());
        int isParellel = getIsParellel();
        int isORAction = getIsORAction();
        int actor1ByOne = getActor1ByOne();
        int eachType1Only = getEachType1Only();
        int security = getSecurity();
        String reminder  = getStringValue(getReminder());
        double remindPeriod = getRemindPeriod();
        String remindType  = getStringValue(getRemindType());
        double remindTimeValue = getRemindTimeValue();

        int curStatus = 2;
        String recordName = getStringValue(getRecordName());
        String recordTime = getStringValue(getRecordTime());
        String modifyName = getStringValue(getModifyName());
        String modifyTime = getStringValue(getModifyTime());
        String deleteName = getStringValue(getDeleteName());
        String deleteTime = getStringValue(getDeleteTime());

        String value = nodeID+","+nodeTitle+","+needFormPage+","+formId+","+wkflwID+","+nodePosition+","+maxWorkHouses+","+stress+","+workType+","+checkPageType+","+showInfoPages+"" +
                ","+flwNodeOpt+","+toSchedule+","+allowOtherDoit+","+warnContent+","+isParellel+","+isORAction+","+actor1ByOne+"" +
                ","+eachType1Only+","+security+","+reminder+","+remindPeriod+","+remindType+","+remindTimeValue+"" +
                ","+curStatus+","+recordName+","+recordTime+","+modifyName+","+modifyTime+","+deleteName+","+deleteTime;
        ht.put("value",value);
        return ht;
    }

    public String getStringValue(Object val){
        if(val!=null){
            return "'"+val+"'";
        }else{
            return null;
        }
    }
}
