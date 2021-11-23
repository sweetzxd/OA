package com.oa.core.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * 错误页面的控制器
 * 
 * @author zxd
 * 
 */
@Controller
public class ErrorController {

	/**
	 * 重复提交页面
	 * @author zxd
	 * @return
	 */
	@RequestMapping(value = "/error_repeat_submit", method = RequestMethod.GET)
	public ModelAndView gotoRepeatSubmitErrorPage() {
		ModelAndView mav = new ModelAndView("error_repeat_submit");
		return mav;
	}

	/**
	 * 无权查看页面
	 * @author zxd
	 * @return
	 */
	@RequestMapping(value = "/error_no_right", method = RequestMethod.GET)
	public ModelAndView gotoNoRightErrorPage() {
		ModelAndView mav = new ModelAndView("error_no_right");
		return mav;
	}
}
