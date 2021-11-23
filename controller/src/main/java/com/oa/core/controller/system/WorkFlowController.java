package com.oa.core.controller.system;

import com.oa.core.bean.Loginer;
import com.oa.core.bean.dd.FieldData;
import com.oa.core.bean.dd.TableData;
import com.oa.core.bean.dd.TaskData;
import com.oa.core.bean.module.Department;
import com.oa.core.bean.system.FormCustomMade;
import com.oa.core.bean.system.MyUrlRegist;
import com.oa.core.bean.system.RoleDefines;
import com.oa.core.bean.user.UserManager;
import com.oa.core.bean.util.PageUtil;
import com.oa.core.bean.work.*;
import com.oa.core.helper.DateHelper;
import com.oa.core.helper.StringHelper;
import com.oa.core.listener.InitDataListener;
import com.oa.core.service.dd.DictionaryService;
import com.oa.core.service.module.DepartmentService;
import com.oa.core.service.system.FormCustomMadeService;
import com.oa.core.service.system.MyUrlRegistService;
import com.oa.core.service.system.RoleDefinesService;
import com.oa.core.service.user.UserManagerService;
import com.oa.core.service.work.*;
import com.oa.core.system.workflow.ProcUtil;
import com.oa.core.util.FieldTypeUtil;
import com.oa.core.util.LogUtil;
import com.oa.core.util.MySqlUtil;
import com.oa.core.util.PrimaryKeyUitl;
import org.json.JSONArray;
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
 * @ClassName:WorkFlowController
 * @author:zxd
 * @Date:2018/09/21
 * @Time:上午 10:34
 * @Version V1.0
 * @Explain 流程定制
 */
@Controller
@RequestMapping("/workflow")
public class WorkFlowController {

    @Autowired
    WorkFlowDefineService workFlowDefineService;
    @Autowired
    WorkFlowNodeService workFlowNodeService;
    @Autowired
    WorkFlowLineService workFlowLineService;
    @Autowired
    WorkFlowProcService workFlowProcService;
    @Autowired
    WorkFlowLogService workFlowLogService;
    @Autowired
    WkflwNodeActorService wkflwNodeActorService;
    @Autowired
    RoleDefinesService roleDefinesService;
    @Autowired
    UserManagerService userManagerService;
    @Autowired
    DepartmentService departmentService;
    @Autowired
    FormCustomMadeService formCustomMadeService;
    @Autowired
    DictionaryService dictionaryService;
    @Autowired
    WkflwFieldMapService wkflwFieldMapService;
    @Autowired
    MyUrlRegistService myUrlRegistService;


    @RequestMapping(value = "/newworkflow", method = RequestMethod.GET)
    public ModelAndView gotoNewWorkFlow(HttpServletRequest request, String id) {
        if (id != null && id != "") {
            WorkFlowDefine wkflw = workFlowDefineService.selectById(id);
            String workflow = wkflw.getWkfValue();
            JSONObject json = new JSONObject(workflow);
            JSONArray nodeArray = json.getJSONArray("node");
            if (nodeArray.length() <= 0 || nodeArray.equals("")) {
                nodeArray = null;
            }
            JSONArray lineArray = json.getJSONArray("line");
            if (lineArray.length() <= 0 || nodeArray.equals("")) {
                lineArray = null;
            }
            ModelAndView mode = new ModelAndView("manage/newworkflow");
            mode.addObject("id", id);
            mode.addObject("wkflw", wkflw);
            mode.addObject("nodeArray", nodeArray);
            mode.addObject("lineArray", lineArray);
            return mode;
        } else {
            ModelAndView mode = new ModelAndView("manage/newworkflow");
            mode.addObject("id", "0");
            mode.addObject("wkflw", "0");
            mode.addObject("nodeArray", "0");
            mode.addObject("lineArray", "0");
            return mode;
        }
    }

