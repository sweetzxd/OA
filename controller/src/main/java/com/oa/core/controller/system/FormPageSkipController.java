package com.oa.core.controller.system;

import com.oa.core.bean.dd.FieldData;
import com.oa.core.bean.module.Department;
import com.oa.core.bean.system.FormCustomMade;
import com.oa.core.bean.system.MyUrlRegist;
import com.oa.core.bean.user.UserManager;
import com.oa.core.bean.util.PageUtil;
import com.oa.core.helper.StringHelper;
import com.oa.core.interceptor.Logined;
import com.oa.core.listener.InitDataListener;
import com.oa.core.service.module.DepartmentService;
import com.oa.core.service.system.AccessRightsService;
import com.oa.core.service.system.FormCustomMadeService;
import com.oa.core.service.system.MyUrlRegistService;
import com.oa.core.service.user.UserManagerService;
import com.oa.core.service.util.TableService;
import com.oa.core.system.postposition.PostPosition;
import com.oa.core.util.*;
import com.oa.core.bean.Loginer;
import com.oa.core.bean.dd.TableData;
import com.oa.core.bean.dd.TaskData;
import com.oa.core.helper.DateHelper;
import com.oa.core.service.dd.DictionaryService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/userpage")
public class FormPageSkipController {
	
    @Autowired
    MyUrlRegistService myUrlRegistService;
    @Autowired
    FormCustomMadeService formCustomMadeService;
    @Autowired
    DictionaryService dictionaryService;
    @Autowired
    TableService tableService;
    @Autowired
    UserManagerService userManagerService;
    @Autowired
    DepartmentService departmentService;
    @Autowired
    AccessRightsService accessRightsService;

    @Logined
    @RequestMapping(value = "formviewpage/{formid}", method = RequestMethod.GET)
    public ModelAndView gotoViewpageF(HttpServletRequest request, @PathVariable String formid) {
        String pageid = null;
        MyUrlRegist myUrlRegist = new MyUrlRegist();
        myUrlRegist.setFormId(formid);
        List<MyUrlRegist> yur = myUrlRegistService.selectTerms(myUrlRegist);
        if (yur.size() > 0) {
            pageid = yur.get(0).getPageId();
        }
        if (pageid != null && pageid != "") {
            return gotoViewpage(request, pageid, formid);
        } else {
            ModelAndView mav = new ModelAndView("manage/form_custom_made");
            mav.addObject("type", "list");
            mav.addObject("error", "请先设置菜单");
            return mav;
        }
    }

    public ModelAndView gotoViewpage(HttpServletRequest request, @PathVariable String pageid, String formid) {
        return gotoViewpage(request, pageid, formid, null, null, null, false);
    }

    public ModelAndView gotoViewpage(HttpServletRequest request, @PathVariable String pageid, String formid, boolean listsave) {
        return gotoViewpage(request, pageid, formid, null, null, null, listsave);
    }

