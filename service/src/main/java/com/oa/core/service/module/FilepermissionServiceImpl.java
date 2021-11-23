package com.oa.core.service.module;

import com.oa.core.bean.module.Filepermission;
import com.oa.core.dao.module.FilepermissionDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.List;

/**
 * @ClassName:FilepermissionServiceImpl
 * @author:wsx
 * @Date:2018/11/19
 * @Time:上午 10:56
 * @Version V1.0
 * @Explain
 */
@Service("filepermissionService")
public class FilepermissionServiceImpl implements FilepermissionService{

    @Resource
    private FilepermissionDao filepermissionDao;

    @Override
    public void insert(Filepermission filepermission){
        filepermissionDao.insert(filepermission);
    }

    @Override
    public void delete(@Param("permissionId") String permissionId, @Param("deleteName") String deleteName, @Param("deleteTime") Timestamp deleteTime){
        filepermissionDao.delete(permissionId, deleteName, deleteTime);
    }

    @Override
    public void update(Filepermission filepermission){
        filepermissionDao.update(filepermission);
    }

    @Override
    public Filepermission selectById(@Param("permissionId") String permissionId){
        return filepermissionDao.selectById(permissionId);
    }

    @Override
    public List<Filepermission> selectAllTerms(Filepermission filepermission){
        return filepermissionDao.selectAllTerms(filepermission);
    }

    @Override
    public int selectAllTermsCont(Filepermission filepermission){
        return filepermissionDao.selectAllTermsCont(filepermission);
    }

    @Override
    public void deletes(@Param("permissionId") String permissionId, @Param("deleteName") String deleteName, @Param("deleteTime") Timestamp deleteTime){
        filepermissionDao.deletes(permissionId, deleteName, deleteTime);
    }

    @Override
    public List<String> getUserFileType(List<String> list){
        return filepermissionDao.getUserFileType(list);
    }
}
