package com.bestwaiting.utils.job.timer;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by bestwaiting on 17/6/14.
 */
public class JobTimerUtils {
    static Timer timer;

    static {
        timer = new Timer();
    }

    public static void startTask(TimerTask timerTask, long delay, long period) {
        timer.schedule(timerTask, delay, period);
    }
}
