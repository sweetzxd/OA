package com.oa.core.util;

import com.oa.core.bean.dd.TaskData;
import com.oa.core.helper.DateHelper;
import com.oa.core.helper.StringHelper;
import com.oa.core.service.dd.DictionaryService;
import com.oa.core.service.util.TableService;
import freemarker.template.utility.DateUtil;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @ClassName:ScheduleUtil
 * @author:zxd
 * @Date:2018/11/20
 * @Time:下午 2:33
 * @Version V1.0
 * @Explain
 */
public class ScheduleUtil {
    private static DictionaryService dictionaryService = (DictionaryService)SpringContextUtil.getBean("dictionaryService");
    private static TableService tableService = (TableService)SpringContextUtil.getBean("tableService");
    public static String getSql (String taskId,List<String> where){
        TaskData task = dictionaryService.selectTaskName(taskId);
        String tableId = task.getTableName();
        String fields = task.getTaskField();
        Vector<String> field = StringHelper.string2Vector(fields,";");
        String sql = MySqlUtil.getSql(field,tableId,where,"order by recordTime asc");
        return sql;
    }

    public static List<Map<String,Object>> getValue(String taskId,List<String> where){
        TaskData task = dictionaryService.selectTaskName(taskId);
        String tableId = task.getTableName();
        String fields = task.getTaskField();
        Vector<String> field = StringHelper.string2Vector(fields,";");
        String sql = MySqlUtil.getSql(field,tableId,where,"order by recordTime asc");
        return tableService.selectSqlMapList(sql);
    }

    public static Hashtable<String,String> findDates(String dBegin, String dEnd,String type,int month){

        Date begin = DateHelper.convert(dBegin,"yyyy-MM-dd");
        Date end = DateHelper.convert(dEnd,"yyyy-MM-dd");

        Hashtable<String,String> lDate = new Hashtable<>();
        String monthStr = String.valueOf(month);
        if(month<10){
            monthStr = "0"+monthStr;
        }
        if(dBegin.substring(5,7).equals(monthStr)){
            lDate.put(dBegin.substring(0,10),type);
        }
        Calendar calBegin = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间  
        calBegin.setTime(begin);
        Calendar calEnd = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间  
        calEnd.setTime(end);
        // 测试此日期是否在指定日期之后  
        while (end.after(calBegin.getTime())){
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量  
            calBegin.add(Calendar.DAY_OF_MONTH, 1);
            // 获取月，这里需要需要月份的范围为0~11，因此获取月份的时候需要+1才是当前月份值
            int month1 = calBegin.get(Calendar.MONTH) + 1;
            if(month==month1){
                lDate.put(DateHelper.getStringDate(calBegin.getTime()),type);
            }
        }
        return lDate;
    }

    public static List<String> findDates(String dBegin, String dEnd,int month,List<String> list){

        Date begin = DateHelper.convert(dBegin,"yyyy-MM-dd");
        Date end = DateHelper.convert(dEnd,"yyyy-MM-dd");

        String monthStr = String.valueOf(month);
        if(month<10){
            monthStr = "0"+monthStr;
        }
        if(dBegin.substring(5,7).equals(monthStr)){
            list.add(dBegin.substring(0,10));
        }
        Calendar calBegin = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间  
        calBegin.setTime(begin);
        Calendar calEnd = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间  
        calEnd.setTime(end);
        // 测试此日期是否在指定日期之后  
        while (end.after(calBegin.getTime())){
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量  
            calBegin.add(Calendar.DAY_OF_MONTH, 1);
            // 获取月，这里需要需要月份的范围为0~11，因此获取月份的时候需要+1才是当前月份值
            int month1 = calBegin.get(Calendar.MONTH) + 1;
            if(month==month1){
                list.add(DateHelper.getStringDate(calBegin.getTime()));
            }
        }
        return list;
    }


    public static JSONObject getjson( JSONObject json,Hashtable<String,String> date,String type){
        for(Iterator<Map.Entry<String, String>> iterator = date.entrySet().iterator(); iterator.hasNext();){
            Map.Entry<String,String> entry=iterator.next();
            String key = entry.getKey();
            String value = entry.getValue();
            String day = key.substring(8,10);
            JSONObject j1 = new JSONObject(true);
            j1.put("exp",value);
            j1.put("am","");
            j1.put("pm","");
            JSONObject j2 = new JSONObject(true);
            j2.put("type",type);
            j2.put("value",j1);
            json.put(day,j2);
        }
        return json;
    }

    public static JSONArray getwxjson( JSONArray jsonArray,Hashtable<String,String> date,String type){
        for(Iterator<Map.Entry<String, String>> iterator = date.entrySet().iterator(); iterator.hasNext();){
            JSONObject json = new JSONObject();
            Map.Entry<String,String> entry=iterator.next();
            String key = entry.getKey();
            String value = entry.getValue();
            String day = key.substring(8,10);
            JSONObject j2 = new JSONObject(true);
            j2.put("exp",value);
            j2.put("am","");
            j2.put("pm","");
            j2.put("type",type);
            json.put("key",day);
            json.put("data",j2);
            jsonArray.put(json);
        }
        return jsonArray;
    }

    //获取当前月的休息日
    public static Hashtable<String,String> findDayOffs(int year,int month){

        ConfParseUtil cp = new ConfParseUtil();
        Hashtable<String, String> ht  = cp.getSchedule();
        List<Integer> weekList  = new ArrayList<>();
        if(ht.get("sunday").equals("false")){
            weekList.add(1);
        }else if(ht.get("monday").equals("false")){
            weekList.add(2);
        }else if(ht.get("tuesday").equals("false")){
            weekList.add(3);
        }else if(ht.get("wednesday").equals("false")){
            weekList.add(4);
        }else if(ht.get("thursday").equals("false")){
            weekList.add(5);
        }else if(ht.get("friday").equals("false")){
            weekList.add(6);
        }else if(ht.get("saturday").equals("false")){
            weekList.add(7);
        }

        Calendar cal = Calendar.getInstance();
        //设置年份
        cal.set(Calendar.YEAR, year);
        //设置月份
        cal.set(Calendar.MONTH, month-1);
        //获取某月最小天数
        int firstDay = cal.getMinimum(Calendar.DATE);
        //设置日历中月份的最小天数
        cal.set(Calendar.DAY_OF_MONTH,firstDay);
        Date begin  = cal.getTime();
        //格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dBegin = sdf.format(cal.getTime());

        //获取某月最大天数
        int lastDay = cal.getActualMaximum(Calendar.DATE);
        //设置日历中月份的最大天数
        cal.set(Calendar.DAY_OF_MONTH, lastDay);
        Date end = cal.getTime();

        Hashtable<String,String> lDate = new Hashtable<>();

        Calendar calBegin = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间  
        calBegin.setTime(begin);
        calBegin.add(Calendar.DAY_OF_MONTH, -1);
        Calendar calEnd = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间  
        calEnd.setTime(end);
        // 测试此日期是否在指定日期之后  
        while (end.after(calBegin.getTime())){
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量  
            calBegin.add(Calendar.DAY_OF_MONTH, 1);
            //获取星期
            int week1 = calBegin.get(Calendar.DAY_OF_WEEK);
            if(weekList.contains(week1)){
                lDate.put(DateHelper.getStringDate(calBegin.getTime()),"休");
            }
        }
        return lDate;
    }
}
