package com.oa.core.controller.module;

import com.oa.core.bean.Loginer;
import com.oa.core.bean.dd.FieldData;
import com.oa.core.bean.dd.TaskData;
import com.oa.core.bean.system.FormCustomMade;
import com.oa.core.bean.system.MyUrlRegist;
import com.oa.core.helper.DateHelper;
import com.oa.core.service.dd.DictionaryService;
import com.oa.core.service.system.FormCustomMadeService;
import com.oa.core.service.system.MyUrlRegistService;
import com.oa.core.service.util.TableService;
import com.oa.core.util.AccessUtil;
import com.oa.core.util.MySqlUtil;
import com.oa.core.util.PrimaryKeyUitl;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @ClassName:DesignformulasController
 * @author:wsx
 * @Date:2018/12/17
 * @Time:下午 3:37
 * @Version V1.0
 * @Explain 计算公式表
 */

@Controller
@RequestMapping("/designformulas")
public class DesignformulasController {

    @Autowired
    TableService tableService;
    @Autowired
    MyUrlRegistService myUrlRegistService;
    @Autowired
    FormCustomMadeService formCustomMadeService;
    @Autowired
    DictionaryService dictionaryService;

    //跳转计算公式管理页面
    @RequestMapping(value = "/gotoDesignformulas", method = RequestMethod.GET)
    public ModelAndView gotoDesignformulas(HttpServletRequest request, String type,String pageid,String recorderNO) {
        ModelAndView mav = new ModelAndView("module/designformulas");
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        mav.addObject("type", type);
        mav.addObject("recorderNO", recorderNO);
        if(pageid!=null&&!pageid.equals("")&&!pageid.equals("null")){
            MyUrlRegist myUrlRegist = myUrlRegistService.selectById(pageid);
            FormCustomMade formCustomMade = formCustomMadeService.selectFormCMByID(myUrlRegist.getFormId());
            TaskData taskData = new TaskData();
            if(formCustomMade.getFormTask()!=null&&!formCustomMade.getFormTask().equals("")){
                taskData = dictionaryService.selectTaskName(formCustomMade.getFormTask());
            }else{
                taskData = dictionaryService.selectTaskName(formCustomMade.getListTask());
            }
            String taskField = taskData.getTaskField();
            mav.addObject("tableName", taskData.getTableName());
            LinkedHashMap<String,Object> fieldMap = new LinkedHashMap<>();
            String[] fields = taskField.split(";");
            for(String field : fields){
                FieldData fielddata = dictionaryService.selectFieldName(field);
                if(fielddata.getFieldType().equals("int")||fielddata.getFieldType().equals("decimal")){
                    fieldMap.put(fielddata.getFieldName(),fielddata.getFieldTitle());
                }
            }
            mav.addObject("fieldMap",fieldMap);
        }
        if(recorderNO!=null&&!recorderNO.equals("")){
            String sql = "SELECT * FROM DesignFormulas WHERE recorderNO ='"+ recorderNO + "' AND curStatus=2";
            Map<String,Object> gsMap = tableService.selectSqlMap(sql);
            mav.addObject("gsMap",gsMap);
            if(pageid==null||pageid.equals("")||pageid.equals("null")){
                String tableName = String.valueOf(gsMap.get("tableName"));
                mav.addObject("tableName", tableName);
                FieldData fieldData = new FieldData();
                fieldData.setTableName(tableName);
                List<FieldData> list = dictionaryService.selectFieldList(fieldData);
                LinkedHashMap<String,Object> fieldMap = new LinkedHashMap<>();
                for(FieldData fieldData1 : list){
                    if(fieldData1.getFieldType().equals("int")||fieldData1.getFieldType().equals("decimal")){
                        fieldMap.put(fieldData1.getFieldName(),fieldData1.getFieldTitle());
                    }
                }
                mav.addObject("fieldMap",fieldMap);
            }
        }
        return mav;
    }

