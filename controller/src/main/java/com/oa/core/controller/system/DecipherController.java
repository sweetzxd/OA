package com.oa.core.controller.system;

import com.oa.core.bean.user.UserManager;
import com.oa.core.helper.DateHelper;
import com.oa.core.listener.InitDataListener;
import com.oa.core.tag.UserDict;
import com.oa.core.util.ToNameUtil;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Hashtable;

@Controller
public class DecipherController {

    @RequestMapping(value = "/decipher",  method = RequestMethod.POST, produces = {"text/html;charset=UTF-8;"})
    @ResponseBody
    public String getDecipher(HttpServletRequest request, String type, String value) {
        return ToNameUtil.getName(type,value);
    }

    @RequestMapping(value = "/iosdecipher",  method = RequestMethod.POST, produces = {"text/html;charset=UTF-8;"})
    @ResponseBody
    public String getIosDecipher(HttpServletRequest request, String type, String value) {
        String data = ToNameUtil.getName(type,value);
        JSONObject json = new JSONObject();
        json.put("data",data);
        json.put("success", 1);
        return json.toString();
    }

    @RequestMapping(value = "/fieldidtoname",  method = RequestMethod.POST, produces = {"text/html;charset=UTF-8;"})
    @ResponseBody
    public String getFieldIdToName(HttpServletRequest request, String value) {
        Hashtable<String,String> hs = InitDataListener.getMapData("fieldData");
        if(hs!=null) {
            return hs.get(value);
        }else{
            return value;
        }
    }

    @RequestMapping(value = "/tableidtoname",  method = RequestMethod.POST, produces = {"text/html;charset=UTF-8;"})
    @ResponseBody
    public String getTableIdToName(HttpServletRequest request, String value) {
        Hashtable<String,String> hs = InitDataListener.getMapData("tableDate");
        if(hs!=null) {
            return hs.get(value);
        }else{
            return value;
        }
    }

    @RequestMapping(value = "/taskidtoname",  method = RequestMethod.POST, produces = {"text/html;charset=UTF-8;"})
    @ResponseBody
    public String getTaskIdToName(HttpServletRequest request, String value) {
        Hashtable<String,String> hs = InitDataListener.getMapData("taskData");
        if(hs!=null) {
            return hs.get(value);
        }else{
            return value;
        }
    }
}
