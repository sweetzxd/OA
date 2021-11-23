package com.oa.core.scada.system;

import com.oa.core.listener.InitDataListener;
import com.oa.core.scada.common.Common;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.List;

/**
 * @ClassName:Subject
 * @author:zxd
 * @Date:2019/06/15
 * @Time:下午 1:41
 * @Version V1.0
 * @Explain 底库管理
 */
public class Subject {

    /**
     * 获取所有用户列表
     *
     * @param cookieStore cookie值
     * @param url
     * @throws Exception
     */
    public static JSONArray mobileAdminSubjects(CookieStore cookieStore, String url) throws Exception {
        System.out.println("Start GET /mobile-admin/subjects 获取所有用户列表 ...");
        System.out.println(url);
        //自定义HttpClients 设置CookieStore
        CloseableHttpClient httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
        HttpGet request = new HttpGet(url);

        CloseableHttpResponse response = httpclient.execute(request);
        String responseBody = EntityUtils.toString(response.getEntity(), "UTF-8");
        System.out.println(responseBody);
        //解析JSON数据
        JSONObject resp = new JSONObject(responseBody);

        int result = resp.optInt("code", -1);
        if (result != 0) {
            System.err.println("code:" + result + ",desc:" + resp.getString("desc"));
            return null;
        } else {
            JSONArray array = resp.getJSONArray("data");
            for (int i = 0; i < array.length(); i++) {
                JSONObject userInfo = (JSONObject) array.get(i);
                System.out.println("id:" + userInfo.getInt("id") + ",name:" + userInfo.getString("name"));
            }
            return array;
        }
    }

    /**
     * 创建用户
     *
     * @param cookieStore  cookie值
     * @param url
     * @param data json数据
     * @throws Exception
     */
    public static int subject(CookieStore cookieStore, String url, JSONObject data) throws Exception {
        System.out.println("Start POST /subject 创建用户 ...");

        //自定义HttpClient 设置CookieStore
        CloseableHttpClient httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
        HttpPost request = new HttpPost(url);

        //设置Content-Type
        request.setHeader("Content-Type", "application/json");
        request.setEntity(new StringEntity(data.toString(), "UTF-8"));

        CloseableHttpResponse response = httpclient.execute(request);
        String responseBody = EntityUtils.toString(response.getEntity(), "UTF-8");

        //解析JSON数据
        JSONObject resp = new JSONObject(responseBody);
        int result = resp.optInt("code", -1);
        if (result != 0) {
            System.err.println("code:" + result + ",desc:" + resp.getString("desc"));
            return 0;
        } else {
            System.out.println("id:" + resp.getJSONObject("data").getInt("id"));
            return resp.getJSONObject("data").getInt("id");
        }
    }

    /**
     * 获取用户信息
     *
     * @param cookieStore
     * @param url
     */
    public static int getSubject(CookieStore cookieStore, String url) throws Exception {
        System.out.println("Start GET /subject/[id] 获取用户信息 ...");

        //自定义HttpClient 设置CookieStore
        CloseableHttpClient httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
        HttpGet request = new HttpGet(url);

        CloseableHttpResponse response = httpclient.execute(request);
        String responseBody = EntityUtils.toString(response.getEntity(), "UTF-8");

        //解析JSON数据
        JSONObject resp = new JSONObject(responseBody);
        int result = resp.optInt("code", -1);
        if (result != 0) {
            System.err.println("code:" + result + ",desc:" + resp.getString("desc"));
            return 0;
        } else {
            JSONObject userInfo = resp.getJSONObject("data");
            System.out.println("id:" + userInfo.getInt("id") + ",name:" + userInfo.getString("name"));
            return 1;
        }
    }

    /**
     * 获取用户信息
     *
     * @param cookieStore
     * @param url
     */
    public static JSONObject getSubjectVal(CookieStore cookieStore, String url) throws Exception {
        System.out.println("Start GET /subject/[id] 获取用户信息 ...");

        //自定义HttpClient 设置CookieStore
        CloseableHttpClient httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
        HttpGet request = new HttpGet(url);

        CloseableHttpResponse response = httpclient.execute(request);
        String responseBody = EntityUtils.toString(response.getEntity(), "UTF-8");

        //解析JSON数据
        JSONObject resp = new JSONObject(responseBody);
        int result = resp.optInt("code", -1);
        if (result != 0) {
            System.err.println("code:" + result + ",desc:" + resp.getString("desc"));
            return null;
        } else {
            JSONObject userInfo = resp.getJSONObject("data");
            System.out.println("id:" + userInfo.getInt("id") + ",name:" + userInfo.getString("name"));
            JSONObject json = new JSONObject();
            json.put("id",userInfo.getInt("id"));
            json.put("name",userInfo.getString("name"));
            json.put("job_number",userInfo.getString("job_number"));
            return json;
        }
    }

