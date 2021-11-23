package com.oa.core.service.module;

import com.oa.core.bean.module.Employees;
import com.oa.core.bean.user.UserManager;
import com.oa.core.dao.module.EmployeesDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.List;

/**
 * @ClassName:EmployeesServiceImpl
 * @author:zxd
 * @Date:2018/10/13
 * @Time:下午 3:22
 * @Version V1.0
 * @Explain
 */
@Service("employeesService")
public class EmployeesServiceImpl implements EmployeesService {
    @Resource
    private EmployeesDao employeesDao;

    @Override
    public void insert(Employees employees) {
        employeesDao.insert(employees);
    }

    @Override
    public void delete(String staffId, String deleteName, Timestamp deleteTime) {
        employeesDao.delete(staffId,deleteName,deleteTime);
    }

    @Override
    public void update(Employees employees) {
        employeesDao.update(employees);
    }

    @Override
    public List<Employees> selectAll() {
        return employeesDao.selectAll();
    }

    @Override
    public Employees selectById(String staffId) {
        return employeesDao.selectById(staffId);
    }

    @Override
    public Employees selectByUserId(String userName) {
        return employeesDao.selectByUserId(userName);
    }

    @Override
    public List<Employees> selectByIds(List<String> staffIds) {
        return employeesDao.selectByIds(staffIds);
    }

    @Override
    public Employees selectTerms(Employees employees) {
        return employeesDao.selectTerms(employees);
    }

    @Override
    public List<Employees> selectAllTerms(Employees employees) {
        return employeesDao.selectAllTerms(employees);
    }

    @Override
    public int selectAllTermsCont(Employees employees) {
        return employeesDao.selectAllTermsCont(employees);
    }

    @Override
    public UserManager selectByUserName(String userName) {
        return employeesDao.selectByUserName(userName);
    }

    @Override
    public List<UserManager> selectByUserNames(List<String> userNames) {
        return employeesDao.selectByUserNames(userNames);
    }

    @Override
    public List<Employees> selectByUserNames_emp(List<String> userName) {
        return employeesDao.selectByUserNames_emp(userName);
    }

    @Override
    public List<Employees> selectAllDept(String deptId) {
        return employeesDao.selectAllDept(deptId);
    }

    @Override
    public List<Employees> selectAllNotDept(String deptId) {
        return employeesDao.selectAllNotDept(deptId);
    }
}
