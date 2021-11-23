package com.oa.core.bean.system;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * @ClassName:AttCalendar
 * @author:zxd
 * @Date:2019/06/19
 * @Time:上午 9:00
 * @Version V1.0
 * @Explain 考勤统计
 */
public class AttCalendar implements Serializable {
    /**
     * 描述：日期
     */
    private String date;
    /**
     * 描述：上午上班
     */
    private String swsb = "";
    /**
     * 描述：上午下班
     */
    private String swxb = "";
    /**
     * 描述：下午上班
     */
    private String xwsb = "";
    /**
     * 描述：下午下班
     */
    private String xwxb = "";
    /**
     * 描述：外出
     */
    private String wc = "";
    /**
     * 描述：请假
     */
    private String qj = "";
    /**
     * 描述：补卡
     */
    private String bk = "";
    /**
     * 描述：考勤
     */
    private String kq = "";


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSwsb() {
        return swsb;
    }

    public void setSwsb(String swsb) {
        this.swsb = swsb;
    }

    public String getSwxb() {
        return swxb;
    }

    public void setSwxb(String swxb) {
        this.swxb = swxb;
    }

    public String getXwsb() {
        return xwsb;
    }

    public void setXwsb(String xwsb) {
        this.xwsb = xwsb;
    }

    public String getXwxb() {
        return xwxb;
    }

    public void setXwxb(String xwxb) {
        this.xwxb = xwxb;
    }

    public String getWc() {
        return wc;
    }

    public void setWc(String wc) {
        this.wc = wc;
    }

    public String getQj() {
        return qj;
    }

    public void setQj(String qj) {
        this.qj = qj;
    }

    public String getBk() {
        return bk;
    }

    public void setBk(String bk) {
        this.bk = bk;
    }

    public String getKq() {
        return kq;
    }

    public void setKq(String kq) {
        this.kq = kq;
    }

    public String getType(){
        String val = "0";
        boolean ss = swsb.equals("");
        boolean sx = swxb.equals("");
        boolean xs = xwsb.equals("");
        boolean xx = xwxb.equals("");
        boolean w = wc.equals("0");
        boolean q = qj.equals("0");
        boolean b = bk.equals("0");

        if(!ss && !xx){
            val="1";
        }else if((!ss && !sx) || (!xs && !xx)){
            val="0.5";
        }
        if(!w){
            val="外";
        }else if(!q){
            val = "假";
        }else if(!b){
            val = "补";
        }
        return val;

    }
}
