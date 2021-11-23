package com.oa.core.service.module;

import com.oa.core.bean.module.UpdatePackage;
import com.oa.core.dao.module.UpdatePackageDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.List;

/**
 * @ClassName:UpdatePackageServiceImpl
 * @author:zxd
 * @Date:2018/11/16
 * @Time:下午 4:03
 * @Version V1.0
 * @Explain
 */
@Service("updatePackageService")
public class UpdatePackageServiceImpl implements UpdatePackageService {

    @Resource
    private UpdatePackageDao updatePackageDao;

    @Override
    public void insert(UpdatePackage pack) {
        updatePackageDao.insert(pack);
    }

    @Override
    public void delete(String packId, String deleteName, Timestamp deleteTime) {
        updatePackageDao.delete(packId,deleteName,deleteTime);
    }

    @Override
    public void deletes(List<String> packIds, String deleteName, Timestamp deleteTime) {
        updatePackageDao.deletes(packIds,deleteName,deleteTime);
    }


    @Override
    public void update(UpdatePackage pack) {
        updatePackageDao.update(pack);
    }

    @Override
    public List<UpdatePackage> selectAll() {
        return updatePackageDao.selectAll();
    }

    @Override
    public UpdatePackage selectById(String packId) {
        return updatePackageDao.selectById(packId);
    }

    @Override
    public List<UpdatePackage> selectByIds(List<String> packIds, int startRow, int endRow) {
        return updatePackageDao.selectByIds(packIds,startRow,endRow);
    }

    @Override
    public int selectByIdsCount(List<String> packIds) {
        return updatePackageDao.selectByIdsCount(packIds);
    }

    @Override
    public List<UpdatePackage> selectTerms(UpdatePackage pack) {
        return updatePackageDao.selectTerms(pack);
    }

    @Override
    public int selectTermsCount(UpdatePackage pack) {
        return updatePackageDao.selectTermsCount(pack);
    }
}
