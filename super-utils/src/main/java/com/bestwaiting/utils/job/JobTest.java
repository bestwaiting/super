package com.bestwaiting.utils.job;

import com.bestwaiting.utils.job.quartz.QuartzUtils;
import org.quartz.SchedulerException;

/**
 * Created by bestwaiting on 17/6/14.
 */
public class JobTest {
    public static void main(String[] args) throws SchedulerException {
//        JobTimerUtils.startTask(new SelfTimerTask("test1"),1000,1000);
//        ScheduledExecutorUtils.startDelayTask(new ScheduledExecutorTask("test"), 1, 1);
        QuartzUtils.startSimpleTask();

    }
}
