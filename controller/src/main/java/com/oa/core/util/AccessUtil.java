package com.oa.core.util;

import com.oa.core.bean.Loginer;
import com.oa.core.bean.system.AccessRights;
import com.oa.core.bean.system.FormCustomMade;
import com.oa.core.bean.system.Menu;
import com.oa.core.bean.system.MyUrlRegist;
import com.oa.core.bean.user.UserManager;
import com.oa.core.bean.work.WorkFlowDefine;
import com.oa.core.helper.StringHelper;
import com.oa.core.service.system.AccessRightsService;
import com.oa.core.service.system.FormCustomMadeService;
import com.oa.core.service.system.MyUrlRegistService;
import com.oa.core.service.system.RoleDefinesService;
import com.oa.core.service.user.UserComputerService;
import com.oa.core.service.util.TableService;
import com.oa.core.service.work.WorkFlowDefineService;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @ClassName:AccessUtil
 * @author:zxd
 * @Date:2018/11/15
 * @Time:下午 4:54
 * @Version V1.0
 * @Explain 权限工具类
 */
public class AccessUtil {

    private static FormCustomMadeService formCustomMadeService = (FormCustomMadeService) SpringContextUtil.getBean("formCustomMadeService");
    private static UserComputerService userComputerService = (UserComputerService) SpringContextUtil.getBean("userComputerService");
    private static RoleDefinesService roleDefinesService = (RoleDefinesService) SpringContextUtil.getBean("roleDefinesService");
    private static AccessRightsService accessRightsService = (AccessRightsService) SpringContextUtil.getBean("accessRightsService");
    private static MyUrlRegistService myUrlRegistService = (MyUrlRegistService) SpringContextUtil.getBean("myUrlRegistService");
    private static TableService tableService = (TableService) SpringContextUtil.getBean("tableService");
    private static WorkFlowDefineService workFlowDefineService = (WorkFlowDefineService) SpringContextUtil.getBean("workFlowDefineService");

    private static Map<String, Map<String, List<String>>> accmap = new HashMap<String, Map<String, List<String>>>();

