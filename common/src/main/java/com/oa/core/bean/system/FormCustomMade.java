package com.oa.core.bean.system;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Hashtable;

public class FormCustomMade implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 页面ID
     */
    private String formcmName;
    /**
     * 页面名称
     */
    private String formcmTitle;
    /**
     * 表单任务
     */
    private String formTask;
    /**
     * 列表任务
     */
    private String listTask;
    /**
     * 所属模块
     */
    private String module;
    /**
     * 页面路径
     */
    private String editPage;
    /**
     * 发布状态
     */
    private int pageRelease;
    /**
     * 页面类型（1：表单页面；2：流程页面；3：外部引入；4：子表页面）
     */
    private int formType=1;

    private int curStatus;

    private String recordName;
    private Timestamp recordTime;
    private String modifyName;
    private Timestamp modifyTime;
    private String deleteName;
    private Timestamp deleteTime;

    private String pageId;
    private int startRow;
    private int endRow;

    public String getFormcmName() {
        return formcmName;
    }

    public void setFormcmName(String formcmName) {
        this.formcmName = formcmName;
    }

    public String getFormcmTitle() {
        return formcmTitle;
    }

    public void setFormcmTitle(String formcmTitle) {
        this.formcmTitle = formcmTitle;
    }

    public String getFormTask() {
        return formTask;
    }

    public void setFormTask(String formTask) {
        this.formTask = formTask;
    }

    public String getListTask() {
        return listTask;
    }

    public void setListTask(String listTask) {
        this.listTask = listTask;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getEditPage() {
        return editPage;
    }

    public void setEditPage(String editPage) {
        this.editPage = editPage;
    }

    public int getPageRelease() {
        return pageRelease;
    }

    public void setPageRelease(int pageRelease) {
        this.pageRelease = pageRelease;
    }

    public int getFormType() {
        return formType;
    }

    public void setFormType(int formType) {
        this.formType = formType;
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

    public String getPageId() {
        return pageId;
    }

    public void setPageId(String pageId) {
        this.pageId = pageId;
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

        String field = "formcmName,formcmTitle,formTask,listTask,module,editPage,pageRelease,formType" +
                ",curStatus,recordName,recordTime,modifyName,modifyTime,deleteName,deleteTime";
        ht.put("field",field);

        String formcmName = getStringValue(getFormcmName());
        String formcmTitle = getStringValue(getFormcmTitle());
        String formTask = getStringValue(getFormTask());
        String listTask = getStringValue(getListTask());
        String module = getStringValue(getModule());
        String editPage = getStringValue(getEditPage());
        int pageRelease = getPageRelease();
        int formType = getFormType();

        int curStatus = 2;
        String recordName = getStringValue(getRecordName());
        String recordTime = getStringValue(getRecordTime());
        String modifyName = getStringValue(getModifyName());
        String modifyTime = getStringValue(getModifyTime());
        String deleteName = getStringValue(getDeleteName());
        String deleteTime = getStringValue(getDeleteTime());

        String value = formcmName+","+formcmTitle+","+formTask+","+listTask+","+module+","+editPage+","+pageRelease+","+formType+"" +
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
