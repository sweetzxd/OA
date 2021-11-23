package com.oa.core.tag;

import com.oa.core.bean.dd.FieldData;
import com.oa.core.bean.dd.TableData;
import com.oa.core.bean.module.Department;
import com.oa.core.bean.system.RoleDefines;
import com.oa.core.bean.user.UserManager;
import com.oa.core.bean.work.WkflwNodeActor;
import com.oa.core.bean.work.WorkFlowDefine;
import com.oa.core.bean.work.WorkFlowNode;
import com.oa.core.helper.StringHelper;
import com.oa.core.service.dd.DictionaryService;
import com.oa.core.service.module.DepartmentService;
import com.oa.core.service.system.RoleDefinesService;
import com.oa.core.service.user.UserManagerService;
import com.oa.core.service.work.WkflwNodeActorService;
import com.oa.core.service.work.WorkFlowDefineService;
import com.oa.core.service.work.WorkFlowNodeService;
import com.oa.core.util.LogUtil;
import com.oa.core.util.SpringContextUtil;
import com.oa.core.util.ToNameUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName:FlowDict
 * @author:zxd
 * @Date:2018/10/12
 * @Time:下午 2:22
 * @Version V1.0
 * @Explain 流程中id转name的工具类
 */
public class FlowDict {


    /**
    * @method: getFlowName
    * @param: value 流程id
    * @return: 流程name
    * @author: zxd
    * @date: 2019/03/02
    * @description: 流程ID转name
    */
    public static String getFlowName(String value){
        try {
            WorkFlowDefineService workFlowDefineService = (WorkFlowDefineService) SpringContextUtil.getBean("workFlowDefineService");
            WorkFlowDefine workFlowDefine = workFlowDefineService.selectById(value);
            return workFlowDefine.getWkfName();
        } catch (Exception e) {
            LogUtil.sysLog("Exception:" + e);
            return value;
        }
    }
    /**
     * 流程消息发送 id 转 name
     *
     * @ param 线条件id
     * @ return name
     */
    public static String getFlowLabFldData(String value) {
        try {
            List<String> msgList = StringHelper.getMsg(value);
            String text = value;
            for (String msg : msgList) {
                String[] p = msg.split("_");
                DictionaryService dictionaryService = (DictionaryService) SpringContextUtil.getBean("dictionaryService");
                TableData tableData = dictionaryService.selectTableName(p[0]);
                FieldData fieldData = dictionaryService.selectFieldName(p[1]);
                String title = tableData.getTableTitle() + "_" + fieldData.getFieldTitle();
                text.replace(msg, title);
            }
            return text;
        } catch (Exception e) {
            LogUtil.sysLog("Exception:" + e);
            return value;
        }
    }

    /**
     * 线条件 id 转 name
     *
     * @ param 线条件id
     * @ return name
     */
    public static String getParam(String value) {
        try {
            value = StringHelper.getMsg(value).get(0);
            String[] param = value.split("\\|");
            String[] p = param[0].split("-");
            WorkFlowNodeService workFlowNodeService = (WorkFlowNodeService) SpringContextUtil.getBean("workFlowNodeService");
            WorkFlowNode node = workFlowNodeService.selectById(p[0]);
            String nodeTitle = node.getNodeTitle();
            DictionaryService dictionaryService = (DictionaryService) SpringContextUtil.getBean("dictionaryService");
            FieldData fieldData = dictionaryService.selectFieldName(p[2]);
            String fieldTitle = fieldData.getFieldTitle();
            String text = nodeTitle + "-" + fieldTitle + "|" + param[1] + "|" + param[2];
            return text;
        } catch (Exception e) {
            LogUtil.sysLog("Exception:" + e);
            return value;
        }
    }

