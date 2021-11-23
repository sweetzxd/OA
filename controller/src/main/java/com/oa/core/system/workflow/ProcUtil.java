package com.oa.core.system.workflow;

import com.oa.core.bean.Loginer;
import com.oa.core.bean.dd.TaskData;
import com.oa.core.bean.module.Department;
import com.oa.core.bean.module.Employees;
import com.oa.core.bean.system.FormCustomMade;
import com.oa.core.bean.system.TaskSender;
import com.oa.core.bean.work.*;
import com.oa.core.helper.DateHelper;
import com.oa.core.helper.StringHelper;
import com.oa.core.listener.InitDataListener;
import com.oa.core.service.dd.DictionaryService;
import com.oa.core.service.module.DepartmentService;
import com.oa.core.service.module.EmployeesService;
import com.oa.core.service.system.FormCustomMadeService;
import com.oa.core.service.system.TaskSenderService;
import com.oa.core.service.util.TableService;
import com.oa.core.service.work.*;
import com.oa.core.system.postposition.PostPosition;
import com.oa.core.tag.UserDict;
import com.oa.core.util.*;
import org.json.JSONArray;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.*;

/**
 * @ClassName:ProcUtil
 * @author:zxd
 * @Date:2018/10/19
 * @Time:下午 2:40
 * @Version V1.0
 * @Explain 流程发出方法
 */
public class ProcUtil {
    private TableService tableService = (TableService) SpringContextUtil.getBean("tableService");
    private TaskSenderService taskSenderService = (TaskSenderService) SpringContextUtil.getBean("taskSenderService");
    private FormCustomMadeService formCustomMadeService = (FormCustomMadeService) SpringContextUtil.getBean("formCustomMadeService");
    private DictionaryService dictionaryService = (DictionaryService) SpringContextUtil.getBean("dictionaryService");

    private EmployeesService employeesService  = (EmployeesService) SpringContextUtil.getBean("employeesService");
    private DepartmentService departmentService = (DepartmentService) SpringContextUtil.getBean("departmentService");

    private WorkFlowNodeService workFlowNodeService = (WorkFlowNodeService) SpringContextUtil.getBean("workFlowNodeService");
    private WorkFlowLineService workFlowLineService = (WorkFlowLineService) SpringContextUtil.getBean("workFlowLineService");
    private WorkFlowDefineService workFlowDefineService = (WorkFlowDefineService) SpringContextUtil.getBean("workFlowDefineService");
    private WorkFlowProcService workFlowProcService = (WorkFlowProcService) SpringContextUtil.getBean("workFlowProcService");
    private WkflwNodeActorService wkflwNodeActorService = (WkflwNodeActorService) SpringContextUtil.getBean("wkflwNodeActorService");
    private WorkFlowLogService workFlowLogService = (WorkFlowLogService) SpringContextUtil.getBean("workFlowLogService");