    @Logined
    @RequestMapping(value = "viewpage/{pageid}", method = RequestMethod.GET)
    public ModelAndView gotoViewpage(HttpServletRequest request, @PathVariable String pageid, String formid, String linkId, String field, String value, boolean listsave) {
        try {
            Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
            String userId = loginer.getId();
            if (formid == null || !formid.equals("")) {
                MyUrlRegist myUrlRegist = myUrlRegistService.selectById(pageid);
                if (myUrlRegist != null) {
                    formid = myUrlRegist.getFormId();
                }
            }
            if (formid != null) {
                FormCustomMade formCustomMade = formCustomMadeService.selectFormCMByID(formid);
                String listTask = formCustomMade.getListTask();
                String title = formCustomMade.getFormcmTitle();
                String page = formCustomMade.getEditPage();
                int formType = formCustomMade.getFormType();
                ModelAndView mav = new ModelAndView(page);
                if (listTask != null && !listTask.equals("")) {
                    TaskData taskData = dictionaryService.selectTaskName(listTask);
                    String tableid = "";
                    if (taskData != null) {
                        tableid = taskData.getTableName();
                    }
                    String num;
                    if (pageid != null && pageid.equals("childtable")) {
                        num = "000000";
                    } else {
                        num = AccessUtil.getDataNum(userId, pageid);
                    }
                    mav.addObject("num", num);
                    mav.addObject("tableid", tableid);
                    if (formType != 3) {
                        StringToHtmlUtil sth = new StringToHtmlUtil();
                        String listTitle = sth.getListTitle(formid, listsave);
                        mav.addObject("listTitle", listTitle);
                    }
                }
                mav.addObject("title", title);
                mav.addObject("formid", formid);
                mav.addObject("pageid", pageid);
                mav.addObject("user", userId);
                mav.addObject("type", "list");
                mav.addObject("linkId", linkId);
                mav.addObject("field", field);
                mav.addObject("value", value);
                mav.addObject("listsave", listsave);
                return mav;
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    @Logined
    @RequestMapping(value = "/gettablevalue")
    @ResponseBody
    public String getTableValue(HttpServletRequest request, String pageid, String formid, String linkId, String field, String item, String value, int limit, int page, String glField, String glValue) {
        List<String> where = new ArrayList<>();
        if (glField != null && !glField.equals("")) {
            where.add(MySqlUtil.getItem(glField, "等于", glValue));
        }
        if (field != null && !field.equals("") && value != null && !value.equals("")) {
            where.add(MySqlUtil.getItem(field, item, value));
        }
        if (linkId != null && !linkId.equals("")) {
            where.add(MySqlUtil.getItem("linkRecorderNO", "等于", linkId));
        }

        where = AccessUtil.special(request, where, pageid);

        FormCustomMade formCustomMade = formCustomMadeService.selectFormCMByID(formid);
        String listTask = formCustomMade.getListTask();
        TaskData taskData = dictionaryService.selectTaskName(listTask);
        String tableId = taskData.getTableName();
        Vector<String> fields = StringHelper.string2Vector(taskData.getTaskField(), ";");
        String countSql = MySqlUtil.getCountSql(tableId, where);
        int count = tableService.selectSqlCount(countSql);
        PageUtil pu = new PageUtil();
        pu.setPageSize(limit);
        pu.setCurrentPage(page);
        int StartRow = pu.getStartRow();
        int EndRow = pu.getEndRow() - pu.getStartRow();
        String getsql = MySqlUtil.getNameSql(fields, tableId, where, "recordTime desc", StartRow, EndRow);
        List<Map<String, Object>> sqlval = tableService.selectSqlMapList(getsql);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 0);
        jsonObject.put("msg", "");
        jsonObject.put("count", count);
        jsonObject.put("data", sqlval);
        jsonObject.put("success", 1);
        jsonObject.put("is", true);
        jsonObject.put("tip", "操作成功");
        return jsonObject.toString();
    }

    @Logined
    @RequestMapping(value = "/pageform/{recorderNO}", method = RequestMethod.GET)
    public ModelAndView gotoPageForm(HttpServletRequest request, @PathVariable String recorderNO, String state) {
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        String userId = loginer.getId();

        String pageid = request.getParameter("pageid");
        String formid = request.getParameter("formid");
        String type = request.getParameter("type");
        String linkId = request.getParameter("linkId");

        //跳转修改页面
        String conmenu = myUrlRegistService.selectContextMenu(pageid);
        if (state == null && conmenu != null && !conmenu.equals("") && !conmenu.equals("[]") && type.equals("info")) {
            ModelAndView mav = new ModelAndView("manage/relevancemenu");
            MyUrlRegist myUrlRegist1 = new MyUrlRegist();
            myUrlRegist1.setParentId(pageid);
            myUrlRegist1.setFormType(5);
            List<MyUrlRegist> list = myUrlRegistService.selectTerms(myUrlRegist1);
            list.get(0).setContextmenu(list.get(0).getContextmenu() + recorderNO + ".do?type=info&state=iframe&pageid=" + pageid);
            for (int i = 1; i < list.size(); i++) {
                if (list.get(i).getContextmenu().endsWith("=")) {
                    String[] fields = list.get(i).getKeepFeild().split("_");
                    String sql = "select " + fields[1] + " from " + fields[0] + " where recorderNO='" + recorderNO + "' and curStatus=2";
                    String value = tableService.selectSql(sql).get(0);
                    list.get(i).setContextmenu(list.get(i).getContextmenu() + value);
                }
            }
            mav.addObject("list", list);
            mav.addObject("src", list.get(0).getContextmenu());
            return mav;
        }
        if (formid == null) {
            MyUrlRegist myUrlRegist = myUrlRegistService.selectById(pageid);
            formid = myUrlRegist.getFormId();
        }
        FormCustomMade formCustomMade = formCustomMadeService.selectFormCMByID(formid);
        String formTask = formCustomMade.getFormTask();
        String editPage = formCustomMade.getEditPage();
        TaskData taskData = dictionaryService.selectTaskName(formTask);
        String tableId = taskData.getTableName();
        TableData tableData = dictionaryService.selectTableName(tableId);
        StringToHtmlUtil sth = new StringToHtmlUtil();
        List<String> form = sth.getFormHtml(request, pageid, formid, type, taskData, recorderNO);
        //String contextmenu = MenuUtil.contextMenu(pageid, formid);
        ModelAndView mav = new ModelAndView(editPage);
        mav.addObject("title", tableData.getTableTitle());
        mav.addObject("user", userId);
        mav.addObject("recno", recorderNO);
        mav.addObject("formtask", formTask);
        mav.addObject("pageid", pageid);
        mav.addObject("formid", formid);
        mav.addObject("tableId", tableId);
        mav.addObject("linkId", linkId);
        mav.addObject("form", form);
        //mav.addObject("contextmenu", contextmenu);
        mav.addObject("type", type);
        return mav;
    }

    @Logined
    @RequestMapping(value = "/pagealldel", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String gotoPageAllDel(HttpServletRequest request) {
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        String userId = loginer.getId();
        String tableid = request.getParameter("tableid");
        String recno = request.getParameter("recno");
        recno = recno.replaceAll(";", "','");
        recno = recno.substring(0, recno.length() - 3);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("deleteName", userId);
        map.put("deleteTime", DateHelper.now());
        String sql = MySqlUtil.getDeleteSql(tableid, map, recno);
        try {
            if (tableid != null && tableid != "") {
                tableService.updateSqlMap(sql);
            }
            return "1";
        } catch (Exception e) {
            return "0";
        }
    }

    @Logined
    @RequestMapping(value = "/listsave", method = RequestMethod.POST)
    @ResponseBody
    public String formModiSave(HttpServletRequest request) {
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        try {
            Map<String, Object> map = new HashMap<>();
            TaskData taskData = dictionaryService.listTaskByFormId(request.getParameter("formid"));
            String fields = taskData.getTaskField();
            String table = taskData.getTableName();
            if (table != null && table != "") {
                String recorderNO = request.getParameter(table + "_recorderNO");
                map.put("recorderNO", recorderNO);
                if (fields.length() > 0) {
                    Vector<String> field = StringHelper.string2Vector(fields, ";");
                    for (String fieldName : field) {
                        String name = table + "_" + fieldName;
                        String value = request.getParameter(name);
                        if (value != null) {
                            //value = new String(value.getBytes("iso-8859-1"), "UTF-8");
                            map.put(fieldName, value);
                        }
                    }
                }
                map.put("modifyName", loginer.getId());
                map.put("modifyTime", DateHelper.now());
                String sql = MySqlUtil.getUpdateSql(table, recorderNO, map, null);
                tableService.updateSqlMap(sql);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return "";
        }
    }

    @Logined
    @RequestMapping(value = "/formsave/{pageid}", method = RequestMethod.POST)
    public ModelAndView formModiSave(HttpServletRequest request, @PathVariable("pageid") String pageid) {
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        String userId = loginer.getId();
        String type = request.getParameter("type");
        String formid = request.getParameter("formid");
        String linkId = request.getParameter("linkId");
        String url = "redirect:/userpage/viewpage/" + pageid + ".do?formid=" + formid;
        if (linkId != null && !linkId.equals("")) {
            url += "&linkId=" + linkId;
        }
        ModelAndView model = new ModelAndView(url);
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            FormCustomMade formCustomMade = formCustomMadeService.selectFormCMByID(formid);
            String formtask = formCustomMade.getFormTask();
            TaskData taskData = dictionaryService.selectTaskName(formtask);
            String fields = taskData.getTaskField();
            String table = taskData.getTableName();
            if (table != null && table != "") {
                String recorderNO = null;
                if (type != null && type.equals("modi")) {
                    recorderNO = request.getParameter(table + "_recorderNO_Value");
                    if (recorderNO == null || recorderNO == "") {
                        model.addObject("error", "主键为空");
                        return model;
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
                                value = new String(value.getBytes("iso-8859-1"), "UTF-8");
                                map.put(fieldName, value);
                            }
                        }
                    }
                } else {
                    model.addObject("error", "字段为空");
                    return model;
                }
                map.put("modifyName", userId);
                map.put("modifyTime", DateHelper.now());
                String sql = null;


                if (false) {
                    PostPosition post = new PostPosition("form");
                    post.setTableName(table);
                    post.setRecorderNO(recorderNO);
                    post.setTableMap(map);
                    boolean postposition = post.getPostPosition(table + "PostPosition");
                }
                if (type != null && type.equals("modi")) {
                    sql = MySqlUtil.getUpdateSql(table, recorderNO, map, null);
                    tableService.updateSqlMap(sql);
                } else if (child) {
                    sql = MySqlUtil.getInsertSql(table, map);
                    tableService.insertSqlMap(sql);
                }
                model.addObject("error", "成功");
                return model;
            } else {
                model.addObject("error", "失败");
                return model;
            }
        } catch (Exception e) {
            e.printStackTrace();
            model.addObject("error", "失败");
            return model;
        }
    }

    @Logined
    @RequestMapping(value = "/pagedelete/{recno}", method = RequestMethod.POST)
    public ModelAndView gotoPageDelete(HttpServletRequest request, @PathVariable("recno") String recno, @RequestParam("pageid") String pageid, @RequestParam("formid") String formid) {
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        String userId = loginer.getId();
        FormCustomMade formCustomMade = formCustomMadeService.selectFormCMByID(formid);
        String listTask = formCustomMade.getListTask();
        String formTask = formCustomMade.getFormTask();
        String title = formCustomMade.getFormcmTitle();
        String page = formCustomMade.getEditPage();
        TaskData taskData = dictionaryService.selectTaskName(formTask);
        String tableid = taskData.getTableName();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("deleteName", userId);
        map.put("deleteTime", DateHelper.now());
        String sql = MySqlUtil.getDeleteSql(tableid, map, recno);
        try {
            if (tableid != null && tableid != "") {
                tableService.implementSql(sql);
            }
        } catch (Exception e) {
        }
        ModelAndView model = new ModelAndView("redirect:/userpage/viewpage/" + pageid + ".do?formid=" + formid);
        model.addObject("title", title);
        model.addObject("formid", formid);
        model.addObject("tableid", tableid);
        model.addObject("user", userId);
        model.addObject("type", "list");
        return model;
    }

    /**
     * 人员选择处理方法
     */
    @RequestMapping(value = "/gotouserselect", method = RequestMethod.GET)
    public ModelAndView gotoUserSelect(HttpServletRequest request, String type, String inputId) {
        String thead = "";
        if (type != null && type.equals("user")) {
            thead += "<th lay-data=\"{type:'radio',field:'userName', width:80}\">选择</th>";
        } else if (type != null && type.equals("users")) {
            thead += "<th lay-data=\"{type:'checkbox',field:'userName',width:80}\">选择</th>";
        }
        thead += "<th lay-data=\"{field:'staffName', sort: true}\">姓名</th>";
        ModelAndView mav = new ModelAndView("manage/tableselect");
        mav.addObject("thead", thead);
        mav.addObject("url", "/user/selectuserbydept.do");
        mav.addObject("id", "userName");
        mav.addObject("name", "staffName");
        mav.addObject("inputId", inputId);
        mav.addObject("type", type);
        return mav;
    }

    /**
     * 部门选择处理方法
     */
    @RequestMapping(value = "/gotodeptselect", method = RequestMethod.GET)
    public ModelAndView gotoDeptSelect(HttpServletRequest request, String type, String inputId) {
        String thead = "";
        if (type != null && type.equals("dept")) {
            thead += "<th lay-data=\"{type:'radio',field:'deptId', width:80}\">选择</th>";
        } else if (type != null && type.equals("depts")) {
            thead += "<th lay-data=\"{type:'checkbox',field:'deptId',width:80}\">选择</th>";
        }
        thead += "<th lay-data=\"{field:'deptName'}\">部门名称</th>";
        ModelAndView mav = new ModelAndView("manage/tableselect");
        mav.addObject("thead", thead);
        mav.addObject("url", "/department/selectdeptinput.do");
        mav.addObject("id", "deptId");
        mav.addObject("name", "deptName");
        mav.addObject("inputId", inputId);
        mav.addObject("type", type);
        return mav;
    }


    /**
     * 表单选择处理方法
     */
    @RequestMapping(value = "/gotoformselect", method = RequestMethod.GET)
    public ModelAndView gotoFormSelect(HttpServletRequest request, String type, String inputId, String formTask) {
        TaskData taskData = dictionaryService.selectTaskName(formTask);
        String tableId = taskData.getTableName();
        String fields = taskData.getTaskField();
        Vector<String> field = StringHelper.string2Vector(fields, ";");
        Hashtable<String, String> ht = InitDataListener.getMapData("fieldData");
        String thead = "";
        if (type != null && type.equals("form")) {
            thead += "<th lay-data=\"{type:'radio',field:'recorderNO', width:80}\">选择</th>";
        } else if (type != null && type.equals("forms")) {
            thead += "<th lay-data=\"{type:'checkbox',field:'recorderNO',width:80}\">选择</th>";
        }
        for (int i = 0; i < field.size(); i++) {
            String f = field.get(i);
            String fn = ht.get(f);
            thead += "<th lay-data=\"{field:'" + f + "'}\">" + fn + "</th>";
        }
        ModelAndView mav = new ModelAndView("manage/tableselect");
        mav.addObject("thead", thead);
        mav.addObject("url", "/userpage/selectforminput.do?formTask=" + formTask);
        mav.addObject("id", "recorderNO");
        mav.addObject("name", field.get(0));
        mav.addObject("inputId", inputId);
        mav.addObject("type", type);
        return mav;
    }

    @RequestMapping("/selectforminput")
    @ResponseBody
    public String getDeptList(HttpServletRequest request, String formTask, int limit, int page) {
        TaskData taskData = dictionaryService.selectTaskName(formTask);
        String tableId = taskData.getTableName();
        String fields = taskData.getTaskField();
        Vector<String> field = StringHelper.string2Vector(fields, ";");
        String countSql = MySqlUtil.getCountSql(tableId, null);
        int count = tableService.selectSqlCount(countSql);
        PageUtil pu = new PageUtil();
        pu.setPageSize(limit);
        pu.setCurrentPage(page);
        int StartRow = pu.getStartRow();
        int EndRow = pu.getEndRow() - pu.getStartRow();
        String getsql = MySqlUtil.getFieldSql(field, tableId, null, StartRow, EndRow);
        List<Map<String, Object>> value = tableService.selectSqlMapList(getsql);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 0);
        jsonObject.put("msg", "");
        jsonObject.put("count", count);
        jsonObject.put("data", value);
        jsonObject.put("success", 1);
        jsonObject.put("is", true);
        jsonObject.put("tip", "操作成功");
        return jsonObject.toString();
    }

