package com.oa.core.util;

import com.oa.core.helper.DateHelper;
import com.oa.core.service.util.TableService;
import org.json.JSONObject;

import java.util.Map;

/**
 * @ClassName:RestrictUtil
 * @author:zxd
 * @Date:2019/04/27
 * @Time:下午 4:49
 * @Version V1.0
 * @Explain
 */
public class RestrictUtil {

    private static JSONObject restrict;

    public static JSONObject getRestrict() {
        if(restrict==null){
            restrict = selectTable();
        }
        return restrict;
    }

    public static void setRestrict(JSONObject restrict) {
        RestrictUtil.restrict = restrict;
    }

    public static JSONObject selectTable(){
        JSONObject jsonObject = new JSONObject();
        TableService tableService = (TableService) SpringContextUtil.getBean("tableService");
        String xq = DateHelper.getWeekOfDate(null);
        Map<String, Object> stringObjectMap = tableService.selectSqlMap("select xxhm190427001,xxhm190427002 from xxxxw19042701 where xq19042700001='"+xq+"'");
        String st = "-",en = "-";
        if(stringObjectMap!=null) {
            st = (String) stringObjectMap.get("xxhm190427001");
            en = (String) stringObjectMap.get("xxhm190427002");
        }

        JSONObject json = new JSONObject();
        json.put("st",st);
        if(st.length()==1){
            json.put("en",en);
            jsonObject.put("type",0);
        }else if(st.length()==2){
            json.put("en","");
            jsonObject.put("type",1);
        }else{
            if(st==null || st.equals("")){
                json.put("st","不限行");
            }
            json.put("en","");
            jsonObject.put("type",2);
        }

        jsonObject.put("data",json);
        setRestrict(jsonObject);
        restrict = jsonObject;
        return jsonObject;
    }
	
}
