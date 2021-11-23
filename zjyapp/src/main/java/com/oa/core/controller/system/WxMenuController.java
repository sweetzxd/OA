package com.oa.core.controller.system;

import com.oa.core.bean.Loginer;
import com.oa.core.util.MenuUtil;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

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

    @RequestMapping(value = "/usermenu", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String getMyMenu(HttpServletRequest request,String user){
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        List userMenu = new ArrayList();
        if (loginer != null || user !=null) {
            String userId = user;
            if(user==null){
                userId = loginer.getId();
            }
            MenuUtil mu = new MenuUtil();
            JSONArray menus = mu.getMenuByJson(userId);
            for(int i1=0,len1=menus.length();i1<len1;i1++){
                JSONObject jsonObject1 = menus.getJSONObject(i1);
                if(!jsonObject1.isNull("menus")) {
                    JSONArray menus1 = jsonObject1.getJSONArray("menus");
                    List list2 = new ArrayList();
                    List list3 = new ArrayList();
                    for(int i2=0,len2=menus1.length();i2<len2;i2++){
                        JSONObject jsonObject2 = menus1.getJSONObject(i2);
                        if(!jsonObject2.isNull("menus")){
                            list3.add(jsonObject2);
                        }else if(!jsonObject2.isNull("url")){
                            list2.add(jsonObject2);
                        }
                    }
                    jsonObject1.put("menus",list2);
                    userMenu.add(jsonObject1);
                    userMenu.addAll(list3);
                }
            }
        }
        JSONObject jsonObject = new JSONObject();
        if(userMenu==null){
            jsonObject.put("msg", "获取菜单失败");
            jsonObject.put("success",0);
        }else {
            jsonObject.put("msg", "");
            jsonObject.put("success",1);
            jsonObject.put("data",userMenu);
            jsonObject.put("title","/resources/images/menutitle.jpg");
        }
        return jsonObject.toString();
    }
}
