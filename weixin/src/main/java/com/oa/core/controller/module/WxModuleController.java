package com.oa.core.controller.module;

import com.oa.core.service.util.TableService;
import com.oa.core.tag.DeptDict;
import com.oa.core.tag.UserDict;
import com.oa.core.util.ToNameUtil;
import org.codehaus.jackson.JsonNode;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName:WxModuleController
 * @author:zxd
 * @Date:2019/04/09
 * @Time:下午 12:10
 * @Version V1.0
 * @Explain
 */
@Controller
@RequestMapping("/weixin")
public class WxModuleController {
    @Autowired
    TableService tableService;

    @RequestMapping(value = "/module/getrizhirul", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String getrizhiurl(HttpServletRequest request) {
        JSONObject json = new JSONObject();
        Map<String,String> map = new HashMap<String,String>(1);
        map.put("url","");
        json.put("data",map);
        json.put("msg","");
        json.put("success", 1);
        return json.toString();
    }

    @RequestMapping(value = "/module/rizhi", method = RequestMethod.GET)
    public String rizhiViewpage(HttpServletRequest request) {
        String userid = request.getParameter("userid");
        String temp = request.getParameter("temp");
        String deptid = DeptDict.getUserDept(userid);
        return "redirect:/views/module/WorkDiary.html?userid="+userid+"&deptid="+deptid;
    }
    @RequestMapping(value = "/module/wendang", method = RequestMethod.GET)
    public String wendangViewpage(HttpServletRequest request) {
        String userid = request.getParameter("userid");
        String temp = request.getParameter("temp");
        return "redirect:/views/module/FileList.html?userid="+userid;
    }
    @RequestMapping(value = "/module/gongzuo", method = RequestMethod.GET)
    public String gongzuoViewpage(HttpServletRequest request) {
        String userid = request.getParameter("userid");
        String temp = request.getParameter("temp");
        return "redirect:/views/module/WorkForm.html?userid="+userid+"&formid=rcap2019040900001";
    }
    @RequestMapping(value = "/workflow/workflowproc", method = RequestMethod.GET)
    public String procViewpage(HttpServletRequest request) {
        String userid = request.getParameter("userid");
        String temp = request.getParameter("temp");
        return "redirect:/views/module/ProcTable.html?userid="+userid+"&formid=rcap2019040900001";
    }
    @RequestMapping(value = "/filetype/gotofiletype", method = RequestMethod.GET)
    public String filetypeViewpage(HttpServletRequest request) {
        String userid = request.getParameter("userid");
        String temp = request.getParameter("temp");
        return "redirect:/views/module/FileTypeTable.html?userid="+userid+"&formid=rcap2019040900001";
    }

    /**
    * @method: getAppVersion
    * @param:
    * @return:
    * @author: zxd
    * @date: 2019/05/30
    * @description: 获取更新包版本
    */
    @RequestMapping(value = "/getappversion", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String getAppVersion(HttpServletRequest request,int version){
        Map<String, Object> map = tableService.selectSqlMap("SELECT IFNULL(packVersion,'') AS packVersion,packUpFile FROM updatepackage WHERE curStatus=2 and packType='APP' ORDER BY modifyTime DESC limit 0,1");
        JSONObject json = new JSONObject();
        if(map!=null){
            int data = Integer.parseInt((String)map.get("packVersion"));
            if(data>version){
                json.put("success", 1);
                json.put("msg", "");
                json.put("url", map.get("packUpFile"));
            }else{
                json.put("success", 0);
                json.put("msg", "已是最新版本。");
            }
        }else{
            json.put("success", 0);
            json.put("msg", "已是最新版本。");
        }
        return json.toString();
    }
}
