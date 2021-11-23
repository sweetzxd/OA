package com.oa.core.controller.module;

import com.oa.core.bean.Loginer;
import com.oa.core.bean.module.Filepermission;
import com.oa.core.bean.module.Filetype;
import com.oa.core.bean.system.RoleDefines;
import com.oa.core.bean.util.PageUtil;
import com.oa.core.helper.DateHelper;
import com.oa.core.service.module.FilepermissionService;
import com.oa.core.service.module.FiletypeService;
import com.oa.core.service.system.RoleDefinesService;
import com.oa.core.util.LogUtil;
import com.oa.core.util.PrimaryKeyUitl;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @ClassName:FilepermissionController
 * @author:wsx
 * @Date:2018/11/20
 * @Time:下午 5:31
 * @Version V1.0
 * @Explain文件类型权限管理
 */
@Controller
@RequestMapping("/filepermission")
public class FilepermissionController {

    @Autowired
    private FilepermissionService filepermissionService;
    @Autowired
    private FiletypeService filetypeService;
    @Autowired
    private RoleDefinesService roleDefinesService;

    @RequestMapping(value = "/gotofilepermission", method = RequestMethod.GET)
    public ModelAndView gotoFilepermission(HttpServletRequest request,String type,String permissionId) {
        ModelAndView mav = new ModelAndView("module/filepermission");
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        Map<String,String> filetypeMap = getFiletypes(request);
        List<RoleDefines> roleList = roleDefinesService.selectAll();
        if(type.equals("edit")&&permissionId!=null&&!permissionId.equals("")){
            Filepermission filepermission = filepermissionService.selectById(permissionId);
            mav.addObject("filepermission",filepermission);
        }
        mav.addObject("roleList",roleList);
        mav.addObject("filetypeMap",filetypeMap);
        mav.addObject("type", type);
        return mav;
    }

    @RequestMapping("/selectlist")
    @ResponseBody
    public String getFilepermissionList(HttpServletRequest request, String inputval, String option, int page, int limit) {
        PageUtil pu = new PageUtil();
        pu.setPageSize(limit);
        pu.setCurrentPage(page);
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        List<String> userRoleList = roleDefinesService.getRoleIds(loginer.getId());
        Filetype filetype = new Filetype();
        filetype.setList(userRoleList);
        List<Filetype> filetypeList = filetypeService.selectAllTerms(filetype);
        List<String> list = new ArrayList<>();
        for(Filetype filetype1 : filetypeList){
            list.add(filetype1.getFileTypeId());
        }
        Filepermission filepermission = new Filepermission();
        filepermission.setList(list);
        filepermission.setStartRow(pu.getStartRow());
        filepermission.setEndRow(pu.getEndRow()-pu.getStartRow());
        if (inputval != null && inputval != "") {
            if ("fileTypeId".equals(option)) {
                filepermission.setFileTypeId(inputval);
            }
        }
        int count = filepermissionService.selectAllTermsCont(filepermission);
        List<Filepermission> filepermissionList = filepermissionService.selectAllTerms(filepermission);
        for(Filepermission filepermission1 : filepermissionList){
            Filetype filetype2 = filetypeService.selectById(filepermission1.getFileTypeId());
            if(filetype2!=null){
                filepermission1.setFileTypeId(filetype2.getFileTypeName());
            }
            if(filepermission1.getActorType().equals("roleactor")){
                filepermission1.setActorType("角色定义");
            }
            RoleDefines roleDefines = roleDefinesService.selectById(filepermission1.getContextValue());
            if(roleDefines!=null){
                filepermission1.setContextValue(roleDefines.getRoleTitle());
            }
            if(filepermission1.getSee()==1){
                filepermission1.setSeeStr("<input type='checkbox' id='"+filepermission1.getPermissionId()+"' lay-skin='switch' lay-text='是|否' lay-filter='seeStatus' checked='checked'>");
            }else if(filepermission1.getSee()==2){
                filepermission1.setSeeStr("<input type='checkbox' id='"+filepermission1.getPermissionId()+"' lay-skin='switch' lay-text='是|否' lay-filter='seeStatus'>");
            }
            if(filepermission1.getDownload()==1){
                filepermission1.setDownloadStr("<input type='checkbox' id='"+filepermission1.getPermissionId()+"' lay-skin='switch' lay-text='是|否' lay-filter='downloadStatus' checked='checked'>");
            }else if(filepermission1.getDownload()==2){
                filepermission1.setDownloadStr("<input type='checkbox' id='"+filepermission1.getPermissionId()+"' lay-skin='switch' lay-text='是|否' lay-filter='downloadStatus'>");
            }
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 0);
        jsonObject.put("msg", "");
        jsonObject.put("count", count);
        jsonObject.put("data", filepermissionList);
        jsonObject.put("success",1);
        jsonObject.put("is", true);
        jsonObject.put("tip", "操作成功");

        return jsonObject.toString();
    }

