package com.oa.core.controller.system;

import com.alibaba.fastjson.JSON;
import com.oa.core.bean.system.FormCustomMade;
import com.oa.core.bean.Loginer;
import com.oa.core.bean.dd.FieldData;
import com.oa.core.bean.dd.TableData;
import com.oa.core.bean.dd.TaskData;
import com.oa.core.bean.util.PageUtil;
import com.oa.core.bean.work.WorkFlowNode;
import com.oa.core.helper.DateHelper;
import com.oa.core.helper.JsonHelper;
import com.oa.core.interceptor.Logined;
import com.oa.core.service.dd.DictionaryService;
import com.oa.core.service.system.FormCustomMadeService;
import com.oa.core.service.util.TableService;
import com.oa.core.service.work.WorkFlowNodeService;
import com.oa.core.util.*;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.util.*;

@Controller
@RequestMapping("/form_custom")
public class FormCustomMadeController {

    @Autowired
    FormCustomMadeService formCustomMadeService;
    @Autowired
    DictionaryService dictionaryService;
    @Autowired
    TableService tableService;
    @Autowired
    WorkFlowNodeService workFlowNodeService;

    @RequestMapping(value = "/form_custom_made", method = RequestMethod.GET)
    public ModelAndView gotoFormCustomMade(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("manage/form_custom_made");
        mav.addObject("type", "list");
        return mav;
    }

    @RequestMapping(value = "/formcm/modi", method = RequestMethod.GET)
    public ModelAndView gotoFormcmModi(HttpServletRequest request) {
        String recno = request.getParameter("recno");
        FormCustomMade formCustomMade = formCustomMadeService.selectFormCMByID(recno);
        ModelAndView mav = new ModelAndView("manage/form_custom_made");
        mav.addObject("formcm", formCustomMade);
        mav.addObject("type", "modi");
        return mav;
    }

    @RequestMapping(value = "/formcm/add", method = RequestMethod.GET)
    public ModelAndView gotoFormcmAdd(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("manage/form_custom_made");
        mav.addObject("type", "add");
        return mav;
    }

    @RequestMapping(value = "/gotoformsetup", method = RequestMethod.GET)
    public ModelAndView gotoFormSetUp(HttpServletRequest request, String type, String data) {
        FieldData fieldData = new FieldData();
        JSONObject json = new JSONObject(data);
        String  fieldNum = json.get("fieldNum")+"";
        if(fieldNum==null){
            json.put("fieldNum",200);
        }
        System.out.println("5555"+json+"5555");
        fieldData.setJsonValue(json);
        ModelAndView mav = new ModelAndView("manage/form_edit_setup");
        mav.addObject("type", type);
        mav.addObject("field", fieldData);
        return mav;
    }

