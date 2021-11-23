package com.oa.core.service.module;

import com.oa.core.bean.module.Festival;
import com.oa.core.dao.module.FestivalDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * @ClassName:FestivalServiceImpl
 * @author:wsx
 * @Date:2018/11/24
 * @Time:上午 11:30
 * @Version V1.0
 * @Explain
 */
@Service("festivalService")
public class FestivalServiceImpl implements FestivalService{

    @Resource
    private FestivalDao festivalDao;

    @Override
    public void insert(Festival festival) {
        festivalDao.insert(festival);
    }

    @Override
    public void delete(String festivalId, String deleteName, Timestamp deleteTime) {
        festivalDao.delete(festivalId,deleteName,deleteTime);
    }

    @Override
    public void update(Festival festival) {
        festivalDao.update(festival);
    }

    @Override
    public Festival selectById(String festivalId) {
        return festivalDao.selectById(festivalId);
    }

    @Override
    public List<Festival> selectAllTerms(Festival festival) {
        return festivalDao.selectAllTerms(festival);
    }

    @Override
    public int selectAllTermsCont(Festival festival) {
        return festivalDao.selectAllTermsCont(festival);
    }

    @Override
    public void deletes(String festivalId, String deleteName, Timestamp deleteTime) {
        festivalDao.deletes(festivalId,deleteName,deleteTime);
    }

    @Override
    public List<Map<String, Object>> getAllByYearAndMonth(int year, int month) {
        return festivalDao.getAllByYearAndMonth(year,month);
    }

    @Override
    public int secountdep(String  depname) {
        return festivalDao.secountfwdep(depname);
    }
}