    @RequestMapping(value = "/insert", method = RequestMethod.POST, produces = {"text/html;charset=UTF-8;", "application/json;"})
    @ResponseBody
    public String insertSave(HttpServletRequest request, Filepermission filepermission) {
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        String userId = loginer.getId();
        //判断是否重复插入
        Filepermission filepermission1 = new Filepermission();
        filepermission1.setFileTypeId(filepermission.getFileTypeId());
        filepermission1.setContextValue(filepermission.getContextValue());
        int count = filepermissionService.selectAllTermsCont(filepermission1);
        if(count==1){
            return "-1";
        }else{
            try {
                PrimaryKeyUitl pk = new PrimaryKeyUitl();
                String permissionId = pk.getNextId("filepermission", "permissionId");
                filepermission.setPermissionId(permissionId);
                filepermission.setRecordName(userId);
                filepermission.setModifyName(userId);
                filepermission.setRecordTime(DateHelper.now());
                filepermission.setModifyTime(DateHelper.now());
                filepermissionService.insert(filepermission);
                //插入相关二级类型
                Filetype filetype = filetypeService.selectById(filepermission.getFileTypeId());
                if(filetype.getSuperiorFileTypeId()==null||filetype.getSuperiorFileTypeId().equals("")){
                    List<String> userRoleList = roleDefinesService.getRoleIds(userId);
                    Filetype filetype1 = new Filetype();
                    filetype1.setSuperiorFileTypeId(filepermission.getFileTypeId());
                    filetype1.setList(userRoleList);
                    List<Filetype> filetypeList = filetypeService.selectAllTerms(filetype1);
                    if(filetypeList!=null){
                        for(Filetype filetype2 : filetypeList){
                            Filepermission filepermission2 = new Filepermission();
                            filepermission2.setFileTypeId(filetype2.getFileTypeId());
                            filepermission2.setContextValue(filepermission.getContextValue());
                            int count2 = filepermissionService.selectAllTermsCont(filepermission2);
                            if(count2==0){
                                PrimaryKeyUitl pk2 = new PrimaryKeyUitl();
                                String permissionId2 = pk2.getNextId("filepermission", "permissionId");
                                filepermission2.setPermissionId(permissionId2);
                                filepermission2.setActorType("roleactor");
                                filepermission2.setSee(1);
                                filepermission2.setDownload(1);
                                filepermission2.setRecordName(userId);
                                filepermission2.setModifyName(userId);
                                filepermission2.setRecordTime(DateHelper.now());
                                filepermission2.setModifyTime(DateHelper.now());
                                filepermissionService.insert(filepermission2);
                            }
                        }
                    }
                }
                return "1";
            } catch (Exception e) {
                return "0";
            }
        }
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST, produces = {"text/html;charset=UTF-8;", "application/json;"})
    @ResponseBody
    public String updateSave(HttpServletRequest request, Filepermission filepermission) {
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        String userId = loginer.getId();
        String permissionId = filepermission.getPermissionId();
        Filepermission filepermission1 = filepermissionService.selectById(permissionId);
        if(filepermission.getFileTypeId().equals(filepermission1.getFileTypeId())&&filepermission.getContextValue().equals(filepermission1.getContextValue())){
            try {
                filepermission.setModifyName(userId);
                filepermission.setModifyTime(DateHelper.now());
                filepermissionService.update(filepermission);
                return "1";
            } catch (Exception e) {
                LogUtil.sysLog("Exception:"+e);
                return "0";
            }
        }else{
            Filepermission filepermission2 = new Filepermission();
            filepermission2.setFileTypeId(filepermission.getFileTypeId());
            filepermission2.setContextValue(filepermission.getContextValue());
            int count = filepermissionService.selectAllTermsCont(filepermission2);
            if(count==1){
                return "-1";
            }else{
                try {
                    filepermission.setModifyName(userId);
                    filepermission.setModifyTime(DateHelper.now());
                    filepermissionService.update(filepermission);
                    //插入相关二级类型
                    Filetype filetype = filetypeService.selectById(filepermission.getFileTypeId());
                    if(filetype.getSuperiorFileTypeId()==null||filetype.getSuperiorFileTypeId().equals("")){
                        List<String> userRoleList = roleDefinesService.getRoleIds(userId);
                        Filetype filetype1 = new Filetype();
                        filetype1.setSuperiorFileTypeId(filepermission.getFileTypeId());
                        filetype1.setList(userRoleList);
                        List<Filetype> filetypeList = filetypeService.selectAllTerms(filetype1);
                        if(filetypeList!=null){
                            for(Filetype filetype2 : filetypeList){
                                Filepermission filepermission3 = new Filepermission();
                                filepermission3.setFileTypeId(filetype2.getFileTypeId());
                                filepermission3.setContextValue(filepermission.getContextValue());
                                int count2 = filepermissionService.selectAllTermsCont(filepermission3);
                                if(count2==0){
                                    PrimaryKeyUitl pk2 = new PrimaryKeyUitl();
                                    String permissionId2 = pk2.getNextId("filepermission", "permissionId");
                                    filepermission3.setPermissionId(permissionId2);
                                    filepermission3.setActorType("roleactor");
                                    filepermission3.setSee(1);
                                    filepermission3.setDownload(1);
                                    filepermission3.setRecordName(userId);
                                    filepermission3.setModifyName(userId);
                                    filepermission3.setRecordTime(DateHelper.now());
                                    filepermission3.setModifyTime(DateHelper.now());
                                    filepermissionService.insert(filepermission3);
                                }
                            }
                        }
                    }
                    return "1";
                } catch (Exception e) {
                    LogUtil.sysLog("Exception:"+e);
                    return "0";
                }
            }
        }

    }

