package com.oa.core.util;

import com.oa.core.bean.dd.FieldData;
import com.oa.core.helper.DateHelper;
import com.oa.core.helper.StringHelper;
import com.oa.core.listener.InitDataListener;
import com.oa.core.service.dd.DictionaryService;
import com.oa.core.service.util.TableService;
import org.junit.Test;

import java.sql.Timestamp;
import java.util.*;

public class MySqlUtil {

    public static String getField(FieldData fieldData) {
        String fieldName = fieldData.getFieldName();
        String fieldTitle = fieldData.getFieldTitle();
        int fieldNum = fieldData.getFieldNum();
        String type = fieldData.getFieldType();
        String fieldsql = "";
        fieldsql += "`" + fieldName + "` ";
        //判断是否是时间类型、decimal类型
        if ("timestamp".equals(type) || "date".equals(type) || "datetime".equals(type)) {
            fieldsql += getSqlType(fieldData.getFieldType()) + " ";
        } else if ("decimal".equals(type)) {
            fieldsql += getSqlType(type) + "(" + fieldNum + "," + fieldData.getFieldDigit() + ")";
        } else {
            fieldsql += getSqlType(type) + "(" + fieldNum + ")";
        }
        if (getAttribute(type)) {
            fieldsql += " DEFAULT NULL COMMENT '" + fieldTitle + "',";
        } else {
            fieldsql += " CHARACTER SET utf8 DEFAULT NULL COMMENT '" + fieldTitle + "',";
        }
        return fieldsql;
    }

    public static boolean getAttribute(String type) {
        String types = "int;float;double;decimal;date;datetime;time";
        Vector<String> vt = StringHelper.string2Vector(types, ";");
        if (vt.contains(type)) {
            return true;
        } else {
            return false;
        }
    }

    public static String getSqlType(String type) {
        String sqltype = "";
        if ("time".equals(type)) {
            sqltype = "timestamp";
        } else if ("date".equals(type)) {
            sqltype = "date";
        } else if ("datetime".equals(type)) {
            sqltype = "datetime";
        } else if ("int".equals(type)) {
            sqltype = "int";
        } else if ("float".equals(type)) {
            sqltype = "float";
        } else if ("double".equals(type)) {
            sqltype = "double";
        } else if ("decimal".equals(type)) {
            sqltype = "decimal";
        } else {
            sqltype = "varchar";
        }
        return sqltype;
    }

    public static String getCreateSql(String tableName, String tableTitle, List<FieldData> fieldDataList) {
        FieldData fieldData = new FieldData();
        fieldData.setTableName(tableName);
        String create = "CREATE TABLE `" + tableName + "` (";
        create += "`recorderNO` varchar(30) NOT NULL,";
        for (int i = 0; i < fieldDataList.size(); i++) {
            create += getField(fieldDataList.get(i));
        }
        create += "`reserveField` varchar(200) CHARACTER SET utf8 DEFAULT NULL COMMENT '备用字段',";
        create += "`linkRecorderNO` varchar(30) CHARACTER SET utf8 DEFAULT NULL COMMENT '关联字段',";
        create += "`curStatus` int(2) DEFAULT '2' COMMENT '状态字段（0：删除，2：正常）',";
        create += "`workflowProcID` varchar(30) CHARACTER SET utf8 DEFAULT NULL COMMENT '流程运行号',";
        create += "`workflowNodeID` varchar(30) CHARACTER SET utf8 DEFAULT NULL COMMENT '流程节点ID',";
        create += "`recordName` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '创建人',";
        create += "`recordTime` datetime DEFAULT NULL COMMENT '创建时间',";
        create += "`modifyName` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '修改人',";
        create += "`modifyTime` datetime DEFAULT NULL COMMENT '修改时间',";
        create += "`deleteName` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '删除人',";
        create += "`deleteTime` datetime DEFAULT NULL COMMENT '删除时间',";
        create += "PRIMARY KEY (`recorderNO`)";
        create += ") ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT = '" + tableTitle + "';";
        return create;
    }


    public static String getAlterSql(String fieldName) {
        DictionaryService dictionaryService = (DictionaryService) SpringContextUtil.getBean("dictionaryService");
        FieldData fieldData = dictionaryService.selectFieldName(fieldName);
        String tableName = fieldData.getTableName();
        return getAlterSql(tableName, fieldData);
    }


