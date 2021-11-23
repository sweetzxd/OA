package com.oa.core.controller.system;

import com.oa.core.bean.Loginer;
import com.oa.core.bean.dd.FieldData;
import com.oa.core.bean.dd.TaskData;
import com.oa.core.bean.system.FormCustomMade;
import com.oa.core.bean.system.MyUrlRegist;
import com.oa.core.bean.user.UserComputer;
import com.oa.core.bean.util.PageUtil;
import com.oa.core.bean.work.WorkFlowDefine;
import com.oa.core.bean.work.WorkFlowNode;
import com.oa.core.helper.DateHelper;
import com.oa.core.helper.StringHelper;
import com.oa.core.interceptor.Logined;
import com.oa.core.listener.InitDataListener;
import com.oa.core.service.dd.DictionaryService;
import com.oa.core.service.system.AccessRightsService;
import com.oa.core.service.system.FormCustomMadeService;
import com.oa.core.service.system.MyUrlRegistService;
import com.oa.core.service.system.RoleDefinesService;
import com.oa.core.service.user.UserComputerService;
import com.oa.core.service.util.TableService;
import com.oa.core.service.work.WorkFlowDefineService;
import com.oa.core.service.work.WorkFlowNodeService;
import com.oa.core.system.postposition.PostPosition;
import com.oa.core.util.*;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @ClassName:MyUrlController
 * @author:zxd
 * @Date:2018/11/29
 * @Time:上午 11:36
 * @Version V1.0
 * @Explain 菜单树定义类
 */

@Controller
@RequestMapping("/myurl")
public class MyUrlController {
    @Autowired
    MyUrlRegistService myUrlRegistService;
    @Autowired
    UserComputerService userComputerService;
    @Autowired
    RoleDefinesService roleDefinesService;
    @Autowired
    AccessRightsService accessRightsService;
    @Autowired
    FormCustomMadeService formCustomMadeService;
    @Autowired
    DictionaryService dictionaryService;
    @Autowired
    WorkFlowDefineService workFlowDefineService;
    @Autowired
    WorkFlowNodeService workFlowNodeService;
    @Autowired
    TableService tableService;

    @RequestMapping(value = "/myurlregist", method = RequestMethod.GET)
    public ModelAndView gotoMyUrlRegist(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("manage/myurlregist");
        mav.addObject("type", "list");
        return mav;
    }

    @RequestMapping(value = "/myurladd", method = RequestMethod.GET)
    public ModelAndView gotoMyUrlAdd(HttpServletRequest request, String pid) {
        MyUrlRegist myUrlRegist = myUrlRegistService.selectById(pid);
        if (myUrlRegist == null) {
            myUrlRegist = new MyUrlRegist();
            myUrlRegist.setPageId(pid);
            myUrlRegist.setPageTitle("OA");
        }
        ModelAndView mav = new ModelAndView("manage/myurlregist");
        mav.addObject("myurl", myUrlRegist);
        mav.addObject("type", "add");
        return mav;
    }

    @RequestMapping(value = "/myurlmodi/{pageid}", method = RequestMethod.GET)
    public ModelAndView gotoMyUrlModi(HttpServletRequest request, @PathVariable String pageid, String pid) {
        ModelAndView mav = new ModelAndView("manage/myurlregist");
        mav.addObject("myurl", myUrlRegistService.selectById(pageid));
        mav.addObject("parent", myUrlRegistService.selectById(pid));
        mav.addObject("type", "modi");
        return mav;
    }

