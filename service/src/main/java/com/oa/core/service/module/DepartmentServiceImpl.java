package com.oa.core.service.module;

import com.oa.core.bean.module.Department;
import com.oa.core.dao.module.DepartmentDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.List;

/**
 * @ClassName:DepartmentServiceImpl
 * @author:zxd
 * @Date:2018/10/13
 * @Time:下午 3:21
 * @Version V1.0
 * @Explain
 */
@Service("departmentService")
public class DepartmentServiceImpl implements DepartmentService {
    @Resource
    private DepartmentDao departmentDao;

    @Override
    public void insert(Department department) {
        departmentDao.insert(department);
    }

    @Override
    public void delete(String deptId, String deleteName, Timestamp deleteTime) {
        departmentDao.delete(deptId, deleteName, deleteTime);
    }

    @Override
    public void update(Department department) {
        departmentDao.update(department);
    }

    @Override
    public List<Department> selectAll() {
        return departmentDao.selectAll();
    }

    @Override
    public Department selectById(String deptId) {
        return departmentDao.selectById(deptId);
    }

    @Override
    public List<Department> selectByIds(List<String> deptIds) {
        return departmentDao.selectByIds(deptIds);
    }

    @Override
    public Department selectTerms(Department department) {
        return departmentDao.selectTerms(department);
    }

    @Override
    public List<Department> selectAllTerms(Department department) {
        return departmentDao.selectAllTerms(department);
    }

    @Override
    public int selectAllTermsCont(Department department) {
        return departmentDao.selectAllTermsCont(department);
    }

    @Override
    public void updateEmp(String department, List<String> staffIds, String modifyName, Timestamp modifyTime) {
        departmentDao.updateEmp(department, staffIds, modifyName, modifyTime);
    }

    @Override
    public void deleteEmp(String department, String modifyName, Timestamp modifyTime) {
        departmentDao.deleteEmp(department, modifyName, modifyTime);
    }

    @Override
    public List<Department> selectDeptInput(String deptId, int startRow, int endRow) {
        return departmentDao.selectDeptInput(deptId,startRow,endRow);
    }

    @Override
    public int selectDeptInputByCont(String deptId) {
        return departmentDao.selectDeptInputByCont(deptId);
    }

    @Override
    public Department getDepartmentByDeptName(@Param("deptName") String deptName){
        return departmentDao.getDepartmentByDeptName(deptName);
    }
}