    public static String getAlterSql(String tableName, FieldData fieldData) {
        String create = "ALTER TABLE " + tableName + " ADD ";
        String fieldName = fieldData.getFieldName();
        String fieldTitle = fieldData.getFieldTitle();
        String fieldType = MySqlUtil.getSqlType(fieldData.getFieldType());
        int fieldNum = fieldData.getFieldNum();
        //判断是否是时间类型、decimal类型
        if ("timestamp".equals(fieldType) || "date".equals(fieldType) || "datetime".equals(fieldType)) {
            create += "" + fieldName + " " + fieldType + " ";
        } else if ("decimal".equals(fieldType)) {
            create += "" + fieldName + " " + fieldType + "(" + fieldNum + "," + fieldData.getFieldDigit() + ")";
        } else {
            create += "" + fieldName + " " + fieldType + "(" + fieldNum + ")";
        }
        if (getAttribute(fieldType)) {
            create += " DEFAULT NULL COMMENT '" + fieldTitle + "' AFTER `linkRecorderNO`";
        } else {
            create += " CHARACTER SET utf8 DEFAULT NULL COMMENT '" + fieldTitle + "' AFTER `linkRecorderNO`";
        }
        return create;
    }

    public static String getSql(List<String> fields, String table) {
        return getSql(fields, table, null);
    }

    public static String getSql(List<String> fields, String table, List<String> where) {
        String order = "order by recordTime desc";
        return getSql(fields, table, where, order);
    }

    /**
     * 生成 根据条件有排序查询的sql语句
     *
     * @param field 字段
     * @param table 表名
     * @param where 条件
     * @param order 排序
     * @return sql 生成的sql语句
     */
    public static String getSql(List<String> field, String table, List<String> where, String order) {
        return getSql(field, table, where, order, "");
    }

    /**
     * 生成 根据条件有排序查询的sql语句
     *
     * @param field 字段
     * @param table 表名
     * @param where 条件
     * @param order 排序
     * @return sql 生成的sql语句
     */
    public static String getSql(List<String> field, String table, List<String> where, String order, String page) {
        String limit;
        int startRow = 0;
        if (page != null) {
            if (page.equals("")) {
                limit = "";
            } else {
                startRow = Integer.parseInt(page) * 10;
                limit = " limit " + startRow + " , " + 10;
            }
        } else {
            limit = " limit " + 0 + " , " + 10;
        }

        String fields = "";
        if (field != null) {
            for (String f : field) {
                fields += "ifnull(" + f + ",'') AS " + f + "" + ",";
            }
        }
        String wheres = "";
        if (where != null) {
            for (String w : where) {
                wheres += " and " + w + " ";
            }
        }
        String sql = "select (@i:=@i+1) num,recorderNO, " + fields + " reserveField,linkRecorderNO,recordName,useridtoname(recordName) as recordName_name,recordTime,modifyName,useridtoname(modifyName) as modifyName_name,modifyTime from " + table + ",(SELECT @i:=" + startRow + ") as i where curStatus=2 " + wheres + " " + order + " " + limit;
        LogUtil.sysLog("SQL:" + sql);
        return sql;
    }

    public static String getUpdateSql(String table, Map<String, Object> field, List<String> where) {
        return getUpdateSql(table, null, field, where);
    }

    /**
     * 生成 根据条件执行修改的sql语句
     *
     * @param table 表名
     * @param recno 主键
     * @param field 字段
     * @param where 条件
     * @return sql 生成的sql语句
     */
    public static String getUpdateSql(String table, String recno, Map<String, Object> field, List<String> where) {
        String fields = "";
        if (field != null) {
            Iterator entries = field.entrySet().iterator();
            while (entries.hasNext()) {
                Map.Entry entry = (Map.Entry) entries.next();
                String key = (String) entry.getKey();
                Object value = entry.getValue();
                if (value == null || value.equals("")) {
                    fields += key + " = null,";
                } else {
                    fields += key + "='" + value + "',";
                }
            }
        }
        String wheres = "";
        if (recno != null && !recno.equals("")) {
            wheres += "AND recorderNO='" + recno + "'";
        }
        if (where != null) {
            for (String w : where) {
                wheres += " AND " + w + " ";
            }
        }
        String sql = " UPDATE " + table + " SET " + fields + " modifyTime='" + DateHelper.now() + "' WHERE curStatus=2 " + wheres;
        LogUtil.sysLog("SQL:" + sql);
        return sql;
    }

