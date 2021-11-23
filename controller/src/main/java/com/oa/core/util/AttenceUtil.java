package com.oa.core.util;

import com.oa.core.bean.Loginer;
import com.oa.core.helper.DateHelper;
import com.oa.core.service.util.TableService;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName:AttenceUtil
 * @author:zxd
 * @Date:2019/05/14
 * @Time:下午 3:31
 * @Version V1.0
 * @Explain
 */
public class AttenceUtil {
    private static TableService tableService = (TableService) SpringContextUtil.getBean("tableService");

    public static String attence(String userId, String attenceTime) {
        JSONObject jsonObject = new JSONObject();
        try {
            ConfParseUtil cp = new ConfParseUtil();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String date = sdf.format(new Date());
            String swsb = date + " " + cp.getSchedule("amtowork");
            String swxb = date + " " + cp.getSchedule("amoffwork");
            String xwsb = date + " " + cp.getSchedule("pmtowork");
            String xwxb = date + " " + cp.getSchedule("pmoffwork");

            String sql = " select * from ygkqj18112001 " +
                    " where jlr1811200001 = '" + userId + "' and curStatus = 2 and DATE_FORMAT(recordTime,'%Y-%m-%d') = '" + date + "'; ";
            List<Map<String, Object>> list = tableService.selectSqlMapList(sql);
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date now = DateHelper.convert(attenceTime, Const.YEAR_MONTH_DAY_HH_MM_SS);
            long d = now.getTime();
            if (list.size() > 0) {//打过一次卡
                Map<String, Object> map = list.get(0);
                String recorderNO = (String) map.get("recorderNO");
                String updateSql = " update ygkqj18112001 set ";
                if (d <= sdf1.parse(swsb).getTime()) {   //
                    updateSql += " swsb181120001 = '" + sdf1.format(now) + "'";
                } else if (d > sdf1.parse(swsb).getTime() && d < sdf1.parse(swxb).getTime()) {//在8:30--12:00之间 可能是上午上班打卡 上午下班打卡
                    if (map.get("swsb181120001") == null) {//上午上班
                        updateSql += " swsb181120001 = '" + sdf1.format(now) + "'";
                    } else {                               //上午下班
                        updateSql += " swxb181120001 = '" + sdf1.format(now) + "'";
                    }
                } else if (d >= sdf1.parse(swxb).getTime() && d <= sdf1.parse(xwsb).getTime()) {//在12:00--13:30之间 可能是上午下班   下午上班打卡
                    if (map.get("swxb181120001") == null) {//上午下班
                        updateSql += " swxb181120001 = '" + sdf1.format(now) + "'";
                    } else {                                //下午上班
                        updateSql += " xwsb181120001 = '" + sdf1.format(now) + "'";
                    }
                } else if (d > sdf1.parse(xwsb).getTime() && d < sdf1.parse(xwxb).getTime()) {//在13:30--18:00之间  课程是下午上班   下午下班
                    if (map.get("xwsb181120001") == null) {
                        updateSql += " xwsb181120001 = '" + sdf1.format(now) + "'";
                    } else {
                        updateSql += " xwxb181120001 = '" + sdf1.format(now) + "'";
                    }
                } else {
                    updateSql += " xwxb181120001 = '" + sdf1.format(now) + "'";
                }
                updateSql += " where recorderNO = '" + recorderNO + "'";
                tableService.updateSqlMap(updateSql);
            } else {//第一次打卡
                PrimaryKeyUitl pk = new PrimaryKeyUitl();
                String table = "ygkqj18112001";
                String recorderNO = pk.getNextId(table, "recorderNO");

                Map<String, Object> map = new HashMap<>();
                map.put("recorderNO", recorderNO);
                map.put("jlr1811200001", userId);
                map.put("recordName", userId);
                map.put("recordTime", sdf1.format(now));
                if (d <= sdf1.parse(swsb).getTime()) {   //
                    map.put("swsb181120001", sdf1.format(now));
                } else if (d > sdf1.parse(swsb).getTime() && d < sdf1.parse(swxb).getTime()) {//在8:30--12:00之间
                    map.put("swsb181120001", sdf1.format(now));
                } else if (d >= sdf1.parse(swxb).getTime() && d <= sdf1.parse(xwsb).getTime()) {//在12:00--13:30之间
                    map.put("xwsb181120001", sdf1.format(now));
                } else if (d > sdf1.parse(xwsb).getTime() && d < sdf1.parse(xwxb).getTime()) {//在13:30--18:00之间  课程是下午上班   下午下班
                    map.put("xwsb181120001", sdf1.format(now));
                } else {
                    map.put("xwxb181120001", sdf1.format(now));
                }
                String insertSql = MySqlUtil.getInsertSql(table, map);
                tableService.insertSqlMap(insertSql);
            }
            jsonObject.put("msg", "签到成功");
            jsonObject.put("date", sdf1.format(now));
            WeatherUtil wu = new WeatherUtil();
            jsonObject.put("weather", wu.getWeather("石家庄"));
            jsonObject.put("success", 1);
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.put("msg", "签到失败");
            jsonObject.put("success", 0);
        }
        return jsonObject.toString();
    }


    public static double[] attencetype(String num) {
        double[] type = new double[2];
        switch (num) {
            case "1111":
                type[0] = 1;
                type[1] = 0;
                break;
            case "0111":
                type[0] = 1;
                type[1] = 1;
                break;
            case "1011":
                type[0] = 1;
                type[1] = 1;
                break;
            case "1101":
                type[0] = 1;
                type[1] = 1;
                break;
            case "1110":
                type[0] = 1;
                type[1] = 1;
                break;
            case "0011":
                type[0] = 0.5;
                type[1] = 0;
                break;
            case "1100":
                type[0] = 0.5;
                type[1] = 0;
                break;
            case "1001":
                type[0] = 0.5;
                type[1] = 1;
                break;
            case "0110":
                type[0] = 0.5;
                type[1] = 2;
                break;
            case "1010":
                type[0] = 0.5;
                type[1] = 2;
                break;
            case "0101":
                type[0] = 0.5;
                type[1] = 2;
                break;
            case "0001":
                type[0] = 0;
                type[1] = 2;
                break;
            case "0010":
                type[0] = 0;
                type[1] = 2;
                break;
            case "0100":
                type[0] = 0;
                type[1] = 2;
                break;
            case "1000":
                type[0] = 0;
                type[1] = 2;
                break;
            default:
                type[0] = 0;
                type[1] = 0;
                break;
        }
        return type;
    }

    /**
     * @Description:	二进制转换成十进制
     * @param binarySource
     * @return int
     */
    public static int binaryToDecimal(String binarySource) {
        BigInteger bi = new BigInteger(binarySource, 2);
        return Integer.parseInt(bi.toString());
    }
}
