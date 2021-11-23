package com.oa.core.controller.module;

import com.oa.core.bean.Loginer;
import com.oa.core.bean.module.Message;
import com.oa.core.bean.util.PageUtil;
import com.oa.core.helper.DateHelper;
import com.oa.core.service.module.MessageService;
import com.oa.core.service.system.TaskSenderService;
import com.oa.core.service.util.TableService;
import com.oa.core.util.MySqlUtil;
import com.oa.core.util.PrimaryKeyUitl;
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
@RequestMapping("/commonurl")
public class NoticeController {
    @Autowired
    TaskSenderService taskSenderService;
    @Autowired
    MessageService messageService;
    @Autowired
    TableService tableService;

    //查看通知公告详情
    @RequestMapping("/seeTzggInfo")
    public ModelAndView seeTzggInfo(String recorderNO) {
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
        String sql = MySqlUtil.getSql(field, "tzgg181110001", where);
        Map<String, Object> sqlvalue = tableService.selectSqlMap(sql);
        String files = (String) sqlvalue.get("xzdwj18111002");
        List<Map<String, Object>> fileLsit = new ArrayList<>();
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
        mav.addObject("tzgg", sqlvalue);
        mav.addObject("fileLsit", fileLsit);
        return mav;
    }

    //查看通知公告列表页
    @RequestMapping(value = "/seeTzggpage", method = RequestMethod.GET, produces = {"text/html;charset=UTF-8;", "application/json;"})
    public ModelAndView seeTzggpage(HttpServletRequest request, String type) {
        ModelAndView mav = new ModelAndView("module/tzggpage");
        mav.addObject("type", type);
        return mav;
    }

    /**
     * 获取所有通知公告
     *
     * @return String
     */
    @RequestMapping("/selectAllTzgg")
    @ResponseBody
    public String selectAllTzgg(HttpServletRequest request, String inputval, String option, String type, int page, int limit) {
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        String userId = loginer.getId();
        PageUtil pu = new PageUtil();
        pu.setPageSize(limit);
        pu.setCurrentPage(page);
        List<String> where = new ArrayList<String>();
        if ("bt18111000001".equals(option)) {
            where.add("bt18111000001 like ('%" + inputval + "%')");
        }
        String typestr = "";
        if (type.equals("1")) {
            typestr = "通知";
            if (!userId.equals("admin")) {
                where.add("ckr1906060002 like ('%" + userId + ";%')");
                //where.add("recorderNO not in (select glzj190606001 from ydzt190606001 where curStatus=2 and dxr1906060001='"+userId+"')");
            }
        } else if (type.equals("2")) {
            typestr = "公告";
        }
        Vector<String> vector = new Vector<>();
        vector.add("bt18111000001");
        vector.add("nr18111000001");
        vector.add("xzdwj18111002");
        vector.add("tzggl18111002");
        where.add("tzggl18111002='" + typestr + "'");

        String sql = MySqlUtil.getNameSql(vector, "tzgg181110001", where, "recordTime desc", pu.getStartRow(), pu.getEndRow() - pu.getStartRow());
        List<Map<String, Object>> tzList = tableService.selectSqlMapList(sql);
        for (Map<String, Object> map : tzList) {
            map.put("tzgg181110001_nr18111000001", "<a class='layui-btn layui-btn-xs' href=javascript:seeTzggInfo('" + map.get("tzgg181110001_recorderNO") + "','" + map.get("tzgg181110001_tzggl18111002") + "')>查看</a>");
            map.put("tzgg181110001_recordTime", ToNameUtil.getName("datetime", String.valueOf(map.get("tzgg181110001_recordTime"))));
        }
        String countSql = MySqlUtil.getCountSql("tzgg181110001", where);
        int count = tableService.selectSqlCount(countSql);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 0);
        jsonObject.put("msg", "");
        jsonObject.put("count", count);
        jsonObject.put("data", tzList);
        jsonObject.put("success", 1);
        return jsonObject.toString();
    }

    /**
     * @method: lookedtz
     * @param:
     * @return:
     * @author: zxd
     * @date: 2019/06/06
     * @description: 处理已看通知
     */
    @RequestMapping(value = "/lookedtz", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String lookedtz(HttpServletRequest request, String userId, String pkid, String type) {
        PrimaryKeyUitl pk = new PrimaryKeyUitl();
        Map<String, Object> map = new HashMap<>();
        map.put("recorderNO", pk.getNextId("ydzt190606001", "recorderNO"));
        map.put("dxr1906060001", userId);
        map.put("ydzt190606001", type);
        map.put("bz19060600001", "");
        map.put("glzj190606001", pkid);
        map.put("lcyxh19060601", "");
        map.put("recordName", userId);
        map.put("recordTime", DateHelper.now());
        map.put("modifyName", userId);
        map.put("modifyTime", DateHelper.now());
        String sql = MySqlUtil.getInsertSql("ydzt190606001", map);
        JSONObject jsonObject = new JSONObject();
        try {
            tableService.insertSqlMap(sql);
            jsonObject.put("msg", "处理成功");
            jsonObject.put("success", 1);
        } catch (Exception e) {
            jsonObject.put("msg", "处理失败");
            jsonObject.put("success", 0);
        }
        return jsonObject.toString();
    }

    /**
     * @method: lookedMessage
     * @param: pkid 消息主键
     * @param  userId 登陆人
     * @return:
     * @author: zxd
     * @date: 2019/06/06
     * @description: 处理已查看消息
     */
    @RequestMapping(value = "/lookedMessage", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String lookedMessage(HttpServletRequest request, String pkid, String userId) {

        JSONObject jsonObject = new JSONObject();
        try {
            tableService.updateSqlMap("update message set msgStatus=2,modifyName='" + userId + "',modifyTime='" + DateHelper.now() + "' where curStatus=2 and msgId='" + pkid + "'");
            jsonObject.put("msg", "处理成功");
            jsonObject.put("success", 1);

        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.put("msg", "处理失败");
            jsonObject.put("success", 0);
        }
        return jsonObject.toString();
    }
}
