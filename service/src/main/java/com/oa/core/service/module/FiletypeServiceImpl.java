package com.oa.core.service.module;

import com.oa.core.bean.module.Filetype;
import com.oa.core.dao.module.FiletypeDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.List;

/**
 * @ClassName:FiletypeServiceImpl
 * @author:wsx
 * @Date:2018/11/19
 * @Time:上午 10:58
 * @Version V1.0
 * @Explain
 */
@Service("filetypeService")
public class FiletypeServiceImpl implements FiletypeService {

    @Resource
    private FiletypeDao filetypeDao;

    @Override
    public void insert(Filetype filetype) {
        filetypeDao.insert(filetype);
    }

    @Override
    public void delete(@Param("fileTypeId") String fileTypeId, @Param("deleteName") String deleteName, @Param("deleteTime") Timestamp deleteTime) {
        filetypeDao.delete(fileTypeId, deleteName, deleteTime);
    }

    @Override
    public void update(Filetype filetype) {
        filetypeDao.update(filetype);
    }

    @Override
    public Filetype selectById(@Param("fileTypeId") String fileTypeId) {
        return filetypeDao.selectById(fileTypeId);
    }

    @Override
    public List<Filetype> selectAllTerms(Filetype filetype) {
        return filetypeDao.selectAllTerms(filetype);
    }

    @Override
    public int selectAllTermsCont(Filetype filetype) {
        return filetypeDao.selectAllTermsCont(filetype);
    }

    @Override
    public List<Filetype> getFiletypesByRoleId(List<String> list) {
        return filetypeDao.getFiletypesByRoleId(list);
    }
}
