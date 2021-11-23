package com.oa.core.util;

import com.oa.core.listener.InitDataListener;
import com.oa.core.bean.dd.FieldData;
import com.oa.core.bean.dd.TableData;
import com.oa.core.bean.dd.TaskData;
import com.oa.core.bean.util.Table;
import com.oa.core.helper.DateHelper;
import com.oa.core.service.dd.DictionaryService;
import com.oa.core.service.util.TableService;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

public class PrimaryKeyUitl {
    private static Hashtable<String, String> maxIds = new Hashtable<String, String>();

    public static String getNextId(String presentId) {
        String subid = presentId.substring(10, presentId.length());
        String sid = String.valueOf(Integer.parseInt(subid) + 1);
        String inId = getDate();
        String nextId = "Z";
        String endId = "000000";
        endId = endId.substring(sid.length(), endId.length()) + sid;
        nextId += inId + endId;
        return nextId;
    }

    /**
     * @return yyyyMMdd
     */
    public static String getDate() {
        String date = "19000101";
        Date d = new Date();//获取时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");//转换格式
        if (sdf != null && d != null) {
            date = sdf.format(d);
        }
        return date;
    }

    /**
     * @return yyMMdd
     */
    public static String getDate1() {
        String date = "000101";
        Date d = new Date();//获取时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");//转换格式
        if (sdf != null && d != null) {
            date = sdf.format(d);
        }
        return date;
    }

    public static String getDataId(String value, String key) {
        String fid = PinyinUtil.getAlpha(PinyinUtil.cleanChar(value));
        if (fid.length() > 5) {
            fid = fid.substring(0, 5) + PrimaryKeyUitl.getDate1() + "01";
        } else {
            fid = fid + PrimaryKeyUitl.getDate1() + ("0000001".substring(fid.length(), 7));
        }

        boolean nextId = getTableIndexId(fid, key);
        if (nextId) {
            fid = getNextDataId(fid, key);
        }
        return fid;
    }

    public static boolean getTableIndexId(String fid, String key) {
        int count = 0;
        DictionaryService m = (DictionaryService) SpringContextUtil.getBean("dictionaryService");
        if ("table".equals(key)) {
            TableData tableData = new TableData();
            tableData.setTableName(fid);
            count = m.selectTableCount(tableData);
        } else if ("task".equals(key)) {
            TaskData taskData = new TaskData();
            taskData.setTaskName(fid);
            count = m.selectTaskCount(taskData);
        } else if ("field".equals(key)) {
            FieldData fieldData = new FieldData();
            fieldData.setFieldName(fid);
            count = m.selectFieldCount(fieldData);
        }
        List<String> list = InitDataListener.getData(key);
        if (list == null) {
            list = new ArrayList<String>();
        }
        if (count > 0) {
            if (list != null && !list.contains(fid)) {
                list.add(fid);
                InitDataListener.putData(key, list);
            }
            return true;
        } else {
            if (list != null && !list.contains(fid)) {
                list.add(fid);
                InitDataListener.putData(key, list);
                return false;
            } else {
                return true;
            }
        }
    }

    public static String getNextDataId(String value, String key) {
        String fid = value.substring(0, value.length() - 2);
        int num = Integer.parseInt(value.substring(value.length() - 2, value.length()));
        if (num < 10) {
            fid = fid + "0" + (num + 1);
        } else {
            fid = fid + (num + 1);
        }
        boolean nextId = getTableIndexId(fid, key);
        if (!nextId) {
            return fid;
        } else {
            return getNextDataId(fid, key);
        }
    }


