package com.oa.core.dao.system;

import com.oa.core.bean.system.MyUrlRegist;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MyUrlRegistDao {

    void insert(MyUrlRegist myUrlRegist);
    void delete(MyUrlRegist myUrlRegist);
    void deleteAll(MyUrlRegist myUrlRegist);
    void update(MyUrlRegist myUrlRegist);
    MyUrlRegist selectById(String pageId);
    List<MyUrlRegist> selectByIds(String pageId);
    List<String> selectByFormId(MyUrlRegist myUrlRegist);
    List<MyUrlRegist> selectAll();
    int selectAllCount();
    List<MyUrlRegist> selectTerms(MyUrlRegist myUrlRegist);
    List<MyUrlRegist> selectNodeTerms(MyUrlRegist myUrlRegist);
    int selectTermsCount(MyUrlRegist myUrlRegist);

    String selectContextMenu(@Param("pageId")String pageId);
    void updateContextMenu(@Param("contextmenu")String contextmenu,@Param("pageId")String pageId);

    int getSecurity(@Param("parentId") String parentId);
    void updateSecurity(@Param("id") String id,@Param("num") int num,@Param("sid") String sid,@Param("snum") int snum);

    void reallyDelete(MyUrlRegist myUrlRegist);
}
