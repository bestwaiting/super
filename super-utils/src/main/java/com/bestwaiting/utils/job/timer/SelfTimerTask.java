package com.bestwaiting.utils.job.timer;

import com.bestwaiting.utils.date.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.TimerTask;

/**
 * Created by bestwaiting on 17/6/14.
 * <p>
 * Timer 的设计核心是一个 TaskList 和一个 TaskThread。Timer 将接收到的任务丢到自己的 TaskList 中，TaskList 按照 Task 的最初执行时间进行排序。
 * TimerThread 在创建 Timer 时会启动成为一个守护线程。这个线程会轮询所有任务，找到一个最近要执行的任务，然后休眠，当到达最近要执行任务的开始时间点，TimerThread 被唤醒并执行该任务。
 * 之后 TimerThread 更新最近一个要执行的任务，继续休眠。
 */
public class SelfTimerTask extends TimerTask {

    Logger logger = LoggerFactory.getLogger(SelfTimerTask.class);

    private String taskName;

    public SelfTimerTask(String taskName) {
        super();
        this.taskName = taskName;
    }

    @Override
    public void run() {
        // TODO 任务处理逻辑
        logger.debug(String.format("任务:[%s]-->执行时间:[%s]",taskName, DateUtils.convertLong2DateStr(scheduledExecutionTime())));
        System.out.println(String.format("任务:[%s]-->执行时间:[%s]",taskName, DateUtils.convertLong2DateStr(scheduledExecutionTime())));
    }
}
