package com.oa.core.system.warning.every;

import com.oa.core.util.LogUtil;
import com.oa.core.util.RestrictUtil;
import com.oa.core.util.WeatherUtil;
import org.json.JSONObject;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @ClassName:SysremRestrict
 * @author:zxd
 * @Date:2019/04/27
 * @Time:下午 4:48
 * @Version V1.0
 * @Explain
 */
public class SysremRestrict implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        LogUtil.sysLog("更新限行接口数据开始");
        RestrictUtil ru = new RestrictUtil();
        JSONObject jsonObject = ru.selectTable();
        ru.setRestrict(jsonObject);
        LogUtil.sysLog("更新限行接口数据结束");
    }

}
