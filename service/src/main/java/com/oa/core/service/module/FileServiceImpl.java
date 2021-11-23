package com.oa.core.service.module;

import com.oa.core.bean.module.File;
import com.oa.core.dao.module.FileDao;
import com.oa.core.service.module.FileService;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.List;

/**
 * @ClassName:FileServiceImpl
 * @author:wsx
 * @Date:2018/11/19
 * @Time:上午 10:58
 * @Version V1.0
 * @Explain
 */
@Service("fileService")
public class FileServiceImpl implements FileService {

    @Resource
    private FileDao fileDao;

    @Override
    public void insert(File file) {
        fileDao.insert(file);
    }

    @Override
    public void delete(@Param("fileId") String fileId, @Param("deleteName") String deleteName, @Param("deleteTime") Timestamp deleteTime) {
        fileDao.delete(fileId, deleteName, deleteTime);
    }

    @Override
    public void update(File file) {
        fileDao.update(file);
    }

    @Override
    public File selectById(@Param("fileId") String fileId) {
        return fileDao.selectById(fileId);

    }

    @Override
    public List<File> selectAllTerms(File file) {
        return fileDao.selectAllTerms(file);
    }

    @Override
    public int selectAllTermsCont(File file) {
        return fileDao.selectAllTermsCont(file);
    }

    @Override
    public void deletes(@Param("fileId") String fileId, @Param("deleteName") String deleteName, @Param("deleteTime") Timestamp deleteTime) {
        fileDao.deletes(fileId, deleteName, deleteTime);
    }

}