    public ModelAndView WorkFlowProc(HttpServletRequest request) {
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        String userId = loginer.getId() == null ? "" : loginer.getId();
        ModelAndView mode = new ModelAndView("redirect:/flowpage/tasksendmsg.do");
        mode.addObject("type", "flow");
        try {
            String formid = request.getParameter("formid") == null ? "" : request.getParameter("formid");//表单页面
            String wkflwId = request.getParameter("wkflwId") == null ? "" : request.getParameter("wkflwId");//流程定义号
            String recno = request.getParameter("recorderNO") == null ? "" : request.getParameter("recorderNO");//表主键
            String procid = request.getParameter("workflowProcID") == null ? "" : request.getParameter("workflowProcID");//流程运行号
            String workOrderNO = request.getParameter("workOrderNO") == null ? "" : request.getParameter("workOrderNO");//任务主键
            //查当前任务
            TaskSender task = taskSenderService.selectById(workOrderNO);

            if (task != null) {
                int msgStatus = task.getMsgStatus();
                if (msgStatus == 0) {
                    mode.addObject("msg", StringHelper.gbEncoding("该任务已删除"));
                    return mode;
                } else if (msgStatus == 4) {
                    mode.addObject("msg", StringHelper.gbEncoding("该任务已处完毕"));
                    return mode;
                } else if (msgStatus == 5) {
                    mode.addObject("msg", StringHelper.gbEncoding("该任务已暂停"));
                    return mode;
                }
            }
            /*
             * 获取当前节点信息
             * */
            WorkFlowNode node = new WorkFlowNode();
            node.setWkflwID(wkflwId);
            node.setFormId(formid);
            List<WorkFlowNode> nodelist = workFlowNodeService.selectTerms(node);
            node = nodelist.get(0);
            String nodeid = node.getNodeID();
            int isORAction = node.getIsORAction();
            /*
             * 获取开始结束节点
             * */
            List<String> sn = workFlowLineService.selectSN(wkflwId);
            String starnode = sn.get(0);

            /*
             * 首节点判定
             * */
            boolean star = starnode.equals(nodeid);
            if (star) {
                procid = recno;
            }

            /*
             * 获取当前节点表内数据
             * */
            FormCustomMade formCustomMade = formCustomMadeService.selectFormCMByID(formid);
            String formtask = formCustomMade.getFormTask();
            TaskData taskData = dictionaryService.selectTaskName(formtask);
            String fields = taskData.getTaskField();
            String table = taskData.getTableName();
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("workflowProcID", procid);
            tableService.updateSqlMap(MySqlUtil.getUpdateSql(table, recno, map, null));
            List<String> list = Arrays.asList(fields.split(";"));
            List<String> field = new ArrayList<String>(list);
            List<String> where = new ArrayList<>();
            where.add("recorderNO='" + recno + "'");
            String sql = MySqlUtil.getSql(field, table, where);
            Map<String, Object> sqlvalue = tableService.selectSqlMap(sql);
            /*
             * 获取首节点表内数据
             * */
            Map<String, Object> starVal = null;
            if (star) {
                starVal = sqlvalue;
            } else {
                WorkFlowNode workFlowNode = workFlowNodeService.selectById(starnode);
                List<String> w = new ArrayList<String>();
                w.add("workflowProcID='" + procid + "'");
                starVal = getVal(workFlowNode.getFormId(), w);
            }

            /*
             * 获取流程定义表内数据
             * */
            WorkFlowDefine wkf = workFlowDefineService.selectById(wkflwId);
            String flowLab = wkf.getFlowLabFld();
            if (flowLab == null || flowLab.length() <= 0) {
                String name = UserDict.getName((String) starVal.get("recordName"));
                flowLab = "处理由【" + name + "】发起的【" + wkf.getWkfName() + "】流程";
            } else {
                List<String> msg = StringHelper.getMsg(flowLab);
                for (String aMsg : msg) {
                    String[] re = aMsg.split("-");
                    List<String> l = new ArrayList<String>();
                    l.add(re[1]);
                    List<String> w = new ArrayList<String>();
                    w.add("workflowProcID='" + procid + "'");
                    String s = MySqlUtil.getSql(l, re[0], w);
                    Map<String, Object> sqlv = tableService.selectSqlMap(s);
                    if(sqlv!=null){
                    String value = (String) sqlv.get(re[1]);
                    value = ToNameUtil.idToName(re[1], value);
                    flowLab = flowLab.replaceAll(aMsg, value);
                    }
                }
            }
            /*
             * 节点后置处理
             * */
            String ss = wkf.getSpecialField();
            if (ss != null && ss.indexOf("<flownode>") >= 0) {
                PostPosition post = new PostPosition("flwo");
                post.setWkflwId(wkflwId);
                post.setProcId(procid);
                post.setWorkOrderNO(workOrderNO);
                post.setRecorderNO(recno);
                post.setTableMap(sqlvalue);
                boolean postposition = post.getPostPosition(wkflwId + "PostPosition");
            }
            /*
             * 获取下一节点信息
             * */
            NodeUtil nodeUtil = new NodeUtil();
            List<String> nextNodeIds = nodeUtil.getNextNode(node, procid, workOrderNO);
            String msg = "";
            Vector<String> flowpeoples = null;
            boolean wkflog = true;
            String nextUser = "";
            for (int i = 0; i < nextNodeIds.size(); i++) {
                String nextNodeId = nextNodeIds.get(i);
                boolean end = nextNodeId.equals("endNode");
                WorkFlowNode nextNode = null;
                if (!end) {
                    nextNode = workFlowNodeService.selectById(nextNodeId);
                }
                /*
                 * 获取下一节点执行人员
                 * */
                WorkFlowProc workFlowProc = workFlowProcService.selectById(procid,wkflwId);
                if (workFlowProc != null) {
                    try {
                        flowpeoples = StringHelper.string2Vector(workFlowProc.getFlowpeoples(), ";");
                    } catch (Exception e) {

                    }
                }
                String actors = null;
                if (!end) {
                    if (nextNodeId.equals(starnode)) {
                        actors = workFlowProc.getOriginator();
                    } else {
                        actors = getNodeUser(nextNodeId, procid);
                    }
                    if (actors == null || actors.length() <= 0) {
                        msg = "找不到下一节点任务执行人;\n";
                        wkflog = false;
                        break;
                    }
                }
                /*
                 * 是否进行下一节点
                 * */

                WorkFlowLine line = new WorkFlowLine();
                line.setFromNode(nodeid);
                line.setToNode(nextNodeId);
                List<WorkFlowLine> linelist = workFlowLineService.selectTerms(line);
                line = linelist.get(0);
                /*
                 * 多人判断条件方式（1：只需一人的填写符合本条件；2：需所有人的填写符合本条件）
                 */
                int logTypeOfByMultActor = line.getLogTypeOfByMultActor();
                /*
                 * 是退回/返回原发件人的路径
                 */
                int isRebackPath = line.getIsRebackPath();
                /*
                 * 所有人提交后才开始判断处理
                 */
                int judgeAfterAll = line.getJudgeAfterAll();
                /*
                 * 路径通时对其他同批任务的处理（0：撤消其他人的任务；1：其他人的任务继续）
                 */
                int path1ToOtherTask = line.getPath1ToOtherTask();
                /**
                 * 是否是并行路径
                 */
                int isAsynchPath = line.getIsAsynchPath();
                /**
                 * 等待同节点他人执行
                 */
                boolean determine = false;
                /**
                 * 等待并行节点他人执行
                 */
                boolean isAsynch = false;
                /*
                 * 处理当前节点任务,判定节点是否为一人执行
                 * 如果一人执行则将其他人任务状态改为0
                 * */
                TaskSender ts = new TaskSender();
                ts.setWorkOrderNO(workOrderNO);
                ts.setMsgStatus(4);
                ts.setAccepter(userId);
                taskSenderService.update(ts);
                if (path1ToOtherTask == 0) {
                    Map tmap = new HashMap();
                    tmap.put("wkflwId", wkflwId);
                    tmap.put("procId", procid);
                    tmap.put("wkfNode", nodeid);
                    tmap.put("workOrderNO", workOrderNO);
                    taskSenderService.cleanTask(tmap);
                }
                ts = new TaskSender();
                ts.setProcID(procid);
                ts.setWkfNode(nodeid);
                List<TaskSender> taskSender = taskSenderService.select4MsgStatus(ts);
                if (taskSender != null && taskSender.size() > 0) {
                    if (logTypeOfByMultActor == 1 && path1ToOtherTask == 0) {
                        ts = new TaskSender();
                        ts.setProcID(procid);
                        ts.setWkfNode(nodeid);
                        ts.setModifyName(userId);
                        ts.setModifyTime(DateHelper.now());
                        taskSenderService.update0MsgStatus(ts);
                        determine = true;
                        if (isAsynchPath == 1) {
                            isAsynch = isAsynchPath(procid, nodeid, nextNodeId);
                        }
                    }
                    if (logTypeOfByMultActor == 2) {
                        determine = false;
                    }
                } else {
                    determine = true;
                    if (isAsynchPath == 1) {
                        isAsynch = isAsynchPath(procid, nodeid, nextNodeId);
                    }
                }
                /*
                 * 如果是首节点
                 * TaskSender,workflowProc,workflowLog
                 * 需要添加新数据
                 */
                if (determine) {
                    if (star) {
                        if (workFlowProc != null) {
                            if (updateProc(procid,wkflwId, userId, flowpeoples, nextNodeId, actors, 1)) {
                                msg = "任务发送失败[cod:1]";
                                wkflog = false;
                                break;
                            }
                        } else {
                            if (insertProc(recno, wkflwId, userId, nextNodeId, starnode, actors)) {
                                msg = "任务发送失败[cod:1]";
                                wkflog = false;
                                break;
                            }
                        }
                    } else {
                        if (end) {
                            if (updateProc(procid,wkflwId, userId, flowpeoples, nextNodeId, actors, 2, nodeid)) {
                                msg = "任务发送失败[cod:1]";
                                wkflog = false;
                                break;
                            }
                        } else {
                            if (updateProc(procid,wkflwId, userId, flowpeoples, nextNodeId, actors, 1)) {
                                msg = "任务发送失败[cod:1]";
                                wkflog = false;
                                break;
                            }
                        }

                    }
                    if (end) {
                        if (!isAsynch) {
                            if (ss != null && ss.indexOf("<workflow>") >= 0) {
                                PostPosition post = new PostPosition();
                                boolean postposition = post.getPostPosition("Flow" + wkflwId, workOrderNO, procid, recno);
                            }
                            if (ss != null && ss.indexOf("<account>") >= 0) {
                                AccountUtil account = new AccountUtil();
                                account.insertTable(userId,wkflwId,procid);
                            }
                            String name = UserDict.getName((String) starVal.get("recordName"));
                            msg = "关于 " + name + " 发起的【" + wkf.getWkfName() + "】流程已结束。";
                        } else {
                            msg = "等待并行节点任务处理";
                        }
                    } else {
                        if (!isAsynch) {
                            if (insertTask(wkflwId, recno, procid, nextNodeId, flowLab, userId, actors)) {
                                msg = "任务发送失败[cod:2]";
                                wkflog = false;
                                break;
                            } else {
                                String name = UserDict.getNames(actors, ";");
                                String title = nextNode.getNodeTitle();
                                msg += "关于流程【" + wkf.getWkfName() + "】的任务已发出给节点“" + title + "”的" + name + "执行<br/>";
                                LogUtil.toHtml("mymsg", StringHelper.string2Vector(actors, ";"), flowLab);
                            }
                        } else {
                            msg = "关于流程【" + wkf.getWkfName() + "】的任务已处理完成，等待并行节点任务处理。";
                        }

                    }
                } else {
                    msg = "关于流程【" + wkf.getWkfName() + "】的任务已处理完成，等待节点他人任务处理。";
                }
                LogUtil.toEmail(StringHelper.string2Vector(actors, ";"), flowLab);
                nextUser+=actors;
            }
            String url = "/task/tasksendpage.do?procId="+procid+"&wkflwId="+wkflwId+"&nodeId="+nodeid+"&workOrderNO="+workOrderNO;
            MessageUtil.addArticle(nextUser,msg,url);
            /*
             * 插入流程日志
             * */
            if(wkflog) {
                insertLog(userId, wkflwId, procid, node, "", workOrderNO, formtask, table, sqlvalue);
            }
            mode.addObject("msg", StringHelper.gbEncoding(msg));
        } catch (Exception e) {
            LogUtil.sysLog("Exception:" + e.getMessage());
            e.printStackTrace();
            mode.addObject("msg", StringHelper.gbEncoding("任务发送失败[cod:0]"));
        }
        return mode;
    }

