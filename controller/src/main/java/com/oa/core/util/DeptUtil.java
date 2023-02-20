package com.oa.core.util;

import com.oa.core.bean.module.Department;
import com.oa.core.bean.module.Employees;
import com.oa.core.service.module.DepartmentService;
import com.oa.core.service.module.EmployeesService;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName:DeptUtil
 * @author:zxd
 * @Date:2018/10/15
 * @Time:下午 3:58
 * @Version V1.0
 * @Explain
 */
public class DeptUtil {
	
    static EmployeesService employeesService = (EmployeesService) SpringContextUtil.getBean("employeesService");
    static DepartmentService departmentService = (DepartmentService) SpringContextUtil.getBean("departmentService");
    public static String getMenu(List<Department> deptlist,boolean spread){
        return getMenu(deptlist,spread,false,false);
    }
	
    public static String getMenu(List<Department> deptlist,boolean spread,boolean empname,boolean empnum){
        String menu = "";
        if (deptlist != null) {
            List<Map<String, Object>> lmap = new ArrayList<>();
            Map<String, Object> maps;
            for (int n = 0; n < deptlist.size(); n++) {
                Department dept = deptlist.get(n);
                String id = dept.getDeptId();
                String pid = dept.getSuperiorDeptId();
                String title = dept.getDeptName();
                int menuNum = 1;
                if (pid!=null && pid.equals("organize")) {
                    maps = new HashMap<>();
                    maps.put("id", id);
                    maps.put("name", title);
                    maps.put("spread", spread);
                    maps.put("alias", title);
                    maps.put("menuNum", menuNum);
                    maps.put("children", getNext(menuNum,id,spread,empname,empnum));
                    if(empnum) {
                        maps.put("empNum", getEmpNum(id));
                    }
                    if(empname) {
                        maps.put("empName", getEmpName(id));
                    }
                    lmap.add( maps);
                }
            }
            JSONArray json = new JSONArray(lmap);
            menu = json.toString();
        }
        return menu;
    }

    public static String getMenu2(List<Department> deptlist,boolean spread,boolean empname,boolean empnum){
        String menu = "";
        if (deptlist != null) {
            List<Map<String, Object>> lmap = new ArrayList<>();
            Map<String, Object> maps;
            for (int n = 0; n < deptlist.size(); n++) {
                Department dept = deptlist.get(n);
                String id = dept.getDeptId();
                String pid = dept.getSuperiorDeptId();
                String title = dept.getDeptName();
                int menuNum = 1;
                if (pid!=null && pid.equals("organize")) {
                    maps = new HashMap<>();
                    maps.put("id", id);
                    maps.put("name", title);
                    maps.put("spread", spread);
                    maps.put("alias", title);
                    maps.put("menuNum", menuNum);
                    if(empnum) {
                        maps.put("empNum", getEmpNum(id));
                    }
                    if(empname) {
                        maps.put("empName", getEmpName(id));
                    }
                    lmap.add(maps);
                    lmap.addAll(getNext(menuNum,id,spread,empname,empnum));
                }
            }
            JSONArray json = new JSONArray(lmap);
            menu = json.toString();
        }
        return menu;
    }

    public static List<Map<String, Object>> getNext(int num, String superiorDeptId,boolean spread,boolean empname,boolean empnum){
        Department nextDept = new Department();
        nextDept.setSuperiorDeptId(superiorDeptId);
        List<Department> deptlist = departmentService.selectAllTerms(nextDept);
        Map<String, Object> maps;
        List<Map<String, Object>> lmap = new ArrayList<>();
        for (int n = 0; n < deptlist.size(); n++) {
            Department dept = deptlist.get(n);
            String id = dept.getDeptId();
            String title = dept.getDeptName();
            int menuNum = num+1;
            maps = new HashMap<>();
            maps.put("id", id);
            maps.put("name", title);
            maps.put("spread", spread);
            maps.put("alias", title);
            maps.put("menuNum", menuNum);
            maps.put("children", getNext(menuNum,id,spread,empname,empnum));
            if(empnum) {
                maps.put("empNum", getEmpNum(id));
            }
            if(empname) {
                maps.put("empName", getEmpName(id));
            }
            lmap.add(maps);
        }
        return lmap;
    }

    public static int getEmpNum(String deptid){
        if(deptid!=null && !deptid.equals("")) {
            Employees employees = new Employees();
            employees.setDepartment(deptid);
            return employeesService.selectAllTermsCont(employees);
        }else{
            return 0;
        }
    }

    public static List<Employees> getEmpName(String deptid){
        if(deptid!=null && !deptid.equals("")) {
            return employeesService.selectAllDept(deptid);
        }else{
            return null;
        }
    }
}
