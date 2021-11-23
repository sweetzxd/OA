package com.oa.core.scada.bean;

import org.apache.http.client.CookieStore;

/**
 * @ClassName:StoreBean
 * @author:zxd
 * @Date:2019/06/21
 * @Time:上午 10:51
 * @Version V1.0
 * @Explain 存放监控机登录Cookie
 */
public class StoreBean {

    public static CookieStore cookieStore;

    public static CookieStore getCookieStore() {
        return cookieStore;
    }

    public static void setCookieStore(CookieStore cookieStore) {
        StoreBean.cookieStore = cookieStore;
    }
}
