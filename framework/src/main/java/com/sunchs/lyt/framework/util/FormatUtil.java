package com.sunchs.lyt.framework.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FormatUtil {

    public static String dateTime(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(date);
    }
}
