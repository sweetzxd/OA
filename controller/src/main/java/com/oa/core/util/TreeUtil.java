package com.oa.core.util;

/**
 * @ClassName:TreeUtil
 * @author:zxd
 * @Date:2019/04/24
 * @Time:下午 3:48
 * @Version V1.0
 * @Explain
 */

import com.oa.core.bean.system.MyUrlRegist;
import com.oa.core.service.system.MyUrlRegistService;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class TreeUtil {
    private static MyUrlRegistService myUrlRegistService = (MyUrlRegistService) SpringContextUtil.getBean("myUrlRegistService");
    public static JSONArray getTree(){
        JSONArray jsonArray = getChildren("topmenu");
        return jsonArray;
    }

    public static JSONArray getChildren(String parentId) {
        JSONArray jsonArray = new JSONArray();
        MyUrlRegist myurls = new MyUrlRegist();
        myurls.setParentId(parentId);
        myurls.setFormType(0);
        List<MyUrlRegist> myurllist = myUrlRegistService.selectTerms(myurls);
        for (MyUrlRegist myurl : myurllist) {
            String id = myurl.getPageId();
            String title = myurl.getPageTitle();
            JSONObject treeObject = new JSONObject();
            treeObject.put("id", id);
            treeObject.put("name", title);
            treeObject.put("spread", false);
            treeObject.put("alias", title);
            treeObject.put("menuNum", myurl.getMenuNum());
            treeObject.put("children", getChildren(id));
            jsonArray.put(treeObject);
        }
        return jsonArray;
    }


}
