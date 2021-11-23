package com.oa.core.tag;

import com.oa.core.bean.dd.FieldData;
import com.oa.core.util.FieldTypeUtil;
import com.oa.core.util.StringToHtmlUtil;

import java.util.Map;

/**
 * @ClassName:FieldInfoTag
 * @author:zxd
 * @Date:2019/06/14
 * @Time:下午 4:22
 * @Version V1.0
 * @Explain
 */
public class FieldInfoTag extends RootTag {
    private static final long serialVersionUID = 1L;

    private String table;

    private String field;

    private Object value;

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

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
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
            FieldData fieldData = sth.getFieldData(field);
            String outHtml = FieldTypeUtil.fieldInfo(table,fieldData, value,fieldData.getFieldType());
            out.append(outHtml);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return EVAL_PAGE;
    }
}