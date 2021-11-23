package com.oa.core.scada.system;

import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;

/**
 * @ClassName:LoginAuth
 * @author:zxd
 * @Date:2019/06/15
 * @Time:下午 12:32
 * @Version V1.0
 * @Explain
 */
public class LoginAuth {
    /**
     * 登录 获取 Cookie
     * @param url API地址
     * @param username 账号, 注意不要使用admin@megvii.com
     * @param password 密码
     * @return cookie CookieStore
     * @throws Exception
     */
    public static CookieStore authLogin(String url, String username, String password) {
        System.out.println("Start /auth/login to ...");
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost request = new HttpPost(url);

        //设置user-agent为 "Koala Admin"
        //设置Content-Type为 "application/json"
        request.setHeader("User-Agent", "Koala Admin");
        request.setHeader("Content-Type", "application/json");
        JSONObject json = new JSONObject();
        json.put("username", username);
        json.put("password", password);
        request.setEntity(new StringEntity(json.toString(), "UTF-8"));

        //发起网络请求，获取结果值
        HttpClientContext context = HttpClientContext.create();
        String responseBody = null;
        try {
            CloseableHttpResponse response = httpclient.execute(request, context);
            responseBody = EntityUtils.toString(response.getEntity(), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e);
        }
        //解析JSON数据
        JSONObject resp = new JSONObject(responseBody);
        int result = resp.optInt("code", -1);
        if (result != 0) {
            System.err.println("Login failed, code:" + result);
        }else{
            System.out.println("Login Success,id:" + resp.getJSONObject("data").getInt("id"));
            return  context.getCookieStore();
        }
        return null;
    }

    public static void main(String[] args) {
        try {
            CookieStore cookieStore = LoginAuth.authLogin("http://192.168.1.50/auth/login", "test@megvii.com", "123456");
            System.out.println("========================>"+cookieStore+"<======================");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
