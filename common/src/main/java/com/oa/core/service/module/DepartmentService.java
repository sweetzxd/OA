package com.oa.core.service.module;

import com.oa.core.bean.module.Department;
import com.oa.core.bean.user.UserManager;
import org.apache.ibatis.annotations.Param;

import java.sql.Timestamp;
import java.util.List;

public interface DepartmentService {

    void insert(Department deprecated);

    void delete(@Param("deptId") String deptId, @Param("deleteName") String deleteName, @Param("deleteTime") Timestamp deleteTime);

    void update(Department deprecated);

    List<Department> selectAll();

    Department selectById(@Param("deptId") String deptId);

    List<Department> selectByIds(List<String> deptIds);

    Department selectTerms(Department deprecated);

    List<Department> selectAllTerms(Department deprecated);

    int selectAllTermsCont(Department deprecated);

    void updateEmp(@Param("department") String department, @Param("staffIds") List<String> staffIds, @Param("modifyName") String modifyName, @Param("modifyTime") Timestamp modifyTime);

    void deleteEmp(@Param("department") String department, @Param("modifyName") String modifyName, @Param("modifyTime") Timestamp modifyTime);

    List<Department> selectDeptInput(@Param("deptId") String deptId,@Param("startRow") int startRow,@Param("endRow") int endRow);

    int selectDeptInputByCont(@Param("deptId") String deptId);

    Department getDepartmentByDeptName(@Param("deptName") String deptName);
}
