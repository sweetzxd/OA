package com.oa.core.dao.module;

import com.oa.core.bean.module.Employees;
import com.oa.core.bean.user.UserManager;
import org.apache.ibatis.annotations.Param;

import java.sql.Timestamp;
import java.util.List;

public interface EmployeesDao {

    void insert(Employees employees);
    void delete(@Param("staffId")String staffId, @Param("deleteName")String deleteName, @Param("deleteTime") Timestamp deleteTime);
    void update(Employees employees);
    List<Employees> selectAll();
    Employees selectById(@Param("staffId")String staffId);
    Employees selectByUserId(@Param("userName")String userName);
    List<Employees> selectByIds(List<String> staffIds);
    Employees selectTerms(Employees employees);
    List<Employees> selectAllTerms(Employees employees);
    int selectAllTermsCont(Employees employees);
    UserManager selectByUserName(@Param("userName")String userName);
    List<Employees> selectByUserNames_emp(@Param("userNames") List<String> userNames);
    List<UserManager> selectByUserNames(List<String> userNames);
    List<Employees> selectAllDept(@Param("deptId")String deptId);
    List<Employees> selectAllNotDept(@Param("deptId")String deptId);
}
