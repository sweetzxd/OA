package com.oa.core.service.user;

import com.oa.core.bean.user.UserManager;
import org.apache.ibatis.annotations.Param;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

public interface UserManagerService {
    /**
     * 添加用户信息
     * @param userManager
     */
    void insertUser(UserManager userManager);
    /**
     * 删除用户
     * @param userName
     */
    void delete(@Param("userName")String userName, @Param("deleteName")String deleteName, @Param("deleteTime") Timestamp deleteTime);
    /**
     * 删除用户
     * @param userManager
     */
    void deleteUser(UserManager userManager);

    /**
     * 删除用户
     * @param userManager
     */
    void deleteAllUser(UserManager userManager);

    /**
     * 修改用户信息
     * @param userManager
     */
    void updateUser(UserManager userManager);
    /**
     * 修改用户信息
     * @param userManager
     * 多userName
     */
    void updateUserAll(UserManager userManager);
    /**
     * 根据ID获取用户信息
     * @param userName
     * @return UserManager
     */
    UserManager selectUserById(String userName);
    /**
     * 根据ID获取用户信息
     * @param userName
     * @return List<UserManager>
     */
    List<UserManager> selectUserByIds(String userName);
    /**
     * 根据查询条件获取用户
     * @param userManager
     * @return UserManager
     */
    UserManager selectUser(UserManager userManager);

    /**
     * 根据查询条件获取用户
     * @param userManager
     * @return  List<UserManager>
     */
    List<UserManager> selectUserAll(UserManager userManager);

    /**
     * 获取所有用户信息
     * @return
     */
    List<UserManager> selectAll();

    /**
     * 获取所有用户数量
     * @return int
     */
    int selectUserCount();
    /**
     * 根据查询条件获取用户数量
     * @param userManager
     * @return  int
     */
    int selectUserByCount(UserManager userManager);

    List<Map<String, String>> selectUserInfo(@Param("userName") String userName,@Param("staffName") String staffName,@Param("deptId") String deptId,@Param("deptName") String deptName,@Param("startRow") int startRow,@Param("endRow") int endRow);
    int selectUserInfoByCount(@Param("userName") String userName,@Param("staffName") String staffName,@Param("deptId") String deptId,@Param("deptName") String deptName);

    List<Map<String, String>> selectUserByDept(@Param("deptId") String deptId,@Param("startRow") int startRow, @Param("endRow") int endRow);
    int selectUserByDeptCount(@Param("deptId") String deptId);

    //跟据姓名查询user
    List<UserManager> getUserByName(@Param("name") String name);
}