    @RequestMapping(value = "/deletes", method = RequestMethod.POST, produces = {"text/html;charset=UTF-8;", "application/json;"})
    @ResponseBody
    public String deletes(HttpServletRequest request, String permissionId) {
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        try {
            String userId = loginer.getId();
            permissionId = permissionId.replaceAll(";","','");
            permissionId = "'"+permissionId.substring(0,permissionId.length()-2);
            filepermissionService.deletes(permissionId,userId,DateHelper.now());
            return "1";
        } catch (Exception e) {
            LogUtil.sysLog("Exception:"+e);
            return "0";
        }
    }

    @RequestMapping(value = "/getFiletypes", method = RequestMethod.POST, produces = {"text/html;charset=UTF-8;", "application/json;"})
    @ResponseBody
    public HashMap<String,String> getFiletypes(HttpServletRequest request){
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        List<String> userRoleList = roleDefinesService.getRoleIds(loginer.getId());
        List<Filetype> list = filetypeService.getFiletypesByRoleId(userRoleList);
        HashMap<String,String> hm = new HashMap<>();
        HashMap<String,HashMap<String,String>> hms = new HashMap<>();
        for (Filetype filetype : list){
            String fileTypeId = filetype.getFileTypeId();
            String fileTypeName = filetype.getFileTypeName();
            String superiorFileTypeId = filetype.getSuperiorFileTypeId();
            if(superiorFileTypeId==null || superiorFileTypeId.equals("")){
                hm.put(fileTypeId,fileTypeName);
            }else{
                HashMap<String,String> map = hms.get(superiorFileTypeId);
                if(map==null){
                    map = new HashMap<>();
                }
                map.put(fileTypeId,fileTypeName);
                hms.put(superiorFileTypeId,map);
            }
        }
        HashMap<String,String> filetype = new LinkedHashMap<>();
        for (Map.Entry<String, String> entry : hm.entrySet()) {
            String fileTypeId = entry.getKey();
            String fileTypeName = entry.getValue();
            filetype.put(fileTypeId,fileTypeName);
            for(Map.Entry<String, String> z : hms.get(fileTypeId).entrySet()){
                String id = z.getKey();
                String name = "&nbsp;&nbsp;&nbsp;&nbsp;"+z.getValue();
                filetype.put(id,name);
                hms.remove(fileTypeId);
            }
        }
        for(Map.Entry<String, HashMap<String,String>> map : hms.entrySet()){
            for (Map.Entry<String, String> map1 : map.getValue().entrySet()){
                String id = map1.getKey();
                String name = map1.getValue();
                filetype.put(id,name);
            }
        }
        return filetype;
    }

    //查看、下载授权
    @RequestMapping(value = "/impower", method = RequestMethod.POST, produces = {"text/html;charset=UTF-8;", "application/json;"})
    @ResponseBody
    public String impowerSave(HttpServletRequest request, Filepermission filepermission) {
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        String userId = loginer.getId();
        try {
            filepermission.setModifyName(userId);
            filepermission.setModifyTime(DateHelper.now());
            filepermissionService.update(filepermission);
            return "1";
        } catch (Exception e) {
            LogUtil.sysLog("Exception:"+e);
            return "0";
        }
    }

}
