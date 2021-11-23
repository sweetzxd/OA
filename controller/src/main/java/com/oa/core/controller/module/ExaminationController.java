package com.oa.core.controller.module;

import com.oa.core.bean.Loginer;
import com.oa.core.bean.dd.FieldData;
import com.oa.core.bean.dd.TaskData;
import com.oa.core.bean.module.Employees;
import com.oa.core.bean.system.FormCustomMade;
import com.oa.core.bean.util.PageUtil;
import com.oa.core.helper.DateHelper;
import com.oa.core.helper.StringHelper;
import com.oa.core.interceptor.Logined;
import com.oa.core.service.dd.DictionaryService;
import com.oa.core.service.module.DepartmentService;
import com.oa.core.service.module.EmployeesService;
import com.oa.core.service.system.FormCustomMadeService;
import com.oa.core.service.user.UserManagerService;
import com.oa.core.service.util.TableService;
import com.oa.core.system.postposition.PostPosition;
import com.oa.core.tag.DeptDict;
import com.oa.core.tag.UserDict;
import com.oa.core.util.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @ClassName:ExaminationController
 * @author:zxd
 * @Date:2018/11/29
 * @Time:上午 11:36
 * @Version V1.0
 * @Explain
 */

@Controller
@RequestMapping("/examination")
public class ExaminationController {

    @Autowired
    TableService tableService;
    @Autowired
    UserManagerService userManagerService;
    @Autowired
    EmployeesService employeesService;
    @Autowired
    DepartmentService departmentService;
    @Autowired
    DictionaryService dictionaryService;
    @Autowired
    FormCustomMadeService formCustomMadeService;

    @RequestMapping(value = "/gotoexamination")
    @ResponseBody
    public String gotoExamination(HttpServletRequest request,String year,String month,int limit,int page) {
        Calendar cale = Calendar.getInstance();
        if(year==null || year.equals("") || year.length()<4){
            year = String.valueOf(cale.get(Calendar.YEAR));
        }
        if(month==null || month.equals("")){
            month = String.valueOf(cale.get(Calendar.MONTH) + 1);
        }
        month =(month.length()==1 ? "0"+month:month);
        String where = "AND year(khrq181129001)='"+year+"' AND month(khrq181129001)='"+month+"'";
        StringBuffer sql = new StringBuffer("SELECT ");
        sql.append(" u.*,IFNULL(userscore,0) AS userscore,IFNULL(totalscores,0) AS totalscores,('"+year+"-"+month+"') AS date,IFNULL(reserveField,'1') AS examine ");
        sql.append(" FROM userinfo u");
        sql.append(" LEFT OUTER JOIN ");
        sql.append("(SELECT khry181129001,SUM(grpf181129001) AS userscore,SUM(pf18112900001) AS totalscores,reserveField FROM jxkhp18112901 WHERE curStatus=2 "+where+" GROUP BY khry181129001) j");
        sql.append(" ON u.userName=j.khry181129001 ");
        sql.append(" GROUP BY userName ");
        List<Map<String, Object>> count = tableService.selectSqlMapList(sql.toString());
        PageUtil pu = new PageUtil();
        pu.setPageSize(limit);
        pu.setCurrentPage(page);
        int startRow = pu.getStartRow();
        int endRow = pu.getEndRow() - pu.getStartRow();
        sql.append(" LIMIT " + startRow + " , " + endRow);
        List<Map<String, Object>> sqlval = tableService.selectSqlMapList(sql.toString());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("success",1);
        jsonObject.put("code", 0);
        jsonObject.put("msg", "");
        jsonObject.put("count", count.size());
        jsonObject.put("data", sqlval);
        jsonObject.put("is", true);
        jsonObject.put("year", year);
        jsonObject.put("month", month);
        jsonObject.put("tip", "操作成功");
        return jsonObject.toString();
    }

    @RequestMapping(value = "/getuservalue")
    public ModelAndView userExamination(HttpServletRequest request) {
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        String userId = loginer.getId();
        if(userId!=null && !userId.equals("admin")) {
            List<Map<String, String>> lm = userManagerService.selectUserInfo(userId, "", "", "", 0, 0);
            String deptId = lm.get(0).get("deptId");
            String date = DateHelper.getYMD();
            return userExamination(request,userId,deptId ,date);
        }else{
            return new ModelAndView("system/blank");
        }
    }

