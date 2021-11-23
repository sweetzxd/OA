package com.oa.core.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;

/**
 * JSON解析
 *
 * @author zxd
 *
 */
public class JsonHelper {

    /**
     * 将json字符串转换成java对象
     * @param jsonStr
     * @param cls
     * @return object
     */
    public static Object jsonToObject(String jsonStr, Class<?> cls) {
        Object obj = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            obj = mapper.readValue(jsonStr, cls);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return obj;
    }

    /**
     * 将json数组转换成对象数组
     * @param jsonArr json数组
     * @param cls 要转换的对象
     * @return list&lt;?&gt;
     */
    public static List<?> jsonToList(JSONArray jsonArr, Class<?> cls) {
        List<Object> jsonToMapList = new ArrayList<Object>();
        for (int i = 0; i < jsonArr.length(); i++) {
            Object object = jsonArr.get(i);
            if (object instanceof JSONArray) {
                jsonToMapList.add(JsonHelper.jsonToList((JSONArray) object,cls));
            } else if (object instanceof JSONObject) {
                jsonToMapList.add(jsonToObject(object.toString(), cls));
            } else {
                jsonToMapList.add(object);
            }
        }
        return jsonToMapList;
    }

    /**
     * 将传递近来的json对象转换为Map对象
     *
     * @param jsonObj
     * @return Map&lt;String, Object&gt;
     */
    public static Map<String, Object> jsonToMap(JSONObject jsonObj, Class<?> cls) {
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        @SuppressWarnings("unchecked")
        Iterator<String> jsonKeys = jsonObj.keys();
        while (jsonKeys.hasNext()) {
            String jsonKey = jsonKeys.next();
            Object jsonValObj = jsonObj.get(jsonKey);
            if (jsonValObj instanceof JSONArray) {
                jsonMap.put(jsonKey, JsonHelper.jsonToList((JSONArray) jsonValObj, cls));
            } else if (jsonValObj instanceof JSONObject) {
                jsonMap.put(jsonKey, JsonHelper.jsonToMap((JSONObject) jsonValObj, cls));
            } else {
                jsonMap.put(jsonKey, jsonValObj);
            }
        }
        return jsonMap;
    }

    /**
     * 将传递近来的json对象转换为Map对象
     *
     * @param jsonObj
     * @return Map&lt;String, Object&gt;
     */
    public static Map<String, Object> jsonToMap(JSONObject jsonObj) {
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        @SuppressWarnings("unchecked")
        Iterator<String> jsonKeys = jsonObj.keys();
        while (jsonKeys.hasNext()) {
            String jsonKey = jsonKeys.next();
            Object jsonValObj = jsonObj.get(jsonKey);
            jsonMap.put(jsonKey, jsonValObj);

        }
        return jsonMap;
    }
    /**
     * 页面定制专用
     * 用于处理传过来的页面字段
     * */
    public static String readJSON (String str,String key){
        JSONObject json = new JSONObject(str);
        JSONArray jsonArray = json.getJSONArray("data");
        String getValue = "";
        for(int i = 0; i<((JSONArray) jsonArray).length(); i++){
            JSONObject subObject=jsonArray.getJSONObject(i);
            getValue += subObject.getString(key)+",";
        }
        return getValue;
    }
}
