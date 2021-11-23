package com.oa.core.service.module;

import com.oa.core.bean.module.UpdatePackage;
import org.apache.ibatis.annotations.Param;

import java.sql.Timestamp;
import java.util.List;

public interface UpdatePackageService {

    void insert(UpdatePackage pack);
    void delete(@Param("packId") String packId, @Param("deleteName") String deleteName, @Param("deleteTime") Timestamp deleteTime);
    void deletes(@Param("packIds") List<String> packIds, @Param("deleteName") String deleteName, @Param("deleteTime") Timestamp deleteTime);
    void update(UpdatePackage pack);
    List<UpdatePackage> selectAll();
    UpdatePackage selectById(@Param("packId") String packId);
    List<UpdatePackage> selectByIds(@Param("packIds") List<String> packIds,@Param("startRow") int startRow,@Param("endRow") int endRow);
    int selectByIdsCount(@Param("packIds") List<String> packIds);
    List<UpdatePackage> selectTerms(UpdatePackage pack);
    int selectTermsCount(UpdatePackage pack);
}
