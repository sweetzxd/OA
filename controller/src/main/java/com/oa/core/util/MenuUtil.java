package com.oa.core.util;

import com.oa.core.bean.Loginer;
import com.oa.core.bean.system.MyUrlRegist;
import com.oa.core.bean.user.UserComputer;
import com.oa.core.service.system.MyUrlRegistService;
import com.oa.core.service.user.UserComputerService;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class MenuUtil {

    public String getMenu(String user){
        UserComputerService ucservice = (UserComputerService) SpringContextUtil.getBean("userComputerService");
        UserComputer uc = ucservice.selectById(user);
        if(uc!=null) {
            try {
                String menu = uc.getUserMenu();
                String menuhtml = "";
                menu = menu.replaceAll("\\\\","");
                menu = menu.replaceAll("\"\\[","[");
                menu = menu.replaceAll("]\"","]");
                if(menu!=null && !menu.equals("")) {
                    JSONArray json;
                    if(menu.indexOf("topmenu")>=0){
                        JSONObject jsonObject = new JSONObject(menu);
                        json = jsonObject.getJSONArray("topmenu");
                    }else {
                        json = new JSONArray(menu);
                    }
                    for (int i = 0; i < json.length(); i++) {
                        JSONObject subObject = json.getJSONObject(i);
                        String id = subObject.getString("id");
                        String title = subObject.getString("title");
                        String img = "";
                        if(!subObject.isNull("img")){
                            img = subObject.getString("img");
                        }
                        menuhtml += "<li class='layui-nav-item'>";
                        menuhtml += "<a href='javascript:;' lay-tips='"+title+"' lay-direction='2' style='height: 55px;'>";
                        menuhtml += "<img class='layui-icon' src='"+img+"'></i>";
                        menuhtml += "<cite>"+title+"</cite></a>";
                        menuhtml += "<dl class='layui-nav-child'>";
                        if(!subObject.isNull("menus")) {
                            JSONArray jsonArray = subObject.getJSONArray("menus");
                            for (int n = 0; n < jsonArray.length(); n++) {
                                JSONObject subObject2 = jsonArray.getJSONObject(n);
                                String id2 = subObject2.getString("id");
                                String title2 = subObject2.getString("title");
                                String url2 = subObject2.getString("url");
                                menuhtml += "<dd data-name='" + id2 + "' ><a lay-href='" + url2 + "' lay-text='" + title2 + "'>" + title2 + "</a></dd>";
                            }
                        }
                        menuhtml += "</dl></li>";
                    }
                }
                return menuhtml;
            } catch (Exception e) {
                e.printStackTrace();
                return "菜单树异常";
            }
        }
        return "菜单树异常";
    }

    public void setMenu(HttpServletRequest request, JSONArray menu){
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        String userId = null;
        if (loginer != null) {
            userId = loginer.getId();
        }
        UserComputerService ucservice = (UserComputerService) SpringContextUtil.getBean("userComputerService");
        List<UserComputer> uclist = ucservice.selectAll();
        for(int i=0;i<uclist.size();i++){
            try {
                UserComputer uc = uclist.get(i);
                UserComputer upuc = new UserComputer();
                upuc.setComputerId(uc.getComputerId());
                upuc.setBackupMenu(uc.getUserMenu());
                upuc.setUserMenu(menu.toString());
                upuc.setModifyName(userId);
                ucservice.update(upuc);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static String contextMenu(String pageid,String formid){
        try {
            MyUrlRegistService myUrlRegistService = (MyUrlRegistService) SpringContextUtil.getBean("myUrlRegistService");
            String conmenu = myUrlRegistService.selectContextMenu(pageid);
            MyUrlRegist url = myUrlRegistService.selectById(pageid);
            String contextmenu = "";
            if (conmenu != null) {
                JSONArray jsonArray = new JSONArray(conmenu);
                contextmenu = "<a href='/userpage/pageadd.do?pageid=" + pageid + "&formid=" + formid + "'>" + url.getPageTitle() + "</a>";
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String pageId = jsonObject.getString("id");
                    String[] asskey = jsonObject.getString("asskey").split("_");
                    String[] mainasskey = jsonObject.getString("mainasskey").split("_");
                    MyUrlRegist myUrlRegist = myUrlRegistService.selectById(pageId);
                    String pagetitle = "";
                    if (myUrlRegist != null) {
                        formid = myUrlRegist.getFormId();
                        pagetitle = myUrlRegist.getPageTitle();
                    }
                    contextmenu += "<a href='javascript:void(0);' onclick=\"addnav('/userpage/viewpage/" + pageId + ".do?formid=" + formid + "&field=" + asskey[1] + "&value=" + mainasskey[1] + "','" + pageId + "','" + pagetitle + "')\">" + pagetitle + "</a>";
                }
            }
            return contextmenu;
        }catch (Exception e){
            return null;
        }
    }



    public JSONArray getMenuByJson(String user){
        UserComputerService ucservice = (UserComputerService) SpringContextUtil.getBean("userComputerService");
        UserComputer uc = ucservice.selectById(user);
        if(uc!=null) {
            JSONObject json = new JSONObject(uc.getUserMenu());
            return json.getJSONArray("topmenu");
        }else{
            return null;
        }

    }
}
