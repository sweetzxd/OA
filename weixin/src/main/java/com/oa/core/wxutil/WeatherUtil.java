package com.oa.core.wxutil;

import com.oa.core.util.PinyinUtil;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.json.JSONObject;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Iterator;

/**
 * @ClassName:WeatherUtil
 * @author:zxd
 * @Date:2019/04/08
 * @Time:上午 11:27
 * @Version V1.0
 * @Explain 天气接口
 */
public class WeatherUtil {

    public static String getURLContent(String id) {
        System.out.println(id);
        String urlStr = "http://flash.weather.com.cn/wmaps/xml/" + id + ".xml";
        //请求的url
        URL url = null;
        //请求的输入流
        BufferedReader in = null;
        //输入流的缓冲
        StringBuffer sb = new StringBuffer();
        try {
            url = new URL(urlStr);
            in = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
            String str = null;
            //一行一行进行读入
            while ((str = in.readLine()) != null) {
                sb.append(str);
            }
        } catch (Exception ex) {

        } finally {
            try {
                if (in != null) {
                    in.close(); //关闭流
                }
            } catch (IOException ex) {

            }
        }
        String result = sb.toString();
        return result;
    }

    public static JSONObject tianqiXml(String weizhi,String chengshi) {
        String pinyin = PinyinUtil.getPingYin(chengshi);
        JSONObject json = null;
        try {
            Document document = DocumentHelper.parseText(getURLContent(pinyin));
            Element rootElt = document.getRootElement();
            Iterator f = rootElt.elementIterator();
            while (f.hasNext()) {
                Element itemAtr = (Element) f.next();
                String cityname = itemAtr.attributeValue("cityname");
                String stateDetailed = itemAtr.attributeValue("stateDetailed");
                String tem1 = itemAtr.attributeValue("tem1");
                String tem2 = itemAtr.attributeValue("tem2");
                System.out.println(cityname + "-" + stateDetailed + "-" + tem1 + "-" + tem2);
                if (weizhi.equals(cityname)) {
                    json = new JSONObject();
                    json.put("cityname", cityname);
                    json.put("stateDetailed", stateDetailed);
                    json.put("tem1", tem1);
                    json.put("tem2", tem2);
                    break;
                }

            }
        } catch (DocumentException e) {
            e.printStackTrace();
        } finally {
            return json;
        }
    }
}
