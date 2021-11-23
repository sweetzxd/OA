package com.oa.core.controller.module;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * @ClassName:ModuleMenuController
 * @author:zxd
 * @Date:2019/05/16
 * @Time:上午 11:42
 * @Version V1.0
 * @Explain
 */

@Controller
@RequestMapping("/modulemuen")
public class ModuleMenuController {

    @RequestMapping(value = "/htwj", method = RequestMethod.GET)
    public ModelAndView gotoTest(){
        ModelAndView model = new ModelAndView("test/htwj");
        return model;
    }
}
