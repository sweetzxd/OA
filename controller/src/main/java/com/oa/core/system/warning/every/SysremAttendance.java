package com.oa.core.system.warning.every;

import com.oa.core.service.util.TableService;
import com.oa.core.util.*;

import org.json.JSONArray;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName:SysremAttendance
 * @author:zxd
 * @Date:2019/07/23
 * @Time:下午 6:54
 * @Version V1.0
 * @Explain
 */
public class SysremAttendance implements Job {

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        LogUtil.sysLog("前一天考勤数据写入开始");

        LogUtil.sysLog("前一天考勤数据写入结束");
    }
}
