package com.oa.core.controller.system;

import com.oa.core.bean.Loginer;
import com.oa.core.bean.system.Menu;
import com.oa.core.bean.work.WorkFlowDefine;
import com.oa.core.service.system.AccessRightsService;
import com.oa.core.service.system.RoleDefinesService;
import com.oa.core.service.util.TableService;
import com.oa.core.service.work.WorkFlowDefineService;
import com.oa.core.util.AccessUtil;
import com.oa.core.util.ConfParseUtil;
import com.oa.core.util.MapObjUtil;
import com.oa.core.util.MenuUtil;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName:WxMenuController
 * @author:zxd
 * @Date:2019/03/20
 * @Time:下午 3:08
 * @Version V1.0
 * @Explain 获取菜单方法
 */
@Controller
@RequestMapping("/weixin")
public class WxMenuController {
    @Autowired
    TableService tableService;
    @Autowired
    RoleDefinesService roleDefinesService;
    @Autowired
    AccessRightsService accessRightsService;
    @Autowired
    WorkFlowDefineService workFlowDefineService;

    private HashMap<String, List<Menu>> userMap = new HashMap<>();

    @RequestMapping(value = "/usermenu", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String getMyMenu(HttpServletRequest request,String user){
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        JSONArray menus=null;
        if (loginer != null || user !=null) {
            String userId = user;
            if(user==null){
                userId = loginer.getId();
            }
            List userMenu = userMap.get(userId);
            if (userMenu == null) {
                menus = getMenu4(userId);
            }else{
                menus = new JSONArray(userMenu);
            }
        }
        JSONObject jsonObject = new JSONObject();
        if(menus==null){
            jsonObject.put("msg", "获取菜单失败");
            jsonObject.put("success",0);
        }else {
            jsonObject.put("msg", "");
            jsonObject.put("success",1);
            jsonObject.put("data",menus);
            jsonObject.put("title","/resources/images/menutitle.jpg");
        }
        return jsonObject.toString();
    }



    /**
     * @method:
     * @param:
     * @return:
     * @author: zxd
     * @date: 2019/07/04
     * @description: 新的生成APP端菜单树方法（排除掉非定制页面）
     */
    public JSONArray getMenu4(String userName){
        ConfParseUtil cpu = new ConfParseUtil();
        boolean srvicehtml = "true".equals(cpu.getPoa("open_servicehtml"));
        List<Menu> plist = null;
        try {
            List<String> roleids = roleDefinesService.getRoleIds(userName);
            roleids.add("allemployees");
            String rolestr = StringUtils.join(roleids, "','");
            rolestr = "'" + rolestr + "'";
            String where = "";
            if (!"admin".equals(userName) && rolestr.length() > 3) {
                List<String> pages = accessRightsService.selectPageids(rolestr);
                String page = StringUtils.join(pages, "','");
                where = "pageid in('" + page + "') and ";
            }else if ("admin".equals(userName)){
                where = "pageid !='topmenu' and ";
            }
            where += " (formtype =0 or formtype =1 or formtype =2) and menuNum !=100 and curStatus=2 ";

            String files = "pageid as id,parentid as pid, pageTitle as title,ifnull(formId,'') as fid,ifnull(menuImg,'') as img,formType as type,menuNum as num";
            if("admin".equals(userName) || where.length()>3){
                String sql = "SELECT "+files+" FROM MyUrlRegist WHERE "+where+"  order by security asc";
                plist = MapObjUtil.ListMapToListBean(tableService.selectSqlMapList(sql),Menu.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(plist!=null) {
            int len = plist.size(), i = 0, j = 0;
            Map<String, Menu> nodeMap = new HashMap<>(len);
            for (; i < len; i++) {
                Menu menu = plist.get(i);
                int type = menu.getType();
                if(type == 1){
                    if(!srvicehtml){
                        menu.setUrl("");
                    }else {
                        menu.setUrl("/userpage/viewpage/" + menu.getId() + ".do");
                    }
                } else if (type == 2) {
                    try {
                        WorkFlowDefine wkflw = workFlowDefineService.selectByPageId(menu.getId());
                        String wkflwId = wkflw.getWkflwID();
                        if(!srvicehtml) {
                            menu.setUrl("");
                        }else{
                            menu.setUrl("/flowpage/flowviewpage/" + menu.getFid() + ".do?wkflwId=" + wkflwId);
                        }
                    } catch (Exception e) {
                        e.getLocalizedMessage();
                    }
                }
                if(type!=3) {
                    nodeMap.put(menu.getId(), menu);
                }
            }

            List<Menu> result = new ArrayList<>(len);
            for (; j < len; j++) {
                Menu curNode = plist.get(j), parentNode = nodeMap.get(curNode.getPid());
                if (parentNode == null) {
                    result.add(curNode);
                } else {
                    if (parentNode.getMenus() == null) {
                        parentNode.setMenus(new ArrayList<Menu>());
                    }
                    parentNode.getMenus().add(curNode);
                }
            }
            return new JSONArray(result);
        }else{
            return null;
        }
    }
}
