package com.oa.core.service.module;

import com.oa.core.bean.module.Festival;
import org.apache.ibatis.annotations.Param;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

public interface FestivalService {
    void insert(Festival festival);

    void delete(@Param("festivalId") String festivalId, @Param("deleteName") String deleteName, @Param("deleteTime") Timestamp deleteTime);

    void update(Festival festival);

    Festival selectById(@Param("festivalId") String festivalId);

    List<Festival> selectAllTerms(Festival festival);

    int selectAllTermsCont(Festival festival);

    void deletes(@Param("festivalId") String festivalId, @Param("deleteName") String deleteName, @Param("deleteTime") Timestamp deleteTime);

    List<Map<String,Object>> getAllByYearAndMonth(@Param("year") int year, @Param("month") int month);
    int secountdep(String depname);
}
