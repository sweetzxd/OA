package com.oa.core.controller.module;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName:WxModuleController
 * @author:zxd
 * @Date:2019/04/09
 * @Time:下午 12:10
 * @Version V1.0
 * @Explain ios android 特殊页面跳转
 */
@Controller
@RequestMapping("/weixin")
public class WxModuleController {

    /**
    * @method: 
    * @param: 
    * @return: 
    * @author: zxd
    * @date: 2019/04/29
    * @description: 入职流程
    */
    @RequestMapping(value = "/module/rizhi", method = RequestMethod.GET)
    public String rizhiViewpage(HttpServletRequest request) {
        String userid = request.getParameter("userid");
        return "redirect:/views/module/WorkDiary.html?userid="+userid;

    }
    /**
    * @method: 
    * @param: 
    * @return: 
    * @author: zxd
    * @date: 2019/04/29
    * @description: 文档查看
    */
    @RequestMapping(value = "/module/wendang", method = RequestMethod.GET)
    public String wendangViewpage(HttpServletRequest request) {
        String userid = request.getParameter("userid");
        return "redirect:/views/module/FileList.html?userid="+userid;
    }

    /**
     * @method:
     * @param:
     * @return:
     * @author: zxd
     * @date: 2019/04/29
     * @description: 党务
     */
    @RequestMapping(value = "/module/dangwu", method = RequestMethod.GET)
    public String dangwuViewpage(HttpServletRequest request) {
        String userid = request.getParameter("userid");
        return "redirect:/views/dangwu/partwork.html?userid="+userid;
    }
    
    /**
    * @method: 
    * @param: 
    * @return: 
    * @author: zxd
    * @date: 2019/04/29
    * @description: 工作安排
    */
    @RequestMapping(value = "/module/gongzuo", method = RequestMethod.GET)
    public String gongzuoViewpage(HttpServletRequest request) {
        String userid = request.getParameter("userid");
        return "redirect:/views/module/WorkForm.html?userid="+userid+"&formid=rcap2019040900001";
    }
    
    /**
    * @method: 
    * @param: 
    * @return: 
    * @author: zxd
    * @date: 2019/04/29
    * @description: 方法说明
    */
    @RequestMapping(value = "/workflow/workflowproc", method = RequestMethod.GET)
    public String procViewpage(HttpServletRequest request) {
        String userid = request.getParameter("userid");
        return "redirect:/views/module/ProcTable.html?userid="+userid+"&formid=rcap2019040900001";

    }


    @RequestMapping(value = "/filetype/gotofiletype", method = RequestMethod.GET)
    public String filetypeViewpage(HttpServletRequest request) {
        String userid = request.getParameter("userid");
        return "redirect:/views/module/FileTypeTable.html?userid="+userid+"&formid=rcap2019040900001";
    }
}
