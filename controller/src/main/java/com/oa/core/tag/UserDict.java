package com.oa.core.tag;

import com.oa.core.bean.system.RoleDefines;
import com.oa.core.bean.user.UserManager;
import com.oa.core.service.system.RoleDefinesService;
import com.oa.core.service.user.UserManagerService;
import com.oa.core.util.SpringContextUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 人员的数据字典信息
 * 
 * @author zxd
 * 
 */
public class UserDict {

	private static Map<String, UserManager> data = null;

	static {
		initData();
	}

	public static void resetData() {
		if (data != null) {
			data.clear();
		}
		data = null;
	}

	public static void initData() {
		UserManagerService m = (UserManagerService) SpringContextUtil.getBean("userManagerService");
		List<UserManager> uList = m.selectAll();
		if (uList.size() > 0) {
			data = new HashMap<String, UserManager>();
			for (UserManager u : uList) {
				data.put(u.getUserName(), u);
			}
		}
	}

	/**
	 * 重新从数据库中获取数据
	 * 
	 * @param name 人员的编号
	 */
	public static void initUserData(String name) {
		UserManagerService m = (UserManagerService) SpringContextUtil.getBean("userManagerService");
		UserManager user = m.selectUserById(name);
		if (user != null) {
			data.put(user.getUserName(), user);
		}
	}

	public static UserManager getData(String name) {
		if (data == null) {
			initData();
		}
		if (data != null) {
			UserManager result = data.get(name);
			if (result == null) {
				initUserData(name);
				return data.get(name);
			} else {
				return result;
			}
		}
		return null;
	}

	public static String getName(String name) {
		UserManager user = getData(name);
		if(user!=null){
			return " "+user.getName()+" ";
		}else{
			return " "+name+" ";
		}

	}
    public static String getNames(String names,String split) {
        String name = "";
        String[] name1 = names.split(split);
        for(int i=0;i<name1.length;i++){
			UserManager user = getData(name1[i]);
			if(user!=null){
				name += user.getName()+" ";
			}else{
				name += name1[i]+" ";
			}
        }
        return " "+name;
    }

	public static String getRoleNames(String names,String split) {
		String name = "";
		String[] name1 = names.split(split);
		for(int i=0;i<name1.length;i++){
			RoleDefinesService roleDefinesService = (RoleDefinesService) SpringContextUtil.getBean("roleDefinesService");
			RoleDefines roleDefines = roleDefinesService.selectById(name1[i]);
			if(roleDefines!=null){
				name += roleDefines.getRoleTitle()+" ";
			}else{
				name += name1[i]+" ";
			}
		}
		return " "+name;
	}
}
