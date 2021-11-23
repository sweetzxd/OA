package com.oa.core.service.user;

import com.oa.core.bean.user.UserComputer;
import org.apache.ibatis.annotations.Param;

import java.sql.Timestamp;
import java.util.List;

public interface UserComputerService {

    void insert(UserComputer userComputer);
    void delete(@Param("userName")String userName, @Param("deleteName")String deleteName, @Param("deleteTime") Timestamp deleteTime);
    void update(UserComputer userComputer);
    UserComputer selectById(String id);
    List<UserComputer> selectAll();
    List<UserComputer> selectTerms(UserComputer userComputer);
    int selectAllCount();
    int selectTermsCount(UserComputer userComputer);

    UserComputer selectUserName(@Param("userName")String userName);
}
