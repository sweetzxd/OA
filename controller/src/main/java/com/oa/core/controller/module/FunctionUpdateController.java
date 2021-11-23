package com.oa.core.controller.module;

import com.oa.core.bean.Loginer;
import com.oa.core.bean.dd.FieldData;
import com.oa.core.bean.dd.TableData;
import com.oa.core.bean.dd.TaskData;
import com.oa.core.bean.module.UpdatePackage;
import com.oa.core.bean.system.FormCustomMade;
import com.oa.core.bean.util.PageUtil;
import com.oa.core.bean.work.*;
import com.oa.core.controller.system.FileUploadController;
import com.oa.core.helper.DateHelper;
import com.oa.core.helper.StringHelper;
import com.oa.core.interceptor.Logined;
import com.oa.core.service.dd.DictionaryService;
import com.oa.core.service.module.DepartmentService;
import com.oa.core.service.module.UpdatePackageService;
import com.oa.core.service.system.FormCustomMadeService;
import com.oa.core.service.system.MyUrlRegistService;
import com.oa.core.service.system.RoleDefinesService;
import com.oa.core.service.user.UserManagerService;
import com.oa.core.service.util.TableService;
import com.oa.core.service.work.*;
import com.oa.core.system.ziputil.ZipCipherUtil;
import com.oa.core.util.LogUtil;
import com.oa.core.util.MySqlUtil;
import com.oa.core.system.ziputil.PackageUtil;
import com.oa.core.util.PrimaryKeyUitl;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

/**
 * @ClassName:FunctionUpdateController
 * @author:zxd
 * @Date:2018/11/15
 * @Time:下午 4:54
 * @Version V1.0
 * @Explain 更新包的摘取与更新
 */
@Controller
@RequestMapping("/updatepack")
public class FunctionUpdateController {

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
    @Autowired
    UpdatePackageService updatePackageService;
    @Autowired
    TableService tableService;

    @RequestMapping(value = "/gotoupdatepack", method = RequestMethod.GET)
    public ModelAndView gotoUpdatePack(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("module/updatepackage");
        mav.addObject("type", "list");
        return mav;
    }

