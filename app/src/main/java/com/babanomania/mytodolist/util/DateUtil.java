package com.babanomania.mytodolist.util;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Shouvik on 31/08/2017.
 */

public class DateUtil {

    public static Date getTodaysDate(){
        return Calendar.getInstance().getTime();
    }

    public static Date getThisWeekendDate(){
        Calendar date = Calendar.getInstance();
        int diff = Calendar.SATURDAY - date.get(Calendar.DAY_OF_WEEK);
        if (!(diff > 0)) {
            diff += 7;
        }
        date.add(Calendar.DAY_OF_MONTH, diff);
        return date.getTime();
    }

    public static Date getThisMonthEndDate(){
        Calendar date = Calendar.getInstance();
        date.set(Calendar.DAY_OF_MONTH, date.getActualMaximum(Calendar.DAY_OF_MONTH));
        return date.getTime();
    }
}
