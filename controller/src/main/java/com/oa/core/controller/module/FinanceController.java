package com.oa.core.controller.module;

import com.oa.core.bean.Loginer;
import com.oa.core.bean.dd.TaskData;
import com.oa.core.bean.util.PageUtil;
import com.oa.core.helper.DateHelper;
import com.oa.core.helper.StringHelper;
import com.oa.core.interceptor.Logined;
import com.oa.core.service.dd.DictionaryService;
import com.oa.core.service.module.FestivalService;
import com.oa.core.service.system.FormCustomMadeService;
import com.oa.core.service.util.TableService;
import com.oa.core.util.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;

/**
 * @ClassName:FinanceController
 * @author:zxd
 * @Date:2018/12/05
 * @Time:下午 4:19
 * @Version V1.0
 * @Explain 资产财务特殊处理类
 */
@Controller
@RequestMapping("/finance")
public class FinanceController {

    @Autowired
    FormCustomMadeService formCustomMadeService;
    @Autowired
    DictionaryService dictionaryService;
    @Autowired
    TableService tableService;
    @Autowired
    FestivalService festivalService;

    @Logined
    @RequestMapping(value = "/gettablevalue")
    @ResponseBody
    public String getTableValue(HttpServletRequest request, int limit, int page) {
        String formid = request.getParameter("formid");
        String field = request.getParameter("field");
        String item = request.getParameter("item");
        String value = request.getParameter("value");
        String year = request.getParameter("year");
        String month = request.getParameter("month");

        List<String> where = new ArrayList<>();
        if (field != null && !field.equals("")) {
            where.add(MySqlUtil.getItem(field, item, value));
        }
        if (year != null && month != null) {
            where.add(" YEAR(ffrq181204001) = " + year + " and MONTH(ffrq181204001) = " + month + " ");
        }
        TaskData taskData = dictionaryService.listTaskByFormId(formid);
        String tableId = taskData.getTableName();
        Vector<String> fields = StringHelper.string2Vector(taskData.getTaskField(), ";");
        String countSql = MySqlUtil.getCountSql(tableId, where);
        int count = tableService.selectSqlCount(countSql);
        PageUtil pu = new PageUtil();
        pu.setPageSize(limit);
        pu.setCurrentPage(page);
        int StartRow = pu.getStartRow();
        int EndRow = pu.getEndRow() - pu.getStartRow();
        String getsql = MySqlUtil.getNameSql(fields, tableId, where, StartRow, EndRow);
        List<Map<String, Object>> sqlval = tableService.selectSqlMapList(getsql);

        String sql = "select COUNT(*) from yggzf18120401 where YEAR(ffrq181204001) = '" + year + "' and MONTH(ffrq181204001) = '" + month + "' AND curStatus=2 and reserveField='3'";
        int payroll = tableService.selectSqlCount(sql);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 0);
        jsonObject.put("msg", "");
        jsonObject.put("count", count);
        jsonObject.put("data", sqlval);
        jsonObject.put("success",1);
        jsonObject.put("payroll", payroll);
        jsonObject.put("is", true);
        jsonObject.put("tip", "操作成功");
        return jsonObject.toString();
    }

    @Logined
    @RequestMapping(value = "/getfinance")
    @ResponseBody
    public String getFinance(HttpServletRequest request, String tableName) {
        StringBuffer sql = new StringBuffer("SELECT ");
        sql.append("recorderNO,formulasName,formulasValue,formulasResult,formulasType,tableName,reserveField,linkRecorderNO,recordName,recordTime,modifyName,modifyTime");
        sql.append(" FROM DesignFormulas ");
        sql.append(" WHERE tableName ='" + tableName + "' AND curStatus=2");
        List<Map<String, Object>> sqlval = tableService.selectSqlMapList(sql.toString());
        if (sqlval != null && sqlval.size() > 0) {
            JSONArray jsonArray = new JSONArray();
            for (int i = 0, len = sqlval.size(); i < len; i++) {
                Map<String, Object> map = sqlval.get(i);
                String formulasValue = (String) map.get("formulasValue");
                String formulasResult = (String) map.get("formulasResult");
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("value", formulasValue);
                jsonObject.put("result", formulasResult);
                jsonArray.put(jsonObject);
            }
            return jsonArray.toString();
        } else {
            return "0";
        }
    }

    @Logined
    @RequestMapping(value = "/makewages")
    @ResponseBody
    public String makeWages(HttpServletRequest request) {
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        String userId = loginer.getId() == null ? "" : loginer.getId();
        String field = request.getParameter("field");
        String item = request.getParameter("item");
        String value = request.getParameter("value");
        String year = request.getParameter("year");
        String month = request.getParameter("month");

        List<String> where = new ArrayList<>();
        if (field != null && !field.equals("")) {
            where.add(MySqlUtil.getItem(field, item, value));
        }
        TaskData taskData = dictionaryService.formTaskByFormId("gzjbb2018120300001");
        String tableId = taskData.getTableName();
        Vector<String> fields = StringHelper.string2Vector(taskData.getTaskField(), ";");
        String getsql = MySqlUtil.getNameSql(fields, tableId, where, 0, 0);
        List<Map<String, Object>> sqlval = tableService.selectSqlMapList(getsql);
        if (sqlval.size() > 0) {
            List<String> f = new ArrayList<>();
            f.add("deleteName='" + userId + "'");
            f.add("deleteTime='" + DateHelper.now() + "'");
            List<String> w = new ArrayList<>();
            w.add("YEAR(ffrq181204001) = " + year + " and MONTH(ffrq181204001) = " + month + "");
            w.add("reserveField != '" + 3 + "'");
            String delsql = MySqlUtil.getDeleteSql("yggzf18120401", f, w, null);
            tableService.updateSqlMap(delsql);
        }
        PrimaryKeyUitl pk = new PrimaryKeyUitl();
        //计算本月工作日

        //获取本月总天数
        Calendar c = Calendar.getInstance();
        c.set(Integer.valueOf(year),Integer.valueOf(month), 0); //输入类型为int类型
        int monthDay = c.get(Calendar.DAY_OF_MONTH);

        JSONObject json = new JSONObject(true);
        List<Map<String,Object>> sqlval1 = new ArrayList<>();

        //休息日数据获取
        sqlval1 = festivalService.getAllByYearAndMonth(Integer.valueOf(year),Integer.valueOf(month));
        Hashtable<String,String> date = new Hashtable<>();
        date.putAll(ScheduleUtil.findDayOffs(Integer.valueOf(year),Integer.valueOf(month)));
        json = ScheduleUtil.getjson(json,date,"rest");

        //节假日数据获取
        sqlval1 = festivalService.getAllByYearAndMonth(Integer.valueOf(year),Integer.valueOf(month));
        Hashtable<String,String> date0 = new Hashtable<>();
        for(Map<String,Object> map : sqlval1){
            String startTime = String.valueOf(map.get("startTime"));
            String endTime = String.valueOf(map.get("endTime"));
            String type = String.valueOf(map.get("festivalName"));
            date0.putAll(ScheduleUtil.findDates(DateHelper.getSqlDateTime(startTime),DateHelper.getSqlDateTime(endTime),type,Integer.valueOf(month)));
        }
        json = ScheduleUtil.getjson(json,date0,"rest");

        int workDay = monthDay - json.length();

        for (Map<String, Object> map : sqlval) {
            Map<String, Object> ffmap = new HashMap<>();
            String uid = (String) map.get(tableId + "_xm18120400001");
            //姓名
            ffmap.put("xm18120400002", uid);
            //部门
            ffmap.put("bm18120400002", map.get(tableId + "_bm18120400001"));
            //发放日期
            ffmap.put("ffrq181204001", year + "-" + month + "-" + Calendar.DATE);
            //薪资(元)
            ffmap.put("xzy1812040002", map.get(tableId + "_xzy1812040001"));
            //出勤天数
            ffmap.put("cqts181204001", kq(uid, year, month));
            //绩效成绩
            ffmap.put("jxcj181204001", jx(uid, year, month));
            //奖惩(元)
            ffmap.put("jcy1812040001", map.get(""));
            //发放小计(元)
            ffmap.put("ffxjy18120401", map.get(tableId + "_xzy1812040001"));
            //养老保险(元)
            ffmap.put("ylbxy18120403", map.get(tableId + "_ylbxy18120401"));
            //医疗保险(元)
            ffmap.put("ylbxy18120404", map.get(tableId + "_ylbxy18120402"));
            //失业保险(元)
            ffmap.put("sybxy18120403", map.get(tableId + "_sybxy18120401"));
            //工伤保险(元)
            ffmap.put("gsbxy18120402", map.get(tableId + "_gsbxy18120401"));
            //生育保险(元)
            ffmap.put("sybxy18120404", map.get(tableId + "_sybxy18120402"));
            //住房公积金(元)
            ffmap.put("zfgjj18120402", map.get(tableId + "_zfgjj18120401"));
            //实发工资(元)
            ffmap.put("sfgzy18120401", map.get(tableId + "_ffhjy18120401"));
            //发放状态
            ffmap.put("reserveField", "1");
            //本月工作日
            ffmap.put("bygzr18122101",workDay);
            ffmap.put("recorderNO", pk.getNextId("yggzf18120401", "recorderNO"));
            ffmap.put("recordName", loginer.getId());
            ffmap.put("recordTime", DateHelper.now());
            ffmap.put("modifyName", loginer.getId());
            ffmap.put("modifyTime", DateHelper.now());
            String insertsql = MySqlUtil.getInsertSql("yggzf18120401", ffmap);
            tableService.insertSqlMap(insertsql);
        }
        return "";
    }

    @Logined
    @RequestMapping(value = "/payroll")
    @ResponseBody
    public String formPayroll(HttpServletRequest request, String year, String month) {
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        Map<String, Object> ffmap = new HashMap<>();
        ffmap.put("reserveField", "3");
        ffmap.put("modifyName", loginer.getId());
        ffmap.put("modifyTime", DateHelper.now());
        List<String> where = new ArrayList<>();
        where.add("YEAR(ffrq181204001) = " + year + " and MONTH(ffrq181204001) = " + month + "");
        String sql = MySqlUtil.getUpdateSql("yggzf18120401", ffmap, where);
        tableService.updateSqlMap(sql);
        return "";
    }

    public double kq(String userId, String year, String month) {
        List<String> where = new ArrayList<>();
        where.add("jlr1811200001='" + userId + "'");
        where.add("(YEAR(swsb181120001) = " + year + " and MONTH(swsb181120001) = " + month + " OR YEAR(xwxb181120001) = " + year + " and MONTH(xwxb181120001) = " + month + ")");
        List<Map<String, Object>> sqlval = ScheduleUtil.getValue("ygkqj18112001", where);

        ConfParseUtil cp = new ConfParseUtil();
        String mydate = DateHelper.getYMD();
        String amtowork = mydate + " " + cp.getSchedule("amtowork");
        String amoffwork = mydate + " " + cp.getSchedule("amoffwork");
        String pmtowork = mydate + " " + cp.getSchedule("pmtowork");
        String pmoffwork = mydate + " " + cp.getSchedule("pmoffwork");
        double oahour = DateHelper.getHour(amtowork, amoffwork) + DateHelper.getHour(pmtowork, pmoffwork);
        double sumdate = 0.0;
        for (Map<String, Object> map : sqlval) {
            String swsb181120001 = String.valueOf(map.get("swsb181120001"));
            String swxb181120001 = String.valueOf(map.get("swxb181120001"));
            if (swxb181120001 == null || swxb181120001.equals("") || swxb181120001.equals("null")) {
                swxb181120001 = amoffwork;
            }
            String xwsb181120001 = String.valueOf(map.get("xwsb181120001"));
            if (xwsb181120001 == null || xwsb181120001.equals("") || xwsb181120001.equals("null")) {
                xwsb181120001 = pmtowork;
            }
            String xwxb181120001 = String.valueOf(map.get("xwxb181120001"));
            double gztime = DateHelper.getHour(swsb181120001, swxb181120001) + DateHelper.getHour(xwsb181120001, xwxb181120001);
            if (gztime > 0 && gztime < oahour) {
                BigDecimal bigDecimal = new BigDecimal(gztime / oahour);
                sumdate += bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            } else {
                sumdate += 1.00;
            }
        }
        return sumdate;
    }

    public double jx(String userId, String year, String month) {
        String sql = "SELECT khry181129001,SUM(pf18112900001) AS totalscores FROM jxkhp18112901 WHERE curStatus=2 AND khry181129001='" + userId + "' AND YEAR(khrq181129001) = " + year + " and MONTH(khrq181129001) = " + month + " GROUP BY khry181129001";
        Map<String, Object> sqlval = tableService.selectSqlMap(sql);
        double ygkh = 0.00;
        if (sqlval != null) {
            BigDecimal totalscores = (BigDecimal) sqlval.get("totalscores");
            ygkh = totalscores.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        }
        return ygkh;
    }

}
