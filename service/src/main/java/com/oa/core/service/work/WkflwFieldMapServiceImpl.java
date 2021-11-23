package com.oa.core.service.work;

import com.oa.core.bean.work.WkflwFieldMap;
import com.oa.core.dao.work.WkflwFieldMapDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName:WkflwFieldMapServiceImpl
 * @author:wsx
 * @Date:2018/11/12
 * @Time:上午 11:07
 * @Version V1.0
 * @Explain
 */
@Service("wkflwFieldMapService")
public class WkflwFieldMapServiceImpl implements WkflwFieldMapService{

    @Resource
    private WkflwFieldMapDao wkflwFieldMapDao;

    @Override
    public void insert(WkflwFieldMap wkflwFieldMap) {
        wkflwFieldMapDao.insert(wkflwFieldMap);
    }

    @Override
    public void delete(WkflwFieldMap wkflwFieldMap) {
        wkflwFieldMapDao.delete(wkflwFieldMap);
    }

    @Override
    public void deletes(WkflwFieldMap wkflwFieldMap) {
        wkflwFieldMapDao.deletes(wkflwFieldMap);
    }

    @Override
    public void update(WkflwFieldMap wkflwFieldMap) {
        wkflwFieldMapDao.update(wkflwFieldMap);
    }

    @Override
    public WkflwFieldMap selectWkflwFieldMapById(String wkflwfieldmapId) {
        return wkflwFieldMapDao.selectWkflwFieldMapById(wkflwfieldmapId);
    }

    @Override
    public List<WkflwFieldMap> selectWkflwFieldMapByWkflwId(String wkflwId) {
        return wkflwFieldMapDao.selectWkflwFieldMapByWkflwId(wkflwId);
    }

    @Override
    public List<WkflwFieldMap> selectWkflwFieldMap(WkflwFieldMap wkflwFieldMap) {
        return wkflwFieldMapDao.selectWkflwFieldMap(wkflwFieldMap);
    }

    @Override
    public int selectWkflwFieldMapByCount(WkflwFieldMap wkflwFieldMap) {
        return wkflwFieldMapDao.selectWkflwFieldMapByCount(wkflwFieldMap);
    }

    @Override
    public WkflwFieldMap selectWkflwFieldMapByFormFieldName(String formFieldName){
        return wkflwFieldMapDao.selectWkflwFieldMapByFormFieldName(formFieldName);
    }
}
