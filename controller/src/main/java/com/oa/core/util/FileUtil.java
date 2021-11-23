package com.oa.core.util;

import com.oa.core.bean.module.Filetype;
import com.oa.core.service.module.FiletypeService;
import org.json.JSONArray;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName:FileUtil
 * @author:wsx
 * @Date:2018/11/23
 * @Time:下午 1:48
 * @Version V1.0
 * @Explain 文件工具类
 */
public class FileUtil {

    //文件类型树
    public static String getMenu(List<Filetype> filetypeList, boolean spread){
        String menu = "";
        if (filetypeList != null) {
            List<Map<String, Object>> lmap = new ArrayList<Map<String, Object>>();
            Map<String, Object> maps = new HashMap<String, Object>();
            for (int n = 0; n < filetypeList.size(); n++) {
                Filetype filetype = filetypeList.get(n);
                String id = filetype.getFileTypeId();
                String pid = filetype.getSuperiorFileTypeId();
                String title = filetype.getFileTypeName();
                int menuNum = 1;
                if (pid==null || pid.equals("")) {
                    maps = new HashMap<String, Object>();
                    maps.put("id", id);
                    maps.put("name", title);
                    maps.put("spread", spread);
                    maps.put("alias", title);
                    maps.put("menuNum", menuNum);
                    maps.put("children", getNext(menuNum,id,spread));
                    lmap.add( maps);
                }
            }
            JSONArray json = new JSONArray(lmap);
            menu = json.toString();
        }
        return menu;
    }

    public static List<Map<String, Object>> getNext(int num, String superiorFileTypeId,boolean spread){
        FiletypeService filetypeService = (FiletypeService) SpringContextUtil.getBean("filetypeService");
        Filetype nextFiletype = new Filetype();
        nextFiletype.setSuperiorFileTypeId(superiorFileTypeId);
        List<Filetype> filetypeList = filetypeService.selectAllTerms(nextFiletype);
        Map<String, Object> maps = new HashMap<String, Object>();
        Map<String, Object> map = new HashMap<String, Object>();
        List<Map<String, Object>> lmap = new ArrayList<Map<String, Object>>();
        for (int n = 0; n < filetypeList.size(); n++) {
            Filetype filetype = filetypeList.get(n);
            String id = filetype.getFileTypeId();
            String title = filetype.getFileTypeName();
            int menuNum = num+1;
            maps = new HashMap<String, Object>();
            maps.put("id", id);
            maps.put("name", title);
            maps.put("spread", spread);
            maps.put("alias", title);
            maps.put("menuNum", menuNum);
            maps.put("children", getNext(menuNum,id,spread));
            lmap.add(maps);
        }
        return lmap;
    }


}