    @RequestMapping(value = "/contextmenu", method = RequestMethod.GET)
    public ModelAndView gotoContextMenu(HttpServletRequest request, String id) {
        String conmenu = myUrlRegistService.selectContextMenu(id);
        String conmenutext = "";
        String conmenuval = "";
        if(conmenu!=null && conmenu!="") {
            JSONArray jsonArray = new JSONArray(conmenu);
            Hashtable ht = InitDataListener.getMapData("fieldData");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String pageId = jsonObject.getString("id");
                String[] asskey = jsonObject.getString("asskey").split("_");
                String[] mainasskey = jsonObject.getString("mainasskey").split("_");
                String title = "";
                try {
                    title = myUrlRegistService.selectById(pageId).getPageTitle();
                } catch (Exception e) {
                    title = pageId;
                }
                conmenuval += mainasskey[1] + ":" + pageId + ":" + asskey[1] + "|";
                conmenutext += "<div style='width: 95%;' data-value='" + mainasskey[1] + ":" + pageId + ":" + asskey[1] + "'>" +
                        "<span>" + ht.get(mainasskey[1]) + ":" + title + ":" + ht.get(asskey[1]) + "</span>" +
                        "<span style='float: right;' onclick='removemenu(this)'><a href='javascript:void(0);'>删除</a></span></div>\n";
            }
        }
        ModelAndView mav = new ModelAndView("manage/contextmenu");
        mav.addObject("myurl", myUrlRegistService.selectById(id));
        mav.addObject("conmenutext", conmenutext);
        mav.addObject("conmenuval", conmenuval);
        mav.addObject("field", FieldTypeUtil.getContextKey(id));
        mav.addObject("id", id);
        return mav;
    }

    @RequestMapping(value = "/openaccess", method = RequestMethod.GET)
    public ModelAndView openAccess(HttpServletRequest request, @RequestParam("pageid") String pageid){
        ModelAndView model = new ModelAndView("manage/template/accessrights");
        Map<String, String> access = accessRightsService.selectOneAccess(pageid);
        model.addObject("data", access);
        return model;
    }

    @RequestMapping(value = "/treecontexttable", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String getTreeContextTable(HttpServletRequest request) {
        MyUrlRegist myurl = new MyUrlRegist();
        myurl.setFormType(1);
        List<MyUrlRegist> myurllist = myUrlRegistService.selectTerms(myurl);
        List<MyUrlRegist> myurnodes = myUrlRegistService.selectNodeTerms(myurl);
        myurnodes.addAll(myurllist);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("msg", "");
        jsonObject.put("code", 0);
        jsonObject.put("count", 0);
        jsonObject.put("is", true);
        jsonObject.put("tip", "操作成功");
        jsonObject.put("data", myurnodes);
        jsonObject.put("success",1);
        return jsonObject.toString();
    }

    @RequestMapping(value = "/myurldel/{pageid}", method = RequestMethod.GET)
    public ModelAndView gotoMyUrlDel(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("manage/myurlregist");
        mav.addObject("type", "del");
        return mav;
    }

    @RequestMapping(value = "/treetable", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String getTreeTable(HttpServletRequest request) {
        List<MyUrlRegist> myurllist = myUrlRegistService.selectAll();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("msg", "");
        jsonObject.put("code", 0);
        jsonObject.put("count", 0);
        jsonObject.put("is", true);
        jsonObject.put("tip", "操作成功");
        jsonObject.put("data", myurllist);
        jsonObject.put("success",1);
        return jsonObject.toString();
    }

    @Logined
    @RequestMapping(value = "/insert", method = RequestMethod.POST, produces = {"text/html;charset=UTF-8;"})
    @ResponseBody
    public String postInsert(HttpServletRequest request, MyUrlRegist mur) {
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        String userId = loginer.getId() == null ? "" : loginer.getId();
        int formType = mur.getFormType();
        try {
            String pid = mur.getParentId();
            int security = myUrlRegistService.getSecurity(pid);
            if ("topmenu".equals(pid)) {
                mur.setMenuNum(1);
            } else {
                mur.setMenuNum(2);
            }
            PrimaryKeyUitl pk = new PrimaryKeyUitl();
            String pageId = pk.getNextId("MyUrlRegist", "pageId");
            mur.setPageId(pageId);
            mur.setSecurity(security);
            mur.setRecordName(userId);
            mur.setRecordTime(DateHelper.now());
            mur.setModifyName(userId);
            mur.setModifyTime(DateHelper.now());

            if (formType == 2) {
                String wkflwId = mur.getFormId();
                WorkFlowDefine flow = new WorkFlowDefine();
                flow.setWkflwID(wkflwId);
                flow.setPageId(pageId);
                flow.setModifyName(userId);
                flow.setModifyTime(DateHelper.now());
                workFlowDefineService.update(flow);
                WorkFlowNode node = workFlowNodeService.selectTopNode(wkflwId);
                String formId = node.getFormId();
                mur.setFormId(formId);
                myUrlRegistService.insert(mur);
            } else {
                myUrlRegistService.insert(mur);
            }
            return "true";
        } catch (Exception e) {
            e.printStackTrace();
            return "false";
        }
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST, produces = {"text/html;charset=UTF-8;"})
    @ResponseBody
    public String postUpdate(HttpServletRequest request, MyUrlRegist mur) {
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        String userId = loginer.getId() == null ? "" : loginer.getId();
        try {
            int formType = mur.getFormType();
            mur.setModifyName(userId);
            mur.setModifyTime(DateHelper.now());
            if (formType == 2) {
                String wkflwId = mur.getFormId();
                WorkFlowNode node = workFlowNodeService.selectTopNode(wkflwId);
                String formId = node.getFormId();
                mur.setFormId(formId);
                myUrlRegistService.update(mur);
            } else {
                myUrlRegistService.update(mur);
            }
            return "true";
        } catch (Exception e) {
            e.printStackTrace();
            return "false";
        }
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST, produces = {"text/html;charset=UTF-8;"})
    @ResponseBody
    public String postDelete(HttpServletRequest request, String pageid) {
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        String userId = loginer.getId() == null ? "" : loginer.getId();
        MyUrlRegist mur = new MyUrlRegist();
        mur.setParentId(pageid);
        List<MyUrlRegist> murlist = myUrlRegistService.selectTerms(mur);
        if (murlist.size() > 0) {
            pageid += "','";
            for (int i = 0; i < murlist.size(); i++) {
                pageid += murlist.get(i).getPageId() + "','";
            }
            pageid = "'" + pageid.substring(0, pageid.length() - 2);
        } else {
            pageid = "'" + pageid + "'";
        }
        try {
            mur = new MyUrlRegist();
            mur.setPageId(pageid);
            mur.setDeleteName(userId);
            mur.setDeleteTime(DateHelper.now());
            myUrlRegistService.deleteAll(mur);
            return "true";
        } catch (Exception e) {
            e.printStackTrace();
            return "false";
        }
    }

    @RequestMapping(value = "/isnode", method = RequestMethod.POST, produces = {"text/html;charset=UTF-8;"})
    @ResponseBody
    public String getIsNode(HttpServletRequest request, String type, String value) {
        String query = "false";
        MyUrlRegist mur = new MyUrlRegist();
        try {
            if (type.equals("parentid")) {
                mur.setParentId(value);
                List<MyUrlRegist> myurllist = myUrlRegistService.selectTerms(mur);
                if (myurllist != null) {
                    return "true";
                }
            }
            return query;
        } catch (Exception e) {
            e.printStackTrace();
            return query;
        }
    }

    /**
     * 更新菜单显示顺序*/
    @RequestMapping(value = "/setsecurity", method = RequestMethod.POST, produces = {"text/html;charset=UTF-8;"})
    @ResponseBody
    public String setSecurity(HttpServletRequest request) {
        String id = request.getParameter("id");
        int num = Integer.parseInt(request.getParameter("num"));
        String sid = request.getParameter("sid");
        int snum = Integer.parseInt(request.getParameter("snum"));
        try {
            boolean existed = num>0 && snum>0 && id!=null && sid!=null;
            if(existed){
                myUrlRegistService.updateSecurity(id,num,sid,snum);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return "0";
        }
    }

    @RequestMapping(value = "/gotoformselect", method = RequestMethod.GET)
    public ModelAndView gotoFormSelect(HttpServletRequest request, String value) {
        ModelAndView mav = new ModelAndView("manage/tableselect");
        if (value == null || value == "") {
            value = "1";
        }
        if (value.equals("2")) {
            String thead = "<th lay-data=\"{type:'radio',field:'wkflwID', width: 80, fixed: 'left'}\">选择</th>";
            thead += "<th lay-data=\"{field:'wkflwID', sort: true}\">流程主键</th>";
            thead += "<th lay-data=\"{field:'wkfName'}\">流程标题</th>";
            thead += "<th lay-data=\"{field:'wkfType', sort: true}\">流程类型</th>";
            mav.addObject("thead", thead);
            mav.addObject("url", "/workflow/workflowurl.do");
            mav.addObject("id", "wkflwID");
            mav.addObject("name", "wkfName");
            mav.addObject("type", "workflow");
        } else {
            String thead = "<th lay-data=\"{type:'radio',field:'formcmName', width: 80, fixed: 'left'}\">选择</th>";
            thead += "<th lay-data=\"{field:'formcmName', sort: true}\">页面主键</th>";
            thead += "<th lay-data=\"{field:'formcmTitle'}\">页面标题</th>";
            thead += "<th lay-data=\"{field:'formTask', sort: true}\">表单任务</th>";
            thead += "<th lay-data=\"{field:'listTask'}\">列表任务</th>";
            mav.addObject("thead", thead);
            mav.addObject("url", "/form_custom/formcmselect.do?option=formType&inputval=" + value);
            mav.addObject("id", "formcmName");
            mav.addObject("name", "formcmTitle");
            mav.addObject("type", "formcm");
        }
        return mav;
    }

    @RequestMapping("/formselect")
    @ResponseBody
    public String formcmSelect(HttpServletRequest request, String inputval, String option, int limit, int page) {
        FormCustomMade formCustomMade = new FormCustomMade();
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
        }
        int count = formCustomMadeService.selectCountFormCM(formCustomMade);
        PageUtil pageUtil = new PageUtil();
        pageUtil.setPageSize(limit);
        pageUtil.setCurrentPage(page);
        formCustomMade.setStartRow(pageUtil.getStartRow());
        formCustomMade.setEndRow(pageUtil.getEndRow() - pageUtil.getStartRow());
        List<FormCustomMade> formCustomMadeList = formCustomMadeService.selectAllFormCM(formCustomMade);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 0);
        jsonObject.put("msg", "");
        jsonObject.put("count", count);
        jsonObject.put("data", formCustomMadeList);
        jsonObject.put("success",1);
        return jsonObject.toString();
    }

    @RequestMapping(value = "/resetmenu", method = RequestMethod.POST, produces = {"text/html;charset=UTF-8;"})
    @ResponseBody
    public String resetMenu(HttpServletRequest request) {
        resetMyMenu(request, "admin");
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        String userId = loginer.getId() == null ? "" : loginer.getId();
        try {
            List<UserComputer> uclist = userComputerService.selectAll();
            for (int i = 0; i < uclist.size(); i++) {
                AccessUtil au = new AccessUtil();
                UserComputer uc = uclist.get(i);
                String menu = au.resetMenu(uc.getUserName());
                UserComputer userc = new UserComputer();
                if (menu == "") {
                    menu = uc.getUserMenu();
                }
                userc.setComputerId(uc.getComputerId());
                userc.setUserMenu(menu);
                userc.setBackupMenu(uc.getUserMenu());
                userc.setModifyName(userId);
                userc.setModifyTime(DateHelper.now());
                userComputerService.update(userc);
            }
            return "1";
        } catch (Exception e) {
            e.printStackTrace();
            return "0";
        }
    }

    @RequestMapping(value = "/resetmymenu", method = RequestMethod.POST, produces = {"text/html;charset=UTF-8;"})
    @ResponseBody
    public String resetMyMenu(HttpServletRequest request, String userName) {
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        String userId = loginer.getId() == null ? "" : loginer.getId();
        try {
            UserComputer uc = new UserComputer();
            uc.setUserName(userName);
            List<UserComputer> uclist = userComputerService.selectTerms(uc);
            AccessUtil au = new AccessUtil();
            uc = uclist.get(0);
            String menu = au.resetMenu(userName);
            UserComputer userc = new UserComputer();
            if (menu == "") {
                menu = uc.getUserMenu();
            }
            userc.setComputerId(uc.getComputerId());
            userc.setUserMenu(menu);
            userc.setBackupMenu(uc.getUserMenu());
            userc.setModifyName(userId);
            userc.setModifyTime(DateHelper.now());
            userComputerService.update(userc);
            return "1";
        } catch (Exception e) {
            e.printStackTrace();
            return "0";
        }
    }

    @RequestMapping(value = "/resetcontextmenu", method = RequestMethod.POST, produces = {"text/html;charset=UTF-8;"})
    @ResponseBody
    public String resetContextMenu(HttpServletRequest request, String pageId) {
        try {
            return FieldTypeUtil.getContextKey(pageId);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
    @RequestMapping(value = "/cmupdate", method = RequestMethod.POST, produces = {"text/html;charset=UTF-8;"})
    @ResponseBody
    public String cmUpdate(HttpServletRequest request, String pageId) {
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        String userId = loginer.getId() == null ? "" : loginer.getId();
        try {
            String val = request.getParameter("menuskip");
            Vector<String> v = StringHelper.string2Vector(val,"|");
            JSONArray jsonArray = new JSONArray();
            String maintId = getTableId(pageId);
            //删除旧的关联菜单数据
            MyUrlRegist myUrlRegist = new MyUrlRegist();
            myUrlRegist.setParentId(pageId);
            myUrlRegist.setFormType(5);
            myUrlRegistService.reallyDelete(myUrlRegist);
            //插入本菜单
            PrimaryKeyUitl pk = new PrimaryKeyUitl();
            String id = pk.getNextId("myurlregist", "pageId");
            String contextmenu = "";
            if(pageId.equals("Z2018101500002")){
                contextmenu = "/employees/modiemployees.do?type=iframe&staffId=";
            }else{
                contextmenu = "/userpage/pageform/";
            }
            MyUrlRegist myUrlRegist1 = myUrlRegistService.selectById(pageId);
            myUrlRegist.setPageId(id);
            myUrlRegist.setPageTitle(myUrlRegist1.getPageTitle());
            myUrlRegist.setMenuNum(100);
            myUrlRegist.setFormId(myUrlRegist1.getFormId());
            myUrlRegist.setContextmenu(contextmenu);
            myUrlRegist.setCurStatus(2);
            myUrlRegist.setRecordName(userId);
            myUrlRegist.setModifyName(userId);
            myUrlRegist.setRecordTime(DateHelper.now());
            myUrlRegist.setModifyTime(DateHelper.now());
            myUrlRegist.setSecurity(myUrlRegistService.getSecurity(pageId));
            myUrlRegistService.insert(myUrlRegist);
            for(String me : v) {
                String[] vme = me.split(":");
                //插入关联菜单数据
                MyUrlRegist myUrlRegist2 = myUrlRegistService.selectById(vme[1]);
                MyUrlRegist myUrlRegist3 = new MyUrlRegist();
                String id1 = pk.getNextId("myurlregist", "pageId");
                String contextmenu1 = "";
                //判断是否是流程页面
                if(myUrlRegist2.getFormType()==2){
                    WorkFlowNode workFlowNode = new WorkFlowNode();
                    workFlowNode.setFormId(myUrlRegist2.getFormId());
                    List<WorkFlowNode> list = workFlowNodeService.selectTerms(workFlowNode);
                    contextmenu1 = "/flowpage/flowviewpage/"+myUrlRegist2.getFormId()+".do?wkflwId="+list.get(0).getWkflwID();
                }else{
                    contextmenu1 = "/userpage/viewpage/"+vme[1]+".do?formid="+myUrlRegist2.getFormId()+"&field="+vme[2]+"&value=";
                }

                myUrlRegist3.setPageId(id1);
                myUrlRegist3.setParentId(pageId);
                myUrlRegist3.setFormId(myUrlRegist2.getFormId());
                myUrlRegist3.setFormType(5);
                myUrlRegist3.setPageTitle(myUrlRegist2.getPageTitle());
                myUrlRegist3.setMenuNum(100);
                myUrlRegist3.setContextmenu(contextmenu1);
                myUrlRegist3.setKeepFeild(maintId+"_"+vme[0]);
                myUrlRegist3.setCurStatus(2);
                myUrlRegist3.setRecordName(userId);
                myUrlRegist3.setModifyName(userId);
                myUrlRegist3.setRecordTime(DateHelper.now());
                myUrlRegist3.setModifyTime(DateHelper.now());
                myUrlRegist3.setSecurity(myUrlRegistService.getSecurity(pageId));
                myUrlRegistService.insert(myUrlRegist3);

                String tId = getTableId(vme[1]);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", vme[1]);
                jsonObject.put("mainasskey", maintId+"_"+vme[0]);
                jsonObject.put("asskey", tId+"_"+vme[2]);
                jsonArray.put(jsonObject);
            }
            myUrlRegistService.updateContextMenu(jsonArray.toString(),pageId);
            return "1";
        } catch (Exception e) {
            e.printStackTrace();
            return "0";
        }
    }

    public String getTableId(String pageId){
        String tableId = "";
        try {
            if(pageId.equals("Z2018101500002")) {
                tableId = "employees";
            }else if(pageId.equals("Z2018101500003")){
                tableId = "department";
            }else if(pageId.equals("Z2018101600001")){
                tableId = "message";
            }else {
                TaskData taskData = dictionaryService.formTaskByPageId(pageId);
                if(taskData!=null) {
                    tableId = taskData.getTableName();
                }
            }
        }catch (Exception e){
            LogUtil.sysLog("Exception:"+e);
            e.printStackTrace();
        }finally {
            return tableId;
        }
    }

    /**
     * 跳转自定义菜单页面
     * */
    @RequestMapping(value = "/custommenu", method = RequestMethod.GET)
    public ModelAndView customMenu(HttpServletRequest request) {
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        ModelAndView mav = new ModelAndView("module/custommenu");
        mav.addObject("listvalue",AccessUtil.getUserMenuValue(loginer.getId()));
        return mav;
    }

    /**
     * 获取个人全部菜单
     * */
    @RequestMapping(value = "/usercustommenu", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String getUserCustomMenu(HttpServletRequest request) {
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        String userId = loginer.getId() == null ? "" : loginer.getId();
        UserComputer sqlvalue = userComputerService.selectUserName(userId);
        String menu = sqlvalue.getUserMenu();
        JSONObject jsonmenu = new JSONObject(menu);
        JSONArray array = jsonmenu.getJSONArray("topmenu");

        List<String> pageid = new ArrayList<>();
        for(int i=0,m=array.length();i<m;i++){
            JSONObject object = array.getJSONObject(i);
            String id = object.getString("id");
            if(!pageid.contains(id)) {
                pageid.add(id);
            }
            JSONArray menuarray = object.getJSONArray("menus");
            for (int j = 0, n = menuarray.length(); j < n; j++) {
                JSONObject menuobject = menuarray.getJSONObject(j);
                String menuid = menuobject.getString("id");
                if (!pageid.contains(menuid)) {
                    pageid.add(menuid);
                }
            }
        }
        List<MyUrlRegist> plist = null;
        String page = StringUtils.join(pageid, "','");
        page = "'" + page + "'";
        if (page.length() > 3) {
            plist = myUrlRegistService.selectByIds(page);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("msg", "");
        jsonObject.put("code", 0);
        jsonObject.put("count", 0);
        jsonObject.put("is", true);
        jsonObject.put("menuskip", StringUtils.join(pageid, ";"));
        jsonObject.put("tip", "操作成功");
        jsonObject.put("data", plist);
        jsonObject.put("success",1);
        return jsonObject.toString();
    }

    /**
     * 保存个人常用菜单
     * */
    @RequestMapping(value = "/usercustommenusave", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String saveUserCustomMenu(HttpServletRequest request) {
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        String userId = loginer.getId();

        String table = "wdcyc18121001";
        List<String> field = new ArrayList<>();
        field.add("deleteName='"+userId+"'");
        field.add("deleteTime='"+DateHelper.now()+"'");
        List<String> where = new ArrayList<>();
        where.add("recordName='"+userId+"'");
        String delsql = MySqlUtil.getDeleteSql(table,field,where,null);
        tableService.updateSqlMap(delsql);

        String pageid = request.getParameter("menuskip");
        pageid = "'"+pageid.replaceAll(";","','")+"'";
        List<MyUrlRegist> plist = myUrlRegistService.selectByIds(pageid);
        PrimaryKeyUitl pk = new PrimaryKeyUitl();
        for(int i=0,len=plist.size();i<len;i++) {
            MyUrlRegist myurl = plist.get(i);
            String id = myurl.getPageId();
            String title = myurl.getPageTitle();
            String formid = myurl.getFormId();
            int type = myurl.getFormType();
            String url = "/userpage/viewpage/" + id + ".do";
            if (type == 2) {
                WorkFlowDefine wkflw = workFlowDefineService.selectByPageId(id);
                String wkflwId = wkflw.getWkflwID();
                url = "/flowpage/flowviewpage/" + formid + ".do?wkflwId=" + wkflwId;
            } else if (type == 3) {
                FormCustomMade formcm = formCustomMadeService.selectFormCMByID(formid);
                String page = formcm.getEditPage();
                if(page.contains(".do")){
                    url = page;
                }
            }
            String recorderNO = pk.getNextId(table, "recorderNO");
            Map<String, Object> map = new HashMap<>();
            map.put("recorderNO", recorderNO);
            map.put("cdmc181210001", title);
            map.put("cdzj181210001", id);
            map.put("cddz181210001", url);
            map.put("cdpx181210001", i);
            map.put("cdyh181210001", userId);
            map.put("recordName", userId);
            map.put("recordTime", DateHelper.now());
            map.put("modifyName", userId);
            map.put("modifyTime", DateHelper.now());
            String sql = MySqlUtil.getInsertSql(table, map);
            tableService.insertSqlMap(sql);
        }
        return "1";
    }

    /*
    * 首页加载个人常用菜单
    * **/

    @RequestMapping(value = "/getusermenu", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String getUserMenu(HttpServletRequest request) {
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        List<Map<String,Object>> value = AccessUtil.getUserMenuValue(loginer.getId());
        JSONArray jsonArray = new JSONArray(value);
        return jsonArray.toString();
    }

}
