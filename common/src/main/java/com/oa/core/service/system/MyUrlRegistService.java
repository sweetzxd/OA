package com.oa.core.service.system;

import com.oa.core.bean.system.MyUrlRegist;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MyUrlRegistService {

    void insert(MyUrlRegist myUrlRegist);
    void delete(MyUrlRegist myUrlRegist);
    void deleteAll(MyUrlRegist myUrlRegist);
    void update(MyUrlRegist myUrlRegist);
    MyUrlRegist selectById(String pageId);
    List<MyUrlRegist> selectByIds(String pageId);
    List<String> selectByFormId(MyUrlRegist myUrlRegist);
    List<MyUrlRegist> selectAll();
    List<MyUrlRegist> selectTerms(MyUrlRegist myUrlRegist);
    List<MyUrlRegist> selectNodeTerms(MyUrlRegist myUrlRegist);
    int selectAllCount();
    int selectTermsCount(MyUrlRegist myUrlRegist);

    String selectContextMenu(String pageId);
    void updateContextMenu(String contextmenu,String pageId);

    int getSecurity(@Param("parentId") String parentId);
    void updateSecurity(@Param("id") String id,@Param("num") int num,@Param("sid") String sid,@Param("snum") int snum);

    void reallyDelete(MyUrlRegist myUrlRegist);
}
