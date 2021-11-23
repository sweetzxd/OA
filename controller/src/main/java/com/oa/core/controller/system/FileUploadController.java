package com.oa.core.controller.system;

import com.oa.core.bean.Loginer;
import com.oa.core.helper.DateHelper;
import com.oa.core.service.util.TableService;
import com.oa.core.util.ConfParseUtil;
import com.oa.core.util.LogUtil;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

/**
 * @ClassName:FileUploadController
 * @author:zxd
 * @Date:2018/10/16
 * @Time:下午 3:48
 * @Version V1.0
 * @Explain
 */

@Controller
@RequestMapping("/uploading")
public class FileUploadController {
    @Autowired
    TableService tableService;

    @RequestMapping(value = "/fileupload", method = RequestMethod.POST, produces = {"text/html;charset=UTF-8;"})
    @ResponseBody
    public String uploadImg(HttpServletRequest request, MultipartFile file) {
        String type = request.getParameter("type");
        if(type==null || type==""){
            type = "temp";
        }
        JSONObject jsonObject = new JSONObject();
        if (null != file) {
            String myFileName = file.getOriginalFilename();
            String fileName =  DateHelper.timeNum() +"-"+ myFileName;
            String sqlPath="upload/"+type+"/"+DateHelper.getYearMonth()+"/";
            File fileDir=new File(getFile()+sqlPath);
            if (!fileDir.exists()) {
                fileDir.mkdirs();
            }
            String path=getFile()+sqlPath+fileName;
            File localFile = new File(path);
            try {
                file.transferTo(localFile);
                jsonObject.put("code",1);
                jsonObject.put("file",sqlPath+fileName);
            } catch (IllegalStateException e) {
                jsonObject.put("code",0);
                e.printStackTrace();
            } catch (IOException e) {
                jsonObject.put("code",0);
                e.printStackTrace();
            }
        }else{
            LogUtil.sysLog("文件为空");
        }
        return jsonObject.toString();
    }

