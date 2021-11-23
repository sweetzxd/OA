package com.oa.core.system.workflow;

import com.oa.core.bean.work.WkflwFieldMap;
import com.oa.core.helper.DateHelper;
import com.oa.core.service.util.TableService;
import com.oa.core.service.work.WkflwFieldMapService;
import com.oa.core.util.MySqlUtil;
import com.oa.core.util.PrimaryKeyUitl;
import com.oa.core.util.SpringContextUtil;

import java.util.*;

/**
 * @ClassName:Account
 * @author:zxd
 * @Date:2018/11/15
 * @Time:下午 4:23
 * @Version V1.0
 * @Explain
 */
public class AccountUtil {
    private TableService tableService = (TableService) SpringContextUtil.getBean("tableService");
    private WkflwFieldMapService wkflwFieldMapService = (WkflwFieldMapService) SpringContextUtil.getBean("wkflwFieldMapService");

    public void insertTable(String userId, String wkflwId, String procid){
        String tableName = "";
        String fields = "";
        String formTableName = "";
        List<WkflwFieldMap> wfmlist = wkflwFieldMapService.selectWkflwFieldMapByWkflwId(wkflwId);
        Hashtable<String,String> ht = new Hashtable<>();
        Hashtable<String,List<String>> nodeht = new Hashtable<>();
        List<String> formlist = new ArrayList<>();
        List<String> nodelist;
        if(wfmlist.size()>0){
        for(int i=0;i<wfmlist.size();i++){
            WkflwFieldMap wkflwFieldMap = wfmlist.get(i);
            formTableName = wkflwFieldMap.getFormTableName();
            String formFeildName = wkflwFieldMap.getFormFieldName();
            formlist.add(formFeildName);

            String nodeTableName = wkflwFieldMap.getNodeTableName();
            String nodeFeildName = wkflwFieldMap.getNodeFieldName();
            nodelist = nodeht.get(nodeTableName);
            if(nodelist==null) {
                nodelist = new ArrayList<>();
                nodelist.add(nodeFeildName);
                nodeht.put(nodeTableName,nodelist);
            }else{
                nodelist.add(nodeFeildName);
                nodeht.put(nodeTableName,nodelist);
            }
            ht.put(formFeildName,nodeFeildName);
        }
        //查询
        Map<String, Object> flowData = new HashMap<String, Object>();
        for (Map.Entry<String, List<String>> entry : nodeht.entrySet()) {
            //查询
            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
            List<String> field = entry.getValue();
            field.add("workflowProcID");
            field.add("workflowNodeID");
            List<String> where = new ArrayList<String>();
            where.add("workflowProcID='" + procid + "'");
            String sql = MySqlUtil.getSql(field, entry.getKey(), where);
            Map<String, Object> sqlvalue = tableService.selectSqlMap(sql);
            if(sqlvalue!=null){
                flowData.putAll(sqlvalue);

            }
        }
        //添加
        Map<String, Object> map = new HashMap<String, Object>();
        for(String field : formlist){
            map.put(field,flowData.get(ht.get(field)));
        }
        PrimaryKeyUitl pk = new PrimaryKeyUitl();
        if(formTableName!=null && !formTableName.equalsIgnoreCase("")) {
            String id = pk.getNextId(formTableName, "recorderNO");
            map.put("recorderNO", id);
            map.put("workflowProcID", procid);
            map.put("recordName", userId);
            map.put("recordTime", DateHelper.now());
            map.put("modifyName", userId);
            map.put("modifyTime", DateHelper.now());
            tableService.insertSqlMap(MySqlUtil.getInsertSql(formTableName, map));
        }
    }}
}
