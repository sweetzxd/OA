package com.oa.core.controller.module;

import com.oa.core.bean.Loginer;
import com.oa.core.bean.module.Festival;
import com.oa.core.bean.util.PageUtil;
import com.oa.core.helper.DateHelper;
import com.oa.core.service.module.FestivalService;
import com.oa.core.util.LogUtil;
import com.oa.core.util.PrimaryKeyUitl;
import com.oa.core.util.ToNameUtil;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @ClassName:FestivalController
 * @author:wsx
 * @Date:2018/11/24
 * @Time:上午 11:35
 * @Version V1.0
 * @Explain
 */
@Controller
@RequestMapping("/festival")
public class FestivalController {
    @Autowired
    private FestivalService festivalService;

    @RequestMapping(value = "/gotofestival", method = RequestMethod.GET)
    public ModelAndView gotoFestival(HttpServletRequest request, String type,String festivalId) {
        ModelAndView mav = new ModelAndView("module/festival");
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        if(type.equals("edit")){
            Festival festival = festivalService.selectById(festivalId);
            mav.addObject("festival",festival);
        }
        mav.addObject("type", type);
        return mav;
    }

    @RequestMapping("/selectlist")
    @ResponseBody
    public String getFestivalList(HttpServletRequest request, String inputval, String option, int page, int limit) {
        PageUtil pu = new PageUtil();
        pu.setPageSize(limit);
        pu.setCurrentPage(page);
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        Festival festival = new Festival();
        festival.setStartRow(pu.getStartRow());
        festival.setEndRow(pu.getEndRow()-pu.getStartRow());
        if (inputval != null && inputval != "") {
            if ("festivalName".equals(option)) {
                festival.setFestivalName(inputval);
            }
        }
        int count = festivalService.selectAllTermsCont(festival);
        List<Festival> festivalList = festivalService.selectAllTerms(festival);
        for(Festival festival1 : festivalList){
            if(festival1.getType()==1){
                festival1.setTypeStr("阳历");
                festival1.setOperation("<a data-method='titlebtn' class='layui-btn layui-btn-normal layui-btn-xs' lay-event='edit' onclick=update('"+festival1.getFestivalId()+"')>修改</a>");
            }else if(festival1.getType()==2){
                festival1.setTypeStr("阴历");
                festival1.setOperation("<a data-method='titlebtn' class='layui-btn layui-btn-normal layui-btn-xs' lay-event='edit' onclick=update('"+festival1.getFestivalId()+"')>修改</a>");
            }else if(festival1.getType()==3){
                festival1.setTypeStr("例外");
                festival1.setOperation("<a data-method='titlebtn' class='layui-btn layui-btn-normal layui-btn-xs' lay-event='edit' onclick=update('"+festival1.getFestivalId()+"')>修改</a><a data-method='titlebtn' class='layui-btn layui-btn-danger layui-btn-xs' lay-event='del' onclick=del('"+festival1.getFestivalId()+"')>删除</a>");
            }
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 0);
        jsonObject.put("msg", "");
        jsonObject.put("count", count);
        jsonObject.put("data", festivalList);
        jsonObject.put("success",1);
        jsonObject.put("is", true);
        jsonObject.put("tip", "操作成功");

        return jsonObject.toString();
    }

    @RequestMapping(value = "/insert", method = RequestMethod.POST, produces = {"text/html;charset=UTF-8;", "application/json;"})
    @ResponseBody
    public String insertSave(HttpServletRequest request, Festival festival) {
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        String userId = loginer.getId();
        try {
            PrimaryKeyUitl pk = new PrimaryKeyUitl();
            String permissionId = pk.getNextId("festival", "festivalId");
            festival.setFestivalId(permissionId);
            festival.setType(3);//只能添加例外类型
            festival.setRecordName(userId);
            festival.setModifyName(userId);
            festival.setRecordTime(DateHelper.now());
            festival.setModifyTime(DateHelper.now());
            festivalService.insert(festival);
            return "1";
        } catch (Exception e) {
            LogUtil.sysLog("Exception:"+e);
            return "0";
        }
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST, produces = {"text/html;charset=UTF-8;", "application/json;"})
    @ResponseBody
    public String updateSave(HttpServletRequest request, Festival festival) {
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        String userId = loginer.getId();
        try {
            festival.setModifyName(userId);
            festival.setModifyTime(DateHelper.now());
            festivalService.update(festival);
            return "1";
        } catch (Exception e) {
            LogUtil.sysLog("Exception:"+e);
            return "0";
        }
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST, produces = {"text/html;charset=UTF-8;", "application/json;"})
    @ResponseBody
    public String delete(HttpServletRequest request, String festivalId) {
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        try {
            String userId = loginer.getId();
            festivalService.delete(festivalId,userId,DateHelper.now());
            return "1";
        } catch (Exception e) {
            LogUtil.sysLog("Exception:"+e);
            return "0";
        }
    }
}
