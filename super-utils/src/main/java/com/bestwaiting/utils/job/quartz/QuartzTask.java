package com.bestwaiting.utils.job.quartz;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Created by bestwaiting on 17/6/14.
 */
@Slf4j
public class QuartzTask implements Job {
    public QuartzTask(){}
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println(context.getJobDetail().getKey().getName());
log.debug("dddddddddd");
    }
}
