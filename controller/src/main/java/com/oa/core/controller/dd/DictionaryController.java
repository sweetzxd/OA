package com.oa.core.controller.dd;


import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oa.core.helper.DateHelper;
import com.oa.core.helper.StringHelper;
import com.oa.core.interceptor.Logined;
import com.oa.core.bean.Loginer;
import com.oa.core.bean.dd.FieldData;
import com.oa.core.bean.dd.TableData;
import com.oa.core.bean.dd.TaskData;
import com.oa.core.bean.util.PageUtil;
import com.oa.core.listener.InitDataListener;
import com.oa.core.service.dd.DictionaryService;
import com.oa.core.util.PrimaryKeyUitl;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.*;

@Controller
@RequestMapping("/dictionary")
public class DictionaryController {

    @Autowired
    DictionaryService dictionaryService;

    /**
     * 跳转字段定义页面
     */
    @RequestMapping(value = "/fieldlist", method = RequestMethod.GET)
    public ModelAndView fieldList(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("manage/field_list");
        mav.addObject("type", "list");
        return mav;
    }
    /**
     * 刷新数据字字典
     * */
    @RequestMapping(value = "/breakdata", method = RequestMethod.GET)
    public ModelAndView breakData(HttpServletRequest request, String key) {
        new InitDataListener(key);
        ModelAndView mav = new ModelAndView("manage/field_list");
        mav.addObject("type", "list");
        return mav;
    }