    //导入Excel
    @RequestMapping(value = "/importTable", method = RequestMethod.POST, produces = {"text/html;charset=UTF-8;", "application/json;"})
    @ResponseBody
    public String importTable(HttpServletRequest request, int type, String filePath) {
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        String userId = loginer.getId();
        LinkedHashMap<String, Object> excelMap = new LinkedHashMap<>();
        try {
            excelMap = ExcelUtil.importTable(filePath);
            //主表主键关联HashMap
            HashMap<String, String> zbMap = new HashMap<>();
            int x = 1;
            for (String key : excelMap.keySet()) {
                List<Map<String, Object>> list = (List<Map<String, Object>>) excelMap.get(key);
                String tableTitle = String.valueOf(list.get(0).get("tableTitle"));
                String time = String.valueOf(DateHelper.now());
                if (type == 1) {
                    List<Map<String, Object>> list1 = new ArrayList<>();
                    for (int i = 1; i < list.size(); i++) {
                        Map<String, Object> map = new HashMap<>();
                        map = list.get(i);
                        for (String key1 : map.keySet()) {
                            FieldData fieldData = dictionaryService.selectFieldName(key1);
                            if (fieldData.getFieldType().equals("user")) {
                                List<UserManager> userList = userManagerService.getUserByName(String.valueOf(map.get(key1)).trim());
                                UserManager userManager = userList.get(0);
                                map.put(key1, userManager.getUserName());
                            } else if (fieldData.getFieldType().equals("dept")) {
                                Department department = departmentService.getDepartmentByDeptName(String.valueOf(map.get(key1)).trim());
                                map.put(key1, department.getDeptId());
                            }
                        }
                        PrimaryKeyUitl pk = new PrimaryKeyUitl();
                        String id = pk.getNextId(key, "recorderNO");
                        if (x == 1) {
                            zbMap.put(String.valueOf(map.get("recorderNO")), id);
                        } else {
                            map.put("linkRecorderNO", zbMap.get(map.get("linkRecorderNO")));
                        }
                        map.put("recorderNO", id);
                        map.put("curStatus", 2);
                        map.put("recordName", userId);
                        map.put("recordTime", time);
                        map.put("modifyName", userId);
                        map.put("modifyTime", time);
                        list1.add(map);
                    }
                    String sql = MySqlUtil.getInsertAllSql(key, list1);
                    tableService.insertSqlMap(sql);
                } else if (type == 2) {
                    List<Map<String, Object>> list1 = new ArrayList<>();
                    for (int i = 1; i < list.size(); i++) {
                        Map<String, Object> map = new HashMap<>();
                        map = list.get(i);
                        for (String key1 : map.keySet()) {
                            FieldData fieldData = dictionaryService.selectFieldName(key1);
                            if (fieldData.getFieldType().equals("user")) {
                                List<UserManager> userList = userManagerService.getUserByName(String.valueOf(map.get(key1)).trim());
                                UserManager userManager = userList.get(0);
                                map.put(key1, userManager.getUserName());
                            } else if (fieldData.getFieldType().equals("dept")) {
                                Department department = departmentService.getDepartmentByDeptName(String.valueOf(map.get(key1)).trim());
                                map.put(key1, department.getDeptId());
                            }
                        }
                        if (x > 1) {
                            if (zbMap.get(map.get("linkRecorderNO")) != null) {
                                PrimaryKeyUitl pk = new PrimaryKeyUitl();
                                String id = pk.getNextId(key, "recorderNO");
                                map.put("linkRecorderNO", zbMap.get(map.get("linkRecorderNO")));
                                map.put("recorderNO", id);
                                map.put("curStatus", 2);
                                map.put("recordName", userId);
                                map.put("recordTime", time);
                                map.put("modifyName", userId);
                                map.put("modifyTime", time);
                                list1.add(map);
                            } else {
                                //如果主键存在更新数据，不存在则插入新数据
                                List<String> wheres = new ArrayList<>();
                                String where = "recorderNO='" + map.get("recorderNO") + "'";
                                wheres.add(where);
                                String sql = MySqlUtil.getCountSql(key, wheres);
                                int count = tableService.selectSqlCount(sql);
                                if (count == 1) {
                                    String updateSql = MySqlUtil.getUpdateSql(key, map, wheres);
                                    tableService.updateSqlMap(updateSql);
                                } else if (count == 0) {
                                    PrimaryKeyUitl pk = new PrimaryKeyUitl();
                                    String id = pk.getNextId(key, "recorderNO");
                                    map.put("recorderNO", id);
                                    map.put("curStatus", 2);
                                    map.put("recordName", userId);
                                    map.put("recordTime", time);
                                    map.put("modifyName", userId);
                                    map.put("modifyTime", time);
                                    list1.add(map);
                                }
                            }
                        } else {
                            //如果主键存在更新数据，不存在则插入新数据
                            List<String> wheres = new ArrayList<>();
                            String where = "recorderNO='" + map.get("recorderNO") + "'";
                            wheres.add(where);
                            String sql = MySqlUtil.getCountSql(key, wheres);
                            int count = tableService.selectSqlCount(sql);
                            if (count == 1) {
                                String updateSql = MySqlUtil.getUpdateSql(key, map, wheres);
                                tableService.updateSqlMap(updateSql);
                            } else if (count == 0) {
                                PrimaryKeyUitl pk = new PrimaryKeyUitl();
                                String id = pk.getNextId(key, "recorderNO");
                                zbMap.put(String.valueOf(map.get("recorderNO")), id);
                                map.put("recorderNO", id);
                                map.put("curStatus", 2);
                                map.put("recordName", userId);
                                map.put("recordTime", time);
                                map.put("modifyName", userId);
                                map.put("modifyTime", time);
                                list1.add(map);
                            }
                        }
                    }
                    if (list1.size() > 0) {
                        String sql = MySqlUtil.getInsertAllSql(key, list1);
                        tableService.insertSqlMap(sql);
                    }
                }
                x++;
            }
        } catch (Exception e) {
            LogUtil.sysLog("Exception:" + e);
        }
        return "1";
    }

