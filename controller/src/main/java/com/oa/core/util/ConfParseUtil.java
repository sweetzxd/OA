package com.oa.core.util;

import com.oa.core.helper.FileHelper;
import com.oa.core.helper.StringHelper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

public class ConfParseUtil {

    public String getProperty(String key) {
        ResourceBundle bundle = ResourceBundle.getBundle("config.path");
        String producefile = bundle.getString(key);
        if (producefile == null || producefile.equals("")) {
            String filePath = this.getClass().getClassLoader().getResource("").getPath() + "config/path.properties";
            Properties properties = FileHelper.getProperties(filePath);
            producefile = properties.getProperty(key);
        }
        return producefile;
    }

    public String setProperty(String key, String value) {
        return setProPerties("config/path.properties", key, value);
    }

    public String getPoa(String key) {
        try {
            ResourceBundle bundle = ResourceBundle.getBundle("config.oa");
            String producefile = bundle.getString(key);
            if (producefile == null || producefile.equals("")) {
                String filePath = this.getClass().getClassLoader().getResource("").getPath() + "config/oa.properties";
                Properties properties = FileHelper.getProperties(filePath);
                producefile = properties.getProperty(key);
            }
            if (producefile != null && producefile != "") {
                if (Const.NOKEY.contains(key) || key.contains("error_") || key.contains("msg_")) {
                    return StringHelper.decodeUnicode(producefile);
                } else {
                    return producefile;
                }
            } else {
                return "";
            }
        }catch (Exception e){
            e.printStackTrace();
            return "";
        }
    }

    public String setPoa(String key, String value) {
        return setProPerties("config/oa.properties", key, value, true);
    }

    /**
     * 获取作息时间设置
     */
    public Hashtable<String, String> getSchedule() {
        Hashtable<String, String> ht = new Hashtable<>();
        String filePath = this.getClass().getClassLoader().getResource("").getPath() + "config/schedule.properties";
        System.out.println("filePath:"+filePath);
        Properties properties = FileHelper.getProperties(filePath);
        Enumeration<?> e = properties.propertyNames();
        while (e.hasMoreElements()) {
            String key = (String) e.nextElement();
            String value = properties.getProperty(key);
            ht.put(key, value);
        }
        return ht;
    }

    public String getSchedule(String key) {
        ResourceBundle bundle = ResourceBundle.getBundle("config.schedule");
        String producefile = bundle.getString(key);

        if (producefile == null || producefile.equals("")) {
            String filePath = this.getClass().getClassLoader().getResource("").getPath() + "config/schedule.properties";
            Properties properties = FileHelper.getProperties(filePath);
            producefile = properties.getProperty(key);
        }

        return producefile;
    }

    public String setSchedule(String key, String value) {
        return setProPerties("config/schedule.properties", key, value);
    }

    public String setProPerties(String name, String key, String value) {
        return setProPerties(name, key, value, false);
    }
    /**
     * 写入配置文件
     *
     * @param name  配置文件名
     * @param key
     * @param value
     * @param zh    是否为中文
     */
    public String setProPerties(String name, String key, String value, boolean zh) {
        String filePath = this.getClass().getClassLoader().getResource("").getPath() + name;
        Properties properties = FileHelper.getProperties(filePath);
        OutputStream output = null;
        try {
            output = new FileOutputStream(filePath);
            if (zh) {
                properties.setProperty(key, StringHelper.gbEncoding(value));
            } else {
                properties.setProperty(key, value);
            }
            properties.store(output, "modify" + new Date().toString());
        } catch (IOException io) {
            io.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        String producefile = properties.getProperty(key);
        return producefile;
    }

    public static void main(String[] args) {
        ConfParseUtil cp = new ConfParseUtil();
        cp.setSchedule("sunday", "off");
    }
}