    /**
     * 生成 执行添加的sql语句
     *
     * @param table 表名
     * @param field 字段
     * @return sql 生成的sql语句
     */
    public static String getInsertSql(String table, Map<String, Object> field) {
        String fields = "";
        String values = "";
        if (field != null) {
            Iterator entries = field.entrySet().iterator();
            while (entries.hasNext()) {
                Map.Entry entry = (Map.Entry) entries.next();
                String key = (String) entry.getKey();
                Object value = entry.getValue();
                if (value != null && value != "") {
                    fields += key + ",";
                    if(value==null || value.equals("")){
                        values += " null ,";
                    }else {
                        values += "'" + value + "',";
                    }
                }
            }
            if (fields.length() > 1) {
                fields = fields.substring(0, fields.length() - 1);
            }
            if (values.length() > 1) {
                values = values.substring(0, values.length() - 1);
            }
        }
        String sql = "INSERT INTO " + table + "(" + fields + ") VALUES (" + values + ")";
        LogUtil.sysLog("sql:" + sql);
        return sql;
    }

    /**
     * 生成 执行修改的sql语句
     *
     * @param table 表名
     * @param field 字段
     * @return sql 生成的sql语句
     */
    public static String getUpdateSql(String table, Map<String, Object> field) {
       /* String fields = "";
        String values = "";*/
        String workflowProcID = "";
        String sql = "update " + table + " set ";
        if (field != null) {
            Iterator entries = field.entrySet().iterator();
            while (entries.hasNext()) {
                Map.Entry entry = (Map.Entry) entries.next();
                String key = (String) entry.getKey();
                Object value = entry.getValue();
                if (value != null && value != "") {
                    /*fields += key + ",";
                    values += "'" + value + "',";*/
                    if ("workflowProcID".equals(key)) {
                        workflowProcID = "" + value;
                    }
                    if ("recorderNO".equals(key)) {
                        continue;
                    } else {
                        if (value == null || value.equals("")) {
                            sql += " " + key + " = null,";
                        } else {
                            sql += " " + key + " = '" + value + "',";
                        }
                        //sql += " " + key + " = '" + value + "', ";
                    }
                }
            }
        }
        sql += "modifyTime='" + DateHelper.now() + "'";
        sql += " where workflowProcID=" + "'" + workflowProcID + "'";
        LogUtil.sysLog("sql:" + sql);
        return sql;
    }

    /**
     * 生成 根据条件执行删除的sql语句
     *
     * @param table 表名
     * @param field 修改的字段
     * @param recno 主键
     * @return sql 生成的sql语句
     */
    public static String getDeleteSql(String table, Map<String, Object> field, String recno) {
        String fields = "";
        String values = "";
        if (field != null) {
            Iterator entries = field.entrySet().iterator();
            while (entries.hasNext()) {
                Map.Entry entry = (Map.Entry) entries.next();
                String key = (String) entry.getKey();
                Object value = entry.getValue();
                fields += key + "='" + value + "',";
            }
        }
        String sql = "UPDATE " + table + " SET " + fields + " curStatus=0  WHERE recorderNO IN('" + recno + "') AND curStatus=2 ";
        LogUtil.sysLog("sql:" + sql);
        return sql;
    }

    public static String getDeleteSql(String table, List<String> field, List<String> where, String recno) {

        String fields = "";
        if (field != null) {
            for (String w : field) {
                fields += w + ", ";
            }
        }
        String wheres = "";
        if (recno != null && !recno.equals("")) {
            wheres += "AND recorderNO='" + recno + "'";
        }
        if (where != null) {
            for (String w : where) {
                wheres += " AND " + w + " ";
            }
        }
        String sql = "UPDATE " + table + " SET " + fields + " curStatus=0  WHERE curStatus=2 " + wheres;
        LogUtil.sysLog("sql:" + sql);
        return sql;
    }

    /**
     * 生成 根据条件进行查询的sql语句
     *
     * @param field 字段名
     * @param table 表名
     * @param where 查询条件
     * @return sql 生成的sql语句
     */
    public static String getFieldSql(String field, String table, List<String> where) {
        return getFieldSql(field, table, where, null);
    }

    public static String getFieldSql(Vector<String> field, String table, List<String> where, String order) {
        String fields = "";
        if (field != null && field.size() > 0) {

            fields = StringHelper.vector2SqlField(field, ",");
            if (!field.contains("recordName")) {
                fields = "recordName,recordTime" + fields;
            }
        }
        return getFieldSql(fields, table, where, order);
    }