    @RequestMapping("/select")
    @ResponseBody
    public String getEmployeesList(HttpServletRequest request, String inputval, String option, int limit, int page) {
        UpdatePackage updatePackage = new UpdatePackage();
        if (inputval != null && inputval != "") {
            if ("packName".equals(option)) {
                updatePackage.setPackName(inputval);
            } else if ("packType".equals(option)) {
                updatePackage.setPackType(inputval);
            }
        }
        int count = updatePackageService.selectTermsCount(updatePackage);
        PageUtil pageUtil = new PageUtil();
        pageUtil.setPageSize(limit);
        pageUtil.setCurrentPage(page);
        updatePackage.setStartRow(pageUtil.getStartRow());
        updatePackage.setEndRow(pageUtil.getEndRow() - pageUtil.getStartRow());
        List<UpdatePackage> packageList = updatePackageService.selectTerms(updatePackage);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 0);
        jsonObject.put("msg", "");
        jsonObject.put("count", count);
        jsonObject.put("data", packageList);
        jsonObject.put("success",1);
        return jsonObject.toString();
    }

    @RequestMapping(value = "/addupdatepack", method = RequestMethod.GET)
    public ModelAndView addUpdatePack(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("module/updatepackage");
        mav.addObject("type", "add");
        return mav;
    }

    @RequestMapping(value = "/modiupdatepack", method = RequestMethod.GET)
    public ModelAndView modiUpdatePack(HttpServletRequest request, String packId) {
        UpdatePackage updatePackage = updatePackageService.selectById(packId);
        ModelAndView mav = new ModelAndView("module/updatepackage");
        mav.addObject("pack", updatePackage);
        mav.addObject("type", "modi");
        return mav;
    }

    @Logined
    @RequestMapping(value = "/save/{type}", method = RequestMethod.POST, produces = {"text/html;charset=UTF-8;", "application/json;"})
    @ResponseBody
    public String insertSave(HttpServletRequest request, @PathVariable String type, UpdatePackage updatePackage) {
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        try {
            if (loginer != null) {
                String userId = loginer.getId();
                if (type.equals("add")) {
                    updatePackage.setRecordName(userId);
                }
                updatePackage.setModifyName(userId);
            }
            updatePackage.setModifyTime(DateHelper.now());
            if (type.equals("add")) {
                updatePackage.setRecordTime(DateHelper.now());
                PrimaryKeyUitl pk = new PrimaryKeyUitl();
                String packId = pk.getNextId("updatePackage", "packId");
                updatePackage.setPackId(packId);
                updatePackageService.insert(updatePackage);
            } else if (type.equals("modi")) {
                updatePackageService.update(updatePackage);
            }
            return "1";
        } catch (Exception e) {
            return "0";
        }
    }

    @Logined
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ModelAndView deleteSave(HttpServletRequest request,String packId) {
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        packId = request.getParameter("packId");
        try {
            String userId = loginer.getId();
            if (packId != null && packId.indexOf(";") >= 0) {
                Vector<String> packIds = StringHelper.string2Vector(packId, ";");
                updatePackageService.deletes(packIds, userId, DateHelper.now());
            } else if (packId != null && !packId.equals("")) {
                updatePackageService.delete(packId, userId, DateHelper.now());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ModelAndView("redirect:/updatepack/gotoupdatepack.do");
    }

    @Logined
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public String updatePackage(HttpServletRequest request, String packId) {
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        String path = FileUploadController.getFile();
        try {
            UpdatePackage updatePackage = updatePackageService.selectById(packId);
            String file = updatePackage.getPackUpFile();
            String name = file.substring(file.lastIndexOf("/")+1,file.length()-4);
            //解密、解压更新包
            new ZipCipherUtil().decryptUnzip(path+file, path+"upload/update/"+name, "zxc123");
            //拷贝更新文件
            File f1 = new File(path+"upload/update/"+name+"/WEB-INF/views");
            File f2 = new File(path+"WEB-INF");
            PackageUtil pack = new PackageUtil();
            pack.copyFolder(f1,f2);
            //执行更新SQL
            String sqlname = name.substring(name.indexOf("-")+1)+"SQL更新文件.sql";
            String sql = pack.readToString(path+"upload/update/"+name+"/"+sqlname);
            tableService.insertSqlMap(sql);
            return "1";
        } catch (Exception e) {
            return "0";
        }
    }

    /**
     * 导出流程更新SQL
     */
    @RequestMapping(value = "/exportfolwupdate", method = RequestMethod.GET)
    public void exportFolwUpdate(HttpServletRequest request, HttpServletResponse response, String wkflwId) {
        StringBuffer sql = new StringBuffer();
        String name = "";
        List<String> listform = new ArrayList();
        /**
         * 导出流程定义更新SQL*/
        try {
            WorkFlowDefine wkflw = workFlowDefineService.selectById(wkflwId);
            StringBuffer wkflwSql = new StringBuffer();
            name = wkflw.getWkfName();
            wkflwSql.append("#导出流程定义更新SQL\n");
            Hashtable<String, String> ht = wkflw.getSqlField();
            wkflwSql.append("DELETE FROM WorkFlowDefine WHERE wkflwID='" + wkflwId + "';\n");
            wkflwSql.append("INSERT INTO WorkFlowDefine(" + ht.get("field") + ")VALUES(" + ht.get("value") + ");\n");
            sql.append(wkflwSql);
        } catch (Exception e) {
            LogUtil.sysLog("Exception:导出流程定义更新SQL");
            e.printStackTrace();
        }
        /**
         * 导出节点定义更新SQL*/
        try {
            List<WorkFlowNode> nodeList = workFlowNodeService.selectByWkflwId(wkflwId);
            StringBuffer nodeSql = new StringBuffer();
            nodeSql.append("#导出节点定义更新SQL\n");
            for (int i = 0, n = nodeList.size(); i < n; i++) {
                WorkFlowNode node = nodeList.get(i);
                String formId = node.getFormId();
                if (formId != null && !formId.equals("") && !listform.contains(formId)) {
                    listform.add(formId);
                }
                String nodeID = node.getNodeID();
                if (nodeID != null && !nodeID.equals("")) {
                    Hashtable<String, String> ht = node.getSqlField();
                    nodeSql.append("DELETE FROM WorkFlowNode WHERE nodeID='" + nodeID + "';\n");
                    nodeSql.append("INSERT INTO WorkFlowNode(" + ht.get("field") + ")VALUES(" + ht.get("value") + ");\n");
                }
            }
            sql.append(nodeSql);
        } catch (Exception e) {
            LogUtil.sysLog("Exception:导出节点定义更新SQL");
            e.printStackTrace();
        }
        /**
         * 导出线定义更新SQL*/
        try {
            List<WorkFlowLine> lineList = workFlowLineService.selectByWkflwId(wkflwId);
            StringBuffer lineSql = new StringBuffer();
            lineSql.append("#导出线定义更新SQL\n");
            for (int i = 0, n = lineList.size(); i < n; i++) {
                WorkFlowLine line = lineList.get(i);
                String pathId = line.getPathId();
                if (pathId != null && !pathId.equals("")) {
                    Hashtable<String, String> ht = line.getSqlField();
                    lineSql.append("DELETE FROM WorkFlowLine WHERE pathId='" + pathId + "';\n");
                    lineSql.append("INSERT INTO WorkFlowLine(" + ht.get("field") + ")VALUES(" + ht.get("value") + ");\n");
                }
            }
            sql.append(lineSql);
        } catch (Exception e) {
            LogUtil.sysLog("Exception:导出线定义更新SQL");
            e.printStackTrace();
        }

        /**
         * 导出节点权限更新SQL*/
        try {
            List<WkflwNodeActor> actorList = wkflwNodeActorService.selectwkflwId(wkflwId);
            StringBuffer nodeActorSql = new StringBuffer();
            nodeActorSql.append("#导出节点权限更新SQL\n");
            for (int i = 0, n = actorList.size(); i < n; i++) {
                WkflwNodeActor actor = actorList.get(i);
                String nodeActorID = actor.getNodeActorID();
                if (nodeActorID != null && !nodeActorID.equals("")) {
                    Hashtable<String, String> ht = actor.getSqlField();
                    nodeActorSql.append("DELETE FROM WkflwNodeActor WHERE nodeActorID='" + nodeActorID + "';\n");
                    nodeActorSql.append("INSERT INTO WkflwNodeActor(" + ht.get("field") + ")VALUES(" + ht.get("value") + ");\n");
                }
            }
            sql.append(nodeActorSql);
        } catch (Exception e) {
            LogUtil.sysLog("Exception:导出节点权限更新SQL");
            e.printStackTrace();
        }
        /**
         * 导出映射关联更新SQL*/
        List<WkflwFieldMap> wkflwFieldMap = wkflwFieldMapService.selectWkflwFieldMapByWkflwId(wkflwId);
        try {
            if (wkflwFieldMap != null && wkflwFieldMap.size() > 0) {
                StringBuffer wfmSql = new StringBuffer();
                wfmSql.append("#导出映射关联更新SQL\n");
                for (int i = 0, n = wkflwFieldMap.size(); i < n; i++) {
                    WkflwFieldMap wfm = wkflwFieldMap.get(i);
                    String wkflwfieldmapId = wfm.getWkflwfieldmapId();
                    if (wkflwfieldmapId != null && !wkflwfieldmapId.equals("")) {
                        Hashtable<String, String> ht = wfm.getSqlField();
                        wfmSql.append("DELETE FROM wkflwfieldmap WHERE wkflwfieldmapId='" + wkflwfieldmapId + "';\n");
                        wfmSql.append("INSERT INTO wkflwfieldmap(" + ht.get("field") + ")VALUES(" + ht.get("value") + ");\n");
                    }
                }
                sql.append(wfmSql);
            }
        } catch (Exception e) {
            LogUtil.sysLog("Exception:导出映射关联更新SQL");
            e.printStackTrace();
        }
        /**
         * 导出页面定义更新SQL*/
        List<String> tasklist = new ArrayList<>();
        List<String> pagelist = new ArrayList<>();
        try {
            if (listform != null && listform.size() > 0) {
                List<FormCustomMade> formList = formCustomMadeService.selectFormCMByIds(listform);
                StringBuffer formcmSql = new StringBuffer();
                formcmSql.append("#导出页面定义更新SQL\n");

                for (int i = 0, n = formList.size(); i < n; i++) {
                    FormCustomMade formcm = formList.get(i);
                    String formTask = formcm.getFormTask();
                    if (formTask != null && !formTask.equals("") && !tasklist.contains(formTask)) {
                        tasklist.add(formTask);
                    }
                    String listTask = formcm.getListTask();
                    if (listTask != null && !listTask.equals("") && !tasklist.contains(listTask)) {
                        tasklist.add(listTask);
                    }
                    String editPage = formcm.getEditPage();
                    if (editPage != null && !editPage.equals("") && !pagelist.contains(editPage)) {
                        pagelist.add(editPage);
                    }
                    String formcmName = formcm.getFormcmName();
                    if (formcmName != null && !formcmName.equals("")) {
                        Hashtable<String, String> ht = formcm.getSqlField();
                        formcmSql.append("DELETE FROM FormPage WHERE formcmName='" + formcmName + "';\n");
                        formcmSql.append("INSERT INTO FormPage(" + ht.get("field") + ")VALUES(" + ht.get("value") + ");\n");
                    }
                }
                sql.append(formcmSql);
            }
        } catch (Exception e) {
            LogUtil.sysLog("Exception:导出基础数据定义表更新SQL");
            e.printStackTrace();
        }

        /**
         * 导出基础数据定义表更新SQL
         */
        sql.append(exportFormDataSql(tasklist));
        /**
         * 导出菜单设置更新SQL*/

        /**
         * 导出权限设置更新SQL*/

        /**
         * 生成更新包*/
        PackageUtil pack = new PackageUtil();
        pack.creatTxtFile(name, sql);
        pack.copyFolder(pagelist);
        pack.download(response);
    }


    /**
     * 导出表单更新SQL
     */
    @RequestMapping(value = "/exportformupdate", method = RequestMethod.GET)
    public void exportFormUpdate(HttpServletRequest request, HttpServletResponse response, String formId) {
        String name = "";
        StringBuffer sql = new StringBuffer();
        /**
         * 导出页面定义更新SQL*/
        List<String> tasklist = new ArrayList<>();
        List<String> pagelist = new ArrayList<>();
        try {
            StringBuffer formcmSql = new StringBuffer();
            formcmSql.append("#导出页面定义更新SQL\n");
            FormCustomMade formcm = formCustomMadeService.selectFormCMByID(formId);
            String formTask = formcm.getFormTask();
            if (formTask != null && !formTask.equals("") && !tasklist.contains(formTask)) {
                tasklist.add(formTask);
            }
            String listTask = formcm.getListTask();
            if (listTask != null && !listTask.equals("") && !tasklist.contains(listTask)) {
                tasklist.add(listTask);
            }
            String editPage = formcm.getEditPage();
            if (editPage != null && !editPage.equals("") && !pagelist.contains(editPage)) {
                pagelist.add(editPage);
            }
            name = formcm.getFormcmTitle();
            String formcmName = formcm.getFormcmName();
            if (formcmName != null && !formcmName.equals("")) {
                Hashtable<String, String> ht = formcm.getSqlField();
                formcmSql.append("DELETE FROM FormPage WHERE formcmName='" + formcmName + "';\n");
                formcmSql.append("INSERT INTO FormPage(" + ht.get("field") + ")VALUES(" + ht.get("value") + ");\n");
                sql.append(formcmSql);
            }
        } catch (Exception e) {
            LogUtil.sysLog("Exception:导出页面定义更新SQL");
            e.printStackTrace();
        }
        /**
         * 导出基础数据定义表更新SQL
         */
        sql.append(exportFormDataSql(tasklist));
        /**
         * 导出菜单设置更新SQL*/

        /**
         * 导出权限设置更新SQL*/

        /**
         * 生成更新包*/
        PackageUtil pack = new PackageUtil();
        pack.creatTxtFile(name, sql);
        pack.copyFolder(pagelist);
        pack.download(response);
    }

    /**
     * 导出基础数据定义表更新SQL
     */
    public String exportFormDataSql(List<String> tasklist) {
        StringBuffer sql = new StringBuffer();
        /**
         * 导出任务定义更新SQL*/
        List<String> tablelist = new ArrayList<>();
        try {
            if (tasklist != null && tasklist.size() > 0) {
                List<TaskData> tdlist = dictionaryService.selectTaskByIds(tasklist);
                StringBuffer taskSql = new StringBuffer();
                taskSql.append("#导出任务定义更新SQL\n");
                for (int i = 0, n = tdlist.size(); i < n; i++) {
                    TaskData task = tdlist.get(i);
                    String taskName = task.getTaskName();
                    String tableName = task.getTableName();
                    if (tableName != null && !tableName.equals("") && !tablelist.contains(tableName)) {
                        tablelist.add(tableName);
                    }
                    if (taskName != null && !taskName.equals("")) {
                        Hashtable<String, String> ht = task.getSqlField();
                        taskSql.append("DELETE FROM TaskData WHERE taskName='" + taskName + "';\n");
                        taskSql.append("INSERT INTO TaskData(" + ht.get("field") + ")VALUES(" + ht.get("value") + ");\n");
                    }
                }
                sql.append(taskSql);
            }
        } catch (Exception e) {
            LogUtil.sysLog("Exception:导出任务定义更新SQL");
            e.printStackTrace();
        }
        /**
         * 导出表定义更新SQL*/
        List<FieldData> fieldlist = new ArrayList<>();
        try {
            if (tablelist != null && tablelist.size() > 0) {
                List<TableData> tableDataList = dictionaryService.selectTableByIds(tablelist);
                StringBuffer tableSql = new StringBuffer();
                tableSql.append("#导出表定义更新SQL\n");

                /**
                 * 导出物理表更新SQL*/
                StringBuffer mySql = new StringBuffer();
                mySql.append("#导出物理表更新SQL\n");
                for (int i = 0, n = tableDataList.size(); i < n; i++) {
                    TableData table = tableDataList.get(i);
                    String tableName = table.getTableName();
                    String tableTitle = table.getTableTitle();
                    String tableField = table.getTableField();
                    if (tableName != null && !tableName.equals("")) {
                        Hashtable<String, String> ht = table.getSqlField();
                        tableSql.append("DELETE FROM TableData WHERE tableName='" + tableName + "';\n");
                        tableSql.append("INSERT INTO TableData(" + ht.get("field") + ")VALUES(" + ht.get("value") + ");\n");

                        Vector<String> fields = StringHelper.string2Vector(tableField, ";");
                        List<FieldData> flist = dictionaryService.selectFieldByIds(fields);
                        fieldlist.addAll(flist);
                        mySql.append("DROP TABLE IF EXISTS `" + tableName + "`;\n");
                        mySql.append(MySqlUtil.getCreateSql(tableName,tableTitle, flist)+"\n");
                    }
                }
                sql.append(tableSql);
                sql.append(mySql);
            }
        } catch (Exception e) {
            LogUtil.sysLog("Exception:导出表定义更新SQL");
            e.printStackTrace();
        }
        /**
         * 导出字段定义更新SQL*/
        try {
            for (int i = 0, n = fieldlist.size(); i < n; i++) {
                FieldData field = fieldlist.get(i);
                StringBuffer fieldSql = new StringBuffer();
                fieldSql.append("#导出字段定义更新SQL\n");
                String fieldName = field.getFieldName();
                if (fieldName != null && !fieldName.equals("")) {
                    Hashtable<String, String> ht = field.getSqlField();
                    fieldSql.append("DELETE FROM FieldData WHERE fieldName='" + fieldName + "';\n");
                    fieldSql.append("INSERT INTO FieldData(" + ht.get("field") + ")VALUES(" + ht.get("value") + ");\n");
                    sql.append(fieldSql);
                }
            }
        } catch (Exception e) {
            LogUtil.sysLog("Exception:导出字段定义更新SQL");
            e.printStackTrace();
        }
        return sql.toString();
    }
}
