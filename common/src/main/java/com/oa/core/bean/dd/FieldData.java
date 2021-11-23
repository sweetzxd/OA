package com.oa.core.bean.dd;

import org.json.JSONObject;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Hashtable;

public class FieldData implements Serializable {
    private static final long serialVersionUID = 1L;

    //字段名 主键
    private String fieldName;

    //字段名称 汉字名
    private String fieldTitle;

    //字段类型
    private String fieldType;

    //字段长度 整数
    private int fieldNum;

    //字段长度 小数位数
    private int fieldDigit;

    //选项
    private String optionVal;

    //默认值
    private String defaultVal;

    //特殊配置
    private String special;

    //是否必填 0：不必填；1：必填
    private int required;
    //
    private String tableName;

    private int curStatus;
    private String recName;
    private Timestamp recTime;
    private String modName;
    private Timestamp modTime;
    private String delName;
    private Timestamp delTime;

    private int startRow;
    private int endRow;

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldTitle() {
        return fieldTitle;
    }

    public void setFieldTitle(String fieldTitle) {
        this.fieldTitle = fieldTitle;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public int getFieldNum() {
        return fieldNum;
    }

    public void setFieldNum(int fieldNum) {
        this.fieldNum = fieldNum;
    }

    public int getFieldDigit() {
        return fieldDigit;
    }

    public void setFieldDigit(int fieldDigit) {
        this.fieldDigit = fieldDigit;
    }

    public String getOptionVal() {
        return optionVal;
    }

    public void setOptionVal(String optionVal) {
        this.optionVal = optionVal;
    }

    public String getDefaultVal() {
        return defaultVal;
    }

    public void setDefaultVal(String defaultVal) {
        this.defaultVal = defaultVal;
    }

    public String getSpecial() {
        return special;
    }

    public void setSpecial(String special) {
        this.special = special;
    }

    public int getRequired() {
        return required;
    }

    public void setRequired(int required) {
        this.required = required;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public int getCurStatus() {
        return curStatus;
    }

    public void setCurStatus(int curStatus) {
        this.curStatus = curStatus;
    }

    public String getRecName() {
        return recName;
    }

    public void setRecName(String recName) {
        this.recName = recName;
    }

    public Timestamp getRecTime() {
        return recTime;
    }

    public void setRecTime(Timestamp recTime) {
        this.recTime = recTime;
    }

    public String getModName() {
        return modName;
    }

    public void setModName(String modName) {
        this.modName = modName;
    }

    public Timestamp getModTime() {
        return modTime;
    }

    public void setModTime(Timestamp modTime) {
        this.modTime = modTime;
    }

    public String getDelName() {
        return delName;
    }

    public void setDelName(String delName) {
        this.delName = delName;
    }

    public Timestamp getDelTime() {
        return delTime;
    }

    public void setDelTime(Timestamp delTime) {
        this.delTime = delTime;
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

        String field = "fieldName,fieldTitle,fieldType,fieldNum,optionVal,defaultVal,special,required,tableName" +
                ",curStatus,recName,recTime,modName,modTime,delName,delTime";
        ht.put("field",field);

        String fieldName = getStringValue(getFieldName());
        String fieldTitle = getStringValue(getFieldTitle());
        String fieldType = getStringValue(getFieldType());
        int fieldNum = getFieldNum();
        String optionVal = getStringValue(getOptionVal());
        String defaultVal = getStringValue(getDefaultVal());
        String special = getStringValue(getSpecial());
        int required = getRequired();
        String tableName = null;

        int curStatus = 2;
        String recName = getStringValue(getRecName());
        String recTime = getStringValue(getRecTime());
        String modName = getStringValue(getModName());
        String modTime = getStringValue(getModTime());
        String delName = getStringValue(getDelName());
        String delTime = getStringValue(getDelTime());

        String value = fieldName+","+fieldTitle+","+fieldType+","+fieldNum+","+optionVal+","+defaultVal+","+special+","+required+","+tableName+"" +
                ","+curStatus+","+recName+","+recTime+","+modName+","+modTime+","+delName+","+delTime;
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

    public void setJsonValue(JSONObject json){
        setFieldName((String)json.get("fieldName"));
        setFieldTitle((String)json.get("fieldTitle"));
        setFieldType((String)json.get("fieldType"));
        setFieldNum((int)json.get("fieldNum"));
        setFieldDigit((int)json.get("fieldDigit"));
        setOptionVal((String)json.get("optionVal"));
        setDefaultVal((String)json.get("defaultVal"));
        setSpecial((String)json.get("special"));
        setRequired((int)json.get("required"));
    }
    @Override
    public boolean equals(Object o) {
        if (o instanceof FieldData) {
            FieldData fieldData = (FieldData) o;
            return this.fieldName.equals(fieldData.fieldName)
                    && this.fieldTitle.equals(fieldData.fieldTitle)
                    && this.fieldType.equals(fieldData.fieldType);
        }
        return super.equals(o);
    }
    @Override
    public int hashCode() {
        int result = fieldName.hashCode();
        result = 31 * result + fieldTitle.hashCode();
        return result;
    }
}
