package com.oa.core.service.user;

import com.oa.core.bean.user.UserComputer;
import com.oa.core.dao.user.UserComputerDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.List;

@Service("userComputerService")
public class UserComputerServiceImpl implements UserComputerService {
    @Resource
    private UserComputerDao userComputerDao;

    @Override
    public void insert(UserComputer userComputer) {
        userComputerDao.insert(userComputer);
    }

    @Override
    public void delete(String userName, String deleteName, Timestamp deleteTime) {
        userComputerDao.delete(userName,deleteName,deleteTime);
    }

    @Override
    public void update(UserComputer userComputer) {
        userComputerDao.update(userComputer);
    }

    @Override
    public UserComputer selectById(String id) {
        return userComputerDao.selectById(id);
    }

    @Override
    public List<UserComputer> selectAll() {
        return userComputerDao.selectAll();
    }

    @Override
    public List<UserComputer> selectTerms(UserComputer userComputer) {
        return userComputerDao.selectTerms(userComputer);
    }

    @Override
    public int selectAllCount() {
        return userComputerDao.selectAllCount();
    }

    @Override
    public int selectTermsCount(UserComputer userComputer) {
        return userComputerDao.selectTermsCount(userComputer);
    }

    @Override
    public UserComputer selectUserName(String userName) {
        return userComputerDao.selectUserName(userName);
    }

}
