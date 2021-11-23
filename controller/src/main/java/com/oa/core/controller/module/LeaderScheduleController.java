package com.oa.core.controller.module;

import com.oa.core.bean.module.Department;
import com.oa.core.bean.module.Employees;
import com.oa.core.service.module.DepartmentService;
import com.oa.core.service.module.EmployeesService;
import com.oa.core.service.util.TableService;
import com.oa.core.util.MySqlUtil;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @ClassName:LeaderScheduleController
 * @author:wsx
 * @Date:2019/1/4
 * @Time:下午 4:12
 * @Version V1.0
 * @Explain 领导日程
 */
@Controller
@RequestMapping("/leaderSchedule")
public class LeaderScheduleController {
    @Autowired
    TableService tableService;

    @Autowired
    EmployeesService employeesService;

    @Autowired
    DepartmentService departmentService;

    //跳转领导日程页面
    @RequestMapping(value = "/gotoLeaderSchedule", method = RequestMethod.GET)
    public ModelAndView gotoLeaderSchedule(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("module/leaderSchedule");
        String table = "ldrc190104001";
        String fields = "rcbt190104001,rcnr190104001,jssj190104001,kssj190104001,rczl190104001,xgld190104001,txr1901040001";
        List<String> where = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        String date = year + "-" + month;
        if(month<10){
            date = year + "-0" + month;
        }
        where.add("DATE_FORMAT(kssj190104001, '%Y-%m') <= '"+date+"' and DATE_FORMAT(jssj190104001, '%Y-%m') >= '"+date+"'");
        String sql = MySqlUtil.getFieldSql(fields,table,where,"kssj190104001 asc");
        List<Map<String,Object>> list = tableService.selectSqlMapList(sql);
        List<Map<String,Object>> list1 = new ArrayList<>();
        List<Map<String,Object>> list2 = new ArrayList<>();
        for(Map<String,Object> map : list){
            String startTime = String.valueOf(map.get("kssj190104001"));
            String endTime = String.valueOf(map.get("jssj190104001"));
            startTime = startTime.substring(0,4)+"年"+startTime.substring(5,7)+"月"+startTime.substring(8,10)+"日";
            endTime = endTime.substring(0,4)+"年"+endTime.substring(5,7)+"月"+endTime.substring(8,10)+"日";
            map.put("kssj190104001",startTime);
            map.put("jssj190104001",endTime);
            Employees employees = employeesService.selectByUserId(String.valueOf(map.get("xgld190104001")));
            Department department = departmentService.selectById(employees.getDepartment());
            map.put("xgld190104001",employees.getStaffName());
            map.put("department",department.getDeptName());
            if(String.valueOf(map.get("rcnr190104001")).length()>100){
                map.put("rcnr",String.valueOf(map.get("rcnr190104001")).substring(0,100)+"......");
            }else{
                map.put("rcnr",String.valueOf(map.get("rcnr190104001")));
            }
            if(map.get("rczl190104001").equals("会议安排")){
                list1.add(map);
            }else if(map.get("rczl190104001").equals("重大事项")){
                list2.add(map);
            }
        }
        mav.addObject("list1",list1);
        mav.addObject("list2",list2);
        return mav;
    }

    //根据时间获取领导日程
    @RequestMapping(value = "/getLeaderScheduleByDate", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String getLeaderScheduleByDate(HttpServletRequest request,String date){
        String table = "ldrc190104001";
        String fields = "rcbt190104001,rcnr190104001,jssj190104001,kssj190104001,rczl190104001,xgld190104001,txr1901040001";
        List<String> where = new ArrayList<>();
        where.add("DATE_FORMAT(kssj190104001, '%Y-%m') <= '"+date+"' and DATE_FORMAT(jssj190104001, '%Y-%m') >= '"+date+"'");
        String sql = MySqlUtil.getFieldSql(fields,table,where,"kssj190104001 asc");
        List<Map<String,Object>> list = tableService.selectSqlMapList(sql);
        for(Map<String,Object> map : list){
            String startTime = String.valueOf(map.get("kssj190104001"));
            String endTime = String.valueOf(map.get("jssj190104001"));
            startTime = startTime.substring(0,4)+"年"+startTime.substring(5,7)+"月"+startTime.substring(8,10)+"日";
            endTime = endTime.substring(0,4)+"年"+endTime.substring(5,7)+"月"+endTime.substring(8,10)+"日";
            map.put("kssj190104001",startTime);
            map.put("jssj190104001",endTime);
            Employees employees = employeesService.selectByUserId(String.valueOf(map.get("xgld190104001")));
            Department department = departmentService.selectById(employees.getDepartment());
            map.put("xgld190104001",employees.getStaffName());
            map.put("department",department.getDeptName());
            if(String.valueOf(map.get("rcnr190104001")).length()>100){
                map.put("rcnr",String.valueOf(map.get("rcnr190104001")).substring(0,100)+"......");
            }else{
                map.put("rcnr",String.valueOf(map.get("rcnr190104001")));
            }
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("list", list);

        return jsonObject.toString();
    }

}
