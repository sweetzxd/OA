package com.oa.core.system.postposition;

import com.oa.core.util.LogUtil;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @ClassName:WorkFlowEndPostPosition
 * @author:zxd
 * @Date:2018/10/12
 * @Time:上午 11:37
 * @Version V1.0
 * @Explain
 */
public class WorkFlowEndPostPosition implements PostPositionInterfaceForm  {
    @Override
    public Map execuTables(String workOrderNO, String procId, String recorderNO, Map tableMap) {
        Map newMap = new HashMap();
        if(tableMap!=null && tableMap.size()>0){
            Iterator<Map.Entry> iter = tableMap.entrySet().iterator();
            while(iter.hasNext()){
                Map.Entry entry = iter.next();
                String key = (String)entry.getKey();
                Object value = (Object)entry.getValue();


                newMap.put(key,value);
                LogUtil.sysLog(key+" "+value);
            }
            tableMap.putAll(newMap);
        }

        return tableMap;
    }

}
