package com.oa.core.tag;

import com.oa.core.bean.user.UserManager;
import com.oa.core.helper.DateHelper;
import com.oa.core.helper.StringHelper;
import com.oa.core.util.ToNameUtil;

public class DictTag extends RootTag {

	private static final long serialVersionUID = 1L;

	private String type;

	private String value;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	@Override
	public int doStartTag() {
		super.init();
		return EVAL_BODY_INCLUDE;
	}
	@Override
	public int doEndTag() {
		try {
			if(value==null || value.equals("null")) {
				out.append("");
			}else{
				out.append(ToNameUtil.getName(type, value));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return EVAL_PAGE;
	}
}
