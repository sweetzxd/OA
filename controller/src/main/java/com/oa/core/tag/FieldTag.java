package com.oa.core.tag;

import com.oa.core.util.StringToHtmlUtil;

/**
 * @ClassName:FieldTag
 * @author:zxd
 * @Date:2019/06/14
 * @Time:下午 2:55
 * @Version V1.0
 * @Explain
 */
public class FieldTag extends RootTag {
    private static final long serialVersionUID = 1L;

    private String table;

    private String field;

    private String value;

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
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
            StringToHtmlUtil sth = new StringToHtmlUtil();
            String outHtml = sth.getFieldValue(table,field,value);
            out.append(outHtml);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return EVAL_PAGE;
    }
}