    /**
     * 节点权限id转name
     *
     * @ param 节点权限id
     * @ return name
     */
    public static String getActorData(String value) {
        try {
            WkflwNodeActorService a = (WkflwNodeActorService) SpringContextUtil.getBean("wkflwNodeActorService");
            WkflwNodeActor nodeActor = a.selectById(value);
            if (nodeActor != null) {
                String text = value;
                String type = nodeActor.getWkfActorType();
                String nodeId = nodeActor.getNodeID();
                WorkFlowNodeService n = (WorkFlowNodeService) SpringContextUtil.getBean("workFlowNodeService");
                if (type.equals("nodeactor")) {
                    String[] context = nodeActor.getContextValue().split("_");
                    WorkFlowNode node = n.selectById(context[0]);
                    String nodeName = node.getNodeTitle();
                    DictionaryService dictionaryService = (DictionaryService) SpringContextUtil.getBean("dictionaryService");
                    TableData tableData = dictionaryService.selectTableName(context[1]);
                    FieldData fieldData = dictionaryService.selectFieldName(context[2]);
                    String tableTitle = tableData.getTableTitle();
                    String fieldTitle = fieldData.getFieldTitle();
                    text = tableTitle + "_" + fieldTitle;
                    return nodeName + " [" + text + "]";
                } else if (type.equals("roleactor")) {
                    RoleDefinesService r = (RoleDefinesService) SpringContextUtil.getBean("roleDefinesService");
                    String context = nodeActor.getContextValue();
                    RoleDefines roleDefines = r.selectById(context);
                    if (roleDefines != null) {
                        text = roleDefines.getRoleTitle();
                    }
                    return " [" + text + "]";
                } else if (type.equals("useractor")) {
                    UserManagerService u = (UserManagerService) SpringContextUtil.getBean("userManagerService");
                    String context = nodeActor.getContextValue();
                    UserManager userManager = u.selectUserById(context);
                    if (userManager != null) {
                        text = userManager.getName();
                    }
                    return " [" + text + "]";
                } else if (type.equals("deptactor")) {//部长审批 [部长审批_主管院领导_部门负责人]
                    DepartmentService d = (DepartmentService) SpringContextUtil.getBean("departmentService");
                    String[] context = nodeActor.getContextValue().split("_");
                    Department department = d.selectById(context[0]);
                    if(department!=null){
                        text = department.getDeptName();
                        String name = department.getDeptHead();
                        if("deptHead".equals(context[1])){
                            text += "_" +ToNameUtil.idToName("user",name);
                        }else{
                            text += "_" +ToNameUtil.idToName("users",name);
                        }
                    }
                    return " [" + text + "]";
                } else {
                    return "";
                }
            } else {
                return value;
            }
        } catch (Exception e) {
            LogUtil.sysLog("Exception:" + e);
            return value;
        }
    }

    private static Map<String, WorkFlowNode> nodeDate = null;

    static {
        initNodeData();
    }

    public static void resetNodeData() {
        if (nodeDate != null) {
            nodeDate.clear();
        }
        nodeDate = null;
    }

    public static void initNodeData() {
        WorkFlowNodeService ns = (WorkFlowNodeService) SpringContextUtil.getBean("workFlowNodeService");
        List<WorkFlowNode> nList = ns.selectAll();
        if (nList.size() > 0) {
            nodeDate = new HashMap<String, WorkFlowNode>();
            for (WorkFlowNode n : nList) {
                nodeDate.put(n.getNodeID(), n);
            }
        }
    }

    public static void initNodeData(String id) {
        WorkFlowNodeService ns = (WorkFlowNodeService) SpringContextUtil.getBean("workFlowNodeService");
        WorkFlowNode n = ns.selectById(id);
        if (n != null) {
            nodeDate.put(id, n);
        }
    }

    public static WorkFlowNode getData(String id) {
        if (nodeDate == null) {
            initNodeData();
        }
        if (nodeDate != null) {
            WorkFlowNode result = nodeDate.get(id);
            if (result == null) {
                initNodeData(id);
                return nodeDate.get(id);
            } else {
                return result;
            }
        }
        return null;
    }

    public static String getName(String id) {
        if (id.equals("endNode")) {
            return " 结束 ";
        }else if (id.equals("starNode")) {
            return" 开始 ";
        }
        WorkFlowNode n = getData(id);
        if (n != null) {
            return n.getNodeTitle();
        } else {
            return id;
        }

    }

    public static String getNames(String ids, String split) {
        String name = "";
        String[] id = ids.split(split);
        for (int i = 0; i < id.length; i++) {
            if (id[i].equals("endNode")) {
                name += " 结束 ";
            }else if (id[i].equals("starNode")) {
                name += " 开始 ";
            } else {
                WorkFlowNode n = getData(id[i]);
                if (n != null) {
                    name += n.getNodeTitle() + " ";
                } else {
                    name += name + " ";
                }
            }
        }
        return " " + name;
    }
}
