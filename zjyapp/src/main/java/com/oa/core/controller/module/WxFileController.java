package com.oa.core.controller.module;

import com.oa.core.bean.Loginer;
import com.oa.core.bean.module.File;
import com.oa.core.bean.util.PageUtil;
import com.oa.core.service.module.FileService;
import com.oa.core.service.module.FilepermissionService;
import com.oa.core.service.system.RoleDefinesService;
import com.oa.core.util.ToNameUtil;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @ClassName:FileController
 * @author:wsx
 * @Date:2018/11/22
 * @Time:下午 4:22
 * @Version V1.0
 * @Explain
 */
@Controller
@RequestMapping("/weixin/file")
public class WxFileController {
    @Autowired
    private FileService fileService;
    @Autowired
    private RoleDefinesService roleDefinesService;
    @Autowired
    private FilepermissionService filepermissionService;

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
        String userId = null;
        if(loginer == null){
            userId = request.getParameter("userid");
        }else{
            userId = loginer.getId();
        }
        List<String> userRoleList = roleDefinesService.getRoleIds(userId);
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
        jsonObject.put("count", count);
        jsonObject.put("data", fileList);
        jsonObject.put("msg", "");
        jsonObject.put("success",1);
        return jsonObject.toString();
    }

}