    @RequestMapping(value = "/fileuploads", method = RequestMethod.POST, produces = {"text/html;charset=UTF-8;"})
    @ResponseBody
    public String webUploadFile(HttpServletRequest request, HttpServletResponse response) {
        String type = request.getParameter("type");
        if(type==null || type==""){
            type = "temp";
        }
        JSONObject jsonObject = new JSONObject();
        //创建一个通用的多部分解析器
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        //判断 request 是否有文件上传,即多部分请求
        if(multipartResolver.isMultipart(request)){
            //转换成多部分request
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest)request;
            //取得request中的所有文件名
            Iterator<String> iter = multiRequest.getFileNames();
            String myFileName = "";
            try {
                String fileNames = "";
                while(iter.hasNext()){
                    //取得上传文件
                    MultipartFile file = multiRequest.getFile(iter.next());
                    if(file != null){
                        //取得当前上传文件的文件名称
                        myFileName = file.getOriginalFilename();
                        //如果名称不为“”,说明该文件存在，否则说明该文件不存在
                        if(myFileName.trim() !=""){
                            LogUtil.sysLog(myFileName);
                            //重命名上传后的文件名  增加时间戳前缀
                            String fileName = DateHelper.timeNum() +"-"+ myFileName;
                            //定义上传路径
                            String sqlPath="upload/"+type+"/"+DateHelper.getYearMonth()+"/";
                            File fileDir=new File(getFile()+sqlPath);
                            if (!fileDir.exists()) {
                                fileDir.mkdirs();
                            }
                            String path=getFile()+sqlPath+fileName;
                            //存文件
                            File localFile = new File(path);
                            file.transferTo(localFile);
                            fileNames = sqlPath+fileName;
                        }
                    }
                }
                jsonObject.put("code", 1);
                jsonObject.put("file",fileNames);
                jsonObject.put("name",myFileName);
            } catch (IllegalStateException e) {
                LogUtil.sysLog(e);
                e.printStackTrace();
                jsonObject.put("code", 0);
            } catch (IOException e) {
                LogUtil.sysLog(e);
                e.printStackTrace();
                jsonObject.put("code", 0);
            }
        }
        LogUtil.sysLog(jsonObject);
        return jsonObject.toString();
    }

    @RequestMapping(value = "/chatfileupload", method = RequestMethod.POST, produces = {"text/html;charset=UTF-8;"})
    @ResponseBody
    public String chatUpload(HttpServletRequest request, MultipartFile file) {
        String server = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
        String type = request.getParameter("type");
        if(type==null || type==""){
            type = "temp";
        }
        JSONObject jsonObject = new JSONObject();
        if (null != file) {
            String myFileName = file.getOriginalFilename();
            String fileName =  DateHelper.timeNum() +"-"+ myFileName;
            String sqlPath="upload/"+type+"/"+DateHelper.getYearMonth()+"/";
            File fileDir=new File(getFile()+sqlPath);
            if (!fileDir.exists()) {
                fileDir.mkdirs();
            }
            String path=getFile()+sqlPath+fileName;
            File localFile = new File(path);
            try {
                file.transferTo(localFile);
                jsonObject.put("code",0);
                jsonObject.put("msg","");
                JSONObject object = new JSONObject();
                object.put("src",server+sqlPath+fileName);
                object.put("name",myFileName);
                jsonObject.put("data",object);
            } catch (IllegalStateException e) {
                jsonObject.put("code",1);
                jsonObject.put("msg","上传失败");
                e.printStackTrace();
            } catch (IOException e) {
                jsonObject.put("code",1);
                jsonObject.put("msg","上传失败");
                e.printStackTrace();
            }
        }else{
            jsonObject.put("code",1);
            jsonObject.put("msg","文件为空");
            LogUtil.sysLog("文件为空");
        }
        return jsonObject.toString();
    }


    @RequestMapping(value = "/chatfileuploads", method = RequestMethod.POST, produces = {"text/html;charset=UTF-8;"})
    @ResponseBody
    public String chatUploads(HttpServletRequest request,HttpServletResponse response) {
        String server = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
        String type = request.getParameter("type");
        if(type==null || type==""){
            type = "temp";
        }
        JSONObject jsonObject = new JSONObject();
        //创建一个通用的多部分解析器
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        //判断 request 是否有文件上传,即多部分请求
        if(multipartResolver.isMultipart(request)) {
            //转换成多部分request
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
            //取得request中的所有文件名
            Iterator<String> iter = multiRequest.getFileNames();
            JSONObject utrue = new JSONObject();
            JSONObject ufalse = new JSONObject();
            while (iter.hasNext()) {
                MultipartFile file = multiRequest.getFile(iter.next());
                if (null != file) {
                    String myFileName = file.getOriginalFilename();
                    String fileName =  DateHelper.timeNum() +"-"+ myFileName;
                    String sqlPath="upload/"+type+"/"+DateHelper.getYearMonth()+"/";
                    File fileDir=new File(getFile()+sqlPath);
                    if (!fileDir.exists()) {
                        fileDir.mkdirs();
                    }
                    String path=getFile()+sqlPath+fileName;
                    File localFile = new File(path);
                    JSONObject object = new JSONObject();
                    object.put("src",server+sqlPath+fileName);
                    object.put("name",myFileName);
                    boolean t = true;
                    try {
                        file.transferTo(localFile);
                    } catch (IllegalStateException e) {
                        t = false;
                        e.printStackTrace();
                    } catch (IOException e) {
                        t = false;
                        e.printStackTrace();
                    }
                    if(t){
                        utrue.put("data",object);
                    }else{
                        ufalse.put("data",object);
                    }
                }else{

                    LogUtil.sysLog("文件为空");
                }
            }
            JSONObject data = new JSONObject();
            data.put("utrue",utrue);
            data.put("ufalse",ufalse);
            jsonObject.put("code",1);
            jsonObject.put("msg","");
            jsonObject.put("data",data);
        }else{
            jsonObject.put("code",0);
            jsonObject.put("msg","上传失败");
        }

        return jsonObject.toString();
    }

    @RequestMapping(value = "/chatfileuploads11", method = RequestMethod.POST, produces = {"text/html;charset=UTF-8;"})
    @ResponseBody
    public String chatUpload1(HttpServletRequest request,HttpServletResponse response, MultipartFile file) {
        int i = 0;
        /*SmartUpload localSmartUpload = new SmartUpload();
        try  {
            localSmartUpload.initialize(this.config, request, response);
            localSmartUpload.upload();
            i = localSmartUpload.save(localSmartUpload.getRequest().getParameter("PATH"));

        }
        catch (Exception localException)
        {

        }*/
        return "";
    }
	
    @RequestMapping(value = "/photo",method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String uploadPhoto(HttpServletRequest request, @RequestParam("file") MultipartFile file) {
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        String userid = loginer.getId();
        JSONObject json = new JSONObject();
        if (file.getOriginalFilename().length() > 0) {
            try {
                String url = "/upload/photo/"+userid+".png";
                file.transferTo(new File(getFile()+"/upload/photo/"+userid+".png"));
                json.put("photourl",url);
                json.put("success",1);
                tableService.updateSqlMap("update employees set photo='"+url+"' where curStatus=2 and userName='"+userid+"'");
                loginer.setPhoto(url);
                request.getSession().setAttribute("loginer", loginer);
                json.put("msg","附件上传成功!");
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
