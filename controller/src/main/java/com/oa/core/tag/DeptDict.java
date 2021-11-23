package com.oa.core.tag;

import com.oa.core.bean.module.Department;
import com.oa.core.bean.module.Employees;
import com.oa.core.service.module.DepartmentService;
import com.oa.core.service.module.EmployeesService;
import com.oa.core.util.SpringContextUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName:DeptDict
 * @author:zxd
 * @Date:2018/10/15
 * @Time:下午 3:32
 * @Version V1.0
 * @Explain
 */
public class DeptDict {

    private static Map<String, Department> data = null;
    private static Map<String, Department> userDept = null;

    static {
        initData();
    }

    public static void resetData() {
        if (data != null) {
            data.clear();
        }
        data = null;
    }

    public static void initData() {
        DepartmentService m = (DepartmentService) SpringContextUtil.getBean("departmentService");
        List<Department> uList = m.selectAll();
        if (uList.size() > 0) {
            data = new HashMap<String, Department>();
            for (Department u : uList) {
                data.put(u.getDeptId(), u);
            }
        }
    }

    public static void initUserDept() {
        EmployeesService e = (EmployeesService) SpringContextUtil.getBean("employeesService");
        List<Employees> eList = e.selectAll();
        if (eList.size() > 0) {
            userDept = new HashMap<String, Department>();
            for (Employees emp : eList) {
                String userName = emp.getUserName();
                String dept = emp.getDepartment();
                userDept.put(userName, getData(dept));
            }
        }
    }

    /**
     * 重新从数据库中获取数据
     *
     * @param name 部门的编号
     */
    public static void initDeptData(String name) {
        DepartmentService m = (DepartmentService) SpringContextUtil.getBean("departmentService");
        Department dept = m.selectById(name);
        if (dept != null) {
            data.put(dept.getDeptId(), dept);
        }
    }

    public static void initUserDeptData(String name) {
        EmployeesService e = (EmployeesService) SpringContextUtil.getBean("employeesService");
        Employees emp = e.selectById(name);
        if (emp != null) {
            String userName = emp.getUserName();
            String dept = emp.getDepartment();
            userDept.put(userName, getData(dept));
        }
    }

    public static Department getData(String name) {
        if (data == null) {
            initData();
        }
        if (data != null) {
            Department result = data.get(name);
            if (result == null) {
                initDeptData(name);
                return data.get(name);
            } else {
                return result;
            }
        }
        return null;
    }

    public static Department getDept(String user) {
        if (userDept == null) {
            initUserDept();
        }
        if (userDept != null) {
            Department result = userDept.get(user);
            if (result == null) {
                initUserDeptData(user);
                return userDept.get(user);
            } else {
                return result;
            }
        }
        return null;
    }

    public static String getName(String name) {
        Department dept = getData(name);
        if (dept != null) {
            return " " + dept.getDeptName() + " ";
        } else {
            if (name != null && name.equals("organize")) {
                name = "";
            }
            return " " + name + " ";
        }

    }

    public static String getNames(String names, String split) {
        String name = "";
        String[] name1 = names.split(split);
        for (int i = 0; i < name1.length; i++) {
            Department dept = getData(name1[i]);
            if (dept != null) {
                name += dept.getDeptName() + " ";
            } else {
                if (name != null && name.equals("organize")) {
                    name = "";
                }
                name += name + " ";
            }
        }
        return " " + name;
    }

    public static String getUserDept(String user){
        Department dept = getDept(user);
        if (dept != null) {
            return dept.getDeptId();
        } else {
            return "";
        }
    }

    public static Department getDeptIdandName(String user){
        Department dept = getDept(user);
        if (dept != null) {
            return dept;
        } else {
            return null;
        }
    }

    public static String getUserDepts(String users,String split){
        String val = "";
        String[] name = users.split(split);
        for (int i = 0; i < name.length; i++) {
            Department dept = getDept(name[i]);
            if (dept != null) {
                val += dept.getDeptId()+";";
            }
        }
        return val;
    }
}
