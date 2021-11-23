package com.oa.core.tag;

import com.oa.core.bean.module.Filetype;
import com.oa.core.service.module.FiletypeService;
import com.oa.core.util.SpringContextUtil;

/**
 * @ClassName:FiletypeDict
 * @author:wsx
 * @Date:2018/11/19 0019
 * @Time:下午 6:52
 * @Version V1.0
 * @Explain
 */
public class FiletypeDict {

    public static String getName(String name) {
        FiletypeService filetypeService = (FiletypeService)SpringContextUtil.getBean("filetypeService");
        Filetype filetype = filetypeService.selectById(name);
        if (filetype != null) {
            return " " + filetype.getFileTypeName() + " ";
        } else {
            return "";
        }

    }
}
