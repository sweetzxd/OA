package com.oa.core.controller.module;

import com.oa.core.bean.Loginer;
import com.oa.core.bean.dd.FieldData;
import com.oa.core.bean.dd.TaskData;
import com.oa.core.bean.system.FormCustomMade;
import com.oa.core.helper.DateHelper;
import com.oa.core.helper.StringHelper;
import com.oa.core.interceptor.Logined;
import com.oa.core.service.dd.DictionaryService;
import com.oa.core.service.system.FormCustomMadeService;
import com.oa.core.service.system.TaskSenderService;
import com.oa.core.service.util.TableService;
import com.oa.core.service.work.WorkFlowLineService;
import com.oa.core.service.work.WorkFlowNodeService;
import com.oa.core.system.postposition.PostPosition;
import com.oa.core.system.workflow.ProcUtil;
import com.oa.core.util.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @ClassName:BumphController
 * @author:zxd
 * @Date:2019/06/11
 * @Time:上午 9:23
 * @Version V1.0
 * @Explain
 */
@Controller
@RequestMapping("/bumph")
public class BumphController {

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

    @RequestMapping(value = "/gotofawen", method = RequestMethod.GET)
    public ModelAndView gotofawen() {
        ModelAndView model = new ModelAndView("bumph/fawen");
        model.addObject("type", "add");
        return model;
    }

    @RequestMapping(value = "/gotoshouwen", method = RequestMethod.GET)
    public ModelAndView gotoshouwen() {
        ModelAndView model = new ModelAndView("bumph/shouwen");
        model.addObject("type", "add");
        return model;
    }