    /**
     * 更新用户信息
     *
     * @param cookieStore
     * @param url         /subject/[id]
     * @param data        数据
     */
    public static void updateSubject(CookieStore cookieStore, String url,JSONObject data) throws Exception {
        System.out.println("Start PUT /subject/[id] 更新用户信息 ...");

        //自定义HttpClient 设置CookieStore
        CloseableHttpClient httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
        HttpPut request = new HttpPut(url);
        //设置Content-Type
        request.setHeader("Content-Type", "application/json");
        request.setEntity(new StringEntity(data.toString(), "UTF-8"));

        CloseableHttpResponse response = httpclient.execute(request);
        String responseBody = EntityUtils.toString(response.getEntity(), "UTF-8");

        //解析JSON数据
        JSONObject resp = new JSONObject(responseBody);
        int result = resp.optInt("code", -1);
        if (result != 0) {
            System.err.println("code:" + result + ",desc:" + resp.getString("desc"));
        } else {
            JSONObject userInfo = resp.getJSONObject("data");
            System.out.println("id:" + userInfo.getInt("id") + ",name:" + userInfo.getString("name"));
        }
    }

    /**
     * 删除用户
     *
     * @param cookieStore cookie值
     * @param url         /subject/[id]
     * @throws Exception
     */
    public static void deleteSubject(CookieStore cookieStore, String url) throws Exception {
        System.out.println("Start DELETE /subject/[id] 删除用户 ...");

        //自定义HttpClient 设置CookieStore
        CloseableHttpClient httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
        HttpDelete request = new HttpDelete(url);

        CloseableHttpResponse response = httpclient.execute(request);
        String responseBody = EntityUtils.toString(response.getEntity(), "UTF-8");

        //解析JSON
        JSONObject resp = new JSONObject(responseBody);
        int result = resp.optInt("code", -1);
        if (result != 0) {
            System.err.println("code:" + result + ",desc:" + resp.getString("desc"));
        } else {
            JSONObject userInfo = resp.getJSONObject("data");
            System.out.println("id:" + userInfo.getInt("id") + ",name:" + userInfo.getString("name"));
        }
    }

    /**
     * 上传识别照片
     *
     * @param photo      底库照片
     * @param subject_id 用户id
     * @throws Exception
     */
    public static int photo(CookieStore cookieStore, String url, String photo, Integer subject_id) throws Exception {
        System.out.println("Start POST /subject/photo 上传识别照片 ...");

        //自定义HttpClient 设置CookieStore
        CloseableHttpClient httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
        HttpPost request = new HttpPost(url);

        //设置底库照片 并关联到用户
        MultipartEntity reqEntity = new MultipartEntity();
        reqEntity.addPart("photo", new FileBody(new File(photo)));
        reqEntity.addPart("subject_id", new StringBody(subject_id.toString()));
        request.setEntity(reqEntity);

        CloseableHttpResponse response = httpclient.execute(request);
        String responseBody = EntityUtils.toString(response.getEntity(), "UTF-8");

        //解析JSON数据
        JSONObject resp = new JSONObject(responseBody);
        int result = resp.optInt("code", -1);
        if (result != 0) {
            System.err.println("code:" + result + ",desc:" + resp.getString("desc"));
            return 0;
        } else {
            JSONObject data = resp.getJSONObject("data");
            System.out.println("id:" + data.getInt("id") + "url:" + data.getString("url"));
            return data.getInt("id");
        }
    }

    /**
     * 上传识别照片
     *
     * @param photo 底库照片
     * @throws Exception
     */
    public static JSONObject photoCheck(CookieStore cookieStore, String url, String photo) throws Exception {
        System.out.println("Start POST /subject/photo/check 入库图片质量判断接口 ...");

        //自定义HttpClient 设置CookieStore
        CloseableHttpClient httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
        HttpPost request = new HttpPost(url);

        //设置底库照片 并关联到用户
        MultipartEntity reqEntity = new MultipartEntity();
        reqEntity.addPart("photo", new FileBody(new File(photo)));
        request.setEntity(reqEntity);

        CloseableHttpResponse response = httpclient.execute(request);
        String responseBody = EntityUtils.toString(response.getEntity(), "UTF-8");
        System.out.println(responseBody);
        //解析JSON数据
        JSONObject resp = new JSONObject(responseBody);
        int result = resp.optInt("code", -1);
        if (result != 0) {
            System.err.println("code:" + result + ",desc:" + resp.getString("desc"));
        } else {
            System.out.println("resp:" + resp);
        }
        return resp;
    }

    /**
     * 上传显示头像
     * @param photo 头像
     * @param subject_id 关联用户id
     * @throws Exception
     */
    public static String avatar(CookieStore cookieStore , String url ,String photo,Integer subject_id) throws Exception {
        System.out.println("Start POST /subject/avatar 上传显示头像 ...");

        //自定义HttpClient 设置CookieStore
        CloseableHttpClient httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
        HttpPost request = new HttpPost(url);

        //设置显示头像  并关联到用户
        MultipartEntity reqEntity = new MultipartEntity();
        reqEntity.addPart("avatar", new FileBody(new File(photo)));
        reqEntity.addPart("subject_id", new StringBody(subject_id.toString()));
        request.setEntity(reqEntity);

        CloseableHttpResponse response = httpclient.execute(request);
        String responseBody = EntityUtils.toString(response.getEntity(), "UTF-8");

        //解析JSON数据
        JSONObject resp = new JSONObject(responseBody);
        int result = resp.optInt("code", -1);
        if (result != 0) {
            System.err.println("code:" + result + ",desc:" + resp.getString("desc"));
            return "";
        }else{
            System.out.println("url:"+ resp.getJSONObject("data").getString("url"));
            return resp.getJSONObject("data").getString("url");
        }
    }