    @Logined
    @RequestMapping(value = "/formedit", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView gotoFormEdit(HttpServletRequest request, String formid, String wkflwId) {
        FormCustomMade formCustomMade = new FormCustomMade();
        ModelAndView mav = new ModelAndView("manage/form_edit_page");
        //截取节点id
        String[] str = formid.split(",");
        //判断是否是从编辑节点页面跳转
        if (str[0].equals("workflow")) {
            //判断formId是否有相对应页面
            WorkFlowNode node = workFlowNodeService.selectById(str[1]);
            if (node.getFormId() != null) {
                FormCustomMade formCustomMade1 = formCustomMadeService.selectFormCMByID(node.getFormId());
                if (formCustomMade1 != null) {
                    formCustomMade.setFormcmName(node.getFormId());
                    formCustomMade.setFormTask(formCustomMade1.getFormTask());
                } else {
                    PrimaryKeyUitl pk = new PrimaryKeyUitl();
                    String formcmName = pk.getNextId("formpage", "formcmName", "flowtable");
                    formCustomMade.setFormcmName(formcmName);
                }
            } else {
                PrimaryKeyUitl pk = new PrimaryKeyUitl();
                String formcmName = pk.getNextId("formpage", "formcmName", "flowtable");
                formCustomMade.setFormcmName(formcmName);
            }
            formCustomMade.setFormcmTitle(node.getNodeTitle());
            mav.addObject("nodeid", str[1]);
            mav.addObject("formid", node.getFormId());
            mav.addObject("wkflwId", wkflwId);
        } else {
            formCustomMade.setFormcmName(formid);
            formCustomMade = formCustomMadeService.selectFormCM(formCustomMade);
        }
        //判断页面是否存在对应表单,获取对应字段
        if (formCustomMade.getFormTask() != null && !formCustomMade.getFormTask().equals("")) {
            TaskData taskData = dictionaryService.selectTaskName(formCustomMade.getFormTask());
            //判断表单是否存在
            if (taskData != null) {
                //判断表单对应的table表是否存在
                if (taskData.getTableName() != null && !taskData.getTableName().equals("")) {
                    TableData tableData = dictionaryService.selectTableName(taskData.getTableName());

                    if (tableData != null) {
                        mav.addObject("tableId", taskData.getTableName());
                    }
                    String[] fieldnames = taskData.getTaskField().split(";");
                    List tablefields = new ArrayList();
                    for (int i = 0; i < fieldnames.length; i++) {
                        StringToHtmlUtil htmlUtil = new StringToHtmlUtil();
                        tablefields.add(htmlUtil.getFieldValue(taskData.getTableName(),fieldnames[i],null));
                    }
                    mav.addObject("tablefields", tablefields);
                    System.out.println(tablefields+"99999");
                }
            }
        }
        mav.addObject("tab", formCustomMade);
        return mav;
    }

    /**
     * 新建页面插入基础定义对应数据
     * fieldData、tableData、taskData
     */
    @RequestMapping(value = "/form_edit_save/{formcmName}", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String saveFormEdit(HttpServletRequest request, @PathVariable String formcmName) {
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        Timestamp nousedate = DateHelper.now();
        String userId = null;
        if (loginer != null) {
            userId = loginer.getId();
        }
        try {
            String postData = request.getParameter("postData");
            JSONObject json = new JSONObject(postData);
            boolean exist = false;
            String tableName = json.getString("id");
            String tableTitle = json.getString("name");
            if ("tableId".equals(tableName)) {
                tableName = PrimaryKeyUitl.getDataId(tableTitle, "table");
                exist = false;
            } else {
                exist = true;
            }

            String tableField = "";
            List<FieldData> listField = new ArrayList<FieldData>();
            JSONArray jsonArray = json.getJSONArray("data");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject subObject = jsonArray.getJSONObject(i);
                String fieldName = subObject.getString("fieldName");
                String fieldTitle = subObject.getString("fieldTitle");
                if (fieldName.indexOf("_field") >= 0) {
                    fieldName = PrimaryKeyUitl.getDataId(fieldTitle, "field");
                }
                tableField += fieldName + ";";
                int fieldNum = 200;
                if (!subObject.isNull("fieldNum")) {
                    fieldNum = subObject.getInt("fieldNum");
                }
                int fieldDigit = 0;
                if (!subObject.isNull("fieldDigit")) {
                    fieldDigit = subObject.getInt("fieldDigit");
                }
                String optionVal = "";
                if (!subObject.isNull("optionVal")) {
                    optionVal = subObject.getString("optionVal");
                }
                String defaultVal = "";
                if (!subObject.isNull("defaultVal")) {
                    defaultVal = subObject.getString("defaultVal");
                }
                String special = "";
                if (!subObject.isNull("special")) {
                    special = subObject.getString("special");
                }
                int required = 0;
                if (!subObject.isNull("required")) {
                    required = subObject.getInt("required");
                }
                FieldData fieldData = new FieldData();
                fieldData.setFieldName(fieldName);
                int num = dictionaryService.selectFieldCount(fieldData);
                fieldData.setFieldTitle(fieldTitle);
                String fieldType = "text";
                if (!subObject.isNull("fieldType")) {
                    fieldType = subObject.getString("fieldType");
                }
                fieldData.setFieldType(fieldType);
                fieldData.setFieldNum(fieldNum);
                fieldData.setFieldDigit(fieldDigit);
                fieldData.setTableName(tableName);
                fieldData.setOptionVal(optionVal);
                fieldData.setDefaultVal(defaultVal);
                fieldData.setSpecial(special);
                fieldData.setRequired(required);
                listField.add(fieldData);
                if (num > 0) {
                    fieldData.setModName(userId);
                    fieldData.setModTime(nousedate);
                    dictionaryService.updateField(fieldData);
                } else {
                    fieldData.setRecName(userId);
                    fieldData.setRecTime(nousedate);
                    fieldData.setModName(userId);
                    fieldData.setModTime(nousedate);
                    dictionaryService.insertField(fieldData);
                    if (exist) {
                        String fieldSql = MySqlUtil.getAlterSql(tableName,fieldData);
                        tableService.creatTable(fieldSql);
                    }
                }
            }
            TableData tableData;
            if (exist) {
                //数组转化为list不支持add和remove方法，所以修改此处
                //List<String> fields = Arrays.asList(dictionaryService.selectTableName(tableName).getTableField().split(";"));
                //List<String> tfields = Arrays.asList(tableField.split(";"));
                List<String> fields = new ArrayList<>();
                List<String> tfields = new ArrayList<>();
                String[] str1 = dictionaryService.selectTableName(tableName).getTableField().split(";");
                for (int i = 0; i < str1.length; i++) {
                    fields.add(str1[i]);
                }
                String[] str2 = tableField.split(";");
                for (int j = 0; j < str2.length; j++) {
                    tfields.add(str2[j]);
                }
                fields.removeAll(tfields);
                tfields.addAll(fields);
                tableData = new TableData();
                tableData.setTableName(tableName);
                tableData.setTableTitle(tableTitle);
                tableData.setTableField(StringUtils.join(tfields.toArray(), ";") + ";");
                tableData.setModName(userId);
                tableData.setModTime(nousedate);
                dictionaryService.updateTable(tableData);
            } else {
                tableData = new TableData();
                tableData.setTableName(tableName);
                tableData.setTableTitle(tableTitle);
                tableData.setTableField(tableField);
                tableData.setRecName(userId);
                tableData.setRecTime(nousedate);
                tableData.setModName(userId);
                tableData.setModTime(nousedate);
                dictionaryService.insertTable(tableData);
                try {
                    String tableSql = MySqlUtil.getCreateSql(tableName,tableTitle, listField);
                    tableService.creatTable(tableSql);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            TaskData taskData;
            String formTask = "", listTask = "";
            if (exist) {
                int num = 0;
                FormCustomMade formCustomMade = formCustomMadeService.selectFormCMByID(formcmName);
                formTask = formCustomMade.getFormTask();
                taskData = new TaskData();
                taskData.setTaskName(formTask);
                taskData.setTaskField(tableField);
                taskData.setModName(userId);
                taskData.setModTime(nousedate);
                num = dictionaryService.updateTask(taskData);
                //判断表单是否存在列表任务
                if (formCustomMade.getListTask() != null) {
                    listTask = formCustomMade.getListTask();
                    taskData = new TaskData();
                    taskData.setTaskName(listTask);
                    taskData.setTaskField(tableField);
                    taskData.setModName(userId);
                    taskData.setModTime(nousedate);
                    num = dictionaryService.updateTask(taskData);
                }
            } else {
                String taskTitle = tableTitle;
                formTask = PrimaryKeyUitl.getDataId(taskTitle + "表单", "task");
                int num = 0;
                taskData = new TaskData();
                taskData.setTaskName(formTask);
                taskData.setTaskTitle(taskTitle + "表单");
                taskData.setTaskType("表单");
                taskData.setTableName(tableName);
                taskData.setTaskField(tableField);
                taskData.setRecName(userId);
                taskData.setRecTime(nousedate);
                taskData.setModName(userId);
                taskData.setModTime(nousedate);
                dictionaryService.insertTask(taskData);

                listTask = PrimaryKeyUitl.getDataId(taskTitle + "列表", "task");
                //判断是否是从编辑节点页面跳转
                if (!"flowtable".equals(formcmName.substring(0, 9))) {
                    taskData = new TaskData();
                    taskData.setTaskName(listTask);
                    taskData.setTaskTitle(taskTitle + "列表");
                    taskData.setTaskType("列表");
                    taskData.setTableName(tableName);
                    taskData.setTaskField(tableField);
                    taskData.setRecName(userId);
                    taskData.setRecTime(nousedate);
                    taskData.setModName(userId);
                    taskData.setModTime(nousedate);
                    dictionaryService.insertTask(taskData);
                }
            }

            FormCustomMade formCustomMade = new FormCustomMade();
            //判断是否是从编辑节点页面跳转
            if ("flowtable".equals(formcmName.substring(0, 9))) {
                String wkflwId = request.getParameter("wkflwId");
                formCustomMade.setFormcmName(formcmName);
                formCustomMade.setEditPage("workflow/" + wkflwId + "/" + formcmName);
                formCustomMade.setFormcmTitle(tableTitle);
                formCustomMade.setFormTask(formTask);
                formCustomMade.setModifyName(userId);
                formCustomMade.setModifyTime(nousedate);
                formCustomMade.setFormType(2);
                formCustomMade.setModule("workflow");
                FormCustomMade formCustomMade1 = formCustomMadeService.selectFormCMByID(formcmName);
                //判断页面是否存在
                if (formCustomMade1 != null) {
                    formCustomMadeService.updateFormCM(formCustomMade);
                } else {
                    formCustomMade.setRecordName(userId);
                    formCustomMade.setRecordTime(nousedate);
                    formCustomMadeService.insertFormCM(formCustomMade);
                }
                NewPageTempUtil np = new NewPageTempUtil();
                np.newPage(request, formCustomMade, "flow",exist);

            } else {
                formCustomMade.setFormcmName(formcmName);
                formCustomMade.setEditPage("userform/" + formcmName);
                formCustomMade.setFormTask(formTask);
                formCustomMade.setListTask(listTask);
                formCustomMade.setModifyName(userId);
                formCustomMade.setModifyTime(nousedate);
                formCustomMadeService.updateFormCM(formCustomMade);
                NewPageTempUtil np = new NewPageTempUtil();
                np.newPage(request, formCustomMade, "form",exist);
            }

            return "1";
        } catch (Exception e) {
            e.printStackTrace();
            return "0";
        }
    }

    @RequestMapping("/gotoformcm")
    public ModelAndView gotoformcm(HttpServletRequest request, String inputval, String option, int page, int limit) {
        FormCustomMade formCustomMade = new FormCustomMade();
        List<String> query = new ArrayList<String>();
        if (inputval != null && inputval != "") {
            if ("formcmName".equals(option)) {
                formCustomMade.setFormcmName(inputval);
            } else if ("formcmTitle".equals(option)) {
                formCustomMade.setFormcmTitle(inputval);
            } else if ("formTask".equals(option)) {
                formCustomMade.setFormTask(inputval);
            } else if ("listTask".equals(option)) {
                formCustomMade.setListTask(inputval);
            } else if ("recordName".equals(option)) {
                formCustomMade.setRecordName(inputval);
            } else if ("modifyName".equals(option)) {
                formCustomMade.setModifyName(inputval);
            }
            query.add(0, option);
            query.add(1, inputval);
        }
        PageUtil pageUtil = new PageUtil();
        pageUtil.setPageSize(limit);
        pageUtil.setCurrentPage(page);
        formCustomMade.setStartRow(pageUtil.getStartRow());
        formCustomMade.setEndRow(pageUtil.getEndRow() - pageUtil.getStartRow());
        List<FormCustomMade> formCustomMadeList = formCustomMadeService.selectAllFormCM(formCustomMade);

        ModelAndView mav = new ModelAndView("manage/form_custom_made");
        mav.addObject("formcnList", formCustomMadeList);
        mav.addObject("formcnQuery", query);
        mav.addObject("type", "list");
        return mav;
    }

    @RequestMapping("/formcmselect")
    @ResponseBody
    public String formcmSelect(HttpServletRequest request, String inputval, String option, int limit, int page) {
        FormCustomMade formCustomMade = new FormCustomMade();
        boolean optiontype = true;
        if (inputval != null && inputval != "") {
            if ("formcmName".equals(option)) {
                formCustomMade.setFormcmName(inputval);
            } else if ("formcmTitle".equals(option)) {
                formCustomMade.setFormcmTitle(inputval);
            } else if ("formTask".equals(option)) {
                formCustomMade.setFormTask(inputval);
            } else if ("listTask".equals(option)) {
                formCustomMade.setListTask(inputval);
            } else if ("recordName".equals(option)) {
                formCustomMade.setRecordName(inputval);
            } else if ("modifyName".equals(option)) {
                formCustomMade.setModifyName(inputval);
            } else if ("formType".equals(option)) {
                formCustomMade.setFormType(Integer.parseInt(inputval));
            } else if ("pageid".equals(option)) {
                formCustomMade.setPageId(inputval);
                optiontype = false;
            }
        }
        int count = 0;
        if (optiontype) {
            count = formCustomMadeService.selectCountFormCM(formCustomMade);
        } else {
            count = formCustomMadeService.selectCountFormPage(formCustomMade);
        }
        PageUtil pageUtil = new PageUtil();
        pageUtil.setPageSize(limit);
        pageUtil.setCurrentPage(page);
        formCustomMade.setStartRow(pageUtil.getStartRow());
        formCustomMade.setEndRow(pageUtil.getEndRow() - pageUtil.getStartRow());
        List<FormCustomMade> formCustomMadeList = null;
        if (optiontype) {
            formCustomMadeList = formCustomMadeService.selectAllFormCM(formCustomMade);
        } else {
            formCustomMadeList = formCustomMadeService.selectAllFormPage(formCustomMade);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 0);
        jsonObject.put("msg", "");
        jsonObject.put("count", count);
        jsonObject.put("data", formCustomMadeList);
        jsonObject.put("success",1);
        return jsonObject.toString();

    }

    @Logined
    @RequestMapping(value = "/formcminsert", method = RequestMethod.POST, produces = {"text/html;charset=UTF-8;", "application/json;"})
    @ResponseBody
    public String formcmInsert(HttpServletRequest request, FormCustomMade formCustomMade) {
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        Date date = new Date();
        Timestamp nousedate = new Timestamp(date.getTime());
        PrimaryKeyUitl pk = new PrimaryKeyUitl();
        String formcmName = pk.getNextId("formpage", "formcmName", formCustomMade.getFormcmTitle());
        formCustomMade.setFormcmName(formcmName);
        if (loginer != null) {
            String userId = loginer.getId();
            formCustomMade.setRecordName(userId);
            formCustomMade.setModifyName(userId);
        }
        try {
            formCustomMade.setRecordTime(nousedate);
            formCustomMade.setModifyTime(nousedate);
            formCustomMadeService.insertFormCM(formCustomMade);
            return "1";
        } catch (Exception e) {
            return "0";
        }
    }

    /**
     * 修改字段
     */
    @RequestMapping(value = "/formcmupdate/{formcmName}")
    @ResponseBody
    public String fieldInfo(Model model, HttpServletRequest request, @PathVariable("formcmName") String formcmName) {
        FormCustomMade formCustomMade = new FormCustomMade();
        formCustomMade.setFormcmName(formcmName);
        formCustomMade = formCustomMadeService.selectFormCM(formCustomMade);
        return JSON.toJSONString(formCustomMade);
    }

    @Logined
    @RequestMapping(value = "/formcmupdate", method = RequestMethod.POST)
    @ResponseBody
    public String formcmUpdate(Model model, HttpServletRequest request, FormCustomMade formCustomMade) {
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        Timestamp nousedate = DateHelper.now();
        if (loginer != null) {
            String userId = loginer.getId();
            formCustomMade.setModifyName(userId);
        }
        try {
            formCustomMade.setModifyTime(nousedate);
            formCustomMadeService.updateFormCM(formCustomMade);
            return "1";
        } catch (Exception e) {
            return "0";
        }
    }

    @Logined
    @RequestMapping(value = "/formcmdelete/{formcmName}", method = RequestMethod.POST)
    @ResponseBody
    public String formcmDelete(Model model, HttpServletRequest request, @PathVariable("formcmName") String formcmName) {
        FormCustomMade formCustomMade = new FormCustomMade();
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        Timestamp nousedate = DateHelper.now();
        if (loginer != null) {
            String userId = loginer.getId();
            formCustomMade.setDeleteName(userId);
        }
        try {
            formCustomMade.setFormcmName(formcmName);
            formCustomMade.setDeleteTime(nousedate);
            formCustomMadeService.deleteFormCM(formCustomMade);
            return "1";
        } catch (Exception e) {
            return "0";
        }
    }

    @Logined
    @RequestMapping(value = "/formcmdelete", method = RequestMethod.POST)
    @ResponseBody
    public String formcmDel(Model model, HttpServletRequest request, String recno) {
        FormCustomMade formCustomMade = new FormCustomMade();
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        Timestamp nousedate = DateHelper.now();
        if (loginer != null) {
            String userId = loginer.getId();
            formCustomMade.setDeleteName(userId);
        }
        try {
            formCustomMade.setDeleteTime(nousedate);

            for (String id : recno.split(",")) {
                formCustomMade.setFormcmName(id);
                formCustomMadeService.deleteFormCM(formCustomMade);
            }
            return "1";
        } catch (Exception e) {
            return "0";
        }
    }

    @Logined
    @RequestMapping(value = "/rebirthPage/{formid}", method = RequestMethod.POST)
    @ResponseBody
    public String rebirthPage(HttpServletRequest request, @PathVariable("formid") String formid) {
        FormCustomMade formCustomMade = formCustomMadeService.selectFormCMByID(formid);
        NewPageTempUtil np = new NewPageTempUtil();
        np.newPage(request, formCustomMade);
        return "1";
    }


    /**
     * 跳转子表定制页面
     */
    @RequestMapping(value = "/gotochildtable", method = RequestMethod.GET)
    public ModelAndView gotoChildTable(HttpServletRequest request, String formid) {
        ModelAndView mav = new ModelAndView("manage/form_child_made");
        mav.addObject("type", "list");
        mav.addObject("formid", formid);
        return mav;
    }

    @RequestMapping("/formcmchildselect")
    @ResponseBody
    public String formcmChildSelect(HttpServletRequest request,String module,int limit,int page) {
        System.out.println("==========module==============>"+module+"<======================");
        FormCustomMade formCustomMade = new FormCustomMade();
        formCustomMade.setModule(module);
        formCustomMade.setFormType(4);
        PageUtil pageUtil = new PageUtil();
        pageUtil.setPageSize(limit);
        pageUtil.setCurrentPage(page);
        formCustomMade.setStartRow(pageUtil.getStartRow());
        formCustomMade.setEndRow(pageUtil.getEndRow() - pageUtil.getStartRow());
        List<FormCustomMade> formCustomMadeList = formCustomMadeService.selectAllFormCM(formCustomMade);;

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 0);
        jsonObject.put("msg", "");
        jsonObject.put("count", formCustomMadeList.size());
        jsonObject.put("data", formCustomMadeList);
        jsonObject.put("success",1);
        return jsonObject.toString();

    }
    /**
     * 跳转添加子表页面
     */
    @RequestMapping(value = "/formcm/childadd", method = RequestMethod.GET)
    public ModelAndView gotoFormcmChildAdd(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("manage/form_child_made");
        String recno = request.getParameter("recno");
        String module = request.getParameter("module");
        if (recno != null && !recno.equals("")) {
            FormCustomMade formCustomMade = formCustomMadeService.selectFormCMByID(recno);
            mav.addObject("formcm", formCustomMade);
            mav.addObject("type", "modi");
        } else {
            mav.addObject("type", "add");
        }
        mav.addObject("module", module);
        return mav;
    }

    @Logined
    @RequestMapping(value = "/childsave/{type}", method = RequestMethod.POST, produces = {"text/html;charset=UTF-8;", "application/json;"})
    @ResponseBody
    public String childSave(HttpServletRequest request, @PathVariable("type") String type, FormCustomMade formCustomMade) {
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        String userId = loginer.getId() == null ? "admin" : loginer.getId();
        if (type != null && type.equals("add")) {
            PrimaryKeyUitl pk = new PrimaryKeyUitl();
            String formcmName = pk.getNextId("formpage", "formcmName", formCustomMade.getFormcmTitle());
            formCustomMade.setFormcmName(formcmName);
            formCustomMade.setRecordName(userId);
            formCustomMade.setRecordTime(DateHelper.now());
            formCustomMade.setFormType(4);
        }
        formCustomMade.setModifyName(userId);
        formCustomMade.setModifyTime(DateHelper.now());
        try {
            if (type != null && type.equals("add")) {
                formCustomMadeService.insertFormCM(formCustomMade);
            } else {
                formCustomMadeService.updateFormCM(formCustomMade);
            }
            return "1";
        } catch (Exception e) {
            return "0";
        }
    }


    @Logined
    @RequestMapping(value = "/formchildedit", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView gotoFormChildEdit(HttpServletRequest request, String formId, String childId) {
        FormCustomMade formCustomMade = new FormCustomMade();
        ModelAndView mav = new ModelAndView("manage/form_edit_child");
        formCustomMade.setFormType(4);
        formCustomMade.setFormcmName(childId);
        formCustomMade = formCustomMadeService.selectFormCM(formCustomMade);
        //判断页面是否存在对应表单,获取对应字段
        if (formCustomMade.getFormTask() != null && !formCustomMade.getFormTask().equals("")) {
            TaskData taskData = dictionaryService.selectTaskName(formCustomMade.getFormTask());
            //判断表单是否存在
            if (taskData != null) {
                //判断表单对应的table表是否存在
                if (taskData.getTableName() != null && !taskData.getTableName().equals("")) {
                    TableData tableData = dictionaryService.selectTableName(taskData.getTableName());
                    mav.addObject("tableId", taskData.getTableName());
                    String[] fieldnames = taskData.getTaskField().split(";");
                    List tablefields = new ArrayList();
                    for (int i = 0; i < fieldnames.length; i++) {
                        StringToHtmlUtil htmlUtil = new StringToHtmlUtil();
                        tablefields.add(htmlUtil.getFieldValue(taskData.getTableName(),fieldnames[i],null));
                    }
                    mav.addObject("tablefields", tablefields);
                }
            }
        }
        mav.addObject("form", formCustomMade);
        mav.addObject("formId", formId);
        return mav;
    }

    /**
     * 子表创建方法
     * fieldData、tableData、taskData
     */
    @RequestMapping(value = "/child_edit_save/{formcmName}", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String saveChildEdit(HttpServletRequest request, @PathVariable String formcmName) {
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        Timestamp nousedate = DateHelper.now();
        String userId = loginer.getId() == null ? "admin" : loginer.getId();
        try {
            String postData = request.getParameter("postData");
            JSONObject json = new JSONObject(postData);
            boolean exist;
            String tableName = json.getString("id");
            String tableTitle = json.getString("name");
            if ("tableId".equals(tableName)) {
                tableName = PrimaryKeyUitl.getDataId(tableTitle, "table");
                exist = false;
            } else {
                exist = true;
            }
            String tableField = "";
            List<FieldData> listField = new ArrayList<FieldData>();
            JSONArray jsonArray = json.getJSONArray("data");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject subObject = jsonArray.getJSONObject(i);
                String fieldName = subObject.getString("fieldName");
                String fieldTitle = subObject.getString("fieldTitle");
                if (fieldName.indexOf("_field") >= 0) {
                    fieldName = PrimaryKeyUitl.getDataId(fieldTitle, "field");
                }
                tableField += fieldName + ";";
                FieldData fieldData = setFieldData(subObject, tableName, fieldName, fieldTitle, userId, exist);
                listField.add(fieldData);
            }
            TableData tableData = new TableData();
            tableData.setTableName(tableName);
            tableData.setTableTitle(tableTitle);
            tableData.setModName(userId);
            tableData.setModTime(nousedate);
            if (exist) {
                List<String> fields = new ArrayList<>();
                List<String> tfields = new ArrayList<>();
                String[] str1 = dictionaryService.selectTableName(tableName).getTableField().split(";");
                for (int i = 0; i < str1.length; i++) {
                    fields.add(str1[i]);
                }
                String[] str2 = tableField.split(";");
                for (int j = 0; j < str2.length; j++) {
                    tfields.add(str2[j]);
                }
                fields.removeAll(tfields);
                tfields.addAll(fields);
                tableData = new TableData();
                tableData.setTableField(StringUtils.join(tfields.toArray(), ";") + ";");
                dictionaryService.updateTable(tableData);
            } else {
                tableData.setTableField(tableField);
                tableData.setRecName(userId);
                tableData.setRecTime(nousedate);
                dictionaryService.insertTable(tableData);
                String tableSql = MySqlUtil.getCreateSql(tableName,tableTitle, listField);
                tableService.creatTable(tableSql);
            }
            String formTask = "", listTask = "";
            FormCustomMade formCustomMade = formCustomMadeService.selectFormCMByID(formcmName);
            String formTitle = formCustomMade.getFormcmTitle();
            String taskTitle = tableTitle;
            TaskData taskData = new TaskData();
            taskData.setTaskField(tableField);
            taskData.setModName(userId);
            taskData.setModTime(nousedate);
            if (!exist) {
                formTask = PrimaryKeyUitl.getDataId(taskTitle + "表单", "task");
                taskData.setTaskTitle(taskTitle + "表单");
                taskData.setTaskType("表单");
                taskData.setTableName(tableName);
                taskData.setRecName(userId);
                taskData.setRecTime(nousedate);
                taskData.setTaskName(formTask);
                dictionaryService.insertTask(taskData);
            } else {
                formTask = formCustomMade.getFormTask();
                taskData.setTaskName(formTask);
                dictionaryService.updateTask(taskData);
            }

            taskData = new TaskData();
            taskData.setModName(userId);
            taskData.setModTime(nousedate);
            taskData.setTaskField(tableField);
            taskData.setTableName(tableName);
            if (!exist) {
                listTask = PrimaryKeyUitl.getDataId(taskTitle + "列表", "task");
                taskData.setTaskName(listTask);
                taskData.setTaskTitle(taskTitle + "列表");
                taskData.setTaskType("列表");
                taskData.setRecName(userId);
                taskData.setRecTime(nousedate);
                dictionaryService.insertTask(taskData);
            } else {
                listTask = formCustomMade.getListTask();
                taskData.setTaskName(listTask);
                dictionaryService.updateTask(taskData);
            }

            formCustomMade.setFormcmName(formcmName);
            formCustomMade.setEditPage("module/childtable");
            formCustomMade.setFormTask(formTask);
            formCustomMade.setListTask(listTask);
            formCustomMade.setModifyName(userId);
            formCustomMade.setModifyTime(nousedate);
            formCustomMadeService.updateFormCM(formCustomMade);

            String formId = request.getParameter("formId");
            if(formId!=null && !formId.equals("")) {
                mainFormUpdate(userId, formId, formcmName, formTitle);
            }
            return "1";
        } catch (Exception e) {
            e.printStackTrace();
            return "0";
        }
    }


    public FieldData setFieldData(JSONObject subObject, String tableName, String fieldName, String fieldTitle, String userId, boolean exist) {
        int fieldNum = 200;
        if (!subObject.isNull("fieldNum")) {
            fieldNum = subObject.getInt("fieldNum");
        }
        int fieldDigit = 0;
        if (!subObject.isNull("fieldDigit")) {
            fieldDigit = subObject.getInt("fieldDigit");
        }
        String optionVal = "";
        if (!subObject.isNull("optionVal")) {
            optionVal = subObject.getString("optionVal");
        }
        String defaultVal = "";
        if (!subObject.isNull("defaultVal")) {
            defaultVal = subObject.getString("defaultVal");
        }
        String special = "";
        if (!subObject.isNull("special")) {
            special = subObject.getString("special");
        }
        int required = 0;
        if (!subObject.isNull("required")) {
            required = subObject.getInt("required");
        }
        FieldData fieldData = new FieldData();
        fieldData.setFieldName(fieldName);

        fieldData.setFieldTitle(fieldTitle);
        String fieldType = "text";
        if (!subObject.isNull("fieldType")) {
            fieldType = subObject.getString("fieldType");
        }
        fieldData.setFieldType(fieldType);
        fieldData.setFieldNum(fieldNum);
        fieldData.setFieldDigit(fieldDigit);
        fieldData.setTableName(tableName);
        fieldData.setOptionVal(optionVal);
        fieldData.setDefaultVal(defaultVal);
        fieldData.setSpecial(special);
        fieldData.setRequired(required);
        int num = dictionaryService.selectFieldCount(fieldData);
        if (num > 0) {
            fieldData.setModName(userId);
            fieldData.setModTime(DateHelper.now());
            dictionaryService.updateField(fieldData);
        } else {
            fieldData.setRecName(userId);
            fieldData.setRecTime(DateHelper.now());
            fieldData.setModName(userId);
            fieldData.setModTime(DateHelper.now());
            dictionaryService.insertField(fieldData);
            if (exist) {
                String fieldSql = MySqlUtil.getAlterSql(tableName,fieldData);
                tableService.creatTable(fieldSql);
            }
        }
        return fieldData;
    }

    public void mainFormUpdate(String userId, String formId, String formcmName, String formTitle) {
        FormCustomMade mainform = formCustomMadeService.selectFormCMByID(formId);
        String mainFormTask = mainform.getFormTask();
        /**
         * 更新任务定义字段（表单任务）
         * */
        TaskData mainTaskData = dictionaryService.selectTaskName(mainFormTask);
        String mainTableName = mainTaskData.getTableName();
        String mainTaskField = mainTaskData.getTaskField();
        if (!mainTaskField.contains(formcmName + ";")) {
            mainTaskData.setTaskField(mainTaskField + formcmName + ";");
            dictionaryService.updateTask(mainTaskData);
        }
        /**
         * 更新表定义字段
         * */
        TableData mainTableData = dictionaryService.selectTableName(mainTableName);
        String mainTableField = mainTableData.getTableField();
        if (!mainTableField.contains(formcmName + ";")) {
            mainTableData.setTableField(mainTableField + formcmName + ";");
            dictionaryService.updateTask(mainTaskData);
        }
        FieldData mainFieldData = dictionaryService.selectFieldName(formcmName);
        String special = "[child-" + formcmName + "]";
        if (mainFieldData != null) {
            mainFieldData.setSpecial(special);
            dictionaryService.updateField(mainFieldData);
        } else {
            /**
             * 新建子表字段
             * */
            mainFieldData = new FieldData();
            mainFieldData.setFieldName(formcmName);
            mainFieldData.setFieldTitle(formTitle);
            mainFieldData.setFieldType("child");
            mainFieldData.setFieldNum(50);
            mainFieldData.setFieldDigit(0);
            mainFieldData.setSpecial(special);
            mainFieldData.setRequired(0);
            mainFieldData.setRecName(userId);
            mainFieldData.setRecTime(DateHelper.now());
            mainFieldData.setModName(userId);
            mainFieldData.setModTime(DateHelper.now());
            dictionaryService.insertField(mainFieldData);
            String fieldSql = MySqlUtil.getAlterSql(mainTableName,mainFieldData);
            tableService.creatTable(fieldSql);
        }

    }
}
