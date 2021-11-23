package com.oa.core.controller.menuskip;

import com.oa.core.bean.Loginer;
import com.oa.core.bean.dd.FieldData;
import com.oa.core.bean.dd.TaskData;
import com.oa.core.bean.system.FormCustomMade;
import com.oa.core.bean.work.WorkFlowNode;
import com.oa.core.controller.util.LoginUtil;
import com.oa.core.helper.DateHelper;
import com.oa.core.helper.StringHelper;
import com.oa.core.service.dd.DictionaryService;
import com.oa.core.service.system.FormCustomMadeService;
import com.oa.core.service.system.RoleDefinesService;
import com.oa.core.service.system.TaskSenderService;
import com.oa.core.service.user.UserManagerService;
import com.oa.core.service.util.TableService;
import com.oa.core.service.work.WorkFlowLineService;
import com.oa.core.service.work.WorkFlowNodeService;
import com.oa.core.system.postposition.PostPosition;
import com.oa.core.system.workflow.ProcUtil;
import com.oa.core.util.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * @ClassName:WorkFlowPageConeroller
 * @author:zxd
 * @Date:2018/09/27
 * @Time:上午 10:47
 * @Version V1.0
 * @Explain 表单保存
 */
@Controller
    @RequestMapping("/weixin/flowpage")
public class WxFlowPageSkipController {

    @Autowired
    private UserManagerService userManagerService;
    @Autowired
    private RoleDefinesService roleDefinesService;

    @Autowired
    FormCustomMadeService formCustomMadeService;
    @Autowired
    DictionaryService dictionaryService;
    @Autowired
    TableService tableService;
    @Autowired
    TaskSenderService taskSenderService;
    @Autowired
    WorkFlowNodeService workFlowNodeService;
    @Autowired
    WorkFlowLineService workFlowLineService;


    @RequestMapping(value = "flowviewpage/{formid}", method = RequestMethod.GET)
    public String gotoViewpage(HttpServletRequest request, @PathVariable String formid) {
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        String userid = request.getParameter("userid");
        if(loginer==null || (loginer.getId()!=userid)) {
            request.getSession().setAttribute("loginer", LoginUtil.getLoginer(userid));
            request.getSession().setAttribute("role",  LoginUtil.getRole(userid));
        }
        String temp = request.getParameter("temp");
        String wkflwId = request.getParameter("wkflwId");
        WorkFlowNode node = workFlowNodeService.selectTopNode(wkflwId);
        return "redirect:/views/task/flowForm.html?userid="+userid+"&formid=" + formid + "&wkflwID=" + wkflwId + "&wkfNode=" + node.getNodeID();

    }

    @RequestMapping(value = "/flowaddsave/{formid}", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String formAddSave(HttpServletRequest request, @PathVariable("formid") String formid) {
        String userId = request.getParameter("userid");
        Map<String, Object> map = new HashMap<String, Object>();
        FormCustomMade formCustomMade = formCustomMadeService.selectFormCMByID(formid);
        String formtask = formCustomMade.getFormTask();
        TaskData taskData = dictionaryService.selectTaskName(formtask);
        String fields = taskData.getTaskField();
        String table = taskData.getTableName();
        String procId = request.getParameter("procId") == null ? "" : request.getParameter("procId");
        String nodeId = request.getParameter("nodeId") == null ? "" : request.getParameter("nodeId");
        String workOrderNO = request.getParameter("workOrderNO") == null ? "" : request.getParameter("workOrderNO");
        String wkflwId = request.getParameter("wkflwId") == null ? "" : request.getParameter("wkflwId");
        boolean modi = false;
        String recorderNO = request.getParameter(table + "_recorderNO_Value");
        if (recorderNO != null && recorderNO != "") {
            modi = true;
        } else {
            String title = null;
            if (procId == null || procId == "") {
                title = "FlowTaskB";
            }
            PrimaryKeyUitl pk = new PrimaryKeyUitl();
            recorderNO = pk.getNextId(table, "recorderNO", title, Const.PREFIXTYPE_YEAR_MONTH_DAY);
        }
        JSONObject jsonObject = new JSONObject();
        if (recorderNO == null || recorderNO == "") {
            jsonObject.put("success", 0);
            jsonObject.put("msg", "主键为空");
            return jsonObject.toString();
        }
        boolean child = true;
        try {
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
                            //value = new String(value.getBytes("iso-8859-1"), "UTF-8");
                            map.put(fieldName, value);
                        }
                    }
                }
            } else {
                jsonObject.put("success", 0);
                jsonObject.put("msg", "未找到添加字段");
                return jsonObject.toString();
            }
            map.put("workflowProcID", procId);
            map.put("workflowNodeID", nodeId);

            map.put("modifyName", userId);
            map.put("modifyTime", DateHelper.now());
            if (table != null && table != "") {
                /*
                 * 节点后置处理
                 * */
                if (false) {
                    PostPosition post = new PostPosition("form");
                    post.setProcId(procId);
                    post.setWorkOrderNO(workOrderNO);
                    post.setRecorderNO(recorderNO);
                    post.setTableMap(map);
                    boolean postposition = post.getPostPosition(wkflwId + "PostPosition");
                }
                if (modi) {
                    tableService.updateSqlMap(MySqlUtil.getUpdateSql(table, recorderNO, map, null));
                } else {
                    map.put("recorderNO", recorderNO);
                    map.put("recordName", userId);
                    map.put("recordTime", DateHelper.now());
                    if (child) {
                        tableService.insertSqlMap(MySqlUtil.getInsertSql(table, map));
                    }
                }
                JSONObject data = new JSONObject();
                data.put("recno", recorderNO);
                data.put("wkflwId", wkflwId);
                data.put("procId", procId);
                data.put("wkfNode", nodeId);
                data.put("workOrderNO", workOrderNO);
                jsonObject.put("success", 1);
                jsonObject.put("msg", "保存成功");
                jsonObject.put("data", data);
                return jsonObject.toString();
            } else {
                jsonObject.put("success", 0);
                jsonObject.put("msg", "未找到数据表");
                return jsonObject.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.put("success", 0);
            jsonObject.put("msg", "保存失败");
            return jsonObject.toString();
        }
    }

    @RequestMapping(value = "/flowformsend/{formid}", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String formSend(HttpServletRequest request, @PathVariable("formid") String formid){
        String userId = request.getParameter("userid");
        String wkflwId = request.getParameter("wkflwId");
        String recno = request.getParameter("recno");
        String procId = request.getParameter("procId");
        String wkfNode = request.getParameter("wkfNode");
        String workOrderNO = request.getParameter("workOrderNO");

        JSONObject jsonObject = new JSONObject();
        ProcUtil pu = new ProcUtil();
        try {
            ModelAndView mode = pu.WorkFlowProc(userId, formid, wkflwId, recno, procId, workOrderNO);
            String msg = (String) mode.getModel().get("msg");
            msg = StringHelper.decodeUnicode(msg);
            String actors = (String) mode.getModel().get("actors");
            String url = (String) mode.getModel().get("url");
            jsonObject.put("success", 1);
            jsonObject.put("msg", msg);
            MessageUtil.addArticle(actors,msg,url);
            return jsonObject.toString();
        }catch (Exception e){
            e.printStackTrace();
            jsonObject.put("success", 0);
            jsonObject.put("msg", "发送失败");
            return jsonObject.toString();
        }
    }
}
