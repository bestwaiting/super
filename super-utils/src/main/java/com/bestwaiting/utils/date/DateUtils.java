package com.bestwaiting.utils.date;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by bestwaiting on 17/6/14.
 */
public class DateUtils {
    public static long convertDate2Long(Date date) {

        return date.getTime();   //得到秒数，Date类型的getTime()返回毫秒数
    }

    public static String convertLong2DateStr(long timeLong) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date dt = new Date(timeLong);
        return sdf.format(dt);
    }
}
