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

    public final static String SHOUWEN = "bt19050900002;xxgk190613002;fwjg190613002;ngr1905090002;rq19050900001;djbm190613002;wh19050900002;cwrq190613002;mj19061300002;fs19050900001;jjcd190613002;lwqd190613002;lwdw190509001;lwnr190509001;sp19050900003;spr1905090004;wjfj190509001;";
    public final static String FAWEN = "bt19050900001;ngr1905090001;ngbm190613003;ngrdh19061303;wh19050900001;fwlx190509001;xxgk190613006;fwrq190509001;ys19061300003;dyys190613003;jjcd190509001;mj19061300005;wjz1906130003;fwwh190613003;fjmc190613003;ztc1906130003;zsdw190613003;csdw190613003;fsfw190613003;zbdw190509001;bmscy19050901;fwsm190509001;sp19050900001;spr1905090001;fj190509000017;";
}
