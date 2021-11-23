package com.oa.core.controller.module;

import com.oa.core.bean.Loginer;
import com.oa.core.bean.module.File;
import com.oa.core.bean.module.Filetype;
import com.oa.core.bean.system.RoleDefines;
import com.oa.core.bean.util.PageUtil;
import com.oa.core.helper.DateHelper;
import com.oa.core.helper.FileHelper;
import com.oa.core.service.module.FileService;
import com.oa.core.service.module.FilepermissionService;
import com.oa.core.service.module.FiletypeService;
import com.oa.core.service.system.RoleDefinesService;
import com.oa.core.util.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.*;

/**
 * @ClassName:FileController
 * @author:wsx
 * @Date:2018/11/22
 * @Time:下午 4:22
 * @Version V1.0
 * @Explain
 */
@Controller
@RequestMapping("/file")
public class FileController {
    @Autowired
    private FileService fileService;
    @Autowired
    private FiletypeService filetypeService;
    @Autowired
    private RoleDefinesService roleDefinesService;
    @Autowired
    private FilepermissionService filepermissionService;

    @RequestMapping(value = "/gotofile", method = RequestMethod.GET)
    public ModelAndView gotoFile(HttpServletRequest request, String type, String fileId) {
        ModelAndView mav = new ModelAndView("module/file");
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        Map<String,String> filetypeMap = getFiletypes(request);
        List<RoleDefines> roleList = roleDefinesService.selectAll();
        if(type.equals("edit")&&fileId!=null&&!fileId.equals("")){
            File file = fileService.selectById(fileId);
            mav.addObject("file",file);
            String fieldValue = file.getFileAdd();
            String filesHtml = "";
            if (fieldValue != null && fieldValue.contains("|")) {
                ConfParseUtil cp = new ConfParseUtil();
                String catPath = cp.getProperty("upload_file");
                String[] files = fieldValue.split("\\|");
                for (int i = 0; i < files.length; i++) {
                    java.io.File f = new java.io.File(files[i]);
                    long size = 0L;
                    try {
                        FileInputStream fis = new FileInputStream(catPath+f);
                        FileChannel fc = fis.getChannel();
                        size = fc.size();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    String fileName = f.getName();
                    fileName = fileName.substring(fileName.indexOf("-") + 1);
                    filesHtml += "<tr id='upload-" + i + "'><td>";
                    filesHtml += "<a href='/" + files[i] + "' data-value='" + files[i] + "' download='" + fileName + "' title='点击下载 " + fileName + "'>" + fileName + "</a></td>";
                    filesHtml += "<td>" + FileHelper.getPrintSize(size) + "</td>";
                    filesHtml +="<td><span style='color: #5FB878'>上传成功</span></td>";
                    filesHtml += "<td><a class='layui-btn layui-btn-xs layui-btn-danger upload-delete' herf='javascript:void(0)' onclick=\"removefile('upload-" + i + "','uploads_fileAdd')\">删除</a></td></tr>";
                }
            }
            mav.addObject("filesHtml",filesHtml);
        }
        mav.addObject("filetypeMap",filetypeMap);
        mav.addObject("type", type);
        return mav;
    }

    @RequestMapping("/selectlist")
    @ResponseBody
    public String getFileList(HttpServletRequest request, String inputval, String option, int page, int limit) {
        PageUtil pu = new PageUtil();
        pu.setPageSize(limit);
        pu.setCurrentPage(page);
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        List<String> userRoleList = roleDefinesService.getRoleIds(loginer.getId());
        Filetype filetype = new Filetype();
        filetype.setList(userRoleList);
        List<Filetype> filetypeList = filetypeService.selectAllTerms(filetype);
        List<String> list = new ArrayList<>();
        for(Filetype filetype1 : filetypeList){
            list.add(filetype1.getFileTypeId());
        }
        File file = new File();
        file.setList(list);
        file.setStartRow(pu.getStartRow());
        file.setEndRow(pu.getEndRow()-pu.getStartRow());
        if (inputval != null && inputval != "") {
            if ("fileTypeId".equals(option)) {
                file.setFileTypeId(inputval);
            }else if("fileName".equals(option)){
                file.setFileName(inputval);
            }
        }
        int count = fileService.selectAllTermsCont(file);
        List<File> fileList = fileService.selectAllTerms(file);
        for(File file1 : fileList){
            Filetype filetype2 = filetypeService.selectById(file1.getFileTypeId());
            if(filetype2!=null){
                file1.setFileTypeId(filetype2.getFileTypeName());
            }
            String uploadTimeStr = ToNameUtil.getName("datetime", String.valueOf(file1.getUploadTime()));
            file1.setUploadTimeStr(uploadTimeStr);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 0);
        jsonObject.put("msg", "");
        jsonObject.put("count", count);
        jsonObject.put("data", fileList);
        jsonObject.put("success",1);
        jsonObject.put("is", true);
        jsonObject.put("tip", "操作成功");

        return jsonObject.toString();
    }

    @RequestMapping(value = "/getFiletypes", method = RequestMethod.POST, produces = {"text/html;charset=UTF-8;", "application/json;"})
    @ResponseBody
    public HashMap<String,String> getFiletypes(HttpServletRequest request){
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        List<String> userRoleList = roleDefinesService.getRoleIds(loginer.getId());
        List<Filetype> list = filetypeService.getFiletypesByRoleId(userRoleList);
        HashMap<String,String> hm = new HashMap<>();
        HashMap<String,HashMap<String,String>> hms = new HashMap<>();
        for (Filetype filetype : list){
            String fileTypeId = filetype.getFileTypeId();
            String fileTypeName = filetype.getFileTypeName();
            String superiorFileTypeId = filetype.getSuperiorFileTypeId();
            if(superiorFileTypeId==null || superiorFileTypeId.equals("")){
                hm.put(fileTypeId,fileTypeName);
            }else{
                HashMap<String,String> map = hms.get(superiorFileTypeId);
                if(map==null){
                    map = new HashMap<>();
                }
                map.put(fileTypeId,fileTypeName);
                hms.put(superiorFileTypeId,map);
            }
        }
        HashMap<String,String> filetype = new LinkedHashMap<>();
        for (Map.Entry<String, String> entry : hm.entrySet()) {
            String fileTypeId = entry.getKey();
            String fileTypeName = entry.getValue();
            filetype.put(fileTypeId,fileTypeName);
            for(Map.Entry<String, String> z : hms.get(fileTypeId).entrySet()){
                String id = z.getKey();
                String name = "&nbsp;&nbsp;&nbsp;&nbsp;"+z.getValue();
                filetype.put(id,name);
                hms.remove(fileTypeId);
            }
        }
        for(Map.Entry<String, HashMap<String,String>> map : hms.entrySet()){
            for (Map.Entry<String, String> map1 : map.getValue().entrySet()){
                String id = map1.getKey();
                String name = map1.getValue();
                filetype.put(id,name);
            }
        }
        return filetype;
    }

    @RequestMapping(value = "/insert", method = RequestMethod.POST, produces = {"text/html;charset=UTF-8;", "application/json;"})
    @ResponseBody
    public String insertSave(HttpServletRequest request, File file) {
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        String userId = loginer.getId();
        try {
            PrimaryKeyUitl pk = new PrimaryKeyUitl();
            String permissionId = pk.getNextId("commonfiles", "fileId");
            file.setFileId(permissionId);
            file.setUploadTime(DateHelper.now());
            file.setRecordName(userId);
            file.setModifyName(userId);
            file.setRecordTime(DateHelper.now());
            file.setModifyTime(DateHelper.now());
            fileService.insert(file);
            return "1";
        } catch (Exception e) {
            LogUtil.sysLog("Exception:"+e);
            return "0";
        }
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST, produces = {"text/html;charset=UTF-8;", "application/json;"})
    @ResponseBody
    public String updateSave(HttpServletRequest request, File file) {
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        String userId = loginer.getId();
        try {
            file.setModifyName(userId);
            file.setModifyTime(DateHelper.now());
            fileService.update(file);
            return "1";
        } catch (Exception e) {
            LogUtil.sysLog("Exception:"+e);
            return "0";
        }
    }

    @RequestMapping(value = "/deletes", method = RequestMethod.POST, produces = {"text/html;charset=UTF-8;", "application/json;"})
    @ResponseBody
    public String deletes(HttpServletRequest request, String fileId) {
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        try {
            String userId = loginer.getId();
            fileId = fileId.replaceAll(";","','");
            fileId = "'"+fileId.substring(0,fileId.length()-2);
            fileService.deletes(fileId,userId,DateHelper.now());
            return "1";
        } catch (Exception e) {
            LogUtil.sysLog("Exception:"+e);
            return "0";
        }
    }

    //跳转常用文档页面
    @RequestMapping(value = "/gotofileList", method = RequestMethod.GET)
    public ModelAndView gotoFileList(HttpServletRequest request, String type) {
        ModelAndView mav = new ModelAndView("module/fileList");
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        mav.addObject("type", "list");
        return mav;
    }

    //文件类型树
    @RequestMapping(value = "/filetypeTree", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String gotoFiletypeTree(HttpServletRequest request,String spread) {
        Filetype filetype = new Filetype();
        List<Filetype> myurllist = filetypeService.selectAllTerms(filetype);
        if(spread!=null && spread.equals("false")) {
            return FileUtil.getMenu(myurllist,false);
        }else{
            return FileUtil.getMenu(myurllist,true);
        }
    }

    //获取常用文档列表
    @RequestMapping("/getUserFileList")
    @ResponseBody
    public String getUserFileList(HttpServletRequest request, String inputval, String option, int limit, int page){
        File file = new File();
        boolean optiontype = true;
        if (inputval != null && inputval != "") {
            if ("fileTypeId".equals(option)) {
                file.setFileTypeId(inputval);
            } else if ("fileName".equals(option)) {
                file.setFileName(inputval);
            }
        }
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        List<String> userRoleList = roleDefinesService.getRoleIds(loginer.getId());
        List<String> filetypeList = filepermissionService.getUserFileType(userRoleList);
        file.setList(filetypeList);
        int count = fileService.selectAllTermsCont(file);
        PageUtil pageUtil = new PageUtil();
        pageUtil.setPageSize(limit);
        pageUtil.setCurrentPage(page);
        file.setStartRow(pageUtil.getStartRow());
        file.setEndRow(pageUtil.getEndRow() - pageUtil.getStartRow());
        List<File> fileList = fileService.selectAllTerms(file);
        for(File file1 : fileList){
            String uploadTimeStr = ToNameUtil.getName("datetime", String.valueOf(file1.getUploadTime()));
            file1.setUploadTimeStr(uploadTimeStr);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 0);
        jsonObject.put("msg", "");
        jsonObject.put("count", count);
        jsonObject.put("data", fileList);
        jsonObject.put("success",1);
        return jsonObject.toString();
    }

}
