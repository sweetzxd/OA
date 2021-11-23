package com.oa.core.controller.menuskip;

import com.oa.core.bean.Loginer;
import com.oa.core.bean.dd.FieldData;
import com.oa.core.bean.dd.TaskData;
import com.oa.core.bean.system.FormCustomMade;
import com.oa.core.bean.system.MyUrlRegist;
import com.oa.core.controller.util.LoginUtil;
import com.oa.core.helper.DateHelper;
import com.oa.core.helper.StringHelper;
import com.oa.core.service.dd.DictionaryService;
import com.oa.core.service.system.FormCustomMadeService;
import com.oa.core.service.system.MyUrlRegistService;
import com.oa.core.service.util.TableService;
import com.oa.core.system.postposition.PostPosition;
import com.oa.core.util.ChildUtil;
import com.oa.core.util.DDUtil;
import com.oa.core.util.MySqlUtil;
import com.oa.core.util.PrimaryKeyUitl;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

@Controller
@RequestMapping("/weixin/userpage")
public class WxFormPageSkipController {

    @Autowired
    FormCustomMadeService formCustomMadeService;
    @Autowired
    DictionaryService dictionaryService;
    @Autowired
    TableService tableService;
    @Autowired
    MyUrlRegistService myUrlRegistService;

    @RequestMapping(value = "viewpage/{pageid}", method = RequestMethod.GET)
    public String gotoViewpage(HttpServletRequest request, @PathVariable String pageid) {
        String userid = request.getParameter("userid");
        request.getSession().setAttribute("loginer", LoginUtil.getLoginer(userid));
        request.getSession().setAttribute("role", LoginUtil.getRole(userid));
        String temp = request.getParameter("temp");
        return "redirect:/views/formpage/TestTable.html?userid=" + userid + "&pageid=" + pageid;
    }

    @RequestMapping(value = "/formsave/{pageid}", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String formModiSave(HttpServletRequest request, @PathVariable("pageid") String pageid) {
        JSONObject json = new JSONObject();
        json.put("success", 0);
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        String userId = loginer.getId();
        String type = request.getParameter("status");
        String formid = request.getParameter("formid");
        if (formid == null || formid.equals("") || formid.equals("undefined")) {
            MyUrlRegist myUrlRegist = myUrlRegistService.selectById(pageid);
            formid = myUrlRegist.getFormId();
        }
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            FormCustomMade formCustomMade = formCustomMadeService.selectFormCMByID(formid);
            String formtask = formCustomMade.getFormTask();
            TaskData taskData = dictionaryService.selectTaskName(formtask);
            String fields = taskData.getTaskField();
            String table = taskData.getTableName();
            if (table != null && table != "") {
                String recorderNO;
                if (type != null && type.equals("update")) {
                    recorderNO = request.getParameter(table + "_recorderNO_Value");
                    if (recorderNO == null || recorderNO == "") {
                        json.put("msg", "主键为空");
                    }
                } else {
                    String linkRecorderNO = request.getParameter(table + "_linkRecorderNO");
                    map.put("linkRecorderNO", linkRecorderNO);
                    PrimaryKeyUitl pk = new PrimaryKeyUitl();
                    recorderNO = pk.getNextId(table, "recorderNO");
                    map.put("recordName", userId);
                    map.put("recordTime", DateHelper.now());

                }
                map.put("recorderNO", recorderNO);
                boolean child = true;
                if (fields.length() > 0) {
                    Vector<String> field = StringHelper.string2Vector(fields, ";");
                    for (String fieldName : field) {
                        FieldData fieldData = DDUtil.getFieldData(fieldName);
                        String fieldType = fieldData.getFieldType();
                        if (fieldType != null && fieldType.equals("child")) {
                            child = ChildUtil.setChildUtil(fieldData, recorderNO, userId);
                        } else {
                            String name = table + "_" + fieldName + "_Value";
                            String value = request.getParameter(name);
                            if (value != null) {
                                /*value = new String(value.getBytes("UTF-8"),"UTF-8");*/
                                map.put(fieldName, value);
                            }
                        }
                    }
                } else {
                    json.put("msg", "字段为空");
                }
                map.put("modifyName", userId);
                map.put("modifyTime", DateHelper.now());


                if (false) {
                    PostPosition post = new PostPosition("form");
                    post.setTableName(table);
                    post.setRecorderNO(recorderNO);
                    post.setTableMap(map);
                    boolean postposition = post.getPostPosition(table + "PostPosition");
                }
                if (type != null && type.equals("update")) {
                    tableService.updateSqlMap(MySqlUtil.getUpdateSql(table, recorderNO, map, null));
                } else if (child) {
                    tableService.insertSqlMap(MySqlUtil.getInsertSql(table, map));
                }
                json.put("msg", "成功");
                json.put("success", 1);
            } else {
                json.put("msg", "失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            json.put("msg", "失败");
        }
        return json.toString();
    }

}

