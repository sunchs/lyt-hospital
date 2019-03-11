package com.sunchs.lyt.framework.util;

public class NumberUtil {

    public static boolean isZero(Object value) {
        if (value == null) {
            return true;
        }
        if (value instanceof Integer || value instanceof Long) {
            if (value.equals(0)) {
                return true;
            }
        }
        return false;
    }

}