    /**
     * 获取数据库中某表的最大主键值，并存入Hashtable中
     *
     * @param tableName   表
     * @param pkFieldName 主键值
     * @return 新的主键值
     */
    private void getTableMaxId(String tableName, String pkFieldName, String prefixType, String title) {
        String prefix = "";
        if (prefixType.equals(Const.PREFIXTYPE_YEAR_MONTH)) {
            prefix = DateHelper.getYearMonth();
        } else if (prefixType.equals(Const.PREFIXTYPE_YEAR_MONTH_DAY)) {
            prefix = DateHelper.getYearMonthDay();
        } else {
            prefix = prefixType.trim();
        }
        if (title != null && title != "") {
            prefix = title + prefix;
        } else {
            ConfParseUtil cp = new ConfParseUtil();
            prefix = cp.getPoa("recno") + prefix;
        }
        Table mqb = new Table(tableName, pkFieldName, prefix);
        TableService t = (TableService) SpringContextUtil.getBean("tableService");
        String maxId = t.getMaxId(mqb);
        if (maxId == null) {
            maxId = prefix + "00000";
        }
        ConfParseUtil cp = new ConfParseUtil();
        String recno = cp.getPoa("recno");
        maxIds.put(recno+tableName, maxId);
    }

    /**
     * 获取下一个主键值，规则为年月+5位流水号
     *
     * @param tableName   表
     * @param pkFieldName 主键字段
     * @return 新的主键值
     */
    public String getNextId(String tableName, String pkFieldName) {
        return getNextId(tableName, pkFieldName, null, Const.PREFIXTYPE_YEAR_MONTH_DAY);
    }

    public String getNextId(String tableName, String pkFieldName, String title) {
        String py = null;
        if (title != null) {
            py = PinyinUtil.getAlpha(PinyinUtil.cleanChar(title));
            LogUtil.sysLog("py:" + py);
        }
        return getNextId(tableName, pkFieldName, py, Const.PREFIXTYPE_YEAR_MONTH_DAY);
    }

    /**
     * 按指定前缀类型获取下一个主键值,规则为前缀+5位流水号
     *
     * @param tableName
     * @param pkFieldName
     * @param prefixType
     * @return
     */
    public String getNextId(String tableName, String pkFieldName, String title, String prefixType) {
        ConfParseUtil cp = new ConfParseUtil();
        String recno = cp.getPoa("recno");
        String idkey = recno+tableName;
        String nextId = "";
        String maxId =  maxIds.get(idkey);
        String curPrefix = "";
        if (prefixType.equals(Const.PREFIXTYPE_YEAR_MONTH)) {
            curPrefix = DateHelper.getYearMonth();
        } else if (prefixType.equals(Const.PREFIXTYPE_YEAR_MONTH_DAY)) {
            curPrefix = DateHelper.getYearMonthDay();
        } else {
            curPrefix = (prefixType == null ? "" : prefixType.trim());
        }
        synchronized (maxIds) {
            if (maxId == null) {
                getTableMaxId(tableName, pkFieldName, prefixType, title);
                maxId = maxIds.get(idkey);
            }
        }
        int star = 1;
        if(recno!=null && !recno.equals("")){
            star = recno.length();
        }
        if (title != null && title.length()>0) {
            star = title.length();
        }
        String prefix = maxId.substring(star, maxId.length() - 5);
        LogUtil.sysLog("prefix:" + prefix +"--"+(!prefix.equals(curPrefix)));
        if (!prefix.equals(curPrefix)) {
            nextId = curPrefix + "00001";
        } else {
            int orderNum = Integer.parseInt(maxId.substring(maxId.length() - 5));
            orderNum++;
            String strZero = "00000";
            String strOrderNum = Integer.toString(orderNum);
            nextId = prefix + strZero.substring(0, 5 - strOrderNum.length()) + strOrderNum;
        }
        if (title != null && title != "") {
            nextId = title + nextId;
        } else {

            nextId = recno + nextId;
        }
        maxIds.put(idkey, nextId);
        return nextId;
    }

    @Test
    public void test() {
        String title = "FlowTask", maxId = "FlowTask2018092900001";
        int star = 0;
        if (title != null) {
            star = title.length();
        }
        String prefix = maxId.substring(star, maxId.length() - 5);
        LogUtil.sysLog("========================>" + prefix + "<======================");
    }

}