    //导出Excel
    @RequestMapping(value = "/exportTable", method = RequestMethod.GET, produces = {"text/html;charset=UTF-8;", "application/json;"})
    public String exportTable(HttpServletRequest request, HttpServletResponse response, String formId, String fields, String childTable, String field, String term, String inputval) {
        FormCustomMade formCustomMade = formCustomMadeService.selectFormCMByID(formId);
        String formTask = formCustomMade.getFormTask();
        TaskData taskData = dictionaryService.selectTaskName(formTask);
        String table = taskData.getTableName();
        String fields1 = "";
        fields1 = fields.substring(1, fields.length());
        List<String> wheres = new ArrayList<>();
        if (inputval != null && !inputval.equals("")) {
            String where = MySqlUtil.getItem(field, term, inputval);
            wheres.add(where);
        }
        String sql = MySqlUtil.getFieldSql(fields1, table, wheres, null);
        List<Map<String, Object>> listMap = tableService.selectSqlMapList(sql);

        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        List<List<String>> list = new ArrayList<>();
        //插入Excel表中第一行数据
        List<String> list1 = new ArrayList<>();
        list1.add("序号");
        Hashtable<String, String> hs = InitDataListener.getMapData("fieldData");
        String[] fields2 = fields1.split(",");
        for (String key : fields2) {
            list1.add(hs.get(key));
        }
        list.add(list1);
        //插入Excel表中具体数据
        int x = 1;
        for (Map<String, Object> map1 : listMap) {
            List<String> list2 = new ArrayList<>();
            list2.add(String.valueOf(x));
            for (String key : fields2) {
                String val = String.valueOf(map1.get(key));
                FieldData fieldData = DDUtil.getFieldData(key);
                String value = ToNameUtil.getName(fieldData.getFieldType(), val);
                if (value == null || value.equals("null")) {
                    value = "";
                }
                list2.add(value);
            }
            list.add(list2);
            x++;
        }
        map.put(formCustomMade.getFormcmTitle(), list);

        if (!childTable.equals("")) {
            String[] childTables = childTable.substring(1, childTable.length()).split(",");

            for (String str : childTables) {
                //插入相关子表
                List<List<String>> list2 = new ArrayList<>();
                //添加空行
                List<String> list3 = new ArrayList<>();

                FieldData fieldData = dictionaryService.selectFieldName(str);
                //添加子表内容
                String special = fieldData.getSpecial();
                String[] specials = special.split("-");
                String formid = specials[1].substring(0, specials[1].length() - 1);
                TaskData taskData1 = dictionaryService.formTaskByFormId(formid);
                Vector<String> childfields = StringHelper.string2Vector(taskData1.getTaskField(), ";");
                String childfields1 = "";
                if (childfields != null && childfields.size() > 0) {
                    childfields1 = StringHelper.vector2SqlField(childfields, ",");
                }
                childfields1 = childfields1.substring(1, childfields1.length());
                List<String> list5 = new ArrayList<>();
                list5.add("序号");
                for (String key : childfields) {
                    list5.add(hs.get(key));
                }

                Vector<String> zbFields = StringHelper.string2Vector(taskData.getTaskField(), ";");
                //插入主表每行数据
                String zbFields1 = "";
                if (zbFields != null && zbFields.size() > 0) {
                    zbFields1 = StringHelper.vector2SqlField(zbFields, ",");
                }
                zbFields1 = "recorderNO" + zbFields1;
                String sql1 = MySqlUtil.getFieldSql(zbFields1, table, wheres, null);
                List<Map<String, Object>> zbListMap = tableService.selectSqlMapList(sql1);

                int i = 1;
                for (Map<String, Object> map1 : zbListMap) {
                    List<String> list4 = new ArrayList<>();
                    list4.add(formCustomMade.getFormcmTitle() + "：" + String.valueOf(i));
                    list2.add(list4);
                    list2.add(list5);

                    List<String> wheres1 = new ArrayList<>();
                    String where = MySqlUtil.getItem("linkRecorderNO", "等于", String.valueOf(map1.get("recorderNO")));
                    wheres1.add(where);
                    String sql2 = MySqlUtil.getFieldSql(childfields1, taskData1.getTableName(), wheres1, null);
                    List<Map<String, Object>> childListMap = tableService.selectSqlMapList(sql2);
                    int j = 1;
                    for (Map<String, Object> map2 : childListMap) {
                        List<String> childList = new ArrayList<>();
                        childList.add(String.valueOf(j));
                        for (String key : childfields) {
                            String val = String.valueOf(map2.get(key));
                            FieldData fieldData2 = DDUtil.getFieldData(key);
                            String value = ToNameUtil.getName(fieldData2.getFieldType(), val);
                            if (value == null || value.equals("null")) {
                                value = "";
                            }
                            childList.add(value);
                        }
                        list2.add(childList);
                        j++;
                    }
                    //添加两个空行
                    list2.add(list3);
                    list2.add(list3);
                    i++;
                }
                map.put(fieldData.getFieldTitle(), list2);
            }

        }

        ConfParseUtil cp = new ConfParseUtil();
        String upload_file = cp.getProperty("upload_file") + "excel/exportTable/";
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        String date = df.format(new Date());
        String filePath = upload_file + formCustomMade.getFormcmTitle() + date + ".xls";
        ExcelUtil.exportTable(filePath, map);
        String fileName = formCustomMade.getFormcmTitle() + date + ".xls";
        try {
            filePath = new String(filePath.getBytes("utf-8"), "iso-8859-1");
            fileName = new String(fileName.getBytes("utf-8"), "iso-8859-1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "redirect:/userpage/downloadTable.do?file=" + filePath + "&fileName=" + fileName;

    }


    //导出Excel
    @RequestMapping(value = "/exportTableTemp", method = RequestMethod.GET, produces = {"text/html;charset=UTF-8;", "application/json;"})
    public String exportTableTemp(HttpServletRequest request, HttpServletResponse response, String formId) {
        FormCustomMade formCustomMade = formCustomMadeService.selectFormCMByID(formId);
        String formTask = formCustomMade.getFormTask();
        TaskData taskData = dictionaryService.selectTaskName(formTask);
        String table = taskData.getTableName();
        String taskField = taskData.getTaskField();
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        List<List<String>> list = new ArrayList<>();
        List<String> list0 = new ArrayList<>();
        list0.add(table);
        list.add(list0);
        //插入Excel表中第一行数据
        List<String> list1 = new ArrayList<>();
        List<String> list2 = new ArrayList<>();
        list1.add("recorderNO");
        list2.add("主键");
        Vector<String> fields2 = StringHelper.string2Vector(taskField, ";");
        for (int i = 0, len = fields2.size(); i < len; i++) {
            FieldData fieldData = DDUtil.getFieldData(fields2.get(i));
            list1.add(fieldData.getFieldName());
            list2.add(fieldData.getFieldTitle());
        }
        list.add(list1);
        list.add(list2);
        map.put(formCustomMade.getFormcmTitle(), list);

        ConfParseUtil cp = new ConfParseUtil();
        String upload_file = cp.getProperty("upload_file") + "excel/exportTable/";
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        String date = df.format(new Date());
        String filePath = upload_file + formCustomMade.getFormcmTitle() + date + ".xls";
        ExcelUtil.exportTable(filePath, map);
        String fileName = formCustomMade.getFormcmTitle() + date + ".xls";
        try {
            filePath = new String(filePath.getBytes("utf-8"), "iso-8859-1");
            fileName = new String(fileName.getBytes("utf-8"), "iso-8859-1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "redirect:/userpage/downloadTable.do?file=" + filePath + "&fileName=" + fileName;

    }

    //下载Excel
    @RequestMapping(value = "/downloadTable", method = RequestMethod.GET, produces = {"text/html;charset=UTF-8;", "application/json;"})
    @ResponseBody
    public void downloadTable(HttpServletResponse response, String file, String fileName) {
        try {
            ExcelUtil.downloadLocal(response, file, fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 导入Excel弹框页面
     */
    @RequestMapping(value = "/gotoExcel", method = RequestMethod.GET)
    public ModelAndView gotoExcel(HttpServletRequest request, String filePath, String formId) {
        ModelAndView mav = new ModelAndView("manage/excel");
        FormCustomMade formCustomMade = formCustomMadeService.selectFormCMByID(formId);
        mav.addObject("filePath", filePath);
        mav.addObject("formCustomMade", formCustomMade);
        return mav;
    }

    //上传Excel文件
    @RequestMapping(value = "/uploadExcel", method = RequestMethod.POST, produces = {"text/html;charset=UTF-8;", "application/json;"})
    public ModelAndView uploadExcel(MultipartFile file, String formId) {
        String filePath = "";
        try {
            int idx = file.getOriginalFilename().lastIndexOf(".");
            String extention = file.getOriginalFilename().substring(idx);
            ConfParseUtil cp = new ConfParseUtil();
            String upload_file = cp.getProperty("upload_file") + "excel/importTable/";
            String path = upload_file + (System.currentTimeMillis() + "") + extention;
            File f = new File(path);
            file.transferTo(f);
            filePath = path;
        } catch (Exception e) {
            LogUtil.sysLog("Exception:" + e);
        }
        ModelAndView mav = new ModelAndView("manage/excel");
        FormCustomMade formCustomMade = formCustomMadeService.selectFormCMByID(formId);
        mav.addObject("filePath", filePath);
        mav.addObject("formCustomMade", formCustomMade);
        return mav;
    }

    //校验Excel数据
    @RequestMapping(value = "/checkExcel", method = RequestMethod.POST, produces = {"text/html;charset=UTF-8;", "application/json;"})
    @ResponseBody
    public String checkExcel(String filePath) {
        LinkedHashMap<String, Object> excelMap = new LinkedHashMap<>();
        excelMap = ExcelUtil.importTable(filePath);
        String result = "";
        List<String> zbIdList = new ArrayList<>();
        int x = 1;
        for (String str : excelMap.keySet()) {
            List<Map<String, Object>> list = (List<Map<String, Object>>) excelMap.get(str);
            String tableTitle = String.valueOf(list.get(0).get("tableTitle"));
            int row = 3;
            String linkRecorderNO = "";
            if (x > 1) {
                row = 4;
                linkRecorderNO = String.valueOf(list.get(1).get("linkRecorderNO")).trim();
                if (!zbIdList.contains(linkRecorderNO)) {
                    result += tableTitle + "第2行主表主键在主表中不存在;<br>";
                }
            }

            for (int i = 1; i < list.size(); i++) {
                int j = 1;
                for (String key : list.get(i).keySet()) {
                    if (list.get(i).get(key) != null && !"".equals(String.valueOf(list.get(i).get(key)))) {
                        FieldData fielData = dictionaryService.selectFieldName(key);
                        if (x > 1) {
                            //判断子表关联主键是否存在
                            String linkRecorderNO1 = String.valueOf(list.get(i).get("linkRecorderNO")).trim();
                            if (!linkRecorderNO.equals(linkRecorderNO1)) {
                                linkRecorderNO = linkRecorderNO1;
                                row = row + 5;
                                if (!zbIdList.contains(linkRecorderNO)) {
                                    result += tableTitle + "第" + (i + row - 3) + "行主表主键在主表中不存在;<br>";
                                }
                            }

                        } else {
                            zbIdList.add(String.valueOf(list.get(i).get("recorderNO")).trim());
                        }
                        //判断相关类型数据是否符合
                        if (fielData.getFieldType().equals("int")) {
                            String val = String.valueOf(list.get(i).get(key)).trim();
                            Pattern pattern = Pattern.compile("^(\\-|\\+)?[1-9]+(\\d+)?$");
                            if (!pattern.matcher(val).matches()) {
                                if (!"0".equals(val)) {
                                    result += tableTitle + "第" + (i + row) + "行" + "第" + j + "列" + "数据应为整型格式;<br>";
                                }
                            }
                        } else if (fielData.getFieldType().equals("decimal")) {
                            String val = String.valueOf(list.get(i).get(key)).trim();
                            Pattern pattern1 = Pattern.compile("^(\\-|\\+)?\\d+(\\.\\d+)?$");
                            if (!pattern1.matcher(val).matches()) {
                                if (!"0".equals(val)) {
                                    result += tableTitle + "第" + (i + row) + "行" + "第" + j + "列" + "数据应为小数格式;<br>";
                                }
                            }
                        } else if (fielData.getFieldType().equals("date")) {
                            Pattern pattern2 = Pattern.compile("^\\d{4}-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01])$");
                            if (!pattern2.matcher(String.valueOf(list.get(i).get(key)).trim()).matches()) {
                                result += tableTitle + "第" + (i + row) + "行" + "第" + j + "列" + "数据应为日期格式;<br>";
                            }
                        } else if (fielData.getFieldType().equals("datetime")) {
                            Pattern pattern3 = Pattern.compile("^\\d{4}-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01])\\s([01]?[0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]$");
                            if (!pattern3.matcher(String.valueOf(list.get(i).get(key)).trim()).matches()) {
                                result += tableTitle + "第" + (i + row) + "行" + "第" + j + "列" + "数据应为时间格式;<br>";
                            }
                        } else if (fielData.getFieldType().equals("user")) {
                            String name = String.valueOf(list.get(i).get(key)).trim();
                            List<UserManager> userList = userManagerService.getUserByName(name);
                            if (userList.size() == 0) {
                                result += tableTitle + "第" + (i + row) + "行" + "第" + j + "列" + "人员不存在;<br>";
                            }
                        } else if (fielData.getFieldType().equals("dept")) {
                            String deptName = String.valueOf(list.get(i).get(key)).trim();
                            Department department = departmentService.getDepartmentByDeptName(deptName);
                            if (department == null) {
                                result += tableTitle + "第" + (i + row) + "行" + "第" + j + "列" + "部门不存在;<br>";
                            }
                        }
                        j++;
                    }
                }
            }
            x++;
        }
        if (result.equals("")) {
            result = "校验通过";
        }
        return result;
    }

    //导出Excel弹框页面
    @RequestMapping(value = "/exportExcel", method = RequestMethod.GET)
    public ModelAndView exportExcel(HttpServletRequest request, String formId) {
        ModelAndView mav = new ModelAndView("manage/exportExcel");
        FormCustomMade formCustomMade = formCustomMadeService.selectFormCMByID(formId);
        mav.addObject("formCustomMade", formCustomMade);
        //获取所有字段
        String formTask = formCustomMade.getFormTask();
        TaskData taskData = dictionaryService.selectTaskName(formTask);
        Vector<String> fields = StringHelper.string2Vector(taskData.getTaskField(), ";");
        List<FieldData> list = new ArrayList<>();
        List<FieldData> childList = new ArrayList<>();
        for (String key : fields) {
            FieldData fieldData = dictionaryService.selectFieldName(key);
            if (fieldData.getFieldType().equals("child")) {
                childList.add(fieldData);
            } else {
                list.add(fieldData);
            }
        }
        mav.addObject("list", list);
        mav.addObject("childList", childList);
        mav.addObject("formId", formId);
        return mav;
    }

    //发送跟踪记录消息页面
    @RequestMapping(value = "/toSeeSendRecord", method = RequestMethod.GET)
    public ModelAndView toSeeSendRecord(HttpServletRequest request, String formid, String recno) {
        ModelAndView mav = new ModelAndView("module/sendRecord");
        FormCustomMade formCustomMade = formCustomMadeService.selectFormCMByID(formid);
        String formTask = formCustomMade.getFormTask();
        TaskData taskData = dictionaryService.selectTaskName(formTask);
        Vector<String> field = StringHelper.string2Vector(taskData.getTaskField(), ";");
        String fields = "";
        if (field != null && field.size() > 0) {
            fields = StringHelper.vector2SqlField(field, ",");
        }
        fields = "recorderNO" + fields + ",modifyName,modifyTime";
        List<String> wheres = new ArrayList<>();
        String where = "recorderNO='" + recno + "'";
        wheres.add(where);
        String sql = MySqlUtil.getFieldSql(fields, taskData.getTableName(), wheres, null);
        Map<String, Object> map = tableService.selectSqlMap(sql);
        String msgText = LogUtil.getFlowTableLog(formTask, "内容", taskData.getTableName(), map);
        mav.addObject("msgText", msgText);
        return mav;
    }

    @RequestMapping(value = "/getfs", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String getFieldSelect(HttpServletRequest request, String field,String value) {
        String fieldId = field.split("_")[1];
        JSONObject json = new JSONObject();
        FieldData fieldData = DDUtil.getFieldData(fieldId);
        String fieldType = fieldData.getFieldType();
        if (fieldType != null && (fieldType.equals("select") || fieldType.equals("selects"))) {
            String optionVal = fieldData.getOptionVal();
            String html = "";
            if (optionVal != null) {
                String[] option = optionVal.split("\n");
                for (String o : option) {
                    String sd = "";
                    if (o.contains(";")) {
                        String[] op = o.split(";");
                        if(op[1].equals(value)){
                            sd = "selected";
                        }
                        html += "<option value='" + op[0] + "' "+sd+">" + op[1] + "</option>\n";
                    } else {
                        if(o.equals(value)){
                            sd = "selected";
                        }
                        html += "<option value='" + o + "' "+sd+">" + o + "</option>\n";
                    }
                }
            }
            json.put("success", 1);
            json.put("data", html);
        } else {
            json.put("success", 0);
        }
        return json.toString();
    }
}