    @RequestMapping(value = "/flowpage", method = RequestMethod.GET)
    public ModelAndView gotoFlowPage(HttpServletRequest request, String id) {
        ModelAndView mode = new ModelAndView("manage/flowpage");
        WorkFlowDefine wkflw = workFlowDefineService.selectById(id);
        List<WorkFlowNode> workFlowNode = workFlowNodeService.selectByWkflwId(id);
        String select = "";
        String nodes = "";
        try {
            for (WorkFlowNode node : workFlowNode) {
                int position = node.getNodePosition();
                String formId = node.getFormId();
                String nodeId = node.getNodeID();
                String nodeTitle = node.getNodeTitle();
                nodes += "<option value='" + nodeId + "'>" + nodeTitle + "</option>";
                if (position == 1) {
                    if(formId!=null && !formId.equals("")) {
                        FormCustomMade formCustomMade = formCustomMadeService.selectFormCMByID(formId);
                        String formTask = formCustomMade.getFormTask();
                        TaskData taskData = dictionaryService.selectTaskName(formTask);
                        String tableName = taskData.getTableName();
                        TableData tableData = dictionaryService.selectTableName(tableName);
                        select += "<optgroup label='" + nodeTitle + "'>";
                        String[] fields = taskData.getTaskField().split(";");
                        for (String field : fields) {
                            FieldData fieldData = dictionaryService.selectFieldName(field);
                            if (fieldData != null) {
                                String value = tableName + "-" + field;
                                String fieldTitle = tableData.getTableTitle() + "-" + fieldData.getFieldTitle();
                                select += "<option value='" + value + "'>" + fieldTitle + "</option>";
                            }
                        }
                        select += "</optgroup>";
                    }
                }
            }
        } catch (Exception e) {
            LogUtil.sysLog("Exception:" + e);
        }

        String text = wkflw.getFlowLabFld();
        try {
            List<String> msgList = StringHelper.getMsg(text);
            for (String msg : msgList) {
                String[] p = msg.split("_");
                TableData tableData = dictionaryService.selectTableName(p[0]);
                FieldData fieldData = dictionaryService.selectFieldName(p[1]);
                String title = tableData.getTableTitle() + "_" + fieldData.getFieldTitle();
                text = text.replaceAll(msg, title);
            }
        } catch (Exception e) {
            LogUtil.sysLog("Exception:" + e);
        }
        mode.addObject("flowLabFldname", text);
        mode.addObject("id", id);
        mode.addObject("wkflw", wkflw);
        mode.addObject("select", select);
        mode.addObject("nodes", nodes);
        return mode;
    }

    @RequestMapping(value = "/nodepage", method = RequestMethod.GET)
    public ModelAndView gotoNodePage(HttpServletRequest request, String id) {
        WorkFlowNode node = workFlowNodeService.selectById(id);
        List<WkflwNodeActor> actors = wkflwNodeActorService.selectByNodeId(id);
        ModelAndView mode = new ModelAndView("manage/nodepage");
        mode.addObject("id", id);
        mode.addObject("node", node);
        mode.addObject("actors", actors);
        return mode;
    }

    @RequestMapping(value = "/linepage", method = RequestMethod.GET)
    public ModelAndView gotoLinePage(HttpServletRequest request, String id) {
        WorkFlowLine line = workFlowLineService.selectById(id);
        String logUnitParams = line.getLogUnitParams();
        String wkflwId = line.getWkflwId();
        List<WorkFlowNode> workFlowNode = workFlowNodeService.selectByWkflwId(wkflwId);
        String select = "";
        try {
            for (WorkFlowNode node : workFlowNode) {
                String formId = node.getFormId();
                String nodeId = node.getNodeID();
                String nodeTitle = node.getNodeTitle();
                FormCustomMade formCustomMade = formCustomMadeService.selectFormCMByID(formId);
                String formTask = formCustomMade.getFormTask();
                TaskData taskData = dictionaryService.selectTaskName(formTask);
                String tableName = taskData.getTableName();
                TableData tableData = dictionaryService.selectTableName(tableName);
                select += "<optgroup label='" + nodeTitle + "'>";
                String[] fields = taskData.getTaskField().split(";");
                for (String field : fields) {
                    FieldData fieldData = dictionaryService.selectFieldName(field);
                    if (fieldData != null) {
                        String value = nodeId + "-" + tableName + "-" + field;
                        String fieldTitle = fieldData.getFieldTitle();
                        select += "<option value='" + value + "'>" + fieldTitle + "</option>";
                    }
                }
                select += "</optgroup>";
            }
        } catch (Exception e) {
            LogUtil.sysLog("Exception:" + e);
        }
        List params = new ArrayList();
        if (logUnitParams != null && logUnitParams.length() > 0) {
            params = StringHelper.getBracket(logUnitParams, "[");
        }
        ModelAndView mode = new ModelAndView("manage/linepage");
        mode.addObject("id", id);
        mode.addObject("line", line);
        mode.addObject("select", select);
        mode.addObject("logUnitParams", params);
        return mode;
    }

    @RequestMapping(value = "/logpage", method = RequestMethod.GET)
    public ModelAndView logPage(HttpServletRequest request, String procId,String wkflowId) {
        ModelAndView mode = new ModelAndView("manage/workflowlog");
        List<WorkFlowLog> wkflwlog = workFlowLogService.selectByProcId(procId,wkflowId);
        mode.addObject("wkflwlog", wkflwlog);
        return mode;
    }

    @RequestMapping(value = "/selectworkflow", method = RequestMethod.GET)
    public ModelAndView gotoSelectWorkFlow(HttpServletRequest request) {
        ModelAndView mode = new ModelAndView("manage/selectworkflow");
        List<WorkFlowDefine> wkflw = workFlowDefineService.selectAll();
        mode.addObject("wkflw", wkflw);
        return mode;
    }

