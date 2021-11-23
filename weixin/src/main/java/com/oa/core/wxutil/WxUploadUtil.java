package com.oa.core.wxutil;

import com.oa.core.helper.DateHelper;
import com.oa.core.util.ConfParseUtil;
import org.json.JSONObject;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;


/**
 * @Description: TODO(上传公共类)
 * @Author: wpengsen
 * @Create 2019/2/11 11:01
 * @Version: V1.0
 */

public class WxUploadUtil {

    /**
     * @Description: TODO(上传到本地TOMCAT下 直接获取)
     * @Author: wpengsen
     * @Create 2019/2/11 10:58
     * @Version: V1.0
     */
    public static JSONObject getTomcatUploadUrl(MultipartFile multipartFile) throws IllegalStateException, IOException {
        return getTomcatUploadUrl(multipartFile, "common");
    }

    public static JSONObject getTomcatUploadUrl(MultipartFile multipartFile, String type) throws IllegalStateException, IOException {
        ConfParseUtil cp = new ConfParseUtil();
        String tomCatPath = cp.getProperty("upload_file");
        String requestPath = "upload/" + type + "/";
        JSONObject json = new JSONObject();
        if (multipartFile == null || multipartFile.isEmpty()) {
            return null;
        }
        String fileNames = multipartFile.getOriginalFilename();
        String time = DateHelper.getYearMonth();
        String imagePath = tomCatPath + requestPath + time + "\\";
        File f1 = new File(imagePath);
        if (!f1.exists()) {
            f1.mkdirs();
        }
        String dl = DateHelper.timeNum();
        String fileName = dl + "_" + fileNames;
        String imageUrl = requestPath + time + "/" + fileName;
        String imagePath2 = tomCatPath + requestPath + time + "\\";
        multipartFile.transferTo(new File(imagePath2 + fileName));
        json.put("size", multipartFile.getSize());
        json.put("name", fileNames);
        json.put("url", imageUrl);
        return json;
    }

}
