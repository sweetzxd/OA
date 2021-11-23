package com.oa.core.service.system;

import com.oa.core.bean.system.Warning;
import com.oa.core.dao.system.WarningDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName:WarningServiceImpl
 * @author:zxd
 * @Date:2018/10/12
 * @Time:下午 3:51
 * @Version V1.0
 * @Explain
 */
@Service("warningService")
public class WarningServiceImpl implements WarningService {
    @Resource
    private WarningDao warningDao;

    @Override
    public void insert(Warning warning) {
        warningDao.insert(warning);
    }

    @Override
    public void delete(String warningId, String deleteName, String deleteTime) {
        warningDao.delete(warningId,deleteName,deleteTime);
    }

    @Override
    public void update(Warning warning) {
        warningDao.update(warning);
    }

    @Override
    public List<Warning> selectAll(String type) {
        return warningDao.selectAll(type);
    }
}