    public Map<String, Object> getVal(String formid, List<String> where) {
        FormCustomMade formCustomMade = formCustomMadeService.selectFormCMByID(formid);
        String formtask = formCustomMade.getFormTask();
        TaskData taskData = dictionaryService.selectTaskName(formtask);
        String fields = taskData.getTaskField();
        String table = taskData.getTableName();
        List<String> list = Arrays.asList(fields.split(";"));
        List<String> field = new ArrayList<String>(list);
        String sql = MySqlUtil.getSql(field, table, where);
        return tableService.selectSqlMap(sql);
    }

    public String getNodeUser(String nextNodeId, String procid) {
        List<WkflwNodeActor> actorlist = wkflwNodeActorService.selectByNodeId(nextNodeId);
        String actors = "";
        if (actorlist != null && actorlist.size() > 0) {
            for (WkflwNodeActor actor : actorlist) {
                String type = actor.getWkfActorType();
                if (type.equals("nodeactor")) {
                    String[] contex = actor.getContextValue().split("_");
                    List<String> f = new ArrayList<String>();
                    f.add(contex[2]);
                    List<String> w = new ArrayList<String>();
                    w.add("workflowProcID='" + procid + "'");
                    String s = MySqlUtil.getSql(f, contex[1], w);
                    Map<String, Object> sqlv = tableService.selectSqlMap(s);
                    String val = (String) sqlv.get(contex[2]);
                    if(contex.length>3 ) {
                        if("dept".equals(contex[3])) {
                            actors += getDeptUser(val);
                        }else if("user".equals(contex[3]) || "users".equals(contex[3])){
                            actors += getEmpUser(val);
                        }
                    } else {
                        actors += getEmpUser(val);
                    }
                } else if (type.equals("roleactor")) {
                    Hashtable<String, String> hs = InitDataListener.getMapData("role");
                    String contex = actor.getContextValue();
                    if (hs != null) {
                        actors += hs.get(contex);
                    }
                } else if (type.equals("useractor")) {
                    actors += actor.getContextValue() + ";";
                } else if (type.equals("deptactor")){
                    String[] contex = actor.getContextValue().split("_");
                    Department department = departmentService.selectById(contex[0]);
                    if("deptHead".equals(contex[1])) {
                        actors += department.getDeptHead();
                    }
                }
            }
        }
        return actors;
    }

