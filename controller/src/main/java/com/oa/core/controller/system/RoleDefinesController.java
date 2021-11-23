package com.oa.core.controller.system;

import com.oa.core.bean.Loginer;
import com.oa.core.bean.system.RoleDefines;
import com.oa.core.bean.util.PageUtil;
import com.oa.core.helper.DateHelper;
import com.oa.core.interceptor.Logined;
import com.oa.core.listener.InitDataListener;
import com.oa.core.service.system.RoleDefinesService;
import com.oa.core.util.Const;
import com.oa.core.util.PrimaryKeyUitl;
import com.oa.core.util.ToNameUtil;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName:RoleDefinesController
 * @author:zxd
 * @Date:2018/09/17
 * @Time:下午 4:20
 * @Version V1.0
 */

@Controller
@RequestMapping("/role")
public class RoleDefinesController {
    @Autowired
    RoleDefinesService roleDefinesService;

    @RequestMapping(value = "/roledefines", method = RequestMethod.GET)
    public ModelAndView gotoRoleDefines(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("manage/roledefines");
        mav.addObject("type", "list");
        return mav;
    }

    @RequestMapping("/selectAll")
    @ResponseBody
    public String selectAll(HttpServletRequest request,String inputval, String option, int page, int limit){
        PageUtil pu = new PageUtil();
        pu.setPageSize(limit);
        pu.setCurrentPage(page);
        RoleDefines role = new RoleDefines();
        if (inputval != null && inputval != "") {
            role = new RoleDefines(option,inputval);
        }
        role.setStartRow(pu.getStartRow());
        role.setEndRow(pu.getEndRow()-pu.getStartRow());
        List<RoleDefines> roleList = roleDefinesService.selectTerms(role);
        int count = roleDefinesService.selectTermsCount(role);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 0);
        jsonObject.put("msg", "");
        jsonObject.put("count", count);
        jsonObject.put("data", roleList);
        jsonObject.put("success",1);
        return jsonObject.toString();
    }

    @RequestMapping("/gotoinsert")
    public ModelAndView gotoInsert(HttpServletRequest request,RoleDefines roleDefines){
        ModelAndView mav = new ModelAndView("manage/roledefines");
        mav.addObject("title", "新增账号信息");
        mav.addObject("type", "add");
        return mav;
    }

    @Logined
    @RequestMapping(value = "/insertrole", method = RequestMethod.POST, produces = {"text/html;charset=UTF-8;", "application/json;"})
    @ResponseBody
    public String insertUser(HttpServletRequest request, RoleDefines roleDefines) {
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        try {
            PrimaryKeyUitl pk = new PrimaryKeyUitl();
            String roleId = pk.getNextId("RoleDefines", "roleId");
            roleDefines.setRoleId(roleId);
            if (loginer != null) {
                String userId = loginer.getId();
                roleDefines.setRecordName(userId);
                roleDefines.setModifyName(userId);
            }
            roleDefines.setRecordTime(DateHelper.now());
            roleDefines.setModifyTime(DateHelper.now());
            roleDefinesService.insert(roleDefines);
            return "1";
        } catch (Exception e) {
            return "0";
        }
    }

    @RequestMapping("/gotoupdate")
    public ModelAndView gotoUpdate(HttpServletRequest request,String roleid){
        ModelAndView mav = new ModelAndView("manage/roledefines");
        RoleDefines roleDefines = roleDefinesService.selectById(roleid);
        String users = roleDefines.getUserName();
        String user[] = users.split(";");
        List<Map<String,String>> userlist= new ArrayList<Map<String,String>>();
        for(int i=0;i<user.length;i++) {
            Map<String,String> map = new HashMap<String,String>();
            String name = ToNameUtil.getName("user", user[i]);
            map.put("id",user[i]);
            map.put("name",name);
            userlist.add(map);
        }
        mav.addObject("role", roleDefines);
        mav.addObject("userlist", userlist);
        mav.addObject("title", "【"+roleDefines.getRoleTitle()+"】的角色信息修改");
        mav.addObject("type", "modi");
        return mav;
    }

    @Logined
    @RequestMapping(value = "/updaterole", method = RequestMethod.POST, produces = {"text/html;charset=UTF-8;", "application/json;"})
    @ResponseBody
    public String updateUser(HttpServletRequest request, RoleDefines roleDefines) {
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        if (loginer != null) {
            String userId = loginer.getId();
            roleDefines.setModifyName(userId);
        }
        try {
            roleDefines.setModifyTime(DateHelper.now());
            roleDefinesService.update(roleDefines);
            return "1";
        } catch (Exception e) {
            return "0";
        }
    }

    @RequestMapping("/deleterole")
    @ResponseBody
    public String del(HttpServletRequest request,String roleid){
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        RoleDefines roleDefines = new RoleDefines();
        if (loginer != null) {
            String userId = loginer.getId();
            roleDefines.setDeleteName(userId);
        }
        try {
            roleDefines.setDeleteTime(DateHelper.now());
            if(roleid.indexOf(";")>0){
                roleid = roleid.replaceAll(";","','");
                roleid = "'"+roleid+"'";
                roleDefines.setRoleId(roleid);
                roleDefinesService.deletes(roleDefines);
            }else{
                roleDefines.setRoleId(roleid);
                roleDefinesService.delete(roleDefines);
            }
            return "1";
        }catch (Exception e){
            e.printStackTrace();
            return "0";
        }
    }

    @RequestMapping(value = "/gotouserselect", method = RequestMethod.GET)
    public ModelAndView gotoFormSelect(HttpServletRequest request) {
        String thead = "<th lay-data=\"{type:'radio',field:'userName',width:80, fixed: 'left'}\">选择</th>";
        thead+="<th lay-data=\"{field:'staffName', sort: true}\">姓名</th>";
        ModelAndView mav = new ModelAndView("manage/tableselect");
        mav.addObject("thead",thead);
        mav.addObject("url","/user/selectuserinfo.do");
        mav.addObject("id","userName");
        mav.addObject("name","staffName");
        mav.addObject("type","users");
        return mav;
    }

    /**
     * 部门选择处理方法
     */
    @RequestMapping(value = "/gotodeptselect", method = RequestMethod.GET)
    public ModelAndView gotoDeptSelect(HttpServletRequest request) {
        String thead = "<th lay-data=\"{type:'radio',field:'deptId', width:80}\">选择</th>";
        thead += "<th lay-data=\"{field:'deptName'}\">部门名称</th>";
        ModelAndView mav = new ModelAndView("manage/tableselect");
        mav.addObject("thead", thead);
        mav.addObject("url", "/department/selectdeptinput.do");
        mav.addObject("id", "deptId");
        mav.addObject("name", "deptName");
        mav.addObject("inputId", "department");
        mav.addObject("type", "dept");
        return mav;
    }

    @Logined
    @RequestMapping(value = "/loadrole", method = RequestMethod.POST)
    public void loadRole(HttpServletRequest request) {
        new InitDataListener("role");
    }
}