    public static String getFieldSql(String field, String table, List<String> where, String order) {
        String wheres = "";
        if (where != null) {
            for (String w : where) {
                wheres += " and " + w + " ";
            }
        }
        String orders = "";
        if (order != null) {
            orders = "order by " + order;
        }
        String sql = "select " + field + " from " + table + " where curStatus=2 " + wheres + " " + orders;
        return sql;
    }

    public static String getFieldSql2(Vector<String> field, String table, List<String> where, String orders, String page) {

        String limit;
        int startRow = 0;
        if (page != null) {
            if (page.equals("")) {
                limit = "";
            } else {
                startRow = Integer.parseInt(page) * 10;
                limit = " limit " + startRow + " , " + 10;
            }
        } else {
            limit = " limit " + 0 + " , " + 10;
        }
        return getFieldSql3(field, table, where, orders, limit);
    }

    public static String getFieldSql3(Vector<String> field, String table, List<String> where, String orders, String limit) {
        String fields = "";
        if (field != null && field.size() > 0) {
            for (String f : field) {
                fields += "ifnull(" + f + ",'') AS " + f + "" + ",";
            }
        }
        String wheres = "";
        if (where != null) {
            for (String w : where) {
                wheres += " and " + w + " ";
            }
        }

        String sql = "select (@i:=@i+1) num ,recorderNO," + fields + " reserveField,linkRecorderNO,recordName,useridtoname(recordName) as recordName_name,recordTime,modifyName,useridtoname(modifyName) as modifyName_name,modifyTime from " + table + ",(SELECT @i:=0) as i where curStatus=2 " + wheres + " " + orders + " " + limit;
        return sql;
    }

    public static String getFieldSql3(String pkid, Vector<String> field, String table, List<String> where, String orders, String limit) {
        String fields = "";
        if (field != null && field.size() > 0) {
            for (String f : field) {
                fields += "ifnull(" + f + ",'') AS " + f + "" + ",";
            }
        }
        String wheres = "";
        if (where != null) {
            for (String w : where) {
                wheres += " and " + w + " ";
            }
        }
        if (pkid != null && !pkid.equals("")) {
            pkid = pkid + " , ";
        }
        String sql = "select (@i:=@i+1) num ," + pkid + fields + " reserveField,linkRecorderNO,recordName,useridtoname(recordName) as recordName_name,recordTime,modifyName,useridtoname(modifyName) as modifyName_name,modifyTime from " + table + ",(SELECT @i:=0) as i where curStatus=2 " + wheres + " " + orders + " " + limit;
        return sql;
    }

    /**
     * 生成 根据条件进行查询的sql语句
     *
     * @param field    字段名
     * @param table    表名
     * @param where    查询条件
     * @param startRow 翻页开始
     * @param endRow   翻页结束
     * @return sql 生成的sql语句
     */
    public static String getFieldSql(Vector<String> field, String table, List<String> where, int startRow, int endRow) {
        String fields = "";
        if (field != null && field.size() > 0) {
            for (String f : field) {
                fields += "ifnull(" + f + ",'') AS " + f + "" + ",";
            }
        }
        String wheres = "";
        if (where != null) {
            for (String w : where) {
                wheres += " and " + w + " ";
            }
        }
        String sql = "select recorderNO, " + fields + " reserveField from " + table + " where curStatus=2 " + wheres;
        if (startRow >= 0 && endRow > 0) {
            sql += " limit " + startRow + " , " + endRow;
        }
        return sql;
    }

    /**
     * 生成 根据条件查询数量的sql
     *
     * @param table 表名
     * @param where 查询条件
     * @return sql 生成的sql语句
     */
    public static String getCountSql(String table, List<String> where) {
        String wheres = "";
        if (where != null) {
            for (String w : where) {
                wheres += " and " + w + " ";
            }
        }
        String sql = "select count(*) from " + table + " where curStatus=2 " + wheres;
        return sql;
    }

    public static String getNameSql(Vector<String> field, String table, List<String> where, int startRow, int endRow) {
        return getNameSql(field, table, where, null, startRow, endRow);
    }

