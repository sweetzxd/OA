package com.oa.core.controller.module;

import com.oa.core.bean.util.PageUtil;
import com.oa.core.service.module.MessageService;
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
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @ClassName:NoticeController
 * @author:zxd
 * @Date:2018/11/30
 * @Time:下午 5:27
 * @Version V1.0
 * @Explain
 */
@Controller
public class NoticeController {
    @Autowired
    TaskSenderService taskSenderService;
    @Autowired
    MessageService messageService;
    @Autowired
    TableService tableService;
    //查看通知公告详情
    @RequestMapping("/seeTzggInfo")
    public ModelAndView seeTzggInfo(String recorderNO){
        ModelAndView mav = new ModelAndView("module/tzggInfo");
        List<String> field = new ArrayList<>();
        field.add("bt18111000001");
        field.add("nr18111000001");
        field.add("xzdwj18111002");
        field.add("tzggl18111002");
        field.add("xzdwj18111002");
        field.add("recordTime");
        List<String> where = new ArrayList<String>();
        where.add("recorderNO='" + recorderNO + "'");
        String sql = MySqlUtil.getSql(field,"tzgg181110001", where);
        Map<String, Object> sqlvalue = tableService.selectSqlMap(sql);
        List<Map<String,Object>> fileLsit = new ArrayList<>();
        if(sqlvalue!=null) {
            String files = (String) sqlvalue.get("xzdwj18111002");
            if (files != null && !files.equals("")) {
                String[] msgFiles = files.split("\\|");
                for (String file : msgFiles) {
                    Map<String, Object> map = new HashMap<>();
                    String[] fileName = file.split("-");
                    map.put("fileName", fileName[1]);
                    map.put("file", file);
                    fileLsit.add(map);
                }
            }
        }
        mav.addObject("tzgg",sqlvalue);
        mav.addObject("fileLsit",fileLsit);
        return mav;
    }

    //查看通知公告详情
    @RequestMapping("/seeNewsInfo")
    public ModelAndView seeNewsInfo(String recorderNO){
        ModelAndView mav = new ModelAndView("module/newsInfo");
        List<String> field = new ArrayList<>();
        field.add("xwbt190414001");//标题
        field.add("fbt1904250001");//副标题
        field.add("dxwb190414001");//内容
        field.add("dxr1904140001");//人
        field.add("rqxz190414001");//拟稿日期
//        field.add("dwjsc19041401");//图片
//        field.add("xwgj190414001");//稿件
        List<String> where = new ArrayList<String>();
        where.add("recorderNO='" + recorderNO + "'");
        String sql = MySqlUtil.getSql(field,"xwtz190414001", where);
        Map<String, Object> sqlvalue = tableService.selectSqlMap(sql);
//        List<Map<String,Object>> photoLsit = new ArrayList<>();
//        List<Map<String,Object>> wordLsit = new ArrayList<>();
        //图片判断
//        if(sqlvalue!=null) {
//            String files1 = (String) sqlvalue.get("dwjsc19041401");
//            if (files1 != null && !files1.equals("")) {
//                photoLsit.add(files1);
//                }
//            }

         //稿件判断
//        if(sqlvalue!=null) {
//            String files = (String) sqlvalue.get("xzdwj18111002");
//            if (files != null && !files.equals("")) {
//                String[] msgFiles = files.split("\\|");
//                for (String file : msgFiles) {
//                    Map<String, Object> map = new HashMap<>();
//                    String[] fileName = file.split("-");
//                    map.put("fileName", fileName[1]);
//                    map.put("file", file);
//                    wordLsit.add(map);
//                }
//            }
//        }
        mav.addObject("news",sqlvalue);
//        mav.addObject("fileLsit",fileLsit);
        return mav;
    }


    //查看通知公告列表页
    @RequestMapping(value = "/seeTzggpage", method = RequestMethod.GET, produces = {"text/html;charset=UTF-8;", "application/json;"})
    public ModelAndView seeTzggpage(HttpServletRequest request, String type){
        ModelAndView mav = new ModelAndView("module/tzggpage");
        mav.addObject("type",type);
        return mav;
    }

    /**
     * 获取所有通知公告
     * @return String
     */
    @RequestMapping("/selectAllTzgg")
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
        where.add("tzggl18111002='"+typestr+"'");
        String sql = MySqlUtil.getNameSql(vector, "tzgg181110001", where,"recordTime desc",pu.getStartRow(),pu.getEndRow()-pu.getStartRow());
        List<Map<String,Object>> tzList = tableService.selectSqlMapList(sql);
        for(Map<String,Object> map : tzList){
            map.put("tzgg181110001_nr18111000001","<a class='layui-btn layui-btn-xs' href=javascript:seeTzggInfo('"+map.get("tzgg181110001_recorderNO")+"','"+map.get("tzgg181110001_tzggl18111002")+"')>查看</a>");
            map.put("tzgg181110001_recordTime", ToNameUtil.getName("datetime",String.valueOf(map.get("tzgg181110001_recordTime"))));
        }
        String countSql = MySqlUtil.getCountSql("tzgg181110001",where);
        int count = tableService.selectSqlCount(countSql);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 0);
        jsonObject.put("msg", "");
        jsonObject.put("count", count);
        jsonObject.put("data", tzList);
        jsonObject.put("success",1);
        return jsonObject.toString();
    }
}
