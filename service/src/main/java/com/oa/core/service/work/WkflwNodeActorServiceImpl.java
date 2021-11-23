package com.oa.core.service.work;

import com.oa.core.bean.work.WkflwNodeActor;
import com.oa.core.dao.work.WkflwNodeActorDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName:WkflwNodeActorServiceImpl
 * @author:zxd
 * @Date:2018/09/28
 * @Time:上午 9:06
 * @Version V1.0
 * @Explain
 */
@Service("wkflwNodeActorService")
public class WkflwNodeActorServiceImpl implements WkflwNodeActorService {
    @Resource
    private WkflwNodeActorDao wkflwNodeActorDao;

    @Override
    public void insert(WkflwNodeActor wkflwNodeActor) {
        wkflwNodeActorDao.insert(wkflwNodeActor);
    }

    @Override
    public void delete(WkflwNodeActor wkflwNodeActor) {
        wkflwNodeActorDao.delete(wkflwNodeActor);
    }

    @Override
    public void deletes(List nodeActorId) {
        wkflwNodeActorDao.deletes(nodeActorId);
    }

    @Override
    public void deleteByNode(String nodeId) {
        wkflwNodeActorDao.deleteByNode(nodeId);
    }

    @Override
    public void update(WkflwNodeActor wkflwNodeActor) {
        wkflwNodeActorDao.update(wkflwNodeActor);
    }

    @Override
    public List<WkflwNodeActor> selectAll() {
        return wkflwNodeActorDao.selectAll();
    }

    @Override
    public List<WkflwNodeActor> selectTerms(WkflwNodeActor wkflwNodeActor) {
        return wkflwNodeActorDao.selectTerms(wkflwNodeActor);
    }

    @Override
    public List<WkflwNodeActor> selectByIds(String nodeActorIds) {
        return wkflwNodeActorDao.selectByIds(nodeActorIds);
    }

    @Override
    public WkflwNodeActor selectById(String nodeActorId) {
        return wkflwNodeActorDao.selectById(nodeActorId);
    }

    @Override
    public List<WkflwNodeActor> selectByNodeId(String nodeId) {
        return wkflwNodeActorDao.selectByNodeId(nodeId);
    }

    @Override
    public ArrayList<String> selectListActor(String nodeId) {
        return wkflwNodeActorDao.selectListActor(nodeId);
    }

    @Override
    public List<WkflwNodeActor> selectwkflwId(String wkflwId) {
        return wkflwNodeActorDao.selectwkflwId(wkflwId);
    }

}
