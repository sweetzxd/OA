package com.oa.core.controller.system;

import com.oa.core.bean.Loginer;
import com.oa.core.bean.system.AccessRights;
import com.oa.core.bean.system.MyUrlRegist;
import com.oa.core.helper.DateHelper;
import com.oa.core.service.system.AccessRightsService;
import com.oa.core.service.system.MyUrlRegistService;
import com.oa.core.util.AccessUtil;
import com.oa.core.util.PrimaryKeyUitl;
import com.oa.core.util.TreeUtil;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;

/**
 * @ClassName:AccessRightsController
 * @Author:zxd
 * @Date:2018/09/18
 * @Time:下午 2:59
 * @Version V1.0
 * @Explain 权限管理
 */
@Controller
@RequestMapping("/access")
public class AccessRightsController {
	
    @Autowired
    MyUrlRegistService myUrlRegistService;
    @Autowired
    AccessRightsService accessRightsService;

    @RequestMapping(value = "/accessrights", method = RequestMethod.GET)
    public ModelAndView gotoAccessRights(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("manage/accessrights");
        mav.addObject("type", "list");
        return mav;
    }

    @RequestMapping(value = "/gotoinsert", method = RequestMethod.GET)
    public ModelAndView gotoInsert(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("manage/accessrights");
        mav.addObject("type", "add");
        return mav;
    }

