package com.oa.core.system.warning.every;

import com.oa.core.util.Const;
import com.oa.core.util.LogUtil;
import com.oa.core.util.WeatherUtil;
import org.json.JSONObject;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;


/**
 * @ClassName:SysremWeather
 * @author:zxd
 * @Date:2019/04/25
 * @Time:下午 4:50
 * @Version V1.0
 * @Explain
 */
public class SysremWeather implements Job {

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        LogUtil.sysLog("更新天气接口数据开始");
        JSONObject jsonObject = WeatherUtil.tianqiJSON(Const.LOCATION);
        new WeatherUtil().setWeather(Const.LOCATION,jsonObject);
        LogUtil.sysLog("更新天气接口数据结束");
    }
}
