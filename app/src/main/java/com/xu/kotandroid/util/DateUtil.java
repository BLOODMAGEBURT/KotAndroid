package com.xu.kotandroid.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @Author Xu
 * Date：2022/11/18 17:31
 * Description：
 */
public class DateUtil {

    /**
     * 英文全称  如：20101201
     */
    public static String FORMAT_YYMD = "yyyyMMdd";


    public static long dayStartTime(long timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(timestamp));
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTimeInMillis();
    }


    public static long addDay(long timestamp, int count) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(timestamp));

        calendar.add(Calendar.DATE, count);


        return calendar.getTimeInMillis();
    }


    public static String date2Str(Date d, String format) {
        if (d == null || format == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String s = sdf.format(d);
        return s;
    }


}
