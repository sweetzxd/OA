package com.oa.core.controller.util;

import com.oa.core.bean.Loginer;
import com.oa.core.bean.user.UserManager;
import com.oa.core.service.system.RoleDefinesService;
import com.oa.core.service.user.UserManagerService;
import com.oa.core.util.SpringContextUtil;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Hashtable;
import java.util.List;

/**
 * @ClassName:SessionUtil
 * @author:zxd
 * @Date:2019/04/03
 * @Time:下午 4:36
 * @Version V1.0
 * @Explain
 */
public class LoginUtil {

    private static Hashtable<String, Loginer> loginers = new Hashtable<>();
    private static Hashtable<String, String> roles = new Hashtable<>();

    public static Loginer getLoginer(String userid){
        if(userid==null){
            return null;
        }
        if(loginers ==null || loginers.get(userid)==null) {
            applog(userid);
        }
        return loginers.get(userid);
    }

    public static void setLoginer(String userid,Loginer loginer){
        loginers.put(userid,loginer);
    }

    public static void setRole(String userid,String role){
        roles.put(userid,role);
    }

    public static String getRole(String userid){
        if(userid==null){
            return null;
        }
        if(roles ==null || roles.get(userid)==null) {
            applog(userid);
        }
        return roles.get(userid);
    }

    public static void applog(String userid){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        UserManagerService u = (UserManagerService) SpringContextUtil.getBean("userManagerService");
        RoleDefinesService r = (RoleDefinesService) SpringContextUtil.getBean("roleDefinesService");
        UserManager user = u.selectUserById(userid);
        List<String> roleList = r.getRoleIds(user.getUserName());
        String role = "notrole;";
        for (int i = 0; i < roleList.size(); i++) {
            role += roleList.get(i) + ";";
        }
        Loginer loginer = new Loginer(user);
        request.getSession().setAttribute("loginer", loginer);
        request.getSession().setAttribute("role", role);
        LoginUtil.setLoginer(userid,loginer);
        LoginUtil.setRole(userid,role);
    }
}
