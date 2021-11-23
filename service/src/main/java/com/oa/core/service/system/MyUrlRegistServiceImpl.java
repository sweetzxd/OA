package com.oa.core.service.system;

import com.oa.core.bean.system.MyUrlRegist;
import com.oa.core.dao.system.MyUrlRegistDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("myUrlRegistService")
public class MyUrlRegistServiceImpl implements MyUrlRegistService {

    @Resource
    private MyUrlRegistDao myUrlRegistDao;


    @Override
    public void insert(MyUrlRegist myUrlRegist) {
        myUrlRegistDao.insert(myUrlRegist);
    }

    @Override
    public void delete(MyUrlRegist myUrlRegist) {
        myUrlRegistDao.delete(myUrlRegist);
    }

    @Override
    public void deleteAll(MyUrlRegist myUrlRegist) {
        myUrlRegistDao.deleteAll(myUrlRegist);
    }

    @Override
    public void update(MyUrlRegist myUrlRegist) {
        myUrlRegistDao.update(myUrlRegist);
    }

    @Override
    public MyUrlRegist selectById(String pageId) {
        return myUrlRegistDao.selectById(pageId);
    }

    @Override
    public List<MyUrlRegist> selectByIds(String pageId) {
        return myUrlRegistDao.selectByIds(pageId);
    }

    @Override
    public List<String> selectByFormId(MyUrlRegist myUrlRegist) { return myUrlRegistDao.selectByFormId(myUrlRegist); }

    @Override
    public List<MyUrlRegist> selectAll() {
        return myUrlRegistDao.selectAll();
    }

    @Override
    public int selectAllCount() {
        return myUrlRegistDao.selectAllCount();
    }

    @Override
    public List<MyUrlRegist> selectTerms(MyUrlRegist myUrlRegist) {
        return myUrlRegistDao.selectTerms(myUrlRegist);
    }

    @Override
    public List<MyUrlRegist> selectNodeTerms(MyUrlRegist myUrlRegist) {
        return myUrlRegistDao.selectNodeTerms(myUrlRegist);
    }

    @Override
    public int selectTermsCount(MyUrlRegist myUrlRegist) {
        return myUrlRegistDao.selectTermsCount(myUrlRegist);
    }

    @Override
    public String selectContextMenu(String pageId) {
        return myUrlRegistDao.selectContextMenu(pageId);
    }

    @Override
    public void updateContextMenu(String contextmenu, String pageId) {
        myUrlRegistDao.updateContextMenu(contextmenu,pageId);
    }

    @Override
    public int getSecurity(String parentId) {
        return myUrlRegistDao.getSecurity(parentId);
    }

    @Override
    public void updateSecurity(String id, int num, String sid, int snum) {
        myUrlRegistDao.updateSecurity(id, num, sid, snum);
    }

    @Override
    public void reallyDelete(MyUrlRegist myUrlRegist){
        myUrlRegistDao.reallyDelete(myUrlRegist);
    }
}