    @RequestMapping(value = "/workflowshow", method = RequestMethod.GET)
    public ModelAndView gotoWorkFlowShow(HttpServletRequest request, String id) {
        WorkFlowDefine wkflw = workFlowDefineService.selectById(id);
        String workflow = wkflw.getWkfValue();
        String flowLabFld = wkflw.getFlowLabFld();
        JSONObject json = new JSONObject(workflow);
        JSONArray nodeArray = json.getJSONArray("node");
        JSONArray lineArray = json.getJSONArray("line");
        ModelAndView mode = new ModelAndView("manage/workflowshow");
        mode.addObject("wkflwId", id);
        mode.addObject("wkflw", wkflw);
        mode.addObject("nodeArray", nodeArray);
        mode.addObject("lineArray", lineArray);
        return mode;
    }

    @RequestMapping(value = "/saveflow", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String gotoSaveFlow(HttpServletRequest request, @RequestParam String wkflwId, @RequestParam String wkfName, @RequestParam String flowval) {
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        String userId = loginer.getId();
        PrimaryKeyUitl pk = new PrimaryKeyUitl();
        boolean newflow = false;
        if (wkflwId == null || wkflwId.equals("") || wkflwId.equals("0")) {
            newflow = true;
            wkflwId = pk.getNextId("WorkFlowDefine", "wkflwId", wkfName);
        }


        JSONObject json = new JSONObject(flowval);
        JSONArray nodeArray = json.getJSONArray("node");
        String title = "";
        Map<String, String> hm = new HashMap<String, String>();
        Map<String, Object> hms = new HashMap<String, Object>();
        hm.put("starNode", "starNode");
        hm.put("endNode", "endNode");
        JSONArray jsona = new JSONArray();
        for (int i = 0; i < nodeArray.length(); i++) {
            JSONObject nodejson = new JSONObject();
            JSONObject subObject = nodeArray.getJSONObject(i);
            String blockId = subObject.getString("BlockId");
            String text = subObject.getString("BlockContent");
            String id = subObject.getString("BlockId");
            String type = subObject.getString("BlockType");
            int x = subObject.getInt("BlockX");
            int y = subObject.getInt("BlockY");
            if (!"starNode".equals(blockId) && !"endNode".equals(blockId)) {
                WorkFlowNode node = new WorkFlowNode();
                if (id.indexOf("flowNode") >= 0) {

                    if (text == null || text == "") {
                        text = "流程节点";
                    }
                    String nodeId = pk.getNextId("WorkFlowNode", "nodeId", text);
                    hm.put(id, nodeId);
                    node.setNodeID(nodeId);
                    node.setNodeTitle(text);
                    node.setWkflwID(wkflwId);
                    node.setNodePosition(2);
                    node.setRecordName(userId);
                    node.setRecordTime(DateHelper.now());
                    node.setModifyName(userId);
                    node.setModifyTime(DateHelper.now());
                    workFlowNodeService.insert(node);
                    nodejson.put("BlockId", nodeId);
                    nodejson.put("BlockContent", text);
                    nodejson.put("BlockType", type);
                    nodejson.put("BlockX", x);
                    nodejson.put("BlockY", y);
                    jsona.put(nodejson);
                } else {
                    node = workFlowNodeService.selectById(id);
                    hm.put(id, id);
                    node.setNodeID(id);
                    node.setNodeTitle(text);
                    node.setWkflwID(wkflwId);
                    node.setModifyName(userId);
                    node.setModifyTime(DateHelper.now());
                    workFlowNodeService.update(node);
                    nodejson.put("BlockId", id);
                    nodejson.put("BlockContent", text);
                    nodejson.put("BlockType", type);
                    nodejson.put("BlockX", x);
                    nodejson.put("BlockY", y);
                    jsona.put(nodejson);
                }
            } else {
                nodejson.put("BlockId", id);
                nodejson.put("BlockContent", text);
                nodejson.put("BlockType", type);
                nodejson.put("BlockX", x);
                nodejson.put("BlockY", y);
                jsona.put(nodejson);
            }
        }
        hms.put("node", jsona);
        jsona = new JSONArray();
        JSONArray lineArray = json.getJSONArray("line");
        List<String> lineIds = workFlowLineService.selectPathIds(wkflwId);
        List<String> newLineId = new ArrayList<>();
        for (int i = 0; i < lineArray.length(); i++) {
            JSONObject linejson = new JSONObject();
            JSONObject subObject = lineArray.getJSONObject(i);
            String text = subObject.getString("ConnectionText");
            String id = subObject.getString("ConnectionId");
            String formNode = subObject.getString("PageSourceId");
            String formText = subObject.getString("SourceText");
            String toNode = subObject.getString("PageTargetId");
            String toText = subObject.getString("TargetText");

            String formuuid = subObject.getString("StarUuid");
            formuuid = formuuid.substring(formNode.length(), formuuid.length());
            String touuid = subObject.getString("EndUuid");
            touuid = touuid.substring(toNode.length(), touuid.length());
            WorkFlowLine line = new WorkFlowLine();
            if (id.indexOf("con_") >= 0) {
                if (text == null || text == "") {
                    text = "线";
                }
                String lineId = pk.getNextId("workflowline", "pathId");
                newLineId.add(lineId);
                line.setPathId(lineId);
                line.setPathTitle(text);
                line.setWkflwId(wkflwId);
                line.setFromNode(hm.get(formNode));
                line.setToNode(hm.get(toNode));
                line.setLogUnitType(1);
                line.setRecordName(userId);
                line.setRecordTime(DateHelper.now());
                line.setModifyName(userId);
                line.setModifyTime(DateHelper.now());
                workFlowLineService.insert(line);
                linejson.put("ConnectionText", text);
                linejson.put("ConnectionId", lineId);
                linejson.put("PageSourceId", hm.get(formNode));
                linejson.put("SourceText", formText);
                linejson.put("PageTargetId", hm.get(toNode));
                linejson.put("TargetText", toText);
                linejson.put("StarUuid", hm.get(formNode) + formuuid);
                linejson.put("EndUuid", hm.get(toNode) + touuid);
                jsona.put(linejson);
            } else {
                line = workFlowLineService.selectById(id);
                line.setPathId(id);
                newLineId.add(id);
                line.setPathTitle(text);
                line.setWkflwId(wkflwId);
                line.setFromNode(formNode);
                line.setToNode(toNode);
                line.setModifyName(userId);
                line.setModifyTime(DateHelper.now());
                workFlowLineService.update(line);
                linejson.put("ConnectionText", text);
                linejson.put("ConnectionId", id);
                linejson.put("PageSourceId", formNode);
                linejson.put("SourceText", formText);
                linejson.put("PageTargetId", toNode);
                linejson.put("TargetText", toText);
                linejson.put("StarUuid", formNode + formuuid);
                linejson.put("EndUuid", toNode + touuid);
                jsona.put(linejson);
            }
            if (formNode.equals("starNode")) {
                workFlowNodeService.updatePosition(toNode, 1);
            } else if (toNode.equals("endNode")) {
                workFlowNodeService.updatePosition(formNode, 3);
            }
        }
        hms.put("line", jsona);
        JSONObject jsonObject = new JSONObject(hms);
        WorkFlowDefine wkflw = new WorkFlowDefine();
        wkflw.setWkflwID(wkflwId);
        wkflw.setModifyName(userId);
        wkflw.setModifyTime(DateHelper.now());
        wkflw.setWkfdefStatus(1);
        LogUtil.sysLog(">" + jsonObject.toString());
        wkflw.setWkfValue(jsonObject.toString());
        if (newflow) {
            wkflw.setRecordName(userId);
            wkflw.setRecordTime(DateHelper.now());
            wkflw.setModifyName(userId);
            wkflw.setModifyTime(DateHelper.now());
            wkflw.setWkfName(wkfName);
            workFlowDefineService.insert(wkflw);
        } else {
            workFlowDefineService.update(wkflw);
            lineIds.removeAll(newLineId);
            if (lineIds.size() > 0) {
                Map map = new HashMap();
                map.put("deleteName", userId);
                map.put("deleteTime", DateHelper.now());
                map.put("ids", lineIds);
                workFlowLineService.deletes(map);
            }
        }
        return "0";
    }

    @RequestMapping(value = "/deletenode", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String deleteNode(HttpServletRequest request, @RequestParam String id) {
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        try {
            String userId = loginer.getId();
            WorkFlowNode node = new WorkFlowNode();
            node.setNodeID(id);
            node.setDeleteName(userId);
            node.setDeleteTime(DateHelper.now());
            workFlowNodeService.delete(node);
            WorkFlowLine line = new WorkFlowLine();
            line.setDeleteName(userId);
            line.setDeleteTime(DateHelper.now());
            line.setFromNode(id);
            line.setToNode(id);
            workFlowLineService.deleteByNode(line);
            return "1";
        } catch (Exception e) {
            return "0";
        }
    }

    @RequestMapping(value = "/deleteline", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String deleteLine(HttpServletRequest request, @RequestParam String id) {
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        String userId = loginer.getId();
        WorkFlowLine line = new WorkFlowLine();
        line.setPathId(id);
        line.setDeleteName(userId);
        line.setDeleteTime(DateHelper.now());
        try {
            workFlowLineService.delete(line);
            return "1";
        } catch (Exception e) {
            return "0";
        }
    }

    @RequestMapping(value = "/actorpage", method = RequestMethod.GET)
    public ModelAndView gotoActorPage(HttpServletRequest request, String workflwId, String nodeId) {
        List<WorkFlowNode> nodelist = workFlowNodeService.selectByWkflwId(workflwId);
        List<String[]> nodeIds = new ArrayList<>();
        for (WorkFlowNode node : nodelist) {
            String id = node.getNodeID();
            if (!nodeId.equals(id)) {
                String name = node.getNodeTitle();
                String[] val = new String[]{id, name};
                nodeIds.add(val);
            }
        }
        List<RoleDefines> rolelist = roleDefinesService.selectAll();
        List<Department> deptlist = departmentService.selectAll();
        List<UserManager> userlist = userManagerService.selectAll();
        ModelAndView mode = new ModelAndView("manage/actorpage");
        mode.addObject("nodeId", nodeId);
        mode.addObject("rolelist", rolelist);
        mode.addObject("userlist", userlist);
        mode.addObject("deptlist", deptlist);
        mode.addObject("nodeIds", nodeIds);
        return mode;
    }

    @RequestMapping(value = "/selectnodeactor", method = RequestMethod.POST)
    @ResponseBody
    public String selectNodeActor(HttpServletRequest request, String nodeId) {
        JSONObject jsonObject = new JSONObject();
        WorkFlowNode node = workFlowNodeService.selectById(nodeId);
        String nodeTitle = node.getNodeTitle();
        String formId = node.getFormId();
        FormCustomMade formCustomMade = formCustomMadeService.selectFormCMByID(formId);
        String formTask = formCustomMade.getFormTask();
        TaskData taskData = dictionaryService.selectTaskName(formTask);
        String tableName = taskData.getTableName();
        TableData tableData = dictionaryService.selectTableName(tableName);
        String tableTitle = tableData.getTableTitle();
        Vector<String> fields = StringHelper.string2Vector(taskData.getTaskField(), ";");
        fields.add("recordName");
        List<Hashtable> data = new ArrayList();
        for (String field : fields) {
            FieldData fieldData = dictionaryService.selectFieldName(field);
            if (fieldData != null) {
                String fieldType = fieldData.getFieldType();
                if (FieldTypeUtil.nodeActorField(fieldType)) {
                    if (fieldType.equals("user") || fieldType.equals("users")) {
                        String fieldTitle = fieldData.getFieldTitle();
                        String id = nodeId + "_" + tableName + "_" + field + "_" + fieldType;
                        String name = nodeTitle + " [" + tableTitle + "_" + fieldTitle + "]";
                        Hashtable hs = new Hashtable();
                        hs.put("actorId", id);
                        hs.put("actorName", name);
                        data.add(hs);
                        Hashtable hs1 = new Hashtable();
                        hs1.put("actorId", nodeId + "_" + tableName + "_" + field + "_dept");
                        hs1.put("actorName", nodeTitle + " [" + tableTitle + "_" + fieldTitle + "_部门负责人]");
                        data.add(hs1);
                    } else if (fieldType.equals("dept") || fieldType.equals("depts")) {
                        String fieldTitle = fieldData.getFieldTitle();
                        String id = nodeId + "_" + tableName + "_" + field + "_" + fieldType;
                        String name = nodeTitle + " [" + tableTitle + "_" + fieldTitle + "_部门负责人]";
                        Hashtable hs = new Hashtable();
                        hs.put("actorId", id);
                        hs.put("actorName", name);
                        data.add(hs);
                    }
                }
            }
        }
        jsonObject.put("code", 0);
        jsonObject.put("msg", "");
        jsonObject.put("count", data.size());
        jsonObject.put("data", data);
        jsonObject.put("success",1);
        return jsonObject.toString();
    }

    @RequestMapping(value = "/flowsave", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String flowSave(HttpServletRequest request, @RequestParam String data) {
        try {
            Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
            String userId = loginer.getId();
            JSONObject json = new JSONObject(data);
            String wkflwID = json.getString("wkflwID");
            String wkfName = json.getString("wkfName");
            String wkfType = json.getString("wkfType");
            String flowLabFld = json.getString("flowLabFld");
            String specialField = json.getString("specialField");
            String flowLabFldliucheng = json.getString("flowLabFldliucheng");
            WorkFlowDefine wkflw = new WorkFlowDefine();
            wkflw.setWkflwID(wkflwID);
            wkflw.setWkfName(wkfName);
            wkflw.setWkfType(wkfType);
            wkflw.setFlowLabFld(flowLabFld);
            wkflw.setFlowLabFldliucheng(flowLabFldliucheng);
            wkflw.setSpecialField(specialField);
            wkflw.setModifyName(userId);
            wkflw.setModifyTime(DateHelper.now());
            workFlowDefineService.update(wkflw);
            return "1";
        } catch (Exception e) {
            return "0";
        }
    }

    @RequestMapping(value = "/nodesave", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String nodeSave(HttpServletRequest request, @RequestParam String data) {
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        String userId = loginer.getId();
        PrimaryKeyUitl pk = new PrimaryKeyUitl();
        JSONObject json = new JSONObject(data);
        String nodeId = json.getString("nodeId");
        String nodeTitle = json.getString("nodeTitle");
        int allowOtherDoit = Integer.parseInt(json.getString("allowOtherDoit"));
        int isORAction = Integer.parseInt(json.getString("isORAction"));
        WorkFlowNode node = new WorkFlowNode();
        node.setNodeID(nodeId);
        node.setNodeTitle(nodeTitle);
        node.setAllowOtherDoit(allowOtherDoit);
        node.setIsParellel(1);
        node.setIsORAction(isORAction);
        node.setModifyName(userId);
        node.setModifyTime(DateHelper.now());
        workFlowNodeService.update(node);
        JSONArray jsonArray = json.getJSONArray("actor");
        ArrayList<String> actorIds = wkflwNodeActorService.selectListActor(nodeId);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject subObject = jsonArray.getJSONObject(i);
            String id = subObject.getString("id");

            if (!actorIds.contains(id)) {
                String type = subObject.getString("type");
                String context = subObject.getString("context");
                WkflwNodeActor actor = new WkflwNodeActor();
                actor.setNodeID(nodeId);
                actor.setContextValue(context);
                actor.setWkfActorType(type);
                List nodeActor = wkflwNodeActorService.selectTerms(actor);
                if (nodeActor != null && nodeActor.size() <= 0) {
                    String nodeactorid = pk.getNextId("WkflwNodeActor", "nodeActorID");
                    actor.setNodeActorID(nodeactorid);
                    actor.setRecordName(userId);
                    actor.setRecordTime(DateHelper.now());
                    actor.setModifyName(userId);
                    actor.setModifyTime(DateHelper.now());
                    wkflwNodeActorService.insert(actor);
                }
            } else {
                actorIds.remove(id);
            }
        }
        if (actorIds != null && actorIds.size() > 0) {
            wkflwNodeActorService.deletes(actorIds);
        }
        return "";
    }

    @RequestMapping(value = "/linesave", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String lineSave(HttpServletRequest request, @RequestParam String data) {
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        String userId = loginer.getId();
        JSONObject json = new JSONObject(data);
        String pathId = json.getString("pathId");
        int isAsynchPath = Integer.parseInt(json.getString("isAsynchPath"));
        int isRebackPath = Integer.parseInt(json.getString("isRebackPath"));
        int logTypeOfByMultActor = Integer.parseInt(json.getString("logTypeOfByMultActor"));
        int logUnitType = Integer.parseInt(json.getString("logUnitType"));
        String logUnitParams = json.getString("logUnitParams");
        if (logUnitType == 1) {
            logUnitParams = "";
        }
        int path1ToOtherTask = Integer.parseInt(json.getString("path1ToOtherTask"));
        int judgeAfterAll = 0;
        if (logTypeOfByMultActor == 2) {
            judgeAfterAll = 1;
        }
        WorkFlowLine line = new WorkFlowLine();
        line.setPathId(pathId);
        line.setIsAsynchPath(isAsynchPath);
        line.setIsRebackPath(isRebackPath);
        line.setLogTypeOfByMultActor(logTypeOfByMultActor);
        line.setLogUnitType(logUnitType);
        line.setLogUnitParams(logUnitParams);
        line.setPath1ToOtherTask(path1ToOtherTask);
        line.setJudgeAfterAll(judgeAfterAll);
        line.setModifyName(userId);
        line.setModifyTime(DateHelper.now());
        workFlowLineService.update(line);
        return "";
    }

    //更新formId
    @RequestMapping(value = "/updateNodeFormId", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String updateNodeFormId(HttpServletRequest request, @RequestParam String nodeID, @RequestParam String formId) {
        WorkFlowNode node = new WorkFlowNode();
        node.setNodeID(nodeID);
        node.setFormId(formId);
        workFlowNodeService.update(node);
        return "1";
    }

    @RequestMapping("/workflowurl")
    @ResponseBody
    public String formcmSelect(HttpServletRequest request, int limit, int page) {
        WorkFlowDefine workFlowDefine = new WorkFlowDefine();
        workFlowDefine.setWkfdefStatus(1);
        int count = workFlowDefineService.selectTermsCount(workFlowDefine);
        PageUtil pageUtil = new PageUtil();
        pageUtil.setPageSize(limit);
        pageUtil.setCurrentPage(page);
        workFlowDefine.setStartRow(pageUtil.getStartRow());
        workFlowDefine.setEndRow(pageUtil.getEndRow() - pageUtil.getStartRow());
        List<WorkFlowDefine> workFlowDefinesList = workFlowDefineService.selectTerms(workFlowDefine);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 0);
        jsonObject.put("msg", "");
        jsonObject.put("count", count);
        jsonObject.put("data", workFlowDefinesList);
        jsonObject.put("success",1);
        return jsonObject.toString();

    }

    @RequestMapping(value = "/workflowproc", method = RequestMethod.GET)
    public ModelAndView wrkFlowProc(HttpServletRequest request, String wkflwId, String type, String starnode) {
        ModelAndView mode = new ModelAndView("manage/workflowproc");
        ProcUtil pu = new ProcUtil();
        String workflow = pu.getMenu();
        mode.addObject("workflow", workflow);
        mode.addObject("wkflwId", wkflwId);
        mode.addObject("type", type);
        mode.addObject("starnode", starnode);
        return mode;
    }

    @RequestMapping(value = "/mywkfprocnum", method = RequestMethod.POST)
    @ResponseBody
    public String getWrkFlowProcNum(HttpServletRequest request, String wkflwId, String type, String starnode) {
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        String userId = loginer.getId();
        JSONObject jsonObject = new JSONObject();
        List<Integer> n = workFlowProcService.getWrkFlowProcNum(wkflwId, userId, starnode);

        jsonObject.put("WC", n.get(1));
        jsonObject.put("JX", n.get(0));
        return jsonObject.toString();
    }

    @RequestMapping("/selectproclist")
    @ResponseBody
    public String getWrkFlowProcList(HttpServletRequest request, String inputval, String option, int limit, int page, String type, String starnode) {
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        String userId = loginer.getId();
        WorkFlowProc workFlowProc = new WorkFlowProc();
        if (inputval != null && inputval != "") {
            if ("wkflwId".equals(option)) {
                workFlowProc.setWkflwID(inputval);
            }else if("originator".equals(option)){
                workFlowProc.setOriginator(inputval);
            }
        }
        if (type != null && !type.equals("") && starnode != null) {
            if (starnode.equals("true")) {
                workFlowProc.setOriginator(userId);
            } else if (starnode.equals("false")) {
                workFlowProc.setFlowpeoples(userId);
                workFlowProc.setNodespeople(userId);
            }
            if (type.equals("wc")) {
                workFlowProc.setWkfStatus(2);
            } else if (type.equals("jx")) {
                workFlowProc.setWkfStatus(1);
            }
        }
        int count = workFlowProcService.selectTermsCount(workFlowProc);
        PageUtil pageUtil = new PageUtil();
        pageUtil.setPageSize(limit);
        pageUtil.setCurrentPage(page);
        pageUtil.setTotalRecord(count);
        int startRow = pageUtil.getStartRow();
        int endRow = pageUtil.getEndRow();
        workFlowProc.setStartRow(startRow);
        workFlowProc.setEndRow(endRow - startRow);
        List<WorkFlowProc> workFlowProcs = workFlowProcService.selectTerms(workFlowProc);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 0);
        jsonObject.put("msg", "");
        jsonObject.put("count", count);
        jsonObject.put("data", workFlowProcs);
        jsonObject.put("success",1);
        jsonObject.put("is", true);
        jsonObject.put("tip", "操作成功");
        return jsonObject.toString();
    }

    //查看映射表单字段
    @RequestMapping(value = "/seeWkflwfieldmap", method = RequestMethod.GET)
    public ModelAndView seeMessagepage(HttpServletRequest request, String wkflwId, String type) {
        ModelAndView mav = new ModelAndView("manage/wkflwfieldmap");
        List<WorkFlowNode> nodeList = workFlowNodeService.selectByWkflwId(wkflwId);
        String option = "";
        for (WorkFlowNode workFlowNode : nodeList) {
            option += "<option value='" + workFlowNode.getNodeID() + "'>" + workFlowNode.getNodeTitle() + "</option>";
        }
        mav.addObject("option", option);
        mav.addObject("type", type);
        mav.addObject("wkflwId", wkflwId);
        return mav;
    }

    //跟据pageId获取表单字段
    @RequestMapping(value = "/getFieldsByPageId", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String getFieldsByPageId(String pageId) {
        String option = "";
        MyUrlRegist myurl = myUrlRegistService.selectById(pageId);
        String formId = myurl.getFormId();
        FormCustomMade form = formCustomMadeService.selectFormCMByID(formId);
        String formTask = form.getFormTask();
        TaskData task = dictionaryService.selectTaskName(formTask);
        String tableName = task.getTableName();
        String fields = task.getTaskField();
        Vector<String> field = StringHelper.string2Vector(fields, ";");
        Hashtable<String, String> ht = InitDataListener.getMapData("fieldData");
        for (String f : field) {
            String nodeFieldName = "";
            String nodeFieldId = "";
            String wkflwfieldmapId = "";
            WkflwFieldMap wkflwFieldMap = wkflwFieldMapService.selectWkflwFieldMapByFormFieldName(f);
            if (wkflwFieldMap != null) {
                if (ht != null) {
                    nodeFieldName = ht.get(wkflwFieldMap.getNodeFieldName());
                }
                nodeFieldId = wkflwFieldMap.getNodeFieldName();
                wkflwfieldmapId = wkflwFieldMap.getWkflwfieldmapId();
            }
            if (ht != null) {
                option += "<input type='hidden' name='fieldmap' id=" + tableName + "_" + f + ">" +
                        "<div class='workflow-fieldmap'>" +
                        "<span title='" + ht.get(f) + "'>表单：" + ht.get(f) + "</span>" +
                        "<span id='node" + tableName + "_" + f + "' title='" + nodeFieldId + "'>节点：" + nodeFieldName + "</span>" +
                        "<a onclick=delNodeFieldName('" + wkflwfieldmapId + "','node" + tableName + "_" + f + "') class='layui-btn layui-btn-xs layui-btn-danger'>删除</a>" +
                        "<a onclick=editNodeFieldName('node" + tableName + "_" + f + "') class='layui-btn layui-btn-xs'>选择</a>" +
                        "</div>";
            }
        }
        return option;
    }

    //跟据nodeId获取表单字段
    @RequestMapping(value = "/getFieldsByNodeId", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String getFieldsByNodeId(String nodeId) {
        String option = "";
        WorkFlowNode workflownode = workFlowNodeService.selectById(nodeId);
        if (workflownode.getFormId() != null && workflownode.getFormId() != "") {
            FormCustomMade form = formCustomMadeService.selectFormCMByID(workflownode.getFormId());
            String formTask = form.getFormTask();
            TaskData task = dictionaryService.selectTaskName(formTask);
            String tableName = task.getTableName();
            String fields = task.getTaskField();
            Vector<String> field = StringHelper.string2Vector(fields, ";");
            Hashtable<String, String> ht = InitDataListener.getMapData("fieldData");
            for (String f : field) {
                String nodeFieldName = "";
                WkflwFieldMap wkflwFieldMap = wkflwFieldMapService.selectWkflwFieldMapByFormFieldName(f);
                if (wkflwFieldMap != null) {
                    nodeFieldName = ht.get(wkflwFieldMap.getNodeFieldName());
                }
                if (ht != null) {
                    option += "<option value='" + tableName + "_" + f + "'>" + ht.get(f) + "</option>";
                }
            }
        }
        return option;
    }

    //添加或修改表单字段映射
    @RequestMapping(value = "/saveWkflwfieldmap", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String saveWkflwfieldmap(HttpServletRequest request, String fieldstr, String wkflwId) {
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        String userId = "";
        if (loginer != null) {
            userId = loginer.getId();
        }
        String[] fields = fieldstr.split(",");
        for (String str : fields) {
            String[] field = str.split(";");
            String[] field1 = field[0].split("_");
            String[] field2 = field[1].split("_");
            WkflwFieldMap wkflwFieldMap = wkflwFieldMapService.selectWkflwFieldMapByFormFieldName(field1[1]);
            if (wkflwFieldMap != null) {
                wkflwFieldMap.setNodeTableName(field2[0]);
                wkflwFieldMap.setNodeFieldName(field2[1]);
                wkflwFieldMap.setModifyName(userId);
                wkflwFieldMap.setModifyTime(DateHelper.now());
                wkflwFieldMapService.update(wkflwFieldMap);
            } else {
                WkflwFieldMap wkflwFieldMap1 = new WkflwFieldMap();
                PrimaryKeyUitl pk = new PrimaryKeyUitl();
                String id = pk.getNextId("wkflwfieldmap", "wkflwfieldmapId");
                wkflwFieldMap1.setWkflwfieldmapId(id);
                wkflwFieldMap1.setWkflwId(wkflwId);
                wkflwFieldMap1.setFormTableName(field1[0]);
                wkflwFieldMap1.setFormFieldName(field1[1]);
                wkflwFieldMap1.setNodeTableName(field2[0]);
                wkflwFieldMap1.setNodeFieldName(field2[1]);
                wkflwFieldMap1.setCurStatus(2);
                wkflwFieldMap1.setRecordName(userId);
                wkflwFieldMap1.setRecordTime(DateHelper.now());
                wkflwFieldMapService.insert(wkflwFieldMap1);
            }
        }
        return "1";
    }

    //删除表单字段映射
    @RequestMapping(value = "/delWkflwfieldmap", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String delWkflwfieldmap(HttpServletRequest request, String formFieldName) {
        String[] str = formFieldName.split("_");
        WkflwFieldMap wkflwFieldMap = wkflwFieldMapService.selectWkflwFieldMapByFormFieldName(str[1]);
        if (wkflwFieldMap != null) {
            Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
            String userId = "";
            if (loginer != null) {
                userId = loginer.getId();
            }
            wkflwFieldMap.setDeleteName(userId);
            wkflwFieldMap.setDeleteTime(DateHelper.now());
            wkflwFieldMapService.delete(wkflwFieldMap);
        }
        return "1";
    }
}
