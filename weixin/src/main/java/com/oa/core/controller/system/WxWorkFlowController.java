package com.oa.core.controller.system;

import com.oa.core.bean.dd.TaskData;
import com.oa.core.bean.system.FormCustomMade;
import com.oa.core.bean.system.TaskSender;
import com.oa.core.bean.work.WorkFlowLog;
import com.oa.core.bean.work.WorkFlowNode;
import com.oa.core.service.dd.DictionaryService;
import com.oa.core.service.system.FormCustomMadeService;
import com.oa.core.service.system.TaskSenderService;
import com.oa.core.service.util.TableService;
import com.oa.core.service.work.WorkFlowLogService;
import com.oa.core.service.work.WorkFlowNodeService;
import com.oa.core.util.MySqlUtil;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @ClassName:WorkFlowController
 * @author:zxd
 * @Date:2018/09/21
 * @Time:上午 10:34
 * @Version V1.0
 * @Explain 流程定制logpage
 */
@Controller
@RequestMapping("/weixin/workflow")
public class WxWorkFlowController {

    @Autowired
    WorkFlowLogService workFlowLogService;
    @Autowired
    TaskSenderService taskSenderService;
    @Autowired
    WorkFlowNodeService workFlowNodeService;
    @Autowired
    FormCustomMadeService formCustomMadeService;
    @Autowired
    DictionaryService dictionaryService;
    @Autowired
    TableService tableService;

    @RequestMapping(value = "/logpage", method = RequestMethod.GET)
    @ResponseBody
    public String logPage(HttpServletRequest request, String procId,String wkflwID) {
        JSONObject jsonObject = new JSONObject();
        try{
            List<WorkFlowLog> wkflwlog = workFlowLogService.selectByProcId(procId,wkflwID);
            jsonObject.put("wkflwlog",wkflwlog);
            jsonObject.put("success","true");
        }catch (Exception e){
            e.printStackTrace();
            jsonObject.put("success","false");
        }
        return jsonObject.toString();
    }
    @RequestMapping(value = "/gototask", method = RequestMethod.GET)
    @ResponseBody
    public String gotoTask(HttpServletRequest request,String procId,String wkflwID,String wkfNode,String workOrderNO) {
        String userid = request.getParameter("userid");
        JSONObject jsonObject = new JSONObject();
        String url = "";
        try{
            TaskSender ts = new TaskSender();
            ts.setWorkOrderNO(workOrderNO);
            ts.setMsgStatus(3);
            taskSenderService.update(ts);
            TaskSender taskSender = taskSenderService.selectById(workOrderNO);
            WorkFlowNode workFlowNode = workFlowNodeService.selectById(wkfNode);
            String formid = workFlowNode.getFormId();
            FormCustomMade formCustomMade = formCustomMadeService.selectFormCMByID(formid);
            String formTask = formCustomMade.getFormTask();
            String title = formCustomMade.getFormcmTitle();
            TaskData taskData = dictionaryService.selectTaskName(formTask);
            String fields = taskData.getTaskField();
            String tableId = taskData.getTableName();
            List<String> list = Arrays.asList(fields.split(";"));
            List<String> field = new ArrayList<>(list);
            field.add("workflowProcID");
            field.add("workflowNodeID");
            List<String> where = new ArrayList<>();
            where.add("workflowProcID='" + procId + "'");
            where.add("recordName='" + taskSender.getAccepter() + "'");
            String sql = MySqlUtil.getSql(field, tableId, where);
            Map<String, Object> sqlvalue = tableService.selectSqlMap(sql);
            jsonObject.put("success",1);
            jsonObject.put("userid", userid);
            jsonObject.put("title", title);
            String recno = "";
            if(sqlvalue!=null){
                recno = (String)sqlvalue.get("recorderNO");
            }
            url = "/views/task/flowForm.html?userid="+userid+"&formid="+formid+"&procId="+procId+"&wkflwID="+wkflwID+"&wkfNode="+wkfNode+"&workOrderNO="+workOrderNO+"&recno="+recno;
            jsonObject.put("url",url);
        }catch (Exception e){
            e.printStackTrace();
            jsonObject.put("success",0);
        }

        return jsonObject.toString();
    }
}
