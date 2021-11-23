package com.oa.core.system.workflow;

import com.oa.core.bean.work.WorkFlowLine;
import com.oa.core.bean.work.WorkFlowNode;
import com.oa.core.service.work.WorkFlowLineService;
import com.oa.core.service.work.WorkFlowNodeService;
import com.oa.core.util.SpringContextUtil;

import java.util.ArrayList;
import java.util.List;
/**
 * @ClassName:WorkFlowNodeUtil
 * @author:zxd
 * @Date:2018/09/28
 * @Time:上午 10:10
 * @Version V1.0
 * @Explain 获取下一节点
 */
public class NodeUtil {

    public List<String> getNextNode(WorkFlowNode currentNode,String procId,String workOrderNO){
        String nodeid = currentNode.getNodeID();
        WorkFlowLineService workFlowLineService = (WorkFlowLineService) SpringContextUtil.getBean("workFlowLineService");
        List<WorkFlowLine> linelist = workFlowLineService.selectByNodeId(nodeid);
        List<String> tonodeid = new ArrayList<>();
        LineUtil lineUtil = new LineUtil();
        for(int i=0;i<linelist.size();i++){
            WorkFlowLine line = linelist.get(i);
            if(lineUtil.getNextNode(line,currentNode,procId,workOrderNO)) {
                if(!tonodeid.contains(line.getToNode())) {
                    tonodeid.add(line.getToNode());
                }
            }
        }
        return tonodeid;
    }

    public boolean getNodeBoolean(WorkFlowNode currentNode){
        return false;
    }
}
