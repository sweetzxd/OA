package com.oa.core.service.system;

import com.oa.core.bean.system.Warning;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WarningService {

    void insert(Warning warning);
    void delete(String warningId, String deleteName, String deleteTime);
    void update(Warning warning);
    List<Warning> selectAll(String type);
}
