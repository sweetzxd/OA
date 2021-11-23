package com.oa.core.util;

import java.util.ArrayList;
import java.util.List;

/**
 * 常量
 *
 * @author zxd
 */
public interface Const {

    public final static String LOCATION="石家庄";
    /**
     * 分隔符
     */
    public final static String SEPARATE = ";";
    /**
     * 使用DateHelper生成自动编号的前缀
     */
    public final static String PREFIXTYPE_YEAR_MONTH = "yyyyMM";
    public final static String PREFIXTYPE_YEAR_MONTH_DAY = "yyyyMMdd";


    /**
     * 时间格式
     */
    public final static String YEAR_MONTH_DAY = "yyyy-MM-dd";
    public final static String YEAR_MONTH_DAY_HH_MM = "yyyy-MM-dd HH:mm";
    public final static String YEAR_MONTH_DAY_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    /**
     * 需转换的key
     */
    public final static List<String> NOKEY = new ArrayList<String>() {{
        add("error_user");
        add("error_pw");
        add("error_us");
        add("error_ip");
        add("error_as");
        add("error_catch");
    }};
}
