package com.oa.core.controller.system;

import com.oa.core.bean.Loginer;
import com.oa.core.helper.DateHelper;
import com.oa.core.util.ConfParseUtil;
import com.oa.core.wxutil.WxUploadUtil;
import org.apache.commons.io.FileUtils;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName:WxUploadController
 * @author:zxd
 * @Date:2019/04/08
 * @Time:下午 4:58
 * @Version V1.0
 * @Explain
 */
@Controller
@RequestMapping("/weixin/upload")
public class WxUploadController {
    @RequestMapping("/files")
    @ResponseBody
    public JSONObject uploads(HttpServletRequest request, @RequestParam("files") MultipartFile[] files,String userid) {

        JSONObject json = new JSONObject();
        try {
            List<JSONObject> jsons = new ArrayList();
            for (int i = 0; i < files.length; i++) {
                jsons.add(WxUploadUtil.getTomcatUploadUrl(files[i]));
            }
            json.put("data",jsons);
            json.put("success",1);
        } catch (Exception e) {
            e.printStackTrace();
            json.put("success",0);
        }
        return json;
    }

    @RequestMapping(value = "/file",method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String upload(MultipartHttpServletRequest muiltRequest, HttpServletRequest servletRequest) {
        JSONObject json = new JSONObject();
        String resultString = "";
        String fileName = muiltRequest.getFileNames().next();
        MultipartFile file = muiltRequest.getFile(fileName);
        if (file.getOriginalFilename().length() > 0) {
            List<JSONObject> jsons = new ArrayList();
            try {
                jsons.add(WxUploadUtil.getTomcatUploadUrl(file));

                json.put("data",jsons);
                json.put("success",1);
            } catch (IOException e) {
                e.printStackTrace();
            } catch(Exception e){
                json.put("msg","附件上传失败");
                json.put("success",0);
            }
        }

        return resultString;
    }
    @RequestMapping(value = "/appfile",method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String appUpload(HttpServletRequest request,@RequestParam("file") MultipartFile file,String type) {
        JSONObject json = new JSONObject();
        if (file.getOriginalFilename().length() > 0) {
            List<JSONObject> jsons = new ArrayList();
            try {
                jsons.add(WxUploadUtil.getTomcatUploadUrl(file,type));
                json.put("data",jsons);
                json.put("success",1);
            } catch (IOException e) {
                e.printStackTrace();
            } catch(Exception e){
                json.put("msg","附件上传失败");
                json.put("success",0);
            }
        }
        return json.toString();
    }
    @RequestMapping(value = "/photo",method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String uploadPhoto(HttpServletRequest request,@RequestParam("file") MultipartFile file,String userid) {
        JSONObject json = new JSONObject();
        if (file.getOriginalFilename().length() > 0) {
            long times = System.currentTimeMillis();
            String name = times+"-"+userid;
            try {
                file.transferTo(new File(getFile()+"/upload/photo/"+name+".png"));
                json.put("photourl","/upload/photo/"+name+".png");
                json.put("success",1);
            } catch (IOException e) {
                e.printStackTrace();
            } catch(Exception e){
                json.put("msg","附件上传失败");
                json.put("success",0);
            }
        }else{
            json.put("msg","附件为空");
            json.put("success",0);
        }

        return json.toString();
    }

    public static String getFile(){
        ConfParseUtil cp = new ConfParseUtil();
        String file = cp.getProperty("upload_file");
        return file;
    }
}
