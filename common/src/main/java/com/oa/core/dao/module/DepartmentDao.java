package com.oa.core.dao.module;

import com.oa.core.bean.module.Department;
import org.apache.ibatis.annotations.Param;

import java.sql.Timestamp;
import java.util.List;

public interface DepartmentDao {

    void insert(Department department);

    void delete(@Param("deptId") String deptId, @Param("deleteName") String deleteName, @Param("deleteTime") Timestamp deleteTime);

    void update(Department department);

    List<Department> selectAll();

    Department selectById(@Param("deptId") String deptId);

    List<Department> selectByIds(@Param("deptIds") List<String> deptIds);

    Department selectTerms(Department department);

    List<Department> selectAllTerms(Department department);

    int selectAllTermsCont(Department department);

    void updateEmp(@Param("department") String department, @Param("staffIds") List<String> staffIds, @Param("modifyName") String modifyName, @Param("modifyTime") Timestamp modifyTime);

    void deleteEmp(@Param("department") String department, @Param("modifyName") String modifyName, @Param("modifyTime") Timestamp modifyTime);

    List<Department> selectDeptInput(@Param("deptId") String deptId,@Param("startRow") int startRow,@Param("endRow") int endRow);

    int selectDeptInputByCont(@Param("deptId") String deptId);

    Department getDepartmentByDeptName(@Param("deptName") String deptName);
}