    @RequestMapping(value = "/accesstree", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String gotoAccessTree(HttpServletRequest request) {
        /*AccessUtil au = new AccessUtil();
        List<MyUrlRegist> myurllist = myUrlRegistService.selectAll();
        return AccessUtil.getMenu(myurllist);*/
        return TreeUtil.getTree().toString();
    }

    @RequestMapping(value = "/selectAccess", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String selectAccess(HttpServletRequest request, String pageid,String pagetitle) {
        List<Map<String, String>> access = accessRightsService.selectAccess(pageid);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 0);
        jsonObject.put("msg", "");
        jsonObject.put("count", access.size());
        jsonObject.put("data", access);
        jsonObject.put("success",1);
        jsonObject.put("pageid", pageid);
        jsonObject.put("pagetitle", pagetitle);
        return jsonObject.toString();
    }

    @RequestMapping(value = "/gotoroleselect", method = RequestMethod.GET)
    public ModelAndView gotoRoleSelect(HttpServletRequest request) {
        String thead = "<th lay-data=\"{type:'radio',field:'roleId',width:80, fixed: 'left'}\">选择</th>";
        thead+="<th lay-data=\"{field:'roleTitle', sort: true}\">角色名称</th>";
        thead+="<th lay-data=\"{field:'userName', sort: true}\">角色人员</th>";
        ModelAndView mav = new ModelAndView("manage/tableselect");
        mav.addObject("thead",thead);
        mav.addObject("url","/role/selectAll.do");
        mav.addObject("id","roleId");
        mav.addObject("name","roleTitle");
        return mav;
    }

    @RequestMapping(value = "/insertaccess", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String gotoInsertAccess(HttpServletRequest request, @RequestBody String datas) {
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        String userId = loginer.getId() == null ? "" : loginer.getId();
        String[] type = new String[]{"info", "add", "modi", "delete", "import", "export", "send"};
        try {
            String ds = URLDecoder.decode(datas.substring(datas.indexOf("=") + 1, datas.length()), "utf-8");
            JSONObject jsonobj = new JSONObject(ds);
            String pageid = jsonobj.getString("pageid");
            String roleid = jsonobj.getString("roleid");

            AccessRights ar = new AccessRights();
            ar.setPageId(pageid);
            ar.setRoleId(roleid);
            List<AccessRights> access = accessRightsService.selectTerms(ar);
            if(access.size()<=0) {
                PrimaryKeyUitl pk = new PrimaryKeyUitl();
                String accessId = pk.getNextId("accessrights", "accessId");
                ar.setAccessId(accessId);
                ar.setAccessType("info");
                ar.setRecordName(userId);
                ar.setRecordTime(DateHelper.now());
                ar.setModifyName(userId);
                ar.setModifyTime(DateHelper.now());
                accessRightsService.insert(ar);
                return accessId;
            }else{
                return "2";
            }
        } catch (UnsupportedEncodingException e) {
            return "0";
        }
    }

    @RequestMapping(value = "/updateaccess", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String gotoUpdateAccess(HttpServletRequest request, @RequestBody String datas) {
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        String userId = loginer.getId() == null ? "" : loginer.getId();
        String[] type = new String[]{"info", "add", "modi", "delete", "import", "export", "send"};
        try {
            String ds = URLDecoder.decode(datas.substring(datas.indexOf("=") + 1, datas.length()), "utf-8");
            JSONObject jsonobj = new JSONObject(ds);
            String pageid = jsonobj.getString("pageid");
            String roleid = jsonobj.getString("roleid");
            JSONArray jsonArray = jsonobj.getJSONArray("data");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = jsonArray.getJSONObject(i);
                String key = json.getString("key");
                String value = json.getString("value");
                if ("0".equals(key)) {
                    if ("是".equals(value)) {
                        AccessRights ar = new AccessRights();
                        PrimaryKeyUitl pk = new PrimaryKeyUitl();
                        String accessId = pk.getNextId("accessrights", "accessId");
                        ar.setAccessId(accessId);
                        ar.setPageId(pageid);
                        ar.setRoleId(roleid);
                        ar.setAccessType(type[i]);
                        ar.setRecordName(userId);
                        ar.setRecordTime(DateHelper.now());
                        ar.setModifyName(userId);
                        ar.setModifyTime(DateHelper.now());
                        accessRightsService.insert(ar);
                    }
                } else {
                    if ("否".equals(value)) {
                        AccessRights ar = new AccessRights();
                        ar.setAccessId(key);
                        ar.setDeleteName(userId);
                        ar.setDeleteTime(DateHelper.now());
                        accessRightsService.deleteTerms(ar);
                    }
                }
            }
            return "1";
        } catch (UnsupportedEncodingException e) {
            return "0";
        }
    }

    @RequestMapping(value = "/deleteaccess", method = RequestMethod.POST)
    @ResponseBody
    public String gotoDeleteAccess(HttpServletRequest request, String data) {
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        String userId = loginer.getId() == null ? "" : loginer.getId();
        try {
            if(data!=null && data!="") {
                AccessRights ar = new AccessRights();
                ar.setPageId(data);
                ar.setDeleteName(userId);
                ar.setDeleteTime(DateHelper.now());
                accessRightsService.deletePageid(ar);
            }
            return "1";
        } catch (Exception e) {
            return "0";
        }
    }

    @RequestMapping(value = "/specialsetup", method = RequestMethod.GET)
    public ModelAndView gotoSpecialSetup(HttpServletRequest request){
        String pageid = request.getParameter("pageid");
        String roleid  = request.getParameter("roleid");
        ModelAndView model = new ModelAndView("manage/template/specialsetup");
        AccessRights access = accessRightsService.selectTerm(pageid,"'"+roleid+"'","special");
        if(access==null){
            access = new AccessRights();
            access.setPageId(pageid);
            access.setRoleId(roleid);
        }
        model.addObject("access",access);
        return model;
    }

    @RequestMapping(value = "/savesprcialsetup", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String saveSpecialSetup(HttpServletRequest request) {
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        String userId = loginer.getId() == null ? "" : loginer.getId();
        try {

            String special = request.getParameter("special");
            String pageid = request.getParameter("pageid");
            String roleid = request.getParameter("roleid");

            AccessRights access = accessRightsService.selectTerm(pageid,"'"+roleid+"'","special");
            if (access==null) {
                PrimaryKeyUitl pk = new PrimaryKeyUitl();
                String accessId = pk.getNextId("accessrights", "accessId");
                access = new AccessRights();
                access.setAccessId(accessId);
                access.setPageId(pageid);
                access.setRoleId(roleid);
                access.setAccessType("special");
                access.setAccessValue(special);
                access.setRecordName(userId);
                access.setRecordTime(DateHelper.now());
                access.setModifyName(userId);
                access.setModifyTime(DateHelper.now());
                accessRightsService.insert(access);
            } else {
                access.setAccessValue(special);
                access.setModifyName(userId);
                access.setModifyTime(DateHelper.now());
                accessRightsService.update(access);
            }
            return "1";
        }catch (Exception e){
            e.getLocalizedMessage();
            System.out.println(e.getMessage());
            return "0";
        }
    }
}