    public String getEmpUser(String user){
        String actors = "";
        if (user != null && user.substring(user.length() - 1).equals(";")) {
            actors += user;
        } else if (user != null) {
            actors += user + ";";
        }
        return actors;
    }
    public String getDeptUser(String dept){
        String actors = "";
        if (dept != null ) {
            if(dept.substring(dept.length() - 1).equals(";")){
                dept = dept.replaceAll(";","','");
            }
            String sql = "select deptHead from (select deptHead from  department where curStatus=2 and deptId in ('"+dept+"') UNION select deptHead from department d join employees e on d.deptId=e.department where e.userName in ('"+dept+"')) a GROUP BY deptHead";
            List<String> duser = tableService.selectSql(sql);
            actors = StringHelper.list2String(duser,";");
        }
        return actors;
    }

    public boolean insertTask(String wkflwId, String recno, String procid, String nextNodeId, String flowLab, String userId, String actors) {
        PrimaryKeyUitl pk = new PrimaryKeyUitl();
        Timestamp datetime = DateHelper.now();
        TaskSender ts = new TaskSender();
        ts.setWkflwID(wkflwId);
        ts.setProcID(procid);
        ts.setWkfNode(nextNodeId);
        ts.setMsgStatus(1);
        ts.setTaskTitle(flowLab);
        ts.setRecordName(userId);
        ts.setRecordTime(datetime);
        ts.setModifyName(userId);
        ts.setModifyTime(datetime);
        String[] accepters = actors.split(";");

        try {
            for (int i = 0; i < accepters.length; i++) {
                String workOrderNO = pk.getNextId("tasksender", "WorkOrderNO");
                ts.setWorkOrderNO(workOrderNO);
                ts.setRefLinkUrl("/task/tasksendpage.do?procId=" + procid + "&wkflwId=" + wkflwId + "&nodeId=" + nextNodeId + "&workOrderNO=" + workOrderNO);
                ts.setAccepter(accepters[i]);
                taskSenderService.insert(ts);
            }
            return false;
        } catch (Exception e) {
            return true;
        }
    }

