package com.oa.core.system.workflow;

import com.oa.core.bean.Loginer;
import com.oa.core.bean.dd.TableData;
import com.oa.core.bean.work.WorkFlowNode;
import com.oa.core.bean.work.WorkFlowProc;
import com.oa.core.helper.StringHelper;
import com.oa.core.service.dd.DictionaryService;
import com.oa.core.service.util.TableService;
import com.oa.core.service.work.WorkFlowLineService;
import com.oa.core.service.work.WorkFlowProcService;
import com.oa.core.util.*;
import org.junit.Test;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @ClassName:WorkFlowLineUtil
 * @author:zxd
 * @Date:2018/09/28
 * @Time:上午 10:10
 * @Version V1.0
 * @Explain 处理流程线运行条件，并获取下一节点
 */
public class LineUtil {

    public boolean getNextNode(com.oa.core.bean.work.WorkFlowLine line, WorkFlowNode node, String procId, String workOrderNO) {
        boolean request = false;
        int type = line.getLogUnitType();
        String wkflwId = line.getWkflwId();
        if (type == 1) {
            request = true;
        } else if (type == 2) {
            List<String> params = StringHelper.getMsg(line.getLogUnitParams());
            String val = params.get(0);
            String[] param = val.split("\\|");
            String[] p = param[0].split("-");
            request = getNodeTabVal(p[1], getWhere(p[2]), procId, p[0], wkflwId);
        } else if (type == 3) {
            List<String> params = StringHelper.getMsg(line.getLogUnitParams());
            String val = params.get(0);
            String[] param = val.split("\\|");
            String[] p = param[0].split("-");
            request = getNodeTabVal(p[1], getString(param, p), procId, p[0], wkflwId);
        } else if (type == 4) {
            List<String> params = StringHelper.getMsg(line.getLogUnitParams());
            for (int i = 0; i < params.size(); i++) {
                String[] param = params.get(i).split("\\|");
                String[] p = param[0].split("-");
                request = getNodeTabVal(p[1], getString(param, p), procId, p[0], wkflwId);
                if (!request) {
                    break;
                }
            }
        } else if (type == 5) {
            List<String> params = StringHelper.getMsg(line.getLogUnitParams());
            for (int i = 0; i < params.size(); i++) {
                String[] param = params.get(i).split("\\|");
                String[] p = param[0].split("-");
                request = getNodeTabVal(p[1], getString(param, p), procId, p[0], wkflwId);
                if (request) {
                    break;
                }
            }
        }
        return request;
    }

    /**
     * 处理发送条件：制定字符串为真
     * */
    private String getWhere(String field) {
        ConfParseUtil conf = new ConfParseUtil();
        String really = conf.getPoa("really");
        String[] values = new String[]{"1", "是", "同意"};
        if (really != null && really.length() > 0) {
            values = really.split(Const.SEPARATE);
        }
        String where = "(";
        for (int i = 0; i < values.length; i++) {
            String value = values[i];
            if (i == values.length - 1) {
                where += field + " = '" + value + "'";
            } else {
                if (value.equals("1")) {
                    where += field + " = '1' OR " + field + " = 1 OR ";
                } else {
                    where += field + " = '" + value + "' OR ";
                }
            }
        }
        where += ")";
        return where;
    }

    /**
     * 处理发送条件：转换where条件
     * */
    private String getString(String[] param, String[] p) {
        if (param[1].equals("等于")) {
            return p[2] + " = '" + param[2] + "'";
        } else if (param[1].equals("不等于")) {
            return p[2] + " != '" + param[2] + "'";
        } else if (param[1].equals("大于")) {
            return p[2] + " > " + param[2];
        } else if (param[1].equals("大于等于")) {
            return p[2] + " >= " + param[2];
        } else if (param[1].equals("小于")) {
            return p[2] + " < " + param[2];
        } else if (param[1].equals("小于等于")) {
            return p[2] + " <= " + param[2];
        } else if (param[1].equals("包含")) {
            return p[2] + " like ('%" + param[2] + "%')";
        } else {
            return "";
        }
    }

    /**
     * 发送条件处理SQL语句
     * */
    public static boolean getNodeTabVal(String table, String where, String procId, String nodeId, String wkflwId) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        String loginId = loginer.getId();
        try {
            DictionaryService dictionaryService = (DictionaryService) SpringContextUtil.getBean("dictionaryService");
            TableData tableData = dictionaryService.selectTableName(table);
            List<String> list = Arrays.asList(tableData.getTableField().split(";"));
            List<String> fields = new ArrayList<String>(list);
            List<String> wheres = new ArrayList<String>();
            wheres.add(where);
            wheres.add("workflowProcID='" + procId + "'");
            if (nodeId != null && nodeId != "") {
                WorkFlowLineService workFlowLineService = (WorkFlowLineService) SpringContextUtil.getBean("workFlowLineService");
                List<String> sn = workFlowLineService.selectSN(wkflwId);
                String starnode = sn.get(0);
                /*
                 * 首节点判定
                 * */
                boolean star = starnode.equals(nodeId);
                if (star) {
                    WorkFlowProcService workFlowProcService = (WorkFlowProcService) SpringContextUtil.getBean("workFlowProcService");
                    WorkFlowProc workFlowProc = workFlowProcService.selectById(procId, wkflwId);
                    if(workFlowProc!=null){
                        wheres.add("recordName='" + workFlowProc.getOriginator() + "'");
                    }else{
                        wheres.add("recordName='" + loginId + "'");
                    }
                } else {
                    wheres.add("recordName in (select accepter from tasksender where procID='" + procId + "' and wkfNode='" + nodeId + "' and msgStatus>1 and msgStatus<4 and curStatus=2)");
                }
            } else {
                wheres.add("recordName='" + loginId + "'");
            }
            String sql = MySqlUtil.getSql(fields, table, wheres);
            TableService tableService = (TableService) SpringContextUtil.getBean("tableService");
            Map<String, Object> sqlvalue = tableService.selectSqlMap(sql);
            if (sqlvalue != null && sqlvalue.size() > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            LogUtil.sysLog("Exception:" + e);
            e.printStackTrace();
            return false;
        }
    }

    @Test
    public void test() {
        LogUtil.sysLog(getWhere("field"));
    }
	
}
