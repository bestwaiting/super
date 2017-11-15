package com.bestwaiting.utils.job.scheduledexecutor;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by bestwaiting on 17/6/14.
 */
public class ScheduledExecutorUtils {
    static ScheduledExecutorService service;

    static {
        service = Executors.newScheduledThreadPool(10);
    }

    public static void startRateTask(Runnable runnable, long initDelay, long period) {
        service.scheduleAtFixedRate(runnable, initDelay, period, TimeUnit.SECONDS);
    }

    public static void startDelayTask(Runnable runnable, long initDelay, long delay) {
        service.scheduleWithFixedDelay(runnable, initDelay, delay, TimeUnit.SECONDS);
    }
}
