package com.sunchs.lyt.framework.util;

import org.apache.commons.beanutils.PropertyUtils;

import java.lang.reflect.InvocationTargetException;

public class ObjectUtil {

    public static <T> T copy(Object old, Class<T> clazz) {
        T t = null;
        try {
            t = clazz.newInstance();
            System.out.println("传后的对象："+old);
            PropertyUtils.copyProperties(t, old);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return t;
    }
}
