package com.bestwaiting.utils.job.scheduledexecutor;

import com.bestwaiting.utils.date.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * Created by bestwaiting on 17/6/14.
 */
public class ScheduledExecutorTask implements Runnable {
    Logger logger = LoggerFactory.getLogger(ScheduledExecutorTask.class);

    private String taskName;

    public ScheduledExecutorTask(String taskName) {
        this.taskName = taskName;
    }

    @Override
    public void run() {
        // TODO 任务处理逻辑
        logger.debug(String.format("任务:[%s]-->执行时间:[%s]", taskName, DateUtils.convertLong2DateStr(new Date().getTime())));
        System.out.println(String.format("任务:[%s]-->执行时间:[%s]", taskName, DateUtils.convertLong2DateStr(new Date().getTime())));

    }
}