    /**
     * 根据每个人不同的权限获取相应的菜单树
     *
     * @param userName 账号
     * @return json格式菜单树
     */
    public String resetMenu(String userName) {
        String menu = "";
        try {
            if ("admin".equals(userName)) {
                List<MyUrlRegist> plist = myUrlRegistService.selectAll();
                menu = nextMenu3(plist);
            } else {
                List<String> roleids = roleDefinesService.getRoleIds(userName);
                roleids.add("allemployees");
                String rolestr = StringUtils.join(roleids, "','");
                rolestr = "'" + rolestr + "'";
                if (rolestr.length() > 3) {
                    List<String> pages = accessRightsService.selectPageids(rolestr);
                    String page = StringUtils.join(pages, "','");
                    page = "'" + page + "'";
                    if (page.length() > 3) {
                        List<MyUrlRegist> plist = myUrlRegistService.selectByIds(page);
                        menu = nextMenu3(plist);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            menu = "";
        }
        return menu;
    }

    /**
     * 根据每个人不同的权限获取相应的菜单树
     *
     * @param plist 所有菜单
     * @param
     * @return json格式菜单树 根据pageid生成的集合
     */
    /*public String nextMenu(List<MyUrlRegist> plist, Map<String, JSONArray> maps) {
        for (int i = 0; i < plist.size(); i++) {
            MyUrlRegist myurl = plist.get(i);
            int menuNum = myurl.getMenuNum();
            String id = myurl.getPageId();
            String pid = myurl.getParentId();
            String title = myurl.getPageTitle();
            String formid = myurl.getFormId();
            if (menuNum == 2) {
                int type = myurl.getFormType();
                String url = "/userpage/viewpage/" + id + ".do";
                if (type == 2) {
                    try {
                        WorkFlowDefine wkflw = workFlowDefineService.selectByPageId(id);
                        String wkflwId = wkflw.getWkflwID();
                        url = "/flowpage/flowviewpage/" + formid + ".do?wkflwId=" + wkflwId;
                    } catch (Exception e) {

                    }
                } else if (type == 3) {
                    FormCustomMade formcm = formCustomMadeService.selectFormCMByID(formid);
                    String page = formcm.getEditPage();
                    if (page.contains(".do")) {
                        url = page;
                    }
                }
                JSONArray jsonArray = maps.get(pid);
                if (jsonArray == null) {
                    jsonArray = new JSONArray();
                }
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", id);
                jsonObject.put("title", title);
                jsonObject.put("url", url);
                if (jsonArray != null && !"null".equals(jsonArray)) {
                    jsonArray.put(jsonObject);
                }
                maps.put(pid, jsonArray);
            }
        }
        JSONArray jsona = new JSONArray();
        for (int i = 0; i < plist.size(); i++) {
            MyUrlRegist myurl = plist.get(i);
            int menuNum = myurl.getMenuNum();
            String id = myurl.getPageId();
            String title = myurl.getPageTitle();
            if (menuNum == 1) {
                String menus = "";
                JSONArray jsonArray = maps.get(id);
                if (jsonArray != null) {
                    menus = jsonArray.toString();
                }
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", id);
                jsonObject.put("title", title);
                jsonObject.put("menus", menus);
                jsona.put(jsonObject);
            }
        }
        return jsona.toString();
    }*/

    public String nextMenu3(List<MyUrlRegist> plist) {
        HashMap<String, List<Menu>> menumap3 = new HashMap<>();
        List<MyUrlRegist> plist2 = new ArrayList<>();
        plist2.addAll(plist);
        for (int i = 0, len = plist.size(); i < len; i++) {
            MyUrlRegist myurl = plist.get(i);
            int menuNum = myurl.getMenuNum();
            String pid = myurl.getParentId();
            String img = myurl.getMenuimg();
            if (menuNum == 3) {
                List<Menu> menuList = menumap3.get(pid);
                if (menuList == null) {
                    menuList = new ArrayList<>();
                }
                Menu menu = new Menu();
                menu.setDataForm(myurl.getPageId(), myurl.getPageTitle(), getPageUrl(myurl), img);
                menuList.add(menu);
                menumap3.put(pid, menuList);
                plist2.remove(myurl);
            }
        }
        List<MyUrlRegist> plist1 = new ArrayList<>();
        plist1.addAll(plist2);
        HashMap<String, List<Menu>> menumap2 = new HashMap<>();
        for (int i = 0, len = plist2.size(); i < len; i++) {
            MyUrlRegist myurl = plist2.get(i);
            int menuNum = myurl.getMenuNum();
            String id = myurl.getPageId();
            String pid = myurl.getParentId();
            String title = myurl.getPageTitle();
            String img = myurl.getMenuimg();
            int type = myurl.getFormType();
            if (menuNum == 2) {
                List<Menu> menuList = menumap2.get(pid);
                if (menuList == null) {
                    menuList = new ArrayList<>();
                }
                Menu menu = new Menu();
                if (type != 0) {
                    menu.setDataForm(id, title, getPageUrl(myurl), img);
                } else {
                    menu.setDataNode(id, title,menumap3.get(id),img);
                }
                menuList.add(menu);
                menumap2.put(pid, menuList);
                plist1.remove(myurl);
            }
        }

        HashMap<String, List<Menu>> menumap1 = new HashMap<>();
        for (int i = 0, len = plist1.size(); i < len; i++) {
            MyUrlRegist myurl = plist1.get(i);
            int menuNum = myurl.getMenuNum();
            String id = myurl.getPageId();
            String pid = myurl.getParentId();
            String title = myurl.getPageTitle();
            int type = myurl.getFormType();
            String img = myurl.getMenuimg();
            if (menuNum == 1) {
                List<Menu> menuList = menumap1.get(pid);
                if (menuList == null) {
                    menuList = new ArrayList<>();
                }
                Menu menu = new Menu();
                if (type != 0) {
                    menu.setDataForm(id, title, getPageUrl(myurl), img);
                } else {
                    menu.setDataNode(id, title,menumap2.get(id),img);
                }
                menuList.add(menu);
                menumap1.put(pid, menuList);
            }
        }
        JSONObject jsonObject = new JSONObject(menumap1);
        return jsonObject.toString();
    }

    public static String getPageUrl(MyUrlRegist myurl) {
        int type = myurl.getFormType();
        String url = "/userpage/viewpage/" + myurl.getPageId() + ".do";
        if (type == 2) {
            try {
                WorkFlowDefine wkflw = workFlowDefineService.selectByPageId(myurl.getPageId());
                String wkflwId = wkflw.getWkflwID();
                url = "/flowpage/flowviewpage/" + myurl.getFormId() + ".do?wkflwId=" + wkflwId;
            } catch (Exception e) {

            }
        } else if (type == 3) {
            FormCustomMade formcm = formCustomMadeService.selectFormCMByID(myurl.getFormId());
            if (formcm != null) {
                String page = formcm.getEditPage();
                if (page.contains(".do")) {
                    url = page;
                }
            }
        }
        return url;
    }

    /*public static JSONArray getChildren(List<MyUrlRegist> myurllist, String parentId) {
        JSONArray jsonArray = new JSONArray();
        MyUrlRegist myurls = new MyUrlRegist();
        myurls.setParentId(parentId);
        myurls.setFormType(0);
        for (MyUrlRegist myurl : myurllist) {
            String id = myurl.getPageId();
            String title = myurl.getPageTitle();
            String formid = myurl.getFormId();
            int type = myurl.getFormType();
            String url = "/userpage/viewpage/" + id + ".do";
            if (type == 2) {
                try {
                    WorkFlowDefine wkflw = workFlowDefineService.selectByPageId(id);
                    String wkflwId = wkflw.getWkflwID();
                    url = "/flowpage/flowviewpage/" + formid + ".do?wkflwId=" + wkflwId;
                } catch (Exception e) {

                }
            } else if (type == 3) {
                FormCustomMade formcm = formCustomMadeService.selectFormCMByID(formid);
                String page = formcm.getEditPage();
                if (page.contains(".do")) {
                    url = page;
                }
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", id);
            jsonObject.put("title", title);
            if (type != 0) {
                jsonObject.put("url", url);
            } else {
                jsonObject.put("menus", getChildren(myurllist, parentId));
            }
            jsonArray.put(jsonObject);
        }
        return jsonArray;
    }*/


    /**
     * 生成菜单树的树状格式
     *
     * @param myurllist 所有菜单
     * @return json格式菜单树
     */
    public static String getMenu(List<MyUrlRegist> myurllist) {
        String menu = "";
        if (myurllist != null) {
            Map<String, Map<String, Object>> lmaps = new HashMap<String, Map<String, Object>>();
            Map<String, Object> maps = new HashMap<String, Object>();
            List<Map<String, Object>> lmap = new ArrayList<Map<String, Object>>();
            Map<String, Object> map = new HashMap<String, Object>();
            for (int n = 0; n < myurllist.size(); n++) {
                MyUrlRegist myurl = myurllist.get(n);
                int menuNum = myurl.getMenuNum();
                String id = myurl.getPageId();
                String pid = myurl.getParentId();
                String title = myurl.getPageTitle();
                if (menuNum == 1) {
                    maps = lmaps.get(id);
                    if (maps == null || maps.size() <= 0) {
                        maps = new HashMap<String, Object>();
                        maps.put("id", id);
                        maps.put("name", title);
                        maps.put("spread", true);
                        maps.put("alias", title);
                        maps.put("menuNum", 1);
                        lmap = new ArrayList<Map<String, Object>>();
                        maps.put("children", lmap);
                    } else {
                        if (maps.get("name") == "") {
                            maps.put("name", title);
                            maps.put("alias", title);
                        }
                    }
                    lmaps.put(id, maps);
                } else if (menuNum == 2) {
                    maps = lmaps.get(pid);
                    if (maps == null || maps.size() <= 0) {
                        maps = new HashMap<String, Object>();
                        maps.put("id", pid);
                        maps.put("name", "");
                        maps.put("spread", true);
                        maps.put("alias", "");
                        maps.put("menuNum", 1);
                        lmap = new ArrayList<Map<String, Object>>();
                        map = new HashMap<String, Object>();
                        map.put("id", id);
                        map.put("name", title);
                        map.put("spread", true);
                        map.put("alias", title);
                        map.put("menuNum", 2);
                        lmap.add(map);
                        maps.put("children", lmap);
                    } else {
                        lmap = (List<Map<String, Object>>) maps.get("children");
                        map = new HashMap<String, Object>();
                        map.put("id", id);
                        map.put("name", title);
                        map.put("spread", true);
                        map.put("alias", title);
                        map.put("menuNum", 2);
                        lmap.add(map);
                        maps.put("children", lmap);
                    }
                    lmaps.put(pid, maps);
                }
            }
            JSONArray json = new JSONArray(lmaps.values());
            menu = json.toString();
        }
        return menu;
    }

    /**
     * 获取个人常用菜单
     */
    public static List<Map<String, Object>> getUserMenuValue(String userId) {
        List<Map<String, Object>> sqlvalue = null;
        if (userId != null && !userId.equals("")) {
            /*String table = "wdcyc18121001";
            List<String> field = new ArrayList<>();
            field.add("cdmc181210001");
            field.add("cdzj181210001");
            field.add("cddz181210001");
            List<String> where = new ArrayList<>();
            where.add("(recordName='" + userId + "' or recordName='all')");
            String delsql = MySqlUtil.getSql(field, table, where, "order by cdpx181210001 asc");*/
            String sql = "select cdmc181210001,cdzj181210001,cddz181210001,IFNULL(menuImg,'/upload/menuimg/其他通用.png') as menuImg from wdcyc18121001 w " +
                    "left join myurlregist m on w.cdzj181210001=m.pageId where (w.recordName='" + userId + "' or w.recordName='all')";
            sqlvalue = tableService.selectSqlMapList(sql);
        }
        return sqlvalue;
    }


    public static void initData() {
        initData("info");
        initData("add");
        initData("modi");
        initData("delete");
        initData("import");
        initData("export");
        initData("send");
    }

    public static void initData(String type) {
        initData(type, null);
    }

    public static void initData(String type, String spageid) {
        if (spageid != null) {
            String sql = "SELECT a.pageid as pageid,r.userName as userName FROM (select pageid,roleId from AccessRights where accessType ='" + type + "' and curStatus=2 and pageid='" + spageid + "' GROUP BY roleid) a join roledefines r on a.roleId = r.roleId where r.curStatus=2";
            List<Map<String, Object>> sqlvalue = tableService.selectSqlMapList(sql);
            for (Map<String, Object> map : sqlvalue) {
                String pageid = (String) map.get("pageid");
                String userName = (String) map.get("userName");
                setAccmap(pageid, userName, type);
            }
        } else {
            List<Map<String, String>> sqlvalue = accessRightsService.selectSqlMapList(type);
            for (Map<String, String> map : sqlvalue) {
                String pageid = map.get("pageid");
                String userName = map.get("userName");
                setAccmap(pageid, userName, type);
            }
        }
    }

    public static void setAccmap(String pageid, String userName, String type) {
        Map<String, List<String>> acc = accmap.get(pageid);
        if (acc == null) {
            acc = new HashMap<>();
        }
        List<String> users = Arrays.asList(userName.split(";"));
        acc.put(type, users);
        accmap.put(pageid, acc);
    }

    /**
     * 获取每个页面按钮权限
     *
     * @param loginid 登录人
     * @param pageid  页面ID
     * @param type    权限类型
     * @return 是否有权限
     */
    public static boolean getData(String loginid, String pageid, String type) {
        boolean rt = false;
        if ("admin".equals(loginid)) {
            return true;
        } else {
            if (accmap == null) {
                initData(type);
            }
            Map<String, List<String>> acc = accmap.get(pageid);
            if (acc == null) {
                initData(type, pageid);
                acc = accmap.get(pageid);
                if (acc != null) {
                    List<String> userIds = acc.get(type);
                    if (userIds != null) {
                        rt = userIds.contains(loginid) || userIds.contains("all");
                    }
                }
            } else {
                List<String> userIds = acc.get(type);
                if (userIds == null) {
                    initData(type, pageid);
                    acc = accmap.get(pageid);
                    userIds = acc.get(type);
                    if (userIds != null) {
                        rt = userIds.contains(loginid) || userIds.contains("all");
                    }
                } else {
                    rt = userIds.contains(loginid) || userIds.contains("all");
                }
            }
        }
        return rt;
    }

    /**
     * 获取每个页面按钮权限
     *
     * @param user   登录人
     * @param pageid 页面ID
     * @return 按钮权限
     */
    public static String getDataNum(String user, String pageid) {
        if ("admin".equals(user)) {
            return "111111";
        } else {
            boolean a = AccessUtil.getData(user, pageid, "add");
            boolean m = AccessUtil.getData(user, pageid, "modi");
            boolean d = AccessUtil.getData(user, pageid, "delete");
            boolean i = AccessUtil.getData(user, pageid, "import");
            boolean e = AccessUtil.getData(user, pageid, "export");
            boolean s = AccessUtil.getData(user, pageid, "send");
            String num = (a ? "1" : "0") + (m ? "1" : "0") + (d ? "1" : "0") + (i ? "1" : "0") + (e ? "1" : "0") + (s ? "1" : "0");
            return num;
        }
    }

    /**
     * 添加按钮权限
     *
     * @param num 按钮权限
     * @return 是否存在
     */
    public static boolean getAdd(String num) {
        //return "100".equals(num) || "110".equals(num) || "101".equals(num) || "111".equals(num);
        return num.substring(0, 1).equals("1");
    }

    /**
     * 修改按钮权限
     *
     * @param num 按钮权限
     * @return 是否存在
     */
    public static boolean getModi(String num) {
        //return "010".equals(num) || "110".equals(num) || "011".equals(num) || "111".equals(num);
        return num.substring(1, 2).equals("1");
    }

    /**
     * 删除按钮权限
     *
     * @param num 按钮权限
     * @return 是否存在
     */
    public static boolean getDelete(String num) {
        //return "001".equals(num) || "011".equals(num) || "101".equals(num) || "111".equals(num);
        return num.substring(2, 3).equals("1");
    }

    /**
     * 导入Excel按钮权限
     *
     * @param num 按钮权限
     * @return 是否存在
     */
    public static boolean getImport(String num) {
        //return "001".equals(num) || "011".equals(num) || "101".equals(num) || "111".equals(num);
        return num.substring(3, 4).equals("1");
    }

    /**
     * 导出Excel按钮权限
     *
     * @param num 按钮权限
     * @return 是否存在
     */
    public static boolean getExport(String num) {
        //return "001".equals(num) || "011".equals(num) || "101".equals(num) || "111".equals(num);
        return num.substring(4, 5).equals("1");
    }

    /**
     * 发送按钮权限
     *
     * @param num 按钮权限
     * @return 是否存在
     */
    public static boolean getSend(String num) {
        //return "001".equals(num) || "011".equals(num) || "101".equals(num) || "111".equals(num);
        return num.substring(5, 6).equals("1");
    }


    public static List<String> special(HttpServletRequest request, List<String> where, String pageid) {
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        String roles = (String) request.getSession().getAttribute("role");
        String userId = loginer.getId();
        roles = "'"+roles.replaceAll(";","','")+"allemployees'";
        try {
            AccessRights special = accessRightsService.selectTerm(pageid, roles, "special");
            if (special != null) {
                String val = special.getAccessValue();
                if (val.equals("loginName")) {
                    where.add("recordName='" + userId + "'");
                } else if (val.equals("loginDept")) {
                    List<String> strings = tableService.selectSql("select userName from employees where department in (select department from employees where userName='" + userId + "')");
                    String s = StringHelper.list2String(strings, "','");
                    where.add("recordName in ('" + s + "')");
                } else if (val.contains("userName-")) {
                    String[] tp = val.split("-");
                    where.add(tp[1] + "='" + userId + "'");
                } else if (val.contains("deptName-")) {
                    List<String> strings = tableService.selectSql("select userName from employees where department in (select department from employees where userName='" + userId + "')");
                    String s = StringHelper.list2String(strings, "','");
                    String[] tp = val.split("-");
                    where.add(tp[1] + " in ('" + s + "')");
                } else if (val.contains("condition-")) {
                    String[] tp = val.split("-");
                    where.add(tp[1]);
                } else if (val.contains("conditions-")) {
                    String[] tp = val.split("-");
                    String[] w = tp[1].split("\\|");
                    for (int i = 0, len = w.length; i < len; i++) {
                        where.add(w[i]);
                    }
                }
            }
        }catch (Exception e){
            e.getLocalizedMessage();
        }
        return where;
    }
}