    /**
     * 获取历史记录
     *
     * @param cookieStore
     * @param url
     */
    public static JSONArray getEvents(CookieStore cookieStore, String url,JSONObject data) throws Exception {
        System.out.println("Start GET /event/events 获取历史识别记录 ...");
        String var = "?category=user";
        if(!data.isNull("start")) {
            var += "&start=" + data.get("start");
        }
        if(!data.isNull("end")){
            var += "&end=" + data.get("end");
        }
        if(!data.isNull("user_role")){
            var += "&user_role=" + data.get("user_role");
        }
        if(!data.isNull("user_name")){
            var += "&user_name=" + data.get("user_name");
        }
        if(!data.isNull("screen_id")){
            var += "&screen_id=" + data.get("screen_id");
        }
        if(!data.isNull("subject_id")){
            var += "&subject_id=" + data.get("subject_id");
        }
        if(!data.isNull("page")){
            var += "&page=" + data.get("page");
        }
        if(!data.isNull("size")){
            var += "&size=" + data.get("size");
        }
        System.out.println(var);
        //自定义HttpClient 设置CookieStore
        CloseableHttpClient httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
        HttpGet request = new HttpGet(url + var+"&_="+System.currentTimeMillis());

        CloseableHttpResponse response = httpclient.execute(request);
        String responseBody = EntityUtils.toString(response.getEntity(), "UTF-8");
        //System.out.println(responseBody);
        //解析JSON数据
        JSONObject resp = new JSONObject(responseBody);
        int result = resp.optInt("code", -1);
        if (result != 0) {
            System.err.println("code:" + result + ",desc:" + resp.getString("desc"));
            return null;
        } else {
            JSONArray jsons = new JSONArray();
            JSONArray userInfos = resp.getJSONArray("data");
            for (int n = 0, len = userInfos.length(); n < len; n++) {
                JSONObject userInfo = userInfos.getJSONObject(n);
                //System.out.println(userInfo);
                JSONObject json = new JSONObject();
                if(!userInfo.isNull("subject_id")) {
                    json.put("id", userInfo.getInt("subject_id"));
                    json.put("name", userInfo.getJSONObject("subject").getString("name"));
                    json.put("job_number", userInfo.getJSONObject("subject").getString("job_number"));
                    int time = userInfo.getInt("timestamp");
                    String timestamp = String.valueOf(time);
                    System.out.println(timestamp.length());
                    if(timestamp.length()< 13){
                        Long tt = Long.valueOf(time)*1000;
                        json.put("timestamp", tt);
                    }else{
                        json.put("timestamp", time);
                    }
                    System.out.println(json);
                    json.put("data",userInfo);
                    jsons.put(json);
                }
            }
            return jsons;
        }
    }

    public static void main(String[] args) {
        try {
            String url = Common.SYSTEM+Common.SYSTEM_URL;
            CookieStore cookieStore = LoginAuth.authLogin(Common.SYSTEM+Common.SYSTEM_URL + Common.LOGIN, Common.SYSTEM_USERNAME, Common.SYSTEM_PASSWORD);
            //subject(cookieStore,Common.SYSTEM+Common.SYSTEM_URL+ Common.ADD_USER,0,"甄旭东",null);
            //photoCheck(cookieStore,Common.SYSTEM+Common.SYSTEM_URL+Common.POST_PHOTO_CHECK,"C:\\Users\\Administrator\\Desktop\\微信图片_20190615141537.jpg");

            //photo(cookieStore,Common.SYSTEM+Common.SYSTEM_URL+Common.POST_PHOTO,"C:\\Users\\Administrator\\Desktop\\微信图片_20190615151412.jpg",5959);
            //int[] user = new int[]{5959};
            //getSubjectVal(cookieStore, url + Common.SELECT_USER + 18);
            //avatar(cookieStore,url+Common.UPDATE_AVATAR,"C:\\Users\\Administrator\\Desktop\\微信图片_20190615141537.jpg",14);


            //Subject.photoCheck(InitDataListener.cookieStore,url+Common.POST_PHOTO_CHECK,"C:\\Users\\Administrator\\Desktop\\微信图片_20190615141537.jpg");
            //mobileAdminSubjects(cookieStore,url+Common.ALL_USER);


            JSONObject json = new JSONObject();
            json.put("start",1570485600);
            json.put("end",1570496400);
            json.put("user_role",0);
            //json.put("screen_id",4);
            //json.put("page",4);
            json.put("size",1000);
            getEvents(cookieStore, url + Common.EVENT_USER_LOG ,json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