    public static String getNameSql(Vector<String> field, String table, List<String> where, String order, int startRow, int endRow) {
        TableService t = (TableService) SpringContextUtil.getBean("tableService");
        List<Map<String, Object>> fe = t.selectSqlMapList("select fieldName,fieldType from FieldData where fieldName in ('" + StringHelper.vector2String(field, "','") + "') and curStatus=2");
        HashMap<String, String> hm = new HashMap();
        for (Map<String, Object> map : fe) {
            hm.put((String) map.get("fieldName"), (String) map.get("fieldType"));
        }
        String fields = "";
        if (field != null && field.size() > 0) {
            for (int i = 0; i < field.size(); i++) {
                fields += ", " + field.get(i) + " AS " + table + "_" + field.get(i);
                switch (hm.get(field.get(i))){
                    case "user":
                    case "users":
                        fields += ", useridstoname(" + field.get(i) + ") AS " + table + "_" + field.get(i)+"_name";
                        break;
                    case "dept":
                    case "depts":
                        fields += ", deptidstoname(" + field.get(i) + ") AS " + table + "_" + field.get(i)+"_name";
                        break;
                    default:
                }
            }
        }
        String wheres = "";
        if (where != null || !where.equals("")) {
            for (String w : where) {
                wheres += " and " + w + " ";
            }
        }
        String sql = "select recorderNO AS " + table + "_recorderNO ,(@i:=@i+1) " + table + "_num" + fields + ",reserveField AS " + table + "_reserveField,linkRecorderNO AS " + table + "_linkRecorderNO, recordName AS " + table + "_recordName,useridtoname(recordName) AS " + table + "_recordName_name,recordTime AS " + table + "_recordTime,modifyName AS " + table + "_modifyName,useridtoname(modifyName) AS " + table + "_modifyName_name,modifyTime AS " + table + "_modifyTime from " + table + ",(SELECT @i:=" + startRow + ") as i where curStatus=2 " + wheres;
        if (order != null && order.length() > 1) {
            sql += " order by " + order;
        }
        if (startRow >= 0 && endRow > 0) {
            sql += " limit " + startRow + " , " + endRow;
        }
        LogUtil.sysLog("SQL:" + sql);
        return sql;
    }

    @Test
    public void test() {
        String name = "asdf_asdfffffff1$asd";
        LogUtil.sysLog("========================>" + name.substring(name.indexOf("_") + 1, name.indexOf("$")) + "<======================");
    }
	

    public static String getItem(String field, String type, String value) {
        String item = null;
        switch (type) {
            case "等于":
                item = DDUtil.getTypeWhere(field, value, "=");
                break;
            case "不等于":
                item = field + " != '" + value + "'";
                break;
            case "包含":
                item = DDUtil.getTypeWhere(field, value);
                break;
            case "大于":
                item = field + " > '" + value + "'";
                break;
            case "大于等于":
                item = field + " >= '" + value + "'";
                break;
            case "小于":
                item = field + " < '" + value + "'";
                break;
            case "小于等于":
                item = field + " <= '" + value + "'";
                break;
            default:
                item = "=";
        }
        return item;
    }

    /**
     * 生成 执行批量添加的sql语句
     *
     * @param table     表名
     * @param fieldList 所有字段
     * @return sql 生成的sql语句
     */
    public static String getInsertAllSql(String table, List<Map<String, Object>> fieldList) {
        String fields = "";
        String values = "";
        if (fieldList != null) {
            for (int i = 0; i < fieldList.size(); i++) {
                String value1 = "";
                String value2 = "";
                Map<String, Object> map = fieldList.get(i);
                Iterator entries = map.entrySet().iterator();
                while (entries.hasNext()) {
                    Map.Entry entry = (Map.Entry) entries.next();
                    String key = (String) entry.getKey();
                    Object value = entry.getValue();
                    if (i == 0) {
                        fields += key + ",";
                    }
                    if (value != null && value != "") {
                        value1 += "'" + value + "',";
                    } else {
                        value1 += " null ,";
                    }
                }
                value2 = "(" + value1.substring(0, value1.length() - 1) + "),";
                values += value2;
            }
            if (fields.length() > 1) {
                fields = fields.substring(0, fields.length() - 1);
            }
            if (values.length() > 1) {
                values = values.substring(0, values.length() - 1);
            }
        }
        String sql = "INSERT INTO " + table + "(" + fields + ") VALUES " + values;
        LogUtil.sysLog("sql:" + sql);
        return sql;
    }
}