    //获取表单树
    @RequestMapping(value = "/getAccessTree", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String getAccessTree(HttpServletRequest request) {
        List<String> list = new ArrayList<>();
        List<String> wheres = new ArrayList<>();
        String where = "(formTask = null or formTask ='') and (formTask = null or formTask ='')";
        wheres.add(where);
        String sql = MySqlUtil.getFieldSql("formcmName","formpage",wheres);
        list = tableService.selectSql(sql);
        String formIds = "";
        for(String field : list){
            formIds += "'" + field + "',";
        }
        MyUrlRegist myUrlRegist = new MyUrlRegist();
        myUrlRegist.setFormIds(formIds.substring(0,formIds.length()-1));
        myUrlRegist.setFormType(0);
        List<MyUrlRegist> myurllist = myUrlRegistService.selectTerms(myUrlRegist);
        Map<String,Object> map = new HashMap<>();
        for(MyUrlRegist myUrlRegist1 : myurllist){
            MyUrlRegist myUrlRegist2 = myUrlRegistService.selectById(myUrlRegist1.getParentId());
            map.put(myUrlRegist1.getParentId(),myUrlRegist2);
        }
        int i = 1;
        for(String str : map.keySet()){
            i++;
            myurllist.add((MyUrlRegist)map.get(str));
        }
        return AccessUtil.getMenu(myurllist);
    }

    //获取计算公式管理数据
    @RequestMapping(value = "/selectDesignformulas", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String selectDesignformulas(HttpServletRequest request, String pageid, String pagetitle) {
        String tableName = "";
        StringBuffer sql = new StringBuffer("SELECT ");
        sql.append("recorderNO,formulasName,formulasValue,formulasResult,formulasType,tableName,reserveField,linkRecorderNO,recordName,recordTime,modifyName,modifyTime");
        sql.append(" FROM DesignFormulas ");
        sql.append(" WHERE curStatus=2");
        if(pageid!=null&&!pageid.equals("")){
            MyUrlRegist myUrlRegist = myUrlRegistService.selectById(pageid);
            if(myUrlRegist.getParentId().equals("topmenu")){
                MyUrlRegist myUrlRegist1 = new MyUrlRegist();
                myUrlRegist1.setParentId(pageid);
                myUrlRegist1.setFormType(0);
                List<MyUrlRegist> list = myUrlRegistService.selectTerms(myUrlRegist1);
                for(MyUrlRegist myUrlRegist2 : list){
                    FormCustomMade formCustomMade = formCustomMadeService.selectFormCMByID(myUrlRegist2.getFormId());
                    TaskData taskData = new TaskData();
                    if(formCustomMade.getFormTask()!=null&&!formCustomMade.getFormTask().equals("")){
                        taskData = dictionaryService.selectTaskName(formCustomMade.getFormTask());
                    }else{
                        taskData = dictionaryService.selectTaskName(formCustomMade.getListTask());
                    }
                    tableName += "'"+taskData.getTableName()+"'"+",";
                }
                tableName = tableName.substring(0,tableName.length()-1);
            }else{
                FormCustomMade formCustomMade = formCustomMadeService.selectFormCMByID(myUrlRegist.getFormId());
                TaskData taskData = new TaskData();
                if(formCustomMade.getFormTask()!=null&&!formCustomMade.getFormTask().equals("")){
                    taskData = dictionaryService.selectTaskName(formCustomMade.getFormTask());
                }else{
                    taskData = dictionaryService.selectTaskName(formCustomMade.getListTask());
                }
                tableName = "'"+taskData.getTableName()+"'";
            }
            sql.append(" AND tableName in (" + tableName + ")");
        }
        List<Map<String, Object>> sqlval = tableService.selectSqlMapList(sql.toString());

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 0);
        jsonObject.put("msg", "");
        jsonObject.put("count", sqlval.size());
        jsonObject.put("data", sqlval);
        jsonObject.put("success",1);
        jsonObject.put("pageid", pageid);
        jsonObject.put("pagetitle", pagetitle);
        return jsonObject.toString();
    }

    //添加/修改计算公式数据
    @RequestMapping(value = "/saveDesignformulas", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String saveDesignformulas(HttpServletRequest request, @RequestParam String data){
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        JSONObject json = new JSONObject(data);
        String recorderNO = json.getString("recorderNO");
        String formulasName = json.getString("formulasName");
        String formulasValue = json.getString("formulasValue");
        String formulasType = json.getString("formulasType");
        String formulasResult = json.getString("formulasResult");
        String tableName = json.getString("tableName");
        Map<String,Object> map = new HashMap<>();
        map.put("formulasName",formulasName);
        map.put("formulasValue",formulasValue);
        map.put("formulasType",formulasType);
        map.put("formulasResult",formulasResult);
        map.put("tableName",tableName);
        map.put("modifyName",loginer.getId());
        if(recorderNO!=null&&!recorderNO.equals("")){
            String sql = MySqlUtil.getUpdateSql("designFormulas", recorderNO, map, null);
            tableService.updateSqlMap(sql);
        }else{
            PrimaryKeyUitl pk = new PrimaryKeyUitl();
            String id = pk.getNextId("designFormulas", "recorderNO");
            map.put("recorderNO",id);
            map.put("recordName",loginer.getId());
            map.put("recordTime",DateHelper.now());
            map.put("modifyTime",DateHelper.now());
            String sql = MySqlUtil.getInsertSql("designFormulas",map);
            tableService.insertSqlMap(sql);
        }
        return "1";
    }

    //删除计算公式数据
    @RequestMapping(value = "/delDesignformulas", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String delDesignformulas(HttpServletRequest request,String recorderNO){
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        Map<String,Object> map = new HashMap<>();
        map.put("deleteName",loginer.getId());
        map.put("deleteTime",DateHelper.now());
        String sql = MySqlUtil.getDeleteSql("designFormulas",map,recorderNO);
        tableService.updateSqlMap(sql);
        return "1";
    }

}