    public boolean insertProc(String recno, String wkflwId, String userId, String nextNodeId, String starnode, String actors) {
        Timestamp datetime = DateHelper.now();
        WorkFlowProc wfproc = new WorkFlowProc();
        wfproc.setProcId(recno);
        wfproc.setWkflwID(wkflwId);
        wfproc.setOriginator(userId);
        wfproc.setFlowpeoples(userId + ";");
        wfproc.setWkfStatus(1);
        wfproc.setCurNodeID(nextNodeId);
        wfproc.setNodespeople(actors);
        wfproc.setStartNodeID(starnode);
        wfproc.setRecordName(userId);
        wfproc.setRecordTime(datetime);
        wfproc.setModifyName(userId);
        wfproc.setModifyTime(datetime);
        try {
            workFlowProcService.insert(wfproc);
            return false;
        } catch (Exception e) {
            return true;
        }
    }

    public boolean updateProc(String procid,String wkflwId, String userId, Vector<String> flowpeoples, String nextNodeId, String actors, int wkfStatus) {
        return updateProc(procid,wkflwId, userId, flowpeoples, nextNodeId, actors, wkfStatus, null);
    }

    public boolean updateProc(String procid,String wkflwId, String userId, Vector<String> flowpeoples, String nextNodeId, String actors, int wkfStatus, String endNode) {
        Timestamp datetime = DateHelper.now();
        WorkFlowProc wfproc = new WorkFlowProc();
        wfproc.setProcId(procid);
        wfproc.setWkflwID(wkflwId);
        if (flowpeoples != null && !flowpeoples.contains(userId)) {
            flowpeoples.add(userId);
        }
        wfproc.setFlowpeoples(StringHelper.vector2String(flowpeoples, ";"));
        if (endNode != null && !endNode.equals("")) {
            wfproc.setEndNodeID(endNode);
        }
        wfproc.setWkfStatus(wkfStatus);
        wfproc.setCurNodeID(nextNodeId);
        wfproc.setNodespeople(actors);
        wfproc.setModifyTime(datetime);
        try {
            workFlowProcService.update(wfproc);
            return false;
        } catch (Exception e) {
            return true;
        }
    }

