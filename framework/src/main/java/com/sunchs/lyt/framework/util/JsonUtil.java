package com.sunchs.lyt.framework.util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

public class JsonUtil {

    private static Gson gson = new Gson();

    public static String toJson(Object src) {
       return gson.toJson(src);
    }

    public static <T> T toObject(String json, Class<T> classOfT) {
        try {
            return gson.fromJson(json, classOfT);
        } catch (JsonSyntaxException e) {
            return null;
        }
    }

    public static JsonObject toJsonObject(Object src) {
        String json = "";
        if (src instanceof String) {
            json = (String) src;
        } else {
            json = toJson(src);
        }
        return new JsonParser().parse(json).getAsJsonObject();
    }

}
