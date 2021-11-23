package com.oa.core.system.warning;

import com.oa.core.bean.system.Warning;
import com.oa.core.helper.DateHelper;
import com.oa.core.service.system.WarningService;
import com.oa.core.util.ConfParseUtil;
import com.oa.core.util.LogUtil;
import org.apache.log4j.Logger;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @ClassName:WarningSystem
 * @author:zxd
 * @Date:2018/10/12
 * @Time:下午 3:24
 * @Version V1.0
 * @Explain 添加定时预警任务
 */
public class WarningSystem {

    private Logger log = Logger.getLogger(WarningSystem.class);
    @Autowired
    WarningService warningService;

    public void addWarning(){
        systemWarning();
        List<Warning> warningList = warningService.selectAll("");
        if (warningList == null || warningList.isEmpty()) {
            log.info("没有需要添加的预警任务");
            return;
        }
        try {
            Scheduler scheduler = getScheduler();
            for (Warning w : warningList) {
                String i = "-" + DateHelper.now();
                String packageName = "com.oa.core.system.warning." + w.getWarningType() + ".";
                String className = packageName + w.getWarningClass();
                Class clazz = Class.forName(className);
                JobDetail jobDetail = JobBuilder.newJob(clazz).withIdentity(w.getWarningName(), "jobGroup" + i).build();
                Trigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger" + i, "triggerGroup" + i)
                        .withSchedule(CronScheduleBuilder.cronSchedule(w.getWarningTime())).startNow().build();
                scheduler.scheduleJob(jobDetail, trigger);
                scheduler.start();
                log.info("添加定时任务[name] -  " + w.getWarningName() + " [class] - " + className);
            }
        }catch (Exception e){
            e.getLocalizedMessage();
            LogUtil.sysLog(e.getMessage());
        }
    }

    public void systemWarning() {
        ConfParseUtil cp = new ConfParseUtil();
        String type = cp.getPoa("open_message");
        if (type != null && type.equals("true")) {
            Warning w = new Warning();
            w.setWarningName("systemMessage");
            w.setWarningType("every");
            w.setWarningClass("SystemMessage");
            w.setWarningTime("0 0/1 * * * ?");
            addJob(w);
        }
    }

    /**
    * @method: 
    * @param: 
    * @return: 
    * @author: zxd
    * @date: 2019/06/12
    * @description: 天气接口
    */
    public void sysremWeather() {
        Warning w = new Warning();
        w.setWarningName("sysremWeather");
        w.setWarningType("every");
        w.setWarningClass("SysremWeather");
        w.setWarningTime("0 0 0/1 * * ?");
        addJob(w);
    }

    /**
     * SchedulerFactory使用获取Scheduler
     *
     * @return
     * @throws Exception
     */
    private static Scheduler getScheduler() throws Exception {
        SchedulerFactory scheduler = new StdSchedulerFactory();
        return scheduler.getScheduler();
    }

