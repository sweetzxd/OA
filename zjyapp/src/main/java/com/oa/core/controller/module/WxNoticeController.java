package com.oa.core.controller.module;

import com.oa.core.bean.util.PageUtil;
import com.oa.core.service.system.TaskSenderService;
import com.oa.core.service.util.TableService;
import com.oa.core.util.MySqlUtil;
import com.oa.core.util.ToNameUtil;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 * @ClassName:NoticeController
 * @author:zxd
 * @Date:2018/11/30
 * @Time:下午 5:27
 * @Version V1.0
 * @Explain
 */
@Controller
@RequestMapping("/weixin")
public class WxNoticeController {
    @Autowired
    TaskSenderService taskSenderService;
    @Autowired
    TableService tableService;

    /**
     * 获取所有通知公告
     * @return String
     */
    @RequestMapping(value = "/selectAllTzgg", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String selectAllTzgg(HttpServletRequest request,String inputval, String option, String type, int page, int limit){
        PageUtil pu = new PageUtil();
        pu.setPageSize(limit);
        pu.setCurrentPage(page);
        List<String> where = new ArrayList<String>();
        if ("bt18111000001".equals(option)) {
            where.add("bt18111000001 like ('%"+inputval+"%')");
        }
        String typestr = "";
        if(type.equals("1")){
            typestr="通知";
        }else if(type.equals("2")){
            typestr="公告";
        }
        Vector<String> vector = new Vector<>();
        vector.add("bt18111000001");
        vector.add("nr18111000001");
        vector.add("xzdwj18111002");
        vector.add("tzggl18111002");
        vector.add("recordName");
        where.add("tzggl18111002='"+typestr+"'");
        String sql = MySqlUtil.getNameSql(vector, "tzgg181110001", where,"recordTime desc",pu.getStartRow(),pu.getEndRow()-pu.getStartRow());
        List<Map<String,Object>> tzList = tableService.selectSqlMapList(sql);
        for(Map<String,Object> map : tzList){
            map.put("tzgg181110001_recordTime", ToNameUtil.getName("datetime",String.valueOf(map.get("tzgg181110001_recordTime"))));
        }
        String countSql = MySqlUtil.getCountSql("tzgg181110001",where);
        int count = tableService.selectSqlCount(countSql);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("count", count);
        jsonObject.put("data", tzList);
        jsonObject.put("msg", "");
        jsonObject.put("success",1);
        return jsonObject.toString();
    }
    /**
     * 查看通知公告
     * @param id 通知id
     * @return
     */
    @RequestMapping("/seeNotice")
    @ResponseBody
    public String seeNotice(HttpServletRequest request,String id){
        JSONObject jsonObject = new JSONObject();
        try{
            String sql = "select * from tzgg181110001 where recorderNO = '"+id+"'";
            Map<String,Object> map = tableService.selectSqlMap(sql);
            jsonObject.put("data",map);
            jsonObject.put("success",true);
        }catch (Exception e){
            e.printStackTrace();
            jsonObject.put("success",false);
        }
        return jsonObject.toString();
    }
}
