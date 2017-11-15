package com.bestwaiting.utils.job.quartz;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;

/**
 * Created by bestwaiting on 17/6/14.
 */
public class QuartzUtils {

    public static void startSimpleTask() {

        try {
            SchedulerFactory schedulerFactory = new StdSchedulerFactory();
            Scheduler scheduler = schedulerFactory.getScheduler();

            JobDetail jobDetail = JobBuilder.newJob(QuartzTask.class).withIdentity("test", "group").build();

            SimpleTrigger simpleTrigger = TriggerBuilder.newTrigger()
                    .withIdentity("trigger", "trigger_group")
                    .startNow()
                    .withSchedule(SimpleScheduleBuilder.simpleSchedule().
                            withIntervalInSeconds(1).withRepeatCount(2))
                    .build();
            scheduler.scheduleJob(jobDetail, simpleTrigger);

            scheduler.start();
            while (true){
                if (scheduler.getTriggerState(new TriggerKey("trigger", "trigger_group"))==Trigger.TriggerState.NONE){
                    scheduler.shutdown();
                    break;
                }
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    public static void startCronTask() {

        try {
            SchedulerFactory schedulerFactory = new StdSchedulerFactory();
            Scheduler scheduler = schedulerFactory.getScheduler();

            JobDetail jobDetail = JobBuilder.newJob(QuartzTask.class).withIdentity("test", "group").build();

            CronTrigger cronTrigger=TriggerBuilder.newTrigger()
                    .withIdentity("trigger", "trigger_group")
                    .startNow()
                    .withSchedule(CronScheduleBuilder.cronSchedule("0/5 * * * * ?"))
                    .build();
            scheduler.scheduleJob(jobDetail, cronTrigger);

            scheduler.start();
            while (true){
                if (scheduler.getTriggerState(new TriggerKey("trigger", "trigger_group"))==Trigger.TriggerState.NONE){
                    scheduler.shutdown();
                    break;
                }
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
}
