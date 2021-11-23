package com.oa.core.tag;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import com.oa.core.bean.Loginer;
import com.oa.core.util.SpringContextUtil;

public class RootTag extends TagSupport {

	private static final long serialVersionUID = 1L;

	JspWriter out = null;
	HttpSession session = null;
	Loginer loginer = null;
	HttpServletRequest request = null;

	public void init() {
		request = (HttpServletRequest) pageContext.getRequest();
		out = pageContext.getOut();
		session = (HttpSession) pageContext.getSession();
		loginer = (Loginer) session.getAttribute("loginer");
	}

	public Object getBean(String beanName) {
		return SpringContextUtil.getBean(beanName);
	}
}
