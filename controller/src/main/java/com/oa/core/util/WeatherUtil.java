package com.oa.core.util;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.zip.GZIPInputStream;

/**
 * @ClassName:WeatherUtil
 * @author:zxd
 * @Date:2019/04/08
 * @Time:上午 11:27
 * @Version V1.0
 * @Explain 天气接口
 */
public class WeatherUtil {

    private static HashMap<String,JSONObject> hm = new HashMap();

    public JSONObject getWeather(String city) {
        JSONObject weather = hm.get(city);
        if (weather == null) {
            weather = tianqiJSON(city);
            if (weather == null) {
                weather = new JSONObject();
                weather.put("cityname", city);
                weather.put("stateDetailed", "晴");
                weather.put("tem1", "0");
                weather.put("tem2", "30");
                weather.put("img", "/upload/tianqi/晴.png");
            }
        }
        return weather;
    }

    public void setWeather(String city,JSONObject weather) {
        hm.put(city,weather);
    }

    public static JSONObject tianqiJSON(String city) {
        JSONObject jsonObject = getWeatherByHttp(city);
        JSONObject data = jsonObject.getJSONObject("data");
        JSONArray forecast = data.getJSONArray("forecast");
        JSONObject weather = forecast.getJSONObject(0);
        if (weather != null) {
            String type = weather.getString("type");
            String tem1 = weather.getString("low");
            tem1 = tem1.replaceAll("低温", "").replaceAll(" ", "");
            String tem2 = weather.getString("high");
            tem2 = tem2.replaceAll("高温", "").replaceAll(" ", "");
            JSONObject json = new JSONObject();
            json.put("cityname", city);
            json.put("stateDetailed", type);
            json.put("tem1", tem1);
            json.put("tem2", tem2);
            json.put("img", getImg(type));
            hm.put(city,json);
            return json;
        } else {
            return null;
        }
    }

    public static String getImg(String type) {
        String img = "/upload/tianqi/";

        if(type.indexOf("晴")>=0){
            img += "晴.png";
        }else if(type.indexOf("阴")>=0){
            img += "阴.png";
        }else if(type.indexOf("雨")>=0){
            img += "小雨.png";
        }else if(type.indexOf("云")>=0){
            img += "多云.png";
        }else if(type.indexOf("雷")>=0){
            img += "雷阵雨.png";
        }else if(type.indexOf("雪")>=0){
            img += "小雪.png";
        }else if(type.indexOf("雾")>=0){
            img += "霾.png";
        }else if(type.indexOf("霾")>=0){
            img += "霾.png";
        }else if(type.indexOf("沙")>=0){
            img += "霾.png";
        }else{
            img += "晴.png";
        }
        return img;
    }

    // 获取天气预报
    public static JSONObject getWeatherByHttp(String city) {
        String url = "http://wthrcdn.etouch.cn/weather_mini?city=" + city;
        HttpGet httpGet = new HttpGet(url);
        HttpClient httpClient = new DefaultHttpClient();
        try {
            HttpResponse httpResponse = httpClient.execute(httpGet);
            JSONObject obj = new JSONObject(getJsonStringFromGZIP(httpResponse));
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String getJsonStringFromGZIP(HttpResponse response) {
        String jsonString = null;
        try {
            InputStream is = response.getEntity().getContent();
            BufferedInputStream bis = new BufferedInputStream(is);
            bis.mark(2);
            // 取前两个字节
            byte[] header = new byte[2];
            int result = bis.read(header);
            // reset输入流到开始位置
            bis.reset();
            // 判断是否是GZIP格式
            int headerData = getShort(header);
            if (result != -1 && headerData == 0x1f8b) {
                is = new GZIPInputStream(bis);
            } else {
                is = bis;
            }
            InputStreamReader reader = new InputStreamReader(is, "utf-8");
            char[] data = new char[100];
            int readSize;
            StringBuffer sb = new StringBuffer();
            while ((readSize = reader.read(data)) > 0) {
                sb.append(data, 0, readSize);
            }
            jsonString = sb.toString();
            bis.close();
            reader.close();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
        return jsonString;
    }

    private static int getShort(byte[] data) {
        return (int) ((data[0] << 8) | data[1] & 0xFF);
    }

}
