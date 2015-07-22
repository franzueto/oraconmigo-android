package org.guateora.oraconmigo.utils;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by franz on 7/21/2015.
 */
public class Utils {

    public static final String FACEBOOK_LARGE_PICTURE = "https://graph.facebook.com/%s/picture?type=large";
    public static final String FACEBOOK_MEDIUM_PICTURE = "https://graph.facebook.com/%s/picture?type=normal";
    public static final String FACEBOOK_SMALL_PICTURE = "https://graph.facebook.com/%s/picture?type=small";
    public static final String FACEBOOK_SQUARE_PICTURE = "https://graph.facebook.com/%s/picture?type=square";

    public static Calendar getTodayInit(){
        // today
        Calendar today_init = new GregorianCalendar();
        // reset hour, minutes, seconds and millis
        today_init.set(Calendar.HOUR_OF_DAY, 0);
        today_init.set(Calendar.MINUTE, 0);
        today_init.set(Calendar.SECOND, 0);
        today_init.set(Calendar.MILLISECOND, 0);

        return today_init;
    }

    public static Calendar getTodayEnd(){
        // today
        Calendar today_end = new GregorianCalendar();
        // reset hour, minutes, seconds and millis
        today_end.set(Calendar.HOUR_OF_DAY, 23);
        today_end.set(Calendar.MINUTE, 59);
        today_end.set(Calendar.SECOND, 59);
        today_end.set(Calendar.MILLISECOND, 0);

        return  today_end;
    }

}
