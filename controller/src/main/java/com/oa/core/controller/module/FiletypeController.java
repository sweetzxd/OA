package com.oa.core.controller.module;

import com.oa.core.bean.Loginer;
import com.oa.core.bean.module.Filetype;
import com.oa.core.bean.system.RoleDefines;
import com.oa.core.helper.DateHelper;
import com.oa.core.interceptor.Logined;
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
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName:FileTypeController
 * @author:wsx
 * @Date:2018/11/19
 * @Time:上午 11:45
 * @Version V1.0
 * @Explain文件类型管理
 */
@Controller
@RequestMapping("/filetype")
public class FiletypeController {

    @Autowired
    private FiletypeService filetypeService;

    @Autowired
    private RoleDefinesService roleDefinesService;

    @RequestMapping(value = "/gotofiletype", method = RequestMethod.GET)
    public ModelAndView gotoFiletype(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("module/filetype");
        mav.addObject("type", "list");
        return mav;
    }

    @RequestMapping("/selectlist")
    @ResponseBody
    public String getFiletypeList(HttpServletRequest request, String inputval, String option) {
        Filetype filetype = new Filetype();
        if (inputval != null && inputval != "") {
            if ("fileTypeName".equals(option)) {
                filetype.setFileTypeName(inputval);
            } else if ("superiorDeptId".equals(option)) {
                filetype.setSuperiorFileTypeId(inputval);
            }
        }
        int count = filetypeService.selectAllTermsCont(filetype);
        List<Filetype> filetypeList = filetypeService.selectAllTerms(filetype);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 0);
        jsonObject.put("msg", "");
        jsonObject.put("count", count);
        jsonObject.put("data", filetypeList);
        jsonObject.put("success",1);
        jsonObject.put("is", true);
        jsonObject.put("tip", "操作成功");

        return jsonObject.toString();
    }

    @RequestMapping(value = "/gotoadd", method = RequestMethod.GET)
    public ModelAndView gotoFiletypeAdd(HttpServletRequest request, String pid) {
        String parent = "";
        if(!pid.equals("")&&pid!=null){
            parent = pid;
        }
        ModelAndView mav = new ModelAndView("module/filetype");
        mav.addObject("parent", parent);
        mav.addObject("type", "add");
        return mav;
    }

    @RequestMapping(value = "/gotoedit", method = RequestMethod.GET)
    public ModelAndView gotoFiletypeEdit(HttpServletRequest request, String fileTypeId) {
        ModelAndView mav = new ModelAndView("module/filetype");
        Filetype filetype = filetypeService.selectById(fileTypeId);
        List<RoleDefines> roleList = new ArrayList<>();
        if(filetype.getRoleId()!=""&&!filetype.getRoleId().equals("")){
            String[] roles = filetype.getRoleId().split(";");
            for(String roleId : roles){
                RoleDefines roleDefines = roleDefinesService.selectById(roleId);
                roleList.add(roleDefines);
            }
        }
        mav.addObject("roleList",roleList);
        mav.addObject("filetype", filetype);
        mav.addObject("type", "edit");
        return mav;
    }

    @Logined
    @RequestMapping(value = "/insert", method = RequestMethod.POST, produces = {"text/html;charset=UTF-8;", "application/json;"})
    @ResponseBody
    public String insertSave(HttpServletRequest request, Filetype filetype) {
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        String userId = loginer.getId();
        try {
            PrimaryKeyUitl pk = new PrimaryKeyUitl();
            String fileTypeId = pk.getNextId("filetype", "fileTypeId");
            filetype.setFileTypeId(fileTypeId);
            filetype.setRecordName(userId);
            filetype.setModifyName(userId);
            filetype.setRecordTime(DateHelper.now());
            filetype.setModifyTime(DateHelper.now());
            filetypeService.insert(filetype);
            return "1";
        } catch (Exception e) {
            return "0";
        }
    }

    @Logined
    @RequestMapping(value = "/update", method = RequestMethod.POST, produces = {"text/html;charset=UTF-8;", "application/json;"})
    @ResponseBody
    public String updateSave(HttpServletRequest request, Filetype filetype) {
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        String userId = loginer.getId();
        try {
            filetype.setModifyName(userId);
            filetype.setModifyTime(DateHelper.now());
            filetypeService.update(filetype);
            //更新相关二级类型
            if(filetype.getSuperiorFileTypeId()==null||filetype.getSuperiorFileTypeId().equals("")){
                Filetype newFiletype = new Filetype();
                newFiletype.setSuperiorFileTypeId(filetype.getFileTypeId());
                List<Filetype> filetypeList = filetypeService.selectAllTerms(newFiletype);
                if(filetypeList!=null){
                    for(Filetype filetype1 :filetypeList){
                        String roles1 = filetype1.getRoleId();
                        String roles11 = filetype1.getRoleId();
                        String roles = filetype.getRoleId();
                        String[] role = roles.split(";");
                        for(String role2 : role){
                            if(roles1.indexOf(role2)==-1){
                                roles1 += role2 + ";";
                            }
                        }
                        if(!roles1.equals(roles11)){
                            filetype1.setRoleId(roles1);
                            filetypeService.update(filetype1);
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

    @Logined
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public ModelAndView deleteSave(HttpServletRequest request, String fileTypeId) {
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        try {
            String userId = loginer.getId();
            filetypeService.delete(fileTypeId,userId,DateHelper.now());
        } catch (Exception e) {
            LogUtil.sysLog("Exception:"+e);
        }
        return new ModelAndView("redirect:/filetype/gotofiletype.do");
    }

    //判断该类型下是否存在二级类型
    @Logined
    @RequestMapping(value = "/isExistNext", method = RequestMethod.POST, produces = {"text/html;charset=UTF-8;", "application/json;"})
    @ResponseBody
    public int isExistNext(HttpServletRequest request, Filetype filetype){
        return filetypeService.selectAllTermsCont(filetype);
    }
}
