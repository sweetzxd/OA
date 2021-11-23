package com.oa.core.bean.system;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * @ClassName:Attendance
 * @author:zxd
 * @Date:2019/03/19
 * @Time:下午 6:13
 * @Version V1.0
 * @Explain 考勤机签到字段
 */
public class Attendance implements Serializable {

    private String user;
    private String name;
    private String year;
    private String month;
    private String day;
    private String hour;
    private String minute;
    private String second;
    private Timestamp datetime;

    public void setValue(String user, String name, String year, String month, String day, String hour, String minute, String second) {
        this.user = user;
        this.name = name;
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.second = second;
        try {
            this.datetime = Timestamp.valueOf(year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getMinute() {
        return minute;
    }

    public void setMinute(String minute) {
        this.minute = minute;
    }

    public String getSecond() {
        return second;
    }

    public void setSecond(String second) {
        this.second = second;
    }

    public Timestamp getDatetime() {
        return datetime;
    }

    public void setDatetime(Timestamp datetime) {
        this.datetime = datetime;
    }
}
