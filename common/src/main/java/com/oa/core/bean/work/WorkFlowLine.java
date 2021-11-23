package com.oa.core.bean.work;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Hashtable;

/**
 * @ClassName:WorkFlowLine
 * @author:zxd
 * @Date:2018/09/20
 * @Time:上午 11:26
 * @Version V1.0
 * @Explain 流程定义- 线定义表
 */
public class WorkFlowLine implements Serializable {

    /**
     * 线ID
     */
    private String pathId;
    /**
     * 线标题
     */
    private String pathTitle;
    /**
     * 流程定义号
     */
    private String wkflwId;
    /**
     * 线开始节点
     */
    private String fromNode;
    /**
     * 线结束节点
     */
    private String toNode;
    /**
     * 待会合路径
     */
    private int isJunction;
    /**
     * 判断单元形式（1：无条件发送；2：表达式为真；3：多个表达式为真；4：任一表达式为真）
     */
    private int logUnitType;
    /**
     * 判断单元参数
     */
    private String logUnitParams;
    /**
     * 多人判断条件方式（0：只需本人的填写符合本条件；1：只需一人的填写符合本条件；2：需所有人的填写符合本条件）
     */
    private int logTypeOfByMultActor;
    /**
     * 是退回/返回原发件人的路径
     */
    private int isRebackPath;
    /**
     * 限定语句
     */
    private String sqlFeild;
    /**
     * 所有人提交后才开始判断处理
     */
    private int judgeAfterAll;
    /**
     * 路径通时对其他同批任务的处理（0：撤消其他人的任务；1：其他人的任务继续）
     */
    private int path1ToOtherTask;
    /**
     * 是否是并行路径
     */
    private int isAsynchPath;

    private int curStatus;
    private String recordName;
    private Timestamp recordTime;
    private String modifyName;
    private Timestamp modifyTime;
    private String deleteName;
    private Timestamp deleteTime;

    private int startRow;
    private int endRow;

    public String getPathId() {
        return pathId;
    }

    public void setPathId(String pathId) {
        this.pathId = pathId;
    }

    public String getPathTitle() {
        return pathTitle;
    }

    public void setPathTitle(String pathTitle) {
        this.pathTitle = pathTitle;
    }

    public String getWkflwId() {
        return wkflwId;
    }

    public void setWkflwId(String wkflwId) {
        this.wkflwId = wkflwId;
    }

    public String getFromNode() {
        return fromNode;
    }

    public void setFromNode(String fromNode) {
        this.fromNode = fromNode;
    }

    public String getToNode() {
        return toNode;
    }

    public void setToNode(String toNode) {
        this.toNode = toNode;
    }

    public int getIsJunction() {
        return isJunction;
    }

    public void setIsJunction(int isJunction) {
        this.isJunction = isJunction;
    }

    public int getLogUnitType() {
        return logUnitType;
    }

    public void setLogUnitType(int logUnitType) {
        this.logUnitType = logUnitType;
    }

    public String getLogUnitParams() {
        return logUnitParams;
    }

    public void setLogUnitParams(String logUnitParams) {
        this.logUnitParams = logUnitParams;
    }

    public int getLogTypeOfByMultActor() {
        return logTypeOfByMultActor;
    }

    public void setLogTypeOfByMultActor(int logTypeOfByMultActor) {
        this.logTypeOfByMultActor = logTypeOfByMultActor;
    }

    public int getIsRebackPath() {
        return isRebackPath;
    }

    public void setIsRebackPath(int isRebackPath) {
        this.isRebackPath = isRebackPath;
    }

    public String getSqlFeild() {
        return sqlFeild;
    }

    public void setSqlFeild(String sqlFeild) {
        this.sqlFeild = sqlFeild;
    }

    public int getJudgeAfterAll() {
        return judgeAfterAll;
    }

    public void setJudgeAfterAll(int judgeAfterAll) {
        this.judgeAfterAll = judgeAfterAll;
    }

    public int getPath1ToOtherTask() {
        return path1ToOtherTask;
    }

    public void setPath1ToOtherTask(int path1ToOtherTask) {
        this.path1ToOtherTask = path1ToOtherTask;
    }

    public int getIsAsynchPath() {
        return isAsynchPath;
    }

    public void setIsAsynchPath(int isAsynchPath) {
        this.isAsynchPath = isAsynchPath;
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

        String field = "pathId,pathTitle,wkflwId,fromNode,toNode,isJunction,logUnitType,logUnitParams,logTypeOfByMultActor,isRebackPath,sqlFeild,judgeAfterAll,path1ToOtherTask,isAsynchPath" +
                ",curStatus,recordName,recordTime,modifyName,modifyTime,deleteName,deleteTime";
        ht.put("field",field);

        String pathId = getStringValue(getPathId());
        String pathTitle = getStringValue(getPathTitle());
        String wkflwId = getStringValue(getWkflwId());
        String fromNode = getStringValue(getFromNode());
        String toNode = getStringValue(getToNode());
        int isJunction = getIsJunction();
        int logUnitType = getLogUnitType();
        String logUnitParams = getStringValue(getLogUnitParams());
        int logTypeOfByMultActor = getLogTypeOfByMultActor();
        int isRebackPath = getIsRebackPath();
        String sqlFeild = getStringValue(getSqlFeild());
        int judgeAfterAll = getJudgeAfterAll();
        int path1ToOtherTask = getPath1ToOtherTask();
        int isAsynchPath = getIsAsynchPath();

        int curStatus = 2;
        String recordName = getStringValue(getRecordName());
        String recordTime = getStringValue(getRecordTime());
        String modifyName = getStringValue(getModifyName());
        String modifyTime = getStringValue(getModifyTime());
        String deleteName = getStringValue(getDeleteName());
        String deleteTime = getStringValue(getDeleteTime());

        String value = pathId+","+pathTitle+","+wkflwId+","+fromNode+","+toNode+","+isJunction+","+logUnitType+","+logUnitParams+","+logTypeOfByMultActor+","+isRebackPath+","+sqlFeild+","+judgeAfterAll+","+path1ToOtherTask+","+isAsynchPath+"" +
                ","+curStatus+","+recordName+","+recordTime+","+modifyName+","+modifyTime+","+deleteName+","+deleteTime;
        ht.put("value",value);
        return ht;
    }

    public String getStringValue(Object val){
        if(val!=null && !val.equals("")){
            return "'"+val+"'";
        }else{
            return null;
        }
    }
}
