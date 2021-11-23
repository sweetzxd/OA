package com.oa.core.tag;

import com.oa.core.bean.system.MyUrlRegist;
import com.oa.core.service.system.MyUrlRegistService;
import com.oa.core.util.SpringContextUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName:MyUrlDict
 * @author:zxd
 * @Date:2018/09/19
 * @Time:上午 9:50
 * @Version V1.0
 */
public class MyUrlDict {

    private static Map<String, MyUrlRegist> data = null;

    static {
        initData();
    }

    public static void resetData() {
        if (data != null) {
            data.clear();
        }
        data = null;
    }

    public static void initData() {
        MyUrlRegistService m = (MyUrlRegistService) SpringContextUtil.getBean("myUrlRegistService");
        List<MyUrlRegist> mList = m.selectAll();
        if (mList.size() > 0) {
            data = new HashMap<String, MyUrlRegist>();
            for (MyUrlRegist u : mList) {
                data.put(u.getPageId(), u);
            }
        }
    }

    /**
     * 重新从数据库中获取数据
     *
     * @param pageid
     */
    public static void initMyUrlData(String pageid) {
        MyUrlRegistService m = (MyUrlRegistService) SpringContextUtil.getBean("myUrlRegistService");
        MyUrlRegist myurl = m.selectById(pageid);
        if (myurl != null) {
            data.put(myurl.getPageId(), myurl);
        }
    }

    public static MyUrlRegist getData(String pageid) {
        if (data == null) {
            initData();
        }
        if (data != null) {
            MyUrlRegist result = data.get(pageid);
            if (result == null) {
                initMyUrlData(pageid);
                return data.get(pageid);
            } else {
                return result;
            }
        }
        return null;
    }
}
