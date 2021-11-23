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
import com.oa.core.util.ToNameUtil;
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
            if(wkflwID.equals("swlc2019050900001")){
                url = "/views/task/taskshouwen.html?userid="+userid+"&formid="+formid+"&procId="+procId+"&wkflwID="+wkflwID+"&wkfNode="+wkfNode+"&workOrderNO="+workOrderNO+"&recno="+recno;
            }else if(wkflwID.equals("fwlc2019050900001")){
                url = "/views/task/taskfawen.html?userid="+userid+"&formid="+formid+"&procId="+procId+"&wkflwID="+wkflwID+"&wkfNode="+wkfNode+"&workOrderNO="+workOrderNO+"&recno="+recno;
            }else {
                url = "/views/task/flowForm.html?userid=" + userid + "&formid=" + formid + "&procId=" + procId + "&wkflwID=" + wkflwID + "&wkfNode=" + wkfNode + "&workOrderNO=" + workOrderNO + "&recno=" + recno;
            }
            jsonObject.put("url",url);
        }catch (Exception e){
            e.printStackTrace();
            jsonObject.put("success",0);
        }

        return jsonObject.toString();
    }

    @RequestMapping(value = "/gotosplc", method = RequestMethod.GET)
    @ResponseBody
    public String gotoSplc(HttpServletRequest request,String procId,String wkflwID,String wkfNode,String workOrderNO) {
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
            String recno="";
            if(sqlvalue!=null){
                recno = (String)sqlvalue.get("recorderNO");
            }
            boolean aa=true;
            List<Map<String, Object>> listjil=new ArrayList<>();
            if(aa){
                String sqlbgs="SELECT spyjs19050904 as  yijian ,recordName as spname,DATE_FORMAT(recordTime, '%Y-%m-%d %h:%i:%s') as recordTime from bgssp19050901 where workflowProcID='" + procId + "'";
                Map<String, Object> mapjilu = tableService.selectSqlMap(sqlbgs);
                if(mapjilu!=null&&mapjilu.size()>0){
                    mapjilu.put("spname", ToNameUtil.getName("user", mapjilu.get("spname")));
                    listjil.add(mapjilu);
                }
                String sqlld="SELECT spyjs19050906 as  yijian,recordName as spname ,DATE_FORMAT(recordTime, '%Y-%m-%d %h:%i:%s') as recordTime  from zgyld19050901 where  workflowProcID='" + procId + "'";
                Map<String, Object> mapjiluld = tableService.selectSqlMap(sqlld);
                if(mapjiluld!=null&&mapjiluld.size()>0){
                    mapjiluld.put("spname", ToNameUtil.getName("user", mapjiluld.get("spname")));
                    listjil.add(mapjiluld);
                }
                String sqlyz="SELECT spyjs19050907 as  yijian ,recordName as spname,DATE_FORMAT(recordTime, '%Y-%m-%d %h:%i:%s') as recordTime from yzsp190509001 where workflowProcID='" + procId + "'";
                Map<String, Object> mapjiluyz = tableService.selectSqlMap(sqlyz);
                if(mapjiluyz!=null&&mapjiluyz.size()>0){
                    mapjiluyz.put("spname", ToNameUtil.getName("user", mapjiluyz.get("spname")));
                    listjil.add(mapjiluyz);
                }
                String sqlcb="SELECT cbyj190509001 as  yijian ,recordName as spname,DATE_FORMAT(recordTime, '%Y-%m-%d %h:%i:%s') as recordTime from cb19050900002 where workflowProcID='" + procId + "'";
                Map<String, Object> mapjilucb = tableService.selectSqlMap(sqlcb);
                if(mapjilucb!=null&&mapjilucb.size()>0){
                    mapjilucb.put("spname", ToNameUtil.getName("user", mapjilucb.get("spname")));
                    listjil.add(mapjilucb);
                }
                String sqlfb="SELECT fbyj190509001 as  yijian ,recordName as spname,DATE_FORMAT(recordTime, '%Y-%m-%d %h:%i:%s') as recordTime from fb19050900002 where workflowProcID='" + procId + "'";
                Map<String, Object> mapjilufb = tableService.selectSqlMap(sqlfb);
                if(mapjilufb!=null&&mapjilufb.size()>0){
                    mapjilufb.put("spname", ToNameUtil.getName("user", mapjilufb.get("spname")));
                    listjil.add(mapjilufb);
                }
            }else {
                String sqlbgsfw="SELECT spyjs19050901 as  yijian ,recordName as spname,DATE_FORMAT(recordTime, '%Y-%m-%d %h:%i:%s') as recordTime from bzps190509001 where workflowProcID='" + procId + "'";
                Map<String, Object> mapjilufw = tableService.selectSqlMap(sqlbgsfw);
                if(mapjilufw!=null&&mapjilufw.size()>0){
                    mapjilufw.put("spname", ToNameUtil.getName("user", mapjilufw.get("spname")));
                    listjil.add(mapjilufw);
                }
                String sqlldfw="SELECT spyjs19050902 as  yijian,recordName as spname ,DATE_FORMAT(recordTime, '%Y-%m-%d %h:%i:%s') as recordTime  from zgldp19050901 where  workflowProcID='" + procId + "'";
                Map<String, Object> mapjilufwld = tableService.selectSqlMap(sqlldfw);
                if(mapjilufwld!=null&&mapjilufwld.size()>0){
                    mapjilufwld.put("spname", ToNameUtil.getName("user", mapjilufwld.get("spname")));
                    listjil.add(mapjilufwld);
                }

                String sqlyzfw="SELECT spyjs19050903 as  yijian ,recordName as spname,DATE_FORMAT(recordTime, '%Y-%m-%d %h:%i:%s') as recordTime from yzps190509001 where workflowProcID='" + procId + "'";
                Map<String, Object> mapjiluyzfw = tableService.selectSqlMap(sqlyzfw);
                if(mapjiluyzfw!=null&&mapjiluyzfw.size()>0){
                    mapjiluyzfw.put("spname", ToNameUtil.getName("user", mapjiluyzfw.get("spname")));
                    listjil.add(mapjiluyzfw);
                }
                String sqlfbfw="SELECT fbyjs19050902 as  yijian ,recordName as spname,DATE_FORMAT(recordTime, '%Y-%m-%d %h:%i:%s') as recordTime from fb19050900004 where workflowProcID='" + procId + "'";
                Map<String, Object> mapjilufwfb = tableService.selectSqlMap(sqlfbfw);

                if(mapjilufwfb!=null&&mapjilufwfb.size()>0){
                    mapjilufwfb.put("spname", ToNameUtil.getName("user", mapjilufwfb.get("spname")));
                    listjil.add(mapjilufwfb);
                }
                String sqlcbfw="SELECT cbyjs19050902 as  yijian ,recordName as spname,DATE_FORMAT(recordTime, '%Y-%m-%d %h:%i:%s') as recordTime from cb19050900003 where workflowProcID='" + procId + "'";
                Map<String, Object> mapjilucbfw = tableService.selectSqlMap(sqlcbfw);
                if(mapjilucbfw!=null&&mapjilucbfw.size()>0) {
                    mapjilucbfw.put("spname", ToNameUtil.getName("user", mapjilucbfw.get("spname")));
                    listjil.add(mapjilucbfw);
                }
            }
            String  wheres = " and t.wkflwID='"+wkflwID+"'";
            String sqllog = "select wkfNode,staffName,nodeTitle,name as nmname,DATE_FORMAT(t.recordTime,'%Y-%m-%d %h:%i:%s') as recordTime from tasksender t join userinfo u on t.accepter=u.userName join workflowNode w on t.wkfNode=w.nodeId join usermanager m on m.username=t.recordName where  t.curStatus=2 and t.procID='" + procId + "' " + wheres + " and t.msgStatus>0 and t.msgStatus<5 order by t.recordTime asc";
            List<Map<String, Object>> maps2 = tableService.selectSqlMapList(sqllog);
            jsonObject.put("success",1);
            jsonObject.put("log", maps2);
            if(wkflwID.equals("swlc2019050900001")){
                url = "/views/task/splcshouwen.html?userid="+userid+"&formid="+formid+"&procId="+procId+"&wkflwID="+wkflwID+"&wkfNode="+wkfNode+"&workOrderNO="+workOrderNO+"&recno="+recno;

            }else if(wkflwID.equals("fwlc2019050900001")){
                url = "/views/task/splcfawen.html";
            }
            jsonObject.put("url",url);
            jsonObject.put("listjil",listjil);
        }catch (Exception e){
            e.printStackTrace();
            jsonObject.put("success",0);
        }

        return jsonObject.toString();
    }

}
