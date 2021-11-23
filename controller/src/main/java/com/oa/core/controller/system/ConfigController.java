package com.oa.core.controller.system;

import com.oa.core.util.ConfParseUtil;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Iterator;

/**
 * @ClassName:ConfigController
 * @author:zxd
 * @Date:2018/10/29
 * @Time:下午 3:38
 * @Version V1.0
 * @Explain 配置维护
 */
@Controller
@RequestMapping("/config")
public class ConfigController {

    @RequestMapping(value = "/configoapage", method = RequestMethod.GET)
    public ModelAndView configOAPage(HttpServletRequest request,String type) {
        ConfParseUtil cp = new ConfParseUtil();

        ModelAndView mav = new ModelAndView("manage/configpage");
        mav.addObject("type", "oapage-"+type);
        if("dl".equals(type)) {
            mav.addObject("error_us", cp.getPoa("error_us"));
            mav.addObject("error_user", cp.getPoa("error_user"));
            mav.addObject("error_pw", cp.getPoa("error_pw"));
            mav.addObject("error_ip", cp.getPoa("error_ip"));
            mav.addObject("error_as", cp.getPoa("error_as"));
            mav.addObject("error_catch", cp.getPoa("error_catch"));
        }else if("yx".equals(type)) {
            mav.addObject("mail_remind", cp.getPoa("mail_remind"));
            mav.addObject("mail_username", cp.getPoa("mail_username"));
            mav.addObject("mail_password", cp.getPoa("mail_password"));
            mav.addObject("mail_adress", cp.getPoa("mail_adress"));
            mav.addObject("mail_form", cp.getPoa("mail_form"));
        }else if("qt".equals(type)) {
            mav.addObject("open_message", cp.getPoa("open_message"));
            mav.addObject("recno", cp.getPoa("recno"));
            mav.addObject("form_list", cp.getPoa("form_list"));
            mav.addObject("record", cp.getPoa("record"));
            mav.addObject("modify", cp.getPoa("modify"));
        }
        return mav;
    }

    @RequestMapping(value = "/saveoapage", method = RequestMethod.POST)
    public ModelAndView saveOapage(HttpServletRequest request,String data) {
        ConfParseUtil cp = new ConfParseUtil();
        JSONObject jsonObject = new JSONObject(data);
        Iterator<String> it = jsonObject.keys();
        while(it.hasNext()){
            String key = it.next();
            String value = jsonObject.getString(key);
            cp.setPoa(key,value);
            System.out.println("key: "+key+",value:"+value);
        }
        return new ModelAndView("redirect:/config/configschedule.do");
    }

    @RequestMapping(value = "/configpathpage", method = RequestMethod.GET)
    public ModelAndView configPathPage(HttpServletRequest request) {
        ConfParseUtil cp = new ConfParseUtil();
        ModelAndView mav = new ModelAndView("manage/configpage");
        mav.addObject("type", "pathpage");
        mav.addObject("produce_file", cp.getProperty("produce_file"));
        mav.addObject("upload_file", cp.getProperty("upload_file"));
        return mav;
    }

    @RequestMapping(value = "/savepathpage", method = RequestMethod.POST)
    public ModelAndView savePathPage(HttpServletRequest request,String data) {
        ConfParseUtil cp = new ConfParseUtil();
        JSONObject jsonObject = new JSONObject(data);
        Iterator<String> it = jsonObject.keys();
        while(it.hasNext()){
            String key = it.next();
            String value = jsonObject.getString(key);
            cp.setProperty(key,value);
            System.out.println("key: "+key+",value:"+value);
        }
        return new ModelAndView("redirect:/config/configschedule.do");
    }

    @RequestMapping(value = "/configschedule", method = RequestMethod.GET)
    public ModelAndView configSchedule(HttpServletRequest request) {
        ConfParseUtil cp = new ConfParseUtil();
        ModelAndView mav = new ModelAndView("manage/configpage");
        mav.addObject("type", "schedule");
        mav.addObject("schedule", cp.getSchedule());
        return mav;
    }

    @RequestMapping(value = "/saveschedule", method = RequestMethod.POST)
    public ModelAndView saveSchedule(HttpServletRequest request,String data) {
        ConfParseUtil cp = new ConfParseUtil();
        JSONObject jsonObject = new JSONObject(data);
        Iterator<String> it = jsonObject.keys();
        while(it.hasNext()){
            String key = it.next();
            String value = jsonObject.getString(key);
            cp.setSchedule(key,value);
            System.out.println("key: "+key+",value:"+value);
        }
        return new ModelAndView("redirect:/config/configschedule.do");
    }
}
