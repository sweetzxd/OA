package com.oa.core.dao.system;

import com.oa.core.bean.system.Warning;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WarningDao {

    void insert(Warning warning);
    void delete(@Param("warningId")String warningId,@Param("deleteName")String deleteName,@Param("deleteTime")String deleteTime);
    void update(Warning warning);
    List<Warning> selectAll(@Param("type") String type);
}
