package com.oa.core.system.warning.every;

import com.oa.core.helper.DateHelper;
import com.oa.core.helper.StringHelper;
import com.oa.core.service.util.TableService;
import com.oa.core.util.Const;
import com.oa.core.util.LogUtil;
import com.oa.core.util.SpringContextUtil;
import org.junit.Test;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @ClassName:SystemEveryPush
 * @author:zxd
 * @Date:2019/04/23
 * @Time:下午 5:11
 * @Version V1.0
 * @Explain
 */
public class SystemEveryPush implements Job {

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        LogUtil.sysLog("开始执行推送检查");
        TableService tableService = (TableService) SpringContextUtil.getBean("tableService");
        String filed = "recorderNO,xxbt190423001 as title,xxnr190423001 as msg,jsr1904230001 as `user`,xh19042300001 as xh,fssj190423001 as time";
        List<Map<String, Object>> msgList = tableService.selectSqlMapList("select " + filed + " from xxtx190423001 where curStatus=2 and sfqy190423001='是'");
        if (msgList != null) {
            for (Map<String, Object> msgVal : msgList) {
                String recorderNO = (String) msgVal.get("recorderNO");
                String title = (String) msgVal.get("title");
                String msg = (String) msgVal.get("msg");
                String user = (String) msgVal.get("user");
                String xh = (String) msgVal.get("xh");
                String time = (String) msgVal.get("time");
                if (xh.equals("单次")) {
                    if (DateHelper.getDateDifference(time + ":00", Const.YEAR_MONTH_DAY_HH_MM_SS)) {
                        sendMsg(user, title);
                        tableService.updateSqlMap("update xxtx190423001 set sfqy190423001='否' where curStatus=2 and recorderNO='" + recorderNO + "'");
                    }
                } else if (xh.equals("多次")) {
                    if (timeFormat(time)) {
                        sendMsg(user, title);
                    }
                }
            }
        }
        LogUtil.sysLog("消息检查结束");
    }

    public void sendMsg(String user, String title) {
        if (user != null) {
            if (user.indexOf(";") > 0) {
                LogUtil.toPhone(StringHelper.string2Vector(user, ";"), title);
            } else {
                Vector<String> users = new Vector<>();
                users.add(user);
                LogUtil.toPhone(users, title);
            }
        }
    }

    public boolean timeFormat(String time) {
        String date = DateHelper.getYMDHM();
        String d_year = date.substring(0, 4);
        String d_month = date.substring(5, 7);
        String d_day = date.substring(8, 10);
        String d_hh = date.substring(11, 13);
        String d_mm = date.substring(14, 16);

        String year = time.substring(0, 4);
        String month = time.substring(5, 7);
        String day = time.substring(8, 10);
        String hh = time.substring(11, 13);
        String mm = time.substring(14, 16);
        String new_time = time;
        if (year.equals("0000")) {
            new_time = new_time.replaceAll("0000", d_year);
        }
        if (month.equals("00")) {
            new_time = new_time.replaceAll("-00-", "-" + d_month + "-");
        }
        if (day.equals("00")) {
            new_time = new_time.replaceAll("-00 ", "-" + d_day + " ");
        }
        if (hh.equals("00")) {
            new_time = new_time.replaceAll(" 00:", " " + d_hh + ":");
        }
        if (mm.equals("00")) {
            new_time = new_time.replaceAll(":00", ":" + d_mm);
        }
        new_time = new_time + ":00";
        boolean this_time = false;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMddHHmmss");
        try {
            Long time_d = Long.valueOf(DateHelper.getYearMonthDayHourMinutesSecond());
            Date parse = sdf.parse(new_time);
            Long time_s = Long.valueOf(sdf2.format(parse));
            System.out.println(time_d);
            System.out.println(time_s);
            if (time_d > time_s && time_d < (time_s + 100)) {
                System.out.println("在时间段内");
                this_time = true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return this_time;
    }

    @Test
    public void test() {
        String time = "0000-00-00 19:39";
        System.out.println(timeFormat(time));
    }
}