    public boolean insertLog(String userId, String wkflowId, String procId, WorkFlowNode node, String exception, String msgId, String formTask, String table, Map<String, Object> nodeTabVal) {
        String parmans = LogUtil.getFlowTableLog(formTask, node.getNodeTitle(), table, nodeTabVal);
        PrimaryKeyUitl pk = new PrimaryKeyUitl();
        String wkflogId = pk.getNextId("WorkFlowLog", "wkflogId");
        WorkFlowLog workFlowLog = new WorkFlowLog();
        workFlowLog.setWkflogId(wkflogId);
        workFlowLog.setWkflowId(wkflowId);
        workFlowLog.setProcId(procId);
        workFlowLog.setNodeAction("运行节点");
        workFlowLog.setNodeID(node.getNodeID());
        workFlowLog.setInParams(parmans);
        workFlowLog.setSuccess(1);
        workFlowLog.setReason(exception);
        workFlowLog.setMsgID(msgId);
        workFlowLog.setRecordName(userId);
        workFlowLog.setRecordTime(DateHelper.now());
        workFlowLog.setModifyName(userId);
        workFlowLog.setModifyTime(DateHelper.now());
        workFlowLogService.insert(workFlowLog);
        return true;
    }

    public boolean isAsynchPath(String procid, String nodeId, String nextNodeId) {
        List<String> nodeIds = workFlowLineService.selectAsynch(nextNodeId);
        String taskNode = "'" + StringHelper.listToString(nodeIds, "','") + "'";
        Map<String, String> maps = new HashMap<>();
        maps.put("procId", procid);
        maps.put("wkfNode", taskNode);
        int num = taskSenderService.selectNextTask(maps);
        if (num > 0) {
            return true;
        } else {
            return false;
        }
    }

    public String getMenu() {
        String[] ptitle = new String[]{"人力资源", "综合办公", "资产财务"};
        List<Map<String, Object>> lmap = new ArrayList<Map<String, Object>>();
        Map<String, Object> maps = new HashMap<String, Object>();
        for (int n = 0; n < ptitle.length; n++) {
            maps = new HashMap<String, Object>();
            maps.put("name", ptitle[n]);
            maps.put("spread", true);
            maps.put("alias", ptitle[n]);
            maps.put("menuNum", 1);
            maps.put("children", getNext(ptitle[n]));
            lmap.add(maps);
        }
        JSONArray json = new JSONArray(lmap);
        return json.toString();
    }

    public List<Map<String, Object>> getNext(String superiorDeptId) {
        WorkFlowDefine nextWkflw = new WorkFlowDefine();
        nextWkflw.setWkfType(superiorDeptId);
        List<WorkFlowDefine> flowlist = workFlowDefineService.selectTerms(nextWkflw);
        List<Map<String, Object>> lmap = new ArrayList<Map<String, Object>>();
        for (int n = 0; n < flowlist.size(); n++) {
            WorkFlowDefine dept = flowlist.get(n);
            String id = dept.getWkflwID();
            String title = dept.getWkfName();
            Map<String, Object> maps = new HashMap<String, Object>();
            maps.put("id", id);
            maps.put("name", title);
            maps.put("spread", true);
            maps.put("alias", title);
            maps.put("menuNum", 2);
            lmap.add(maps);
        }
        return lmap;
    }
}
