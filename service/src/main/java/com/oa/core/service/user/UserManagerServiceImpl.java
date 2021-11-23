package com.oa.core.service.user;

import com.oa.core.bean.user.UserManager;
import com.oa.core.dao.user.UserManagerDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@Service("userManagerService")
public class UserManagerServiceImpl implements UserManagerService {

    @Resource
    private UserManagerDao userManagerDao;

    @Override
    public void insertUser(UserManager userManager) {
        userManagerDao.insertUser(userManager);
    }

    @Override
    public void delete(String userName, String deleteName, Timestamp deleteTime) {
        userManagerDao.delete(userName,deleteName,deleteTime);
    }

    @Override
    public void deleteUser(UserManager userManager) {
        userManagerDao.deleteUser(userManager);
    }

    @Override
    public void deleteAllUser(UserManager userManager) {
        userManagerDao.deleteAllUser(userManager);
    }

    @Override
    public void updateUser(UserManager userManager) {
        userManagerDao.updateUser(userManager);
    }

    @Override
    public void updateUserAll(UserManager userManager) {
        userManagerDao.updateUser(userManager);
    }

    @Override
    public UserManager selectUserById(String userName) {
        return userManagerDao.selectUserById(userName);
    }

    @Override
    public List<UserManager> selectUserByIds(String userName) {
        return userManagerDao.selectUserByIds(userName);
    }

    @Override
    public UserManager selectUser(UserManager userManager) {
        return userManagerDao.selectUser(userManager);
    }

    @Override
    public List<UserManager> selectUserAll(UserManager userManager) {
        return userManagerDao.selectUserAll(userManager);
    }

    @Override
    public List<UserManager> selectAll() {
        return userManagerDao.selectAll();
    }

    @Override
    public int selectUserCount() {
        return userManagerDao.selectUserCount();
    }

    @Override
    public int selectUserByCount(UserManager userManager) {
        return userManagerDao.selectUserByCount(userManager);
    }

    @Override
    public List<Map<String, String>> selectUserInfo(String userName, String staffName, String deptId, String deptName,int startRow,int endRow) {
        return userManagerDao.selectUserInfo(userName,staffName,deptId,deptName,startRow,endRow);
    }
    @Override
    public int selectUserInfoByCount(String userName,String staffName,String deptId,String deptName){
        return userManagerDao.selectUserInfoByCount(userName,staffName,deptId,deptName);
    }

    @Override
    public List<Map<String, String>> selectUserByDept(String deptId, int startRow, int endRow) {
        return userManagerDao.selectUserByDept(deptId,startRow,endRow);
    }

    @Override
    public int selectUserByDeptCount(String deptId) {
        return userManagerDao.selectUserByDeptCount(deptId);
    }

    //跟据姓名查询user
    @Override
    public List<UserManager> getUserByName(@Param("name") String name){
        return userManagerDao.getUserByName(name);
    }
}