    public static void addJob(Warning w) {
        try {
            String i = "-" + DateHelper.now();
            String packageName = "com.oa.core.system.warning." + w.getWarningType() + ".";
            String className = packageName + w.getWarningClass();
            Class clazz = Class.forName(className);
            String jobName = w.getWarningName();
            String jobGroupName = "jobGroup-" + w.getWarningClass();
            String triggerName = "trigger-" + w.getWarningClass();
            String triggerGroupName = "triggerGroup-" + w.getWarningClass();
            String cron = w.getWarningTime();
            addJob(jobName, jobGroupName, triggerName, triggerGroupName, clazz, cron);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param jobName          任务名
     * @param jobGroupName     任务组名
     * @param triggerName      触发器名
     * @param triggerGroupName 触发器组名
     * @param jobClass         任务
     * @param cron             时间设置，参考quartz说明文档
     * @Description: 添加一个定时任务
     */
    public static void addJob(String jobName, String jobGroupName, String triggerName, String triggerGroupName, Class jobClass, String cron) {
        try {
            Scheduler sched = getScheduler();
            // 任务名，任务组，任务执行类
            JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(jobName, jobGroupName).build();
            // 触发器
            TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger();
            // 触发器名,触发器组
            triggerBuilder.withIdentity(triggerName, triggerGroupName);
            triggerBuilder.startNow();
            // 触发器时间设定
            triggerBuilder.withSchedule(CronScheduleBuilder.cronSchedule(cron));
            // 创建Trigger对象
            CronTrigger trigger = (CronTrigger) triggerBuilder.build();
            // 调度容器设置JobDetail和Trigger
            sched.scheduleJob(jobDetail, trigger);
            // 启动
            if (!sched.isShutdown()) {
                sched.start();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void modifyJobTime(Warning w) {
        String jobName = w.getWarningName();
        String jobGroupName = "jobGroup-" + w.getWarningClass();
        String triggerName = "trigger-" + w.getWarningClass();
        String triggerGroupName = "triggerGroup-" + w.getWarningClass();
        String cron = w.getWarningTime();
        modifyJobTime(jobName, jobGroupName, triggerName, triggerGroupName, cron);
    }

    /**
     * @param jobName
     * @param jobGroupName
     * @param triggerName      触发器名
     * @param triggerGroupName 触发器组名
     * @param cron             时间设置，参考quartz说明文档
     * @Description: 修改一个任务的触发时间
     */
    public static void modifyJobTime(String jobName, String jobGroupName, String triggerName, String triggerGroupName, String cron) {
        try {
            Scheduler sched = getScheduler();
            TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, triggerGroupName);
            CronTrigger trigger = (CronTrigger) sched.getTrigger(triggerKey);
            if (trigger == null) {
                return;
            }

            String oldTime = trigger.getCronExpression();
            if (!oldTime.equalsIgnoreCase(cron)) {
                /** 方式一 ：调用 rescheduleJob 开始 */
                // 触发器
                TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger();
                // 触发器名,触发器组
                triggerBuilder.withIdentity(triggerName, triggerGroupName);
                triggerBuilder.startNow();
                // 触发器时间设定
                triggerBuilder.withSchedule(CronScheduleBuilder.cronSchedule(cron));
                // 创建Trigger对象
                trigger = (CronTrigger) triggerBuilder.build();
                // 方式一 ：修改一个任务的触发时间
                sched.rescheduleJob(triggerKey, trigger);
                /** 方式一 ：调用 rescheduleJob 结束 */

                /** 方式二：先删除，然后在创建一个新的Job  */
                //JobDetail jobDetail = sched.getJobDetail(JobKey.jobKey(jobName, jobGroupName));
                //Class<? extends Job> jobClass = jobDetail.getJobClass();
                //removeJob(jobName, jobGroupName, triggerName, triggerGroupName);
                //addJob(jobName, jobGroupName, triggerName, triggerGroupName, jobClass, cron);
                /** 方式二 ：先删除，然后在创建一个新的Job */
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void pauseJob(Warning w) {
        pauseJob(w.getWarningName(), "jobGroup-" + w.getWarningClass());
    }

    /**
     * @param jobName
     * @param jobGroupName
     * @Description: 暂停任务
     */
    public static void pauseJob(String jobName, String jobGroupName) {
        try {
            Scheduler scheduler = getScheduler();
            scheduler.pauseJob(JobKey.jobKey(jobName, jobGroupName));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void resumeJob(Warning w) {
        resumeJob(w.getWarningName(), "jobGroup-" + w.getWarningClass());
    }

    /**
     * @param jobName
     * @param jobGroupName
     * @Description: 恢复任务
     */
    public static void resumeJob(String jobName, String jobGroupName) {
        try {
            Scheduler scheduler = getScheduler();
            scheduler.resumeJob(JobKey.jobKey(jobName, jobGroupName));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void removeJob(Warning w) {
        String jobName = w.getWarningName();
        String jobGroupName = "jobGroup-" + w.getWarningClass();
        String triggerName = "trigger-" + w.getWarningClass();
        String triggerGroupName = "triggerGroup-" + w.getWarningClass();
        removeJob(jobName, jobGroupName, triggerName, triggerGroupName);
    }

    /**
     * @param jobName
     * @param jobGroupName
     * @param triggerName
     * @param triggerGroupName
     * @Description: 移除一个任务
     */
    public static void removeJob(String jobName, String jobGroupName, String triggerName, String triggerGroupName) {
        try {
            Scheduler sched = getScheduler();
            TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, triggerGroupName);
            // 停止触发器
            sched.pauseTrigger(triggerKey);
            // 移除触发器
            sched.unscheduleJob(triggerKey);
            // 删除任务
            sched.deleteJob(JobKey.jobKey(jobName, jobGroupName));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @Description:启动所有定时任务
     */
    public static void startJobs() {
        try {
            Scheduler sched = getScheduler();
            sched.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * @Description:关闭所有定时任务
     */
    public static void shutdownJobs() {
        try {
            Scheduler sched = getScheduler();
            if (!sched.isShutdown()) {
                sched.shutdown();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}