    @RequestMapping(value = "/gettablevalue")
    public ModelAndView userExamination(HttpServletRequest request,String userName,String deptId ,String date) {
        String l = request.getParameter("leader");
        String leader = (l!=null && l.equals("1"))?"1":"0";
        String[] ym = date.split("-");
        String field = "p.recorderNO,khdx181129002,khzl181129002,khnr181129002,khqz181129002,pfry181129002,ckyj181129002,zl18112900001,bz18112900001,khry181129001,ssbm181129001,khrq181129001,s.recorderNO AS khx1811290001,IFNULL(grpf181129001,0) AS grpf181129001,IFNULL(pf18112900001,0) AS pf18112900001,pfck181129001,bz18112900002,fj18112900001";
        StringBuffer sql = new StringBuffer("SELECT ");
        sql.append(field);
        sql.append(" FROM jxkhs18112901 s ");
        sql.append(" LEFT JOIN ");
        sql.append(" (select * from jxkhp18112901 where YEAR(khrq181129001)='"+ym[0]+"' AND MONTH(khrq181129001)='"+ym[1]+"' AND khry181129001='"+userName+"' AND curStatus=2) p ");
        sql.append(" ON s.recorderNO=p.khx1811290001 ");
        sql.append(" WHERE khdx181129002 LIKE('%"+deptId+";%')");

        List<Map<String, Object>> sqlval = tableService.selectSqlMapList(sql.toString());
        ModelAndView mav = new ModelAndView("module/examination");
        mav.addObject("type", "user");
        mav.addObject("userName", userName);
        mav.addObject("name", UserDict.getName(userName));
        mav.addObject("deptId", deptId);
        mav.addObject("deptName", DeptDict.getName(deptId));
        mav.addObject("date", date);
        mav.addObject("leader", leader);
        mav.addObject("tbodyval", sqlval);
        return mav;
    }

    @RequestMapping(value = "/examineput")
    public void examinePut(HttpServletRequest request, String userName, String deptId, String date) {
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        String userId = loginer.getId();
        String[] ym = date.split("-");
        StringBuffer sql = new StringBuffer("UPDATE jxkhp18112901 SET ");
        sql.append(" reserveField='3',modifyName='" + userId + "',modifyTime='" + DateHelper.now() + "' ");
        sql.append(" WHERE reserveField='2' AND khry181129001='" + userName + "' AND ssbm181129001='" + deptId + "' AND YEAR(khrq181129001)='" + ym[0] + "' AND MONTH(khrq181129001)='" + ym[1] + "'");
        try {
            tableService.updateSqlMap(sql.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public String formAddSave(HttpServletRequest request) {
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        String userId = loginer.getId();
        String tableName = "jxkhp18112901";
        String datas = request.getParameter("datas");
        JSONArray jsonArray = new JSONArray(datas);
        boolean insert = false;
        for(int i = 0; i < jsonArray.length(); i++){
            try {
                JSONObject subObject = jsonArray.getJSONObject(i);
                Map<String, Object> map = new HashMap<String, Object>();
                //主键
                String recorderNO = null;
                if (!subObject.isNull("recorderNO")) {
                    recorderNO = subObject.getString("recorderNO");
                }
                if (recorderNO == null || recorderNO.equals("") || recorderNO.length()<1) {
                    insert = true;
                    PrimaryKeyUitl pk = new PrimaryKeyUitl();
                    recorderNO = pk.getNextId(tableName, "recorderNO");
                    map.put("recordName", userId);
                    map.put("recordTime", DateHelper.now());
                }
                map.put("recorderNO", recorderNO);
                //考核人员
                map.put("khry181129001", subObject.getString("khry181129001"));
                //所属部门
                map.put("ssbm181129001", subObject.getString("ssbm181129001"));
                //考核日期
                map.put("khrq181129001", subObject.getString("khrq181129001") + "-" + Calendar.DATE);
                //绩效关联键
                map.put("khx1811290001", subObject.getString("khx1811290001"));
                //个人评分
                map.put("grpf181129001", subObject.getInt("grpf181129001"));
                //附件
                map.put("bz18112900002", subObject.getString("bz18112900002"));
                //备注
                map.put("fj18112900001", subObject.getString("fj18112900001"));
                if(subObject.getString("leader").equals("1")) {
                    //评分
                    map.put("pf18112900001", subObject.getInt("pf18112900001"));
                    map.put("reserveField", "2");
                }else{
                    //评分参考
                    map.put("pfck181129001", subObject.getString("pfck181129001"));
                    map.put("reserveField", "1");
                }
                map.put("modifyName", userId);
                map.put("modifyTime", DateHelper.now());
                String sql = "";
                if (insert) {
                    sql = MySqlUtil.getInsertSql(tableName, map);
                    tableService.insertSqlMap(sql);
                } else {
                    sql = MySqlUtil.getUpdateSql(tableName, recorderNO, map, null);
                    tableService.updateSqlMap(sql);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return "";
    }


}