    @RequestMapping("/selectfield")
    @ResponseBody
    public String selectAllField(HttpServletRequest request, String option, String inputval, int limit, int page) {
        // 得到表单数据
        FieldData fieldData = new FieldData();
        if ("fieldName".equals(option)) {
            fieldData.setFieldName(inputval);
        } else if ("fieldTitle".equals(option)) {
            fieldData.setFieldTitle(inputval);
        } else if ("fieldType".equals(option)) {
            fieldData.setFieldType(inputval);
        }
        int count = dictionaryService.selectFieldCount(fieldData);
        PageUtil pageUtil = new PageUtil();
        pageUtil.setPageSize(limit);
        pageUtil.setCurrentPage(page);
        fieldData.setStartRow(pageUtil.getStartRow());
        fieldData.setEndRow(pageUtil.getEndRow() - pageUtil.getStartRow());
        List<FieldData> fieldList = dictionaryService.selectFieldList(fieldData);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 0);
        jsonObject.put("msg", "");
        jsonObject.put("count", count);
        jsonObject.put("data", fieldList);
        jsonObject.put("success",1);
        return jsonObject.toString();
    }

    /**
     * 跳转字段定义页面
     */
    @RequestMapping(value = "/gotofieldadd", method = RequestMethod.GET)
    public ModelAndView gotoFieldAdd(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("manage/field_list");
        mav.addObject("type", "add");
        return mav;
    }

    @Logined
    @RequestMapping(value = "/insertfield", method = RequestMethod.POST, produces = {"text/html;charset=UTF-8;", "application/json;"})
    @ResponseBody
    public String insertField(HttpServletRequest request) {
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        String fieldName = request.getParameter("fieldName");
        String fieldTitle = request.getParameter("fieldTitle");
        String fieldType = request.getParameter("fieldType");
        int fieldNum = Integer.parseInt(request.getParameter("fieldNum"));
        String optionVal = request.getParameter("optionVal");
        String defaultVal = request.getParameter("defaultVal");
        String special = request.getParameter("special");
        int required = Integer.parseInt(request.getParameter("required"));

        if(fieldTitle!=null && !fieldTitle.equals("")) {

            fieldName = PrimaryKeyUitl.getDataId(fieldTitle, "field");
            FieldData fieldData = new FieldData();
            fieldData.setFieldName(fieldName);
            fieldData.setFieldTitle(fieldTitle);
            fieldData.setFieldType(fieldType);
            fieldData.setRequired(required);
            fieldData.setFieldNum(fieldNum);
            fieldData.setDefaultVal(defaultVal);
            fieldData.setOptionVal(optionVal);
            fieldData.setSpecial(special);
            if (loginer != null) {
                String userId = loginer.getId();
                fieldData.setRecName(userId);
                fieldData.setModName(userId);
            }
            try {
                fieldData.setRecTime(DateHelper.now());
                fieldData.setModTime(DateHelper.now());
                dictionaryService.insertField(fieldData);
                return "1";
            } catch (Exception e) {
                return "0";
            }
        }else{
            return "0";
        }
    }

    /**
     * 修改字段
     */
    @RequestMapping(value = "/fieldinfo/{fieldName}")
    @ResponseBody
    public String fieldInfo(Model model, HttpServletRequest request, @PathVariable("fieldName") String fieldName) {
        FieldData fieldData = dictionaryService.selectFieldName(fieldName);
        return JSON.toJSONString(fieldData);
    }

    /**
     * 跳转字段定义页面
     */
    @RequestMapping(value = "/gotofieldmodi", method = RequestMethod.GET)
    public ModelAndView gotoFieldModi(HttpServletRequest request,String fieldName) {
        ModelAndView mav = new ModelAndView("manage/field_list");
        FieldData fieldData = dictionaryService.selectFieldName(fieldName);
        mav.addObject("field", fieldData);
        mav.addObject("type", "modi");
        return mav;
    }

    @Logined
    @RequestMapping(value = "/upfield", method = RequestMethod.POST, produces = {"text/html;charset=UTF-8;", "application/json;"})
    @ResponseBody
    public String updateField(HttpServletRequest request) {
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        Date date = new Date();
        Timestamp nousedate = new Timestamp(date.getTime());

        String fieldName = request.getParameter("fieldName");
        String fieldTitle = request.getParameter("fieldTitle");
        String fieldType = request.getParameter("fieldType");
        int fieldNum = Integer.parseInt(request.getParameter("fieldNum"));
        String optionVal = request.getParameter("optionVal");
        String defaultVal = request.getParameter("defaultVal");
        String special = request.getParameter("special");
        int required = Integer.parseInt(request.getParameter("required"));

        FieldData fieldData = new FieldData();
        fieldData.setFieldName(fieldName);
        fieldData.setFieldTitle(fieldTitle);
        fieldData.setFieldType(fieldType);
        fieldData.setRequired(required);
        fieldData.setFieldNum(fieldNum);
        fieldData.setDefaultVal(defaultVal);
        fieldData.setOptionVal(optionVal);
        fieldData.setSpecial(special);
        if (loginer != null) {
            String userId = loginer.getId();
            fieldData.setModName(userId);
        }
        try {
            fieldData.setModTime(nousedate);
            dictionaryService.updateField(fieldData);
            return "1";
        } catch (Exception e) {
            return "0";
        }
    }

    /**
     * 返回String类型的结果
     * 检查法性,如果已经存在，返回false，否则返回true(返回json数据，格式为{"valid",true})
     */
    @RequestMapping(value = "/inspect/{type}", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String inspect(@PathVariable("type") String type, @RequestParam(value = "value", required = true) String value) {
        boolean result = true;
        if (type.equals("field")) {
            List<FieldData> allField = dictionaryService.selectAllField();
            for (FieldData fieldData : allField) {
                if (fieldData.getFieldName().equals(value)) {
                    result = false;
                    break;
                }
            }
        } else if (type.equals("table")) {
            List<TableData> allTable = dictionaryService.selectAllTable();
            for (TableData tableData : allTable) {
                if (tableData.getTableName().equals(value)) {
                    result = false;
                    break;
                }
            }
        } else if (type.equals("task")) {
            List<TaskData> allTask = dictionaryService.selectAllTask();
            for (TaskData taskData : allTask) {
                if (taskData.getTaskName().equals(value)) {
                    result = false;
                    break;
                }
            }
        }
        Map<String, Boolean> map = new HashMap<>();
        map.put("valid", result);
        ObjectMapper mapper = new ObjectMapper();
        String resultString = "";
        try {
            resultString = mapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return resultString;
    }

    /**
     * 跳转数据表定义
     *
     * @return tablelist
     */
    @RequestMapping("/tablelist")
    public ModelAndView tableList(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("manage/table_list");
        mav.addObject("type", "list");
        return mav;
    }

    /**
     * 数据表列表
     */
    @RequestMapping("/selecttable")
    @ResponseBody
    public String selectTable(HttpServletRequest request, String option, String inputval, int limit, int page) {
        TableData tableData = new TableData();
        if ("tableName".equals(option)) {
            tableData.setTableName(inputval);
        } else if ("tableTitle".equals(option)) {
            tableData.setTableTitle(inputval);
        } else if ("tableField".equals(option)) {
            tableData.setTableField(inputval);
        }
        int count = dictionaryService.selectTableCount(tableData);
        PageUtil pageUtil = new PageUtil();
        pageUtil.setPageSize(limit);
        pageUtil.setCurrentPage(page);
        tableData.setStartRow(pageUtil.getStartRow());
        tableData.setEndRow(pageUtil.getEndRow() - pageUtil.getStartRow());
        List<TableData> tableDataList = dictionaryService.selectTableList(tableData);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 0);
        jsonObject.put("msg", "");
        jsonObject.put("count", count);
        jsonObject.put("data", tableDataList);
        jsonObject.put("success",1);
        return jsonObject.toString();
    }

    @RequestMapping("/inserttable")
    public ModelAndView insertTable(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("manage/table_list");
        mav.addObject("type", "ins");
        return mav;
    }

    /**
     * 查询数据表定义
     */
    @RequestMapping("/tablemodi/{tableName}")
    public ModelAndView tableModi(HttpServletRequest request, @PathVariable("tableName") String tableName) {
        TableData tableData = dictionaryService.selectTableName(tableName);
        String tableField = tableData.getTableField();
        List<FieldData> tableFieldList = null;
        if(tableField!=null && tableField.length()>0) {
            List<String> tabfl = StringHelper.string2Vector(tableField, ";");
            tableFieldList = dictionaryService.selectFieldByTable(tabfl);
        }
        ModelAndView mav = new ModelAndView("manage/table_list");
        mav.addObject("tableData", tableData);
        mav.addObject("tablefieldlist", tableFieldList);
        mav.addObject("type", "modi");
        return mav;
    }

    @Logined
    @RequestMapping(value = "/savetable/{type}", method = RequestMethod.POST, produces = {"text/html;charset=UTF-8;", "application/json;"})
    public ModelAndView saveTable(HttpServletRequest request, @PathVariable("type") String type) {
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        String tableName = request.getParameter("tableName");
        String tableTitle = request.getParameter("tableTitle");
        String tableField = request.getParameter("tableField");
        try {
            tableTitle = new String(tableTitle.getBytes("iso-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if(tableTitle!=null && !tableTitle.equals("")) {
            TableData tableData = new TableData();
            tableData.setTableName(tableName);
            tableData.setTableTitle(tableTitle);
            tableData.setTableField(tableField);
            if (loginer != null) {
                String userId = loginer.getId();
                if (type.equals("ins")) {
                    if(tableName!=null && tableName.length()<5) {
                        tableData.setTableName(PrimaryKeyUitl.getDataId(tableTitle, "table"));
                    }
                    tableData.setRecName(userId);
                }
                tableData.setModName(userId);
            }
            try {
                tableData.setModTime(DateHelper.now());
                if (type.equals("ins")) {
                    tableData.setRecTime(DateHelper.now());
                    dictionaryService.insertTable(tableData);
                } else if (type.equals("modi")) {
                    dictionaryService.updateTable(tableData);
                }
                return new ModelAndView("redirect:/dictionary/tablelist.do");
            } catch (Exception e) {
                return new ModelAndView("redirect:/dictionary/tablelist.do");
            }
        }else{
            return new ModelAndView("redirect:/dictionary/tablelist.do");
        }
    }

    /**
     * 跳转任务定义表
     *
     * @return fieldlist
     */
    @RequestMapping(value = "/tasklist", method = RequestMethod.GET)
    public ModelAndView taskList(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("manage/task_list");
        mav.addObject("type", "list");
        return mav;
    }

    /**
     * 任务列表
     */
    @RequestMapping("/selecttask")
    @ResponseBody
    public String selectTask(HttpServletRequest request, String option, String inputval, int limit, int page) {
        TaskData taskData = new TaskData();
        if ("taskName".equals(option)) {
            taskData.setTaskName(inputval);
        } else if ("taskTitle".equals(option)) {
            taskData.setTaskTitle(inputval);
        } else if ("taskType".equals(option)) {
            taskData.setTaskType(inputval);
        } else if ("tableName".equals(option)) {
            taskData.setTableName(inputval);
        } else if ("tableField".equals(option)) {
            taskData.setTaskField(inputval);
        }
        int count = dictionaryService.selectTaskCount(taskData);
        PageUtil pageUtil = new PageUtil();
        pageUtil.setPageSize(limit);
        pageUtil.setCurrentPage(page);
        taskData.setStartRow(pageUtil.getStartRow());
        taskData.setEndRow(pageUtil.getEndRow() - pageUtil.getStartRow());
        List<TaskData> taskDataList = dictionaryService.selectTaskList(taskData);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 0);
        jsonObject.put("msg", "");
        jsonObject.put("count", count);
        jsonObject.put("data", taskDataList);
        jsonObject.put("success",1);
        return jsonObject.toString();
    }

    @RequestMapping("/gotoinserttask")
    public ModelAndView gotoInsertTask(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("manage/task_list");
        mav.addObject("type", "ins");
        return mav;
    }

    @RequestMapping("/gotomoditask")
    public ModelAndView gotoModiTask(HttpServletRequest request,String taskName) {
        TaskData taskData = dictionaryService.selectTaskName(taskName);
        String tableName = taskData.getTableName();
        String taskField = taskData.getTaskField();
        TableData tableData = dictionaryService.selectTableName(tableName);
        String tableField = tableData.getTableField();
        List<String> taskfl = null;
        List<FieldData> tableFieldList = null;
        List<FieldData> taskFieldList = null;
        if(taskField!=null && taskField.length()>0){
            taskfl = StringHelper.string2Vector(taskField, ";");
            taskFieldList = dictionaryService.selectFieldByTable(taskfl);
        }
        if(tableField!=null && tableField.length()>0) {
            List<String> tabfl = StringHelper.string2Vector(tableField, ";");
            if(taskfl!=null) {
                tabfl.removeAll(taskfl);
            }
            if(tabfl!=null && tabfl.size()>0) {
                tableFieldList = dictionaryService.selectFieldByTable(tabfl);
            }

        }
        ModelAndView mav = new ModelAndView("manage/task_list");
        mav.addObject("task", taskData);
        mav.addObject("taskfieldlist", taskFieldList);
        mav.addObject("table", tableData);
        mav.addObject("tablefieldList", tableFieldList);
        mav.addObject("type", "modi");
        return mav;
    }

    @Logined
    @RequestMapping(value = "/savetask/{type}", method = RequestMethod.POST, produces = {"text/html;charset=UTF-8;", "application/json;"})
    public ModelAndView saveTask(HttpServletRequest request, @PathVariable("type") String type) {
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        String taskName = request.getParameter("taskName");
        String taskTitle = request.getParameter("taskTitle");
        String taskType = request.getParameter("taskType");
        String tableName = request.getParameter("tableName");
        String taskField = request.getParameter("taskField");
        try {
            taskTitle = new String(taskTitle.getBytes("iso-8859-1"), "UTF-8");
            taskType = new String(taskType.getBytes("iso-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        TaskData taskData = new TaskData();
        if(taskTitle!=null && !taskTitle.equals("")) {
            taskData.setTaskName(taskName);
            taskData.setTaskTitle(taskTitle);
            taskData.setTaskType(taskType);
            taskData.setTableName(tableName);
            taskData.setTaskField(taskField);
            if (loginer != null) {
                String userId = loginer.getId();
                if (type.equals("ins")) {
                    if(taskName!=null && taskName.length()<5) {
                        taskData.setTaskName(PrimaryKeyUitl.getDataId(taskTitle, "task"));
                    }
                    taskData.setRecName(userId);
                }
                taskData.setModName(userId);
            }
            try {
                taskData.setModTime(DateHelper.now());
                if (type.equals("ins")) {
                    taskData.setRecTime(DateHelper.now());
                    dictionaryService.insertTask(taskData);
                } else if (type.equals("modi")) {
                    dictionaryService.updateTask(taskData);
                }
                return new ModelAndView("redirect:/dictionary/tasklist.do");
            } catch (Exception e) {
                return new ModelAndView("redirect:/dictionary/tasklist.do");
            }
        }else{
            return new ModelAndView("redirect:/dictionary/tasklist.do");
        }
    }

    /**
     * 数据表选择处理方法
     */
    @RequestMapping(value = "/gototableselect", method = RequestMethod.GET)
    public ModelAndView gotoTableSelect(HttpServletRequest request) {
        String thead = "<th lay-data=\"{type:'radio', width:80}\">选择</th>";
        thead += "<th lay-data=\"{field:'tableName', width:150}\">数据表主键</th>";
        thead += "<th lay-data=\"{field:'tableTitle'}\">数据表名称</th>";
        ModelAndView mav = new ModelAndView("manage/task_list");
        mav.addObject("thead", thead);
        mav.addObject("url", "/dictionary/selecttable.do");
        mav.addObject("id", "tableName");
        mav.addObject("name", "tableTitle");
        mav.addObject("type", "table");
        return mav;
    }

    @Logined
    @RequestMapping(value = "/deltask/{taskName}", method = RequestMethod.POST, produces = {"text/html;charset=UTF-8;", "application/json;"})
    @ResponseBody
    public String delTaskField(HttpServletRequest request, @PathVariable("taskName") String taskName) {
        TaskData taskData = new TaskData();
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        if (loginer != null) {
            String userId = loginer.getId();
            taskData.setDelName(userId);
        }
        Date date = new Date();
        Timestamp nousedate = new Timestamp(date.getTime());
        taskData.setDelTime(nousedate);
        try {
            taskData.setTaskName(taskName);
            dictionaryService.deleteTask(taskData);
            return "1";
        } catch (Exception e) {
            return "0";
        }
    }

    @Logined
    @RequestMapping(value = "/loadfield", method = RequestMethod.POST)
    public void loadField(HttpServletRequest request) {
        new InitDataListener("fieldData");
    }
    @Logined
    @RequestMapping(value = "/loadtable", method = RequestMethod.POST)
    public void loadTable(HttpServletRequest request) {
        new InitDataListener("tableDate");
    }
    @Logined
    @RequestMapping(value = "/loadtask", method = RequestMethod.POST)
    public void loadTask(HttpServletRequest request) {
        new InitDataListener("taskData");
    }
}