    @RequestMapping(value = "/selectbumph", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String getSelectBumph(HttpServletRequest request, String type, int page, int limit) {
        String leixing = request.getParameter("leixing");
        String where = "";
        if (leixing != null && !leixing.equals("") && !leixing.equals("全部")) {
            where += " AND leixing='" + leixing + "' ";
        }

        String wenhao = request.getParameter("wenhao");
        if (wenhao != null && !wenhao.equals("")) {
            where += " AND wenhao like('%" + wenhao + "%') ";
        }
        String title = request.getParameter("title");
        if (title != null && !title.equals("")) {
            where += " AND biaoti like('%" + title + "%') ";
        }
        String name = request.getParameter("name");
        if (name != null && !name.equals("")) {
            where += " AND (qicaoren in (select userName from usermanager where `name` like ('%" + name + "%') ) or qicaoren like ('%" + name + "%'))";
        }
        String time = request.getParameter("time");
        if (time != null && !time.equals("")) {
            String[] times = time.split(" - ");
            where += " AND shijian BETWEEN '" + times[0] + "' AND '" + times[1] + "' ";
        }
        int star = (page * limit) - limit;
        String fy = " limit " + star + "," + limit + " ";
        JSONObject json = new JSONObject();
        String where1 = "", where2 = "";
        switch (type) {
            case "agents":
                where1 = " and recorderNO not in (select procId from workflowproc where curStatus=2 and wkflwID='fwlc2019050900001')";
                where2 = " and recorderNO not in (select procId from workflowproc where curStatus=2 and wkflwID='swlc2019050900001')";
                break;
            case "track":
                where1 = " and recorderNO in (select procId from workflowproc where curStatus=2 and curNodeID != 'endNode' and wkflwID='fwlc2019050900001')";
                where2 = " and recorderNO in (select procId from workflowproc where curStatus=2 and curNodeID != 'endNode' and wkflwID='swlc2019050900001')";
                break;
            case "complete":
                where1 = " and recorderNO in (select procId from workflowproc where curStatus=2 and curNodeID = 'endNode' and wkflwID='fwlc2019050900001')";
                where2 = " and recorderNO in (select procId from workflowproc where curStatus=2 and curNodeID = 'endNode' and wkflwID='swlc2019050900001')";
                break;
            case "inquire":
                where1 = " and recorderNO in (select procId from workflowproc where curStatus=2 and curNodeID = 'endNode' and wkflwID='fwlc2019050900001')";
                where2 = " and recorderNO in (select procId from workflowproc where curStatus=2 and curNodeID = 'endNode' and wkflwID='swlc2019050900001')";
                break;
            case "statistics":
                break;
            default:
                break;
        }
        try {
            List<Map<String, Object>> maps = tableService.selectSqlMapList("select (@i:=@i+1) num,a.* from (select  * from (select recorderNO,'发文' as leixing,wh19050900001 as wenhao, bt19050900001 as biaoti,ngr1905090001 as qicaoren,fwrq190509001 as shijian from txfw190509001 where curStatus=2 " + where1 + " union select recorderNO,'收文' as leixing, wh19050900002 as wenhao, bt19050900002 as biaoti,ngr1905090002 as qicaoren,rq19050900001 as shijian from txsw190509001 where curStatus=2 " + where2 + ") a order by shijian desc) a ,(SELECT @i:=0) as i where 1=1 " + where + " " + fy);
            String sql = "select count(*) from (select  * from (select recorderNO,'发文' as leixing,wh19050900001 as wenhao, bt19050900001 as biaoti,ngr1905090001 as qicaoren,fwrq190509001 as shijian from txfw190509001 where curStatus=2 " + where1 + " union select recorderNO,'收文' as leixing, wh19050900002 as wenhao, bt19050900002 as biaoti,ngr1905090002 as qicaoren,rq19050900001 as shijian from txsw190509001 where curStatus=2 " + where2 + ") a order by shijian desc) a where 1=1 " + where + " ";
            int count = tableService.selectSqlCount(sql);
            json.put("code", 0);
            json.put("msg", "");
            json.put("count", count);
            json.put("data", maps);
            json.put("success", 1);
        } catch (Exception e) {
            json.put("code", 1);
            json.put("msg", "查询失败");
            json.put("count", 0);
            json.put("data", "");
            json.put("success", 0);
        }

        System.out.println(json.toString());
        return json.toString();
    }

    @RequestMapping(value = "/flowaddsave/{formid}", method = RequestMethod.POST)
    public ModelAndView formAddSave(HttpServletRequest request, @PathVariable("formid") String formid) {
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        String userId = loginer.getId();

        String type = "收文", table = "txsw190509001";
        String fields = Const.SHOUWEN;
        if ("flowtable2019050900001".equals(formid)) {
            type = "发文";
            table = "txfw190509001";
            fields = Const.FAWEN;
        }

        String procId = request.getParameter("workflowProcID");
        String nodeId = request.getParameter("workflowNodeID");
        String workOrderNO = request.getParameter("workOrderNO") == null ? "" : request.getParameter("workOrderNO");
        String wkflwId = request.getParameter("wkflwId");
        boolean modi = false;
        String recorderNO = request.getParameter(table + "_recorderNO_Value");
        if (recorderNO != null && recorderNO != "") {
            modi = true;
        } else {
            String title = null;
            if (procId == null || procId == "") {
                title = "FlowTaskG";
            }
            PrimaryKeyUitl pk = new PrimaryKeyUitl();
            recorderNO = pk.getNextId(table, "recorderNO", title, Const.PREFIXTYPE_YEAR_MONTH_DAY);
        }
        ModelAndView mode;
        if (recorderNO == null || recorderNO == "") {
            mode = new ModelAndView("redirect:/flowpage/error.do");
            mode.addObject("msg", "主键为空");
            return mode;
        }
        try {
            Vector<String> field = StringHelper.string2Vector(fields, ";");
            Map<String, Object> map = new HashMap<String, Object>();
            for (String fieldName : field) {
                String value = request.getParameter(table + "_" + fieldName + "_Value");
                if (value != null && !value.equals("")) {
                    value = new String(value.getBytes("iso-8859-1"), "UTF-8");
                    map.put(fieldName, value);
                }
            }
            map.put("workflowProcID", procId);
            map.put("workflowNodeID", nodeId);

            map.put("modifyName", userId);
            map.put("modifyTime", DateHelper.now());

            if (modi) {
                tableService.updateSqlMap(MySqlUtil.getUpdateSql(table, recorderNO, map, null));
            } else {
                map.put("recorderNO", recorderNO);
                map.put("recordName", userId);
                map.put("recordTime", DateHelper.now());
                tableService.insertSqlMap(MySqlUtil.getInsertSql(table, map));
            }
            mode = new ModelAndView("redirect:/bumph/gettasksender.do?save=info&recno=" + recorderNO + "&wkflwId=" + wkflwId + "&procId=" + procId + "&workOrderNO=" + workOrderNO);
            mode.addObject("msg", "保存成功");
            return mode;
        } catch (Exception e) {
            e.printStackTrace();
            mode = new ModelAndView("redirect:/flowpage/error.do");
            mode.addObject("msg", "保存失败");
            return mode;
        }
    }

    @RequestMapping(value = "/deletedata", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public void deleteData(HttpServletRequest request, String recno) {
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        String userId = loginer.getId();
        tableService.updateSqlMap("update txfw190509001 set curStatus=0,deleteName='" + userId + "',deleteTime='" + DateHelper.now() + "' where curStatus=2 and recorderNO='" + recno + "'");

    }

    @RequestMapping(value = "/gettasksender", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ModelAndView getTaskSender(HttpServletRequest request, String recno, String type, String save) {
        String wkflwId = request.getParameter("wkflwId");
        String formid = request.getParameter("formid");
        String wjtp = "";
        if ("fwlc2019050900001".equals(wkflwId)) {
            type = "发文";
            wjtp = "fw";
        } else if ("swlc2019050900001".equals(wkflwId)) {
            type = "收文";
            wjtp = "sw";
        }

        String where = "", pageurl = "", field = "", table = "";
        List<String> wheres = new ArrayList<>();
        wheres.add("recorderNO='" + recno + "'");
        if (type != null) {
            if (type.equals("发文")) {
                where = " and t.wkflwID='fwlc2019050900001'";
                pageurl = "bumph/fawen";
                field = Const.FAWEN;
                table = "txfw190509001";
            } else if (type.equals("收文")) {
                where = " and t.wkflwID='swlc2019050900001'";
                pageurl = "bumph/shouwen";
                field = Const.SHOUWEN;
                table = "txsw190509001";
                wjtp = "sw";
            }
        }
        ModelAndView model = new ModelAndView(pageurl);
        try {
            Vector<String> fields = StringHelper.string2Vector(field, ";");
            Map<String, Object> maps1 = tableService.selectSqlMap(MySqlUtil.getFieldSql(fields, table, wheres));
            String sql = "select wkfNode,staffName,nodeTitle,name as nmname,DATE_FORMAT(t.recordTime,'%Y-%m-%d %h:%i:%s') as recordTime from tasksender t join userinfo u on t.accepter=u.userName join workflowNode w on t.wkfNode=w.nodeId join usermanager m on m.username=t.recordName where  t.curStatus=2 and t.procID='" + recno + "' " + where + " and t.msgStatus>0 and t.msgStatus<5 order by t.recordTime asc";
            List<Map<String, Object>> maps2 = tableService.selectSqlMapList(sql);
            String sqlsp = "";
            if ("modi".equals(save)) {
                model.addObject("type", "modi");
            } else {
                model.addObject("type", "info");
            }
            if (maps2.size() > 0) {
                String wkfNode = maps2.get(0).get("wkfNode") + "";
                if (!wjtp.equals("") && wjtp.equals("sw")) {
                    switch (wkfNode) {
                        case "bgssp2019050900001":
                            sqlsp = "SELECT spyjs19050904 as  yijian ,recordName as spname,DATE_FORMAT(recordTime, '%Y-%m-%d %h:%i:%s') as recordTime from bgssp19050901 where workflowProcID='" + recno + "'";
                             break;
                        case "zgyldsp2019050900001":
                            sqlsp = "SELECT spyjs19050906 as  yijian,recordName as spname ,DATE_FORMAT(recordTime, '%Y-%m-%d %h:%i:%s') as recordTime  from zgyld19050901 where  workflowProcID='" + recno + "'";
                            break;
                        case "yzsp2019050900001":
                            sqlsp = "SELECT spyjs19050907 as  yijian ,recordName as spname,DATE_FORMAT(recordTime, '%Y-%m-%d %h:%i:%s') as recordTime from yzsp190509001 where workflowProcID='" + recno + "'";
                            break;
                        case "cb2019050900002":
                            sqlsp = "SELECT cbyj190509001 as  yijian ,recordName as spname,DATE_FORMAT(recordTime, '%Y-%m-%d %h:%i:%s') as recordTime from cb19050900002 where workflowProcID='" + recno + "'";
                            break;
                       case "fb2019050900003":
                            sqlsp = "SELECT fbyj190509001 as  yijian ,recordName as spname,DATE_FORMAT(recordTime, '%Y-%m-%d %h:%i:%s') as recordTime from fb19050900002 where workflowProcID='" + recno + "'";
                            break;
                       default:
                    }
                } else {
                    switch (wkfNode) {
                        case "bzps2019050900001":
                            sqlsp = "SELECT spyjs19050901 as  yijian ,recordName as spname,DATE_FORMAT(recordTime, '%Y-%m-%d %h:%i:%s') as recordTime from bzps190509001 where workflowProcID='" + recno + "'";
                            break;
                        case "zgldps2019050900001":
                            sqlsp = "SELECT spyjs19050902 as  yijian,recordName as spname ,DATE_FORMAT(recordTime, '%Y-%m-%d %h:%i:%s') as recordTime  from zgldp19050901 where  workflowProcID='" + recno + "'";
                            break;
                        case "yzps2019050900002":
                            sqlsp = "SELECT spyjs19050903 as  yijian ,recordName as spname,DATE_FORMAT(recordTime, '%Y-%m-%d %h:%i:%s') as recordTime from yzps190509001 where workflowProcID='" + recno + "'";
                            break;
                        case "cb2019050900007":
                            sqlsp = "SELECT cbyjs19050902 as  yijian ,recordName as spname,DATE_FORMAT(recordTime, '%Y-%m-%d %h:%i:%s') as recordTime from cb19050900003 where workflowProcID='" + recno + "'";
                            break;
                        case "fb2019050900008":
                            sqlsp = "SELECT fbyjs19050902 as  yijian ,recordName as spname,DATE_FORMAT(recordTime, '%Y-%m-%d %h:%i:%s') as recordTime from fb19050900004 where workflowProcID='" + recno + "'";
                            break;
                        default:
                    }
                }
            }
            Map<String, Object> mapmp = new HashMap<>();
            if (sqlsp != null && !sqlsp.equals("")) {
                Map<String, Object> Mapjl = tableService.selectSqlMap(sqlsp);
                if (Mapjl != null) {
                    mapmp.put("spname", ToNameUtil.getName("user", Mapjl.get("spname")));
                    mapmp.put("yijian", Mapjl.get("yijian"));
                    mapmp.put("recordTime", Mapjl.get("recordTime"));
                }
            }
            String workOrderNO=request.getParameter("workOrderNO");
            model.addObject("data", maps1);
            model.addObject("log", maps2);
            model.addObject("jili", mapmp);
            model.addObject("workOrderNO", workOrderNO);
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
        return model;
    }

    @Logined
    @RequestMapping(value = "/flowtasknew.do", method = RequestMethod.GET)
    public ModelAndView gotoFormPage(HttpServletRequest request) {
        ProcUtil pu = new ProcUtil();
        return pu.WorkFlowProc(request);
    }


    @RequestMapping(value = "/selectworlfor", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String getselectworlfor(HttpServletRequest request) {
        String recorderNO = request.getParameter("recorderNO");
        String tableid = request.getParameter("tableid");
        String workOrderNO = request.getParameter("workOrderNO");
        String fields = Const.SHOUWEN;
        if ("txfw190509001".equals(tableid)) {
            fields = Const.FAWEN;
        }
        Vector<String> field = StringHelper.string2Vector(fields, ";");
        List<String> where = new ArrayList<>();
        where.add("recorderNO='" + recorderNO + "'");
        String sql = MySqlUtil.getFieldSql(field, tableid, where);
        JSONObject json = new JSONObject();
        try {
            Map<String, Object> maps = tableService.selectSqlMap(sql);
            String wheres="";
            boolean aa=true;
            if (tableid != null) {
                if (tableid.equals("txfw190509001")) {
                    wheres = " and t.wkflwID='fwlc2019050900001'";
                    aa=false;
                } else if (tableid.equals("txsw190509001")) {
                    wheres = " and t.wkflwID='swlc2019050900001'";
                }
            }
            String sqllog = "select wkfNode,staffName,nodeTitle,name as nmname,DATE_FORMAT(t.recordTime,'%Y-%m-%d %h:%i:%s') as recordTime from tasksender t join userinfo u on t.accepter=u.userName join workflowNode w on t.wkfNode=w.nodeId join usermanager m on m.username=t.recordName where  t.curStatus=2 and t.procID='" + recorderNO + "' " + wheres + " and t.msgStatus>0 and t.msgStatus<5 order by t.recordTime asc";
            List<Map<String, Object>> maps2 = tableService.selectSqlMapList(sqllog);
            List<Map<String, Object>> listjil=new ArrayList<>();
            if(aa){
                String sqlbgs="SELECT spyjs19050904 as  yijian ,recordName as spname,DATE_FORMAT(recordTime, '%Y-%m-%d %h:%i:%s') as recordTime from bgssp19050901 where workflowProcID='" + recorderNO + "'";
                Map<String, Object> mapjilu = tableService.selectSqlMap(sqlbgs);
                if(mapjilu!=null&&mapjilu.size()>0){
                    mapjilu.put("spname", ToNameUtil.getName("user", mapjilu.get("spname")));
                    listjil.add(mapjilu);
                }
                String sqlld="SELECT spyjs19050906 as  yijian,recordName as spname ,DATE_FORMAT(recordTime, '%Y-%m-%d %h:%i:%s') as recordTime  from zgyld19050901 where  workflowProcID='" + recorderNO + "'";
                Map<String, Object> mapjiluld = tableService.selectSqlMap(sqlld);
               if(mapjiluld!=null&&mapjiluld.size()>0){
                   mapjiluld.put("spname", ToNameUtil.getName("user", mapjiluld.get("spname")));
                   listjil.add(mapjiluld);
               }
                String sqlyz="SELECT spyjs19050907 as  yijian ,recordName as spname,DATE_FORMAT(recordTime, '%Y-%m-%d %h:%i:%s') as recordTime from yzsp190509001 where workflowProcID='" + recorderNO + "'";
                Map<String, Object> mapjiluyz = tableService.selectSqlMap(sqlyz);
                if(mapjiluyz!=null&&mapjiluyz.size()>0){
                    mapjiluyz.put("spname", ToNameUtil.getName("user", mapjiluyz.get("spname")));
                    listjil.add(mapjiluyz);
                }
                String sqlcb="SELECT cbyj190509001 as  yijian ,recordName as spname,DATE_FORMAT(recordTime, '%Y-%m-%d %h:%i:%s') as recordTime from cb19050900002 where workflowProcID='" + recorderNO + "'";
                Map<String, Object> mapjilucb = tableService.selectSqlMap(sqlcb);
                if(mapjilucb!=null&&mapjilucb.size()>0){
                    mapjilucb.put("spname", ToNameUtil.getName("user", mapjilucb.get("spname")));
                    listjil.add(mapjilucb);
                }
                String sqlfb="SELECT fbyj190509001 as  yijian ,recordName as spname,DATE_FORMAT(recordTime, '%Y-%m-%d %h:%i:%s') as recordTime from fb19050900002 where workflowProcID='" + recorderNO + "'";
                Map<String, Object> mapjilufb = tableService.selectSqlMap(sqlfb);
               if(mapjilufb!=null&&mapjilufb.size()>0){
                   mapjilufb.put("spname", ToNameUtil.getName("user", mapjilufb.get("spname")));
                   listjil.add(mapjilufb);
               }
            }else {
                String sqlbgsfw="SELECT spyjs19050901 as  yijian ,recordName as spname,DATE_FORMAT(recordTime, '%Y-%m-%d %h:%i:%s') as recordTime from bzps190509001 where workflowProcID='" + recorderNO + "'";
                Map<String, Object> mapjilufw = tableService.selectSqlMap(sqlbgsfw);
                if(mapjilufw!=null&&mapjilufw.size()>0){
                    mapjilufw.put("spname", ToNameUtil.getName("user", mapjilufw.get("spname")));
                    listjil.add(mapjilufw);
                }
                String sqlldfw="SELECT spyjs19050902 as  yijian,recordName as spname ,DATE_FORMAT(recordTime, '%Y-%m-%d %h:%i:%s') as recordTime  from zgldp19050901 where  workflowProcID='" + recorderNO + "'";
                Map<String, Object> mapjilufwld = tableService.selectSqlMap(sqlldfw);
                if(mapjilufwld!=null&&mapjilufwld.size()>0){
                    mapjilufwld.put("spname", ToNameUtil.getName("user", mapjilufwld.get("spname")));
                    listjil.add(mapjilufwld);
                }

                String sqlyzfw="SELECT spyjs19050903 as  yijian ,recordName as spname,DATE_FORMAT(recordTime, '%Y-%m-%d %h:%i:%s') as recordTime from yzps190509001 where workflowProcID='" + recorderNO + "'";
                Map<String, Object> mapjiluyzfw = tableService.selectSqlMap(sqlyzfw);
                if(mapjiluyzfw!=null&&mapjiluyzfw.size()>0){
                    mapjiluyzfw.put("spname", ToNameUtil.getName("user", mapjiluyzfw.get("spname")));
                    listjil.add(mapjiluyzfw);
                }
                String sqlfbfw="SELECT fbyjs19050902 as  yijian ,recordName as spname,DATE_FORMAT(recordTime, '%Y-%m-%d %h:%i:%s') as recordTime from fb19050900004 where workflowProcID='" + recorderNO + "'";
                Map<String, Object> mapjilufwfb = tableService.selectSqlMap(sqlfbfw);

                if(mapjilufwfb!=null&&mapjilufwfb.size()>0){
                    mapjilufwfb.put("spname", ToNameUtil.getName("user", mapjilufwfb.get("spname")));
                    listjil.add(mapjilufwfb);
                }
                String sqlcbfw="SELECT cbyjs19050902 as  yijian ,recordName as spname,DATE_FORMAT(recordTime, '%Y-%m-%d %h:%i:%s') as recordTime from cb19050900003 where workflowProcID='" + recorderNO + "'";
                Map<String, Object> mapjilucbfw = tableService.selectSqlMap(sqlcbfw);
                if(mapjilucbfw!=null&&mapjilucbfw.size()>0) {
                    mapjilucbfw.put("spname", ToNameUtil.getName("user", mapjilucbfw.get("spname")));
                    listjil.add(mapjilucbfw);

                }
            }
            json.put("success", 1);
            json.put("msg", "");
            json.put("log", maps2);
            json.put("reco", listjil);
            json.put("data", maps);
            json.put("workOrderNO", workOrderNO);
        } catch (Exception e) {
            json.put("success", 0);
            json.put("msg", "查询失败");
        } finally {
            return json.toString();
        }
    }


}
